package org.instituteatri.deep.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dto.request.ChatRequestDTO;
import org.instituteatri.deep.dto.response.ChatResponseDTO;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.model.Occurrence;
import org.instituteatri.deep.model.Role;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${spring.ai.ollama.chat.options.model}")
    private String model;

    @Value("${gemini.key}")
    String GOOGLE_API_KEY;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);
    private final OccurrenceService occurrenceService;
    private final MessageService messageService;
    private final ModelMapper modelMapper;

    public ChatResponse generate(ChatRequestDTO request, String occurrenceId) {
        OccurrenceResponseDTO occurrenceResponse = occurrenceService.getById(occurrenceId);
        Occurrence occurrence = modelMapper.map(occurrenceResponse, Occurrence.class);
        List<Message> messages = new ArrayList<>();
        occurrence.getMessages().forEach(x -> {
            Message msg = Message.builder(Message.Role.valueOf(x.getRole().toString())).withContent(x.getContent()).build();
            messages.add(msg);
        });
        Message msg = Message.builder(Message.Role.USER).withContent(request.getMessage()).build();
        messages.add(msg);
        LOGGER.info("Inserting {} to the database", msg);
        messageService.saveOllama(msg, occurrenceId);
        OllamaApi.ChatRequest build =
                OllamaApi.ChatRequest.builder(model).withMessages(messages).build();
        ChatResponse response = new OllamaApi().chat(build);
        messages.add(Message.builder(Message.Role.ASSISTANT)
                .withContent(response.message().content()).build());
        LOGGER.info("Inserting {} to the database", msg);
        messageService.saveOllama(response.message(), occurrenceId);
        return response;
    }

    public ChatResponseDTO generateGemini(ChatRequestDTO request, String occurrenceId) {
        OccurrenceResponseDTO occurrenceResponse = occurrenceService.getById(occurrenceId);
        Occurrence occurrence = modelMapper.map(occurrenceResponse, Occurrence.class);
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + GOOGLE_API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Map<String, Object>> contents = new ArrayList<>();
        occurrence.getMessages().forEach(x -> {
            contents.add(Map.of("role", x.getRole().toString(), "parts", List.of(Map.of("text", x.getContent()))));
        });
        contents.add(Map.of("role", "user", "parts", List.of(Map.of("text", request.getMessage()))));

        org.instituteatri.deep.model.Message msgUser = org.instituteatri.deep.model.Message.builder()
                .role(Role.USER)
                .occurrence(occurrence)
                .content(request.getMessage())
                .build();
        messageService.saveGemini(msgUser);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", contents);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        String text = "";
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            // Parse to json and get text
            text = getString(responseBody, text);
            org.instituteatri.deep.model.Message msgAI = org.instituteatri.deep.model.Message.builder()
                    .role(Role.MODEL)
                    .occurrence(occurrence)
                    .content(text)
                    .build();
            messageService.saveGemini(msgAI);
        } else {
            System.out.println("Request failed with status code: " + response.getStatusCode());
        }
        return ChatResponseDTO.builder().text(text).build();
    }

    private static String getString(String responseBody, String text) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode candidates = jsonNode.get("candidates");
            for (JsonNode candidate : candidates) {
                JsonNode content = candidate.get("content");
                JsonNode parts = content.get("parts");
                for (JsonNode part : parts) {
                    text = part.get("text").asText();
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return text;
    }
}

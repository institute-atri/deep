package org.instituteatri.deep.service;

import java.util.ArrayList;
import java.util.List;

import org.instituteatri.deep.dto.request.ChatRequestDTO;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.model.Occurrence;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${spring.ai.ollama.chat.options.model}")
    private String model;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    private final OccurrenceService occurrenceService;
    private final MessageService messageService;
    private final ModelMapper modelMapper;

    public OllamaApi.ChatResponse generate(ChatRequestDTO request, String occurrenceId) {
        OccurrenceResponseDTO occurrenceResponse = occurrenceService.getById(occurrenceId);
        Occurrence occurrence = modelMapper.map(occurrenceResponse, Occurrence.class);
        List<Message> messages = new ArrayList<>();
        occurrence.getMessages().forEach(x -> {
            Message msg = Message.builder(x.getRole()).withContent(x.getContent()).build();
            messages.add(msg);
        });
        Message msg = Message.builder(Message.Role.USER).withContent(request.getMessage()).build();
        messages.add(msg);
        LOGGER.info("Inserting {} to the database", msg);
        messageService.save(msg, occurrenceId);
        OllamaApi.ChatRequest build =
                OllamaApi.ChatRequest.builder(model).withMessages(messages).build();
        ChatResponse response = new OllamaApi().chat(build);
        messages.add(Message.builder(Message.Role.ASSISTANT)
                .withContent(response.message().content()).build());
        LOGGER.info("Inserting {} to the database", msg);
        messageService.save(response.message(), occurrenceId);
        return response;
    }
    public void deleteChatMessage(String occurrenceId) {
        LOGGER.info("Deleting chat messages for occurrence ID: {}", occurrenceId);
        messageService.delete(occurrenceId);
    }
}

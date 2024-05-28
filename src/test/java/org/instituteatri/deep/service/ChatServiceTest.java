package org.instituteatri.deep.service;

import org.instituteatri.deep.dto.request.ChatRequestDTO;
import org.instituteatri.deep.dto.response.ChatResponseDTO;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.model.Occurrence;
import org.instituteatri.deep.model.ActorRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private OccurrenceService occurrenceService;

    @Mock
    private MessageService messageService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ChatService chatService;

    @Mock
    private RestTemplate template;

    @Test
    void generateGemini_shouldGenerateChatResponseAndSaveMessages() {
        OccurrenceResponseDTO occurrenceResponseDTO = OccurrenceResponseDTO.builder()
                .id("1")
                .name("Roubo")
                .description("Roubo na Avenida Y")
                .build();

        org.instituteatri.deep.model.Message message = org.instituteatri.deep.model.Message.builder()
                .actorRole(ActorRole.USER)
                .content("Initial message")
                .build();

        List<org.instituteatri.deep.model.Message> messages = new ArrayList<>();
        messages.add(message);
        Occurrence occurrence = Occurrence.builder()
                .id("1")
                .name("Roubo")
                .description("Roubo na Avenida Y")
                .messages(messages)
                .build();

        when(occurrenceService.getById("1")).thenReturn(occurrenceResponseDTO);
        when(modelMapper.map(any(OccurrenceResponseDTO.class), eq(Occurrence.class))).thenReturn(occurrence);

        ChatRequestDTO request = ChatRequestDTO.builder().message("Hello").build();

        String apiResponseBody = "{\"candidates\": [{\"content\": {\"parts\": [{\"text\": \"Hi there!\"}]}}]}";
        ResponseEntity<String> apiResponse = ResponseEntity.ok(apiResponseBody);

        when(template.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(apiResponse);

        ChatResponseDTO chatResponse = chatService.generateMessageResponseFromGemini(request, "1");

        verify(occurrenceService).getById("1");
        verify(modelMapper).map(occurrenceResponseDTO, Occurrence.class);
        verify(messageService, times(2)).saveMessageFromGemini(any(org.instituteatri.deep.model.Message.class));

        assertEquals("Hi there!", chatResponse.getText());
    }

    @Test
    void generateOllama_shouldGenerateChatResponseAndSaveMessages() {
        OccurrenceResponseDTO occurrenceResponseDTO = OccurrenceResponseDTO.builder()
                .id("1")
                .name("Roubo")
                .description("Roubo na Avenida Y")
                .build();

        org.instituteatri.deep.model.Message message = org.instituteatri.deep.model.Message.builder()
                .actorRole(ActorRole.USER)
                .content("Initial message")
                .build();

        List<org.instituteatri.deep.model.Message> messages = new ArrayList<>();
        messages.add(message);
        Occurrence occurrence = Occurrence.builder()
                .id("1")
                .name("Roubo")
                .description("Roubo na Avenida Y")
                .messages(messages)
                .build();

        when(occurrenceService.getById("1")).thenReturn(occurrenceResponseDTO);
        when(modelMapper.map(any(OccurrenceResponseDTO.class), eq(Occurrence.class))).thenReturn(occurrence);

        ChatRequestDTO mockChatRequestDTO = ChatRequestDTO.builder()
                .message("Hello")
                .build();

        OllamaApi.ChatResponse chatResponse = chatService.generateMessageResponseFromOllama(mockChatRequestDTO, "1");

        verify(occurrenceService).getById("1");
        verify(modelMapper).map(occurrenceResponseDTO, Occurrence.class);
        verify(messageService, times(2)).saveMessageFromOllama(any(), eq("1"));

        assertThat(chatResponse.message().content()).isNotEmpty();
    }
}
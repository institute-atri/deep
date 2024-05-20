package org.instituteatri.deep.service;

import org.instituteatri.deep.dto.request.ChatRequestDTO;
import org.instituteatri.deep.dto.response.ChatResponseDTO;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.model.Message;
import org.instituteatri.deep.model.Occurrence;
import org.instituteatri.deep.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    private final TestRestTemplate template = new TestRestTemplate();

    @Mock
    private OccurrenceService occurrenceService;

    @Mock
    private MessageService messageService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ChatService chatService;


    @Test
    void generate_shouldGenerateChatResponseAndSaveMessages() {
        OccurrenceResponseDTO occurrenceResponseDTO = OccurrenceResponseDTO.builder()
                .id("1")
                .name("Roubo")
                .description("Roubo na Avenida Y")
                .build();

        Message message = Message.builder()
                .role(Role.USER)
                .content("Initial message")
                .build();

        List<Message> messages = new ArrayList<>();
        messages.add(message);
        Occurrence occurrence = Occurrence.builder()
                .id("1")
                .name("Roubo")
                .description("Roubo na Avenida Y")
                .messages(messages)
                .build();

        when(occurrenceService.getById("1")).thenReturn(occurrenceResponseDTO);
        when(modelMapper.map(Mockito.any(OccurrenceResponseDTO.class), Mockito.eq(Occurrence.class))).thenReturn(occurrence);

        ChatRequestDTO request = ChatRequestDTO.builder().message("Hello").build();

        String apiResponseBody = "{\"contents\": [{\"parts\": [{\"text\": \"Hi there!\"}]}]}";
        ResponseEntity<String> apiResponse = ResponseEntity.ok(apiResponseBody);

        when(template.exchange(
                        anyString(),
                        HttpMethod.POST,
                        any(HttpEntity.class),
                        eq(String.class)
                )
        ).thenReturn(apiResponse);

        ChatResponseDTO chatResponse = chatService.generateGemini(request, "1");

        verify(occurrenceService).getById("1");
        verify(modelMapper).map(occurrenceResponseDTO, Occurrence.class);
        verify(messageService).saveGemini(any(org.instituteatri.deep.model.Message.class));
//        verify(template).exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));

        assertEquals("Hi there!", chatResponse.getText());
    }
}
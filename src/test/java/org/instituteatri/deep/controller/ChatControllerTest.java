package org.instituteatri.deep.controller;

import org.instituteatri.deep.dto.request.ChatRequestDTO;
import org.instituteatri.deep.dto.response.ChatResponseDTO;
import org.instituteatri.deep.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatController chatController;

    @Test
    @DisplayName("Generate Gemini test return a ChatResponse when request is valid")
    public void generateGemini_WhenChatRequest_ThenReturnChatResponse() {
        ChatResponseDTO expectedResponse = ChatResponseDTO.builder().text("Ok").build();

        when(chatService.generateGemini(any(ChatRequestDTO.class), anyString())).thenReturn(expectedResponse);
        ChatRequestDTO request = new ChatRequestDTO();
        String occurrenceId = "1";

        ResponseEntity<ChatResponseDTO> response = chatController.generateGemini(request, occurrenceId);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        assertThat(response.getBody().getText()).isEqualTo("Ok");
    }

}
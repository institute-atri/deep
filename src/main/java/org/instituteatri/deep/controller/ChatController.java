package org.instituteatri.deep.controller;

import java.util.List;

import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    @Value("${spring.ai.ollama.chat.options.model}")
    private String model;

    @GetMapping("/ai/generate/test")
    public OllamaApi.ChatResponse generate(
            @RequestParam(value = "message") String message) {
        Message msg1 = Message.builder(Message.Role.USER).withContent("Ola meu nome é Nicholas").build();
        Message msg2 = Message.builder(Message.Role.ASSISTANT)
                .withContent("Olá Nicholas! Prazer em conhecê-lo! Como posso ajudá-lo hoje?").build();
        Message msg3 = Message.builder(Message.Role.USER).withContent(message).build();
        OllamaApi.ChatRequest build = OllamaApi.ChatRequest.builder(model).withMessages(List.of(msg1, msg2, msg3))
                .build();
        return new OllamaApi().chat(build);
    }
}
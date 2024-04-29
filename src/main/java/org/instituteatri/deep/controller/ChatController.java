package org.instituteatri.deep.controller;

import java.util.Map;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final OllamaChatClient chatClient;

    @GetMapping("/ai/generate/test")
    public Map<String, String> generate(
            @RequestParam(value = "message", defaultValue = "Alô, quem ta ai?") String message) {
        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping("/ai/generateStream/test")
    public Flux<ChatResponse> generateStream(
            @RequestParam(value = "message", defaultValue = "Alô, quem ta ai?") String message) {
        Prompt prompt = new Prompt(message);
        return chatClient.stream(prompt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id) {

        return ResponseEntity.noContent().build();
    }
}
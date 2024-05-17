package org.instituteatri.deep.controller;

import org.instituteatri.deep.dto.request.ChatRequestDTO;
import org.instituteatri.deep.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @PostMapping("/ai/generate/{occurrenceId}")
    public ResponseEntity<OllamaApi.ChatResponse> generate(@RequestBody ChatRequestDTO request,
            @PathVariable String occurrenceId) {
        LOGGER.info("Received request to generate a chat response");
        return ResponseEntity.ok(service.generate(request, occurrenceId));
    }
}

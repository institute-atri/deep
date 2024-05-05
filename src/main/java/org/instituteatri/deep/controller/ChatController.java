package org.instituteatri.deep.controller;

import org.instituteatri.deep.dto.request.ChatRequestDTO;
import org.instituteatri.deep.service.ChatService;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService service;

    @PostMapping("/ai/generate/{occurrenceId}")
    public ResponseEntity<OllamaApi.ChatResponse> generate(@RequestBody ChatRequestDTO request,
            @PathVariable String occurrenceId) {
        return ResponseEntity.ok(service.generate(request, occurrenceId));
    }
}

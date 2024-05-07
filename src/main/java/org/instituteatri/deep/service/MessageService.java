package org.instituteatri.deep.service;

import java.time.Instant;

import org.instituteatri.deep.controller.ChatController;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.model.Occurrence;
import org.instituteatri.deep.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository repository;
    private final OccurrenceService occurrenceService;
    private final ModelMapper modelMapper;


    public void save(Message request, String occurrenceId) {
        org.instituteatri.deep.model.Message msg = org.instituteatri.deep.model.Message.builder()
                .content(request.content()).role(request.role()).build();
        OccurrenceResponseDTO responseDTO = occurrenceService.getById(occurrenceId);
        Occurrence occurrence = modelMapper.map(responseDTO, Occurrence.class);
        msg.setOccurrence(occurrence);
        msg.setCreatedAt(Instant.now());
        repository.save(msg);
    }
}

package org.instituteatri.deep.service;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.model.Occurrence;
import org.instituteatri.deep.model.ActorRole;
import org.instituteatri.deep.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository repository;
    private final OccurrenceService occurrenceService;
    private final ModelMapper modelMapper;

    public void saveMessageFromOllama(Message request, String occurrenceId) {
        org.instituteatri.deep.model.Message msg = org.instituteatri.deep.model.Message.builder()
                .content(request.content()).actorRole(ActorRole.valueOf(request.role().toString())).build();
        OccurrenceResponseDTO responseDTO = occurrenceService.getById(occurrenceId);
        Occurrence occurrence = modelMapper.map(responseDTO, Occurrence.class);
        msg.setOccurrence(occurrence);
        msg.setCreatedAt(Instant.now());
        LOGGER.info("Inserting {} to the database", msg);
        repository.save(msg);
    }

    public void saveMessageFromGemini(org.instituteatri.deep.model.Message msg){
        msg.setCreatedAt(Instant.now());
        LOGGER.info("Inserting message to the database");
        repository.save(msg);
    }
}

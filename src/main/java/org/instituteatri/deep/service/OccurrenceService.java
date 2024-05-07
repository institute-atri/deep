package org.instituteatri.deep.service;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dto.request.OccurrenceRequestDTO;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.exception.BadRequestException;
import org.instituteatri.deep.model.Occurrence;
import org.instituteatri.deep.repository.OccurrenceRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OccurrenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OccurrenceService.class);
    private final OccurrenceRepository repository;
    private final ModelMapper modelMapper;

    public List<OccurrenceResponseDTO> getAll() {
        List<Occurrence> all = repository.findAll();
        List<OccurrenceResponseDTO> response = new ArrayList<>();
        all.forEach(x -> response.add(modelMapper.map(x, OccurrenceResponseDTO.class)));
        return response;
    }

    public OccurrenceResponseDTO getById(String id) {
        Occurrence occurrence = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Occurrence not found!"));
        return modelMapper.map(occurrence, OccurrenceResponseDTO.class);
    }

    public OccurrenceResponseDTO save(OccurrenceRequestDTO request) {
        Occurrence occurrence = modelMapper.map(request, Occurrence.class);
        occurrence.setCreatedAt(Instant.now());
        LOGGER.info("Inserting {} to the database", occurrence);
        Occurrence occurrenceSaved = repository.save(occurrence);
        return modelMapper.map(occurrenceSaved, OccurrenceResponseDTO.class);
    }

    public void delete(String id) {
        LOGGER.info("Deleting ID: {} from the database", id);
        repository.deleteById(id);
    }
}
package org.instituteatri.deep.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.instituteatri.deep.dto.request.OccurrenceRequestDTO;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.exception.BadRequestException;
import org.instituteatri.deep.model.Occurrence;
import org.instituteatri.deep.repository.OccurrenceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OccurrenceService {

    private final OccurrenceRepository repository;
    private final ModelMapper modelMapper;

    public List<OccurrenceResponseDTO> getAll() {
        List<Occurrence> all = repository.findAll();
        List<OccurrenceResponseDTO> response = new ArrayList<>();
        all.forEach(x -> response.add(modelMapper.map(response, OccurrenceResponseDTO.class)));
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
        Occurrence occurrenceSaved = repository.save(occurrence);
        return modelMapper.map(occurrenceSaved, OccurrenceResponseDTO.class);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}

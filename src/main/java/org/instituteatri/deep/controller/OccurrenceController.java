package org.instituteatri.deep.controller;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dto.request.OccurrenceRequestDTO;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.service.OccurrenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/occurrence")
@RequiredArgsConstructor
public class OccurrenceController {

    private final OccurrenceService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(OccurrenceController.class);

    @GetMapping
    public ResponseEntity<List<OccurrenceResponseDTO>> getAll() {
        List<OccurrenceResponseDTO> all = service.getAll();
        LOGGER.info("Received request to get all occurrences");
        return ResponseEntity.ok().body(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OccurrenceResponseDTO> getById(@PathVariable String id) {
        OccurrenceResponseDTO responseDTO = service.getById(id);
        LOGGER.info("Received request to get occurrence by ID");
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<OccurrenceResponseDTO> create(@RequestBody OccurrenceRequestDTO request) {
        OccurrenceResponseDTO responseDTO = service.save(request);
        LOGGER.info("Received request to create a new occurrence");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        LOGGER.info("Received request to delete an occurrence");
        return ResponseEntity.noContent().build();
    }
}

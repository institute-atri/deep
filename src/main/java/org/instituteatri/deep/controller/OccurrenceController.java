package org.instituteatri.deep.controller;

import java.util.List;
import org.instituteatri.deep.dto.request.OccurrenceRequestDTO;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.service.OccurrenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/occurrence")
@RequiredArgsConstructor
public class OccurrenceController {

    private final OccurrenceService service;

    @GetMapping
    public ResponseEntity<List<OccurrenceResponseDTO>> getAll() {
        List<OccurrenceResponseDTO> all = service.getAll();
        return ResponseEntity.ok().body(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OccurrenceResponseDTO> getById(@PathVariable String id) {
        OccurrenceResponseDTO responseDTO = service.getById(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<OccurrenceResponseDTO> create(@RequestBody OccurrenceRequestDTO request) {
        OccurrenceResponseDTO responseDTO = service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
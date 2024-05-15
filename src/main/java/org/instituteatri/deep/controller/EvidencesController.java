package org.instituteatri.deep.controller;


import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.service.EvidencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.instituteatri.deep.dto.response.EvidencesResponseDTO;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/evidences")
@RequiredArgsConstructor
public class EvidencesController {

    private final EvidencesService evidencesService;
    private static final Logger logger = LoggerFactory.getLogger(EvidencesController.class);


    @PostMapping("/evidences")

public ResponseEntity<EvidencesResponseDTO> createEvidence(@RequestBody EvidencesResponseDTO request) {
EvidencesResponseDTO response = evidencesService.createEvidences(request);
        logger.info("Creating evidence with ID {}", request.getId());
        return ResponseEntity.ok(evidencesService.createEvidences(request));
    }

    @GetMapping("/evidences/{id}")
    public ResponseEntity<EvidencesResponseDTO> getEvidenceById(@PathVariable String id) {
        logger.info("Getting evidence by ID {}", id);
        return ResponseEntity.ok(evidencesService.getEvidenceById(id));
    }

    @DeleteMapping("/evidences/{id}")
    public ResponseEntity<Void> deleteEvidenceById(@PathVariable String id) {
        evidencesService.deleteEvidenceById(id);
        logger.info("Deleting evidence by ID {}", id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/evidences/{id}")
    public ResponseEntity<EvidencesResponseDTO> updateEvidenceById(@RequestBody EvidencesResponseDTO request) {
        evidencesService.updateEvidencesById(request);
        logger.info("Updating evidence by ID {}", request.getId());
        return ResponseEntity.ok(evidencesService.updateEvidencesById(request));
    }

    @GetMapping("/evidences/all")
    public ResponseEntity<List<EvidencesResponseDTO>> getAllEvidences() {
        logger.info("Getting all evidences");
        return ResponseEntity.ok(evidencesService.getAllEvidences());
    }



}


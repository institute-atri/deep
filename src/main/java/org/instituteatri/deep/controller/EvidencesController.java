package org.instituteatri.deep.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.service.EvidencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.instituteatri.deep.dto.response.EvidencesResponseDTO;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/v1/evidences")
@RequiredArgsConstructor
@Tag(name = "Evidences controller" , description = "This controller is responsible for managing evidences.")
public class EvidencesController {

    private final EvidencesService evidencesService;
    private static final Logger logger = LoggerFactory.getLogger(EvidencesController.class);

@Operation(
        method = "POST",
        summary = "Create an evidence.",
        description = "This endpoint is used to create a new evidence. It returns the unique identifier of the created evidence."
)
@ApiResponse(responseCode = "200", description = "Evidence created.")
@ApiResponse(responseCode = "500", description = "Internal server error.")
@ApiResponse(responseCode = "400", description = "Bad request.")
    @PostMapping("/create-evidence")
public ResponseEntity<EvidencesResponseDTO> createEvidence(@RequestBody EvidencesResponseDTO request) {
EvidencesResponseDTO response = evidencesService.createEvidences(request);
        logger.info("Creating evidence with ID {}", request.getId());
        return ResponseEntity.ok(evidencesService.createEvidences(request));
    }
@Operation(
            method = "GET",
            summary = "Find an evidence by ID.",
            description = "This endpoint is used to return an evidence by ID."
    )
@ApiResponse(responseCode = "200", description = "Evidence found.")
@ApiResponse(responseCode = "404", description = "Evidence not found.")
@ApiResponse(responseCode = "500", description = "Internal server error.")
    @GetMapping("/find-evidence/{id}")
    public ResponseEntity<EvidencesResponseDTO> getEvidenceById(@PathVariable String id) {
        logger.info("Getting evidence by ID {}", id);
        return ResponseEntity.ok(evidencesService.getEvidenceById(id));
    }

@Operation(
            method = "DELETE",
            summary = "Delete an evidence by ID.",
            description = "This endpoint is used to delete an evidence by ID."
    )
@ApiResponse(responseCode = "200", description = "Evidence deleted.")
@ApiResponse(responseCode = "404", description = "Evidence not found.")
@ApiResponse(responseCode = "500", description = "Internal server error.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvidenceById(@PathVariable String id) {
        evidencesService.deleteEvidenceById(id);
        logger.info("Deleting evidence by ID {}", id);
        return ResponseEntity.ok().build();
    }

@Operation(
            method = "PUT",
            summary = "Update an evidence by ID.",
            description = "This endpoint is used to update an evidence by ID."
    )
@ApiResponse(responseCode = "200", description = "Evidence updated.")
@ApiResponse(responseCode = "404", description = "Evidence not found.")
@ApiResponse(responseCode = "500", description = "Internal server error.")
    @PutMapping("/update-evidence/{id}")
    public ResponseEntity<EvidencesResponseDTO> updateEvidenceById(@RequestBody EvidencesResponseDTO request) {
        evidencesService.updateEvidencesById(request);
        logger.info("Updating evidence by ID {}", request.getId());
        return ResponseEntity.ok(evidencesService.updateEvidencesById(request));
    }

@Operation(
        method = "GET",
        summary = "Find evidences by occurrence ID.",
        description = "This endpoint is used to return evidences by occurrence ID."
)
@ApiResponse(responseCode = "200", description = "Evidences found.")
@ApiResponse(responseCode = "404", description = "Evidences not found.")
@ApiResponse(responseCode = "500", description = "Internal server error.")
    @GetMapping("/find-evidence/{occurrenceId}")
    public ResponseEntity<List<EvidencesResponseDTO>> getEvidencesByOccurrenceId(@PathVariable String occurrenceId) {
        logger.info("Getting evidences by occurrence ID {}", occurrenceId);
        List<EvidencesResponseDTO> evidences = evidencesService.getEvidenceByOccurrenceId(occurrenceId);
        return ResponseEntity.ok(evidences);

    }



}


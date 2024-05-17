package org.instituteatri.deep.controller;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.service.DocumentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.instituteatri.deep.dto.response.DocumentsResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentsController {

    private final DocumentsService documentsService;
    private static final Logger logger = LoggerFactory.getLogger(DocumentsController.class);

    @PostMapping("/documents")
    ResponseEntity<DocumentsResponseDTO> createDocument(@RequestBody DocumentsResponseDTO request) {
        DocumentsResponseDTO response = documentsService.createDocuments(request);
        logger.info("Creating document with ID {}", request.getId());
        return ResponseEntity.ok(documentsService.createDocuments(request));
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<DocumentsResponseDTO> getDocumentById(@PathVariable String id) {
        logger.info("Getting document by ID {}", id);
        return ResponseEntity.ok(documentsService.getDocumentsById(id));
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable String id) {
        documentsService.deleteDocumentById(id);
        logger.info("Deleting document by ID {}", id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/documents/{id}")
    public ResponseEntity<DocumentsResponseDTO> updateDocumentById(@RequestBody DocumentsResponseDTO request) {
        documentsService.updateDocumentsById(request);
        logger.info("Updating document by ID {}", request.getId());
        return ResponseEntity.ok(documentsService.updateDocumentsById(request));
    }

    @GetMapping("/cases/{caseId}")
    public ResponseEntity<List<DocumentsResponseDTO>> getDocumentsByCaseId(@PathVariable String caseId) {
        logger.info("Getting documents by case ID {}", caseId);
        List<DocumentsResponseDTO> documents = documentsService.getDocumentsByCaseId(caseId);
        return ResponseEntity.ok(documents);

    }
}



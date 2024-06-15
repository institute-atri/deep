package org.instituteatri.deep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.service.DocumentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.instituteatri.deep.dto.response.DocumentsResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/documents")
@RequiredArgsConstructor
@Tag(name = "Documents controller" , description = "This controller is responsible for managing documents.")
public class DocumentsController {

    private final DocumentsService documentsService;
    private static final Logger logger = LoggerFactory.getLogger(DocumentsController.class);

    @Operation(
            method = "POST",
            summary = "Create a document.",
            description = "This endpoint is used to create a new document. It returns the unique identifier of the created document.")
    @ApiResponse(responseCode = "200", description = "Document created.")
    @ApiResponse(responseCode = "500", description = "Internal server error.")
    @ApiResponse(responseCode = "400", description = "Bad request.")
    @PostMapping("/create-document")
    ResponseEntity<DocumentsResponseDTO> createDocument(@RequestBody DocumentsResponseDTO request) {
        DocumentsResponseDTO response = documentsService.createDocuments(request);
        logger.info("Creating document with ID {}", request.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            method = "GET",
            summary = "Find a document by ID.",
            description = "This endpoint is used to return a document by ID.")
    @ApiResponse(responseCode = "200", description = "Document found.")
    @ApiResponse(responseCode = "404", description = "Document not found.")
    @ApiResponse(responseCode = "500", description = "Internal server error.")
    @GetMapping("/find-document/{id}")
    public ResponseEntity<DocumentsResponseDTO> getDocumentById(@PathVariable String id) {
        logger.info("Getting document by ID {}", id);
        return ResponseEntity.ok(documentsService.getDocumentsById(id));
    }

    @Operation(
            method = "DELETE",
            summary = "Delete a document by ID.",
            description = "This endpoint is used to delete a document by ID."
    )
    @ApiResponse(responseCode = "200", description = "Document deleted.")
    @ApiResponse(responseCode = "404", description = "Document not found.")
    @ApiResponse(responseCode = "500", description = "Internal server error.")
    @DeleteMapping("/delete-document/{id}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable String id) {
        documentsService.deleteDocumentById(id);
        logger.info("Deleting document by ID {}", id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            method = "PUT",
            summary = "Update a document by ID.",
            description = "This endpoint is used to update a document by ID."
    )
    @ApiResponse(responseCode = "200", description = "Document updated.")
    @ApiResponse(responseCode = "404", description = "Document not found.")
    @ApiResponse(responseCode = "500", description = "Internal server error.")
    @PutMapping("/update-document/{id}")
    public ResponseEntity<DocumentsResponseDTO> updateDocumentById(@RequestBody DocumentsResponseDTO request) {
        documentsService.updateDocumentsById(request);
        logger.info("Updating document by ID {}", request.getId());
        return ResponseEntity.ok(documentsService.updateDocumentsById(request));
    }

    @Operation(
            method = "GET",
            summary = "Find documents by occurrence ID.",
            description = "This endpoint is used to return documents by occurrence ID."
    )
    @ApiResponse(responseCode = "200", description = "Documents found.")
    @ApiResponse(responseCode = "404", description = "Documents not found.")
    @ApiResponse(responseCode = "500", description = "Internal server error.")
    @GetMapping("/find-document/{occurrenceId}")
    public ResponseEntity<List<DocumentsResponseDTO>> getDocumentsByOccurrenceId(@PathVariable String occurrenceId) {
        logger.info("Getting documents by occurrence ID {}", occurrenceId);
        List<DocumentsResponseDTO> documents = documentsService.getDocumentsByOccurrenceId(occurrenceId);
        return ResponseEntity.ok(documents);

    }
}



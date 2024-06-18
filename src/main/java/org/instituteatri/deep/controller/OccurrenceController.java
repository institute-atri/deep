package org.instituteatri.deep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            method = "GET",
            summary = "Get  all ocurrences",
            description = "Endpoint to retrieve all occurrences. This operation returns a list containing all available occurrences in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "[{" +
                                            "\"id\": \"string\"," +
                                            "\"name\": \"string\"," +
                                            "\"description\": \"string\"" +
                                            "}]"
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<OccurrenceResponseDTO>> getAll() {
        List<OccurrenceResponseDTO> all = service.getAll();
        LOGGER.info("Received request to get all occurrences");
        return ResponseEntity.ok().body(all);
    }
    @Operation (
            method = "GET",
            summary = "Retrieve occurrence by ID",
            description = "Returns the details of a specific occurrence identified by the provided ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            " \"id\": \"e7b1a29f-4f5c-4c2d-b732-1b12f8e678a4\"," +
                                            " \name\": \"string\", "+
                                            " \"description\": \"string\""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found.",
                    content = @Content (mediaType = "application/json",
                            examples = @ExampleObject(
                                    value ="{\"message\":\"Could not find occurrence with id:e7b1a29f-4f5c-4c2d-b732-1b12f8e678a4\"}"
                            ))
            )

    })
    @GetMapping("/{id}")
    public ResponseEntity<OccurrenceResponseDTO> getById(@PathVariable String id) {
        OccurrenceResponseDTO responseDTO = service.getById(id);
        LOGGER.info("Received request to get occurrence by ID");
        return ResponseEntity.ok().body(responseDTO);
    }
    @Operation(
            method = "POST",
            summary = "Create a new occurrence."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "[{" +
                                            "\"id\":\"string\"," +
                                            "\"name\":\"string\"," +
                                            "\"description\":\"string\"" +
                                            "}]"
                            ))),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"name\": [\"Name is required.\", \"Name cannot be longer than 50 charecters.\"]," +
                                            "\"description\":[\"Description is required.\", \"Description cannot be longer than 255 charecters.\"]" +
                                            "}"
                            ))),

            @ApiResponse(responseCode = "403", description = "Forbidden.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User isn't authorized.\"}"
                            )
                    )
            )
    }
    )
    @PostMapping("/create")
    public ResponseEntity<OccurrenceResponseDTO> create(@RequestBody OccurrenceRequestDTO request) {
        OccurrenceResponseDTO responseDTO = service.save(request);
        LOGGER.info("Received request to create a new occurrence");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(responseDTO);
    }
    @Operation (
            method = "DELETE",
            summary = "Delete an occurrence by ID",
            description = "Delete an existing occurrence identified by the provided ID",
            responses ={
                    @ApiResponse(responseCode = "204", description = "Success."),
                    @ApiResponse(responseCode = "403", description = "Forbidden: User lacks permission to delete the occurrence.",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"message\":\"User isn't authorized.\"}"
                                    ))),
                    @ApiResponse(responseCode = "404", description = "Not Found: Occurrence with the specified ID does not exist.",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{" +
                                                    "\"message\":\"Occurrence not found with ID: {id}\"" +
                                                    "}")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        LOGGER.info("Received request to delete an occurrence");
        return ResponseEntity.noContent().build();
    }
}

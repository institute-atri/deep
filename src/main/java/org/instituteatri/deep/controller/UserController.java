package org.instituteatri.deep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dto.request.RegisterRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.dto.response.UserResponseDTO;
import org.instituteatri.deep.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Endpoints for user management")
public class UserController {

    private final UserService userService;

    @Operation(
            method = "GET",
            summary = "Get all users.",
            description = "Returns a list of all users registered in the system, only with the admin token.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"id\":\"string\"" +
                                            ",\"name\":\"string\"," +
                                            "\"email\":\"string\"}"
                            ))),

            @ApiResponse(responseCode = "403", description = "Forbidden.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User isn't authorized.\"}"
                            )))
    })
    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return userService.getAllUsers();
    }


    @Operation(
            method = "GET",
            summary = "Get an user by ID",
            description = "Returns the user with the specified ID. Only users with the ADMIN role can search by user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"id\":\"string\"" +
                                            ",\"name\":\"string\"," +
                                            "\"email\":\"string\"}"
                            ))),

            @ApiResponse(
                    responseCode = "404",
                    description = "Not found.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Could not find user with id:661eff5e4af2c96e8a7dedc92\"}"
                            ))),

            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User isn't authorized.\"}"
                            )))
    })
    @GetMapping("/find/{id}")
    public ResponseEntity<UserResponseDTO> getByUserId(@PathVariable String id) {
        UserResponseDTO user = userService.getByUserId(id);
        return ResponseEntity.ok().body(user);
    }

    @Operation(
            method = "PUT",
            summary = "Update an event.",
            description = "Updates the user identified by the specified ID. " +
                    "Only authenticated users with a valid token are authorized to perform this operation.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"token\": [\"string\"]," +
                                            "\"refresh-token\": [\"string\"]" +
                                            "}"
                            ))),

            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User isn't authenticated.\"}"
                            ))),

            @ApiResponse(responseCode = "403", description = "Forbidden.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User isn't authorized.\"}"
                            ))),

            @ApiResponse(responseCode = "404", description = "Not found.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Could not find user with id:661eff5e4af2c96e8a7dedc92\"}"
                            ))),

            @ApiResponse(responseCode = "409", description = "Conflicts.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"E-mail not available.\"}"
                            )))
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<TokenResponseDTO> updateUser(
            @PathVariable String id,
            @RequestBody @Valid RegisterRequestDTO registerRequestDTO,
            Authentication authentication
    ) {
        return userService.updateUser(id, registerRequestDTO, authentication);
    }

    @Operation(
            method = "DELETE",
            summary = "Delete an user by ID",
            description = "Deletes the user with the specified ID. Only users with the ADMIN role can delete the user.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content."),

            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User isn't authenticated.\"}"
                            ))),

            @ApiResponse(responseCode = "403", description = "Forbidden.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User isn't authorized.\"}"
                            ))),

            @ApiResponse(responseCode = "404", description = "Not found.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Could not find user with id:661eff5e4af2c96e8a7dedc92\"}"
                            ))),
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }
}

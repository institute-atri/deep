package org.instituteatri.deep.controller;

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
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<UserResponseDTO> getByUserId(@PathVariable String id) {
        UserResponseDTO user = userService.getByUserId(id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TokenResponseDTO> updateUser(
            @PathVariable String id,
            @RequestBody @Valid RegisterRequestDTO registerRequestDTO,
            Authentication authentication
    ) {
        return userService.updateUser(id, registerRequestDTO, authentication);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }
}

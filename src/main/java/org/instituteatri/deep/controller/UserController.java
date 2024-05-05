package org.instituteatri.deep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dtos.user.RegisterDTO;
import org.instituteatri.deep.dtos.user.ResponseDTO;
import org.instituteatri.deep.dtos.user.UserDTO;
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
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<UserDTO> getByUserId(@PathVariable String id) {
        UserDTO user = userService.getByUserId(id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updateUser(
            @PathVariable String id,
            @RequestBody @Valid RegisterDTO registerDTO,
            Authentication authentication
    ) {
        return userService.updateUser(id, registerDTO, authentication);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }
}

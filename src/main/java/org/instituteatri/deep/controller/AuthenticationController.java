package org.instituteatri.deep.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    @PostMapping("/login")
    public String getLogin() {
        return "Hello, Deep!";
    }

    @PostMapping("/logout")
    public String getLogout() {
        return "Hello, Deep!";
    }
}

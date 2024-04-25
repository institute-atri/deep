package org.instituteatri.deep.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {


    @GetMapping()
    public String getUsers() {
        return "Hello, Deep!";
    }

    @PutMapping("/update/{id}")
    public String getUpdateById() {
        return "Hello, Deep!";
    }
}

package org.instituteatri.deep.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/investigation")
public class InvestigationController {

    @GetMapping
    public String getInvestigation() {
        return "Hello, Deep!";
    }

}

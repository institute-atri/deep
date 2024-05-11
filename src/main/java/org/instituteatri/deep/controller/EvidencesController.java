package org.instituteatri.deep.controller;



import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


@RestController
@RequestMapping("/evidences")
public class EvidencesController {


    @PostMapping("/evidences")
    public String getEvidences() {return "Hello, Deep!";}

    @GetMapping("/evidences/{id}")
    public String getEvidencesById() {
        return "Hello, Deep!";
    }

    @GetMapping("/cases/{casesId}/evidences")
    public String getEvidencesByCasesId() {
        return "Hello, Deep!";
    }

    @PutMapping("/evidences/{id}")
    public String updateEvidencesById() {
        return "Hello, Deep!";
    }

    @DeleteMapping("/evidences/{id}")
    public String deleteEvidencesById() {
        return "Hello, Deep!";
    }


}

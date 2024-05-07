package org.instituteatri.deep.controllers;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents-and-evidences")
public class DocumentsAndEvidencesController {

    @PostMapping("/documents")
    public String getDocuments() {
        return "Hello, Deep!";
    }

    @GetMapping("/documents/{id}")
    public String getDocumentsById() {
        return "Hello, Deep!";
    }

    @GetMapping("/cases/{casesId}/documents")
    public String getDocumentsByCasesId() {
        return "Hello, Deep!";
    }

    @PutMapping("/documents/{id}")
    public String updateDocumentsById() {
        return "Hello, Deep!";
    }

    @DeleteMapping("/documents/{id}")
    public String deleteDocumentsById() {
        return "Hello, Deep!";
    }


}

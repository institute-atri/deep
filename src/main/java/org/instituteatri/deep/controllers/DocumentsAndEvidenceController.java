package org.instituteatri.deep.controllers;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents-and-evidence")
public class DocumentsAndEvidenceController {

    @PostMapping("/documents")
    public String getDocuments() {
        return "Hello, Documents!";
    }

    @GetMapping("/documents/{id}")
    public String getDocumentsById() {
        return "Hello, Documents!";
    }

    @GetMapping("/cases/{casesId}/documents")
    public String getDocumentsByCasesId() {
        return "Hello, Documents!";
    }

    @PutMapping("/documents/{id}")
    public String updateDocumentsById() {
        return "Hello, Documents!";
    }

    @DeleteMapping("/documents/{id}")
    public String deleteDocumentsById() {
        return "Hello, Documents!";
    }


}

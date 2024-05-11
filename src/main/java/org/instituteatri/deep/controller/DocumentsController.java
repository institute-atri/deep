package org.instituteatri.deep.controller;



import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


@RestController
@RequestMapping("/documents")
public class DocumentsController {


    @PostMapping("/documents")
    public String getDocuments() { return "Hello, Deep!"; }

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

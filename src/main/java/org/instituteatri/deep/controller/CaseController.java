package org.instituteatri.deep.controller;
import jakarta.transaction.Transactional;
import org.instituteatri.deep.controller.caseData.Case;
import org.instituteatri.deep.controller.caseData.CaseRepository;

import org.instituteatri.deep.controller.dto.CaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/case")
public class CaseController {
    @Autowired
    private CaseRepository repository;

    @PostMapping("/new-case")
    @Transactional
    public void createCase(@RequestBody CaseDto inquiry){
        repository.save(new Case(inquiry));
    }

    @GetMapping("/case-by-id/{id}")
    public Optional<Case> listOfCasesById(@PathVariable Long id){
        return repository.findById(id);
    }

    @GetMapping("/list")
    public List<Case> listOfCases(){
         return repository.findAll();
    }

    @PutMapping("/update")
    @Transactional
    public void update(@RequestBody CaseDto inquiry){
        var cases = repository.getReferenceById(inquiry.id());
        cases.updateCase(inquiry);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void delete(@PathVariable Long id){
        repository.deleteById(id);
    }
}
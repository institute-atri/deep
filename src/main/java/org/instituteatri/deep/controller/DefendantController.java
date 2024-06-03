package org.instituteatri.deep.controller;


import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dto.request.OccurrenceRequestDTO;
import org.instituteatri.deep.dto.response.DefendantDTO;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.model.Defendant;
import org.instituteatri.deep.model.Occurrence;
import org.instituteatri.deep.service.DefendantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/defendant")
@RequiredArgsConstructor
public class DefendantController {

    private final DefendantService defendantService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefendantController.class);

    @GetMapping
    public ResponseEntity<List<DefendantDTO>> getAll(){
        List<DefendantDTO> defendantDTOList = defendantService.getAll();
        LOGGER.info("Request to get all defendants received");
        return ResponseEntity.ok(defendantDTOList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DefendantDTO> getById(@PathVariable String id){
        DefendantDTO defendantDTO = defendantService.getById(id);
        LOGGER.info("Request to get defendant by id received");
        return ResponseEntity.ok(defendantDTO);
    }
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<DefendantDTO> create(@RequestBody DefendantDTO defendant){
        DefendantDTO defendantDTO = defendantService.save(defendant);
        LOGGER.info("Request to save a new defendant received");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(defendantDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        defendantService.delete(id);
        LOGGER.info("Request to delete a defendant received");
        return ResponseEntity.noContent().build();
    }



}

package org.instituteatri.deep.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.instituteatri.deep.dto.response.EvidencesResponseDTO;
import org.instituteatri.deep.model.EvidencesModel;
import org.instituteatri.deep.repository.EvidencesRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EvidencesService {

    private static final Logger logger = LoggerFactory.getLogger(EvidencesService.class);
    private final EvidencesRepository evidencesRepository;
    private final ModelMapper modelMapper;

    // Find evidence by ID
    public EvidencesResponseDTO getEvidenceById(String id) {
        EvidencesModel evidence = evidencesRepository.findById(id).orElse(null);
        if (evidence == null) {
            logger.error("Evidence with ID {} not found", id);
            return null;
        }
        return modelMapper.map(evidence, EvidencesResponseDTO.class);
    }
    // This method is used to save and map evidences, it is used by both create and update methods
    private EvidencesResponseDTO saveAndMapEvidences(EvidencesResponseDTO request) {
        EvidencesModel evidence = modelMapper.map(request, EvidencesModel.class);
        evidence.setCreatedAt(Instant.now());
        evidencesRepository.save(evidence);
        return modelMapper.map(evidence, EvidencesResponseDTO.class);
    }

    // Update evidence by ID
    public EvidencesResponseDTO updateEvidencesById(EvidencesResponseDTO request) {
        logger.info("Updating evidence with ID {}", request.getId());
        return saveAndMapEvidences(request);
    }

    // Create evidence
     public EvidencesResponseDTO createEvidences(EvidencesResponseDTO request) {
         logger.info("Creating evidence with ID {}", request.getId());
         return saveAndMapEvidences(request);
    }

    // Find evidences by occurrence ID
    public List<EvidencesResponseDTO> getEvidenceByOccurrenceId(String occurrenceId) {
        List<EvidencesModel> evidences = evidencesRepository.findByOccurrenceId(occurrenceId);
        if (evidences.isEmpty()) {
            logger.error("Evidences with occurrence ID {} not found", occurrenceId);
            return new ArrayList<>();
        } else {
            return evidences.stream()
                    .map(evidence -> modelMapper.map(evidence, EvidencesResponseDTO.class))
                    .toList();
        }
    }
     // Delete evidence by ID
    public void deleteEvidenceById(String id) {
        logger.info("Deleting evidence with ID {}", id);
        evidencesRepository.deleteById(id);
    }

}
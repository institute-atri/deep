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

    public List<EvidencesResponseDTO> getAllEvidences() {
        List<EvidencesModel> evidences = evidencesRepository.findAll();
        List<EvidencesResponseDTO> evidencesResponseDTO = new ArrayList<>();
        for (EvidencesModel evidence : evidences) {
            evidencesResponseDTO.add(modelMapper.map(evidence, EvidencesResponseDTO.class));
        }
        return evidencesResponseDTO;
    }

    public EvidencesResponseDTO createEvidences(EvidencesResponseDTO request) {
        EvidencesModel evidence = modelMapper.map(request, EvidencesModel.class);
        evidence.setCreatedAt(Instant.now());
        logger.info("Creating evidence with ID {}", evidence.getId());
        evidencesRepository.save(evidence);
        return modelMapper.map(evidence, EvidencesResponseDTO.class);
    }

    public EvidencesResponseDTO getEvidenceById(String id) {
        EvidencesModel evidence = evidencesRepository.findById(id).orElse(null);
        if (evidence == null) {
            logger.error("Evidence with ID {} not found", id);
            return null;
        }
        return modelMapper.map(evidence, EvidencesResponseDTO.class);
    }

    public EvidencesResponseDTO updateEvidencesById(EvidencesResponseDTO request) {
        EvidencesModel evidence = modelMapper.map(request, EvidencesModel.class);
        evidence.setCreatedAt(Instant.now());
        logger.info("Updating evidence with ID {}", evidence.getId());
        evidencesRepository.save(evidence);
        return modelMapper.map(evidence, EvidencesResponseDTO.class);

    }

    public void deleteEvidenceById(String id) {
        logger.info("Deleting evidence with ID {}", id);
        evidencesRepository.deleteById(id);
    }

}
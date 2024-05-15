package org.instituteatri.deep.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.instituteatri.deep.dto.response.DocumentsResponseDTO;
import org.instituteatri.deep.model.DocumentsModel;
import org.instituteatri.deep.repository.DocumentsRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DocumentsService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentsService.class);
    private final DocumentsRepository documentsRepository;
    private final ModelMapper modelMapper;

    public List<DocumentsResponseDTO> getAllDocuments() {
        List<DocumentsModel> documents = documentsRepository.findAll();
        List<DocumentsResponseDTO> documentsResponseDTO = new ArrayList<>();
        for (DocumentsModel document : documents) {
            documentsResponseDTO.add(modelMapper.map(document, DocumentsResponseDTO.class));
        }
        return documentsResponseDTO;
    }
    public DocumentsResponseDTO getDocumentById(String id) {
        DocumentsModel document = documentsRepository.findById(id).orElse(null);
        if (document == null) {
            logger.error("Document with ID {} not found", id);
            return null;
        }
        return modelMapper.map(document, DocumentsResponseDTO.class);
    }
    public DocumentsResponseDTO updateDocumentsById(DocumentsResponseDTO request) {
        DocumentsModel document = modelMapper.map(request, DocumentsModel.class);
        document.setCreatedAt(Instant.now());
        logger.info("Updating document with ID {}", document.getId());
        documentsRepository.save(document);
        return modelMapper.map(document, DocumentsResponseDTO.class);
    }
    public void deleteDocumentById(String id) {
        logger.info("Deleting document with ID {}", id);
        documentsRepository.deleteById(id);
    }
}


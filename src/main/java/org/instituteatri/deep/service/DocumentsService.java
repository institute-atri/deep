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


    // Find documents by ID
    public DocumentsResponseDTO getDocumentsById(String id) {
        DocumentsModel document = documentsRepository.findById(id).orElse(null);
        if (document == null) {
            logger.error("Document with ID {} not found", id);
            return null;
        }
        return modelMapper.map(document, DocumentsResponseDTO.class);
    }

    // This method is used to save and map documents, it is used by both create and update methods
    private DocumentsResponseDTO saveAndMapDocuments(DocumentsResponseDTO request) {
        DocumentsModel document = modelMapper.map(request, DocumentsModel.class);
        document.setCreatedAt(Instant.now());
        documentsRepository.save(document);
        return modelMapper.map(document, DocumentsResponseDTO.class);
    }

    // Update documents by ID
    public DocumentsResponseDTO updateDocumentsById(DocumentsResponseDTO request) {
        logger.info("Updating document with ID {}", request.getId());
        return saveAndMapDocuments(request);
    }

    // Create documents
    public DocumentsResponseDTO createDocuments(DocumentsResponseDTO request) {
        logger.info("Creating document with ID {}", request.getId());
        return saveAndMapDocuments(request);
    }

    // Find documents by occurrence ID
    public List<DocumentsResponseDTO> getDocumentsByOccurrenceId(String occurrenceId) {
        List<DocumentsModel> documents = documentsRepository.findByOccurrenceId(occurrenceId);
        if (documents.isEmpty()) {
            logger.error("Documents with occurrence ID {} not found", occurrenceId);
            return new ArrayList<>();
        } else {
            return documents.stream()
                    .map(document -> modelMapper.map(document, DocumentsResponseDTO.class))
                    .toList();
        }
    }

    // Delete documents by ID
    public void deleteDocumentById(String id) {
        logger.info("Deleting document with ID {}", id);
        documentsRepository.deleteById(id);
    }

}


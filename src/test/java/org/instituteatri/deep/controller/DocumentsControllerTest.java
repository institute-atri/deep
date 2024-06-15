package org.instituteatri.deep.controller;

import org.instituteatri.deep.dto.response.DocumentsResponseDTO;
import org.instituteatri.deep.service.DocumentsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DocumentsControllerTest {

    @Mock
    private DocumentsService documentsService;

    @InjectMocks
    private DocumentsController DocumentsController;

    @Test
    @DisplayName("When the Id is valid, it should return the document")
    public void when_valid_id_then_return_document() {

        //Given
        String id = "1";
        DocumentsResponseDTO expectedDocument = new DocumentsResponseDTO("test2", "testName2", "testDescription2", null, null, null, null, null, null);
        when(documentsService.getDocumentsById(id)).thenReturn(expectedDocument);

        //When
        ResponseEntity<DocumentsResponseDTO> responseEntity = DocumentsController.getDocumentById(id);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedDocument);
    }


    @Test
    @DisplayName("When the OccurrenceId is valid, it should return the document")
    public void when_valid_occurrence_id_then_return_document() {
        List<DocumentsResponseDTO> expectedDocuments = Arrays.asList(
                new DocumentsResponseDTO("1", "testName1", "testDescription1", null, null, null, null, null, null),
                new DocumentsResponseDTO("2", "testName2", "testDescription2", null, null, null, null, null, null)
        );
        when(documentsService.getDocumentsByOccurrenceId("1")).thenReturn(expectedDocuments);

        //When
        ResponseEntity<List<DocumentsResponseDTO>> responseEntity = DocumentsController.getDocumentsByOccurrenceId("1");

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedDocuments);

    }

    @Test
    @DisplayName("When the Id is valid, it should delete the document")
    public void when_valid_id_then_delete_document() {

        //Given
        String documentId = "1";

        //When
        ResponseEntity<Void> responseEntity = DocumentsController.deleteDocumentById(documentId);

        //Then
        verify(documentsService).deleteDocumentById("1");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();

    }

    @Test
    @DisplayName("When the Id is valid, it should update the document")
    public void when_valid_id_then_update_document() {
        // Given
        DocumentsResponseDTO updateDocument = new DocumentsResponseDTO("testId1", "testName1", "testDescription1", null, null, null, null, null, null);
        when(documentsService.updateDocumentsById(updateDocument)).thenReturn(updateDocument);

        // When
        ResponseEntity<DocumentsResponseDTO> responseEntity = DocumentsController.updateDocumentById(updateDocument);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(updateDocument);
    }


    @Test
    @DisplayName("This test must create a new Document and return the created")
    public void this_test_must_create_a_document() {
        //Given
        DocumentsResponseDTO newDocument = new DocumentsResponseDTO("testNewDocId", "testNewDocName", "testNewDocDescription", null, null, null, null, null, null);
        DocumentsResponseDTO createdDocument = new DocumentsResponseDTO("testId1", "testName1", "testDescription1", null, null, null, null, null, null);
        when(documentsService.createDocuments(newDocument)).thenReturn(createdDocument);

        //When
        ResponseEntity<DocumentsResponseDTO> responseEntity = DocumentsController.createDocument(newDocument);

        //Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(createdDocument);
    }
}

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DocumentsControllerTest {

    @Mock
    private DocumentsService documentsService;

    @InjectMocks
    private DocumentsController DocumentsController;

    @Test
    @DisplayName("When the Id is valid, it should return the document")
    public void whenValidId_ThenReturnDocument() {

        //Given
        String DocumentId = "testId1";
        DocumentsResponseDTO expectedDocument = new DocumentsResponseDTO("testId1", "testName1", "testDescription1", null, null, null, null, null, null);
        when(documentsService.getDocumentsById("1")).thenReturn(expectedDocument);

        //when
        ResponseEntity<DocumentsResponseDTO> responseEntity = DocumentsController.getDocumentById(DocumentId);

        //Then
        

    }


    @Test

    public void testGetDocumentByOccurrenceId() {
        List<DocumentsResponseDTO> expectedDocuments = Arrays.asList(
                new DocumentsResponseDTO("1", "testName1", "testDescription1", null, null, null, null, null, null),
                new DocumentsResponseDTO("2", "testName2", "testDescription2", null, null, null, null, null, null)
        );
        when(documentsService.getDocumentsByOccurrenceId("1")).thenReturn(expectedDocuments);

        //when
        ResponseEntity<List<DocumentsResponseDTO>> responseEntity = DocumentsController.getDocumentsByOccurrenceId("1");



    }

    @Test
    public void testDeleteDocumentById() {
        documentsService.deleteDocumentById("1");
    }

    @Test
    public void testUpdateDocumentById() {
        when(documentsService.updateDocumentsById(new DocumentsResponseDTO())).thenReturn(new DocumentsResponseDTO());
    }

    @Test
    public void testCreateDocuments() {
        when(documentsService.createDocuments(new DocumentsResponseDTO())).thenReturn(new DocumentsResponseDTO());

        String result = documentsService.createDocuments(new DocumentsResponseDTO()).toString();
        assertEquals("DocumentsResponseDTO(id=null, name=null, description=null, date=null, place=null, type=null, currentState=null, message=null, responsible=null)", result);
    }
}

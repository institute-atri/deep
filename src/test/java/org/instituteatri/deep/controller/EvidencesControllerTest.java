package org.instituteatri.deep.controller;

import org.instituteatri.deep.dto.response.EvidencesResponseDTO;
import org.instituteatri.deep.service.EvidencesService;
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
public class EvidencesControllerTest {

    @Mock
    private EvidencesService EvidencesService;

    @InjectMocks
    private EvidencesController EvidencesController;

    @Test
    @DisplayName("When the Id is valid, it should return the evidence")
    public void when_valid_id_then_return_evidence() {

        //Given
        String id = "1";
        EvidencesResponseDTO expectedEvidence = new EvidencesResponseDTO("test2", "testName2", "testDescription2", null, null, null, null, null, null);
        when(EvidencesService.getEvidenceById(id)).thenReturn(expectedEvidence);

        //When
        ResponseEntity<EvidencesResponseDTO> responseEntity = EvidencesController.getEvidenceById(id);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedEvidence);
    }


    @Test
    @DisplayName("When the OccurrenceId is valid, it should return the evidence")
    public void when_valid_occurrenceId_then_return_evidence() {
        List<EvidencesResponseDTO> expectedEvidences = Arrays.asList(
                new EvidencesResponseDTO("1", "testName1", "testDescription1", null, null, null, null, null, null),
                new EvidencesResponseDTO("2", "testName2", "testDescription2", null, null, null, null, null, null)
        );
        when(EvidencesService.getEvidenceByOccurrenceId("1")).thenReturn(expectedEvidences);
        //When
        ResponseEntity<List<EvidencesResponseDTO>> responseEntity = EvidencesController.getEvidencesByOccurrenceId("1");

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedEvidences);
    }

    @Test
    @DisplayName("When the Id is valid, it should delete the evidence")
    public void when_valid_id_then_delete_evidence() {
        //Given
        String id = "1";

        //When
        ResponseEntity<Void> responseEntity = EvidencesController.deleteEvidenceById(id);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(EvidencesService).deleteEvidenceById(id);
    }

    @Test
    @DisplayName("When the Id is valid, it should update the evidence")
    public void when_valid_id_then_update_evidence() {
        //Given
        EvidencesResponseDTO updatedEvidence = new EvidencesResponseDTO("test1", "testName1", "testDescription1", null, null, null, null, null, null);
        when(EvidencesService.updateEvidencesById(updatedEvidence)).thenReturn(updatedEvidence);

        //When
        ResponseEntity<EvidencesResponseDTO> responseEntity = EvidencesController.updateEvidenceById(updatedEvidence);

        //Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(updatedEvidence);
    }

    @Test
    @DisplayName("When the request is valid, it should create the evidence")
    public void when_valid_request_then_create_evidence() {
        //Given
        EvidencesResponseDTO newEvidence = new EvidencesResponseDTO("testNewEvidenceId", "testNewEvidenceName", "testNewEvidenceDescription", null, null, null, null, null, null);
        EvidencesResponseDTO createdEvidence = new EvidencesResponseDTO("test1", "testName1", "testDescription1", null, null, null, null, null, null);
        when(EvidencesService.createEvidences(newEvidence)).thenReturn(createdEvidence);

        //When
        ResponseEntity<EvidencesResponseDTO> responseEntity = EvidencesController.createEvidence(newEvidence);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(createdEvidence);
    }
}
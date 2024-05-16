package org.instituteatri.deep.controller;

import org.instituteatri.deep.dto.request.OccurrenceRequestDTO;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.service.OccurrenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class OccurrenceControllerTest {

    @Mock
    private OccurrenceService occurrenceService;
    @InjectMocks
    private OccurrenceController occurrenceController;

    @Test
    @DisplayName("When the service returns multiple occurrences, it should return a list of occurrences")
    void getAll_WhenServiceReturnsMultipleOccurrences_ReturnsListOfOccurrences() {
        // Given
        List<OccurrenceResponseDTO> expectedOccurrences = Arrays.asList(
                new OccurrenceResponseDTO("testId1", "testName1", "testDescription1", Instant.now(), null, null),
                new OccurrenceResponseDTO("testId2", "testName2", "testDescription2", Instant.now(), null, null)
        );
        when(occurrenceService.getAll()).thenReturn(expectedOccurrences);

        // When
        ResponseEntity<List<OccurrenceResponseDTO>> responseEntity = occurrenceController.getAll();

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedOccurrences);
    }

    @Test
    @DisplayName("When the service returns an empty list, it must return an empty list")
    void getAll_WhenServiceReturnsEmptyList_ShouldReturnEmptyList() {
        // Given
        List<OccurrenceResponseDTO> expectedOccurrences = Collections.emptyList();
        when(occurrenceService.getAll()).thenReturn(expectedOccurrences);

        // When
        ResponseEntity<List<OccurrenceResponseDTO>> responseEntity = occurrenceController.getAll();

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedOccurrences);
    }

    @Test
    @DisplayName("When the ID is valid, it should return the corresponding occurrence")
    void getById_WhenValidId_ThenReturnOccurrence() {
        // Given
        String occurrenceId = "testId1";
        OccurrenceResponseDTO expectedResponse = new OccurrenceResponseDTO("testId2", "testName2", "testDescription2", Instant.now(), null, null);
        when(occurrenceService.getById(occurrenceId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<OccurrenceResponseDTO> responseEntity = occurrenceController.getById(occurrenceId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Upon receiving a valid request to create, it should return the created occurrence")
    void create_WhenValidRequest_ThenReturnCreatedOccurrence() {
        // Given
        OccurrenceRequestDTO requestDTO = new OccurrenceRequestDTO("testName", "testDescription");
        OccurrenceResponseDTO expectedResponse = new OccurrenceResponseDTO("testId2", "testName2", "testDescription2", Instant.now(), null, null);
        when(occurrenceService.save(requestDTO)).thenReturn(expectedResponse);

        // When
        ResponseEntity<OccurrenceResponseDTO> responseEntity = occurrenceController.create(requestDTO);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Upon receiving a valid request to delete, it should return an empty content response")
    void delete_WhenValidRequest_ThenReturnNoContentResponse() {
        // Given
        String occurrenceId = "testId";
        doNothing().when(occurrenceService).delete(occurrenceId);

        // When
        ResponseEntity<Void> responseEntity = occurrenceController.delete(occurrenceId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
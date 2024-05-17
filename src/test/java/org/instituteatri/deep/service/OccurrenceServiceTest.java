package org.instituteatri.deep.service;

import org.instituteatri.deep.dto.request.OccurrenceRequestDTO;
import org.instituteatri.deep.dto.response.OccurrenceResponseDTO;
import org.instituteatri.deep.model.Occurrence;
import org.instituteatri.deep.repository.OccurrenceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class OccurrenceServiceTest {

    @InjectMocks
    private OccurrenceService occurrenceService;

    @Mock
    private OccurrenceRepository occurrenceRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    @DisplayName("Return a list of occurrences when successful")
    void getAll_ReturnAListOfOccurrence_WhenSuccessful() {
        // Given
        List<Occurrence> occurrences = new ArrayList<>();
        occurrences.add(new Occurrence(null, "Occurrence 1", "Description 1", Instant.now(), null, Collections.emptyList(), Collections.emptyList()));
        occurrences.add(new Occurrence(null, "Occurrence 2", "Description 2", Instant.now(), null, Collections.emptyList(), Collections.emptyList()));

        when(occurrenceRepository.findAll()).thenReturn(occurrences);

        List<OccurrenceResponseDTO> mappedOccurrences = new ArrayList<>();
        mappedOccurrences.add(new OccurrenceResponseDTO(occurrences.getFirst().getId(), "Occurrence 1", "Description 1", Instant.now(), Instant.now(), null));
        mappedOccurrences.add(new OccurrenceResponseDTO(occurrences.get(1).getId(), "Occurrence 2", "Description 2", Instant.now(), Instant.now(), null));

        when(modelMapper.map(any(), eq(OccurrenceResponseDTO.class))).thenReturn(mappedOccurrences.get(0), mappedOccurrences.get(1));

        // When
        List<OccurrenceResponseDTO> result = occurrenceService.getAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Occurrence 1");
        assertThat(result.get(1).getName()).isEqualTo("Occurrence 2");

        verify(occurrenceRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(), eq(OccurrenceResponseDTO.class));
    }

    @Test
    @DisplayName("Return an occurrence when successful")
    void getById_ReturnAListOfOccurrence_WhenSuccessful() {
        // Given
        OccurrenceResponseDTO mappedOccurrence = new OccurrenceResponseDTO("1", "Test Occurrence", "Test Description", Instant.now(), Instant.now(), null);
        when(modelMapper.map(any(), eq(OccurrenceResponseDTO.class))).thenReturn(mappedOccurrence);
        when(occurrenceRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(new Occurrence("1", "Occurrence 1", "Description 1", Instant.now(), null, Collections.emptyList(), Collections.emptyList())));

        // When
        OccurrenceResponseDTO result = occurrenceService.getById("1");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Occurrence");

        verify(occurrenceRepository, times(1)).findById("1");
        verify(modelMapper, times(1)).map(any(), eq(OccurrenceResponseDTO.class));
    }

    @Test
    @DisplayName("Save an occurrence when successful")
    void save_SaveAnOccurrence_WhenSuccessful() {
        // Given
        OccurrenceRequestDTO request = new OccurrenceRequestDTO("Occurrence 1", "Description 1");
        Occurrence occurrenceToSave = new Occurrence(null, "Occurrence 1", "Description 1", null, null, Collections.emptyList(), Collections.emptyList());
        Occurrence occurrenceSaved = new Occurrence("1", "Occurrence 1", "Description 1", Instant.now(), null, Collections.emptyList(), Collections.emptyList());
        OccurrenceResponseDTO mappedOccurrence = new OccurrenceResponseDTO("1", "Occurrence 1", "Description 1", Instant.now(), Instant.now(), null);

        when(modelMapper.map(request, Occurrence.class)).thenReturn(occurrenceToSave);
        when(occurrenceRepository.save(occurrenceToSave)).thenReturn(occurrenceSaved);
        when(modelMapper.map(occurrenceSaved, OccurrenceResponseDTO.class)).thenReturn(mappedOccurrence);

        // When
        OccurrenceResponseDTO result = occurrenceService.save(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("Occurrence 1");

        verify(modelMapper, times(1)).map(request, Occurrence.class);
        verify(occurrenceRepository, times(1)).save(occurrenceToSave);
        verify(modelMapper, times(1)).map(occurrenceSaved, OccurrenceResponseDTO.class);
    }

    @Test
    @DisplayName("Delete an when successful")
    void delete_RemoveOccurrence_WhenSuccessful() {
        // Given
        String occurrenceId = "1";

        // When
        occurrenceService.delete(occurrenceId);

        // Then
        verify(occurrenceRepository, times(1)).deleteById(occurrenceId);
    }
}
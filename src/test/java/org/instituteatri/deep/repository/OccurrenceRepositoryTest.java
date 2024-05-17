package org.instituteatri.deep.repository;

import org.instituteatri.deep.model.Occurrence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OccurrenceRepositoryTest {

    @Autowired
    private OccurrenceRepository occurrenceRepository;

    @Test
    void findAll_ReturnAListOfOccurrence_WhenSuccessful() {
        // Given
        Occurrence occurrence1 = new Occurrence(null, "Occurrence 1", "Description 1", Instant.now(), null, Collections.emptyList(), Collections.emptyList());
        Occurrence occurrence2 = new Occurrence(null, "Occurrence 2", "Description 2", Instant.now(), null, Collections.emptyList(), Collections.emptyList());
        occurrenceRepository.save(occurrence1);
        occurrenceRepository.save(occurrence2);

        // When
        List<Occurrence> result = occurrenceRepository.findAll();

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void findById_ReturnOccurrence_WhenSuccessful() {
        // Given
        Occurrence occurrence = new Occurrence(null, "Test Occurrence", "Description 2", Instant.now(), null, Collections.emptyList(), Collections.emptyList());
        Occurrence saved = occurrenceRepository.save(occurrence);

        // When
        Optional<Occurrence> result = occurrenceRepository.findById(saved.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(saved.getId()).isEqualTo(result.get().getId());
        assertThat(result.get().getName()).isEqualTo("Test Occurrence");
    }

    @Test
    void save_PersistOccurrence_WhenSuccessful() {
        // Given
        Occurrence occurrenceToSave = new Occurrence(null, "Occurrence 1", "Description 1", Instant.now(), null, Collections.emptyList(), Collections.emptyList());

        // When
        Occurrence result = occurrenceRepository.save(occurrenceToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotEmpty();
        assertThat(result.getName()).isEqualTo("Occurrence 1");
    }

    @Test
    void delete_RemoveOccurrence_WhenSuccessful() {
        // Given
        Occurrence occurrenceToSave = new Occurrence(null, "Occurrence 1", "Description 1", Instant.now(), null, Collections.emptyList(), Collections.emptyList());
        Occurrence saved = occurrenceRepository.save(occurrenceToSave);
        assertThat(occurrenceRepository.findById(saved.getId())).isNotEmpty();

        // When
        occurrenceRepository.delete(saved);

        // Then
        Optional<Occurrence> occurrence = occurrenceRepository.findById(saved.getId());
        assertThat(occurrence).isEmpty();
    }
}
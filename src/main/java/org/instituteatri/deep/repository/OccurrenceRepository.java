package org.instituteatri.deep.repository;

import org.instituteatri.deep.model.Occurrence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OccurrenceRepository extends JpaRepository<Occurrence, String> {
}

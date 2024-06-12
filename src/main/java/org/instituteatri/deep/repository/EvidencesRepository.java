package org.instituteatri.deep.repository;
import org.instituteatri.deep.model.EvidencesModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface EvidencesRepository extends JpaRepository<EvidencesModel, String>{
    List<EvidencesModel> findByOccurrenceId(String occurrenceId);
}

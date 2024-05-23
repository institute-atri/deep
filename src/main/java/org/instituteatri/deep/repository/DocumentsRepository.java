package org.instituteatri.deep.repository;

import org.instituteatri.deep.model.DocumentsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface DocumentsRepository extends JpaRepository<DocumentsModel, String>{
    List<DocumentsModel> findByCaseId(String caseId);
}

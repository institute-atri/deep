package org.instituteatri.deep.repository;

import org.instituteatri.deep.model.DocumentsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentsRepository extends JpaRepository<DocumentsModel, String> {
}

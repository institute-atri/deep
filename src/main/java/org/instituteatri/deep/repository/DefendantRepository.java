package org.instituteatri.deep.repository;


import org.instituteatri.deep.model.Defendant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefendantRepository extends JpaRepository<Defendant, String> {
}

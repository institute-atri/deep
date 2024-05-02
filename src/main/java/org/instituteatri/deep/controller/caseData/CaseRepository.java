package org.instituteatri.deep.controller.caseData;


import org.instituteatri.deep.controller.caseData.Case;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseRepository extends JpaRepository<Case, Long> {
}

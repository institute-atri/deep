package org.instituteatri.deep.controller.dto;

import org.instituteatri.deep.controller.caseData.Defendant;
import org.instituteatri.deep.controller.caseData.TypeOfCrime;

public record CaseDto(Long id,
                      TypeOfCrime typeOfCrime,
                      String date,
                      Defendant defendant,
                      String processNumber,
                      String expertReports){
}

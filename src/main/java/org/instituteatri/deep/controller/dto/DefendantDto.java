package org.instituteatri.deep.controller.dto;


import org.instituteatri.deep.controller.caseData.Adress;

public record DefendantDto(String name,
                           String dateOfBirth,
                           String idNumber,
                           Adress adress) {
}

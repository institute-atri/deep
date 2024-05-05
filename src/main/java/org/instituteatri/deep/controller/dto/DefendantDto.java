package org.instituteatri.deep.controller.dto;


import org.instituteatri.deep.controller.caseData.Address;

public record DefendantDto(String name,
                           String dateOfBirth,
                           String idNumber,
                           Address address) {
}

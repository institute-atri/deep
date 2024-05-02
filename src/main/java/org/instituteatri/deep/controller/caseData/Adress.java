package org.instituteatri.deep.controller.caseData;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Adress {
    private String streetAddress;
    private String houseNumber;
    private String neighborhood;
    private String city;
    private String zipCode;
    private String state;
}

package org.instituteatri.deep.dto.response;

import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.instituteatri.deep.model.Occurrence;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private String id;
    private String streetAddress;
    private String houseNumber;
    private String neighborhood;
    private String city;
    private String zipCode;
    private String state;

    @OneToOne
    private Occurrence occurrence;
}

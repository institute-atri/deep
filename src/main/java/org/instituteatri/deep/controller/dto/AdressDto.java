package org.instituteatri.deep.controller.dto;

public record AdressDto(String streetAddress,
                        String houseNumber,
                        String neighborhood,
                        String city,
                        String zipCode,
                        String state,
                        String fu) {
}

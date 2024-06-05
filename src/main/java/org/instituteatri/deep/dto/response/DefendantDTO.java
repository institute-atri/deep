package org.instituteatri.deep.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefendantDTO {

    private String id;
    private String name;
    private String dateOfBirth;
    private String idNumber;
}

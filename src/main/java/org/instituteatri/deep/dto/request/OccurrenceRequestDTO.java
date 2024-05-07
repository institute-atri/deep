package org.instituteatri.deep.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OccurrenceRequestDTO {

    private String name;
    private String description;
}
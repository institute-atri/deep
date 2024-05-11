package org.instituteatri.deep.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentsAndEvidencesResponseDTO {
    private String id;
    private String name;
    private String description;
    private String date;
    private String place;
    private String type;
    private String currentState;
    private String message;
    private String responsible;
}

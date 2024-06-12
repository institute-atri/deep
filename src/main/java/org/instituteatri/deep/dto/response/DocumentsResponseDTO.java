package org.instituteatri.deep.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentsResponseDTO {
    private String id;
    private String name;
    private String description;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String date;
    private String place;
    private String type;
    private String currentState;
    private String message;
    private String responsible;
}

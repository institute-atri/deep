package org.instituteatri.deep.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentsRequestDTO {

    @NotBlank
    private String id;
    @NotBlank
    private String name;
    private String description;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String date;
    private String place;
    private String type;
    private String currentState;
    private String responsible;
}

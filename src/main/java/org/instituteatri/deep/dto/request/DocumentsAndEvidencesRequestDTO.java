package org.instituteatri.deep.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentsAndEvidencesRequestDTO {
    private String id;
    private String name;
    private String description;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String date;

    private String place;
}

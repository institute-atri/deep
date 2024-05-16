package org.instituteatri.deep.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.instituteatri.deep.model.Defendant;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OccurrenceResponseDTO {

    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    private List<MessageResponseDTO> messages;
}

package org.instituteatri.deep.dto.response;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

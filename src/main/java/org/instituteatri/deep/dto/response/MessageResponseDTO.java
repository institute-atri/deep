package org.instituteatri.deep.dto.response;

import java.time.Instant;
import org.springframework.ai.ollama.api.OllamaApi.Message.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDTO {

    private Role role;
    private String content;
    private Instant createdAt;
}

package org.instituteatri.deep.dto.request;

import org.springframework.ai.ollama.api.OllamaApi.Message.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageRequestDTO {

    private Role role;
    private String content;
}

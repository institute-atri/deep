package org.instituteatri.deep.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRequestDTO {

    private String message;
}

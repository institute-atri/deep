package org.instituteatri.deep.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StandardError {
    private String message;
    private Integer status;
    private Long timestamp;
    private String path;
    private String error;
}



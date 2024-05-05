package org.instituteatri.deep.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> notFound(BadRequestException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = StandardError.builder()
                .message(e.getMessage())
                .error("Not found")
                .path(request.getRequestURI())
                .status(status.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(status).body(error);
    }
}
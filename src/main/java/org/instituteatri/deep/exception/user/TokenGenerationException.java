package org.instituteatri.deep.infrastructure.exceptions.user;

public class TokenGenerationException extends RuntimeException {

    public TokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}

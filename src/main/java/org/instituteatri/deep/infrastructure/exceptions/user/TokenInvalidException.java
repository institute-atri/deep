package org.instituteatri.deep.infrastructure.exceptions.user;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException(String token) {
        super("Token is invalid:" + token);
    }
}
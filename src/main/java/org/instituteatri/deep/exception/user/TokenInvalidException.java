package org.instituteatri.deep.exception.user;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException(String token) {
        super("Token is invalid:" + token);
    }
}
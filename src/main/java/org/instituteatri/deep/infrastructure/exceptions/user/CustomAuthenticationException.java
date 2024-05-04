package org.instituteatri.deep.infrastructure.exceptions.user;

public class CustomAuthenticationException extends RuntimeException {
    public CustomAuthenticationException() {
        super("Invalid username or password.");
    }
}


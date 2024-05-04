package org.instituteatri.deep.infrastructure.exceptions.user;

public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException() {
        super("User isn't authenticated.");
    }
}

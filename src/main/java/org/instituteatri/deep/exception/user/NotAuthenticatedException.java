package org.instituteatri.deep.exception.user;

public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException() {
        super("User isn't authenticated.");
    }
}

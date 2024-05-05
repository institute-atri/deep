package org.instituteatri.deep.infrastructure.exceptions.user;

public class UserEmailNotFoundException extends RuntimeException {
    public UserEmailNotFoundException() {
        super("User email not found in token.");
    }
}

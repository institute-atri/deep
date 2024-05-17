package org.instituteatri.deep.exception.user;

public class UserEmailNotFoundException extends RuntimeException {
    public UserEmailNotFoundException() {
        super("User email not found in token.");
    }
}

package org.instituteatri.deep.infrastructure.exceptions.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super("Could not find user with id:" + id);
    }
}

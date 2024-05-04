package org.instituteatri.deep.infrastructure.exceptions.user;

public class UserAccessDeniedException extends RuntimeException {
    public UserAccessDeniedException() {
        super("User isn't authorized.");
    }
}

package org.instituteatri.deep.exception.user;

public class UserAccessDeniedException extends RuntimeException {
    public UserAccessDeniedException() {
        super("User isn't authorized.");
    }
}

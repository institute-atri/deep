package org.instituteatri.deep.exception.user;

public class PasswordsNotMatchException extends RuntimeException {
    public PasswordsNotMatchException() {
        super("The passwords do not match.");
    }
}

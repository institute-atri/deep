package org.instituteatri.deep.infrastructure.exceptions.user;

public class PasswordsNotMatchException extends RuntimeException {
    public PasswordsNotMatchException() {
        super("The passwords do not match.");
    }
}

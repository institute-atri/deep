package org.instituteatri.deep.infrastructure.exceptions.user;

public class EmailAlreadyExistsException  extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("E-mail not available.");
    }
}

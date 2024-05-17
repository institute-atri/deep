package org.instituteatri.deep.exception.user;

public class EmailAlreadyExistsException  extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("E-mail not available.");
    }
}

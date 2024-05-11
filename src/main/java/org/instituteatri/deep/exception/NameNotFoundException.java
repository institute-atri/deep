package org.instituteatri.deep.exception;

public class NameNotFoundException extends RuntimeException{
    public NameNotFoundException() {
        super("Name not found in the database. Please enter a valid name.");
    }
}

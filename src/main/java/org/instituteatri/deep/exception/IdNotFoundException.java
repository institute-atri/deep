package org.instituteatri.deep.exception;

public class IdNotFoundException extends RuntimeException{
    public IdNotFoundException() {
        super("Id not found in the database. Please enter a valid id.");
    }
}

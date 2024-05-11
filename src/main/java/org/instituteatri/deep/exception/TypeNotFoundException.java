package org.instituteatri.deep.exception;

public class TypeNotFoundException extends RuntimeException{
    public TypeNotFoundException() {
        super("Type not found in the database. Please enter a valid type.");
    }
}

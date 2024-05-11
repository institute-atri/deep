package org.instituteatri.deep.exception;

public class DateNotFoundException extends RuntimeException{
    public DateNotFoundException() {
        super("Date not found in the database. Please enter a valid date.");
    }

}

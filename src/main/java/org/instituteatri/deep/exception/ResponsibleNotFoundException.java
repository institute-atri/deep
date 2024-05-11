package org.instituteatri.deep.exception;

public class ResponsibleNotFoundException extends RuntimeException{
    public ResponsibleNotFoundException() {
        super("Responsible not found in the database. Please enter a valid responsible.");
    }
}

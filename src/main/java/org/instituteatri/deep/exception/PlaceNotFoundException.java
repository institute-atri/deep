package org.instituteatri.deep.exception;

public class PlaceNotFoundException extends RuntimeException{
    public PlaceNotFoundException() {
        super("Place not found in the database. Please enter a valid place.");
    }

}

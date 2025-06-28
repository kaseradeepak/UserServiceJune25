package com.scaler.userservicejune25.exceptions;

public class InvalidTokenException extends Exception{
    private String message;

    public InvalidTokenException(String message) {
        super(message);
    }
}

package com.ecobite.supplier_service.exception;

public class InvalidRatingException extends RuntimeException{
    public InvalidRatingException(String message) {
        super(message);
    }
}

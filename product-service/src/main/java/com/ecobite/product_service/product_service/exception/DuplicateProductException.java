package com.ecobite.product_service.product_service.exception;

public class DuplicateProductException extends RuntimeException{
    public DuplicateProductException(String message) {
        super(message);
    }
}

package com.ecobite.product_service.product_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleNotFound(
            ResourceNotFoundException ex){

        Map<String,String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND
        );
    }

    // DTO validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return new ResponseEntity<>(
                errors,
                HttpStatus.BAD_REQUEST
        );
    }

    //Business validation errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(
            RuntimeException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    // Unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(
            Exception ex) {

        ex.printStackTrace();

        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal server error");

        return new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<Map<String, String>>
    handleDuplicateProduct(DuplicateProductException ex) {

        Map<String, String> error = new HashMap<>();

        error.put("error", ex.getMessage());

        return new ResponseEntity<>(
                error,
                HttpStatus.CONFLICT
        );
    }
}

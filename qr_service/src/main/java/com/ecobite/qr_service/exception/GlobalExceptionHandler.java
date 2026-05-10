package com.ecobite.qr_service.exception;

import com.ecobite.qr_service.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(
            ResourceNotFoundException.class
    )
    public ResponseEntity<ApiResponse<?>>
    handleNotFound(
            ResourceNotFoundException ex
    ){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        new ApiResponse<>(
                                404,
                                ex.getMessage(),
                                null,
                                LocalDateTime.now()
                        )
                );
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ApiResponse<?>>
    handleValidation(
            MethodArgumentNotValidException ex
    ){

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity.badRequest()
                .body(
                        new ApiResponse<>(
                                400,
                                "Validation failed",
                                errors,
                                LocalDateTime.now()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>>
    handleGlobal(Exception ex){

        return ResponseEntity.status(
                        HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ApiResponse<>(
                                500,
                                ex.getMessage(),
                                null,
                                LocalDateTime.now()
                        )
                );
    }

    @ExceptionHandler(
            InvalidRequestException.class
    )
    public ResponseEntity<ApiResponse<?>>
    handleInvalidRequest(
            InvalidRequestException ex
    ){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new ApiResponse<>(
                                400,
                                ex.getMessage(),
                                null,
                                LocalDateTime.now()
                        )
                );
    }

    @ExceptionHandler(
            QrGenerationException.class
    )
    public ResponseEntity<ApiResponse<?>>
    handleQrGeneration(
            QrGenerationException ex
    ){

        return ResponseEntity.status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(
                        new ApiResponse<>(
                                500,
                                ex.getMessage(),
                                null,
                                LocalDateTime.now()
                        )
                );
    }
}

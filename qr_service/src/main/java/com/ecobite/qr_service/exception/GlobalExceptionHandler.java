package com.ecobite.qr_service.exception;

import com.ecobite.qr_service.dto.response.ApiResponse;
import feign.FeignException;
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
    // Resource Not Found
    @ExceptionHandler(
            ResourceNotFoundException.class
    )
    public ResponseEntity<ApiResponse<?>>
    handleNotFound(
            ResourceNotFoundException ex
    ){

        return ResponseEntity.status(
                        HttpStatus.NOT_FOUND
                )
                .body(
                        new ApiResponse<>(
                                404,
                                ex.getMessage(),
                                null,
                                LocalDateTime.now()
                        )
                );
    }

    // Validation Errors
    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ApiResponse<?>>
    handleValidation(
            MethodArgumentNotValidException ex
    ){

        Map<String, String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity.status(
                        HttpStatus.BAD_REQUEST
                )
                .body(
                        new ApiResponse<>(
                                400,
                                "Validation failed",
                                errors,
                                LocalDateTime.now()
                        )
                );
    }

    // Invalid Request
    @ExceptionHandler(
            InvalidRequestException.class
    )
    public ResponseEntity<ApiResponse<?>>
    handleInvalidRequest(
            InvalidRequestException ex
    ){

        return ResponseEntity.status(
                        HttpStatus.BAD_REQUEST
                )
                .body(
                        new ApiResponse<>(
                                400,
                                ex.getMessage(),
                                null,
                                LocalDateTime.now()
                        )
                );
    }

    // QR Generation Errors
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

    // Feign Client Errors
    @ExceptionHandler(
            FeignException.class
    )
    public ResponseEntity<ApiResponse<?>>
    handleFeignException(
            FeignException ex
    ){

        String message =
                "External service error";

        HttpStatus status =
                HttpStatus.INTERNAL_SERVER_ERROR;

        if(ex.status() == 404){

            message = "Batch not found";
            status = HttpStatus.NOT_FOUND;

        } else if(ex.status() == 503){

            message =
                    "Batch service unavailable";

            status =
                    HttpStatus.SERVICE_UNAVAILABLE;
        }

        return ResponseEntity.status(status)
                .body(
                        new ApiResponse<>(
                                status.value(),
                                message,
                                null,
                                LocalDateTime.now()
                        )
                );
    }

    // Global Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>>
    handleGlobal(
            Exception ex
    ){

        ex.printStackTrace();

        return ResponseEntity.status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(
                        new ApiResponse<>(
                                500,
                                "Unexpected server error",
                                null,
                                LocalDateTime.now()
                        )
                );
    }
}

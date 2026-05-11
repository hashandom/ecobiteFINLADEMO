package com.ecobite.dashboard_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(
            DashboardNotFoundException.class
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDashboardNotFound(
            DashboardNotFoundException ex
    ) {

        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(
            ServiceCommunicationException.class
    )
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleServiceCommunication(
            ServiceCommunicationException ex
    ) {

        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .timestamp(LocalDateTime.now())
                .build();
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(
            HttpStatus.INTERNAL_SERVER_ERROR
    )
    public ErrorResponse handleGenericException(
            Exception ex
    ) {

        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
                .timestamp(LocalDateTime.now())
                .build();
    }
}

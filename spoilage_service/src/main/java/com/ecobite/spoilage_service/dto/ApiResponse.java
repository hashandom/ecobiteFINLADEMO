package com.ecobite.spoilage_service.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Request Successful")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

package com.ecobite.qr_service.dto.response;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}

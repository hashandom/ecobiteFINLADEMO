package com.ecobite.dashboard_service.exception;

public class DashboardNotFoundException extends RuntimeException {
    public DashboardNotFoundException(String message) {
        super(message);
    }
}

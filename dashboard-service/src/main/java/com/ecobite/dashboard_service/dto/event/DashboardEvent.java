package com.ecobite.dashboard_service.dto.event;

import lombok.Data;

@Data
public class DashboardEvent {
    private String type;
    private String message;
}

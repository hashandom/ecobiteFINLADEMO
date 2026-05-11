package com.ecobite.dashboard_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthlyTrendResponse {

    private String month;
    private Double sales;
    private Long orders;
    private Long notifications;
}

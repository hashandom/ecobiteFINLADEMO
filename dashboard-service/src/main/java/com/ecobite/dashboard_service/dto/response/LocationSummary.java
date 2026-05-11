package com.ecobite.dashboard_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationSummary {
    private Long warehouseCount;
}

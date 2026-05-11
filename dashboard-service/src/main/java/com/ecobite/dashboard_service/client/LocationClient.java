package com.ecobite.dashboard_service.client;

import com.ecobite.dashboard_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "location-service", configuration = FeignConfig.class)
public interface LocationClient {
    @GetMapping("/locations/warehouses/count")
    Long getWarehouseCount();
}

package com.ecobite.dashboard_service.client;

import com.ecobite.dashboard_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "reorder-service", configuration = FeignConfig.class)
public interface ReorderClient {
    @GetMapping("/reorders/pending/count")
    Long getPendingReorders();
}

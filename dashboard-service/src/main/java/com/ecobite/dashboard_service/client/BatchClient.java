package com.ecobite.dashboard_service.client;

import com.ecobite.dashboard_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "batch-service", configuration = FeignConfig.class)
public interface BatchClient {
    @GetMapping("/api/batches/count")
    Long getBatchCount();

    @GetMapping("/api/batches/expiring/count")
    Long getExpiringSoonCount();
}

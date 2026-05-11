package com.ecobite.dashboard_service.client;

import com.ecobite.dashboard_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "supplier-service", configuration = FeignConfig.class)
public interface SupplierClient {
    @GetMapping("/api/suppliers/count")
    Long getSupplierCount();
}

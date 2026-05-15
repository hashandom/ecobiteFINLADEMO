package com.ecobite.dashboard_service.client;

import com.ecobite.dashboard_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductClient {
    @GetMapping("/products/count")
    Long getProductCount();

    @GetMapping("/products/low-stock/count")
    Long getLowStockCount();
}

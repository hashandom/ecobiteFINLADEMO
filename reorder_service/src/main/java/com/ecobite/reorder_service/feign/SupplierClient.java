package com.ecobite.reorder_service.feign;

import com.ecobite.reorder_service.DTOs.response.SupplierResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "supplier-service" , configuration = FeignConfig.class)
public interface SupplierClient {
    @GetMapping("/suppliers/{id}")
    SupplierResponse getSupplier(@PathVariable("id") Long id);
}

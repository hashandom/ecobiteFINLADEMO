package com.ecobite.reorder_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "supplier-service")
public class SupplierClient {
    @GetMapping("/suppliers/{id}")
    SupplierResponse getSupplier(@PathVariable Long id);

    class SupplierResponse {
        public Long id;
        public String name;
        public String email;
    }
}

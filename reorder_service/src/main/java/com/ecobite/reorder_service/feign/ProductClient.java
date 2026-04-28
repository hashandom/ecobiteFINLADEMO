package com.ecobite.reorder_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "product-service")
public class ProductClient {
    @GetMapping("/products/{id}")
    ProductResponse getProduct(@PathVariable Long id);

    class ProductResponse {
        public Long id;
        public String name;
        public int stock;
        public int reorderLevel;
        public Long supplierId;
    }
}

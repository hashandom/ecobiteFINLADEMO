package com.ecobite.supplier_service.feign;


import com.ecobite.supplier_service.dtos.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", configuration = com.ecobite.supplier_service.feign.FeignConfig.class)
public interface ProductClient {
    @GetMapping("/products/{id}")
    ProductResponseDTO getProduct(@PathVariable("id") String id);
}

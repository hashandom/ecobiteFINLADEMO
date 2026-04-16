package com.ecobite.batch_service.feign;

import com.ecobite.batch_service.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductClient {
    @GetMapping("/products/{id}")
    ProductResponse getProduct(@PathVariable("id") String id);
}

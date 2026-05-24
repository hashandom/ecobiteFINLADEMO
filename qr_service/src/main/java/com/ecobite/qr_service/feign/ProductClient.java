package com.ecobite.qr_service.feign;

import com.ecobite.qr_service.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductClient {
    @GetMapping("/products/{productId}")
    ProductResponse getProduct(
            @PathVariable String productId
    );
}

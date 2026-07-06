package com.ecobite.reorder_service.feign;

import com.ecobite.reorder_service.DTOs.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product-service" , configuration = FeignConfig.class)

public interface  ProductClient {
    @GetMapping("/products/{id}")
    ProductResponse getProduct(@PathVariable("id") String id);

    @GetMapping("/products")
    List<ProductResponse> getAllProducts();

    @GetMapping("/products/low-stock")
    List<ProductResponse> getLowStockProducts();
}

package com.ecobite.batch_service.feign;

import com.ecobite.batch_service.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductClient {
    @GetMapping("/products/{id}")
    ProductResponse getProduct(@PathVariable("id") String id);

    @PutMapping("/products/update-stock/{id}")
    ProductResponse updateStock(
            @PathVariable String id,
            @RequestParam int stock
    );

//    @PutMapping("/products/add-stock/{id}")
//    void addStock(
//            @PathVariable("id") String id,
//            @RequestParam("quantity") int quantity
//    );
//
//    @PutMapping("/products/deduct-stock/{id}")
//    void deductStock(
//            @PathVariable("id") String id,
//            @RequestParam("quantity") int quantity
//    );
}

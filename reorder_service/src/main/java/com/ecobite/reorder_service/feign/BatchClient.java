package com.ecobite.reorder_service.feign;

import com.ecobite.reorder_service.DTOs.response.BatchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "batch-service", configuration = FeignConfig.class)
public interface BatchClient {
    @GetMapping("/batches/product/{productId}")
    List<BatchResponse> getBatchByProductId(@PathVariable("productId") String productId);

    @GetMapping("/batches/supplier/{productId}")
    BatchResponse getSupplierForProduct(
            @PathVariable String productId
    );

}

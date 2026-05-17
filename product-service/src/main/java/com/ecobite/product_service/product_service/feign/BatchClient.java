package com.ecobite.product_service.product_service.feign;

import com.ecobite.product_service.product_service.dto.BatchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "batch-service", configuration = FeignConfig.class)
public interface BatchClient {
    @GetMapping("/batches/product/{productId}")
    List<BatchResponse> getBatchesByProduct(
            @PathVariable String productId
    );

}

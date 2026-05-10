package com.ecobite.qr_service.feign;

import com.ecobite.qr_service.dto.response.BatchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "batch-service", configuration = FeignConfig.class)
public interface BatchClient {
    @GetMapping("/batches/{batchId}")
    BatchResponse getBatch(
            @PathVariable String batchId
    );

}

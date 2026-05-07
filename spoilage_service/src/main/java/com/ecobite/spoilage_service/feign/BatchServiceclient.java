package com.ecobite.spoilage_service.feign;

import com.ecobite.spoilage_service.dto.BatchResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "batch-service")
public interface BatchServiceclient {

    @GetMapping("/batches/{id}")
    BatchResponseDTO getBatchById(@PathVariable Long id);
}

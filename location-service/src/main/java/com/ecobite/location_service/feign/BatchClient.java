package com.ecobite.location_service.feign;


import com.ecobite.location_service.DTO.BatchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "batch-service", configuration =FeignConfig.class)
public interface BatchClient {
    @GetMapping("/batches/{id}")
    BatchResponse getBatch(@PathVariable("id") Long id);
}

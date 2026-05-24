package com.ecobite.batch_service.feign;


import com.ecobite.batch_service.dto.response.LocationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "location-service", configuration = FeignConfig.class)
public interface LocationClient {
    @GetMapping("/location/{id}")
    LocationResponse getLocation(
            @PathVariable Long id
    );
}

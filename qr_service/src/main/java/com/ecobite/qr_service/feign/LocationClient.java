package com.ecobite.qr_service.feign;

import com.ecobite.qr_service.dto.response.LocationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "location-service", configuration = FeignConfig.class)
public interface LocationClient {
    @GetMapping("/locations/{locationId}")
    LocationResponse getLocation(
            @PathVariable Long locationId
    );
}

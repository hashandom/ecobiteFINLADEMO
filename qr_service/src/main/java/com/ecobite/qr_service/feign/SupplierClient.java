package com.ecobite.qr_service.feign;
import com.ecobite.qr_service.dto.response.SupplierResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "supplier-service", configuration = FeignConfig.class)
public interface SupplierClient {
    @GetMapping("/suppliers/{supplierId}")
    SupplierResponse getSupplier(
            @PathVariable Long supplierId
    );
}

package com.ecobite.qr_service.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface BatchClient {
    @GetMapping("/batches/{batchId}")
    BatchResponse getBatch(
            @PathVariable String batchId
    );

}

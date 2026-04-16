package com.ecobite.batch_service.service;

import com.ecobite.batch_service.dto.BatchRequestDTO;
import com.ecobite.batch_service.dto.BatchResponseDTO;
import com.ecobite.batch_service.dto.request.CreateBatchRequest;
import com.ecobite.batch_service.dto.request.ReduceStockRequest;
import com.ecobite.batch_service.dto.response.BatchResponse;

import java.util.List;

public interface BatchService {

    BatchResponse createBatch(CreateBatchRequest request);

    BatchResponse getBatchById(Long id);

    List<BatchResponse> getBatchesByProduct(String productId);

    BatchResponse reduceStock(Long id, ReduceStockRequest request);

    List<BatchResponse> getExpiringSoon(int days);

    List<BatchResponse> getAvailableBatches(String productId);

    BatchResponse spoilBatch(Long id);

    BatchResponse recallBatch(Long id);

}

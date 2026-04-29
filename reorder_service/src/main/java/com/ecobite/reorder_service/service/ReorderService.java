package com.ecobite.reorder_service.service;

import com.ecobite.reorder_service.DTOs.request.ReorderRequest;
import com.ecobite.reorder_service.DTOs.response.ReorderResponse;

public interface ReorderService {
    ReorderResponse createReorder(ReorderRequest request);

}

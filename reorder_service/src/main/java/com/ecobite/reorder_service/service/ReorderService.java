package com.ecobite.reorder_service.service;

import com.ecobite.reorder_service.DTOs.request.ReorderRequest;
import com.ecobite.reorder_service.DTOs.response.LowStockSupplierResponse;
import com.ecobite.reorder_service.DTOs.response.ReorderResponse;

import java.util.List;

public interface ReorderService {
    ReorderResponse createReorder(ReorderRequest request);

    Long getPendingReordersCount();

    List<LowStockSupplierResponse>  getLowStockWithSuppliers();
}

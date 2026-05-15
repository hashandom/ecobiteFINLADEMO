package com.ecobite.supplier_service.service;

import com.ecobite.supplier_service.dtos.AssignProductRequestDTO;
import com.ecobite.supplier_service.dtos.ProductResponseDTO;
import com.ecobite.supplier_service.dtos.SupplierRequestDTO;
import com.ecobite.supplier_service.dtos.SupplierResponseDTO;

import java.util.List;

public interface SupplierService {
    SupplierResponseDTO createSupplier(SupplierRequestDTO dto);

    SupplierResponseDTO getSupplier(Long id);

    void assignProduct(AssignProductRequestDTO dto);

    List<ProductResponseDTO> getProductsBySupplier(Long supplierId);

    List<SupplierResponseDTO> getAllSuppliers();

    SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO dto);

    void deleteSupplier(Long id);

    Long getSupplierCount();
}

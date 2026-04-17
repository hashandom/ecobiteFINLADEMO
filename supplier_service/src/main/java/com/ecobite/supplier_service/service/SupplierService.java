package com.ecobite.supplier_service.service;

import com.ecobite.supplier_service.DTOs.AssignProductRequestDTO;
import com.ecobite.supplier_service.DTOs.ProductResponseDTO;
import com.ecobite.supplier_service.DTOs.SupplierRequestDTO;
import com.ecobite.supplier_service.DTOs.SupplierResponseDTO;
import com.ecobite.supplier_service.entity.Supplier;

import java.util.List;

public interface SupplierService {
    SupplierResponseDTO createSupplier(SupplierRequestDTO dto);

    SupplierResponseDTO getSupplier(Long id);

    void assignProduct(AssignProductRequestDTO dto);

    List<ProductResponseDTO> getProductsBySupplier(Long supplierId);

    List<SupplierResponseDTO> getAllSuppliers();

    SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO dto);

    void deleteSupplier(Long id);

}

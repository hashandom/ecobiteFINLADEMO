package com.ecobite.supplier_service.service;

import com.ecobite.supplier_service.DTOs.AssignProductRequestDTO;
import com.ecobite.supplier_service.DTOs.ProductResponseDTO;
import com.ecobite.supplier_service.DTOs.SupplierRequestDTO;
import com.ecobite.supplier_service.DTOs.SupplierResponseDTO;
import com.ecobite.supplier_service.entity.Supplier;
import com.ecobite.supplier_service.entity.SupplierProduct;
import com.ecobite.supplier_service.feign.ProductClient;
import com.ecobite.supplier_service.repository.SupplierProductRepository;
import com.ecobite.supplier_service.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierProductRepository supplierProductRepository;
    private final ProductClient productClient;

    @Override
    public SupplierResponseDTO createSupplier(SupplierRequestDTO dto) {

        Supplier supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setContactEmail(dto.getContactEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setRating(0.0);

        supplierRepository.save(supplier);

        return mapToResponse(supplier);
    }

    @Override
    public SupplierResponseDTO getSupplier(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        return mapToResponse(supplier);
    }

    @Override
    public void assignProduct(AssignProductRequestDTO dto) {

        System.out.println("Calling product-service for ID: " + dto.getProductId());
        ProductResponseDTO product =
                productClient.getProduct(dto.getProductId());

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        SupplierProduct mapping = new SupplierProduct();
        mapping.setSupplierId(dto.getSupplierId());
        mapping.setProductId(dto.getProductId());

        supplierProductRepository.save(mapping);
    }

    @Override
    public List<ProductResponseDTO> getProductsBySupplier(Long supplierId) {

        List<SupplierProduct> mappings =
                supplierProductRepository.findBySupplierId(supplierId);

        return mappings.stream()
                .map(m -> productClient.getProduct(m.getProductId()))
                .toList();
    }

    @Override
    public List<SupplierResponseDTO> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();

        return suppliers.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO dto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        // Update fields
        supplier.setName(dto.getName());
        supplier.setContactEmail(dto.getContactEmail());
        supplier.setPhone(dto.getPhone());

        // Save updated supplier
        supplierRepository.save(supplier);

        return mapToResponse(supplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        // 🔥 IMPORTANT: delete mappings first (avoid FK issues)
        supplierProductRepository.deleteAllBySupplierId(id);

        supplierRepository.delete(supplier);
    }

    private SupplierResponseDTO mapToResponse(Supplier supplier) {
        return new SupplierResponseDTO(
                supplier.getId(),
                supplier.getName(),
                supplier.getContactEmail(),
                supplier.getPhone(),
                supplier.getRating()
        );
    }
}

package com.ecobite.supplier_service.service;

import com.ecobite.supplier_service.dtos.AssignProductRequestDTO;
import com.ecobite.supplier_service.dtos.ProductResponseDTO;
import com.ecobite.supplier_service.dtos.SupplierRequestDTO;
import com.ecobite.supplier_service.dtos.SupplierResponseDTO;
import com.ecobite.supplier_service.dtos.event.SupplierEvent;
import com.ecobite.supplier_service.kafka.SupplierEventProducer;
import com.ecobite.supplier_service.entity.Supplier;
import com.ecobite.supplier_service.entity.SupplierProduct;
import com.ecobite.supplier_service.feign.ProductClient;
import com.ecobite.supplier_service.repository.SupplierProductRepository;
import com.ecobite.supplier_service.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ecobite.supplier_service.exception.DuplicateResourceException;
import com.ecobite.supplier_service.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierProductRepository supplierProductRepository;
    private final ProductClient productClient;
    private final SupplierEventProducer producer;

    @Override
    public SupplierResponseDTO createSupplier(SupplierRequestDTO dto) {
        if (supplierRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException(
                    "Supplier name already exists"
            );
        }
        if (supplierRepository.existsByContactEmail(dto.getContactEmail())) {
            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }
        if (supplierRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicateResourceException(
                    "Phone number already exists"
            );
        }

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
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found"
                        )
                );

        return mapToResponse(supplier);
    }

    @Override
    public void assignProduct(AssignProductRequestDTO dto) {
        System.out.println("Calling product-service for ID: " + dto.getProductId());
        ProductResponseDTO product =
                productClient.getProduct(dto.getProductId());

        if (product == null) {
            throw new ResourceNotFoundException(
                    "Product not found"
            );
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
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found"
                        )
                );

        // Duplicate checks

        if (!supplier.getName().equals(dto.getName())
                && supplierRepository.existsByName(dto.getName())) {

            throw new DuplicateResourceException(
                    "Supplier name already exists"
            );
        }

        if (!supplier.getContactEmail().equals(dto.getContactEmail())
                && supplierRepository.existsByContactEmail(dto.getContactEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        if (!supplier.getPhone().equals(dto.getPhone())
                && supplierRepository.existsByPhone(dto.getPhone())) {

            throw new DuplicateResourceException(
                    "Phone number already exists"
            );
        }

        supplier.setName(dto.getName());
        supplier.setContactEmail(dto.getContactEmail());
        supplier.setPhone(dto.getPhone());

        supplierRepository.save(supplier);

        return mapToResponse(supplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found"
                        )
                );

        supplierProductRepository.deleteAllBySupplierId(id);
        supplierRepository.delete(supplier);
    }

    @Override
    public Long getSupplierCount() {
        return supplierRepository.count();
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

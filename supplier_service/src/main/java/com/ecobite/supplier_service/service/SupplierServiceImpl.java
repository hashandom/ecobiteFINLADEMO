package com.ecobite.supplier_service.service;

import com.ecobite.supplier_service.dtos.AssignProductRequestDTO;
import com.ecobite.supplier_service.dtos.ProductResponseDTO;
import com.ecobite.supplier_service.dtos.SupplierRequestDTO;
import com.ecobite.supplier_service.dtos.SupplierResponseDTO;
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



    // =========================
    // CREATE SUPPLIER
    // =========================

    @Override
    public SupplierResponseDTO createSupplier(
            SupplierRequestDTO dto
    ) {

        // Duplicate validations

        if (supplierRepository.existsByName(dto.getName())) {

            throw new DuplicateResourceException(
                    "Supplier name already exists"
            );
        }

        if (supplierRepository.existsByContactEmail(
                dto.getContactEmail()
        )) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        if (supplierRepository.existsByPhone(
                dto.getPhone()
        )) {

            throw new DuplicateResourceException(
                    "Phone number already exists"
            );
        }

        // Create supplier

        Supplier supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setContactEmail(
                dto.getContactEmail()
        );

        supplier.setPhone(dto.getPhone());

        // Internal system field

        supplier.setRating(0.0);
        supplierRepository.save(supplier);
        return mapToResponse(supplier);
    }

    // =========================
    // GET SUPPLIER BY ID
    // =========================

    @Override
    public SupplierResponseDTO getSupplier(Long id) {

        Supplier supplier = supplierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found"
                        )
                );

        return mapToResponse(supplier);
    }

    // =========================
    // ASSIGN PRODUCT
    // =========================

    @Override
    public void assignProduct(
            AssignProductRequestDTO dto
    ) {

        // Validate supplier exists

        supplierRepository.findById(
                        dto.getSupplierId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found"
                        )
                );

        // Validate product exists

        try {

            ProductResponseDTO product =
                    productClient.getProduct(
                            dto.getProductId()
                    );

            if (product == null) {

                throw new ResourceNotFoundException(
                        "Product not found"
                );
            }

        } catch (Exception e) {

            throw new ResourceNotFoundException(
                    "Product not found"
            );
        }

        // Prevent duplicate assignment

        boolean alreadyAssigned =
                supplierProductRepository
                        .existsBySupplierIdAndProductId(
                                dto.getSupplierId(),
                                dto.getProductId()
                        );

        if (alreadyAssigned) {

            throw new DuplicateResourceException(
                    "Product already assigned to supplier"
            );
        }

        // Save mapping

        SupplierProduct mapping =
                new SupplierProduct();

        mapping.setSupplierId(
                dto.getSupplierId()
        );

        mapping.setProductId(
                dto.getProductId()
        );

        supplierProductRepository.save(mapping);
    }

    // =========================
    // GET PRODUCTS BY SUPPLIER
    // =========================

    @Override
    public List<ProductResponseDTO>
    getProductsBySupplier(Long supplierId) {

        // Validate supplier exists

        supplierRepository.findById(supplierId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found"
                        )
                );

        List<SupplierProduct> mappings =
                supplierProductRepository
                        .findBySupplierId(supplierId);

        return mappings.stream()

                .map(mapping -> {

                    try {

                        return productClient.getProduct(
                                mapping.getProductId()
                        );

                    } catch (Exception e) {

                        return null;
                    }
                })

                .filter(product -> product != null)

                .toList();
    }

    // =========================
    // GET ALL SUPPLIERS
    // =========================

    @Override
    public List<SupplierResponseDTO>
    getAllSuppliers() {

        List<Supplier> suppliers =
                supplierRepository.findAll();

        return suppliers.stream()

                .map(this::mapToResponse)

                .toList();
    }

    // =========================
    // UPDATE SUPPLIER
    // =========================

    @Override
    public SupplierResponseDTO updateSupplier(
            Long id,
            SupplierRequestDTO dto
    ) {

        Supplier supplier =
                supplierRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Supplier not found"
                                )
                        );

        // Duplicate validations

        if (!supplier.getName().equals(dto.getName())

                && supplierRepository.existsByName(
                dto.getName()
        )) {

            throw new DuplicateResourceException(
                    "Supplier name already exists"
            );
        }

        if (!supplier.getContactEmail()
                .equals(dto.getContactEmail())

                && supplierRepository
                .existsByContactEmail(
                        dto.getContactEmail()
                )) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        if (!supplier.getPhone()
                .equals(dto.getPhone())

                && supplierRepository
                .existsByPhone(dto.getPhone())) {

            throw new DuplicateResourceException(
                    "Phone number already exists"
            );
        }

        // Update values

        supplier.setName(dto.getName());

        supplier.setContactEmail(
                dto.getContactEmail()
        );

        supplier.setPhone(dto.getPhone());

        supplierRepository.save(supplier);

        return mapToResponse(supplier);
    }

    // =========================
    // DELETE SUPPLIER
    // =========================

    @Override
    public void deleteSupplier(Long id) {

        Supplier supplier =
                supplierRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Supplier not found"
                                )
                        );

        // Delete supplier-product mappings

        supplierProductRepository
                .deleteAllBySupplierId(id);

        // Delete supplier

        supplierRepository.delete(supplier);
    }

    // =========================
    // SUPPLIER COUNT
    // =========================

    @Override
    public Long getSupplierCount() {

        return supplierRepository.count();
    }

    @Override
    public SupplierResponseDTO getBestSupplierForProduct(String productId) {
        // STEP 1
        // Find all supplier-product mappings
        List<SupplierProduct> mappings =
                supplierProductRepository
                        .findByProductId(productId);

        // STEP 2
        // Check if suppliers exist

        if (mappings.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No suppliers found for product"
            );
        }

        // STEP 3
        // Variables to track best supplier
        Supplier bestSupplier = null;
        double highestRating = -1;

        // STEP 4
        // Loop through all suppliers

        for (SupplierProduct mapping : mappings) {
            Supplier supplier =
                    supplierRepository
                            .findById(mapping.getSupplierId())
                            .orElse(null);

            // STEP 5
            // Compare ratings
            if (supplier != null
                    && supplier.getRating() > highestRating) {
                highestRating = supplier.getRating();
                bestSupplier = supplier;
            }
        }

        // STEP 6
        // Final validation
        if (bestSupplier == null) {
            throw new ResourceNotFoundException(
                    "No valid supplier found"
            );
        }

        // STEP 7
        // Return best supplier
        return mapToResponse(bestSupplier);
    }

    @Override
    public SupplierResponseDTO updateSupplierRating(Long supplierId, Double rating) {
        Supplier supplier =
                supplierRepository.findById(supplierId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Supplier not found"
                                )
                        );

        // validation

        if (rating < 0 || rating > 5) {
            throw new RuntimeException(
                    "Rating must be between 0 and 5"
            );
        }

        supplier.setRating(rating);
        supplierRepository.save(supplier);
        return mapToResponse(supplier);
    }

    // =========================
    // DTO MAPPING
    // =========================

    private SupplierResponseDTO mapToResponse(
            Supplier supplier
    ) {

        return new SupplierResponseDTO(

                supplier.getId(),
                supplier.getName(),
                supplier.getContactEmail(),
                supplier.getPhone(),
                supplier.getRating()
        );
    }
}

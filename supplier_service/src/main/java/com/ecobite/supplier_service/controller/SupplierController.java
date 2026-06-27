package com.ecobite.supplier_service.controller;

import com.ecobite.supplier_service.dtos.*;
import com.ecobite.supplier_service.service.SupplierService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public SupplierResponseDTO create(
            @Valid @RequestBody SupplierRequestDTO dto) {

        return supplierService.createSupplier(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}")
    public SupplierResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequestDTO dto) {

        return supplierService.updateSupplier(id, dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        supplierService.deleteSupplier(id);

        return "Supplier deleted successfully";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping("/assign-product")
    public String assignProduct(
            @RequestBody AssignProductRequestDTO dto) {

        supplierService.assignProduct(dto);

        return "Product assigned to supplier";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}/rating")
    public SupplierResponseDTO updateRating(
            @PathVariable Long id,
            @RequestBody UpdateSupplierRatingDTO dto) {

        return supplierService.updateSupplierRating(
                id,
                dto.getRating()
        );
    }


    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping("/count")
    public Long getSupplierCount() {
        return supplierService.getSupplierCount();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping("/{id}")
    public SupplierResponseDTO get(
            @PathVariable Long id) {

        return supplierService.getSupplier(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping
    public List<SupplierResponseDTO> getAll() {
        return supplierService.getAllSuppliers();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping("/{id}/products")
    public List<ProductResponseDTO> getProducts(
            @PathVariable Long id) {

        return supplierService.getProductsBySupplier(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    @GetMapping("/best/{productId}")
    public SupplierResponseDTO getBestSupplier(
            @PathVariable String productId) {

        return supplierService
                .getBestSupplierForProduct(productId);
    }
}

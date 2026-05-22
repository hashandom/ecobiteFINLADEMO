package com.ecobite.supplier_service.controller;

import com.ecobite.supplier_service.dtos.*;
import com.ecobite.supplier_service.service.SupplierService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @PostMapping
    public SupplierResponseDTO create(
            @Valid @RequestBody SupplierRequestDTO dto
    ) {
        return supplierService.createSupplier(dto);
    }

    @GetMapping("/count")
    public Long getSupplierCount() {
        return supplierService.getSupplierCount();
    }

    @GetMapping("/{id}")
    public SupplierResponseDTO get(@PathVariable Long id) {
        return supplierService.getSupplier(id);
    }

    @GetMapping
    public List<SupplierResponseDTO> getAll() {
        return supplierService.getAllSuppliers();
    }

    @PutMapping("/{id}")
    public SupplierResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequestDTO dto
    ) {
        return supplierService.updateSupplier(id, dto);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        supplierService.deleteSupplier(id);

        return "Supplier deleted successfully";
    }

    @PostMapping("/assign-product")
    public String assignProduct(
            @RequestBody AssignProductRequestDTO dto
    ) {

        supplierService.assignProduct(dto);

        return "Product assigned to supplier";
    }

    @GetMapping("/{id}/products")
    public List<ProductResponseDTO> getProducts(
            @PathVariable Long id
    ) {
        return supplierService.getProductsBySupplier(id);
    }

    @GetMapping("/best/{productId}")
    public SupplierResponseDTO getBestSupplier(
            @PathVariable String productId
    ) {

        return supplierService
                .getBestSupplierForProduct(productId);
    }

    @PutMapping("/{id}/rating")
    public SupplierResponseDTO updateRating(
            @PathVariable Long id,
            @RequestBody UpdateSupplierRatingDTO dto
    ) {

        return supplierService.updateSupplierRating(
                id,
                dto.getRating()
        );
    }
}

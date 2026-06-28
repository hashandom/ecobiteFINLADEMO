package com.ecobite.product_service.product_service.controller;

import com.ecobite.product_service.product_service.dto.ProductRequest;
import com.ecobite.product_service.product_service.dto.ProductResponse;
import com.ecobite.product_service.product_service.entity.Product;
import com.ecobite.product_service.product_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;


//    @PutMapping("/add-stock/{id}")
//    public ProductResponse addStock(
//            @PathVariable String id,
//            @RequestParam int quantity) {
//
//        return service.addStock(id, quantity);
//    }

//    @PutMapping("/deduct-stock/{id}")
//    public ProductResponse deductStock(
//            @PathVariable String id,
//            @RequestParam int quantity) {
//        return service.deductStock(id, quantity);
//    }



    public ProductController(ProductService service) {
        this.service = service;
    }

    // ================= WRITE OPERATIONS =================

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('PRODUCT_CREATE')"
    )
    @PostMapping
    public ProductResponse createProduct(
            @Valid @RequestBody ProductRequest request) {

        return service.createProduct(request);
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('PRODUCT_UPDATE')"
    )
    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequest request) {

        return service.updateProduct(id, request);
    }

    @PreAuthorize(
            "hasRole('ADMIN') or hasAuthority('PRODUCT_DELETE')"
    )
    @DeleteMapping("/{id}")
    public String deleteProduct(
            @PathVariable String id) {

        service.deleteProduct(id);

        return "Product deleted successfully";
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER') or hasAuthority('PRODUCT_UPDATE_STOCK')"
    )
    @PutMapping("/update-stock/{id}")
    public ProductResponse updateStock(
            @PathVariable String id,
            @RequestParam int stock) {

        return service.updateStock(id, stock);
    }

    // ================= READ OPERATIONS =================

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('PRODUCT_READ')"
    )
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return service.getAllProducts();
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('PRODUCT_READ')"
    )
    @GetMapping("/{id}")
    public ProductResponse getProductById(
            @PathVariable String id) {

        return service.getProductById(id);
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('PRODUCT_READ')"
    )
    @GetMapping("/low-stock")
    public List<ProductResponse> getLowStockProducts() {

        return service.getLowStockProducts();
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('PRODUCT_READ')"
    )
    @GetMapping("/search")
    public List<ProductResponse> searchProduct(
            @RequestParam String name) {

        return service.searchProduct(name);
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('PRODUCT_READ')"
    )
    @GetMapping("/category/{category}")
    public List<ProductResponse> getProductsByCategory(
            @PathVariable String category) {

        return service.getProductsByCategory(category);
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('PRODUCT_READ')"
    )
    @GetMapping("/count")
    public Long getProductCount() {

        return service.getProductCount();
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','STAFF') or hasAuthority('PRODUCT_READ')"
    )
    @GetMapping("/low-stock/count")
    public Long getLowStockCount() {

        return (long) service.getLowStockProducts().size();
    }
}

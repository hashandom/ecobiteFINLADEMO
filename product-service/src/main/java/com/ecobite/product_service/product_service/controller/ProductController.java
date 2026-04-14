package com.ecobite.product_service.product_service.controller;

import com.ecobite.product_service.product_service.dto.ProductRequest;
import com.ecobite.product_service.product_service.dto.ProductResponse;
import com.ecobite.product_service.product_service.entity.Product;
import com.ecobite.product_service.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return service.createProduct(request);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable String id) {
        return service.getProductById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable String id,
                                         @RequestBody ProductRequest request) {
        return service.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable String id) {
        service.deleteProduct(id);
        return "Product deleted successfully";
    }

    @GetMapping("/low-stock")
    public List<ProductResponse> getLowStockProducts() {
        return service.getLowStockProducts();
    }

    @PutMapping("/update-stock/{id}")
    public ProductResponse updateStock(@PathVariable String id,
                                       @RequestParam int stock) {
        return service.updateStock(id, stock);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProduct(@RequestParam String name) {
        return service.searchProduct(name);
    }

    @GetMapping("/category/{category}")
    public List<ProductResponse> getProductsByCategory(@PathVariable String category) {
        return service.getProductsByCategory(category);
    }
}

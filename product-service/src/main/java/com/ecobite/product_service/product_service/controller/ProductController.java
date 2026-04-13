package com.ecobite.product_service.product_service.controller;

import com.ecobite.product_service.product_service.entity.Product;
import com.ecobite.product_service.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts() {
        return productService.getLowStockProducts();
    }
}

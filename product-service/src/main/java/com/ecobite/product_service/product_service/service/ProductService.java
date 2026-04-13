package com.ecobite.product_service.product_service.service;

import com.ecobite.product_service.product_service.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);

    List<Product> getAllProducts();

    List<Product> getLowStockProducts();
}

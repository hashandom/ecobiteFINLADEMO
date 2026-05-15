package com.ecobite.product_service.product_service.service;

import com.ecobite.product_service.product_service.dto.ProductRequest;
import com.ecobite.product_service.product_service.dto.ProductResponse;
import com.ecobite.product_service.product_service.entity.Product;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(String id);

    ProductResponse updateProduct(String id, ProductRequest request);

    void deleteProduct(String id);

    List<ProductResponse> getLowStockProducts();

    ProductResponse updateStock(String id, int stock);

    List<ProductResponse> searchProduct(String name);

    List<ProductResponse> getProductsByCategory(String category);

    Long getProductCount();
}

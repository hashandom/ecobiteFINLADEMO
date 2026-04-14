package com.ecobite.product_service.product_service.repository;

import com.ecobite.product_service.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByStockLessThan(int stock);

    List<Product> findByCategory(String category);

    List<Product> findByNameContainingIgnoreCase(String name);
}

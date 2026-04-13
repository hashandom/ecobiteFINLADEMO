package com.ecobite.product_service.product_service.service;

import com.ecobite.product_service.product_service.entity.Product;
import com.ecobite.product_service.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repository;

    @Override
    public Product createProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public List<Product> getLowStockProducts() {
        return repository.findByStockLessThan(10);
    }
}

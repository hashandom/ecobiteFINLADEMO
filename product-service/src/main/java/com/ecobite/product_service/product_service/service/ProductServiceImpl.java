package com.ecobite.product_service.product_service.service;

import com.ecobite.product_service.product_service.dto.ProductRequest;
import com.ecobite.product_service.product_service.dto.ProductResponse;
import com.ecobite.product_service.product_service.entity.Product;
import com.ecobite.product_service.product_service.exception.ResourceNotFoundException;
import com.ecobite.product_service.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    private String generateProductId() {

        String lastId = repository.findLastProductId();

        if (lastId == null) {
            return "PRODUCT001";
        }

        int number = Integer.parseInt(lastId.substring(7));
        number++;

        return String.format("PRODUCT%03d", number);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .stock(product.getStock())
                .reorderLevel(product.getReorderLevel())
                .unitPrice(product.getUnitPrice())
                .build();
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Product product = Product.builder()
                .id(generateProductId())
                .name(request.getName())
                .category(request.getCategory())
                .stock(request.getStock())
                .reorderLevel(request.getReorderLevel())
                .unitPrice(request.getUnitPrice())
                .build();

        repository.save(product);

        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(String id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return mapToResponse(product);
    }

    @Override
    public ProductResponse updateProduct(String id, ProductRequest request) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setStock(request.getStock());
        product.setReorderLevel(request.getReorderLevel());
        product.setUnitPrice(request.getUnitPrice());
        repository.save(product);

        return mapToResponse(product);
    }

    @Override
    public void deleteProduct(String id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        repository.delete(product);
    }

    @Override
    public List<ProductResponse> getLowStockProducts() {

        return repository.findAll()
                .stream()
                .filter(p -> p.getStock() < p.getReorderLevel())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateStock(String id, int stock) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setStock(stock);

        repository.save(product);

        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> searchProduct(String name) {

        return repository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) {

        return repository.findByCategory(category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


}

package com.ecobite.product_service.product_service.service;

import com.ecobite.product_service.product_service.dto.BatchResponse;
import com.ecobite.product_service.product_service.dto.ProductRequest;
import com.ecobite.product_service.product_service.dto.ProductResponse;
import com.ecobite.product_service.product_service.dto.event.ProductEvent;
import com.ecobite.product_service.product_service.entity.Product;
import com.ecobite.product_service.product_service.exception.DuplicateProductException;
import com.ecobite.product_service.product_service.exception.ResourceNotFoundException;
import com.ecobite.product_service.product_service.feign.BatchClient;
import com.ecobite.product_service.product_service.kafkaEventProducer.ProductEventProducer;
import com.ecobite.product_service.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductEventProducer producer;
    private final BatchClient batchClient;

    public ProductServiceImpl(ProductRepository repository, ProductEventProducer producer, BatchClient batchClient) {
        this.repository = repository;
        this.producer = producer;
        this.batchClient = batchClient;
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
        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateProductException(
                    "Product '" + request.getName() + "' already exists."
            );
        }

        Product product = Product.builder()
                .id(generateProductId())
                .name(request.getName())
                .category(request.getCategory())
                .stock(0)
                .reorderLevel(request.getReorderLevel())
                .unitPrice(request.getUnitPrice())
                .build();

        repository.save(product);

        ProductEvent event = new ProductEvent();

        event.setEventType("PRODUCT_CREATED");
        event.setProductId(product.getId());
        event.setProductName(product.getName());
        event.setStock(product.getStock());
        event.setReorderLevel(product.getReorderLevel());

        producer.sendEvent(event);

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
        product.setReorderLevel(request.getReorderLevel());
        product.setUnitPrice(request.getUnitPrice());
        repository.save(product);

        return mapToResponse(product);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));
        List<BatchResponse> batches;
        try {

            batches = batchClient.getBatchesByProduct(id);

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Batch service unavailable"
            );
        }
        System.out.println("Product ID: " + id);
        System.out.println("Batches found: " + batches.size());

        //BLOCK deletion if batches exist
        if (!batches.isEmpty()) {
            throw new RuntimeException(
                    "Cannot delete product. Existing batches found."
            );
        }

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

        // LOW STOCK ALERT
        if (stock < product.getReorderLevel() && !product.isLowStockAlertSent()) {

            ProductEvent event = new ProductEvent();
            event.setEventType("LOW_STOCK");
            event.setProductId(product.getId());
            event.setProductName(product.getName());
            event.setStock(stock);
            event.setReorderLevel(product.getReorderLevel());

            producer.sendEvent(event);

            product.setLowStockAlertSent(true);

            System.out.println("Low stock alert sent for product: " + product.getId());
        }

        // RESET ALERT
        if (stock >= product.getReorderLevel()) {
            product.setLowStockAlertSent(false);
        }

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

    @Override
    public Long getProductCount() {
        return repository.count();
    }

//    @Override
//    public ProductResponse addStock(String id, int quantity) {
//        Product product = repository.findById(id)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Product not found"));
//        // ADD quantity to existing stock
//        product.setStock(product.getStock() + quantity);
//        repository.save(product);
//        return mapToResponse(product);
//    }


//    @Override
//    public ProductResponse deductStock(String id, int quantity) {
//        Product product = repository.findById(id)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Product not found"));
//
//        if(product.getStock() < quantity){
//            throw new RuntimeException("Not enough stock");
//        }
//
//        product.setStock(product.getStock() - quantity);
//        repository.save(product);
//        return mapToResponse(product);
//    }


}

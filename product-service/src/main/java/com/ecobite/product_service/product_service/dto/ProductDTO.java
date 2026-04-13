package com.ecobite.product_service.product_service.dto;

public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private int stock;

    public ProductDTO(Long id, String name, String category, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.stock = stock;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getCategory() { return category; }

    public int getStock() { return stock; }
}

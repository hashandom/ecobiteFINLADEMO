package com.ecobite.product_service.product_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        private String category;

        private int stock;

        private int reorderLevel;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public int getStock() {
            return stock;
        }

        public int getReorderLevel() {
            return reorderLevel;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public void setReorderLevel(int reorderLevel) {
            this.reorderLevel = reorderLevel;
        }
    }


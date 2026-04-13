package com.ecobite.dashboard_service.dto;

public class DashBoardDto {
    private int totalProducts;
    private int lowStockProducts;
    private int spoilageCount;

    public int getTotalProducts() {
        return totalProducts;
    }

    public int getLowStockProducts() {
        return lowStockProducts;
    }

    public int getSpoilageCount() {
        return spoilageCount;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public void setLowStockProducts(int lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }

    public void setSpoilageCount(int spoilageCount) {
        this.spoilageCount = spoilageCount;
    }
}

package com.ecobite.supplier_service.repository;

import com.ecobite.supplier_service.entity.SupplierProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long> {
    List<SupplierProduct> findBySupplierId(Long supplierId);
    void deleteAllBySupplierId(Long supplierId);

    boolean existsBySupplierIdAndProductId(
            Long supplierId,
            String productId
    );

    List<SupplierProduct> findByProductId(String productId);
}

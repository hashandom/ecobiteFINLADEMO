package com.ecobite.supplier_service.repository;

import com.ecobite.supplier_service.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByName(String name);
    boolean existsByContactEmail(String contactEmail);
    boolean existsByPhone(String phone);
}

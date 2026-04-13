package com.ecobite.supplier_service.service;

import com.ecobite.supplier_service.entity.Supplier;

import java.util.List;

public interface SupplierService {
    Supplier createSupplier(Supplier supplier);

    List<Supplier> getAllSuppliers();

}

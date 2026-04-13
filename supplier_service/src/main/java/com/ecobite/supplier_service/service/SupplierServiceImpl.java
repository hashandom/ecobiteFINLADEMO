package com.ecobite.supplier_service.service;

import com.ecobite.supplier_service.entity.Supplier;
import com.ecobite.supplier_service.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierRepository repository;

    @Override
    public Supplier createSupplier(Supplier supplier) {
        return repository.save(supplier);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return repository.findAll();
    }
}

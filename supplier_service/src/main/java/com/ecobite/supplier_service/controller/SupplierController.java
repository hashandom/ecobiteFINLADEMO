package com.ecobite.supplier_service.controller;

import com.ecobite.supplier_service.entity.Supplier;
import com.ecobite.supplier_service.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        return supplierService.createSupplier(supplier);
    }

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/performance")
    public List<Supplier> getSupplierPerformance() {
        return supplierService.getAllSuppliers();
    }
}

package com.ecobite.reorder_service.service;

import com.ecobite.reorder_service.entity.Reorder;
import com.ecobite.reorder_service.repository.ReorderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReorderServiceImpl implements ReorderService {
    @Autowired
    private ReorderRepository repository;

    @Override
    public Reorder createReorder(Reorder reorder) {
        reorder.setStatus("CREATED");
        return repository.save(reorder);
    }

    @Override
    public List<Reorder> getAllReorders() {
        return repository.findAll();
    }
}

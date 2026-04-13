package com.ecobite.recall_service.service;

import com.ecobite.recall_service.entity.Recall;
import com.ecobite.recall_service.repository.RecallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RecallServiceImpl implements RecallService {
    @Autowired
    private RecallRepository repository;

    @Override
    public Recall createRecall(Recall recall) {

        recall.setRecallDate(LocalDate.now());
        recall.setStatus("CREATED");

        return repository.save(recall);
    }

    @Override
    public Recall getRecallByBatch(Long batchId) {

        return repository.findByBatchId(batchId)
                .orElseThrow(() -> new RuntimeException("Recall not found"));

    }
}

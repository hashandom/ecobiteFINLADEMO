package com.ecobite.recall_service.service;

import com.ecobite.recall_service.entity.Recall;

public interface RecallService {
    Recall createRecall(Recall recall);

    Recall getRecallByBatch(Long batchId);
}

package com.ecobite.batch_service.service;

import com.ecobite.batch_service.entity.Batch;
import com.ecobite.batch_service.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpiryScheduler {
    private final BatchRepository repository;

    @Scheduled(cron = "0 0 1 * * ?")
    public void updateExpiredBatches(){

        LocalDate today = LocalDate.now();

        List<Batch> batches =
                repository.findByExpiryDateBeforeAndStatus(today,"ACTIVE");

        for(Batch batch : batches){

            batch.setStatus("EXPIRED");

            repository.save(batch);
        }
    }
}

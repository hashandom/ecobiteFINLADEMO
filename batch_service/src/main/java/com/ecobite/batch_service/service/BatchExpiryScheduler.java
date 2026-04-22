package com.ecobite.batch_service.service;

import com.ecobite.batch_service.KafKaEventProducer.BatchEventProducer;
import com.ecobite.batch_service.dto.Kafkaevent.BatchEvent;
import com.ecobite.batch_service.entity.Batch;
import com.ecobite.batch_service.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchExpiryScheduler {
    private final BatchRepository batchRepository;
    private final BatchEventProducer producer;

    @Scheduled(cron = "0 0 8 * * ?") // every day at 8 AM
    //@Scheduled(fixedRate = 10000) // every day at 8 AM
    public void checkExpiringBatches() {

        LocalDate today = LocalDate.now();
        LocalDate next7Days = today.plusDays(7);

        List<Batch> expiringBatches =
                batchRepository.findByExpiryDateBetween(today, next7Days);

        for (Batch batch : expiringBatches) {

            BatchEvent event = new BatchEvent(
                    "BATCH_EXPIRING",
                    batch.getBatchNumber(),
                    batch.getId(),
                    batch.getExpiryDate(),
                    batch.getRemainingQuantity()
            );

            producer.sendEvent(event);

            System.out.println("Sent expiry event for batch: " + batch.getId());
        }
    }
}

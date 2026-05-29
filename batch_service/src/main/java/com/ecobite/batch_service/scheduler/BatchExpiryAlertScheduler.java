package com.ecobite.batch_service.scheduler;

import com.ecobite.batch_service.KafKaEventProducer.BatchEventProducer;
import com.ecobite.batch_service.dto.Kafkaevent.BatchEvent;
import com.ecobite.batch_service.dto.response.ProductResponse;
import com.ecobite.batch_service.entity.Batch;
import com.ecobite.batch_service.feign.ProductClient;
import com.ecobite.batch_service.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchExpiryAlertScheduler {
    private final BatchRepository batchRepository;
    private final BatchEventProducer producer;
    private final ProductClient productClient;

    //@Scheduled(cron = "0 0 8 * * ?") // every day at 8 AM
    @Scheduled(fixedRate = 10000) // every day at 8 AM
    public void checkExpiringBatches() {

        LocalDate today = LocalDate.now();
        LocalDate next7Days = today.plusDays(7);

        List<Batch> expiringBatches =
                batchRepository
                        .findByExpiryDateBetweenAndStatusAndExpiryAlertSentFalse(
                                today,
                                next7Days,
                                "ACTIVE"
                        );

        for (Batch batch : expiringBatches) {
            ProductResponse product =
                    productClient.getProduct(batch.getProductId());


            BatchEvent event = new BatchEvent(
                    "BATCH_EXPIRING",
                    product.getName(),
                    batch.getId(),
                    batch.getExpiryDate(),
                    batch.getRemainingQuantity()
            );

            producer.sendEvent(event);

            // prevent duplicate alerts
            batch.setExpiryAlertSent(true);
            batchRepository.save(batch);
            log.info(
                    "Sent expiry event for batch: {}",
                    batch.getId()
            );

        }
    }
}

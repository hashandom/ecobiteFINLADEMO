package com.ecobite.batch_service.scheduler;

import com.ecobite.batch_service.entity.Batch;
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

public class ExpiredBatchScheduler {
    private final BatchRepository batchRepository;

    //Runs every 10 seconds (FOR TESTING)
    //@Scheduled(fixedRate = 10000)

    // Production scheduler (daily midnight)
    @Scheduled(cron = "0 0 0 * * ?")
    public void markExpiredBatches() {
        log.info("Checking expired batches...");
        List<Batch> expiredBatches =
                batchRepository.findByExpiryDateBeforeAndStatus(
                        LocalDate.now(),
                        "ACTIVE"
                );

        for (Batch batch : expiredBatches) {
            batch.setStatus("EXPIRED");
            // remove sellable stock
            batch.setRemainingQuantity(0);
            batchRepository.save(batch);
            log.info("Batch expired automatically: {}",
                    batch.getBatchNumber());
        }
        log.info("Expired batch processing completed");
    }
}

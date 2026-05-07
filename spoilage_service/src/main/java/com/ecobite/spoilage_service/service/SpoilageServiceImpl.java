package com.ecobite.spoilage_service.service;

import com.ecobite.spoilage_service.Enum.RecordStatus;
import com.ecobite.spoilage_service.dto.SpoilageRequestDTO;
import com.ecobite.spoilage_service.dto.SpoilageResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SpoilageServiceImpl implements SpoilageService{
    private final SpoilageRepository repository;
    private final BatchServiceClient batchClient;

    @Override
    public SpoilageResponseDTO createSpoilage(
            SpoilageRequestDTO dto) {

        BatchResponseDTO batch =
                batchClient.getBatchById(dto.getBatchId());

        if(batch == null) {
            throw new ResourceNotFoundException(
                    "Batch not found");
        }

        if(dto.getQuantity() >
                batch.getAvailableQuantity()) {

            throw new BusinessException(
                    "Spoilage quantity exceeds batch quantity");
        }

        BigDecimal totalLoss =
                dto.getUnitPrice()
                        .multiply(
                                BigDecimal.valueOf(dto.getQuantity()));

        Spoilage spoilage = Spoilage.builder()
                .spoilageCode(generateCode())
                .batchId(dto.getBatchId())
                .productName(dto.getProductName())
                .quantity(dto.getQuantity())
                .unitPrice(dto.getUnitPrice())
                .totalLoss(totalLoss)
                .reason(dto.getReason())
                .spoilageType(dto.getSpoilageType())
                .reportedBy(dto.getReportedBy())
                .reportedDate(LocalDateTime.now())
                .status(RecordStatus.ACTIVE)
                .build();

        repository.save(spoilage);

        return mapToResponse(spoilage);
    }

    private String generateCode() {
        return "SPL-" + UUID.randomUUID()
                .toString()
                .substring(0,8)
                .toUpperCase();
    }
}

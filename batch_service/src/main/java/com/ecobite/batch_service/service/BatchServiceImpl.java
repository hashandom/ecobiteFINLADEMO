package com.ecobite.batch_service.service;


import com.ecobite.batch_service.dto.BatchRequestDTO;
import com.ecobite.batch_service.dto.BatchResponseDTO;
import com.ecobite.batch_service.entity.Batch;
import com.ecobite.batch_service.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchServiceImpl implements BatchService{
    @Autowired
    private BatchRepository repository;

    @Override
    public BatchResponseDTO createBatch(BatchRequestDTO request) {

        Batch batch = new Batch();
        batch.setProductId(request.getProductId());
        batch.setQuantity(request.getQuantity());
        batch.setExpiryDate(request.getExpiryDate());

        Batch saved = repository.save(batch);

        return new BatchResponseDTO(
                saved.getId(),
                saved.getProductId(),
                saved.getQuantity(),
                saved.getExpiryDate()
        );
    }

    @Override
    public List<BatchResponseDTO> getExpiringSoon() {

        LocalDate limitDate = LocalDate.now().plusDays(3);

        return repository.findByExpiryDateBefore(limitDate)
                .stream()
                .map(b -> new BatchResponseDTO(
                        b.getId(),
                        b.getProductId(),
                        b.getQuantity(),
                        b.getExpiryDate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<BatchResponseDTO> getFefoBatches() {

        return repository.findAllByOrderByExpiryDateAsc()
                .stream()
                .map(b -> new BatchResponseDTO(
                        b.getId(),
                        b.getProductId(),
                        b.getQuantity(),
                        b.getExpiryDate()
                ))
                .collect(Collectors.toList());
    }

}

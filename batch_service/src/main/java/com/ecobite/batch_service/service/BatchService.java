package com.ecobite.batch_service.service;

import com.ecobite.batch_service.dto.BatchRequestDTO;
import com.ecobite.batch_service.dto.BatchResponseDTO;

import java.util.List;

public interface BatchService {

    BatchResponseDTO createBatch(BatchRequestDTO request);

    List<BatchResponseDTO> getExpiringSoon();

    List<BatchResponseDTO> getFefoBatches();

}

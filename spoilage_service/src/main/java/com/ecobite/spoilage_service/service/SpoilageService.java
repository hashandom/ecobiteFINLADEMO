package com.ecobite.spoilage_service.service;

import com.ecobite.spoilage_service.dto.SpoilageRequestDTO;
import com.ecobite.spoilage_service.dto.SpoilageResponseDTO;

import java.util.List;

public interface SpoilageService {

    SpoilageResponseDTO createSpoilage(
            SpoilageRequestDTO requestDTO);

    SpoilageResponseDTO getByCode(String code);

    List<SpoilageResponseDTO> getAll();

    void delete(String code);
}

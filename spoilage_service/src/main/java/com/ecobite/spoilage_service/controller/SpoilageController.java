package com.ecobite.spoilage_service.controller;

import com.ecobite.spoilage_service.dto.SpoilageRequestDTO;
import com.ecobite.spoilage_service.dto.SpoilageResponseDTO;
import com.ecobite.spoilage_service.service.SpoilageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spoilage")
@RequiredArgsConstructor
public class SpoilageController {
    private final SpoilageService service;

    @PostMapping
    public ResponseEntity<ApiResponse<SpoilageResponseDTO>>
    create(@Valid @RequestBody SpoilageRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                service.createSpoilage(dto)
                        )
                );
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<SpoilageResponseDTO>>
    get(@PathVariable String code) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        service.getByCode(code)
                )
        );
    }
}

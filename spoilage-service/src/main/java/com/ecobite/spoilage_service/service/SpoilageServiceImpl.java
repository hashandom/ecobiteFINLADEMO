package com.ecobite.spoilage_service.service;

import com.ecobite.spoilage_service.entity.SpoilageLog;
import com.ecobite.spoilage_service.repository.SpoilageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SpoilageServiceImpl implements SpoilageService{
    @Autowired
    private SpoilageRepository repository;

    @Override
    public SpoilageLog addSpoilage(SpoilageLog spoilage) {

        spoilage.setReportedDate(LocalDate.now());

        return repository.save(spoilage);
    }

    @Override
    public List<SpoilageLog> getSpoilageReport() {
        return repository.findAll();
    }
}

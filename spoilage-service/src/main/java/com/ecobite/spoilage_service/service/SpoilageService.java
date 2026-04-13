package com.ecobite.spoilage_service.service;

import com.ecobite.spoilage_service.entity.SpoilageLog;

import java.util.List;

public interface SpoilageService {

    SpoilageLog addSpoilage(SpoilageLog spoilage);

    List<SpoilageLog> getSpoilageReport();
}

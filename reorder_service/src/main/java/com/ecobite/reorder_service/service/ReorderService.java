package com.ecobite.reorder_service.service;

import com.ecobite.reorder_service.entity.Reorder;

import java.util.List;

public interface ReorderService {
    Reorder createReorder(Reorder reorder);
    List<Reorder> getAllReorders();
}

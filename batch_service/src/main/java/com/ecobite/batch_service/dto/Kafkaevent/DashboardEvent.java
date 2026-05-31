package com.ecobite.batch_service.dto.Kafkaevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardEvent {
    private String type;
    private String message;
}

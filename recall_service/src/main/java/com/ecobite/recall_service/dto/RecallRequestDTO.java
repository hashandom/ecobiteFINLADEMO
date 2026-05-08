package com.ecobite.recall_service.dto;

public class RecallRequestDTO {
    @NotBlank(message = "Batch ID is required")
    private String batchId;

    @NotBlank(message = "Recall reason is required")
    private String reason;

    @NotBlank(message = "Initiated by is required")
    private String initiatedBy;
}

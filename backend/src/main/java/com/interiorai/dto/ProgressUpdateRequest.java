package com.interiorai.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ProgressUpdateRequest {

    @NotNull(message = "Progress percentage is required")
    @Min(value = 0, message = "Progress cannot be less than 0%")
    @Max(value = 100, message = "Progress cannot exceed 100%")
    private Integer progressPercentage;

    private String status;

    public ProgressUpdateRequest() {
    }

    public ProgressUpdateRequest(Integer progressPercentage, String status) {
        this.progressPercentage = progressPercentage;
        this.status = status;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

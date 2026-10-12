package com.interiorai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ProjectRequest {

    @NotNull(message = "Room ID is required")
    private Long roomId;

    private Long designId;

    @NotBlank(message = "Project name is required")
    private String projectName;

    private String status = "PLANNING";
    private LocalDate startDate;
    private LocalDate targetEndDate;
    private Integer progressPercentage = 0;

    public ProjectRequest() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getDesignId() {
        return designId;
    }

    public void setDesignId(Long designId) {
        this.designId = designId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getTargetEndDate() {
        return targetEndDate;
    }

    public void setTargetEndDate(LocalDate targetEndDate) {
        this.targetEndDate = targetEndDate;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}

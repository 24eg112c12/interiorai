package com.interiorai.dto;

import com.interiorai.entity.Booking;
import com.interiorai.entity.Design;
import com.interiorai.entity.Project;

import java.math.BigDecimal;
import java.util.List;

public class DashboardSummaryDto {
    private long totalRooms;
    private long totalSavedDesigns;
    private long activeProjects;
    private long pendingBookings;
    private BigDecimal totalEstimatedBudget;
    private List<Design> recentDesigns;
    private List<Project> recentProjects;
    private List<Booking> recentBookings;

    public DashboardSummaryDto() {
    }

    public long getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(long totalRooms) {
        this.totalRooms = totalRooms;
    }

    public long getTotalSavedDesigns() {
        return totalSavedDesigns;
    }

    public void setTotalSavedDesigns(long totalSavedDesigns) {
        this.totalSavedDesigns = totalSavedDesigns;
    }

    public long getActiveProjects() {
        return activeProjects;
    }

    public void setActiveProjects(long activeProjects) {
        this.activeProjects = activeProjects;
    }

    public long getPendingBookings() {
        return pendingBookings;
    }

    public void setPendingBookings(long pendingBookings) {
        this.pendingBookings = pendingBookings;
    }

    public BigDecimal getTotalEstimatedBudget() {
        return totalEstimatedBudget;
    }

    public void setTotalEstimatedBudget(BigDecimal totalEstimatedBudget) {
        this.totalEstimatedBudget = totalEstimatedBudget;
    }

    public List<Design> getRecentDesigns() {
        return recentDesigns;
    }

    public void setRecentDesigns(List<Design> recentDesigns) {
        this.recentDesigns = recentDesigns;
    }

    public List<Project> getRecentProjects() {
        return recentProjects;
    }

    public void setRecentProjects(List<Project> recentProjects) {
        this.recentProjects = recentProjects;
    }

    public List<Booking> getRecentBookings() {
        return recentBookings;
    }

    public void setRecentBookings(List<Booking> recentBookings) {
        this.recentBookings = recentBookings;
    }
}

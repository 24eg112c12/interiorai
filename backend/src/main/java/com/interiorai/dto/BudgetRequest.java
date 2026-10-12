package com.interiorai.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class BudgetRequest {

    @NotNull(message = "Room ID is required")
    private Long roomId;

    private Long designId;

    @NotNull(message = "Total budget is required")
    private BigDecimal totalBudget;

    private BigDecimal estimatedCost;
    private BigDecimal actualCost;
    private String currency = "INR";
    private List<BudgetItemDto> items;

    public BudgetRequest() {
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

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public BigDecimal getActualCost() {
        return actualCost;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<BudgetItemDto> getItems() {
        return items;
    }

    public void setItems(List<BudgetItemDto> items) {
        this.items = items;
    }
}

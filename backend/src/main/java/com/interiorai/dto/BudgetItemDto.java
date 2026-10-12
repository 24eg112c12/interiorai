package com.interiorai.dto;

import java.math.BigDecimal;

public class BudgetItemDto {
    private String category;
    private String itemName;
    private BigDecimal estimatedAmount;
    private String notes;

    public BudgetItemDto() {
    }

    public BudgetItemDto(String category, String itemName, BigDecimal estimatedAmount, String notes) {
        this.category = category;
        this.itemName = itemName;
        this.estimatedAmount = estimatedAmount;
        this.notes = notes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(BigDecimal estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

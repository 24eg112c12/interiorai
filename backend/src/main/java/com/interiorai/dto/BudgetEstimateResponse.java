package com.interiorai.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BudgetEstimateResponse {
    private BigDecimal userBudget;
    private BigDecimal estimatedTotal;
    private BigDecimal remainingAmount;
    private boolean overBudget;
    private String status; // UNDER_BUDGET, WITHIN_BUDGET, OVER_BUDGET
    private BigDecimal areaSqM;
    private BigDecimal areaSqFt;
    private List<BudgetItemDto> items = new ArrayList<>();
    private List<String> recommendations = new ArrayList<>();

    public BudgetEstimateResponse() {
    }

    public BigDecimal getUserBudget() {
        return userBudget;
    }

    public void setUserBudget(BigDecimal userBudget) {
        this.userBudget = userBudget;
    }

    public BigDecimal getEstimatedTotal() {
        return estimatedTotal;
    }

    public void setEstimatedTotal(BigDecimal estimatedTotal) {
        this.estimatedTotal = estimatedTotal;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public boolean isOverBudget() {
        return overBudget;
    }

    public void setOverBudget(boolean overBudget) {
        this.overBudget = overBudget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAreaSqM() {
        return areaSqM;
    }

    public void setAreaSqM(BigDecimal areaSqM) {
        this.areaSqM = areaSqM;
    }

    public BigDecimal getAreaSqFt() {
        return areaSqFt;
    }

    public void setAreaSqFt(BigDecimal areaSqFt) {
        this.areaSqFt = areaSqFt;
    }

    public List<BudgetItemDto> getItems() {
        return items;
    }

    public void setItems(List<BudgetItemDto> items) {
        this.items = items;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }
}

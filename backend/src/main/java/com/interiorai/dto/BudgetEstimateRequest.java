package com.interiorai.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BudgetEstimateRequest {

    @NotNull(message = "Room length is required")
    @DecimalMin(value = "0.5")
    private BigDecimal lengthM;

    @NotNull(message = "Room width is required")
    @DecimalMin(value = "0.5")
    private BigDecimal widthM;

    private BigDecimal ceilingHeightM;
    private String roomType;
    private String interiorStyle;

    @NotNull(message = "Total budget is required")
    @DecimalMin(value = "1000", message = "Minimum budget must be at least ₹1,000")
    private BigDecimal totalBudget;

    public BudgetEstimateRequest() {
    }

    public BigDecimal getLengthM() {
        return lengthM;
    }

    public void setLengthM(BigDecimal lengthM) {
        this.lengthM = lengthM;
    }

    public BigDecimal getWidthM() {
        return widthM;
    }

    public void setWidthM(BigDecimal widthM) {
        this.widthM = widthM;
    }

    public BigDecimal getCeilingHeightM() {
        return ceilingHeightM;
    }

    public void setCeilingHeightM(BigDecimal ceilingHeightM) {
        this.ceilingHeightM = ceilingHeightM;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getInteriorStyle() {
        return interiorStyle;
    }

    public void setInteriorStyle(String interiorStyle) {
        this.interiorStyle = interiorStyle;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }
}

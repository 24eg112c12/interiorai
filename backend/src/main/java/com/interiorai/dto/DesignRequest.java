package com.interiorai.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DesignRequest {

    @NotNull(message = "Room ID is required")
    private Long roomId;

    private String designName;
    private String interiorStyle;
    private String colorPalette;
    private BigDecimal budgetInr;
    private String designDescription;
    private String furniturePreferences;
    private String materialPreferences;
    private String budgetFlexibility;

    public DesignRequest() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public String getInteriorStyle() {
        return interiorStyle;
    }

    public void setInteriorStyle(String interiorStyle) {
        this.interiorStyle = interiorStyle;
    }

    public String getColorPalette() {
        return colorPalette;
    }

    public void setColorPalette(String colorPalette) {
        this.colorPalette = colorPalette;
    }

    public BigDecimal getBudgetInr() {
        return budgetInr;
    }

    public void setBudgetInr(BigDecimal budgetInr) {
        this.budgetInr = budgetInr;
    }

    public String getDesignDescription() {
        return designDescription;
    }

    public void setDesignDescription(String designDescription) {
        this.designDescription = designDescription;
    }

    public String getFurniturePreferences() {
        return furniturePreferences;
    }

    public void setFurniturePreferences(String furniturePreferences) {
        this.furniturePreferences = furniturePreferences;
    }

    public String getMaterialPreferences() {
        return materialPreferences;
    }

    public void setMaterialPreferences(String materialPreferences) {
        this.materialPreferences = materialPreferences;
    }

    public String getBudgetFlexibility() {
        return budgetFlexibility;
    }

    public void setBudgetFlexibility(String budgetFlexibility) {
        this.budgetFlexibility = budgetFlexibility;
    }
}

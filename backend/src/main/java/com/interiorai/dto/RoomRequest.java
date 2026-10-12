package com.interiorai.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class RoomRequest {

    @NotBlank(message = "Room type is required")
    private String roomType;

    @NotNull(message = "Room length in meters is required")
    @DecimalMin(value = "0.5", message = "Room length must be at least 0.5 meters")
    @DecimalMax(value = "100.0", message = "Room length cannot exceed 100 meters")
    private BigDecimal lengthM;

    @NotNull(message = "Room width in meters is required")
    @DecimalMin(value = "0.5", message = "Room width must be at least 0.5 meters")
    @DecimalMax(value = "100.0", message = "Room width cannot exceed 100 meters")
    private BigDecimal widthM;

    @DecimalMin(value = "1.5", message = "Ceiling height must be at least 1.5 meters")
    @DecimalMax(value = "20.0", message = "Ceiling height cannot exceed 20 meters")
    private BigDecimal ceilingHeightM;

    private String existingFurniture;
    private Integer numberOfDoors = 1;
    private Integer numberOfWindows = 1;
    private Integer numberOfOccupants = 1;
    private String additionalRequirements;

    public RoomRequest() {
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
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

    public String getExistingFurniture() {
        return existingFurniture;
    }

    public void setExistingFurniture(String existingFurniture) {
        this.existingFurniture = existingFurniture;
    }

    public Integer getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(Integer numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public Integer getNumberOfWindows() {
        return numberOfWindows;
    }

    public void setNumberOfWindows(Integer numberOfWindows) {
        this.numberOfWindows = numberOfWindows;
    }

    public Integer getNumberOfOccupants() {
        return numberOfOccupants;
    }

    public void setNumberOfOccupants(Integer numberOfOccupants) {
        this.numberOfOccupants = numberOfOccupants;
    }

    public String getAdditionalRequirements() {
        return additionalRequirements;
    }

    public void setAdditionalRequirements(String additionalRequirements) {
        this.additionalRequirements = additionalRequirements;
    }
}

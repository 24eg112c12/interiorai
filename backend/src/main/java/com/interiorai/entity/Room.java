package com.interiorai.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "room_type", nullable = false, length = 50)
    private String roomType;

    @Column(name = "length_m", nullable = false, precision = 8, scale = 2)
    private BigDecimal lengthM;

    @Column(name = "width_m", nullable = false, precision = 8, scale = 2)
    private BigDecimal widthM;

    @Column(name = "ceiling_height_m", precision = 8, scale = 2)
    private BigDecimal ceilingHeightM;

    @Column(name = "existing_furniture", columnDefinition = "TEXT")
    private String existingFurniture;

    @Column(name = "number_of_doors")
    private Integer numberOfDoors = 1;

    @Column(name = "number_of_windows")
    private Integer numberOfWindows = 1;

    @Column(name = "number_of_occupants")
    private Integer numberOfOccupants = 1;

    @Column(name = "additional_requirements", columnDefinition = "TEXT")
    private String additionalRequirements;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Room() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public BigDecimal getArea() {
        if (lengthM != null && widthM != null) {
            return lengthM.multiply(widthM);
        }
        return BigDecimal.ZERO;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

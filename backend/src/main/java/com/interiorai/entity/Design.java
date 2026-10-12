package com.interiorai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "designs")
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "design_id")
    private Long designId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "design_name", nullable = false, length = 150)
    private String designName;

    @Column(name = "interior_style", nullable = false, length = 50)
    private String interiorStyle;

    @Column(name = "color_palette", length = 100)
    private String colorPalette;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "prompt_used", columnDefinition = "TEXT")
    private String promptUsed;

    @Column(name = "generation_status", length = 30)
    private String generationStatus = "COMPLETED";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Design() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.generationStatus == null) {
            this.generationStatus = "COMPLETED";
        }
    }

    public Long getDesignId() {
        return designId;
    }

    public void setDesignId(Long designId) {
        this.designId = designId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPromptUsed() {
        return promptUsed;
    }

    public void setPromptUsed(String promptUsed) {
        this.promptUsed = promptUsed;
    }

    public String getGenerationStatus() {
        return generationStatus;
    }

    public void setGenerationStatus(String generationStatus) {
        this.generationStatus = generationStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

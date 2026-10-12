package com.interiorai.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "professionals")
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "professional_id")
    private Long professionalId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "mobile", length = 15)
    private String mobile;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "experience_years")
    private Integer experienceYears = 1;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "service_type", nullable = false, length = 80)
    private String serviceType;

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating = new BigDecimal("4.8");

    @Column(name = "reviews_count")
    private Integer reviewsCount = 10;

    @Column(name = "hourly_or_base_rate", length = 50)
    private String hourlyOrBaseRate;

    @Column(name = "portfolio_image_url", length = 500)
    private String portfolioImageUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Professional() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.rating == null) {
            this.rating = new BigDecimal("4.8");
        }
        if (this.reviewsCount == null) {
            this.reviewsCount = 10;
        }
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public String getHourlyOrBaseRate() {
        return hourlyOrBaseRate;
    }

    public void setHourlyOrBaseRate(String hourlyOrBaseRate) {
        this.hourlyOrBaseRate = hourlyOrBaseRate;
    }

    public String getPortfolioImageUrl() {
        return portfolioImageUrl;
    }

    public void setPortfolioImageUrl(String portfolioImageUrl) {
        this.portfolioImageUrl = portfolioImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

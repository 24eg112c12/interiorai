package com.interiorai.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "budget_items")
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    @JsonBackReference
    private Budget budget;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "item_name", length = 150)
    private String itemName;

    @Column(name = "estimated_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal estimatedAmount;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public BudgetItem() {
    }

    public BudgetItem(String category, String itemName, BigDecimal estimatedAmount, String notes) {
        this.category = category;
        this.itemName = itemName;
        this.estimatedAmount = estimatedAmount;
        this.notes = notes;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
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

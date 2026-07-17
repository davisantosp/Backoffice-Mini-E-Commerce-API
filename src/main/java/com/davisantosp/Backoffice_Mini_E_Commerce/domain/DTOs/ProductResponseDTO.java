package com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Category;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ProductResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantityStored;
    private Category category;
    private Instant createdAt;
    private Instant updatedAt;

    public ProductResponseDTO(UUID id, String name, String description, BigDecimal price, Integer quantityStored, Category category, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityStored = quantityStored;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ProductResponseDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantityStored() {
        return quantityStored;
    }

    public void setQuantityStored(Integer quantityStored) {
        this.quantityStored = quantityStored;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

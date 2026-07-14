package com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductCreateDTO {
    @NotBlank
    private String name;

    @Max(1024)
    @NotBlank
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer quantityStored;

    @NotNull
    private Category category;

    public ProductCreateDTO(String name, String description, BigDecimal price, Integer quantityStored, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityStored = quantityStored;
        this.category = category;
    }

    public ProductCreateDTO() {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getQuantityStored() {
        return quantityStored;
    }

    public void setQuantityStored(Integer quantityStored) {
        this.quantityStored = quantityStored;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

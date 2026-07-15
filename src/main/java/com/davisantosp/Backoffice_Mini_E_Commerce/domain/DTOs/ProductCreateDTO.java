package com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductCreateDTO {
    @NotBlank
    private String name;

    @Length(max = 1024)
    @NotBlank
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer quantityStored;

    @NotNull
    private UUID category_id;

    public ProductCreateDTO(String name, String description, BigDecimal price, Integer quantityStored, UUID category_id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityStored = quantityStored;
        this.category_id = category_id;
    }

    public ProductCreateDTO() {
    }

    public UUID getCategory_id() {
        return category_id;
    }

    public void setCategory_id(UUID category_id) {
        this.category_id = category_id;
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

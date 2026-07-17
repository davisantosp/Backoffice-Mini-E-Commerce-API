package com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs;

import jakarta.validation.constraints.NotBlank;

public class CategoryCreateDTO {

    @NotBlank
    private String name;

    public CategoryCreateDTO(String name) {
        this.name = name;
    }

    public CategoryCreateDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

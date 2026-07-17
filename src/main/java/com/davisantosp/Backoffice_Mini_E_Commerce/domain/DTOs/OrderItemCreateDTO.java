package com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemCreateDTO(
        @NotNull UUID productId,
        @NotNull @Positive Integer quantity,
        @NotNull BigDecimal price
){}
package com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public record OrderCreateDTO (
        @NotNull List<OrderItemCreateDTO> orderItems,
        @NotNull OrderStatus status,
        @NotNull Instant purchasedDate
){}

package com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.OrderItem;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.enums.OrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
    UUID id,
    List<OrderItem> orderItems,
    Instant purchasedDate,
    OrderStatus status,
    Instant createdAt,
    Instant updatedAt
){}

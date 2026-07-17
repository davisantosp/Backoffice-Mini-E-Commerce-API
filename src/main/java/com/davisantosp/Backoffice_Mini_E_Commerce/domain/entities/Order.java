package com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities;


import com.davisantosp.Backoffice_Mini_E_Commerce.domain.enums.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @DateTimeFormat
    @Column(nullable = false)
    private Instant purchaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public Order(List<OrderItem> items, Instant purchaseDate, OrderStatus status) {
        this.items = items;
        this.purchaseDate = purchaseDate;
        this.status = status;
    }

    public Order() {
    }

    public BigDecimal getTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderItem item : items) {
            sum = sum.add(item.getSubtotal());
        }
        return sum;
    }

    public UUID getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Instant getPurchaseDate() {
        return purchaseDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setPurchaseDate(Instant purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

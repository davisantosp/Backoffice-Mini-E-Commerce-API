package com.davisantosp.Backoffice_Mini_E_Commerce.services;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.OrderCreateDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.OrderItemCreateDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.OrderResponseDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Order;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.OrderItem;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Product;
import com.davisantosp.Backoffice_Mini_E_Commerce.infra.exceptions.ResourceNotFoundException;
import com.davisantosp.Backoffice_Mini_E_Commerce.repositories.OrderRepository;
import com.davisantosp.Backoffice_Mini_E_Commerce.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    OrderRepository orderRepository;
    ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<OrderResponseDTO> findAll(){
        return orderRepository.findAll()
                .stream()
                .map(this::fromOrder)
                .toList();
    }

    public OrderResponseDTO findById(UUID id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        return this.fromOrder(order);
    }

    public OrderResponseDTO create(OrderCreateDTO orderCreateDTO){
        List<OrderItem> orderItems = orderCreateDTO.orderItems()
                .stream()
                .map(this::fromOrderItemCreateDTO)
                .toList();

        Order order = new Order(orderItems, orderCreateDTO.purchasedDate(), orderCreateDTO.status());
        order.getItems().forEach(item -> item.setOrder(order));

        Order savedOrder = orderRepository.save(order);
        return this.fromOrder(savedOrder);
    }

    public OrderResponseDTO update(OrderCreateDTO updatedOrderDTO, UUID id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        List<OrderItem> orderItems = updatedOrderDTO.orderItems()
                .stream()
                .map(this::fromOrderItemCreateDTO)
                .toList();

        order.getItems().clear();
        orderItems.forEach(item -> {
            item.setOrder(order);
            order.getItems().add(item);
        });
        order.setPurchaseDate(updatedOrderDTO.purchasedDate());
        order.setStatus(updatedOrderDTO.status());

        Order updatedOrder = orderRepository.save(order);
        return this.fromOrder(updatedOrder);
    }

    public void delete(UUID id){
        if(!orderRepository.existsById(id))
            throw new ResourceNotFoundException("Order not found.");
        orderRepository.deleteById(id);
    }

    private OrderItem fromOrderItemCreateDTO(OrderItemCreateDTO orderItemCreateDTO){
        Product product = productRepository.findById(orderItemCreateDTO.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        return new OrderItem(null, product, orderItemCreateDTO.quantity(), orderItemCreateDTO.price());
    }

    private OrderResponseDTO fromOrder(Order order){
        return new OrderResponseDTO(
                order.getId(),
                order.getItems(),
                order.getPurchaseDate(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}

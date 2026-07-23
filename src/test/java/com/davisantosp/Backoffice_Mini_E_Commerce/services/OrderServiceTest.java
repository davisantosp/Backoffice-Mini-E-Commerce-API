package com.davisantosp.Backoffice_Mini_E_Commerce.services;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.OrderCreateDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.OrderItemCreateDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.OrderResponseDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Order;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.OrderItem;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Product;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.enums.OrderStatus;
import com.davisantosp.Backoffice_Mini_E_Commerce.infra.exceptions.ResourceNotFoundException;
import com.davisantosp.Backoffice_Mini_E_Commerce.repositories.OrderRepository;
import com.davisantosp.Backoffice_Mini_E_Commerce.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService")
class OrderServiceTest {

    @Mock OrderRepository orderRepository;
    @Mock ProductRepository productRepository;

    @InjectMocks OrderService orderService;

    Order order;
    OrderCreateDTO orderCreateDTO;
    Product product;
    OrderItem orderItem;
    Instant now;

    @BeforeEach
    void setup() throws Exception {
        now = Instant.now();

        product = new Product(
                UUID.randomUUID(),
                "Test Product",
                "Test Description",
                BigDecimal.valueOf(10.99),
                100,
                null,
                now,
                now
        );

        orderItem = new OrderItem(null, product, 2, BigDecimal.valueOf(10.99));

        order = new Order(
                new ArrayList<>(List.of(orderItem)),
                now,
                OrderStatus.PENDING
        );

        orderItem.setOrder(order);

        Field idField = Order.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(order, UUID.randomUUID());

        var orderItemCreateDTO = new OrderItemCreateDTO(
                product.getId(),
                2,
                BigDecimal.valueOf(10.99)
        );

        orderCreateDTO = new OrderCreateDTO(
                List.of(orderItemCreateDTO),
                OrderStatus.PENDING,
                now
        );
    }

    @Test
    public void testFindAll_returnOrderResponseDTOList() {
        when(orderRepository.findAll())
                .thenReturn(Arrays.asList(order));

        List<OrderResponseDTO> list = orderService.findAll();
        assertNotEquals(0, list.toArray().length);

        var orderResponse = list.getFirst();
        assertEquals(orderResponse.id(), order.getId());
        assertEquals(orderResponse.purchasedDate(), order.getPurchaseDate());
        assertEquals(orderResponse.status(), order.getStatus());
        assertEquals(orderResponse.createdAt(), order.getCreatedAt());
        assertEquals(orderResponse.updatedAt(), order.getUpdatedAt());

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_returnEmptyList() {
        when(orderRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<OrderResponseDTO> list = orderService.findAll();
        assertEquals(0, list.toArray().length);

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_returnOrderResponseDTO() {
        var id = order.getId();
        when(orderRepository.findById(id))
                .thenReturn(Optional.of(order));

        OrderResponseDTO response = orderService.findById(id);

        assertNotNull(response);
        assertEquals(response.id(), id);
        assertEquals(response.purchasedDate(), order.getPurchaseDate());
        assertEquals(response.status(), order.getStatus());
        assertEquals(response.createdAt(), order.getCreatedAt());
        assertEquals(response.updatedAt(), order.getUpdatedAt());

        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_throwResourceNotFoundException() {
        var id = UUID.randomUUID();
        when(orderRepository.findById(id))
                .thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.findById(id)
        );

        assertEquals("Order not found.", e.getMessage());

        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    public void testCreate_returnOrderResponseDTO() {
        ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);

        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        OrderResponseDTO response = orderService.create(orderCreateDTO);

        verify(orderRepository, times(1)).save(argumentCaptor.capture());
        Order savedOrder = argumentCaptor.getValue();

        assertEquals(savedOrder.getStatus(), orderCreateDTO.status());
        assertEquals(savedOrder.getPurchaseDate(), orderCreateDTO.purchasedDate());
        assertEquals(savedOrder.getItems().size(), orderCreateDTO.orderItems().size());

        assertNotNull(response.id());
        assertEquals(response.status(), orderCreateDTO.status());
        assertEquals(response.purchasedDate(), orderCreateDTO.purchasedDate());

        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    public void testCreate_throwResourceNotFoundException_whenProductNotFound() {
        when(productRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.create(orderCreateDTO)
        );

        assertEquals("Product not found.", e.getMessage());

        verify(productRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    public void testUpdate_returnOrderResponseDTO() {
        ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);

        var updatedItemCreateDTO = new OrderItemCreateDTO(
                product.getId(),
                3,
                BigDecimal.valueOf(15.99)
        );
        OrderCreateDTO updatedOrderDTO = new OrderCreateDTO(
                List.of(updatedItemCreateDTO),
                OrderStatus.PAYED,
                Instant.now()
        );
        var id = order.getId();

        when(orderRepository.findById(id))
                .thenReturn(Optional.of(order));
        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        OrderResponseDTO response = orderService.update(updatedOrderDTO, id);

        verify(orderRepository, times(1)).save(argumentCaptor.capture());
        Order capturedOrder = argumentCaptor.getValue();

        assertEquals(updatedOrderDTO.status(), capturedOrder.getStatus());
        assertEquals(updatedOrderDTO.purchasedDate(), capturedOrder.getPurchaseDate());
        assertEquals(1, capturedOrder.getItems().size());
        assertEquals(updatedItemCreateDTO.quantity(), capturedOrder.getItems().getFirst().getQuantity());

        assertEquals(response.id(), id);
        assertEquals(response.status(), updatedOrderDTO.status());
        assertEquals(response.purchasedDate(), updatedOrderDTO.purchasedDate());

        verify(orderRepository, times(1)).findById(id);
        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    public void testUpdate_throwResourceNotFoundException() {
        var updatedItemCreateDTO = new OrderItemCreateDTO(
                product.getId(),
                3,
                BigDecimal.valueOf(15.99)
        );
        OrderCreateDTO updatedOrderDTO = new OrderCreateDTO(
                List.of(updatedItemCreateDTO),
                OrderStatus.PAYED,
                Instant.now()
        );
        var id = UUID.randomUUID();

        when(orderRepository.findById(id))
                .thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.update(updatedOrderDTO, id)
        );

        assertEquals("Order not found.", e.getMessage());

        verify(orderRepository, times(1)).findById(id);
        verify(productRepository, times(0)).findById(any(UUID.class));
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    public void testDelete_successfulDelete() {
        var id = order.getId();
        when(orderRepository.existsById(id))
                .thenReturn(true);

        doNothing().when(orderRepository).deleteById(id);

        orderService.delete(id);

        verify(orderRepository, times(1)).existsById(id);
        verify(orderRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDelete_throwResourceNotFoundException() {
        var id = UUID.randomUUID();
        when(orderRepository.existsById(id))
                .thenReturn(false);

        ResourceNotFoundException e = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.delete(id)
        );

        assertEquals("Order not found.", e.getMessage());
        verify(orderRepository, times(1)).existsById(id);
        verify(orderRepository, times(0)).deleteById(any(UUID.class));
    }
}
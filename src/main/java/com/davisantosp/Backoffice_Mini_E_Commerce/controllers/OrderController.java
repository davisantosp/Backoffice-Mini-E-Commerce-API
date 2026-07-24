package com.davisantosp.Backoffice_Mini_E_Commerce.controllers;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.OrderCreateDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.OrderResponseDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> index(){
        List<OrderResponseDTO> responseDTOList = orderService.findAll();
        return ResponseEntity.ok(responseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> get(@PathVariable UUID id){
        OrderResponseDTO responseDTO = orderService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> post(@Valid @RequestBody OrderCreateDTO body){
        OrderResponseDTO responseDTO = orderService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> update(@Valid @RequestBody OrderCreateDTO body, @PathVariable UUID id){
        OrderResponseDTO responseDTO = orderService.update(body, id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
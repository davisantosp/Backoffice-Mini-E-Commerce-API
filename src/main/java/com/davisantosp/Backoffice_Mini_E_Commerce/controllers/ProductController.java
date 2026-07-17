package com.davisantosp.Backoffice_Mini_E_Commerce.controllers;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.ProductCreateDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.ProductResponseDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> index(){
        List<ProductResponseDTO> responseDTOList = productService.findAll();
        return ResponseEntity.ok(responseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> get(@PathVariable UUID id){
        ProductResponseDTO responseDTO = productService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> post(@Valid @RequestBody ProductCreateDTO body){
        ProductResponseDTO responseDTO = productService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductResponseDTO> update(@Valid @RequestBody ProductCreateDTO body, @PathVariable UUID id){
        ProductResponseDTO responseDTO = productService.update(body, id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

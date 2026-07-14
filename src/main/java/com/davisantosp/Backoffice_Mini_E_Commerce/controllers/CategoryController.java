package com.davisantosp.Backoffice_Mini_E_Commerce.controllers;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.CategoryCreateDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.CategoryResponseDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity index(){
        List<CategoryResponseDTO> responseDTOList = categoryService.findAll();
        return ResponseEntity.ok(responseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable UUID id){
        CategoryResponseDTO responseDTO = categoryService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody CategoryCreateDTO body){
        CategoryResponseDTO responseDTO = categoryService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity post(@Valid @RequestBody CategoryCreateDTO body, @PathVariable UUID id){
        CategoryResponseDTO responseDTO = categoryService.update(body, id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity post(@PathVariable UUID id){
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

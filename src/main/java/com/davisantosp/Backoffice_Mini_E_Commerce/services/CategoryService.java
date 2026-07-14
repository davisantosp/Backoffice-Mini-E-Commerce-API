package com.davisantosp.Backoffice_Mini_E_Commerce.services;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.CategoryCreateDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.CategoryResponseDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Category;
import com.davisantosp.Backoffice_Mini_E_Commerce.infra.exceptions.ResourceNotFoundException;
import com.davisantosp.Backoffice_Mini_E_Commerce.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseDTO> findAll(){
        return categoryRepository.findAll()
                .stream()
                .map(this::fromCategory)
                .toList();
    }
    public CategoryResponseDTO findById(UUID id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Category not found"));

        return this.fromCategory(category);
    }

    public CategoryResponseDTO create(CategoryCreateDTO categoryCreateDTO){
        Category category = categoryRepository.save(
                this.fromCategoryCreateDTO(categoryCreateDTO)
        );

        return this.fromCategory(category);
    }

    public CategoryResponseDTO update(CategoryCreateDTO updatedCategoryDTO, UUID id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Category not found"));

        category.setName(updatedCategoryDTO.getName());

        Category updatedCategory = categoryRepository.save(category);
        return this.fromCategory(updatedCategory);
    }

    public void delete(UUID id){
        if(!categoryRepository.existsById(id))
                throw new ResourceNotFoundException("Category not found");
        categoryRepository.deleteById(id);
    }

    private CategoryResponseDTO fromCategory(Category category){
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    private Category fromCategoryCreateDTO(CategoryCreateDTO categoryCreateDTO){
        return new Category(
                categoryCreateDTO.getName()
        );
    }
}

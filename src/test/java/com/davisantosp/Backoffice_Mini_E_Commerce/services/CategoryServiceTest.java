package com.davisantosp.Backoffice_Mini_E_Commerce.services;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.CategoryCreateDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.CategoryResponseDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Category;
import com.davisantosp.Backoffice_Mini_E_Commerce.infra.exceptions.ResourceNotFoundException;
import com.davisantosp.Backoffice_Mini_E_Commerce.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService")
class CategoryServiceTest {

    @Mock CategoryRepository categoryRepository;

    @InjectMocks CategoryService categoryService;

    Category category;
    CategoryCreateDTO categoryCreateDTO;

    @BeforeEach
    void setup(){
        category = new Category(
                UUID.randomUUID(),
                "Test category",
                Instant.now(),
                Instant.now()
        );

        categoryCreateDTO = new CategoryCreateDTO(
                "Test category"
        );
    }

    @Test
    public void testFindAll_returnCategoryResponseDTOList(){
        when(categoryRepository.findAll())
                .thenReturn(Arrays.asList(category));

        List<CategoryResponseDTO> list = categoryService.findAll();
        assertNotEquals(0, list.toArray().length);

        var categoryResponse = list.getFirst();
        assertEquals(categoryResponse.getId(), category.getId());
        assertEquals(categoryResponse.getName(), category.getName());
        assertEquals(categoryResponse.getCreatedAt(), category.getCreatedAt());
        assertEquals(categoryResponse.getUpdatedAt(), category.getUpdatedAt());

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_returnEmptyList(){
        when(categoryRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<CategoryResponseDTO> list = categoryService.findAll();
        assertEquals(0, list.toArray().length);

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_returnCategoryResponseDTO(){
        var id = category.getId();
        when(categoryRepository.findById(id))
                .thenReturn(Optional.ofNullable(category));

        CategoryResponseDTO response = categoryService.findById(id);

        assertNotNull(response);
        assertEquals(response.getId(), id);
        assertEquals(response.getName(), category.getName());
        assertEquals(response.getCreatedAt(), category.getCreatedAt());
        assertEquals(response.getUpdatedAt(), category.getUpdatedAt());

        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_throwResourceNotFoundException(){
        var id = category.getId();
        when(categoryRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(
            ResourceNotFoundException.class,
                () -> categoryService.findById(id)
        );

        assertEquals("Category not found", e.getMessage());

        verify(categoryRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    public void testCreate_returnCategoryResponseDTO(){
        ArgumentCaptor<Category> argumentCaptor = ArgumentCaptor.forClass(Category.class);

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        CategoryResponseDTO response = categoryService.create(categoryCreateDTO);

        verify(categoryRepository, times(1)).save(argumentCaptor.capture());
        Category savedCategory = argumentCaptor.getValue();

        assertEquals(savedCategory.getName(), categoryCreateDTO.getName());

        assertEquals(response.getName(), categoryCreateDTO.getName());
        assertNotNull(response.getId());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());
    }

    @Test
    public void testUpdate_returnCategoryResponseDTO(){
        ArgumentCaptor<Category> argumentCaptor = ArgumentCaptor.forClass(Category.class);

        CategoryCreateDTO updatedCategory = new CategoryCreateDTO(
            "Updated category"
        );
        var id = category.getId();

        when(categoryRepository.findById(id))
                .thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        CategoryResponseDTO response = categoryService.update(updatedCategory, id);

        verify(categoryRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(updatedCategory.getName(), argumentCaptor.getValue().getName());
        assertEquals(id, argumentCaptor.getValue().getId());

        assertEquals(response.getId(), id);
        assertEquals(response.getName(), updatedCategory.getName());
        assertEquals(response.getCreatedAt(), category.getCreatedAt());
        assertEquals(response.getUpdatedAt(), category.getUpdatedAt());

        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdate_throwResourceNotFoundException() {
        CategoryCreateDTO updatedCategory = new CategoryCreateDTO(
                "Updated category"
        );
        var id = category.getId();

        when(categoryRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.update(updatedCategory, id)
        );

        assertEquals("Category not found", e.getMessage());

        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(0)).save(any(Category.class));
    }

    @Test
    public void testDelete_successfulDelete(){
        var id = category.getId();
        when(categoryRepository.existsById(id))
                .thenReturn(true);

        doNothing().when(categoryRepository).deleteById(id);

        categoryService.delete(id);

        verify(categoryRepository, times(1)).existsById(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDelete_throwResourceNotFoundException(){
        var id = category.getId();
        when(categoryRepository.existsById(any(UUID.class)))
                .thenReturn(false);

        ResourceNotFoundException e = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.delete(id)
        );

        assertEquals("Category not found", e.getMessage());
        verify(categoryRepository, times(1)).existsById(id);
        verify(categoryRepository, times(0)).deleteById(any(UUID.class));
    }
}
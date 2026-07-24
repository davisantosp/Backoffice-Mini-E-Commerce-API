package com.davisantosp.Backoffice_Mini_E_Commerce.services;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.ProductCreateDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.ProductResponseDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Category;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Product;
import com.davisantosp.Backoffice_Mini_E_Commerce.infra.exceptions.ResourceNotFoundException;
import com.davisantosp.Backoffice_Mini_E_Commerce.repositories.CategoryRepository;
import com.davisantosp.Backoffice_Mini_E_Commerce.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService")
class ProductServiceTest {

    @Mock ProductRepository productRepository;
    @Mock CategoryRepository categoryRepository;

    @InjectMocks ProductService productService;

    Product product;
    ProductCreateDTO productCreateDTO;
    Category category;

    @BeforeEach
    void setup() {
        category = new Category(
                UUID.randomUUID(),
                "Test category",
                Instant.now(),
                Instant.now()
        );

        product = new Product(
                UUID.randomUUID(),
                "Test Product",
                "Test Description",
                BigDecimal.valueOf(10.99),
                100,
                category,
                Instant.now(),
                Instant.now()
        );

        productCreateDTO = new ProductCreateDTO(
                "Test Product",
                "Test Description",
                BigDecimal.valueOf(10.99),
                100,
                category.getId()
        );
    }

    @Test
    public void testFindAll_returnProductResponseDTOList() {
        when(productRepository.findAll())
                .thenReturn(Arrays.asList(product));

        List<ProductResponseDTO> list = productService.findAll();
        assertNotEquals(0, list.toArray().length);

        var productResponse = list.getFirst();
        assertEquals(productResponse.getId(), product.getId());
        assertEquals(productResponse.getName(), product.getName());
        assertEquals(productResponse.getDescription(), product.getDescription());
        assertEquals(productResponse.getPrice(), product.getPrice());
        assertEquals(productResponse.getQuantityStored(), product.getQuantityStored());
        assertEquals(productResponse.getCategory(), product.getCategory());
        assertEquals(productResponse.getCreatedAt(), product.getCreatedAt());
        assertEquals(productResponse.getUpdatedAt(), product.getUpdatedAt());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_returnEmptyList() {
        when(productRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<ProductResponseDTO> list = productService.findAll();
        assertEquals(0, list.toArray().length);

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_returnProductResponseDTO() {
        var id = product.getId();
        when(productRepository.findById(id))
                .thenReturn(Optional.of(product));

        ProductResponseDTO response = productService.findById(id);

        assertNotNull(response);
        assertEquals(response.getId(), id);
        assertEquals(response.getName(), product.getName());
        assertEquals(response.getDescription(), product.getDescription());
        assertEquals(response.getPrice(), product.getPrice());
        assertEquals(response.getQuantityStored(), product.getQuantityStored());
        assertEquals(response.getCategory(), product.getCategory());
        assertEquals(response.getCreatedAt(), product.getCreatedAt());
        assertEquals(response.getUpdatedAt(), product.getUpdatedAt());

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_throwResourceNotFoundException() {
        var id = product.getId();
        when(productRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.findById(id)
        );

        assertEquals("Product not found", e.getMessage());

        verify(productRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    public void testCreate_returnProductResponseDTO() {
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        ProductResponseDTO response = productService.create(productCreateDTO);

        verify(productRepository, times(1)).save(argumentCaptor.capture());
        Product savedProduct = argumentCaptor.getValue();

        assertEquals(savedProduct.getName(), productCreateDTO.getName());
        assertEquals(savedProduct.getDescription(), productCreateDTO.getDescription());
        assertEquals(savedProduct.getPrice(), productCreateDTO.getPrice());
        assertEquals(savedProduct.getQuantityStored(), productCreateDTO.getQuantityStored());
        assertEquals(savedProduct.getCategory().getId(), productCreateDTO.getCategory_id());

        assertEquals(response.getName(), productCreateDTO.getName());
        assertNotNull(response.getId());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());

        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    public void testCreate_throwResourceNotFoundException_whenCategoryNotFound() {
        when(categoryRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.create(productCreateDTO)
        );

        assertEquals("Category not found.", e.getMessage());

        verify(categoryRepository, times(1)).findById(any(UUID.class));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    public void testUpdate_returnProductResponseDTO() {
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);

        ProductCreateDTO updatedProductDTO = new ProductCreateDTO(
                "Updated Product",
                "Updated Description",
                BigDecimal.valueOf(15.99),
                50,
                category.getId()
        );
        var id = product.getId();

        when(productRepository.findById(id))
                .thenReturn(Optional.of(product));
        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        ProductResponseDTO response = productService.update(updatedProductDTO, id);

        verify(productRepository, times(1)).save(argumentCaptor.capture());
        Product capturedProduct = argumentCaptor.getValue();

        assertEquals(updatedProductDTO.getName(), capturedProduct.getName());
        assertEquals(updatedProductDTO.getDescription(), capturedProduct.getDescription());
        assertEquals(updatedProductDTO.getPrice(), capturedProduct.getPrice());
        assertEquals(updatedProductDTO.getQuantityStored(), capturedProduct.getQuantityStored());
        assertEquals(updatedProductDTO.getCategory_id(), capturedProduct.getCategory().getId());
        assertEquals(id, capturedProduct.getId());

        assertEquals(response.getId(), id);
        assertEquals(response.getName(), updatedProductDTO.getName());
        assertEquals(response.getDescription(), updatedProductDTO.getDescription());
        assertEquals(response.getPrice(), updatedProductDTO.getPrice());
        assertEquals(response.getQuantityStored(), updatedProductDTO.getQuantityStored());
        assertEquals(response.getCategory(), category);

        verify(productRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    public void testUpdate_throwResourceNotFoundException_whenProductNotFound() {
        ProductCreateDTO updatedProductDTO = new ProductCreateDTO(
                "Updated Product",
                "Updated Description",
                BigDecimal.valueOf(15.99),
                50,
                category.getId()
        );
        var id = product.getId();

        when(productRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.update(updatedProductDTO, id)
        );

        assertEquals("Product not found", e.getMessage());

        verify(productRepository, times(1)).findById(id);
        verify(categoryRepository, times(0)).findById(any(UUID.class));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    public void testUpdate_throwResourceNotFoundException_whenCategoryNotFound() {
        ProductCreateDTO updatedProductDTO = new ProductCreateDTO(
                "Updated Product",
                "Updated Description",
                BigDecimal.valueOf(15.99),
                50,
                category.getId()
        );
        var id = product.getId();

        when(productRepository.findById(id))
                .thenReturn(Optional.of(product));
        when(categoryRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.update(updatedProductDTO, id)
        );

        assertEquals("Category not found.", e.getMessage());

        verify(productRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).findById(any(UUID.class));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    public void testDelete_successfulDelete() {
        var id = product.getId();
        when(productRepository.existsById(id))
                .thenReturn(true);

        doNothing().when(productRepository).deleteById(id);

        productService.delete(id);

        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDelete_throwResourceNotFoundException() {
        var id = product.getId();
        when(productRepository.existsById(any(UUID.class)))
                .thenReturn(false);

        ResourceNotFoundException e = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.delete(id)
        );

        assertEquals("Product not found", e.getMessage());
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, times(0)).deleteById(any(UUID.class));
    }
}
package com.davisantosp.Backoffice_Mini_E_Commerce.services;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.CategoryResponseDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.ProductCreateDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.DTOs.ProductResponseDTO;
import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Product;
import com.davisantosp.Backoffice_Mini_E_Commerce.infra.exceptions.ResourceNotFoundException;
import com.davisantosp.Backoffice_Mini_E_Commerce.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDTO> findAll(){
        return productRepository.findAll()
                .stream()
                .map(this::fromProduct)
                .toList();
    }
    public ProductResponseDTO findById(UUID id){
        Product product = productRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Category not found"));

        return this.fromProduct(product);
    }

    public ProductResponseDTO create(ProductCreateDTO productCreateDTO){
        Product product = productRepository.save(
                this.fromProductCreateDTO(productCreateDTO)
        );

        return this.fromProduct(product);
    }

    public ProductResponseDTO update(ProductCreateDTO updatedProductCreateDTO, UUID id){
        Product product = productRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Category not found"));

        product.setName(updatedProductCreateDTO.getName());
        product.setDescription(updatedProductCreateDTO.getDescription());
        product.setCategory(updatedProductCreateDTO.getCategory());
        product.setPrice(updatedProductCreateDTO.getPrice());
        product.setQuantityStored(updatedProductCreateDTO.getQuantityStored());

        Product updatedProduct = productRepository.save(product);
        return this.fromProduct(updatedProduct);
    }

    public void delete(UUID id){
        if(!productRepository.existsById(id))
            throw new ResourceNotFoundException("Category not found");
        productRepository.deleteById(id);
    }

    private ProductResponseDTO fromProduct(Product product){
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantityStored(),
                product.getCategory(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    private Product fromProductCreateDTO(ProductCreateDTO productCreateDTO){
        return new Product(
                productCreateDTO.getName(),
                productCreateDTO.getDescription(),
                productCreateDTO.getPrice(),
                productCreateDTO.getQuantityStored(),
                productCreateDTO.getCategory()
        );
    }
}

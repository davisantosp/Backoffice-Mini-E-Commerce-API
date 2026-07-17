package com.davisantosp.Backoffice_Mini_E_Commerce.repositories;

import com.davisantosp.Backoffice_Mini_E_Commerce.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
}

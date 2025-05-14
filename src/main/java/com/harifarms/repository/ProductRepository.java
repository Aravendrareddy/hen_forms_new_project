package com.harifarms.repository;

import com.harifarms.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByHenBreed(String henBreed);
    List<Product> findByIsAvailable(boolean isAvailable);
    List<Product> findByCategoryAndIsAvailable(String category, boolean isAvailable);
}
package com.harifarms.service;

import com.harifarms.model.Product;
import com.harifarms.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
    
    public List<Product> findAvailableProducts() {
        return productRepository.findByIsAvailable(true);
    }
    
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    public List<Product> findByHenBreed(String henBreed) {
        return productRepository.findByHenBreed(henBreed);
    }
    
    public List<Product> findAvailableProductsByCategory(String category) {
        return productRepository.findByCategoryAndIsAvailable(category, true);
    }
    
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    @Transactional
    public Product updateProductStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        int newStock = product.getStockQuantity() - quantity;
        if (newStock < 0) {
            throw new RuntimeException("Not enough stock available");
        }
        
        product.setStockQuantity(newStock);
        if (newStock == 0) {
            product.setAvailable(false);
        }
        
        return productRepository.save(product);
    }
}
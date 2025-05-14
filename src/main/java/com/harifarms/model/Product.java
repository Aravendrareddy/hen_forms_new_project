package com.harifarms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, length = 1000)
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(nullable = false)
    private String category;
    
    @Column(name = "hen_breed")
    private String henBreed;
    
    @Column(name = "average_weight")
    private Double averageWeight;
    
    @Column(name = "egg_production_rate")
    private String eggProductionRate;
    
    @Column(name = "is_available")
    private boolean isAvailable = true;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();
    
    @Transient
    public Double getAverageRating() {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
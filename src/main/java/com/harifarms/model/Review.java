package com.harifarms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer rating;
    
    @Column(nullable = false, length = 1000)
    private String comment;
    
    @Column(name = "review_date", nullable = false)
    private LocalDateTime reviewDate = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
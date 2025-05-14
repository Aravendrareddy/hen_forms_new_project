package com.harifarms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;
    
    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @PrePersist
    @PreUpdate
    private void calculateSubtotal() {
        this.subtotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }
}
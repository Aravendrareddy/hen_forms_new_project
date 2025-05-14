package com.harifarms.service;

import com.harifarms.model.Order;
import com.harifarms.model.OrderItem;
import com.harifarms.model.Product;
import com.harifarms.model.User;
import com.harifarms.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductService productService;
    
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
    
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    public List<Order> findByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    public List<Order> findByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    @Transactional
    public Order createOrder(User user, Set<OrderItem> orderItems, String shippingAddress, String paymentMethod) {
        // Calculate total amount
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> {
                    Product product = productService.findById(item.getProduct().getId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    
                    // Verify stock availability
                    if (product.getStockQuantity() < item.getQuantity()) {
                        throw new RuntimeException("Not enough stock for " + product.getName());
                    }
                    
                    // Set the correct unit price from product
                    item.setUnitPrice(product.getPrice());
                    
                    // Calculate subtotal
                    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(Order.OrderStatus.PENDING);
        
        // Save order first to get ID
        Order savedOrder = orderRepository.save(order);
        
        // Set order reference in each item and save
        orderItems.forEach(item -> {
            item.setOrder(savedOrder);
            
            // Update product stock
            productService.updateProductStock(item.getProduct().getId(), item.getQuantity());
        });
        
        savedOrder.setOrderItems(orderItems);
        
        return orderRepository.save(savedOrder);
    }
    
    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(status);
        
        // Set delivery date if status is DELIVERED
        if (status == Order.OrderStatus.DELIVERED) {
            order.setDeliveryDate(LocalDateTime.now());
        }
        
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order updatePaymentStatus(Long orderId, String paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setPaymentStatus(paymentStatus);
        
        return orderRepository.save(order);
    }
}
package com.harifarms.controller;

import com.harifarms.model.OrderItem;
import com.harifarms.model.Product;
import com.harifarms.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final ProductService productService;
    
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        List<OrderItem> cartItems = getCartItems(session);
        BigDecimal total = calculateTotal(cartItems);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", total);
        
        return "cart/view";
    }
    
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam Integer quantity, HttpSession session) {
        // Validate product exists
        Product product = productService.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Validate quantity
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be positive");
        }
        
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }
        
        List<OrderItem> cartItems = getCartItems(session);
        
        // Check if product already in cart
        Optional<OrderItem> existingItem = cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
        
        if (existingItem.isPresent()) {
            // Update quantity
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        } else {
            // Add new item
            OrderItem newItem = new OrderItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(product.getPrice());
            newItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cartItems.add(newItem);
        }
        
        session.setAttribute("cartItems", cartItems);
        
        return "redirect:/cart";
    }
    
    @PostMapping("/update")
    public String updateCartItem(@RequestParam Long productId, @RequestParam Integer quantity, HttpSession session) {
        List<OrderItem> cartItems = getCartItems(session);
        
        // Find the item
        Optional<OrderItem> existingItem = cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
        
        if (existingItem.isPresent()) {
            OrderItem item = existingItem.get();
            
            if (quantity <= 0) {
                // Remove item if quantity <= 0
                cartItems.remove(item);
            } else {
                // Update quantity
                item.setQuantity(quantity);
                item.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
            }
            
            session.setAttribute("cartItems", cartItems);
        }
        
        return "redirect:/cart";
    }
    
    @PostMapping("/remove")
    public String removeCartItem(@RequestParam Long productId, HttpSession session) {
        List<OrderItem> cartItems = getCartItems(session);
        
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
        session.setAttribute("cartItems", cartItems);
        
        return "redirect:/cart";
    }
    
    @GetMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cartItems");
        return "redirect:/cart";
    }
    
    private List<OrderItem> getCartItems(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<OrderItem> cartItems = (List<OrderItem>) session.getAttribute("cartItems");
        
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            session.setAttribute("cartItems", cartItems);
        }
        
        return cartItems;
    }
    
    private BigDecimal calculateTotal(List<OrderItem> cartItems) {
        return cartItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
package com.harifarms.controller;

import com.harifarms.model.Order;
import com.harifarms.model.OrderItem;
import com.harifarms.model.User;
import com.harifarms.service.OrderService;
import com.harifarms.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    private final UserService userService;
    
    @GetMapping
    public String viewOrders(Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        List<Order> orders = orderService.findByUser(user);
        
        model.addAttribute("orders", orders);
        return "orders/list";
    }
    
    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable Long id, Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Security check - ensure user can only view their own orders
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        model.addAttribute("order", order);
        return "orders/details";
    }
    
    @PostMapping("/checkout")
    public String checkout(@RequestParam String shippingAddress, @RequestParam String paymentMethod,
                          Authentication authentication, HttpSession session, Model model) {
        User user = getCurrentUser(authentication);
        
        @SuppressWarnings("unchecked")
        List<OrderItem> cartItems = (List<OrderItem>) session.getAttribute("cartItems");
        
        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart?error=empty";
        }
        
        try {
            Set<OrderItem> orderItems = new HashSet<>(cartItems);
            Order order = orderService.createOrder(user, orderItems, shippingAddress, paymentMethod);
            
            // Clear cart
            session.removeAttribute("cartItems");
            
            return "redirect:/orders/" + order.getId() + "?success=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/cart?error=checkout";
        }
    }
    
    @GetMapping("/checkout")
    public String showCheckoutForm(Authentication authentication, HttpSession session, Model model) {
        User user = getCurrentUser(authentication);
        
        @SuppressWarnings("unchecked")
        List<OrderItem> cartItems = (List<OrderItem>) session.getAttribute("cartItems");
        
        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart?error=empty";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);
        return "orders/checkout";
    }
    
    private User getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
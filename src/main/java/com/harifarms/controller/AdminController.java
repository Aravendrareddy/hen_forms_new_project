package com.harifarms.controller;

import com.harifarms.model.Order;
import com.harifarms.model.Product;
import com.harifarms.service.OrderService;
import com.harifarms.service.ProductService;
import com.harifarms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    
    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("userCount", userService.findAllUsers().size());
        model.addAttribute("productCount", productService.findAllProducts().size());
        model.addAttribute("orderCount", orderService.findAllOrders().size());
        model.addAttribute("pendingOrderCount", orderService.findByStatus(Order.OrderStatus.PENDING).size());
        
        return "admin/dashboard";
    }
    
    // Product Management
    @GetMapping("/products")
    public String productList(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "admin/products/list";
    }
    
    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/products/form";
    }
    
    @PostMapping("/products/add")
    public String addProduct(@Valid @ModelAttribute Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/products/form";
        }
        
        productService.saveProduct(product);
        return "redirect:/admin/products?added=true";
    }
    
    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        model.addAttribute("product", product);
        return "admin/products/form";
    }
    
    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/products/form";
        }
        
        productService.saveProduct(product);
        return "redirect:/admin/products?updated=true";
    }
    
    @GetMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products?deleted=true";
    }
    
    // Order Management
    @GetMapping("/orders")
    public String orderList(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        return "admin/orders/list";
    }
    
    @GetMapping("/orders/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        model.addAttribute("order", order);
        return "admin/orders/details";
    }
    
    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam Order.OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders/" + id + "?updated=true";
    }
    
    // User Management
    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users/list";
    }
    
    @GetMapping("/users/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
        return "admin/users/details";
    }
}
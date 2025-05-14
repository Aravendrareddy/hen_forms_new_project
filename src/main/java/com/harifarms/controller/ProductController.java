package com.harifarms.controller;

import com.harifarms.model.Product;
import com.harifarms.model.Review;
import com.harifarms.service.ProductService;
import com.harifarms.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    private final ReviewService reviewService;
    
    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.findAvailableProducts());
        return "products/list";
    }
    
    @GetMapping("/{id}")
    public String getProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        List<Review> reviews = reviewService.findByProduct(product);
        
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        
        return "products/details";
    }
    
    @GetMapping("/category/{category}")
    public String getProductsByCategory(@PathVariable String category, Model model) {
        model.addAttribute("products", productService.findAvailableProductsByCategory(category));
        model.addAttribute("categoryName", category);
        return "products/category";
    }
    
    @GetMapping("/search")
    public String searchProducts(@RequestParam String query, Model model) {
        // Implement search logic here (could filter by name, description, or other fields)
        // For simplicity, just showing all products for now
        model.addAttribute("products", productService.findAvailableProducts());
        model.addAttribute("searchQuery", query);
        return "products/search-results";
    }
}
package com.harifarms.controller;

import com.harifarms.model.Product;
import com.harifarms.model.Review;
import com.harifarms.model.User;
import com.harifarms.service.ProductService;
import com.harifarms.service.ReviewService;
import com.harifarms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;
    private final ProductService productService;
    private final UserService userService;
    
    @GetMapping("/product/{productId}")
    public String showReviewForm(@PathVariable Long productId, Model model) {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        model.addAttribute("product", product);
        model.addAttribute("review", new Review());
        
        return "reviews/form";
    }
    
    @PostMapping("/product/{productId}")
    public String submitReview(@PathVariable Long productId, @Valid @ModelAttribute Review review,
                               BindingResult result, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            Product product = productService.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            
            model.addAttribute("product", product);
            return "reviews/form";
        }
        
        Product product = productService.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        User user = getCurrentUser(authentication);
        
        review.setProduct(product);
        review.setUser(user);
        
        reviewService.saveReview(review);
        
        return "redirect:/products/" + productId;
    }
    
    @GetMapping("/{id}/delete")
    public String deleteReview(@PathVariable Long id, Authentication authentication) {
        Review review = reviewService.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        User user = getCurrentUser(authentication);
        
        // Security check - ensure user can only delete their own reviews
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        reviewService.deleteReview(id);
        
        return "redirect:/products/" + review.getProduct().getId();
    }
    
    private User getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
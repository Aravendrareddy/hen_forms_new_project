package com.harifarms.controller;

import com.harifarms.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final ProductService productService;
    
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("featuredProducts", productService.findAvailableProducts());
        return "home";
    }
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}
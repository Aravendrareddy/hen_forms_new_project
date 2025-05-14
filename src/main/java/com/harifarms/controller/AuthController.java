package com.harifarms.controller;

import com.harifarms.model.User;
import com.harifarms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        try {
            userService.registerUser(user);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            result.rejectValue("username", "error.user", e.getMessage());
            return "auth/register";
        }
    }
}
package com.harifarms.controller;

import com.harifarms.model.Address;
import com.harifarms.model.User;
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
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public String viewProfile(Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        model.addAttribute("user", user);
        return "users/profile";
    }
    
    @GetMapping("/edit")
    public String editProfileForm(Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        model.addAttribute("user", user);
        return "users/edit-profile";
    }
    
    @PostMapping("/edit")
    public String updateProfile(@Valid @ModelAttribute User updatedUser, BindingResult result,
                               Authentication authentication, Model model) {
        if (result.hasErrors()) {
            return "users/edit-profile";
        }
        
        User currentUser = getCurrentUser(authentication);
        
        // Update only allowed fields
        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setEmail(updatedUser.getEmail());
        
        userService.updateUser(currentUser);
        
        return "redirect:/profile?updated=true";
    }
    
    @GetMapping("/addresses")
    public String viewAddresses(Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        model.addAttribute("user", user);
        model.addAttribute("addresses", user.getAddresses());
        return "users/addresses";
    }
    
    @GetMapping("/addresses/add")
    public String addAddressForm(Model model) {
        model.addAttribute("address", new Address());
        return "users/add-address";
    }
    
    @PostMapping("/addresses/add")
    public String addAddress(@Valid @ModelAttribute Address address, BindingResult result,
                            Authentication authentication) {
        if (result.hasErrors()) {
            return "users/add-address";
        }
        
        User user = getCurrentUser(authentication);
        
        address.setUser(user);
        user.getAddresses().add(address);
        
        userService.updateUser(user);
        
        return "redirect:/profile/addresses?added=true";
    }
    
    private User getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
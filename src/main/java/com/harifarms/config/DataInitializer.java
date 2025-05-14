package com.harifarms.config;

import com.harifarms.model.Product;
import com.harifarms.model.User;
import com.harifarms.service.ProductService;
import com.harifarms.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
@Profile("!prod")
public class DataInitializer {
    
    private final UserService userService;
    private final ProductService productService;
    
    @PostConstruct
    public void init() {
        // Create admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123"); // Will be encoded by UserService
        admin.setEmail("admin@harifarms.com");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRoles(new HashSet<>(Arrays.asList("ROLE_ADMIN", "ROLE_USER")));
        
        try {
            userService.registerUser(admin);
        } catch (Exception e) {
            // User might already exist
        }
        
        // Create sample products
        createSampleProduct(
                "Country Hen",
                "Traditional country hen known for its flavorful eggs and meat. These hens are free-range and naturally raised.",
                new BigDecimal("450.00"),
                50,
                "https://images.pexels.com/photos/6246881/pexels-photo-6246881.jpeg",
                "Egg Layers",
                "Country/Desi",
                1.5,
                "150-180 eggs per year"
        );
        
        createSampleProduct(
                "White Leghorn",
                "Excellent egg layer with white feathers. Known for high egg production and feed efficiency.",
                new BigDecimal("550.00"),
                40,
                "https://images.pexels.com/photos/2255564/pexels-photo-2255564.jpeg",
                "Egg Layers",
                "Leghorn",
                1.8,
                "280-320 eggs per year"
        );
        
        createSampleProduct(
                "Rhode Island Red",
                "Dual-purpose breed good for both egg laying and meat. Hardy and adaptable to various climates.",
                new BigDecimal("600.00"),
                30,
                "https://images.pexels.com/photos/7706440/pexels-photo-7706440.jpeg",
                "Dual Purpose",
                "Rhode Island Red",
                2.2,
                "250-300 eggs per year"
        );
        
        createSampleProduct(
                "Broiler Chicken",
                "Fast growing meat-purpose chicken breed. Ready for market in just 6-8 weeks.",
                new BigDecimal("350.00"),
                60,
                "https://images.pexels.com/photos/5528990/pexels-photo-5528990.jpeg",
                "Meat Purpose",
                "Broiler",
                2.5,
                "N/A"
        );
        
        createSampleProduct(
                "Kadaknath",
                "Indian breed known for its black meat, high protein content and medicinal properties.",
                new BigDecimal("850.00"),
                20,
                "https://images.pexels.com/photos/7706446/pexels-photo-7706446.jpeg",
                "Premium Breeds",
                "Kadaknath",
                1.3,
                "80-120 eggs per year"
        );
    }
    
    private void createSampleProduct(String name, String description, BigDecimal price, 
                                    Integer stockQuantity, String imageUrl, String category, 
                                    String henBreed, Double averageWeight, String eggProductionRate) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        product.setHenBreed(henBreed);
        product.setAverageWeight(averageWeight);
        product.setEggProductionRate(eggProductionRate);
        product.setAvailable(true);
        
        productService.saveProduct(product);
    }
}
package com.harifarms.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        // Public pages
                        .requestMatchers("/", "/home", "/products/**", "/images/**", "/css/**", "/js/**", "/webjars/**", "/h2-console/**").permitAll()
                        .requestMatchers("/register", "/login").permitAll()
                        // Admin pages
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // User pages
                        .requestMatchers("/cart/**", "/checkout/**", "/orders/**", "/reviews/**", "/profile/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .headers(headers -> headers.frameOptions().disable()); // For H2 Console
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
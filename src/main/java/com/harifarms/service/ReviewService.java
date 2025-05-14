package com.harifarms.service;

import com.harifarms.model.Product;
import com.harifarms.model.Review;
import com.harifarms.model.User;
import com.harifarms.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    
    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
    }
    
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }
    
    public List<Review> findByProduct(Product product) {
        return reviewRepository.findByProductOrderByReviewDateDesc(product);
    }
    
    public List<Review> findByUser(User user) {
        return reviewRepository.findByUser(user);
    }
    
    public Double getAverageRatingForProduct(Product product) {
        return reviewRepository.findAverageRatingByProduct(product);
    }
    
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
    
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
package com.harifarms.repository;

import com.harifarms.model.Product;
import com.harifarms.model.Review;
import com.harifarms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
    List<Review> findByUser(User user);
    List<Review> findByProductOrderByReviewDateDesc(Product product);
    Double findAverageRatingByProduct(Product product);
}
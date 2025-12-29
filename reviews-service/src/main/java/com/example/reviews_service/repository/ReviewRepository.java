package com.example.reviews_service.repository;

import com.example.reviews_service.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByRestaurantId(Long restaurantId, Pageable pageable);
    Page<Review> findByUserId(Long userId, Pageable pageable);
}


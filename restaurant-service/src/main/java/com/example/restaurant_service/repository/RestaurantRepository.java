package com.example.restaurant_service.repository;

import com.example.restaurant_service.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByIsActiveTrue();

    Optional<Restaurant> findByIdAndIsActiveTrue(Long id);

    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true AND r.name LIKE %:keyword%")
    List<Restaurant> searchByName(String keyword);

    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true AND r.rating >= :minRating ORDER BY r.rating DESC")
    List<Restaurant> findByRatingGreaterThanEqual(Double minRating);
}

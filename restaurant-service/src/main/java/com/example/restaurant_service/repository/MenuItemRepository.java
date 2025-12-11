package com.example.restaurant_service.repository;

import com.example.restaurant_service.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantId(Long restaurantId);

    List<MenuItem> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);

    @Query("SELECT m FROM MenuItem m JOIN m.categories c WHERE c.id = :categoryId AND m.isAvailable = true")
    List<MenuItem> findByCategoryId(Long categoryId);

    @Query("SELECT m FROM MenuItem m WHERE m.id IN :ids")
    List<MenuItem> findAllByIdIn(Iterable<Long> ids);

    List<MenuItem> findByIsAvailableTrue();
}

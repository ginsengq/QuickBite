package com.example.reviews_service.controller;

import com.example.reviews_service.dto.ReviewDto;
import com.example.reviews_service.entity.Review;
import com.example.reviews_service.service.ReviewService;
import com.example.reviews_service.repository.ReviewRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto dto) {
        ReviewDto created = reviewService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Page<ReviewDto>> getByRestaurant(@PathVariable Long restaurantId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Page<Review> p = reviewRepository.findByRestaurantId(restaurantId, PageRequest.of(page, size));
        Page<ReviewDto> res = p.map(r -> {
            ReviewDto dto = new ReviewDto();
            BeanUtils.copyProperties(r, dto);
            return dto;
        });
        return ResponseEntity.ok(res);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReviewDto>> getByUser(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Page<Review> p = reviewRepository.findByUserId(userId, PageRequest.of(page, size));
        Page<ReviewDto> res = p.map(r -> {
            ReviewDto dto = new ReviewDto();
            BeanUtils.copyProperties(r, dto);
            return dto;
        });
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getById(@PathVariable Long id) {
        return reviewRepository.findById(id)
                .map(r -> {
                    ReviewDto dto = new ReviewDto();
                    BeanUtils.copyProperties(r, dto);
                    return ResponseEntity.ok(dto);
                }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/moderate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewDto> moderate(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null) return ResponseEntity.badRequest().build();
        ReviewDto out = reviewService.moderate(id, status);
        return ResponseEntity.ok(out);
    }
}

package com.example.reviews_service.service;

import com.example.reviews_service.dto.ReviewDto;
import com.example.reviews_service.entity.Review;
import com.example.reviews_service.kafka.ReviewEventProducer;
import com.example.reviews_service.repository.ReviewRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewEventProducer producer;

    public ReviewDto create(ReviewDto dto) {
        if (dto.rating == null || dto.rating < 1 || dto.rating > 5) {
            throw new IllegalArgumentException("rating must be between 1 and 5");
        }
        Review r = new Review();
        BeanUtils.copyProperties(dto, r);
        r.setStatus("PENDING");
        Review saved = reviewRepository.save(r);
        ReviewDto out = new ReviewDto();
        BeanUtils.copyProperties(saved, out);
        producer.publishReviewCreated(out);
        return out;
    }

    public ReviewDto moderate(Long id, String status) {
        Review r = reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        r.setStatus(status);
        Review saved = reviewRepository.save(r);
        ReviewDto out = new ReviewDto();
        BeanUtils.copyProperties(saved, out);
        return out;
    }
}

package ma.emsi.restaurantservice.service;

import ma.emsi.restaurantservice.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    ReviewDto addReview(ReviewDto dto);
    ReviewDto updateReview(Long id, ReviewDto dto);
    void deleteReview(Long id);
    ReviewDto getById(Long id);  // ADD THIS
    List<ReviewDto> getReviewsForRestaurant(Long restaurantId);
}

package ma.emsi.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import ma.emsi.restaurantservice.dto.ReviewDto;
import ma.emsi.restaurantservice.entity.Restaurant;
import ma.emsi.restaurantservice.entity.Review;
import ma.emsi.restaurantservice.mapper.ReviewMapper;
import ma.emsi.restaurantservice.repository.RestaurantRepository;
import ma.emsi.restaurantservice.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewMapper mapper;

    @Override
    @Transactional
    public ReviewDto addReview(ReviewDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Review review = mapper.toEntity(dto);
        review.setRestaurant(restaurant);

        Review saved = reviewRepository.save(review);

        // Recalculate average rating using fresh query
        updateRestaurantRating(restaurant.getId());

        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public ReviewDto updateReview(Long id, ReviewDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setComment(dto.getComment());
        review.setRating(dto.getRating());

        Review updated = reviewRepository.save(review);

        // Recalculate average rating
        updateRestaurantRating(review.getRestaurant().getId());

        return mapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        Long restaurantId = review.getRestaurant().getId();

        reviewRepository.deleteById(id);

        // Recalculate average rating after deletion
        updateRestaurantRating(restaurantId);
    }

    @Override
    public List<ReviewDto> getReviewsForRestaurant(Long restaurantId) {
        return reviewRepository.findByRestaurant_Id(restaurantId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDto getById(Long id) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return mapper.toDto(r);
    }

    // Helper method to update restaurant rating
    private void updateRestaurantRating(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        List<Review> allReviews = reviewRepository.findByRestaurant_Id(restaurantId);

        if (allReviews.isEmpty()) {
            restaurant.setAverageRating(0.0);
        } else {
            double average = allReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            restaurant.setAverageRating(Math.round(average * 10.0) / 10.0);
        }

        restaurantRepository.save(restaurant);
    }
}
package ma.emsi.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import ma.emsi.restaurantservice.dto.ReviewDto;
import ma.emsi.restaurantservice.entity.Restaurant;
import ma.emsi.restaurantservice.entity.Review;
import ma.emsi.restaurantservice.mapper.ReviewMapper;
import ma.emsi.restaurantservice.repository.RestaurantRepository;
import ma.emsi.restaurantservice.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewMapper mapper;

    @Override
    public ReviewDto addReview(ReviewDto dto) {
        Restaurant r = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Review review = mapper.toEntity(dto);
        review.setRestaurant(r);

        Review saved = reviewRepository.save(review);
        return mapper.toDto(saved);
    }

    @Override
    public ReviewDto getById(Long id) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return mapper.toDto(r);
    }

    @Override
    public ReviewDto updateReview(Long id, ReviewDto dto) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        r.setComment(dto.getComment());
        r.setRating(dto.getRating());

        return mapper.toDto(reviewRepository.save(r));
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public List<ReviewDto> getReviewsForRestaurant(Long restaurantId) {
        return reviewRepository.findByRestaurant_Id(restaurantId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}

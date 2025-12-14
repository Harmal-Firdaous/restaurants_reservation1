package ma.emsi.restaurantservice.mapper;

import ma.emsi.restaurantservice.dto.ReviewDto;
import ma.emsi.restaurantservice.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDto toDto(Review r) {
        if (r == null) return null;

        return ReviewDto.builder()
                .id(r.getId())
                .restaurantId(r.getRestaurant() != null ? r.getRestaurant().getId() : null)
                .userId(r.getUserId())
                .rating(r.getRating())
                .comment(r.getComment())
                .build();
    }

    public Review toEntity(ReviewDto dto) {
        if (dto == null) return null;

        return Review.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
    }
}

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
                .rating(r.getRating())
                .comment(r.getComment())
                .restaurantId(r.getRestaurant() != null ? r.getRestaurant().getId() : null)
                .userId(r.getUserId())
                .build();
    }

    public Review toEntity(ReviewDto dto) {
        if (dto == null) return null;
        Review r = new Review();
        r.setId(dto.getId());
        r.setRating(dto.getRating());
        r.setComment(dto.getComment());
        r.setUserId(dto.getUserId());
        // restaurant assignment must be done in service
        return r;
    }
}

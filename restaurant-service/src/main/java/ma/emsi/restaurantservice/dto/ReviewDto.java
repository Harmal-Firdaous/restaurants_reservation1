package ma.emsi.restaurantservice.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewDto {
    private Long id;
    private Long restaurantId;
    private Long userId;
    private Integer rating;
    private String comment;
}

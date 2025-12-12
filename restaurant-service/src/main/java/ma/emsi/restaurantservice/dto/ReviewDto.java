package ma.emsi.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private int rating;
    private String comment;
    private Long restaurantId;
    private Long userId; // venant du USER-SERVICE
}

package ma.emsi.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.restaurantservice.enums.CuisineType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {
    private Long id;
    private String name;
    private CuisineType cuisineType;
    private String address;
    private Double latitude;
    private Double longitude;
    private String googlePlaceId;
    private Double averageRating;
}
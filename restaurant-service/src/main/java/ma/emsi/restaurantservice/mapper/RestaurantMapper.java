package ma.emsi.restaurantservice.mapper;

import ma.emsi.restaurantservice.dto.RestaurantDto;
import ma.emsi.restaurantservice.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    public RestaurantDto toDto(Restaurant r) {
        if (r == null) return null;

        return RestaurantDto.builder()
                .id(r.getId())
                .name(r.getName())
                .address(r.getAddress())
                .latitude(r.getLatitude())
                .longitude(r.getLongitude())
                .googlePlaceId(r.getGooglePlaceId())
                .cuisineType(r.getCuisineType())
                .averageRating(r.getAverageRating() != null ? r.getAverageRating() : 0.0)
                .build();
    }

    public Restaurant toEntity(RestaurantDto dto) {
        if (dto == null) return null;

        return Restaurant.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .googlePlaceId(dto.getGooglePlaceId())
                .cuisineType(dto.getCuisineType())
                .build();
    }
}
package ma.emsi.restaurantservice.service;

import ma.emsi.restaurantservice.dto.RestaurantDto;
import ma.emsi.restaurantservice.enums.CuisineType;

import java.util.List;

public interface RestaurantService {
    RestaurantDto create(RestaurantDto dto);
    RestaurantDto getById(Long id);
    List<RestaurantDto> getAll();  // ADD THIS
    List<RestaurantDto> searchByName(String q);
    List<RestaurantDto> findByCuisineType(CuisineType cuisineType);
    List<RestaurantDto> findByRatingRange(Double minRating, Double maxRating);  // ADD THIS
    void delete(Long id);
}
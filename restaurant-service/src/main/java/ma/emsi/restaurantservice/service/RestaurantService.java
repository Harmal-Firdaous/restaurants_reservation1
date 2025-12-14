package ma.emsi.restaurantservice.service;

import ma.emsi.restaurantservice.dto.RestaurantDto;
import ma.emsi.restaurantservice.enums.CuisineType;

import java.util.List;

public interface RestaurantService {
    void delete(Long id);
    RestaurantDto create(RestaurantDto dto);
    RestaurantDto getById(Long id);
    List<RestaurantDto> searchByName(String q);
    List<RestaurantDto> findByCuisineType(CuisineType cuisineType);
}

package ma.emsi.restaurantservice.service;

import ma.emsi.restaurantservice.dto.RestaurantDto;

import java.util.List;

public interface RestaurantService {
    RestaurantDto create(RestaurantDto dto);
    RestaurantDto getById(Long id);
    List<RestaurantDto> searchByName(String q);
}

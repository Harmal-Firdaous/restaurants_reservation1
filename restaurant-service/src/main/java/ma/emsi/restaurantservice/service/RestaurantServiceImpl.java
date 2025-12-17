package ma.emsi.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import ma.emsi.restaurantservice.client.GeoClient;
import ma.emsi.restaurantservice.dto.PlaceDetailsDto;
import ma.emsi.restaurantservice.dto.RestaurantDto;
import ma.emsi.restaurantservice.entity.Restaurant;
import ma.emsi.restaurantservice.enums.CuisineType;
import ma.emsi.restaurantservice.mapper.RestaurantMapper;
import ma.emsi.restaurantservice.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;
    private final GeoClient geoClient;

    @Override
    public RestaurantDto create(RestaurantDto dto) {

        // Enrichissement via GeoService
        if (dto.getGooglePlaceId() != null && !dto.getGooglePlaceId().isEmpty()) {

            PlaceDetailsDto place =
                    geoClient.getPlaceDetails(dto.getGooglePlaceId());

            dto.setName(place.getName());
            dto.setAddress(place.getAddress());
            dto.setLatitude(place.getLat());
            dto.setLongitude(place.getLng());
            dto.setAverageRating(place.getRating());
        }

        Restaurant restaurant = mapper.toEntity(dto);

        if (restaurant.getAverageRating() == null) {
            restaurant.setAverageRating(0.0);
        }

        return mapper.toDto(repository.save(restaurant));
    }

    @Override
    public RestaurantDto getById(Long id) {
        Restaurant r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        return mapper.toDto(r);
    }

    @Override
    public List<RestaurantDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantDto> findByCuisineType(CuisineType cuisineType) {
        return repository.findByCuisineType(cuisineType)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantDto> searchByName(String q) {
        return repository.findByNameContainingIgnoreCase(q)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantDto> findByRatingRange(Double minRating, Double maxRating) {
        return repository.findByAverageRatingBetween(minRating, maxRating)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

package ma.emsi.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import ma.emsi.restaurantservice.dto.RestaurantDto;
import ma.emsi.restaurantservice.entity.Restaurant;
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

    @Override
    public RestaurantDto create(RestaurantDto dto) {
        Restaurant r = mapper.toEntity(dto);
        Restaurant saved = repository.save(r);
        return mapper.toDto(saved);
    }

    @Override
    public RestaurantDto getById(Long id) {
        Restaurant r = repository.findById(id).orElseThrow();
        return mapper.toDto(r);
    }

    @Override
    public List<RestaurantDto> searchByName(String q) {
        return repository.findByNameContainingIgnoreCase(q).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
package ma.emsi.restaurantservice.service;

import ma.emsi.restaurantservice.dto.PlatDto;

import java.util.List;

public interface PlatService {
    PlatDto create(Long menuId, PlatDto dto);
    PlatDto update(Long id, PlatDto dto);
    void delete(Long id);
    PlatDto getById(Long id);
    List<PlatDto> getByMenu(Long menuId);
}
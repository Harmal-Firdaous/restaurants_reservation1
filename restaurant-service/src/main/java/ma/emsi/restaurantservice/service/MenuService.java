package ma.emsi.restaurantservice.service;

import ma.emsi.restaurantservice.dto.MenuDto;
import java.util.List;

public interface MenuService {

    MenuDto create(Long restaurantId, MenuDto dto);

    MenuDto update(Long id, MenuDto dto);

    void delete(Long id);

    List<MenuDto> getByRestaurant(Long restaurantId);
}

package ma.emsi.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import ma.emsi.restaurantservice.dto.MenuDto;
import ma.emsi.restaurantservice.entity.Menu;
import ma.emsi.restaurantservice.entity.Plat;
import ma.emsi.restaurantservice.entity.Restaurant;
import ma.emsi.restaurantservice.mapper.MenuMapper;
import ma.emsi.restaurantservice.mapper.PlatMapper;
import ma.emsi.restaurantservice.repository.MenuRepository;
import ma.emsi.restaurantservice.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuMapper menuMapper;
    private final PlatMapper platMapper;

    @Override
    public MenuDto create(Long restaurantId, MenuDto dto) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Menu menu = new Menu();
        menu.setTitle(dto.getTitle());
        menu.setRestaurant(restaurant);

        List<Plat> plats = new ArrayList<>();

        if (dto.getPlats() != null) {
            for (var platDto : dto.getPlats()) {
                Plat plat = platMapper.toEntity(platDto);
                plat.setMenu(menu);
                plats.add(plat);
            }
        }

        menu.setPlats(plats);

        Menu saved = menuRepository.save(menu);
        return menuMapper.toDto(saved);
    }

    @Override
    public MenuDto update(Long id, MenuDto dto) {

        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        menu.setTitle(dto.getTitle());
        menu.getPlats().clear();

        if (dto.getPlats() != null) {
            for (var platDto : dto.getPlats()) {
                Plat plat = platMapper.toEntity(platDto);
                plat.setMenu(menu);
                menu.getPlats().add(plat);
            }
        }

        Menu updated = menuRepository.save(menu);
        return menuMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }


    @Override
    public List<MenuDto> getByRestaurant(Long restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(menuMapper::toDto)
                .toList();
    }
}

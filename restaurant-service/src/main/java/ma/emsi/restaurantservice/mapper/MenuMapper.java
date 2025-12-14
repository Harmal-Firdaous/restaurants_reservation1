package ma.emsi.restaurantservice.mapper;

import lombok.RequiredArgsConstructor;
import ma.emsi.restaurantservice.dto.MenuDto;
import ma.emsi.restaurantservice.dto.PlatDto;
import ma.emsi.restaurantservice.entity.Menu;
import ma.emsi.restaurantservice.entity.Plat;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MenuMapper {

    private final PlatMapper platMapper;

    public MenuDto toDto(Menu m) {
        MenuDto dto = new MenuDto();
        dto.setId(m.getId());
        dto.setTitle(m.getTitle());
        dto.setRestaurantId(m.getRestaurant().getId());
        dto.setPlats(
                m.getPlats().stream()
                        .map(platMapper::toDto)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    public Menu toEntity(MenuDto dto) {
        Menu m = Menu.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .build();

        if (dto.getPlats() != null) {
            m.setPlats(
                    dto.getPlats().stream()
                            .map(platMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        return m;
    }
}

package ma.emsi.restaurantservice.mapper;

import ma.emsi.restaurantservice.dto.MenuDto;
import ma.emsi.restaurantservice.entity.Menu;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    public MenuDto toDto(Menu m) {
        if (m == null) return null;
        return MenuDto.builder()
                .id(m.getId())
                .name(m.getName())
                .price(m.getPrice())
                .restaurantId(m.getRestaurant() != null ? m.getRestaurant().getId() : null)
                .build();
    }

    public Menu toEntity(MenuDto dto) {
        if (dto == null) return null;
        Menu m = new Menu();
        m.setId(dto.getId());
        m.setName(dto.getName());
        m.setPrice(dto.getPrice());
        // restaurant assignment must be done in service
        return m;
    }
}

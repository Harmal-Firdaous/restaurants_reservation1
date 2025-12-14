package ma.emsi.restaurantservice.mapper;

import ma.emsi.restaurantservice.dto.PlatDto;
import ma.emsi.restaurantservice.entity.Plat;
import org.springframework.stereotype.Component;

@Component
public class PlatMapper {

    public PlatDto toDto(Plat p) {
        if (p == null) return null;

        return PlatDto.builder()
                .id(p.getId())
                .menuId(p.getMenu() != null ? p.getMenu().getId() : null)
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .build();
    }

    public Plat toEntity(PlatDto dto) {
        if (dto == null) return null;

        return Plat.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }
}
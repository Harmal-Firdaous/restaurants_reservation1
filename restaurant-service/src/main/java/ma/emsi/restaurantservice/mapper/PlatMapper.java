package ma.emsi.restaurantservice.mapper;

import ma.emsi.restaurantservice.dto.PlatDto;
import ma.emsi.restaurantservice.entity.Plat;
import org.springframework.stereotype.Component;

@Component
public class PlatMapper {

    public PlatDto toDto(Plat p) {
        PlatDto dto = new PlatDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        return dto;
    }

    public Plat toEntity(PlatDto dto) {
        return Plat.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }
}

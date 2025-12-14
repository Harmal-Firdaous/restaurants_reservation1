package ma.emsi.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class MenuDto {
    private Long id;
    private String title;
    private Long restaurantId;
    private List<PlatDto> plats;
}

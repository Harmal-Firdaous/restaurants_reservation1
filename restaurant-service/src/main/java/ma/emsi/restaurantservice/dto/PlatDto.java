package ma.emsi.restaurantservice.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlatDto {
    private Long id;
    private Long menuId;  // ADD THIS
    private String name;
    private String description;
    private Double price;
}
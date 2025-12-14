package ma.emsi.restaurantservice.dto;

import lombok.Data;

@Data
public class PlatDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
}

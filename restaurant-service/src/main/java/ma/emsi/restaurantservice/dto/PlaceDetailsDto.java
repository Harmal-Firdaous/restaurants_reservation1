package ma.emsi.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDetailsDto {
    private String placeId;
    private String name;
    private String address;
    private double rating;
    private double lat;
    private double lng;
    private List<String> photos;
}

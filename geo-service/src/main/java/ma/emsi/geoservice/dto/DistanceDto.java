package ma.emsi.geoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistanceDto {
    private double userLat;
    private double userLng;
    private double restaurantLat;
    private double restaurantLng;
    private double distanceKm;
}

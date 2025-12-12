package ma.emsi.geoservice.dto;

import lombok.Data;

@Data
public class PlaceDto {
    private String placeId;
    private String name;
    private Double latitude;
    private Double longitude;
    private Double rating;
}

package ma.emsi.geoservice.service;

import ma.emsi.geoservice.dto.PlaceDto;

import java.util.List;

public interface GeoService {
    PlaceDto getPlaceDetails(String placeId);
    List<PlaceDto> searchNearby(double lat, double lng, int radius, String keyword);
    double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2);
}

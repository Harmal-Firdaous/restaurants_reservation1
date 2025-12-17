package ma.emsi.geoservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.geoservice.dto.PlaceDetailsDto;
import ma.emsi.geoservice.dto.PlaceDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoServiceImpl implements GeoService {

    private final WebClient webClient;

    @Value("${google.places.key}")
    private String apiKey;

    @Value("${google.places.base-url}")
    private String baseUrl;

    @Override
    public PlaceDetailsDto getPlaceDetails(String placeId) {

        String url = baseUrl + "/details/json"
                + "?place_id=" + placeId
                + "&fields=place_id,name,formatted_address,rating,geometry,photos,reviews"
                + "&key=" + apiKey;

        Map<String, Object> resp = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (resp == null || !resp.containsKey("result")) {
            return new PlaceDetailsDto();
        }

        Map<String, Object> result = (Map<String, Object>) resp.get("result");

        Map<String, Object> location =
                (Map<String, Object>) ((Map<String, Object>)
                        result.get("geometry")).get("location");

        List<String> photos = new ArrayList<>();
        if (result.containsKey("photos")) {
            List<Map<String, Object>> rawPhotos =
                    (List<Map<String, Object>>) result.get("photos");
            rawPhotos.forEach(p ->
                    photos.add(p.get("photo_reference").toString())
            );
        }

        return PlaceDetailsDto.builder()
                .placeId(placeId)
                .name((String) result.get("name"))
                .address((String) result.get("formatted_address"))
                .rating(
                        result.get("rating") == null ? 0 :
                                Double.parseDouble(result.get("rating").toString())
                )
                .lat(Double.parseDouble(location.get("lat").toString()))
                .lng(Double.parseDouble(location.get("lng").toString()))
                .photos(photos)
                .build();
    }


    @Override
    public List<PlaceDto> searchNearby(double lat, double lng, int radius, String keyword) {

        StringBuilder urlBuilder = new StringBuilder()
                .append(baseUrl)
                .append("/nearbysearch/json?")
                .append("location=").append(lat).append(",").append(lng)
                .append("&radius=").append(radius)
                .append("&type=restaurant") // ✅ FIX MAJEUR
                .append("&key=").append(apiKey);

        if (keyword != null && !keyword.isEmpty()) {
            urlBuilder.append("&keyword=").append(keyword);
        }

        String url = urlBuilder.toString();
        log.info("Google Places Nearby URL: {}", url);

        try {
            Map<String, Object> resp = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (resp == null) {
                log.error("Google response is null");
                return Collections.emptyList();
            }

            String status = (String) resp.get("status");
            log.info("Google Places status: {}", status);

            if (!"OK".equals(status)) {
                log.warn("Google returned non-OK status: {}", status);
                log.warn("Full response: {}", resp);
                return Collections.emptyList();
            }

            List<Map<String, Object>> results =
                    (List<Map<String, Object>>) resp.get("results");

            if (results == null || results.isEmpty()) {
                log.warn("Google returned empty results");
                return Collections.emptyList();
            }

            List<PlaceDto> places = new ArrayList<>();

            for (Map<String, Object> r : results) {
                PlaceDto p = new PlaceDto();
                p.setPlaceId((String) r.get("place_id"));
                p.setName((String) r.get("name"));

                if (r.containsKey("geometry")) {
                    Map<String, Object> geometry =
                            (Map<String, Object>) r.get("geometry");

                    Map<String, Object> location =
                            (Map<String, Object>) geometry.get("location");

                    p.setLatitude(Double.parseDouble(location.get("lat").toString()));
                    p.setLongitude(Double.parseDouble(location.get("lng").toString()));
                }

                if (r.containsKey("rating")) {
                    p.setRating(Double.parseDouble(r.get("rating").toString()));
                }

                places.add(p);
            }

            log.info("Nearby restaurants found: {}", places.size());
            return places;

        } catch (Exception e) {
            log.error("Error calling Google Places API", e);
            return Collections.emptyList();
        }
    }


    @Override
    public double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula
        final int R = 6371; // Radius of earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        log.info("Calculated distance: {} km", distance);
        return distance;
    }
}
package ma.emsi.geoservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public PlaceDto getPlaceDetails(String placeId) {
        String url = String.format("%s/details/json?place_id=%s&key=%s", baseUrl, placeId, apiKey);

        log.info("Fetching place details from: {}", url);

        try {
            Map<String, Object> resp = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.info("Response received: {}", resp);

            if (resp == null || !resp.containsKey("result")) {
                log.error("Invalid response structure");
                return new PlaceDto();
            }

            Map<String, Object> result = (Map<String, Object>) resp.get("result");
            PlaceDto dto = new PlaceDto();

            if (result != null) {
                dto.setPlaceId((String) result.get("place_id"));
                dto.setName((String) result.get("name"));

                if (result.containsKey("rating")) {
                    dto.setRating(Double.parseDouble(result.get("rating").toString()));
                }

                // Extract geometry location
                if (result.containsKey("geometry")) {
                    Map<String, Object> geometry = (Map<String, Object>) result.get("geometry");
                    if (geometry.containsKey("location")) {
                        Map<String, Object> location = (Map<String, Object>) geometry.get("location");
                        dto.setLatitude(Double.parseDouble(location.get("lat").toString()));
                        dto.setLongitude(Double.parseDouble(location.get("lng").toString()));
                    }
                }
            }

            return dto;

        } catch (Exception e) {
            log.error("Error fetching place details: ", e);
            return new PlaceDto();
        }
    }

    @Override
    public List<PlaceDto> searchNearby(double lat, double lng, int radius, String keyword) {
        // Build URL with proper encoding
        StringBuilder urlBuilder = new StringBuilder()
                .append(baseUrl)
                .append("/nearbysearch/json?location=")
                .append(lat).append(",").append(lng)
                .append("&radius=").append(radius)
                .append("&key=").append(apiKey);

        if (keyword != null && !keyword.isEmpty()) {
            urlBuilder.append("&keyword=").append(keyword);
        }

        String url = urlBuilder.toString();
        log.info("Searching nearby places: {}", url);

        try {
            Map<String, Object> resp = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.info("Nearby search response: {}", resp);

            if (resp == null || !resp.containsKey("results")) {
                log.warn("No results found in response");
                return Collections.emptyList();
            }

            String status = (String) resp.get("status");
            if (!"OK".equals(status) && !"ZERO_RESULTS".equals(status)) {
                log.error("API returned error status: {}", status);
                if (resp.containsKey("error_message")) {
                    log.error("Error message: {}", resp.get("error_message"));
                }
                return Collections.emptyList();
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) resp.get("results");
            if (results == null || results.isEmpty()) {
                return Collections.emptyList();
            }

            List<PlaceDto> list = new ArrayList<>();
            for (Map<String, Object> r : results) {
                PlaceDto p = new PlaceDto();
                p.setPlaceId((String) r.get("place_id"));
                p.setName((String) r.get("name"));

                if (r.containsKey("geometry")) {
                    Map<String, Object> geo = (Map<String, Object>) r.get("geometry");
                    if (geo != null && geo.containsKey("location")) {
                        Map<String, Object> loc = (Map<String, Object>) geo.get("location");
                        if (loc != null) {
                            p.setLatitude(Double.parseDouble(loc.get("lat").toString()));
                            p.setLongitude(Double.parseDouble(loc.get("lng").toString()));
                        }
                    }
                }

                if (r.containsKey("rating")) {
                    p.setRating(Double.parseDouble(r.get("rating").toString()));
                }

                list.add(p);
            }

            log.info("Found {} nearby places", list.size());
            return list;

        } catch (Exception e) {
            log.error("Error searching nearby places: ", e);
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
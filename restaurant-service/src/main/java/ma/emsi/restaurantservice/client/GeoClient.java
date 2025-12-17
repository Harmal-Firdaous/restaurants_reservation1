package ma.emsi.restaurantservice.client;

import lombok.RequiredArgsConstructor;
import ma.emsi.restaurantservice.dto.PlaceDetailsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class GeoClient {

    private final WebClient webClient;

    @Value("${geo.service.url}")
    private String geoServiceUrl;

    public PlaceDetailsDto getPlaceDetails(String placeId) {
        return webClient.get()
                .uri(geoServiceUrl + "/api/geo/place/{id}", placeId)
                .retrieve()
                .bodyToMono(PlaceDetailsDto.class)
                .block();
    }

    public Double calculateDistance(double lat1, double lon1,
                                    double lat2, double lon2) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(geoServiceUrl + "/api/geo/distance")
                        .queryParam("lat1", lat1)
                        .queryParam("lon1", lon1)
                        .queryParam("lat2", lat2)
                        .queryParam("lon2", lon2)
                        .build())
                .retrieve()
                .bodyToMono(Double.class)
                .block();
    }
}

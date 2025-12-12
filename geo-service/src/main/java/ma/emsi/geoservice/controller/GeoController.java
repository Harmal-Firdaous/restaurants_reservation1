package ma.emsi.geoservice.controller;

import lombok.RequiredArgsConstructor;
import ma.emsi.geoservice.dto.PlaceDto;
import ma.emsi.geoservice.service.GeoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geo")
@RequiredArgsConstructor
public class GeoController {

    private final GeoService geoService;

    @GetMapping("/place/{placeId}")
    public ResponseEntity<PlaceDto> place(@PathVariable String placeId) {
        return ResponseEntity.ok(geoService.getPlaceDetails(placeId));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<PlaceDto>> nearby(@RequestParam double lat, @RequestParam double lng,
                                                 @RequestParam(defaultValue = "1000") int radius,
                                                 @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(geoService.searchNearby(lat, lng, radius, keyword));
    }

    @GetMapping("/distance")
    public ResponseEntity<Double> distance(@RequestParam double lat1, @RequestParam double lon1,
                                           @RequestParam double lat2, @RequestParam double lon2) {
        return ResponseEntity.ok(geoService.calculateDistanceKm(lat1, lon1, lat2, lon2));
    }
}

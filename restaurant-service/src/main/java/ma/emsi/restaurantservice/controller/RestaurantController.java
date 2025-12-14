package ma.emsi.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
import ma.emsi.restaurantservice.dto.RestaurantDto;
import ma.emsi.restaurantservice.enums.CuisineType;
import ma.emsi.restaurantservice.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantDto> create(@RequestBody RestaurantDto dto) {
        return ResponseEntity.ok(restaurantService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantDto>> search(@RequestParam("q") String q) {
        return ResponseEntity.ok(restaurantService.searchByName(q));
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<List<RestaurantDto>> getByCuisine(@PathVariable CuisineType cuisine) {
        return ResponseEntity.ok(restaurantService.findByCuisineType(cuisine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }


}

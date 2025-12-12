package ma.emsi.reservationservice.client;

import ma.emsi.restaurantservice.dto.RestaurantDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// base URL is the restaurant-service (for dev use localhost:8082)
@FeignClient(name = "restaurant-service", url = "${feign.restaurant.url:http://localhost:8082}")
public interface RestaurantClient {

    @GetMapping("/api/restaurants/{id}")
    RestaurantDto getRestaurantById(@PathVariable("id") Long id);
}

package ma.emsi.reservationservice.client;

import ma.emsi.reservationservice.dto.RestaurantDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service")
public interface RestaurantClient {

    @GetMapping("/api/restaurants/{id}")
    RestaurantDto getRestaurantById(@PathVariable Long id);
}


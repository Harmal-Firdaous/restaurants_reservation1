package ma.emsi.restaurantservice.repository;

import ma.emsi.restaurantservice.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.List;

@RepositoryRestResource(path = "restaurants", collectionResourceRel = "restaurants")
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByNameContainingIgnoreCase(String name);
    Optional<Restaurant> findByGooglePlaceId(String googlePlaceId);
}

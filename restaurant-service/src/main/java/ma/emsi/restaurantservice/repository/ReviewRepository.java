package ma.emsi.restaurantservice.repository;

import ma.emsi.restaurantservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRestaurant_Id(Long restaurantId);
}

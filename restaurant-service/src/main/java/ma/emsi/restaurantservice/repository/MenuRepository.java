package ma.emsi.restaurantservice.repository;

import ma.emsi.restaurantservice.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurantId(Long id);
}

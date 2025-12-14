package ma.emsi.restaurantservice.repository;

import ma.emsi.restaurantservice.entity.Plat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatRepository extends JpaRepository<Plat, Long> {
    List<Plat> findByMenuId(Long menuId);
}
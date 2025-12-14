package ma.emsi.restaurantservice.repository;

import ma.emsi.restaurantservice.entity.Plat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatRepository extends JpaRepository<Plat, Long> { }

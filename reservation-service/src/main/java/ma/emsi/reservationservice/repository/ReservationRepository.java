package ma.emsi.reservationservice.repository;

import ma.emsi.reservationservice.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRestaurantIdAndDateTimeBetween(Long restaurantId, LocalDateTime from, LocalDateTime to);
    List<Reservation> findByUserId(Long userId);
}

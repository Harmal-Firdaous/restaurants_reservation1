package ma.emsi.reservationservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // référence user-service
    private Long restaurantId; // référence restaurant-service

    private LocalDateTime dateTime;
    private Integer partySize;

    private String status; // PENDING, CONFIRMED, CANCELLED
}

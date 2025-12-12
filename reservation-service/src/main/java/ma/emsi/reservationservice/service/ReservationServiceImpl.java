package ma.emsi.reservationservice.service;

import lombok.RequiredArgsConstructor;
import ma.emsi.reservationservice.client.RestaurantClient;
import ma.emsi.reservationservice.dto.ReservationDto;
import ma.emsi.reservationservice.entity.Reservation;
import ma.emsi.reservationservice.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repo;
    private final RestaurantClient restaurantClient;

    @Override
    public ReservationDto create(ReservationDto dto) {
        // simple availability check: number of reservations at same time < some threshold (omitted)
        Reservation r = Reservation.builder()
                .userId(dto.getUserId())
                .restaurantId(dto.getRestaurantId())
                .dateTime(dto.getDateTime())
                .partySize(dto.getPartySize())
                .status("PENDING")
                .build();
        Reservation saved = repo.save(r);
        dto.setId(saved.getId());
        // Optionally call restaurantClient.getRestaurantById to enrich dto
        return dto;
    }

    @Override
    public List<ReservationDto> findByUser(Long userId) {
        return repo.findByUserId(userId).stream()
                .map(r -> new ReservationDto(r.getId(), r.getUserId(), r.getRestaurantId(), r.getDateTime(), r.getPartySize(), r.getStatus()))
                .collect(Collectors.toList());
    }
}

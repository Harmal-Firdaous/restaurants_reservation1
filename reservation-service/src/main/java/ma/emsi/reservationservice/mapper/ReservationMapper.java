package ma.emsi.reservationservice.mapper;

import ma.emsi.reservationservice.dto.ReservationDto;
import ma.emsi.reservationservice.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationDto toDto(Reservation r) {
        if (r == null) return null;
        return ReservationDto.builder()
                .id(r.getId())
                .userId(r.getUserId())
                .restaurantId(r.getRestaurantId())
                .dateTime(r.getDateTime())
                .partySize(r.getPartySize())
                .status(r.getStatus())
                .build();
    }

    public Reservation toEntity(ReservationDto dto) {
        if (dto == null) return null;
        return Reservation.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .restaurantId(dto.getRestaurantId())
                .dateTime(dto.getDateTime())
                .partySize(dto.getPartySize())
                .status(dto.getStatus())
                .build();
    }
}

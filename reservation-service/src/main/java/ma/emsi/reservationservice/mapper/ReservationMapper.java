package ma.emsi.reservationservice.mapper;

import ma.emsi.reservationservice.dto.ReservationDto;
import ma.emsi.reservationservice.entity.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    public ReservationDto toDto(Reservation entity) {
        if (entity == null) return null;

        return ReservationDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .restaurantId(entity.getRestaurantId())
                .dateTime(entity.getDateTime())
                .partySize(entity.getPartySize())
                .status(entity.getStatus())
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

    public List<ReservationDto> toDtoList(List<Reservation> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
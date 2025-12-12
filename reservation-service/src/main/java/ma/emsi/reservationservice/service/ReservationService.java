package ma.emsi.reservationservice.service;

import ma.emsi.reservationservice.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    ReservationDto create(ReservationDto dto);
    List<ReservationDto> findByUser(Long userId);
}

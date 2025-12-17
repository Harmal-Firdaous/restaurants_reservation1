package ma.emsi.reservationservice.service;

import ma.emsi.reservationservice.dto.ReservationDto;

import java.util.List;

public interface ReservationService {

    ReservationDto create(ReservationDto dto);

    ReservationDto update(Long id, ReservationDto dto);

    void cancel(Long id);

    List<ReservationDto> findByUser(Long userId);
}

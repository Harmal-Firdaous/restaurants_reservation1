package ma.emsi.reservationservice.controller;

import lombok.RequiredArgsConstructor;
import ma.emsi.reservationservice.dto.ReservationDto;
import ma.emsi.reservationservice.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDto> create(@Valid @RequestBody ReservationDto dto) {
        return ResponseEntity.ok(reservationService.create(dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDto>> byUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.findByUser(userId));
    }
}

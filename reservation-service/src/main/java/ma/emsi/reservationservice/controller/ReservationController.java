package ma.emsi.reservationservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.reservationservice.dto.ReservationDto;
import ma.emsi.reservationservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * CREATE - Create a new reservation
     */
    @PostMapping
    public ResponseEntity<ReservationDto> create(@Valid @RequestBody ReservationDto dto) {
        ReservationDto created = reservationService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * UPDATE - Update an existing reservation
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservationDto dto) {
        ReservationDto updated = reservationService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * CANCEL - Cancel a reservation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        reservationService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * LIST BY USER - Get all reservations for a specific user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDto>> byUser(@PathVariable Long userId) {
        List<ReservationDto> reservations = reservationService.findByUser(userId);
        return ResponseEntity.ok(reservations);
    }
}
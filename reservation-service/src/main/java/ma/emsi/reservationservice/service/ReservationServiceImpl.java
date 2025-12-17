package ma.emsi.reservationservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.reservationservice.client.RestaurantClient;
import ma.emsi.reservationservice.client.UserClient;
import ma.emsi.reservationservice.dto.ReservationDto;
import ma.emsi.reservationservice.dto.RestaurantDto;
import ma.emsi.reservationservice.dto.UserDto;
import ma.emsi.reservationservice.entity.Reservation;
import ma.emsi.reservationservice.mapper.ReservationMapper;
import ma.emsi.reservationservice.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repo;
    private final RestaurantClient restaurantClient;
    private final UserClient userClient;
    private final ReservationMapper mapper;

    @Override
    public ReservationDto create(ReservationDto dto) {
        log.info("Creating reservation for user {} at restaurant {}", dto.getUserId(), dto.getRestaurantId());

        // 1. Validate restaurant exists (REQUIRED)
        RestaurantDto restaurant = validateRestaurantExists(dto.getRestaurantId());

        // 2. Validate user exists (OPTIONAL - skip if user-service has security)
        validateUserExists(dto.getUserId());

        // 3. Create reservation (NO TIME VALIDATION)
        Reservation reservation = Reservation.builder()
                .userId(dto.getUserId())
                .restaurantId(dto.getRestaurantId())
                .dateTime(dto.getDateTime())
                .partySize(dto.getPartySize())
                .status("PENDING")
                .build();

        Reservation saved = repo.save(reservation);
        log.info("Reservation created with ID: {} - Status: PENDING", saved.getId());

        ReservationDto result = mapper.toDto(saved);
        result.setRestaurant(restaurant);
        return result;
    }

    @Override
    public ReservationDto update(Long id, ReservationDto dto) {
        log.info("Updating reservation {}", id);

        Reservation reservation = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));

        // Only PENDING reservations can be updated
        if ("CANCELLED".equals(reservation.getStatus()) || "CONFIRMED".equals(reservation.getStatus())) {
            throw new IllegalStateException("Cannot update a " + reservation.getStatus() + " reservation");
        }

        // Update fields (NO TIME VALIDATION)
        reservation.setDateTime(dto.getDateTime());
        reservation.setPartySize(dto.getPartySize());

        Reservation updated = repo.save(reservation);
        log.info("Reservation {} updated successfully", id);

        return mapper.toDto(updated);
    }

    @Override
    public void cancel(Long id) {
        log.info("Cancelling reservation {}", id);

        Reservation reservation = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));

        if ("CANCELLED".equals(reservation.getStatus())) {
            throw new IllegalStateException("Reservation is already cancelled");
        }

        reservation.setStatus("CANCELLED");
        repo.save(reservation);

        log.info("Reservation {} cancelled successfully", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> findByUser(Long userId) {
        log.info("Finding reservations for user {}", userId);
        return repo.findByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    // ========== Helper Methods ==========

    private RestaurantDto validateRestaurantExists(Long restaurantId) {
        try {
            RestaurantDto restaurant = restaurantClient.getRestaurantById(restaurantId);
            if (restaurant == null) {
                throw new RuntimeException("Restaurant not found with ID: " + restaurantId);
            }
            log.info("Restaurant {} validated successfully", restaurantId);
            return restaurant;
        } catch (FeignException.NotFound e) {
            log.error("Restaurant {} does not exist", restaurantId);
            throw new RuntimeException("Restaurant not found with ID: " + restaurantId);
        } catch (Exception e) {
            log.error("Error validating restaurant {}: {}", restaurantId, e.getMessage());
            throw new RuntimeException("Could not validate restaurant. Service may be unavailable.");
        }
    }

    private void validateUserExists(Long userId) {
        try {
            UserDto user = userClient.getUserById(userId);
            if (user != null) {
                log.info("User {} validated successfully", userId);
            }
        } catch (FeignException.Forbidden e) {
            // User service has security - skip validation but log warning
            log.warn("Cannot validate user {} - user-service returned 403 Forbidden. Skipping user validation.", userId);
        } catch (FeignException.NotFound e) {
            log.error("User {} does not exist", userId);
            throw new RuntimeException("User not found with ID: " + userId);
        } catch (Exception e) {
            // If user-service is unavailable or has other errors, just log and continue
            log.warn("Cannot validate user {} - user-service error: {}. Skipping user validation.", userId, e.getMessage());
        }
    }
}
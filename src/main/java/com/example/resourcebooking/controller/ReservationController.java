package com.example.resourcebooking.controller;

import com.example.resourcebooking.entity.Reservation;
import com.example.resourcebooking.entity.Resource;
import com.example.resourcebooking.entity.User;
import com.example.resourcebooking.entity.Role;
import com.example.resourcebooking.repository.ResourceRepository;
import com.example.resourcebooking.repository.UserRepository;
import com.example.resourcebooking.service.ReservationService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.resourcebooking.entity.ReservationStatus;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    public ReservationController(
            ReservationService reservationService,
            UserRepository userRepository,
            ResourceRepository resourceRepository) {

        this.reservationService = reservationService;
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    // CREATE RESERVATION
    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestBody ReservationRequest request,
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Resource resource = resourceRepository
                .findById(request.getResourceId())
                .orElseThrow(() ->
                        new RuntimeException("Resource not found"));

        Reservation reservation =
                reservationService.createReservation(
                        user,
                        resource,
                        request.getPrice(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        return ResponseEntity.ok(reservation);
    }

 // GET ALL RESERVATIONS - ADMIN ONLY
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getAllReservations() {

        return ResponseEntity.ok(
                reservationService.getAllReservations()
        );
    }

 // GET RESERVATION BY ID - ADMIN ONLY
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable Long id) {

        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET CURRENT USER'S RESERVATIONS
    @GetMapping("/my")
    public ResponseEntity<List<Reservation>> getMyReservations(
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return ResponseEntity.ok(
                reservationService.getReservationsByUser(user)
        );
    }

 // CANCEL RESERVATION

    @DeleteMapping("/{id}")
    public ResponseEntity<Reservation> cancelReservation(
            @PathVariable Long id,
            Authentication authentication) {

        Optional<Reservation> optionalReservation =
                reservationService.getReservationById(id);

        // Reservation does not exist
        if (optionalReservation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = optionalReservation.get();

        // Get logged-in user
        User loggedInUser = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // USER can cancel only their own reservation
        if (loggedInUser.getRole() != Role.ADMIN
                && !reservation.getUser().getId().equals(loggedInUser.getId())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        // Cancel reservation
        reservation.setStatus(ReservationStatus.CANCELLED);

        Reservation cancelledReservation =
                reservationService.saveReservation(reservation);

        return ResponseEntity.ok(cancelledReservation);
    }

    // REQUEST DTO
    public static class ReservationRequest {

        private Long resourceId;
        private BigDecimal price;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public Long getResourceId() {
            return resourceId;
        }

        public void setResourceId(Long resourceId) {
            this.resourceId = resourceId;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
        }
    }
}

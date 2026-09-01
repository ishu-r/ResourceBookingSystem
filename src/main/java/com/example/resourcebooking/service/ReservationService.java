package com.example.resourcebooking.service;

import com.example.resourcebooking.entity.Reservation;
import com.example.resourcebooking.entity.ReservationStatus;
import com.example.resourcebooking.entity.Resource;
import com.example.resourcebooking.entity.User;
import com.example.resourcebooking.repository.ReservationRepository;
import com.example.resourcebooking.exception.ConflictException;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByUser(User user) {
        return reservationRepository.findByUser(user);
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(
            User user,
            Resource resource,
            BigDecimal price,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        // 1. Validate time
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException(
                    "Start time and end time are required");
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(
                    "Start time must be before end time");
        }

        // 2. Check resource availability
        if (!resource.isAvailable()) {
            throw new IllegalArgumentException(
                    "Resource is currently unavailable");
        }

        // 3. Validate price
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative");
        }

        // 4. Check overlapping reservation
        boolean conflict =
                reservationRepository
                        .existsByResourceAndStartTimeLessThanAndEndTimeGreaterThanAndStatusNot(
                                resource,
                                endTime,
                                startTime,
                                ReservationStatus.CANCELLED);

        if (conflict) {

            throw new ConflictException(
                    "Resource is already reserved for the selected time");
        }

        // 5. Create reservation
        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setResource(resource);
        reservation.setPrice(price);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);

        // Initially pending
        reservation.setStatus(ReservationStatus.PENDING);

        // 6. Save
        return reservationRepository.save(reservation);
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
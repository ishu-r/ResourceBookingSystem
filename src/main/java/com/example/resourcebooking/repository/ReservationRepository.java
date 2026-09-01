package com.example.resourcebooking.repository;

import com.example.resourcebooking.entity.Reservation;
import com.example.resourcebooking.entity.ReservationStatus;
import com.example.resourcebooking.entity.Resource;
import com.example.resourcebooking.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser(User user);

    boolean existsByResourceAndStartTimeLessThanAndEndTimeGreaterThanAndStatusNot(
            Resource resource,
            LocalDateTime endTime,
            LocalDateTime startTime,
            ReservationStatus status
    );
}
package com.interiorai.repository;

import com.interiorai.entity.Booking;
import com.interiorai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserOrderByCreatedAtDesc(User user);
    Optional<Booking> findByBookingIdAndUser(Long bookingId, User user);
    long countByUserAndStatus(User user, String status);
}

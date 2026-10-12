package com.interiorai.controller;

import com.interiorai.dto.ApiResponse;
import com.interiorai.dto.BookingRequest;
import com.interiorai.entity.Booking;
import com.interiorai.entity.User;
import com.interiorai.service.BookingService;
import com.interiorai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Booking>> createBooking(@Valid @RequestBody BookingRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        Booking booking = bookingService.createBooking(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Booking request submitted successfully.", booking));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Booking>>> getMyBookings() {
        User user = userService.getCurrentAuthenticatedUser();
        List<Booking> list = bookingService.getUserBookings(user);
        return ResponseEntity.ok(ApiResponse.ok("Bookings retrieved.", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> getBookingById(@PathVariable("id") Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        Booking booking = bookingService.getBookingById(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Booking retrieved.", booking));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Booking>> cancelBooking(@PathVariable("id") Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        Booking cancelled = bookingService.cancelBooking(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Booking has been cancelled.", cancelled));
    }
}

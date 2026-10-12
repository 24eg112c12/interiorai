package com.interiorai.service;

import com.interiorai.dto.BookingRequest;
import com.interiorai.entity.Booking;
import com.interiorai.entity.Professional;
import com.interiorai.entity.Project;
import com.interiorai.entity.User;
import com.interiorai.exception.BadRequestException;
import com.interiorai.exception.ResourceNotFoundException;
import com.interiorai.repository.BookingRepository;
import com.interiorai.repository.ProfessionalRepository;
import com.interiorai.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ProfessionalRepository professionalRepository;
    private final ProjectRepository projectRepository;

    public BookingService(BookingRepository bookingRepository,
                          ProfessionalRepository professionalRepository,
                          ProjectRepository projectRepository) {
        this.bookingRepository = bookingRepository;
        this.professionalRepository = professionalRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Booking createBooking(BookingRequest request, User user) {
        Professional professional = professionalRepository.findById(request.getProfessionalId())
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found with ID: " + request.getProfessionalId()));

        Project project = null;
        if (request.getProjectId() != null) {
            project = projectRepository.findByProjectIdAndUser(request.getProjectId(), user).orElse(null);
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setProfessional(professional);
        booking.setProject(project);
        booking.setBookingDate(request.getBookingDate());
        booking.setMessage(request.getMessage());
        booking.setStatus("PENDING");

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Booking getBookingById(Long bookingId, User user) {
        return bookingRepository.findByBookingIdAndUser(bookingId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found or unauthorized."));
    }

    @Transactional
    public Booking cancelBooking(Long bookingId, User user) {
        Booking booking = getBookingById(bookingId, user);
        if ("COMPLETED".equalsIgnoreCase(booking.getStatus())) {
            throw new BadRequestException("Completed bookings cannot be cancelled.");
        }
        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            throw new BadRequestException("Booking is already cancelled.");
        }
        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateStatus(Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
        booking.setStatus(status.toUpperCase());
        return bookingRepository.save(booking);
    }
}

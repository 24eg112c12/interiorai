package com.interiorai.service;

import com.interiorai.dto.BookingRequest;
import com.interiorai.entity.Booking;
import com.interiorai.entity.Professional;
import com.interiorai.entity.User;
import com.interiorai.repository.BookingRepository;
import com.interiorai.repository.ProfessionalRepository;
import com.interiorai.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ProfessionalRepository professionalRepository;
    @Mock
    private ProjectRepository projectRepository;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingRepository, professionalRepository, projectRepository);
    }

    @Test
    void testCreateBooking_Success() {
        User user = new User("Client", "client@test.com", "pass", "123");
        user.setUserId(1L);

        Professional pro = new Professional();
        pro.setProfessionalId(5L);
        pro.setName("Rajesh Painter");

        when(professionalRepository.findById(5L)).thenReturn(Optional.of(pro));

        Booking savedBooking = new Booking();
        savedBooking.setBookingId(101L);
        savedBooking.setUser(user);
        savedBooking.setProfessional(pro);
        savedBooking.setStatus("PENDING");
        savedBooking.setBookingDate(LocalDate.now().plusDays(2));

        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        BookingRequest request = new BookingRequest();
        request.setProfessionalId(5L);
        request.setBookingDate(LocalDate.now().plusDays(2));
        request.setMessage("Need painting consultation for 2BHK");

        Booking result = bookingService.createBooking(request, user);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(101L, result.getBookingId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCancelBooking_Success() {
        User user = new User("Client", "client@test.com", "pass", "123");
        user.setUserId(1L);

        Booking booking = new Booking();
        booking.setBookingId(200L);
        booking.setUser(user);
        booking.setStatus("PENDING");

        when(bookingRepository.findByBookingIdAndUser(200L, user)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.cancelBooking(200L, user);

        assertEquals("CANCELLED", result.getStatus());
    }
}

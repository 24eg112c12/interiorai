package com.interiorai.service;

import com.interiorai.dto.RoomRequest;
import com.interiorai.entity.Room;
import com.interiorai.entity.User;
import com.interiorai.exception.ResourceNotFoundException;
import com.interiorai.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    private FileStorageService fileStorageService;
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageService("target/test-uploads");
        roomService = new RoomService(roomRepository, fileStorageService);
    }

    @Test
    void testCreateRoom_SavesAndCalculatesDimensions() {
        User user = new User("Test User", "test@example.com", "pass", "123");
        user.setUserId(1L);

        RoomRequest request = new RoomRequest();
        request.setRoomType("Bedroom");
        request.setLengthM(new BigDecimal("4.5"));
        request.setWidthM(new BigDecimal("3.5"));
        request.setCeilingHeightM(new BigDecimal("2.9"));
        request.setExistingFurniture("Wardrobe, Queen Bed");

        Room savedRoom = new Room();
        savedRoom.setRoomId(10L);
        savedRoom.setUser(user);
        savedRoom.setRoomType("Bedroom");
        savedRoom.setLengthM(new BigDecimal("4.5"));
        savedRoom.setWidthM(new BigDecimal("3.5"));

        when(roomRepository.save(any(Room.class))).thenReturn(savedRoom);

        Room result = roomService.createRoom(request, user);

        assertNotNull(result);
        assertEquals(10L, result.getRoomId());
        assertEquals("Bedroom", result.getRoomType());
        assertEquals(new BigDecimal("15.75"), result.getArea());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testGetRoomById_ThrowsExceptionWhenUnauthorizedOrMissing() {
        User userA = new User("User A", "a@example.com", "pass", "123");
        userA.setUserId(1L);

        when(roomRepository.findByRoomIdAndUser(99L, userA)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomById(99L, userA));
    }
}

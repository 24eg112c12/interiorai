package com.interiorai.controller;

import com.interiorai.dto.ApiResponse;
import com.interiorai.dto.RoomRequest;
import com.interiorai.entity.Room;
import com.interiorai.entity.User;
import com.interiorai.service.RoomService;
import com.interiorai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final UserService userService;

    public RoomController(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Room>> createRoom(@Valid @RequestBody RoomRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        Room room = roomService.createRoom(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Room details saved successfully.", room));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Room>>> getUserRooms() {
        User user = userService.getCurrentAuthenticatedUser();
        List<Room> rooms = roomService.getUserRooms(user);
        return ResponseEntity.ok(ApiResponse.ok("Rooms retrieved successfully.", rooms));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Room>> getRoomById(@PathVariable("id") Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        Room room = roomService.getRoomById(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Room retrieved.", room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Room>> updateRoom(@PathVariable("id") Long id,
                                                        @Valid @RequestBody RoomRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        Room updated = roomService.updateRoom(id, request, user);
        return ResponseEntity.ok(ApiResponse.ok("Room updated successfully.", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable("id") Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        roomService.deleteRoom(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Room deleted successfully.", null));
    }

    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Room>> uploadRoomPhoto(@PathVariable("id") Long id,
                                                             @RequestParam("photo") MultipartFile photo) {
        User user = userService.getCurrentAuthenticatedUser();
        Room room = roomService.uploadRoomPhoto(id, photo, user);
        return ResponseEntity.ok(ApiResponse.ok("Room photograph uploaded successfully.", room));
    }
}

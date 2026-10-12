package com.interiorai.service;

import com.interiorai.dto.RoomRequest;
import com.interiorai.entity.Room;
import com.interiorai.entity.User;
import com.interiorai.exception.ResourceNotFoundException;
import com.interiorai.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final FileStorageService fileStorageService;

    public RoomService(RoomRepository roomRepository, FileStorageService fileStorageService) {
        this.roomRepository = roomRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public Room createRoom(RoomRequest request, User user) {
        Room room = new Room();
        room.setUser(user);
        room.setRoomType(request.getRoomType());
        room.setLengthM(request.getLengthM());
        room.setWidthM(request.getWidthM());
        room.setCeilingHeightM(request.getCeilingHeightM());
        room.setExistingFurniture(request.getExistingFurniture());
        room.setNumberOfDoors(request.getNumberOfDoors() != null ? request.getNumberOfDoors() : 1);
        room.setNumberOfWindows(request.getNumberOfWindows() != null ? request.getNumberOfWindows() : 1);
        room.setNumberOfOccupants(request.getNumberOfOccupants() != null ? request.getNumberOfOccupants() : 1);
        room.setAdditionalRequirements(request.getAdditionalRequirements());

        return roomRepository.save(room);
    }

    public List<Room> getUserRooms(User user) {
        return roomRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Room getRoomById(Long roomId, User user) {
        return roomRepository.findByRoomIdAndUser(roomId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found or you do not have permission to access it."));
    }

    @Transactional
    public Room updateRoom(Long roomId, RoomRequest request, User user) {
        Room room = getRoomById(roomId, user);
        room.setRoomType(request.getRoomType());
        room.setLengthM(request.getLengthM());
        room.setWidthM(request.getWidthM());
        room.setCeilingHeightM(request.getCeilingHeightM());
        room.setExistingFurniture(request.getExistingFurniture());
        room.setNumberOfDoors(request.getNumberOfDoors());
        room.setNumberOfWindows(request.getNumberOfWindows());
        room.setNumberOfOccupants(request.getNumberOfOccupants());
        room.setAdditionalRequirements(request.getAdditionalRequirements());

        return roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(Long roomId, User user) {
        Room room = getRoomById(roomId, user);
        if (room.getPhotoUrl() != null) {
            fileStorageService.deleteFile(room.getPhotoUrl());
        }
        roomRepository.delete(room);
    }

    @Transactional
    public Room uploadRoomPhoto(Long roomId, MultipartFile file, User user) {
        Room room = getRoomById(roomId, user);
        String oldPhoto = room.getPhotoUrl();

        String photoUrl = fileStorageService.storeFile(file);
        room.setPhotoUrl(photoUrl);

        Room saved = roomRepository.save(room);
        if (oldPhoto != null && !oldPhoto.isBlank()) {
            fileStorageService.deleteFile(oldPhoto);
        }
        return saved;
    }
}

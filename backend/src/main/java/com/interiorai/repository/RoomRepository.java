package com.interiorai.repository;

import com.interiorai.entity.Room;
import com.interiorai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByUserOrderByCreatedAtDesc(User user);
    Optional<Room> findByRoomIdAndUser(Long roomId, User user);
    long countByUser(User user);
}

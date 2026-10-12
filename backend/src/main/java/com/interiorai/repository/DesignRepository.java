package com.interiorai.repository;

import com.interiorai.entity.Design;
import com.interiorai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DesignRepository extends JpaRepository<Design, Long> {
    List<Design> findByRoom_UserOrderByCreatedAtDesc(User user);
    List<Design> findByRoom_RoomIdOrderByCreatedAtDesc(Long roomId);
    Optional<Design> findByDesignIdAndRoom_User(Long designId, User user);
    long countByRoom_User(User user);
}

package com.interiorai.repository;

import com.interiorai.entity.Budget;
import com.interiorai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByRoom_UserOrderByCreatedAtDesc(User user);
    List<Budget> findByRoom_RoomId(Long roomId);
    Optional<Budget> findByBudgetIdAndRoom_User(Long budgetId, User user);
    Optional<Budget> findFirstByDesign_DesignId(Long designId);
}

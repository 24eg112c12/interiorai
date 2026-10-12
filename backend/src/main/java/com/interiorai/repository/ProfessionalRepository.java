package com.interiorai.repository;

import com.interiorai.entity.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    List<Professional> findByServiceTypeIgnoreCase(String serviceType);
    List<Professional> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(String name, String location);
    List<Professional> findByServiceTypeIgnoreCaseAndLocationContainingIgnoreCase(String serviceType, String location);
}

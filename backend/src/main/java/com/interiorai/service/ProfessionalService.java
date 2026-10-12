package com.interiorai.service;

import com.interiorai.entity.Professional;
import com.interiorai.exception.ResourceNotFoundException;
import com.interiorai.repository.ProfessionalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    public ProfessionalService(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }

    public List<Professional> getAllProfessionals() {
        return professionalRepository.findAll();
    }

    public Professional getProfessionalById(Long professionalId) {
        return professionalRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found with ID: " + professionalId));
    }

    public List<Professional> getProfessionalsByCategory(String category) {
        if (category == null || category.isBlank() || "all".equalsIgnoreCase(category)) {
            return professionalRepository.findAll();
        }
        return professionalRepository.findByServiceTypeIgnoreCase(category.trim());
    }

    public List<Professional> searchProfessionals(String query, String category, String location) {
        if ((query == null || query.isBlank()) && (category == null || category.isBlank() || "all".equalsIgnoreCase(category)) && (location == null || location.isBlank())) {
            return professionalRepository.findAll();
        }

        if (category != null && !category.isBlank() && !"all".equalsIgnoreCase(category)) {
            if (location != null && !location.isBlank()) {
                return professionalRepository.findByServiceTypeIgnoreCaseAndLocationContainingIgnoreCase(category.trim(), location.trim());
            }
            return professionalRepository.findByServiceTypeIgnoreCase(category.trim());
        }

        String search = query != null && !query.isBlank() ? query.trim() : (location != null ? location.trim() : "");
        return professionalRepository.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(search, search);
    }
}

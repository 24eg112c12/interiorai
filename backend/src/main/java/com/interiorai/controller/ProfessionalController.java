package com.interiorai.controller;

import com.interiorai.dto.ApiResponse;
import com.interiorai.entity.Professional;
import com.interiorai.service.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Professional>>> getAllProfessionals() {
        List<Professional> professionals = professionalService.getAllProfessionals();
        return ResponseEntity.ok(ApiResponse.ok("Professionals retrieved.", professionals));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Professional>> getProfessionalById(@PathVariable("id") Long id) {
        Professional professional = professionalService.getProfessionalById(id);
        return ResponseEntity.ok(ApiResponse.ok("Professional retrieved.", professional));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Professional>>> searchProfessionals(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "location", required = false) String location) {
        List<Professional> results = professionalService.searchProfessionals(query, category, location);
        return ResponseEntity.ok(ApiResponse.ok("Found " + results.size() + " professionals matching criteria.", results));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Professional>>> getByCategory(@PathVariable("category") String category) {
        List<Professional> list = professionalService.getProfessionalsByCategory(category);
        return ResponseEntity.ok(ApiResponse.ok("Professionals in category " + category + " retrieved.", list));
    }
}

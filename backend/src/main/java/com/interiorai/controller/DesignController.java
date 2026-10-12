package com.interiorai.controller;

import com.interiorai.dto.ApiResponse;
import com.interiorai.dto.DesignRequest;
import com.interiorai.entity.Design;
import com.interiorai.entity.User;
import com.interiorai.service.AiDesignService;
import com.interiorai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DesignController {

    private final AiDesignService aiDesignService;
    private final UserService userService;

    public DesignController(AiDesignService aiDesignService, UserService userService) {
        this.aiDesignService = aiDesignService;
        this.userService = userService;
    }

    @PostMapping("/designs/generate")
    public ResponseEntity<ApiResponse<Design>> generateDesign(@Valid @RequestBody DesignRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        Design design = aiDesignService.generateDesign(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Interior design generated successfully.", design));
    }

    @GetMapping("/designs")
    public ResponseEntity<ApiResponse<List<Design>>> getUserDesigns() {
        User user = userService.getCurrentAuthenticatedUser();
        List<Design> designs = aiDesignService.getUserDesigns(user);
        return ResponseEntity.ok(ApiResponse.ok("Designs retrieved successfully.", designs));
    }

    @GetMapping("/designs/{id}")
    public ResponseEntity<ApiResponse<Design>> getDesignById(@PathVariable("id") Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        Design design = aiDesignService.getDesignById(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Design retrieved.", design));
    }

    @GetMapping("/rooms/{roomId}/designs")
    public ResponseEntity<ApiResponse<List<Design>>> getRoomDesigns(@PathVariable("roomId") Long roomId) {
        User user = userService.getCurrentAuthenticatedUser();
        List<Design> designs = aiDesignService.getRoomDesigns(roomId, user);
        return ResponseEntity.ok(ApiResponse.ok("Designs retrieved for room.", designs));
    }

    @DeleteMapping("/designs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDesign(@PathVariable("id") Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        aiDesignService.deleteDesign(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Design deleted successfully.", null));
    }
}

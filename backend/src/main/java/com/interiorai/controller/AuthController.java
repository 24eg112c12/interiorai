package com.interiorai.controller;

import com.interiorai.dto.ApiResponse;
import com.interiorai.dto.AuthResponse;
import com.interiorai.dto.LoginRequest;
import com.interiorai.dto.RegistrationRequest;
import com.interiorai.dto.UserDto;
import com.interiorai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegistrationRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Registration successful. Welcome to InteriorAI!", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful.", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully.", null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser() {
        UserDto profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(ApiResponse.ok("User profile retrieved.", profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> updateProfile(@RequestBody UserDto profileDto) {
        UserDto updated = userService.updateProfile(profileDto);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully.", updated));
    }
}

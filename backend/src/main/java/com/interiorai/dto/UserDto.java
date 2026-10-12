package com.interiorai.dto;

import com.interiorai.entity.User;
import java.time.LocalDateTime;

public class UserDto {
    private Long userId;
    private String fullName;
    private String email;
    private String mobile;
    private String role;
    private LocalDateTime createdAt;

    public UserDto() {
    }

    public UserDto(User user) {
        if (user != null) {
            this.userId = user.getUserId();
            this.fullName = user.getFullName();
            this.email = user.getEmail();
            this.mobile = user.getMobile();
            this.role = user.getRole();
            this.createdAt = user.getCreatedAt();
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

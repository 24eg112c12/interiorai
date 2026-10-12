package com.interiorai.controller;

import com.interiorai.dto.ApiResponse;
import com.interiorai.dto.ProgressUpdateRequest;
import com.interiorai.dto.ProjectRequest;
import com.interiorai.entity.Project;
import com.interiorai.entity.User;
import com.interiorai.service.ProjectService;
import com.interiorai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Project>> createProject(@Valid @RequestBody ProjectRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        Project project = projectService.createProject(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Renovation project created successfully.", project));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Project>>> getUserProjects() {
        User user = userService.getCurrentAuthenticatedUser();
        List<Project> list = projectService.getUserProjects(user);
        return ResponseEntity.ok(ApiResponse.ok("Projects retrieved.", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Project>> getProjectById(@PathVariable("id") Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        Project project = projectService.getProjectById(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Project retrieved.", project));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Project>> updateProject(@PathVariable("id") Long id,
                                                              @Valid @RequestBody ProjectRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        Project updated = projectService.updateProject(id, request, user);
        return ResponseEntity.ok(ApiResponse.ok("Project updated successfully.", updated));
    }

    @PatchMapping("/{id}/progress")
    public ResponseEntity<ApiResponse<Project>> updateProgress(@PathVariable("id") Long id,
                                                               @Valid @RequestBody ProgressUpdateRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        Project updated = projectService.updateProgress(id, request, user);
        return ResponseEntity.ok(ApiResponse.ok("Project progress updated.", updated));
    }
}

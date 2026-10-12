package com.interiorai.service;

import com.interiorai.dto.ProgressUpdateRequest;
import com.interiorai.dto.ProjectRequest;
import com.interiorai.entity.Design;
import com.interiorai.entity.Project;
import com.interiorai.entity.Room;
import com.interiorai.entity.User;
import com.interiorai.exception.ResourceNotFoundException;
import com.interiorai.repository.DesignRepository;
import com.interiorai.repository.ProjectRepository;
import com.interiorai.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final RoomRepository roomRepository;
    private final DesignRepository designRepository;

    public ProjectService(ProjectRepository projectRepository,
                          RoomRepository roomRepository,
                          DesignRepository designRepository) {
        this.projectRepository = projectRepository;
        this.roomRepository = roomRepository;
        this.designRepository = designRepository;
    }

    @Transactional
    public Project createProject(ProjectRequest request, User user) {
        Room room = roomRepository.findByRoomIdAndUser(request.getRoomId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found or unauthorized"));

        Design design = null;
        if (request.getDesignId() != null) {
            design = designRepository.findByDesignIdAndRoom_User(request.getDesignId(), user).orElse(null);
        }

        Project project = new Project();
        project.setUser(user);
        project.setRoom(room);
        project.setDesign(design);
        project.setProjectName(request.getProjectName().trim());
        project.setStatus(request.getStatus() != null ? request.getStatus().toUpperCase() : "PLANNING");
        project.setStartDate(request.getStartDate() != null ? request.getStartDate() : LocalDate.now());
        project.setTargetEndDate(request.getTargetEndDate());
        project.setProgressPercentage(request.getProgressPercentage() != null ? request.getProgressPercentage() : 0);

        return projectRepository.save(project);
    }

    public List<Project> getUserProjects(User user) {
        return projectRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Project getProjectById(Long projectId, User user) {
        return projectRepository.findByProjectIdAndUser(projectId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found or unauthorized."));
    }

    @Transactional
    public Project updateProject(Long projectId, ProjectRequest request, User user) {
        Project project = getProjectById(projectId, user);
        project.setProjectName(request.getProjectName().trim());
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus().toUpperCase());
        }
        if (request.getStartDate() != null) {
            project.setStartDate(request.getStartDate());
        }
        if (request.getTargetEndDate() != null) {
            project.setTargetEndDate(request.getTargetEndDate());
        }
        if (request.getProgressPercentage() != null) {
            project.setProgressPercentage(request.getProgressPercentage());
        }
        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProgress(Long projectId, ProgressUpdateRequest request, User user) {
        Project project = getProjectById(projectId, user);
        project.setProgressPercentage(request.getProgressPercentage());

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            project.setStatus(request.getStatus().toUpperCase());
        } else {
            // Auto update status based on progress
            if (request.getProgressPercentage() == 100) {
                project.setStatus("COMPLETED");
            } else if (request.getProgressPercentage() > 0 && "PLANNING".equalsIgnoreCase(project.getStatus())) {
                project.setStatus("IN_PROGRESS");
            }
        }
        return projectRepository.save(project);
    }
}

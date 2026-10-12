package com.interiorai.service;

import com.interiorai.dto.ProgressUpdateRequest;
import com.interiorai.dto.ProjectRequest;
import com.interiorai.entity.Project;
import com.interiorai.entity.Room;
import com.interiorai.entity.User;
import com.interiorai.repository.DesignRepository;
import com.interiorai.repository.ProjectRepository;
import com.interiorai.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private DesignRepository designRepository;

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectService = new ProjectService(projectRepository, roomRepository, designRepository);
    }

    @Test
    void testCreateProject_Success() {
        User user = new User("Homeowner", "home@test.com", "pass", "123");
        user.setUserId(1L);

        Room room = new Room();
        room.setRoomId(10L);
        room.setUser(user);
        room.setLengthM(new BigDecimal("5.0"));
        room.setWidthM(new BigDecimal("4.0"));

        when(roomRepository.findByRoomIdAndUser(10L, user)).thenReturn(Optional.of(room));

        Project saved = new Project();
        saved.setProjectId(50L);
        saved.setUser(user);
        saved.setRoom(room);
        saved.setProjectName("Dream Living Room Renovation");
        saved.setStatus("PLANNING");
        saved.setProgressPercentage(0);

        when(projectRepository.save(any(Project.class))).thenReturn(saved);

        ProjectRequest request = new ProjectRequest();
        request.setRoomId(10L);
        request.setProjectName("Dream Living Room Renovation");
        request.setStatus("PLANNING");
        request.setTargetEndDate(LocalDate.now().plusMonths(2));

        Project result = projectService.createProject(request, user);

        assertNotNull(result);
        assertEquals(50L, result.getProjectId());
        assertEquals("Dream Living Room Renovation", result.getProjectName());
        assertEquals("PLANNING", result.getStatus());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testUpdateProgress_AutoTransitionsToCompletedWhen100Percent() {
        User user = new User("Homeowner", "home@test.com", "pass", "123");
        user.setUserId(1L);

        Project project = new Project();
        project.setProjectId(50L);
        project.setUser(user);
        project.setStatus("IN_PROGRESS");
        project.setProgressPercentage(75);

        when(projectRepository.findByProjectIdAndUser(50L, user)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        ProgressUpdateRequest updateRequest = new ProgressUpdateRequest(100, null);
        Project updated = projectService.updateProgress(50L, updateRequest, user);

        assertEquals(100, updated.getProgressPercentage());
        assertEquals("COMPLETED", updated.getStatus());
    }
}

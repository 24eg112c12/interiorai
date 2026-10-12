package com.interiorai.controller;

import com.interiorai.dto.ApiResponse;
import com.interiorai.dto.DashboardSummaryDto;
import com.interiorai.entity.Booking;
import com.interiorai.entity.Design;
import com.interiorai.entity.Project;
import com.interiorai.entity.User;
import com.interiorai.repository.BookingRepository;
import com.interiorai.repository.DesignRepository;
import com.interiorai.repository.ProjectRepository;
import com.interiorai.repository.RoomRepository;
import com.interiorai.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final UserService userService;
    private final RoomRepository roomRepository;
    private final DesignRepository designRepository;
    private final ProjectRepository projectRepository;
    private final BookingRepository bookingRepository;

    public DashboardController(UserService userService,
                               RoomRepository roomRepository,
                               DesignRepository designRepository,
                               ProjectRepository projectRepository,
                               BookingRepository bookingRepository) {
        this.userService = userService;
        this.roomRepository = roomRepository;
        this.designRepository = designRepository;
        this.projectRepository = projectRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryDto>> getDashboardSummary() {
        User user = userService.getCurrentAuthenticatedUser();

        long totalRooms = roomRepository.countByUser(user);
        long totalDesigns = designRepository.countByRoom_User(user);
        long activeProjects = projectRepository.countByUserAndStatus(user, "IN_PROGRESS")
                + projectRepository.countByUserAndStatus(user, "PLANNING");
        long pendingBookings = bookingRepository.countByUserAndStatus(user, "PENDING");

        List<Design> recentDesigns = designRepository.findByRoom_UserOrderByCreatedAtDesc(user);
        if (recentDesigns.size() > 5) {
            recentDesigns = recentDesigns.subList(0, 5);
        }

        List<Project> recentProjects = projectRepository.findByUserOrderByCreatedAtDesc(user);
        if (recentProjects.size() > 5) {
            recentProjects = recentProjects.subList(0, 5);
        }

        List<Booking> recentBookings = bookingRepository.findByUserOrderByCreatedAtDesc(user);
        if (recentBookings.size() > 5) {
            recentBookings = recentBookings.subList(0, 5);
        }

        DashboardSummaryDto summary = new DashboardSummaryDto();
        summary.setTotalRooms(totalRooms);
        summary.setTotalSavedDesigns(totalDesigns);
        summary.setActiveProjects(activeProjects);
        summary.setPendingBookings(pendingBookings);
        summary.setTotalEstimatedBudget(new BigDecimal("150000"));
        summary.setRecentDesigns(recentDesigns);
        summary.setRecentProjects(recentProjects);
        summary.setRecentBookings(recentBookings);

        return ResponseEntity.ok(ApiResponse.ok("Dashboard summary retrieved.", summary));
    }
}

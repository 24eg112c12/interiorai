package com.interiorai.controller;

import com.interiorai.dto.ApiResponse;
import com.interiorai.dto.BudgetEstimateRequest;
import com.interiorai.dto.BudgetEstimateResponse;
import com.interiorai.dto.BudgetRequest;
import com.interiorai.entity.Budget;
import com.interiorai.entity.User;
import com.interiorai.service.BudgetService;
import com.interiorai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BudgetController {

    private final BudgetService budgetService;
    private final UserService userService;

    public BudgetController(BudgetService budgetService, UserService userService) {
        this.budgetService = budgetService;
        this.userService = userService;
    }

    @PostMapping("/budgets/estimate")
    public ResponseEntity<ApiResponse<BudgetEstimateResponse>> calculateEstimate(
            @Valid @RequestBody BudgetEstimateRequest request) {
        BudgetEstimateResponse response = budgetService.estimateBudget(request);
        return ResponseEntity.ok(ApiResponse.ok("Budget estimate calculated successfully.", response));
    }

    @PostMapping("/budgets")
    public ResponseEntity<ApiResponse<Budget>> createBudget(@Valid @RequestBody BudgetRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        Budget budget = budgetService.createBudget(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Budget saved successfully.", budget));
    }

    @GetMapping("/budgets/{id}")
    public ResponseEntity<ApiResponse<Budget>> getBudgetById(@PathVariable("id") Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        Budget budget = budgetService.getBudgetById(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Budget retrieved.", budget));
    }

    @GetMapping("/rooms/{roomId}/budgets")
    public ResponseEntity<ApiResponse<List<Budget>>> getBudgetsByRoom(@PathVariable("roomId") Long roomId) {
        User user = userService.getCurrentAuthenticatedUser();
        List<Budget> budgets = budgetService.getBudgetsByRoom(roomId, user);
        return ResponseEntity.ok(ApiResponse.ok("Budgets retrieved for room.", budgets));
    }

    @PutMapping("/budgets/{id}")
    public ResponseEntity<ApiResponse<Budget>> updateBudget(@PathVariable("id") Long id,
                                                            @Valid @RequestBody BudgetRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        Budget updated = budgetService.updateBudget(id, request, user);
        return ResponseEntity.ok(ApiResponse.ok("Budget updated successfully.", updated));
    }
}

package com.interiorai.service;

import com.interiorai.dto.BudgetEstimateRequest;
import com.interiorai.dto.BudgetEstimateResponse;
import com.interiorai.repository.BudgetItemRepository;
import com.interiorai.repository.BudgetRepository;
import com.interiorai.repository.DesignRepository;
import com.interiorai.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private BudgetItemRepository budgetItemRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private DesignRepository designRepository;

    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        budgetService = new BudgetService(budgetRepository, budgetItemRepository, roomRepository, designRepository);
    }

    @Test
    void testEstimateBudget_CalculatesAreaAndItems() {
        BudgetEstimateRequest request = new BudgetEstimateRequest();
        request.setLengthM(new BigDecimal("5.0"));
        request.setWidthM(new BigDecimal("4.0"));
        request.setCeilingHeightM(new BigDecimal("3.0"));
        request.setRoomType("Living Room");
        request.setInteriorStyle("Modern");
        request.setTotalBudget(new BigDecimal("150000"));

        BudgetEstimateResponse response = budgetService.estimateBudget(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("20.00"), response.getAreaSqM());
        assertTrue(response.getAreaSqFt().compareTo(new BigDecimal("200")) > 0);
        assertFalse(response.getItems().isEmpty());
        assertTrue(response.getEstimatedTotal().compareTo(BigDecimal.ZERO) > 0);
        assertNotNull(response.getStatus());
        assertFalse(response.getRecommendations().isEmpty());
    }

    @Test
    void testEstimateBudget_DetectsOverBudgetAndProvidesSavingsAdvice() {
        BudgetEstimateRequest request = new BudgetEstimateRequest();
        request.setLengthM(new BigDecimal("6.0"));
        request.setWidthM(new BigDecimal("5.0")); // 30 sq.m (~322 sq.ft)
        request.setRoomType("Master Bedroom");
        request.setInteriorStyle("Luxury");
        request.setTotalBudget(new BigDecimal("30000")); // Very low budget for luxury large room

        BudgetEstimateResponse response = budgetService.estimateBudget(request);

        assertTrue(response.isOverBudget());
        assertEquals("OVER_BUDGET", response.getStatus());
        assertTrue(response.getRemainingAmount().compareTo(BigDecimal.ZERO) < 0);
        // Verify cost-saving recommendations exist
        boolean hasRecommendation = response.getRecommendations().stream()
                .anyMatch(r -> r.contains("laminate") || r.contains("ceiling") || r.contains("teak"));
        assertTrue(hasRecommendation);
    }

    @Test
    void testEstimateBudget_DetectsUnderBudget() {
        BudgetEstimateRequest request = new BudgetEstimateRequest();
        request.setLengthM(new BigDecimal("3.0"));
        request.setWidthM(new BigDecimal("3.0"));
        request.setRoomType("Office");
        request.setInteriorStyle("Minimalist");
        request.setTotalBudget(new BigDecimal("300000")); // Generous budget

        BudgetEstimateResponse response = budgetService.estimateBudget(request);

        assertFalse(response.isOverBudget());
        assertEquals("UNDER_BUDGET", response.getStatus());
        assertTrue(response.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0);
    }
}

package com.interiorai.service;

import com.interiorai.dto.BudgetEstimateRequest;
import com.interiorai.dto.BudgetEstimateResponse;
import com.interiorai.dto.BudgetItemDto;
import com.interiorai.dto.BudgetRequest;
import com.interiorai.entity.Budget;
import com.interiorai.entity.BudgetItem;
import com.interiorai.entity.Design;
import com.interiorai.entity.Room;
import com.interiorai.entity.User;
import com.interiorai.exception.ResourceNotFoundException;
import com.interiorai.repository.BudgetItemRepository;
import com.interiorai.repository.BudgetRepository;
import com.interiorai.repository.DesignRepository;
import com.interiorai.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetItemRepository budgetItemRepository;
    private final RoomRepository roomRepository;
    private final DesignRepository designRepository;

    public BudgetService(BudgetRepository budgetRepository,
                         BudgetItemRepository budgetItemRepository,
                         RoomRepository roomRepository,
                         DesignRepository designRepository) {
        this.budgetRepository = budgetRepository;
        this.budgetItemRepository = budgetItemRepository;
        this.roomRepository = roomRepository;
        this.designRepository = designRepository;
    }

    public BudgetEstimateResponse estimateBudget(BudgetEstimateRequest request) {
        BigDecimal length = request.getLengthM();
        BigDecimal width = request.getWidthM();
        BigDecimal areaSqM = length.multiply(width).setScale(2, RoundingMode.HALF_UP);
        BigDecimal sqFtMultiplier = new BigDecimal("10.7639");
        BigDecimal areaSqFt = areaSqM.multiply(sqFtMultiplier).setScale(2, RoundingMode.HALF_UP);

        String style = request.getInteriorStyle() != null ? request.getInteriorStyle() : "Modern";
        double styleMultiplier = getStyleMultiplier(style);

        // Base rate in INR per sq.ft (approx ₹650/sq.ft for mid-range interior renovation in Indian metros)
        double baseRatePerSqFt = 650.0 * styleMultiplier;
        double rawEstimatedTotal = areaSqFt.doubleValue() * baseRatePerSqFt;

        // Ensure reasonable minimum threshold based on room dimensions
        if (rawEstimatedTotal < 25000.0) {
            rawEstimatedTotal = 25000.0;
        }

        // Percentage breakdown across core categories
        double furnitureAmount = rawEstimatedTotal * 0.36;
        double paintingAmount = rawEstimatedTotal * 0.14;
        double lightingAmount = rawEstimatedTotal * 0.10;
        double flooringAmount = rawEstimatedTotal * 0.10;
        double ceilingAmount = rawEstimatedTotal * 0.08;
        double decorAmount = rawEstimatedTotal * 0.06;
        double laborAmount = rawEstimatedTotal * 0.11;
        double miscAmount = rawEstimatedTotal * 0.05;

        List<BudgetItemDto> items = new ArrayList<>();
        items.add(new BudgetItemDto("Furniture", "Bed/Sofa, Wardrobe, Side Tables & Modular Storage", roundBigDecimal(furnitureAmount), "Ergonomic designs tailored to " + style + " style"));
        items.add(new BudgetItemDto("Painting", "Wall Emulsion & Accent Feature Wall Finish", roundBigDecimal(paintingAmount), "Dust-free primer, putty & 2-coat washable emulsion"));
        items.add(new BudgetItemDto("Lighting", "Ambient, Task & Warm Architectural Cove Lighting", roundBigDecimal(lightingAmount), "Energy-efficient warm white (3000K) LED fixtures"));
        items.add(new BudgetItemDto("Flooring", "Floor Treatment / Engineered Tile Polish / Rugs", roundBigDecimal(flooringAmount), "High-durability matte tiles or premium underlay rugs"));
        items.add(new BudgetItemDto("Ceiling work", "Gypsum False Ceiling & Peripheral Framing", roundBigDecimal(ceilingAmount), "Saint-Gobain Gyproc false ceiling with concealed wiring channels"));
        items.add(new BudgetItemDto("Decorative items", "Curtains, Wall Art, Mirrors & Planters", roundBigDecimal(decorAmount), "Coordinated textiles, natural indoor greenery and accents"));
        items.add(new BudgetItemDto("Labor", "Skilled Carpentry, Electrical & Installation Labor", roundBigDecimal(laborAmount), "Verified licensed craftsmen across all trades"));
        items.add(new BudgetItemDto("Miscellaneous expenses", "Hardware, Fasteners, Transportation & Clean-up", roundBigDecimal(miscAmount), "Site protection sheets, transport and post-renovation cleanup"));

        BigDecimal calculatedTotal = BigDecimal.ZERO;
        for (BudgetItemDto item : items) {
            calculatedTotal = calculatedTotal.add(item.getEstimatedAmount());
        }

        BigDecimal userBudget = request.getTotalBudget();
        BigDecimal diff = userBudget.subtract(calculatedTotal);

        BudgetEstimateResponse response = new BudgetEstimateResponse();
        response.setUserBudget(userBudget);
        response.setEstimatedTotal(calculatedTotal);
        response.setRemainingAmount(diff);
        response.setAreaSqM(areaSqM);
        response.setAreaSqFt(areaSqFt);
        response.setItems(items);

        List<String> recommendations = new ArrayList<>();
        if (diff.compareTo(BigDecimal.ZERO) < 0) {
            response.setOverBudget(true);
            response.setStatus("OVER_BUDGET");
            recommendations.add("Consider opting for high-density HDHMR boards with scratch-resistant acrylic laminate instead of solid teak wood to save up to 35% on furniture.");
            recommendations.add("Select peripheral perimeter cove false ceiling instead of full-coverage false ceiling to reduce POP and channel costs by ~₹12,000.");
            recommendations.add("Use high-quality Asian Paints Royale emulsion for the focal wall and premium tractor emulsion for remaining walls.");
            recommendations.add("Standardize wardrobe dimensions to modular factory-pressed units to significantly reduce custom on-site carpentry labor.");
        } else if (diff.compareTo(new BigDecimal("15000")) > 0) {
            response.setOverBudget(false);
            response.setStatus("UNDER_BUDGET");
            recommendations.add("Your allocated budget comfortably covers this interior scope with a surplus of ₹" + diff.toPlainString() + ".");
            recommendations.add("Consider upgrading to smart Zigbee/Wi-Fi mood lighting dimmers or acoustic motorized curtain tracks.");
            recommendations.add("You can incorporate fluted charcoal acoustic wall panelling behind the bed or TV console for added luxury.");
        } else {
            response.setOverBudget(false);
            response.setStatus("WITHIN_BUDGET");
            recommendations.add("Your allocated budget is well-balanced for this room transformation.");
            recommendations.add("Reserve a 5-10% contingency buffer for on-site civil adjustments.");
        }
        response.setRecommendations(recommendations);

        return response;
    }

    private double getStyleMultiplier(String style) {
        return switch (style.toLowerCase()) {
            case "luxury" -> 1.45;
            case "traditional indian" -> 1.25;
            case "contemporary" -> 1.15;
            case "industrial" -> 1.10;
            case "scandinavian" -> 1.05;
            case "bohemian" -> 0.95;
            case "minimalist" -> 0.85;
            default -> 1.00;
        };
    }

    private BigDecimal roundBigDecimal(double val) {
        return BigDecimal.valueOf(Math.round(val / 500.0) * 500).setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional
    public Budget createBudget(BudgetRequest request, User user) {
        Room room = roomRepository.findByRoomIdAndUser(request.getRoomId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found or unauthorized"));

        Design design = null;
        if (request.getDesignId() != null) {
            design = designRepository.findByDesignIdAndRoom_User(request.getDesignId(), user).orElse(null);
        }

        Budget budget = new Budget();
        budget.setRoom(room);
        budget.setDesign(design);
        budget.setTotalBudget(request.getTotalBudget());
        budget.setEstimatedCost(request.getEstimatedCost() != null ? request.getEstimatedCost() : request.getTotalBudget());
        budget.setActualCost(request.getActualCost() != null ? request.getActualCost() : BigDecimal.ZERO);
        budget.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (BudgetItemDto itemDto : request.getItems()) {
                BudgetItem item = new BudgetItem(
                        itemDto.getCategory(),
                        itemDto.getItemName(),
                        itemDto.getEstimatedAmount(),
                        itemDto.getNotes()
                );
                budget.addItem(item);
            }
        }

        return budgetRepository.save(budget);
    }

    public Budget getBudgetById(Long budgetId, User user) {
        return budgetRepository.findByBudgetIdAndRoom_User(budgetId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found or unauthorized."));
    }

    public List<Budget> getBudgetsByRoom(Long roomId, User user) {
        Room room = roomRepository.findByRoomIdAndUser(roomId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found or unauthorized"));
        return budgetRepository.findByRoom_RoomId(room.getRoomId());
    }

    @Transactional
    public Budget updateBudget(Long budgetId, BudgetRequest request, User user) {
        Budget budget = getBudgetById(budgetId, user);
        budget.setTotalBudget(request.getTotalBudget());
        if (request.getEstimatedCost() != null) {
            budget.setEstimatedCost(request.getEstimatedCost());
        }
        if (request.getActualCost() != null) {
            budget.setActualCost(request.getActualCost());
        }
        return budgetRepository.save(budget);
    }
}

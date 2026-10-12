package com.interiorai.service;

import com.interiorai.dto.BudgetEstimateRequest;
import com.interiorai.dto.BudgetEstimateResponse;
import com.interiorai.dto.BudgetRequest;
import com.interiorai.dto.DesignRequest;
import com.interiorai.entity.Design;
import com.interiorai.entity.Room;
import com.interiorai.entity.User;
import com.interiorai.exception.ResourceNotFoundException;
import com.interiorai.repository.DesignRepository;
import com.interiorai.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiDesignService {

    private static final Logger log = LoggerFactory.getLogger(AiDesignService.class);

    private final DesignRepository designRepository;
    private final RoomRepository roomRepository;
    private final BudgetService budgetService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${interiorai.ai.api-key:}")
    private String apiKey;

    @Value("${interiorai.ai.api-url:https://api.openai.com/v1/images/generations}")
    private String apiUrl;

    @Value("${interiorai.ai.demo-mode:true}")
    private boolean demoMode;

    @Value("${interiorai.ai.model:dall-e-3}")
    private String aiModel;

    public AiDesignService(DesignRepository designRepository,
                           RoomRepository roomRepository,
                           BudgetService budgetService) {
        this.designRepository = designRepository;
        this.roomRepository = roomRepository;
        this.budgetService = budgetService;
    }

    @Transactional
    public Design generateDesign(DesignRequest request, User user) {
        Room room = roomRepository.findByRoomIdAndUser(request.getRoomId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found or unauthorized"));

        String style = request.getInteriorStyle() != null && !request.getInteriorStyle().isBlank()
                ? request.getInteriorStyle() : "Modern";
        String roomType = room.getRoomType() != null ? room.getRoomType() : "Living Room";

        String designName = request.getDesignName();
        if (designName == null || designName.isBlank()) {
            designName = style + " " + roomType + " Makeover";
        }

        // Construct detailed prompt
        String prompt = buildPrompt(room, request, style);

        String generatedImageUrl;
        String status = "COMPLETED";

        // Check if real AI API key is configured and demoMode is disabled
        if (!demoMode && apiKey != null && !apiKey.isBlank() && !apiKey.startsWith("YOUR_")) {
            try {
                generatedImageUrl = callExternalAiApi(prompt);
            } catch (Exception e) {
                log.warn("External AI image generation failed: {}. Falling back to curated design mode.", e.getMessage());
                generatedImageUrl = getCuratedDemoImage(roomType, style);
            }
        } else {
            // Demonstration Mode with curated realistic high-resolution room designs
            generatedImageUrl = getCuratedDemoImage(roomType, style);
        }

        Design design = new Design();
        design.setRoom(room);
        design.setDesignName(designName);
        design.setInteriorStyle(style);
        design.setColorPalette(request.getColorPalette() != null ? request.getColorPalette() : "Warm Beige, Charcoal & Natural Oak");
        design.setDescription(request.getDesignDescription() != null && !request.getDesignDescription().isBlank()
                ? request.getDesignDescription()
                : "Harmonious " + style.toLowerCase() + " interior with optimized natural lighting, bespoke joinery, and ergonomic space distribution.");
        design.setImageUrl(generatedImageUrl);
        design.setPromptUsed(prompt);
        design.setGenerationStatus(status);

        Design savedDesign = designRepository.save(design);

        // Auto-generate linked budget estimate for this new design
        BigDecimal budgetAmount = request.getBudgetInr() != null ? request.getBudgetInr() : new BigDecimal("75000");
        BudgetEstimateRequest estimateRequest = new BudgetEstimateRequest();
        estimateRequest.setLengthM(room.getLengthM());
        estimateRequest.setWidthM(room.getWidthM());
        estimateRequest.setCeilingHeightM(room.getCeilingHeightM());
        estimateRequest.setRoomType(roomType);
        estimateRequest.setInteriorStyle(style);
        estimateRequest.setTotalBudget(budgetAmount);

        BudgetEstimateResponse estimateResponse = budgetService.estimateBudget(estimateRequest);

        BudgetRequest budgetRequest = new BudgetRequest();
        budgetRequest.setRoomId(room.getRoomId());
        budgetRequest.setDesignId(savedDesign.getDesignId());
        budgetRequest.setTotalBudget(budgetAmount);
        budgetRequest.setEstimatedCost(estimateResponse.getEstimatedTotal());
        budgetRequest.setItems(estimateResponse.getItems());

        budgetService.createBudget(budgetRequest, user);

        return savedDesign;
    }

    private String buildPrompt(Room room, DesignRequest request, String style) {
        StringBuilder sb = new StringBuilder();
        sb.append("Architectural interior photograph of a ").append(room.getRoomType()).append(", ");
        sb.append("redesigned in ").append(style).append(" style. ");
        sb.append("Dimensions: ").append(room.getLengthM()).append("m length by ").append(room.getWidthM()).append("m width");
        if (room.getCeilingHeightM() != null) {
            sb.append(" with ").append(room.getCeilingHeightM()).append("m ceiling height. ");
        } else {
            sb.append(". ");
        }
        if (request.getColorPalette() != null) {
            sb.append("Color palette: ").append(request.getColorPalette()).append(". ");
        }
        if (request.getFurniturePreferences() != null) {
            sb.append("Furniture: ").append(request.getFurniturePreferences()).append(". ");
        }
        sb.append("Photorealistic, natural soft daylight, wide-angle lens, 8k resolution, architectural digest style.");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private String callExternalAiApi(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", aiModel);
        body.put("prompt", prompt);
        body.put("n", 1);
        body.put("size", "1024x1024");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("data")) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
            if (!data.isEmpty() && data.get(0).containsKey("url")) {
                return (String) data.get(0).get("url");
            }
        }
        throw new RuntimeException("No image URL returned from AI provider.");
    }

    public String getCuratedDemoImage(String roomType, String style) {
        String lowerStyle = style != null ? style.toLowerCase() : "modern";
        String lowerRoom = roomType != null ? roomType.toLowerCase() : "living room";

        if (lowerStyle.contains("traditional") || lowerStyle.contains("indian")) {
            if (lowerRoom.contains("bedroom")) {
                return "https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?auto=format&fit=crop&w=1200&q=80";
            }
            return "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1200&q=80";
        } else if (lowerStyle.contains("scandinavian")) {
            if (lowerRoom.contains("bedroom")) {
                return "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?auto=format&fit=crop&w=1200&q=80";
            }
            return "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?auto=format&fit=crop&w=1200&q=80";
        } else if (lowerStyle.contains("minimalist")) {
            if (lowerRoom.contains("kitchen")) {
                return "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?auto=format&fit=crop&w=1200&q=80";
            }
            return "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?auto=format&fit=crop&w=1200&q=80";
        } else if (lowerStyle.contains("luxury")) {
            return "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?auto=format&fit=crop&w=1200&q=80";
        } else if (lowerStyle.contains("industrial")) {
            return "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&w=1200&q=80";
        } else if (lowerStyle.contains("bohemian")) {
            return "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?auto=format&fit=crop&w=1200&q=80";
        } else if (lowerStyle.contains("contemporary")) {
            return "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?auto=format&fit=crop&w=1200&q=80";
        }

        // Modern default
        if (lowerRoom.contains("kitchen")) {
            return "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?auto=format&fit=crop&w=1200&q=80";
        } else if (lowerRoom.contains("bedroom")) {
            return "https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?auto=format&fit=crop&w=1200&q=80";
        } else if (lowerRoom.contains("bathroom")) {
            return "https://images.unsplash.com/photo-1584622650111-993a426fbf0a?auto=format&fit=crop&w=1200&q=80";
        } else if (lowerRoom.contains("office")) {
            return "https://images.unsplash.com/photo-1524758631624-e2822e304c36?auto=format&fit=crop&w=1200&q=80";
        }
        return "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1200&q=80";
    }

    public List<Design> getUserDesigns(User user) {
        return designRepository.findByRoom_UserOrderByCreatedAtDesc(user);
    }

    public List<Design> getRoomDesigns(Long roomId, User user) {
        Room room = roomRepository.findByRoomIdAndUser(roomId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found or unauthorized"));
        return designRepository.findByRoom_RoomIdOrderByCreatedAtDesc(room.getRoomId());
    }

    public Design getDesignById(Long designId, User user) {
        return designRepository.findByDesignIdAndRoom_User(designId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Design not found or unauthorized"));
    }

    @Transactional
    public void deleteDesign(Long designId, User user) {
        Design design = getDesignById(designId, user);
        designRepository.delete(design);
    }
}

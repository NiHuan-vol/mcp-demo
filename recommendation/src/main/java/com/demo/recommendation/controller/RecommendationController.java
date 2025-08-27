package com.demo.recommendation.controller;

import com.demo.recommendation.mcp.assistant.AssistantRequest;
import com.demo.recommendation.mcp.assistant.AssistantResponse;
import com.demo.recommendation.mcp.assistant.TravelAssistant;
import com.demo.recommendation.mcp.commercial.CommercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 自驾游推荐服务的控制器
 * 提供各种推荐功能的API接口
 */
@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {

    @Autowired
    private TravelAssistant travelAssistant;
    
    @Autowired
    private CommercialService commercialService;

    /**
     * 处理旅行助手请求
     * 通用接口，支持不同类型的请求
     */
    @PostMapping("/assistant/process")
    public Mono<AssistantResponse> processAssistantRequest(@RequestBody AssistantRequest request) {
        // 确保请求ID存在
        if (request.getRequestId() == null || request.getRequestId().isEmpty()) {
            request.setRequestId(UUID.randomUUID().toString());
        }
        
        // 确保时间戳存在
        if (request.getTimestamp() <= 0) {
            request.setTimestamp(System.currentTimeMillis());
        }
        
        return travelAssistant.processRequest(request);
    }
    
    /**
     * 智能路线规划接口
     * 专门用于路线规划的便捷接口
     */
    @GetMapping("/route/planning")
    public Mono<AssistantResponse> planRoute(
            @RequestParam String startLocation, 
            @RequestParam String endLocation, 
            @RequestParam(required = false, defaultValue = "高速优先") String preferences,
            @RequestParam(required = false, defaultValue = "amap") String mapProvider,
            @RequestParam(required = false) String userId) {
        
        AssistantRequest request = new AssistantRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setUserId(userId);
        request.setRequestType("routePlanning");
        request.setContent("规划从" + startLocation + "到" + endLocation + "的路线");
        request.setTimestamp(System.currentTimeMillis());
        
        Map<String, Object> params = new HashMap<>();
        params.put("startLocation", startLocation);
        params.put("endLocation", endLocation);
        params.put("preferences", preferences);
        params.put("mapProvider", mapProvider);
        
        request.setParameters(params);
        
        return travelAssistant.processRequest(request);
    }
    
    /**
     * 沿途服务推荐接口
     */
    @GetMapping("/service/recommendation")
    public Mono<AssistantResponse> recommendService(
            @RequestParam String location,
            @RequestParam(required = false, defaultValue = "restaurant") String serviceType,
            @RequestParam(required = false, defaultValue = "5.0") double radius,
            @RequestParam(required = false, defaultValue = "4.0") double ratingThreshold,
            @RequestParam(required = false) String userId) {
        
        AssistantRequest request = new AssistantRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setUserId(userId);
        request.setRequestType("serviceRecommendation");
        request.setContent("推荐" + location + "附近的" + getServiceTypeName(serviceType) + "服务");
        request.setTimestamp(System.currentTimeMillis());
        
        Map<String, Object> params = new HashMap<>();
        params.put("location", location);
        params.put("serviceType", serviceType);
        params.put("radius", radius);
        params.put("ratingThreshold", ratingThreshold);
        
        request.setParameters(params);
        
        return travelAssistant.processRequest(request);
    }
    
    /**
     * 智能翻译接口
     */
    @GetMapping("/translation")
    public Mono<AssistantResponse> translate(
            @RequestParam String text,
            @RequestParam(required = false, defaultValue = "auto") String sourceLanguage,
            @RequestParam(required = false, defaultValue = "zh") String targetLanguage,
            @RequestParam(required = false, defaultValue = "baidu") String providerName,
            @RequestParam(required = false) String userId) {
        
        AssistantRequest request = new AssistantRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setUserId(userId);
        request.setRequestType("translation");
        request.setContent(text);
        request.setTimestamp(System.currentTimeMillis());
        
        Map<String, Object> params = new HashMap<>();
        params.put("text", text);
        params.put("sourceLanguage", sourceLanguage);
        params.put("targetLanguage", targetLanguage);
        params.put("providerName", providerName);
        
        request.setParameters(params);
        
        return travelAssistant.processRequest(request);
    }
    
    /**
     * 社交与攻略推荐接口
     */
    @GetMapping("/travel-guide")
    public Mono<AssistantResponse> recommendTravelGuide(
            @RequestParam(required = false) String keyword,
            @RequestParam String location,
            @RequestParam(required = false, defaultValue = "3") int maxDuration,
            @RequestParam(required = false, defaultValue = "4.5") double popularityThreshold,
            @RequestParam(required = false) String userId) {
        
        AssistantRequest request = new AssistantRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setUserId(userId);
        request.setRequestType("travelGuide");
        request.setContent(keyword != null ? "查找关于" + keyword + "的攻略" : "查找" + location + "附近的攻略");
        request.setTimestamp(System.currentTimeMillis());
        
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("location", location);
        params.put("maxDuration", maxDuration);
        params.put("popularityThreshold", popularityThreshold);
        
        request.setParameters(params);
        
        return travelAssistant.processRequest(request);
    }
    
    /**
     * 紧急服务接口
     */
    @PostMapping("/emergency")
    public Mono<AssistantResponse> requestEmergencyService(
            @RequestParam String emergencyType,
            @RequestParam String location,
            @RequestBody(required = false) Map<String, Object> additionalParams,
            @RequestParam(required = false) String userId) {
        
        AssistantRequest request = new AssistantRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setUserId(userId);
        request.setRequestType("emergency");
        request.setContent("请求" + getEmergencyTypeName(emergencyType) + "服务");
        request.setTimestamp(System.currentTimeMillis());
        
        Map<String, Object> params = new HashMap<>();
        params.put("emergencyType", emergencyType);
        params.put("location", location);
        
        // 添加额外参数
        if (additionalParams != null) {
            params.putAll(additionalParams);
        }
        
        request.setParameters(params);
        
        return travelAssistant.processRequest(request);
    }
    
    /**
     * 创建订阅
     */
    @PostMapping("/commercial/subscription/create")
    public Map<String, Object> createSubscription(
            @RequestParam String userId,
            @RequestParam String subscriptionType,
            @RequestParam String paymentMethod) {
        
        return commercialService.createSubscription(userId, subscriptionType, paymentMethod);
    }
    
    /**
     * 查询订阅状态
     */
    @GetMapping("/commercial/subscription/status")
    public Map<String, Object> getSubscriptionStatus(@RequestParam String userId) {
        
        return commercialService.getSubscriptionStatus(userId);
    }
    
    /**
     * 取消订阅
     */
    @PostMapping("/commercial/subscription/cancel")
    public Map<String, Object> cancelSubscription(
            @RequestParam String userId,
            @RequestParam String subscriptionId) {
        
        return commercialService.cancelSubscription(userId, subscriptionId);
    }
    
    /**
     * 检查功能访问权限
     */
    @GetMapping("/commercial/feature-access")
    public Map<String, Object> checkFeatureAccess(
            @RequestParam String userId,
            @RequestParam String featureCode) {
        
        boolean hasAccess = commercialService.checkFeatureAccess(userId, featureCode);
        Map<String, Object> result = new HashMap<>();
        result.put("status", "SUCCESS");
        result.put("hasAccess", hasAccess);
        result.put("featureCode", featureCode);
        
        return result;
    }
    
    /**
     * 获取服务类型的中文名称
     */
    private String getServiceTypeName(String serviceType) {
        switch (serviceType) {
            case "restaurant": return "餐饮";
            case "hotel": return "酒店";
            case "gasStation": return "加油站";
            case "chargingStation": return "充电桩";
            default: return serviceType;
        }
    }
    
    /**
     * 获取紧急服务类型的中文名称
     */
    private String getEmergencyTypeName(String emergencyType) {
        switch (emergencyType) {
            case "roadsideAssistance": return "道路救援";
            case "insurance": return "保险服务";
            default: return "紧急";
        }
    }
}
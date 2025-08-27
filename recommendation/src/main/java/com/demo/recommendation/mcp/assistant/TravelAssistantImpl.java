package com.demo.recommendation.mcp.assistant;

import com.demo.recommendation.mcp.gateway.MapServiceGateway;
import com.demo.recommendation.mcp.gateway.WeatherServiceGateway;
import com.demo.recommendation.mcp.gateway.ServiceRecommendationGateway;
import com.demo.recommendation.mcp.gateway.TranslationServiceGateway;
import com.demo.recommendation.mcp.gateway.TravelGuideGateway;
import com.demo.recommendation.mcp.gateway.EmergencyServiceGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 旅行助手实现类，集成各种网关服务，处理用户的智能旅行请求
 */
@Component
public class TravelAssistantImpl implements TravelAssistant {

    private static final String ASSISTANT_ID = "travel-assistant-001";
    private static final String ASSISTANT_NAME = "智能旅行助手";
    private boolean initialized = false;
    
    @Autowired
    private MapServiceGateway mapServiceGateway;
    
    @Autowired
    private WeatherServiceGateway weatherServiceGateway;
    
    @Autowired
    private ServiceRecommendationGateway serviceRecommendationGateway;
    
    @Autowired
    private TranslationServiceGateway translationServiceGateway;
    
    @Autowired
    private TravelGuideGateway travelGuideGateway;
    
    @Autowired
    private EmergencyServiceGateway emergencyServiceGateway;
    
    // 存储用户上下文
    private final Map<String, Map<String, Object>> userContexts = new ConcurrentHashMap<>();

    @Override
    public void initialize() {
        // 初始化所有网关
        mapServiceGateway.initialize();
        weatherServiceGateway.initialize();
        serviceRecommendationGateway.initialize();
        translationServiceGateway.initialize();
        travelGuideGateway.initialize();
        emergencyServiceGateway.initialize();
        
        initialized = true;
        System.out.println(ASSISTANT_NAME + "初始化完成");
    }

    @Override
    public void shutdown() {
        // 关闭所有网关
        mapServiceGateway.shutdown();
        weatherServiceGateway.shutdown();
        serviceRecommendationGateway.shutdown();
        translationServiceGateway.shutdown();
        travelGuideGateway.shutdown();
        emergencyServiceGateway.shutdown();
        
        // 清空用户上下文
        userContexts.clear();
        
        initialized = false;
        System.out.println(ASSISTANT_NAME + "已关闭");
    }

    @Override
    public Mono<AssistantResponse> processRequest(AssistantRequest request) {
        long startTime = System.currentTimeMillis();
        
        // 创建响应对象
        AssistantResponse response = new AssistantResponse();
        response.setResponseId(UUID.randomUUID().toString());
        response.setRequestId(request.getRequestId());
        response.setStatus("SUCCESS");
        
        try {
            // 根据请求类型处理不同的任务
            String requestType = request.getRequestType();
            
            if ("routePlanning".equals(requestType)) {
                // 智能路线规划
                processRoutePlanning(request, response);
            } else if ("serviceRecommendation".equals(requestType)) {
                // 沿途服务推荐
                processServiceRecommendation(request, response);
            } else if ("translation".equals(requestType)) {
                // 智能翻译
                processTranslation(request, response);
            } else if ("travelGuide".equals(requestType)) {
                // 社交与攻略推荐
                processTravelGuide(request, response);
            } else if ("emergency".equals(requestType)) {
                // 紧急服务
                processEmergency(request, response);
            } else if ("chat".equals(requestType)) {
                // 通用聊天
                processChat(request, response);
            } else {
                response.setStatus("FAILED");
                response.setErrorMessage("不支持的请求类型: " + requestType);
            }
            
            // 保存用户上下文
            saveUserContext(request);
            
        } catch (Exception e) {
            response.setStatus("FAILED");
            response.setErrorMessage("处理请求时发生错误: " + e.getMessage());
        } finally {
            // 计算处理时间
            response.setProcessingTime(System.currentTimeMillis() - startTime);
        }
        
        return Mono.just(response);
    }

    @Override
    public String getAssistantId() {
        return ASSISTANT_ID;
    }

    @Override
    public String getAssistantName() {
        return ASSISTANT_NAME;
    }

    @Override
    public boolean isAvailable() {
        return initialized && 
               mapServiceGateway.isAvailable() &&
               weatherServiceGateway.isAvailable() &&
               serviceRecommendationGateway.isAvailable() &&
               translationServiceGateway.isAvailable() &&
               travelGuideGateway.isAvailable() &&
               emergencyServiceGateway.isAvailable();
    }

    /**
     * 处理智能路线规划请求
     */
    private void processRoutePlanning(AssistantRequest request, AssistantResponse response) {
        String startLocation = (String) request.getParameters().getOrDefault("startLocation", "");
        String endLocation = (String) request.getParameters().getOrDefault("endLocation", "");
        String preferences = (String) request.getParameters().getOrDefault("preferences", "高速优先");
        String mapProvider = (String) request.getParameters().getOrDefault("mapProvider", "amap");
        
        // 验证参数
        if (startLocation.isEmpty() || endLocation.isEmpty()) {
            response.setStatus("FAILED");
            response.setErrorMessage("起始位置和目的地不能为空");
            return;
        }
        
        // 调用地图服务规划路线
        Map<String, Object> routeResult = mapServiceGateway.planRoute(startLocation, endLocation, preferences, mapProvider);
        
        // 检查路线是否受到天气影响
        if ("SUCCESS".equals(routeResult.get("status"))) {
            String[] routePoints = (String[]) routeResult.getOrDefault("routePoints", new String[0]);
            Map<String, Object> weatherCheckResult = weatherServiceGateway.checkRouteWeather(routePoints);
            
            if ((boolean) weatherCheckResult.getOrDefault("hasWeatherIssues", false)) {
                routeResult.put("weatherWarning", "当前路线部分路段有天气问题，建议调整");
                routeResult.put("weatherIssues", weatherCheckResult.get("affectedPoints"));
            }
        }
        
        response.setContent("路线规划完成");
        response.setData(routeResult);
    }

    /**
     * 处理沿途服务推荐请求
     */
    private void processServiceRecommendation(AssistantRequest request, AssistantResponse response) {
        String location = (String) request.getParameters().getOrDefault("location", "");
        String serviceType = (String) request.getParameters().getOrDefault("serviceType", "restaurant");
        double radius = (double) request.getParameters().getOrDefault("radius", 5.0);
        double ratingThreshold = (double) request.getParameters().getOrDefault("ratingThreshold", 4.0);
        
        // 验证参数
        if (location.isEmpty()) {
            response.setStatus("FAILED");
            response.setErrorMessage("位置信息不能为空");
            return;
        }
        
        // 处理用户的自然语言请求
        String content = request.getContent();
        if (content != null && !content.isEmpty()) {
            // 简单的意图识别
            if (content.contains("川菜")) {
                Map<String, Object> params = new HashMap<>();
                params.put("serviceType", "restaurant");
                params.put("cuisineType", "川菜");
                request.setParameters(params);
            }
        }
        
        // 调用服务推荐网关
        Map<String, Object> serviceResult = serviceRecommendationGateway.searchNearbyServices(
                location, serviceType, radius, ratingThreshold);
        
        response.setContent("找到 " + serviceResult.get("totalCount") + " 个符合条件的" + 
                           getServiceTypeName(serviceType) + "服务");
        response.setData(serviceResult);
    }

    /**
     * 处理智能翻译请求
     */
    private void processTranslation(AssistantRequest request, AssistantResponse response) {
        String text = (String) request.getParameters().getOrDefault("text", "");
        String sourceLanguage = (String) request.getParameters().getOrDefault("sourceLanguage", "auto");
        String targetLanguage = (String) request.getParameters().getOrDefault("targetLanguage", "zh");
        String providerName = (String) request.getParameters().getOrDefault("providerName", "baidu");
        
        // 验证参数
        if (text.isEmpty()) {
            response.setStatus("FAILED");
            response.setErrorMessage("待翻译文本不能为空");
            return;
        }
        
        // 调用翻译服务网关
        Map<String, Object> translationResult = translationServiceGateway.translateText(
                text, sourceLanguage, targetLanguage, providerName);
        
        response.setContent("翻译完成");
        response.setData(translationResult);
    }

    /**
     * 处理社交与攻略推荐请求
     */
    private void processTravelGuide(AssistantRequest request, AssistantResponse response) {
        String keyword = (String) request.getParameters().getOrDefault("keyword", "");
        String location = (String) request.getParameters().getOrDefault("location", "");
        int maxDuration = (int) request.getParameters().getOrDefault("maxDuration", 3);
        double popularityThreshold = (double) request.getParameters().getOrDefault("popularityThreshold", 4.5);
        
        // 验证参数
        if (location.isEmpty()) {
            response.setStatus("FAILED");
            response.setErrorMessage("位置信息不能为空");
            return;
        }
        
        // 处理用户的自然语言请求
        String content = request.getContent();
        if (content != null && !content.isEmpty()) {
            // 简单的意图识别
            if (content.contains("网红景点")) {
                // 推荐网红景点
                Map<String, Object> popularAttractions = travelGuideGateway.recommendPopularAttractions(
                        location, 10.0, 5);
                response.setContent("找到 " + popularAttractions.get("totalCount") + " 个网红景点");
                response.setData(popularAttractions);
                return;
            }
        }
        
        // 调用攻略服务网关
        Map<String, Object> guideResult = travelGuideGateway.searchTravelGuides(
                keyword, location, maxDuration, popularityThreshold);
        
        response.setContent("找到 " + guideResult.get("totalCount") + " 篇符合条件的攻略");
        response.setData(guideResult);
    }

    /**
     * 处理紧急服务请求
     */
    private void processEmergency(AssistantRequest request, AssistantResponse response) {
        String emergencyType = (String) request.getParameters().getOrDefault("emergencyType", "");
        String location = (String) request.getParameters().getOrDefault("location", "");
        
        // 验证参数
        if (emergencyType.isEmpty() || location.isEmpty()) {
            response.setStatus("FAILED");
            response.setErrorMessage("紧急类型和位置信息不能为空");
            return;
        }
        
        if ("roadsideAssistance".equals(emergencyType)) {
            // 请求道路救援
            Map<String, Object> vehicleInfo = (Map<String, Object>) request.getParameters().getOrDefault("vehicleInfo", new HashMap<>());
            Map<String, Object> assistanceResult = emergencyServiceGateway.requestRoadsideAssistance(
                    location, vehicleInfo, emergencyType);
            
            response.setContent("道路救援请求已提交，救援人员正在赶来");
            response.setData(assistanceResult);
        } else if ("insurance".equals(emergencyType)) {
            // 联系保险公司
            Map<String, Object> policyInfo = (Map<String, Object>) request.getParameters().getOrDefault("policyInfo", new HashMap<>());
            Map<String, Object> incidentInfo = (Map<String, Object>) request.getParameters().getOrDefault("incidentInfo", new HashMap<>());
            Map<String, Object> insuranceResult = emergencyServiceGateway.contactInsuranceCompany(
                    policyInfo, emergencyType, incidentInfo);
            
            response.setContent("已为您联系保险公司，请保持电话畅通");
            response.setData(insuranceResult);
        } else {
            // 获取附近救援点
            Map<String, Object> rescuePoints = emergencyServiceGateway.getNearbyRescuePoints(location, 10.0, 5);
            
            response.setContent("找到 " + rescuePoints.get("totalCount") + " 个附近的救援点");
            response.setData(rescuePoints);
        }
    }

    /**
     * 处理通用聊天请求
     */
    private void processChat(AssistantRequest request, AssistantResponse response) {
        String content = request.getContent();
        
        // 简单的聊天逻辑示例
        if (content.contains("你好") || content.contains("Hello")) {
            response.setContent("你好！我是智能旅行助手，请问有什么可以帮助您的？");
        } else if (content.contains("谢谢") || content.contains("Thanks")) {
            response.setContent("不客气，很高兴能帮到您！");
        } else if (content.contains("再见") || content.contains("Goodbye")) {
            response.setContent("再见！祝您旅途愉快！");
        } else {
            response.setContent("抱歉，我还在学习中，暂时无法回答这个问题。您可以尝试其他问题或使用我的专业功能。");
        }
        
        // 提取可能的意图并给出建议操作
        Map<String, Object> suggestedActions = new HashMap<>();
        if (content.contains("路线") || content.contains("导航")) {
            suggestedActions.put("routePlanning", "点击进行路线规划");
        } else if (content.contains("酒店") || content.contains("吃饭")) {
            suggestedActions.put("serviceRecommendation", "点击查看附近服务");
        }
        
        response.setSuggestedActions(suggestedActions);
    }

    /**
     * 保存用户上下文
     */
    private void saveUserContext(AssistantRequest request) {
        String userId = request.getUserId();
        if (userId != null && !userId.isEmpty()) {
            Map<String, Object> context = userContexts.computeIfAbsent(userId, k -> new HashMap<>());
            context.put("lastRequestTime", request.getTimestamp());
            context.put("lastRequestType", request.getRequestType());
            context.put("userPreferences", request.getUserPreferences());
            
            // 限制上下文大小，防止内存泄漏
            if (context.size() > 100) {
                // 简单的清理逻辑，可以根据实际需求优化
                context.clear();
            }
        }
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
}
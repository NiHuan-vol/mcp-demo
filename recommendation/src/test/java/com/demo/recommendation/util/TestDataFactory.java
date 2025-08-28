package com.demo.recommendation.util;

import com.demo.recommendation.mcp.assistant.AssistantRequest;
import com.demo.recommendation.mcp.assistant.AssistantResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.map.HashedMap;

/**
 * 测试数据工厂类，用于生成各类测试数据
 */
public class TestDataFactory {

    /**
     * 创建智能路线规划的请求对象
     * @param userId 用户ID
     * @return AssistantRequest对象
     */
    public static AssistantRequest createSmartRoutePlanningRequest(String userId) {
        AssistantRequest request = new AssistantRequest();
        request.setRequestId(generateRequestId());
        request.setUserId(userId);
        request.setRequestType("SMART_ROUTE_PLANNING");
        request.setContent("规划从北京到上海的自驾游路线");
        request.setTimestamp(System.currentTimeMillis());
        
        Map<String, Object> params = new HashMap<>();
        params.put("startLocation", "北京");
        params.put("endLocation", "上海");
        params.put("preferences", "风景优先");
        params.put("mapProvider", "amap");
        params.put("includeAttractions", true);
        params.put("includeRestaurants", true);
        params.put("includeHotels", true);
        params.put("avoidHighways", false);
        params.put("avoidTolls", false);
        request.setParameters(params);
        
        Map<String, Object> context = new HashMap<>();
        context.put("tripType", "自驾游");
        context.put("travelersCount", 2);
        context.put("budget", "中等");
        request.setContext(context);
        
        Map<String, Object> userPreferences = new HashMap<>();
        userPreferences.put("preferredCuisine", "川菜");
        userPreferences.put("preferredHotelType", "经济型");
        userPreferences.put("smokingAllowed", false);
        request.setUserPreferences(userPreferences);
        
        return request;
    }

    /**
     * 创建沿途服务推荐的请求对象
     * @param userId 用户ID
     * @return AssistantRequest对象
     */
    public static AssistantRequest createAlongRouteServiceRequest(String userId) {
        AssistantRequest request = new AssistantRequest();
        request.setRequestId(generateRequestId());
        request.setUserId(userId);
        request.setRequestType("ALONG_ROUTE_SERVICE_RECOMMENDATION");
        request.setContent("推荐路线沿途的加油站和餐厅");
        request.setTimestamp(System.currentTimeMillis());
        
        Map<String, Object> params = new HashMap<>();
        params.put("routeId", "test_route_001");
        params.put("serviceTypes", new String[]{"gasStation", "restaurant"});
        params.put("radius", 5.0);
        params.put("ratingThreshold", 4.5);
        request.setParameters(params);
        
        return request;
    }

    /**
     * 创建智能翻译的请求对象
     * @param userId 用户ID
     * @return AssistantRequest对象
     */
    public static AssistantRequest createTranslationRequest(String userId) {
        AssistantRequest request = new AssistantRequest();
        request.setRequestId(generateRequestId());
        request.setUserId(userId);
        request.setRequestType("INTELLIGENT_TRANSLATION");
        request.setContent("将英文翻译为中文");
    request.setTimestamp(System.currentTimeMillis());
        
        Map<String, Object> params = new HashMap<>();
        params.put("text", "The scenery along the highway is beautiful. We should stop to take some photos.");
        params.put("sourceLanguage", "en");
        params.put("targetLanguage", "zh");
        params.put("provider", "baidu");
        request.setParameters(params);
        
        return request;
    }

    /**
     * 创建社交与攻略推荐的请求对象
     * @param userId 用户ID
     * @return AssistantRequest对象
     */
    public static AssistantRequest createSocialGuideRequest(String userId) {
        AssistantRequest request = new AssistantRequest();
        request.setRequestId(generateRequestId());
        request.setUserId(userId);
        request.setRequestType("SOCIAL_AND_GUIDE_RECOMMENDATION");
        request.setContent("推荐目的地的网红景点和美食攻略");
       request.setTimestamp(System.currentTimeMillis());
        
        Map<String, Object> params = new HashMap<>();
        params.put("location", "上海");
        params.put("keywords", new String[]{"网红景点", "美食"});
        params.put("maxDuration", 3);
        params.put("popularityThreshold", 4.5);
        params.put("sourcePlatforms", new String[]{"xiaohongshu", "tripadvisor"});
        request.setParameters(params);
        
        return request;
    }

    /**
     * 创建紧急服务的请求对象
     * @param userId 用户ID
     * @return AssistantRequest对象
     */
    public static AssistantRequest createEmergencyRequest(String userId) {
        AssistantRequest request = new AssistantRequest();
        request.setRequestId(generateRequestId());
        request.setUserId(userId);
        request.setRequestType("EMERGENCY_SERVICE");
        request.setTimestamp(System.currentTimeMillis());
        request.setContent("车辆在路上爆胎，需要道路救援");
        
        Map<String, Object> params = new HashMap<>();
        params.put("location", "北京市朝阳区建国路88号");
        params.put("emergencyType", "爆胎");
        
        Map<String, Object> vehicleInfo = new HashMap<>();
        vehicleInfo.put("brand", "丰田");
        vehicleInfo.put("model", "卡罗拉");
        vehicleInfo.put("plateNumber", "京A12345");
        vehicleInfo.put("year", "2020");
        vehicleInfo.put("color", "白色");
        
        params.put("vehicleInfo", vehicleInfo);
        request.setParameters(params);
        
        return request;
    }

    /**
     * 创建通用聊天的请求对象
     * @param userId 用户ID
     * @param message 聊天消息
     * @return AssistantRequest对象
     */
    public static AssistantRequest createGeneralChatRequest(String userId, String message) {
        AssistantRequest request = new AssistantRequest();
        request.setRequestId(generateRequestId());
        request.setUserId(userId);
        request.setRequestType("GENERAL_CHAT");
        request.setContent(message);
        request.setTimestamp(System.currentTimeMillis());
        
        return request;
    }

    /**
     * 创建成功的响应对象
     * @param request 请求对象
     * @param data 响应数据
     * @return AssistantResponse对象
     */
    public static AssistantResponse createSuccessResponse(AssistantRequest request, Map<String, Object> data) {
        AssistantResponse response = new AssistantResponse();
        response.setResponseId(generateResponseId());
        response.setRequestId(request.getRequestId());
        response.setStatus("SUCCESS");
        response.setContent("操作成功");
        response.setData(data);
        response.setProcessingTime(123); // 模拟处理时间，毫秒
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("serviceVersion", "1.0.0");
        metadata.put("timestamp", LocalDateTime.now());
        response.setMetadata(metadata);
        
        return response;
    }

    /**
     * 创建失败的响应对象
     * @param request 请求对象
     * @param errorMessage 错误消息
     * @return AssistantResponse对象
     */
    public static AssistantResponse createErrorResponse(AssistantRequest request, String errorMessage) {
        AssistantResponse response = new AssistantResponse();
        response.setResponseId(generateResponseId());
        response.setRequestId(request.getRequestId());
        response.setStatus("ERROR");
        response.setErrorMessage(errorMessage);
        response.setProcessingTime(50); // 模拟处理时间，毫秒
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("serviceVersion", "1.0.0");
        metadata.put("timestamp", LocalDateTime.now());
        response.setMetadata(metadata);
        
        return response;
    }

    /**
     * 生成请求ID
     * @return 请求ID字符串
     */
    public static String generateRequestId() {
        return "req_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 生成响应ID
     * @return 响应ID字符串
     */
    public static String generateResponseId() {
        return "resp_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 生成模拟的地图路线规划结果
     * @param startLocation 起始位置
     * @param endLocation 目的地
     * @return 路线规划结果Map
     */
    public static Map<String, Object> createMockRoutePlanningResult(String startLocation, String endLocation) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "SUCCESS");
        result.put("startLocation", startLocation);
        result.put("endLocation", endLocation);
        result.put("distance", 1200.5);
        result.put("duration", 720);
        result.put("estimatedFuelConsumption", 85.5);
        result.put("tollFee", 450.0);
        result.put("routePoints", new String[]{"北京", "天津", "济南", "南京", "上海"});
        result.put("recommendedAttractions", createMockAttractions());
        result.put("recommendedRestaurants", createMockRestaurants());
        result.put("recommendedHotels", createMockHotels());
        
        return result;
    }

    /**
     * 创建模拟的景点数据
     * @return 景点数据数组
     */
    private static List<Map<String, Object>> createMockAttractions() {
        List<Map<String, Object>> attractions = new ArrayList<>();
        
        Map<String, Object> attraction1 = new HashMap<>();
        attraction1.put("id", "attraction_001");
        attraction1.put("name", "泰山风景区");
        attraction1.put("location", "山东省泰安市");
        attraction1.put("rating", 4.8);
        attraction1.put("description", "五岳之首，中国著名的旅游胜地");
        attractions.add(attraction1);
        
        Map<String, Object> attraction2 = new HashMap<>();
        attraction2.put("id", "attraction_002");
        attraction2.put("name", "南京夫子庙");
        attraction2.put("location", "江苏省南京市");
        attraction2.put("rating", 4.7);
        attraction2.put("description", "中国四大文庙之一，历史文化底蕴深厚");
        attractions.add(attraction2);
        
        Map<String, Object> attraction3 = new HashMap<>();
        attraction3.put("id", "attraction_003");
        attraction3.put("name", "上海外滩");
        attraction3.put("location", "上海市黄浦区");
        attraction3.put("rating", 4.9);
        attraction3.put("description", "上海的标志性景点，欣赏黄浦江两岸的美景");
        attractions.add(attraction3);
        
        return attractions;
    }

    /**
     * 创建模拟的餐厅数据
     * @return 餐厅数据数组
     */
    private static List<Map<String, Object>> createMockRestaurants() {
        List<Map<String, Object>> restaurants = new ArrayList<>();
        
        Map<String, Object> restaurant1 = new HashMap<>();
        restaurant1.put("id", "rest_001");
        restaurant1.put("name", "全聚德烤鸭店");
        restaurant1.put("location", "北京市东城区");
        restaurant1.put("rating", 4.8);
        restaurant1.put("cuisine", "北京烤鸭");
        restaurant1.put("priceLevel", "人均200元");
        restaurants.add(restaurant1);
        
        Map<String, Object> restaurant2 = new HashMap<>();
        restaurant2.put("id", "rest_002");
        restaurant2.put("name", "南京大牌档");
        restaurant2.put("location", "江苏省南京市");
        restaurant2.put("rating", 4.6);
        restaurant2.put("cuisine", "南京菜");
        restaurant2.put("priceLevel", "人均80元");
        restaurants.add(restaurant2);
        
        Map<String, Object> restaurant3 = new HashMap<>();
        restaurant3.put("id", "rest_003");
        restaurant3.put("name", "南翔小笼");
        restaurant3.put("location", "上海市黄浦区");
        restaurant3.put("rating", 4.7);
        restaurant3.put("cuisine", "上海小吃");
        restaurant3.put("priceLevel", "人均60元");
        restaurants.add(restaurant3);
        
        return restaurants;
    }

    /**
     * 创建模拟的酒店数据
     * @return 酒店数据数组
     */
    private static List<Map<String, Object>> createMockHotels() {
        List<Map<String, Object>> hotels = new ArrayList<>();
        
        Map<String, Object> hotel1 = new HashMap<>();
        hotel1.put("id", "hotel_001");
        hotel1.put("name", "希尔顿酒店");
        hotel1.put("location", "山东省济南市");
        hotel1.put("rating", 4.8);
        hotel1.put("pricePerNight", 780.0);
        hotel1.put("starRating", 5);
        hotels.add(hotel1);
        
        Map<String, Object> hotel2 = new HashMap<>();
        hotel2.put("id", "hotel_002");
        hotel2.put("name", "如家酒店");
        hotel2.put("location", "江苏省南京市");
        hotel2.put("rating", 4.2);
        hotel2.put("pricePerNight", 230.0);
        hotel2.put("starRating", 3);
        hotels.add(hotel2);
        
        Map<String, Object> hotel3 = new HashMap<>();
        hotel3.put("id", "hotel_003");
        hotel3.put("name", "上海外滩华尔道夫酒店");
        hotel3.put("location", "上海市黄浦区");
        hotel3.put("rating", 4.9);
        hotel3.put("pricePerNight", 1800.0);
        hotel3.put("starRating", 5);
        hotels.add(hotel3);
        
        return hotels;
    }
}
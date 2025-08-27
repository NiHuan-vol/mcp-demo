package com.demo.recommendation.mcp.gateway;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * 旅游攻略网关，统一封装社交平台和旅游攻略API
 */
@Component
public class TravelGuideGateway implements McpGateway {

    private static final String GATEWAY_ID = "travel-guide-gateway";
    private static final String GATEWAY_NAME = "旅游攻略网关";
    private boolean initialized = false;

    @Override
    public void initialize() {
        // 初始化旅游攻略服务
        // 实际项目中这里会加载配置、初始化各平台SDK等
        initialized = true;
        System.out.println("旅游攻略网关初始化完成");
    }

    @Override
    public void shutdown() {
        // 关闭服务连接
        initialized = false;
        System.out.println("旅游攻略网关已关闭");
    }

    @Override
    public boolean isAvailable() {
        return initialized;
    }

    @Override
    public String getGatewayId() {
        return GATEWAY_ID;
    }

    @Override
    public String getGatewayName() {
        return GATEWAY_NAME;
    }

    /**
     * 搜索旅游攻略
     * @param keyword 搜索关键词
     * @param location 位置
     * @param maxDuration 最大游玩时长（小时）
     * @param popularityThreshold 人气阈值
     * @return 攻略搜索结果
     */
    public Map<String, Object> searchTravelGuides(String keyword, String location, 
                                                int maxDuration, double popularityThreshold) {
        // 实际项目中这里会调用小红书、TripAdvisor、马蜂窝等API
        // 返回示例数据
        Map<String, Object> result = new HashMap<>();
        result.put("status", "SUCCESS");
        result.put("keyword", keyword);
        result.put("location", location);
        result.put("maxDuration", maxDuration);
        result.put("popularityThreshold", popularityThreshold);
        
        List<Map<String, Object>> guides = new ArrayList<>();
        
        // 示例：3小时以内的网红景点推荐
        Map<String, Object> guide1 = new HashMap<>();
        guide1.put("id", "guide_001");
        guide1.put("title", "【必去】城市中心的网红打卡地");
        guide1.put("source", "xiaohongshu");
        guide1.put("author", "旅行达人");
        guide1.put("duration", 2);
        guide1.put("popularity", 4.9);
        guide1.put("likes", 12580);
        guide1.put("comments", 2356);
        guide1.put("publishDate", LocalDateTime.now().minusDays(15));
        guide1.put("contentPreview", "这个位于城市中心的景点绝对是拍照胜地，推荐下午4点前往，阳光正好...");
        
        Map<String, Object> guide2 = new HashMap<>();
        guide2.put("id", "guide_002");
        guide2.put("title", "隐藏的美食天堂，本地人都爱去");
        guide2.put("source", "tripadvisor");
        guide2.put("author", "美食探险家");
        guide2.put("duration", 1.5);
        guide2.put("popularity", 4.7);
        guide2.put("likes", 8923);
        guide2.put("comments", 1890);
        guide2.put("publishDate", LocalDateTime.now().minusDays(23));
        guide2.put("contentPreview", "这条小巷子里藏着多家地道美食，人均消费不高，但味道绝对正宗...");
        
        guides.add(guide1);
        guides.add(guide2);
        
        result.put("guides", guides);
        result.put("totalCount", guides.size());
        
        return result;
    }

    /**
     * 获取攻略详情
     * @param guideId 攻略ID
     * @param source 来源平台
     * @return 攻略详情
     */
    public Map<String, Object> getGuideDetail(String guideId, String source) {
        // 实际项目中这里会调用相应平台的API获取详细信息
        // 返回示例数据
        Map<String, Object> result = new HashMap<>();
        result.put("status", "SUCCESS");
        result.put("guideId", guideId);
        result.put("source", source);
        
        // 示例详情数据
        Map<String, Object> detail = new HashMap<>();
        detail.put("id", guideId);
        detail.put("title", "【详细攻略】如何在3小时内玩转城市中心");
        detail.put("source", source);
        detail.put("author", "资深旅行家");
        detail.put("authorAvatar", "https://example.com/avatar.jpg");
        detail.put("content", "详细的攻略内容...\n1. 首先前往...\n2. 然后参观...\n3. 最后在...用餐");
        detail.put("images", new String[]{"https://example.com/image1.jpg", "https://example.com/image2.jpg"});
        detail.put("location", "城市中心");
        detail.put("duration", 3);
        detail.put("popularity", 4.8);
        detail.put("likes", 25890);
        detail.put("comments", 3256);
        detail.put("collects", 18900);
        detail.put("publishDate", LocalDateTime.now().minusDays(30));
        
        result.put("detail", detail);
        
        return result;
    }

    /**
     * 推荐网红景点
     * @param location 位置
     * @param radius 搜索半径（公里）
     * @param limit 推荐数量
     * @return 网红景点推荐
     */
    public Map<String, Object> recommendPopularAttractions(String location, 
                                                        double radius, int limit) {
        // 实际项目中这里会调用各平台API推荐网红景点
        // 返回示例数据
        Map<String, Object> result = new HashMap<>();
        result.put("status", "SUCCESS");
        result.put("location", location);
        result.put("radius", radius);
        
        List<Map<String, Object>> attractions = new ArrayList<>();
        
        // 示例网红景点数据
        for (int i = 0; i < limit; i++) {
            Map<String, Object> attraction = new HashMap<>();
            attraction.put("id", "attraction_" + i);
            attraction.put("name", "网红景点 " + (i + 1));
            attraction.put("description", "这是一个热门的网红打卡景点，吸引了大量游客前来拍照。");
            attraction.put("location", location);
            attraction.put("distance", 1.2 + i * 0.5);
            attraction.put("popularity", 4.8 - i * 0.1);
            attraction.put("visitCount", 10000 + i * 5000);
            attraction.put("imageUrl", "https://example.com/attraction" + i + ".jpg");
            attraction.put("openingHours", "09:00-18:00");
            
            attractions.add(attraction);
        }
        
        result.put("attractions", attractions);
        result.put("totalCount", attractions.size());
        
        return result;
    }
}
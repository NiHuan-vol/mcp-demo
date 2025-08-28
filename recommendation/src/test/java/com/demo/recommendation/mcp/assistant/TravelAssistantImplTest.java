package com.demo.recommendation.mcp.assistant;

import com.demo.recommendation.mcp.gateway.*;

import reactor.core.publisher.Mono;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TravelAssistantImplTest {

    @Mock
    private MapServiceGateway mapServiceGateway;

    @Mock
    private WeatherServiceGateway weatherServiceGateway;

    @Mock
    private ServiceRecommendationGateway serviceRecommendationGateway;

    @Mock
    private TranslationServiceGateway translationServiceGateway;

    @Mock
    private TravelGuideGateway travelGuideGateway;

    @Mock
    private EmergencyServiceGateway emergencyServiceGateway;

    @InjectMocks
    private TravelAssistantImpl travelAssistant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 模拟网关初始化
        when(mapServiceGateway.isAvailable()).thenReturn(true);
        when(weatherServiceGateway.isAvailable()).thenReturn(true);
        when(serviceRecommendationGateway.isAvailable()).thenReturn(true);
        when(translationServiceGateway.isAvailable()).thenReturn(true);
        when(travelGuideGateway.isAvailable()).thenReturn(true);
        when(emergencyServiceGateway.isAvailable()).thenReturn(true);
    }

    @Test
    void testProcessRequest_smartRoutePlanning() {
        // 准备测试数据
        AssistantRequest request = new AssistantRequest();
        request.setRequestId("req_001");
        request.setUserId("user_001");
        request.setRequestType("SMART_ROUTE_PLANNING");
        request.setContent("规划从北京到上海的路线");
        
        Map<String, Object> params = new HashMap<>();
        params.put("startLocation", "北京");
        params.put("endLocation", "上海");
        params.put("preferences", "风景优先");
        params.put("mapProvider", "amap");
        request.setParameters(params);
        
        // 模拟地图服务返回
        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put("status", "SUCCESS");
        mapResult.put("distance", 1200.5);
        mapResult.put("duration", 720);
        when(mapServiceGateway.planRoute("北京", "上海", "风景优先", "amap")).thenReturn(mapResult);
        
        // 模拟天气服务返回
        Map<String, Object> weatherResult = new HashMap<>();
        weatherResult.put("status", "SUCCESS");
        weatherResult.put("currentWeather", "sunny");
        when(weatherServiceGateway.getWeatherInfo(anyString())).thenReturn(weatherResult);
        
        // 执行测试
        Mono<AssistantResponse> response = travelAssistant.processRequest(request);
        
        // 验证结果
        assertNotNull(response);
        assertEquals("req_001", response.block().getRequestId());
        assertEquals("SUCCESS", response.block().getStatus());
        assertNotNull(response.block().getData());
    }

    @Test
    void testProcessRequest_alongRouteServiceRecommendation() {
        // 准备测试数据
        AssistantRequest request = new AssistantRequest();
        request.setRequestId("req_002");
        request.setUserId("user_001");
        request.setRequestType("ALONG_ROUTE_SERVICE_RECOMMENDATION");
        request.setContent("推荐沿途加油站");
        
        Map<String, Object> params = new HashMap<>();
        params.put("routeId", "route_001");
        params.put("serviceType", "gasStation");
        params.put("radius", 5.0);
        request.setParameters(params);
        
        // 模拟服务推荐返回
        Map<String, Object> serviceResult = new HashMap<>();
        serviceResult.put("status", "SUCCESS");
        serviceResult.put("serviceType", "gasStation");
        when(serviceRecommendationGateway.searchNearbyServices(anyString(), anyString(), anyDouble(), anyDouble()))
                .thenReturn(serviceResult);
        
        // 执行测试
        Mono<AssistantResponse> response = travelAssistant.processRequest(request);
        
        // 验证结果
        assertNotNull(response);
        assertEquals("req_002", response.block().getRequestId());
        assertEquals("SUCCESS", response.block().getStatus());
    }

    @Test
    void testProcessRequest_intelligentTranslation() {
        // 准备测试数据
        AssistantRequest request = new AssistantRequest();
        request.setRequestId("req_003");
        request.setUserId("user_001");
        request.setRequestType("INTELLIGENT_TRANSLATION");
        request.setContent("翻译这段文字");
        
        Map<String, Object> params = new HashMap<>();
        params.put("text", "Hello, world!");
        params.put("sourceLanguage", "en");
        params.put("targetLanguage", "zh");
        params.put("provider", "baidu");
        request.setParameters(params);
        
        // 模拟翻译服务返回
        Map<String, Object> translateResult = new HashMap<>();
        translateResult.put("status", "SUCCESS");
        translateResult.put("translatedText", "你好，世界！");
        when(translationServiceGateway.translateText("Hello, world!", "en", "zh", "baidu"))
                .thenReturn(translateResult);
        
        // 执行测试
        Mono<AssistantResponse> response = travelAssistant.processRequest(request);
        
        // 验证结果
        assertNotNull(response);
        assertEquals("req_003", response.block().getRequestId());
        assertEquals("SUCCESS", response.block().getStatus());
    }

    @Test
    void testProcessRequest_emergencyService() {
        // 准备测试数据
        AssistantRequest request = new AssistantRequest();
        request.setRequestId("req_004");
        request.setUserId("user_001");
        request.setRequestType("EMERGENCY_SERVICE");
        request.setContent("请求道路救援");
        
        Map<String, Object> params = new HashMap<>();
        params.put("location", "北京市朝阳区建国路88号");
        params.put("emergencyType", "爆胎");
        Map<String, Object> vehicleInfo = new HashMap<>();
        vehicleInfo.put("brand", "丰田");
        vehicleInfo.put("model", "卡罗拉");
        params.put("vehicleInfo", vehicleInfo);
        request.setParameters(params);
        
        // 模拟紧急服务返回
        Map<String, Object> emergencyResult = new HashMap<>();
        emergencyResult.put("status", "SUCCESS");
        when(emergencyServiceGateway.requestRoadsideAssistance(anyString(), anyMap(), anyString()))
                .thenReturn(emergencyResult);
        
        // 执行测试
        Mono<AssistantResponse> response = travelAssistant.processRequest(request);
        
        // 验证结果
        assertNotNull(response);
        assertEquals("req_004", response.block().getRequestId());
        assertEquals("SUCCESS", response.block().getStatus());
    }

    @Test
    void testProcessRequest_invalidRequestType() {
        // 准备测试数据 - 无效的请求类型
        AssistantRequest request = new AssistantRequest();
        request.setRequestId("req_005");
        request.setUserId("user_001");
        request.setRequestType("INVALID_TYPE");
        request.setContent("无效的请求类型");
        
        // 执行测试
        Mono<AssistantResponse> response = travelAssistant.processRequest(request);
        
        // 验证结果
        assertNotNull(response);
        assertEquals("req_005", response.block().getRequestId());
        assertEquals("ERROR", response.block().getStatus());
        assertNotNull(response.block().getErrorMessage());
    }
}
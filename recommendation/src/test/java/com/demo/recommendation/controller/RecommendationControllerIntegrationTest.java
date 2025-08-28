package com.demo.recommendation.controller;

import com.demo.recommendation.mcp.assistant.AssistantRequest;
import com.demo.recommendation.mcp.assistant.AssistantResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecommendationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders headers = new HttpHeaders();

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/recommendation" + uri;
    }

    @Test
    void testSmartRoutePlanning() {
        // 准备测试数据
        AssistantRequest request = new AssistantRequest();
        request.setRequestId("integration_test_001");
        request.setUserId("test_user_001");
        request.setRequestType("SMART_ROUTE_PLANNING");
        request.setContent("规划从北京到上海的自驾游路线");
        
        Map<String, Object> params = new HashMap<>();
        params.put("startLocation", "北京");
        params.put("endLocation", "上海");
        params.put("preferences", "风景优先");
        params.put("mapProvider", "amap");
        params.put("includeAttractions", true);
        params.put("includeRestaurants", true);
        params.put("includeHotels", true);
        request.setParameters(params);
        
        HttpEntity<AssistantRequest> entity = new HttpEntity<>(request, headers);
        
        // 发送请求
        ResponseEntity<AssistantResponse> responseEntity = restTemplate.exchange(
                createURLWithPort("/v1/assistant/smart-route-planning"),
                HttpMethod.POST,
                entity,
                AssistantResponse.class);
        
        // 验证响应
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode());
        
        AssistantResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("integration_test_001", response.getRequestId());
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getData());
    }

    @Test
    void testAlongRouteServiceRecommendation() {
        // 准备测试数据
        AssistantRequest request = new AssistantRequest();
        request.setRequestId("integration_test_002");
        request.setUserId("test_user_001");
        request.setRequestType("ALONG_ROUTE_SERVICE_RECOMMENDATION");
        request.setContent("推荐北京到上海路线沿途的加油站和餐厅");
        
        Map<String, Object> params = new HashMap<>();
        params.put("routeId", "test_route_001");
        params.put("serviceTypes", new String[]{"gasStation", "restaurant"});
        params.put("radius", 5.0);
        params.put("ratingThreshold", 4.5);
        request.setParameters(params);
        
        HttpEntity<AssistantRequest> entity = new HttpEntity<>(request, headers);
        
        // 发送请求
        ResponseEntity<AssistantResponse> responseEntity = restTemplate.exchange(
                createURLWithPort("/v1/assistant/along-route-services"),
                HttpMethod.POST,
                entity,
                AssistantResponse.class);
        
        // 验证响应
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode());
        
        AssistantResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("integration_test_002", response.getRequestId());
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getData());
    }

    @Test
    void testIntelligentTranslation() {
        // 准备测试数据
        AssistantRequest request = new AssistantRequest();
        request.setRequestId("integration_test_003");
        request.setUserId("test_user_001");
        request.setRequestType("INTELLIGENT_TRANSLATION");
        request.setContent("将这段英文翻译为中文");
        
        Map<String, Object> params = new HashMap<>();
        params.put("text", "The scenery along the highway is beautiful. We should stop to take some photos.");
        params.put("sourceLanguage", "en");
        params.put("targetLanguage", "zh");
        params.put("provider", "baidu");
        request.setParameters(params);
        
        HttpEntity<AssistantRequest> entity = new HttpEntity<>(request, headers);
        
        // 发送请求
        ResponseEntity<AssistantResponse> responseEntity = restTemplate.exchange(
                createURLWithPort("/v1/assistant/translation"),
                HttpMethod.POST,
                entity,
                AssistantResponse.class);
        
        // 验证响应
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode());
        
        AssistantResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("integration_test_003", response.getRequestId());
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getData());
    }

    @Test
    void testSocialAndGuideRecommendation() {
        // 准备测试数据
        AssistantRequest request = new AssistantRequest();
        request.setRequestId("integration_test_004");
        request.setUserId("test_user_001");
        request.setRequestType("SOCIAL_AND_GUIDE_RECOMMENDATION");
        request.setContent("推荐上海的网红景点和美食攻略");
        
        Map<String, Object> params = new HashMap<>();
        params.put("location", "上海");
        params.put("keywords", new String[]{"网红景点", "美食"});
        params.put("maxDuration", 3);
        params.put("popularityThreshold", 4.5);
        request.setParameters(params);
        
        HttpEntity<AssistantRequest> entity = new HttpEntity<>(request, headers);
        
        // 发送请求
        ResponseEntity<AssistantResponse> responseEntity = restTemplate.exchange(
                createURLWithPort("/v1/assistant/social-guides"),
                HttpMethod.POST,
                entity,
                AssistantResponse.class);
        
        // 验证响应
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode());
        
        AssistantResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("integration_test_004", response.getRequestId());
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getData());
    }

    @Test
    void testEmergencyService() {
        // 准备测试数据
        AssistantRequest request = new AssistantRequest();
        request.setRequestId("integration_test_005");
        request.setUserId("test_user_001");
        request.setRequestType("EMERGENCY_SERVICE");
        request.setContent("车辆在路上爆胎，需要道路救援");
        
        Map<String, Object> params = new HashMap<>();
        params.put("location", "北京市朝阳区建国路88号");
        params.put("emergencyType", "爆胎");
        Map<String, Object> vehicleInfo = new HashMap<>();
        vehicleInfo.put("brand", "丰田");
        vehicleInfo.put("model", "卡罗拉");
        vehicleInfo.put("plateNumber", "京A12345");
        params.put("vehicleInfo", vehicleInfo);
        request.setParameters(params);
        
        HttpEntity<AssistantRequest> entity = new HttpEntity<>(request, headers);
        
        // 发送请求
        ResponseEntity<AssistantResponse> responseEntity = restTemplate.exchange(
                createURLWithPort("/v1/assistant/emergency"),
                HttpMethod.POST,
                entity,
                AssistantResponse.class);
        
        // 验证响应
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode());
        
        AssistantResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("integration_test_005", response.getRequestId());
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getData());
    }
}
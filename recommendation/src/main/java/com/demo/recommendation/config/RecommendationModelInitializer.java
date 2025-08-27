package com.demo.recommendation.config;

import com.demo.core.registry.ModelInfo;
import com.demo.core.registry.ModelRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 推荐模块模型初始化器，负责在应用启动时注册相关AI模型
 */
@Component
public class RecommendationModelInitializer implements ApplicationRunner {

    @Autowired
    private ModelRegistry modelRegistry;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 注册智能路线规划模型
        registerRoutePlanningModel();
        
        // 注册沿途服务推荐模型
        registerServiceRecommendationModel();
        
        // 注册智能翻译模型
        registerTranslationModel();
        
        // 注册社交与攻略推荐模型
        registerTravelGuideModel();
        
        // 注册紧急服务模型
        registerEmergencyServiceModel();
    }

    /**
     * 注册智能路线规划模型
     */
    private void registerRoutePlanningModel() {
        Map<String, Object> config = new HashMap<>();
        config.put("mapProvider", "amap,google");
        config.put("weatherIntegrationEnabled", true);
        config.put("trafficIntegrationEnabled", true);
        config.put("premiumFeaturesEnabled", true);
        
        ModelInfo routeModel = ModelInfo.builder()
                .modelId("recommendation-route-planning-001")
                .modelName("智能路线规划模型")
                .description("结合地图API、天气API，提供个性化路线规划")
                .modelType("route")
                .provider("demo-provider")
                .version("1.0")
                .registeredAt(LocalDateTime.now())
                .configuration(config)
                .enabled(true)
                .build();
        
        modelRegistry.registerModel(routeModel.getModelId(), routeModel);
    }

    /**
     * 注册沿途服务推荐模型
     */
    private void registerServiceRecommendationModel() {
        Map<String, Object> config = new HashMap<>();
        config.put("serviceTypes", "restaurant,hotel,gasStation,chargingStation");
        config.put("ratingThreshold", 4.0);
        config.put("distanceRadius", 5.0); // 公里
        
        ModelInfo serviceModel = ModelInfo.builder()
                .modelId("recommendation-service-001")
                .modelName("沿途服务推荐模型")
                .description("推荐沿途餐饮、酒店、加油站、充电桩等服务")
                .modelType("service")
                .provider("demo-provider")
                .version("1.0")
                .registeredAt(LocalDateTime.now())
                .configuration(config)
                .enabled(true)
                .build();
        
        modelRegistry.registerModel(serviceModel.getModelId(), serviceModel);
    }

    /**
     * 注册智能翻译模型
     */
    private void registerTranslationModel() {
        Map<String, Object> config = new HashMap<>();
        config.put("translationProviders", "baidu,deepl,chatgpt");
        config.put("voiceRecognitionEnabled", true);
        config.put("voiceSynthesisEnabled", true);
        
        ModelInfo translationModel = ModelInfo.builder()
                .modelId("recommendation-translation-001")
                .modelName("智能翻译模型")
                .description("提供文本翻译和语音助手功能")
                .modelType("translation")
                .provider("demo-provider")
                .version("1.0")
                .registeredAt(LocalDateTime.now())
                .configuration(config)
                .enabled(true)
                .build();
        
        modelRegistry.registerModel(translationModel.getModelId(), translationModel);
    }

    /**
     * 注册社交与攻略推荐模型
     */
    private void registerTravelGuideModel() {
        Map<String, Object> config = new HashMap<>();
        config.put("contentSources", "xiaohongshu,tripadvisor,mafengwo");
        config.put("maxDuration", 3); // 小时
        config.put("popularityThreshold", 4.5);
        
        ModelInfo guideModel = ModelInfo.builder()
                .modelId("recommendation-travel-guide-001")
                .modelName("社交与攻略推荐模型")
                .description("推荐网红景点、游记攻略等内容")
                .modelType("guide")
                .provider("demo-provider")
                .version("1.0")
                .registeredAt(LocalDateTime.now())
                .configuration(config)
                .enabled(true)
                .build();
        
        modelRegistry.registerModel(guideModel.getModelId(), guideModel);
    }

    /**
     * 注册紧急服务模型
     */
    private void registerEmergencyServiceModel() {
        Map<String, Object> config = new HashMap<>();
        config.put("emergencyTypes", "roadsideAssistance,insurance");
        config.put("responseTimeTarget", 30); // 分钟
        config.put("serviceProviderIntegrationEnabled", true);
        
        ModelInfo emergencyModel = ModelInfo.builder()
                .modelId("recommendation-emergency-001")
                .modelName("紧急服务模型")
                .description("提供道路救援、保险服务等紧急支持")
                .modelType("emergency")
                .provider("demo-provider")
                .version("1.0")
                .registeredAt(LocalDateTime.now())
                .configuration(config)
                .enabled(true)
                .build();
        
        modelRegistry.registerModel(emergencyModel.getModelId(), emergencyModel);
    }
}
package com.demo.recommendation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 自驾游推荐服务的核心配置类
 * 包含模型配置、网关服务配置和商业化服务配置
 */
@Configuration
public class RecommendationConfig {
    
    // ============ 模型配置 ============    
    @Value("${recommendation.model.route-planning.id:recommendation-route-planning-001}")
    private String routePlanningModelId;
    
    @Value("${recommendation.model.route-planning.name:智能路线规划模型}")
    private String routePlanningModelName;
    
    @Value("${recommendation.model.service.id:recommendation-service-001}")
    private String serviceModelId;
    
    @Value("${recommendation.model.service.name:沿途服务推荐模型}")
    private String serviceModelName;
    
    @Value("${recommendation.model.translation.id:recommendation-translation-001}")
    private String translationModelId;
    
    @Value("${recommendation.model.translation.name:智能翻译模型}")
    private String translationModelName;
    
    @Value("${recommendation.model.travel-guide.id:recommendation-travel-guide-001}")
    private String travelGuideModelId;
    
    @Value("${recommendation.model.travel-guide.name:社交与攻略推荐模型}")
    private String travelGuideModelName;
    
    @Value("${recommendation.model.emergency.id:recommendation-emergency-001}")
    private String emergencyModelId;
    
    @Value("${recommendation.model.emergency.name:紧急服务模型}")
    private String emergencyModelName;
    
    // ============ 地图服务配置 ============    
    @Value("${recommendation.map.amap.api-key:}")
    private String amapApiKey;
    
    @Value("${recommendation.map.google.api-key:}")
    private String googleMapApiKey;
    
    @Value("${recommendation.map.timeout:5000}")
    private int mapServiceTimeout;
    
    // ============ 天气服务配置 ============    
    @Value("${recommendation.weather.api-key:}")
    private String weatherApiKey;
    
    @Value("${recommendation.weather.provider:openweathermap}")
    private String weatherProvider;
    
    @Value("${recommendation.weather.timeout:3000}")
    private int weatherServiceTimeout;
    
    // ============ 服务推荐配置 ============    
    @Value("${recommendation.service.dianping.api-key:}")
    private String dianpingApiKey;
    
    @Value("${recommendation.service.google-places.api-key:}")
    private String googlePlacesApiKey;
    
    @Value("${recommendation.service.timeout:5000}")
    private int serviceRecommendationTimeout;
    
    // ============ 翻译服务配置 ============    
    @Value("${recommendation.translation.baidu.api-key:}")
    private String baiduTranslationApiKey;
    
    @Value("${recommendation.translation.deepl.api-key:}")
    private String deeplApiKey;
    
    @Value("${recommendation.translation.timeout:3000}")
    private int translationServiceTimeout;
    
    // ============ 攻略服务配置 ============    
    @Value("${recommendation.guide.xiaohongshu.api-key:}")
    private String xiaohongshuApiKey;
    
    @Value("${recommendation.guide.tripadvisor.api-key:}")
    private String tripadvisorApiKey;
    
    @Value("${recommendation.guide.mafengwo.api-key:}")
    private String mafengwoApiKey;
    
    @Value("${recommendation.guide.timeout:5000}")
    private int travelGuideTimeout;
    
    // ============ 紧急服务配置 ============    
    @Value("${recommendation.emergency.roadside.api-key:}")
    private String roadsideAssistanceApiKey;
    
    @Value("${recommendation.emergency.insurance.api-key:}")
    private String insuranceApiKey;
    
    @Value("${recommendation.emergency.timeout:5000}")
    private int emergencyServiceTimeout;
    
    // ============ 商业化配置 ============    
    @Value("${recommendation.commercial.subscription.monthly-price:19.00}")
    private double monthlySubscriptionPrice;
    
    @Value("${recommendation.commercial.subscription.yearly-price:99.00}")
    private double yearlySubscriptionPrice;
    
    @Value("${recommendation.commercial.cps.restaurant-rate:0.10}")
    private double restaurantCpsRate;
    
    @Value("${recommendation.commercial.cps.hotel-rate:0.15}")
    private double hotelCpsRate;
    
    @Value("${recommendation.commercial.cps.gas-station-rate:0.05}")
    private double gasStationCpsRate;
    
    @Value("${recommendation.commercial.cps.charging-station-rate:0.03}")
    private double chargingStationCpsRate;
    
    // ============ 构造方法 ============    
    public RecommendationConfig() {
    }
    
    // ============ Getter 方法 ============    
    public String getRoutePlanningModelId() {
        return routePlanningModelId;
    }
    
    public void setRoutePlanningModelId(String routePlanningModelId) {
        this.routePlanningModelId = routePlanningModelId;
    }
    
    public String getRoutePlanningModelName() {
        return routePlanningModelName;
    }
    
    public void setRoutePlanningModelName(String routePlanningModelName) {
        this.routePlanningModelName = routePlanningModelName;
    }
    
    public String getServiceModelId() {
        return serviceModelId;
    }
    
    public void setServiceModelId(String serviceModelId) {
        this.serviceModelId = serviceModelId;
    }
    
    public String getServiceModelName() {
        return serviceModelName;
    }
    
    public void setServiceModelName(String serviceModelName) {
        this.serviceModelName = serviceModelName;
    }
    
    public String getTranslationModelId() {
        return translationModelId;
    }
    
    public void setTranslationModelId(String translationModelId) {
        this.translationModelId = translationModelId;
    }
    
    public String getTranslationModelName() {
        return translationModelName;
    }
    
    public void setTranslationModelName(String translationModelName) {
        this.translationModelName = translationModelName;
    }
    
    public String getTravelGuideModelId() {
        return travelGuideModelId;
    }
    
    public void setTravelGuideModelId(String travelGuideModelId) {
        this.travelGuideModelId = travelGuideModelId;
    }
    
    public String getTravelGuideModelName() {
        return travelGuideModelName;
    }
    
    public void setTravelGuideModelName(String travelGuideModelName) {
        this.travelGuideModelName = travelGuideModelName;
    }
    
    public String getEmergencyModelId() {
        return emergencyModelId;
    }
    
    public void setEmergencyModelId(String emergencyModelId) {
        this.emergencyModelId = emergencyModelId;
    }
    
    public String getEmergencyModelName() {
        return emergencyModelName;
    }
    
    public void setEmergencyModelName(String emergencyModelName) {
        this.emergencyModelName = emergencyModelName;
    }
    
    public String getAmapApiKey() {
        return amapApiKey;
    }
    
    public void setAmapApiKey(String amapApiKey) {
        this.amapApiKey = amapApiKey;
    }
    
    public String getGoogleMapApiKey() {
        return googleMapApiKey;
    }
    
    public void setGoogleMapApiKey(String googleMapApiKey) {
        this.googleMapApiKey = googleMapApiKey;
    }
    
    public int getMapServiceTimeout() {
        return mapServiceTimeout;
    }
    
    public void setMapServiceTimeout(int mapServiceTimeout) {
        this.mapServiceTimeout = mapServiceTimeout;
    }
    
    public String getWeatherApiKey() {
        return weatherApiKey;
    }
    
    public void setWeatherApiKey(String weatherApiKey) {
        this.weatherApiKey = weatherApiKey;
    }
    
    public String getWeatherProvider() {
        return weatherProvider;
    }
    
    public void setWeatherProvider(String weatherProvider) {
        this.weatherProvider = weatherProvider;
    }
    
    public int getWeatherServiceTimeout() {
        return weatherServiceTimeout;
    }
    
    public void setWeatherServiceTimeout(int weatherServiceTimeout) {
        this.weatherServiceTimeout = weatherServiceTimeout;
    }
    
    public String getDianpingApiKey() {
        return dianpingApiKey;
    }
    
    public void setDianpingApiKey(String dianpingApiKey) {
        this.dianpingApiKey = dianpingApiKey;
    }
    
    public String getGooglePlacesApiKey() {
        return googlePlacesApiKey;
    }
    
    public void setGooglePlacesApiKey(String googlePlacesApiKey) {
        this.googlePlacesApiKey = googlePlacesApiKey;
    }
    
    public int getServiceRecommendationTimeout() {
        return serviceRecommendationTimeout;
    }
    
    public void setServiceRecommendationTimeout(int serviceRecommendationTimeout) {
        this.serviceRecommendationTimeout = serviceRecommendationTimeout;
    }
    
    public String getBaiduTranslationApiKey() {
        return baiduTranslationApiKey;
    }
    
    public void setBaiduTranslationApiKey(String baiduTranslationApiKey) {
        this.baiduTranslationApiKey = baiduTranslationApiKey;
    }
    
    public String getDeeplApiKey() {
        return deeplApiKey;
    }
    
    public void setDeeplApiKey(String deeplApiKey) {
        this.deeplApiKey = deeplApiKey;
    }
    
    public int getTranslationServiceTimeout() {
        return translationServiceTimeout;
    }
    
    public void setTranslationServiceTimeout(int translationServiceTimeout) {
        this.translationServiceTimeout = translationServiceTimeout;
    }
    
    public String getXiaohongshuApiKey() {
        return xiaohongshuApiKey;
    }
    
    public void setXiaohongshuApiKey(String xiaohongshuApiKey) {
        this.xiaohongshuApiKey = xiaohongshuApiKey;
    }
    
    public String getTripadvisorApiKey() {
        return tripadvisorApiKey;
    }
    
    public void setTripadvisorApiKey(String tripadvisorApiKey) {
        this.tripadvisorApiKey = tripadvisorApiKey;
    }
    
    public String getMafengwoApiKey() {
        return mafengwoApiKey;
    }
    
    public void setMafengwoApiKey(String mafengwoApiKey) {
        this.mafengwoApiKey = mafengwoApiKey;
    }
    
    public int getTravelGuideTimeout() {
        return travelGuideTimeout;
    }
    
    public void setTravelGuideTimeout(int travelGuideTimeout) {
        this.travelGuideTimeout = travelGuideTimeout;
    }
    
    public String getRoadsideAssistanceApiKey() {
        return roadsideAssistanceApiKey;
    }
    
    public void setRoadsideAssistanceApiKey(String roadsideAssistanceApiKey) {
        this.roadsideAssistanceApiKey = roadsideAssistanceApiKey;
    }
    
    public String getInsuranceApiKey() {
        return insuranceApiKey;
    }
    
    public void setInsuranceApiKey(String insuranceApiKey) {
        this.insuranceApiKey = insuranceApiKey;
    }
    
    public int getEmergencyServiceTimeout() {
        return emergencyServiceTimeout;
    }
    
    public void setEmergencyServiceTimeout(int emergencyServiceTimeout) {
        this.emergencyServiceTimeout = emergencyServiceTimeout;
    }
    
    public double getMonthlySubscriptionPrice() {
        return monthlySubscriptionPrice;
    }
    
    public void setMonthlySubscriptionPrice(double monthlySubscriptionPrice) {
        this.monthlySubscriptionPrice = monthlySubscriptionPrice;
    }
    
    public double getYearlySubscriptionPrice() {
        return yearlySubscriptionPrice;
    }
    
    public void setYearlySubscriptionPrice(double yearlySubscriptionPrice) {
        this.yearlySubscriptionPrice = yearlySubscriptionPrice;
    }
    
    public double getRestaurantCpsRate() {
        return restaurantCpsRate;
    }
    
    public void setRestaurantCpsRate(double restaurantCpsRate) {
        this.restaurantCpsRate = restaurantCpsRate;
    }
    
    public double getHotelCpsRate() {
        return hotelCpsRate;
    }
    
    public void setHotelCpsRate(double hotelCpsRate) {
        this.hotelCpsRate = hotelCpsRate;
    }
    
    public double getGasStationCpsRate() {
        return gasStationCpsRate;
    }
    
    public void setGasStationCpsRate(double gasStationCpsRate) {
        this.gasStationCpsRate = gasStationCpsRate;
    }
    
    public double getChargingStationCpsRate() {
        return chargingStationCpsRate;
    }
    
    public void setChargingStationCpsRate(double chargingStationCpsRate) {
        this.chargingStationCpsRate = chargingStationCpsRate;
    }
}
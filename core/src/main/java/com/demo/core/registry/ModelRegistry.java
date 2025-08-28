package com.demo.core.registry;

import com.demo.core.exception.LicenseException;
import com.demo.core.license.LicenseInfo;
import com.demo.core.license.LicenseManager;
import com.demo.core.model.AIRequest;
import com.demo.core.model.AIResponse;
import com.demo.core.vectorstore.service.VectorStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI模型注册中心，支持动态注册和注销AI模型
 */
@Component
public class ModelRegistry {
    
    @Autowired
    private LicenseManager licenseManager;
    
    @Autowired
    private VectorStoreService vectorStoreService;
    
    // 使用ConcurrentHashMap保证线程安全
    private final Map<String, ModelInfo> modelRegistry = new ConcurrentHashMap<>();
    
    /**
     * 注册AI模型
     * @param modelId 模型唯一标识
     * @param modelInfo 模型信息
     */
    public void registerModel(String modelId, ModelInfo modelInfo) {
        modelRegistry.put(modelId, modelInfo);
    }
    
    /**
     * 注销AI模型
     * @param modelId 模型唯一标识
     * @return 是否成功注销
     */
    public boolean unregisterModel(String modelId) {
        return modelRegistry.remove(modelId) != null;
    }
    
    /**
     * 获取模型信息
     * @param modelId 模型唯一标识
     * @return 模型信息，如果不存在则返回空
     */
    public Optional<ModelInfo> getModelInfo(String modelId) {
        return Optional.ofNullable(modelRegistry.get(modelId));
    }
    
    /**
     * 使用License验证获取模型信息
     * @param tenantId 租户ID
     * @param moduleId 模块ID
     * @param modelId 模型唯一标识
     * @return 模型信息
     * @throws LicenseException 如果License验证失败
     */
    public ModelInfo getModelWithLicenseVerification(String tenantId, String moduleId, String modelId) {
        // 验证License
        LicenseManager.LicenseValidationResult validationResult = licenseManager.validateLicense(tenantId, moduleId);
        
        if (!validationResult.isValid()) {
            throw new LicenseException(validationResult.getMessage(), tenantId, moduleId);
        }
        
        // 减少剩余调用次数
        boolean decrementResult = licenseManager.decrementRemainingCalls(tenantId, moduleId);
        if (!decrementResult) {
            throw new LicenseException("License调用次数减少失败", tenantId, moduleId);
        }
        
        // 获取模型信息
        return modelRegistry.get(modelId);
    }
    
    /**
     * 处理AI请求
     * @param request AI请求对象
     * @return AI响应对象
     */
    public AIResponse processRequest(AIRequest request) {
        AIResponse response = new AIResponse();
        response.setResponseId(UUID.randomUUID().toString());
        response.setModelId(request.getModelId());
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 从请求中获取必要的参数
            String modelId = request.getModelId();
            String tenantId = request.getTenantId();
            String moduleId = request.getModuleId();
            
            // 如果没有提供租户ID和模块ID，使用默认值
            if (tenantId == null || tenantId.isEmpty()) {
                tenantId = "default-tenant";
            }
            if (moduleId == null || moduleId.isEmpty()) {
                moduleId = "default-module";
            }
            
            // 进行License验证并获取模型信息
            ModelInfo modelInfo = getModelWithLicenseVerification(tenantId, moduleId, modelId);
            
            if (modelInfo == null) {
                throw new LicenseException("模型不存在：" + modelId, tenantId, moduleId);
            }
            
            // 根据模型类型处理请求
            if ("vector".equals(modelInfo.getModelType())) {
                // 处理向量搜索请求
                handleVectorSearchRequest(request, response, modelInfo);
            } else {
                // 处理其他类型的请求
                response.setData("处理完成：" + modelInfo.getModelName());
                response.setStatus("SUCCESS");
            }
            
            // 添加License信息到响应元数据
            Map<String, Object> metadata = response.getMetadata();
            metadata.put("tenantId", tenantId);
            metadata.put("moduleId", moduleId);
            
            // 获取License信息
            Optional<LicenseInfo> licenseInfoOpt = licenseManager.getLicenseInfo(tenantId, moduleId);
            if (licenseInfoOpt.isPresent()) {
                LicenseInfo licenseInfo = licenseInfoOpt.get();
                metadata.put("remainingCalls", licenseInfo.getRemainingCalls());
                metadata.put("expirationDate", licenseInfo.getExpiryDate());
            }
            
        } catch (LicenseException e) {
            // License验证失败，设置失败状态和错误信息
            response.setStatus("FAILED");
            response.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            // 其他错误，设置失败状态和错误信息
            response.setStatus("FAILED");
            response.setErrorMessage("处理请求时发生错误：" + e.getMessage());
        } finally {
            // 计算处理时间
            long endTime = System.currentTimeMillis();
            response.setProcessingTime(endTime - startTime);
        }
        
        return response;
    }
    
    /**
     * 检查模型是否已注册
     * @param modelId 模型唯一标识
     * @return 是否已注册
     */
    public boolean isRegistered(String modelId) {
        return modelRegistry.containsKey(modelId);
    }
    
    /**
     * 获取所有已注册模型
     * @return 模型注册表
     */
    public Map<String, ModelInfo> getAllModels() {
        return new ConcurrentHashMap<>(modelRegistry);
    }
    
    /**
     * 处理向量搜索请求
     */
    private void handleVectorSearchRequest(AIRequest request, AIResponse response, ModelInfo modelInfo) {
        try {
            // 从请求中获取搜索参数
            Object queryVectorObj = request.getParameters().getOrDefault("queryVector", java.util.Collections.emptyList());
            List<Float> queryVector = new java.util.ArrayList<>();
            if (queryVectorObj instanceof List) {
                for (Object item : (List<?>) queryVectorObj) {
                    if (item instanceof Number) {
                        queryVector.add(((Number) item).floatValue());
                    } else if (item instanceof String) {
                        try {
                            queryVector.add(Float.parseFloat((String) item));
                        } catch (NumberFormatException e) {
                            // 忽略无法解析的字符串
                        }
                    }
                }
            } else if (queryVectorObj instanceof String) {
                try {
                    queryVector.add(Float.parseFloat((String) queryVectorObj));
                } catch (NumberFormatException e) {
                    // 忽略无法解析的字符串
                }
            }
            Integer topK = (Integer) request.getParameters().getOrDefault("topK", 5);
            String collectionName = (String) request.getParameters().getOrDefault("collectionName", 
                    modelInfo.getConfiguration().getOrDefault("defaultCollection", "default_collection"));
            
            if (queryVector.isEmpty()) {
                throw new IllegalArgumentException("Query vector cannot be empty");
            }
            
            // 执行向量搜索
            List<Map<String, Object>> results = vectorStoreService.searchSimilar(queryVector, topK, collectionName);
            
            // 设置响应数据
            Map<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("results", results);
            responseData.put("model", modelInfo.getModelName());
            responseData.put("timestamp", System.currentTimeMillis());
            responseData.put("topK", topK);
            responseData.put("collectionName", collectionName);
            
            response.setData(responseData);
            response.setStatus("SUCCESS");
        } catch (Exception e) {
            response.setStatus("FAILED");
            response.setErrorMessage("向量搜索失败：" + e.getMessage());
        }
    }
}
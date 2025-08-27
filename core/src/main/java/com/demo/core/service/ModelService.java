package com.demo.core.service;

import com.demo.core.entity.ModelEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 大模型服务接口
 */
public interface ModelService {
    
    /**
     * 保存模型信息
     */
    Mono<ModelEntity> saveModel(ModelEntity model);
    
    /**
     * 更新模型信息
     */
    Mono<Boolean> updateModel(ModelEntity model);
    
    /**
     * 根据ID删除模型
     */
    Mono<Boolean> deleteModel(String id);
    
    /**
     * 根据模型ID查询模型
     */
    Mono<ModelEntity> getModelById(String modelId);
    
    /**
     * 根据模型类型查询模型列表
     */
    Flux<ModelEntity> getModelsByType(String modelType);
    
    /**
     * 查询所有启用的模型
     */
    Flux<ModelEntity> getEnabledModels();
    
    /**
     * 启用或禁用模型
     */
    Mono<Boolean> enableModel(String modelId, boolean enable);
    
    /**
     * 批量保存模型信息
     */
    Flux<ModelEntity> batchSaveModels(List<ModelEntity> models);
}
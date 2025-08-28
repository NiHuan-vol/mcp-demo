package com.demo.core.service.impl;

import com.demo.core.entity.ModelEntity;
import com.demo.core.repository.ModelRepository;
import com.demo.core.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 大模型服务实现类
 */
@Service
public class ModelServiceImpl implements ModelService {
    
    @Autowired
    private ModelRepository modelRepository;
    
    @Override
    public Mono<ModelEntity> saveModel(ModelEntity model) {
        return Mono.fromCallable(() -> {
            if (model.getId() == null) {
                model.setId(UUID.randomUUID());
                model.setCreateTime(LocalDateTime.now());
                model.setUpdateTime(LocalDateTime.now());
            }
            modelRepository.insert(model);
            return model;
        }).subscribeOn(Schedulers.boundedElastic());
    }
    
    @Override
    public Mono<Boolean> updateModel(ModelEntity model) {
        return Mono.fromCallable(() -> {
            model.setUpdateTime(LocalDateTime.now());
            return modelRepository.updateById(model) > 0;
        }).subscribeOn(Schedulers.boundedElastic());
    }
    
    @Override
    public Mono<Boolean> deleteModel(String id) {
        return Mono.fromCallable(() -> {
            return modelRepository.deleteById(id) > 0;
        }).subscribeOn(Schedulers.boundedElastic());
    }
    
    @Override
    public Mono<ModelEntity> getModelById(String modelId) {
        return Mono.fromCallable(() -> {
            return modelRepository.findByModelId(modelId);
        }).subscribeOn(Schedulers.boundedElastic());
    }
    
    @Override
    public Flux<ModelEntity> getModelsByType(String modelType) {
        return Mono.fromCallable(() -> {
            return modelRepository.findByModelType(modelType);
        }).subscribeOn(Schedulers.boundedElastic()).flatMapMany(Flux::fromIterable);
    }
    
    @Override
    public Flux<ModelEntity> getEnabledModels() {
        return Mono.fromCallable(() -> {
            return modelRepository.findEnabledModels();
        }).subscribeOn(Schedulers.boundedElastic()).flatMapMany(Flux::fromIterable);
    }
    
    @Override
    public Mono<Boolean> enableModel(String modelId, boolean enable) {
        return Mono.fromCallable(() -> {
            ModelEntity model = modelRepository.findByModelId(modelId);
            if (model != null) {
                model.setEnabled(enable);
                model.setUpdateTime(LocalDateTime.now());
                return modelRepository.updateById(model) > 0;
            }
            return false;
        }).subscribeOn(Schedulers.boundedElastic());
    }
    
    @Override
    public Flux<ModelEntity> batchSaveModels(List<ModelEntity> models) {
        return Flux.fromIterable(models)
                .flatMap(this::saveModel)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
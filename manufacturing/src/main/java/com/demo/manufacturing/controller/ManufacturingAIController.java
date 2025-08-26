package com.demo.manufacturing.controller;

import com.demo.core.model.AIRequest;
import com.demo.core.model.AIResponse;
import com.demo.core.registry.ModelRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/manufacturing/ai")
public class ManufacturingAIController {

    @Autowired
    private ModelRegistry modelRegistry;

    /**
     * 制造业智能预测接口
     */
    @PostMapping(value = "/predictive-maintenance", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AIResponse> predictiveMaintenance(@RequestBody AIRequest request) {
        // 使用核心模块的能力处理制造业预测性维护请求
        request.setModelId("manufacturing-prediction-001");
        return Mono.just(modelRegistry.processRequest(request));
    }

    /**
     * 质量检测接口
     */
    @PostMapping(value = "/quality-inspection", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AIResponse> qualityInspection(@RequestBody AIRequest request) {
        // 使用核心模块的能力处理质量检测请求
        request.setModelId("manufacturing-quality-001");
        return Mono.just(modelRegistry.processRequest(request));
    }

    /**
     * 生产优化建议接口
     */
    @PostMapping(value = "/production-optimization", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AIResponse> productionOptimization(@RequestBody AIRequest request) {
        // 使用核心模块的能力处理生产优化请求
        request.setModelId("manufacturing-optimization-001");
        return Mono.just(modelRegistry.processRequest(request));
    }
}
package com.demo.recommendation.controller;

import com.demo.core.model.AIRequest;
import com.demo.core.model.AIResponse;
import com.demo.core.registry.ModelRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/recommendation/ai")
public class RecommendationAIController {

    @Autowired
    private ModelRegistry modelRegistry;

    /**
     * 个性化推荐接口
     */
    @PostMapping(value = "/personalized", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AIResponse> personalizedRecommendation(@RequestBody AIRequest request) {
        // 使用核心模块的能力处理个性化推荐请求
        request.setModelId("recommendation-personalized-001");
        return Mono.just(modelRegistry.processRequest(request));
    }

    /**
     * 相似内容推荐接口
     */
    @PostMapping(value = "/similar-content", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AIResponse> similarContentRecommendation(@RequestBody AIRequest request) {
        // 使用核心模块的能力处理相似内容推荐请求
        request.setModelId("recommendation-similar-001");
        return Mono.just(modelRegistry.processRequest(request));
    }

    /**
     * 关联推荐接口
     */
    @PostMapping(value = "/correlation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AIResponse> correlationRecommendation(@RequestBody AIRequest request) {
        // 使用核心模块的能力处理关联推荐请求
        request.setModelId("recommendation-correlation-001");
        return Mono.just(modelRegistry.processRequest(request));
    }
}
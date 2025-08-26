package com.demo.core.vectorstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.core.vectorstore.service.VectorStoreService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 向量存储API控制器
 */
@RestController
@RequestMapping("/api/vector-store")
public class VectorStoreController {

    private final VectorStoreService vectorStoreService;

    @Autowired
    public VectorStoreController(VectorStoreService vectorStoreService) {
        this.vectorStoreService = vectorStoreService;
    }

    /**
     * 创建向量集合
     */
    @PostMapping("/collections")
    public ResponseEntity<Void> createCollection(
            @RequestParam String collectionName,
            @RequestParam(defaultValue = "768") int dimension) {
        
        vectorStoreService.createCollection(collectionName, dimension);
        return ResponseEntity.ok().build();
    }

    /**
     * 添加单个向量到数据库
     */
    @PostMapping("/vectors")
    public ResponseEntity<Map<String, Object>> addVector(
            @RequestParam(required = false) String id,
            @RequestBody List<Float> vector,
            @RequestParam(required = false) Map<String, Object> metadata,
            @RequestParam(defaultValue = "default_collection") String collectionName) {
        
        // 如果没有提供ID，生成一个新的UUID
        String vectorId = (id != null) ? id : UUID.randomUUID().toString();
        boolean success = vectorStoreService.addVector(vectorId, vector, metadata, collectionName);
        
        if (success) {
            Map<String, Object> response = Map.of(
                    "success", true,
                    "id", vectorId
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = Map.of(
                    "success", false,
                    "message", "Failed to add vector"
            );
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 批量添加向量到数据库
     */
    @PostMapping("/vectors/batch")
    public ResponseEntity<Map<String, Object>> addVectors(
            @RequestBody List<Map<String, Object>> vectors,
            @RequestParam(defaultValue = "default_collection") String collectionName) {
        
        int count = vectorStoreService.addVectors(vectors, collectionName);
        
        Map<String, Object> response = Map.of(
                "success", true,
                "addedCount", count
        );
        return ResponseEntity.ok(response);
    }

    /**
     * 搜索相似向量
     */
    @PostMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchSimilar(
            @RequestBody List<Float> queryVector,
            @RequestParam(defaultValue = "5") int topK,
            @RequestParam(defaultValue = "default_collection") String collectionName) {
        
        List<Map<String, Object>> results = vectorStoreService.searchSimilar(queryVector, topK, collectionName);
        return ResponseEntity.ok(results);
    }

    /**
     * 删除向量
     */
    @DeleteMapping("/vectors")
    public ResponseEntity<Map<String, Object>> deleteVectors(
            @RequestParam List<String> ids,
            @RequestParam(defaultValue = "default_collection") String collectionName) {
        
        int deletedCount = vectorStoreService.deleteVectors(ids, collectionName);
        
        Map<String, Object> response = Map.of(
                "success", true,
                "deletedCount", deletedCount
        );
        return ResponseEntity.ok(response);
    }

    /**
     * 删除向量集合
     */
    @DeleteMapping("/collections")
    public ResponseEntity<Map<String, Object>> deleteCollection(
            @RequestParam String collectionName) {
        
        boolean success = vectorStoreService.deleteCollection(collectionName);
        
        if (success) {
            Map<String, Object> response = Map.of(
                    "success", true
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = Map.of(
                    "success", false,
                    "message", "Failed to delete collection"
            );
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 检查集合是否存在
     */
    @GetMapping("/collections/exists")
    public ResponseEntity<Boolean> collectionExists(
            @RequestParam String collectionName) {
        
        boolean exists = vectorStoreService.collectionExists(collectionName);
        return ResponseEntity.ok(exists);
    }
}
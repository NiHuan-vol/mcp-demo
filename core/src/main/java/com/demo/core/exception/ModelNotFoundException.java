package com.demo.core.exception;

/**
 * 模型不存在异常，当请求的模型不存在时抛出
 */
public class ModelNotFoundException extends RuntimeException {
    
    private final String modelId;
    
    /**
     * 构造函数
     * @param modelId 不存在的模型ID
     */
    public ModelNotFoundException(String modelId) {
        super("Model not found: " + modelId);
        this.modelId = modelId;
    }
    
    /**
     * 获取不存在的模型ID
     * @return 模型ID
     */
    public String getModelId() {
        return modelId;
    }
}
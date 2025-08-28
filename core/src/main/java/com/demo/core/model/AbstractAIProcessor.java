package com.demo.core.model;

/**
 * AI处理器的抽象实现，提供了基础功能
 */
public abstract class AbstractAIProcessor implements AIProcessor {
    
    private final String processorId;
    private final String processorName;
    private boolean initialized = false;
    private boolean available = false;
    
    /**
     * 构造函数
     * @param processorId 处理器ID
     * @param processorName 处理器名称
     */
    protected AbstractAIProcessor(String processorId, String processorName) {
        this.processorId = processorId;
        this.processorName = processorName;
    }
    
    @Override
    public String getProcessorId() {
        return processorId;
    }
    
    @Override
    public String getProcessorName() {
        return processorName;
    }
    
    @Override
    public synchronized void initialize() {
        if (!initialized) {
            try {
                doInitialize();
                initialized = true;
                available = true;
            } catch (Exception e) {
                available = false;
                throw new RuntimeException("Failed to initialize processor: " + processorId, e);
            }
        }
    }
    
    @Override
    public synchronized void shutdown() {
        if (initialized) {
            try {
                doShutdown();
                available = false;
                initialized = false;
            } catch (Exception e) {
                throw new RuntimeException("Failed to shutdown processor: " + processorId, e);
            }
        }
    }
    
    @Override
    public boolean isAvailable() {
        return available;
    }
    
    /**
     * 设置处理器可用性状态
     * @param available 是否可用
     */
    protected void setAvailable(boolean available) {
        this.available = available;
    }
    
    /**
     * 子类实现的初始化逻辑
     */
    protected abstract void doInitialize();
    
    /**
     * 子类实现的关闭逻辑
     */
    protected abstract void doShutdown();
}
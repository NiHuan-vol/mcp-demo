package com.demo.core.metrics;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 集成Prometheus的指标收集器
 */
@Component
public class MetricsCollector {
    
    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> counters = new ConcurrentHashMap<>();
    
    public MetricsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    /**
     * 增加计数器指标
     * @param metricName 指标名称
     * @param tags 标签键值对
     */
    public void incrementCounter(String metricName, String... tags) {
        getOrCreateCounter(metricName, tags).increment();
    }
    
    /**
     * 增加计数器指标指定次数
     * @param metricName 指标名称
     * @param amount 增加的次数
     * @param tags 标签键值对
     */
    public void incrementCounter(String metricName, double amount, String... tags) {
        getOrCreateCounter(metricName, tags).increment(amount);
    }
    
    /**
     * 记录方法执行时间（可以配合@Timed注解使用）
     * 此方法主要作为示例，实际使用中推荐直接使用@Timed注解
     */
    @Timed(value = "method.execution.time", description = "Time taken to execute method")
    public void recordExecutionTime(Runnable operation, String... tags) {
        operation.run();
    }
    
    /**
     * 获取或创建计数器
     * @param metricName 指标名称
     * @param tags 标签键值对
     * @return 计数器
     */
    private Counter getOrCreateCounter(String metricName, String... tags) {
        String key = createCounterKey(metricName, tags);
        return counters.computeIfAbsent(key, 
                k -> Counter.builder(metricName)
                        .tags(tags)
                        .register(meterRegistry));
    }
    
    /**
     * 创建计数器的唯一键
     * @param metricName 指标名称
     * @param tags 标签键值对
     * @return 唯一键
     */
    private String createCounterKey(String metricName, String... tags) {
        StringBuilder sb = new StringBuilder(metricName);
        for (String tag : tags) {
            sb.append(":").append(tag);
        }
        return sb.toString();
    }
}
package com.demo.recommendation.mcp.commercial;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 商业化服务接口
 * 定义订阅管理、CPS分成、支付处理等商业化相关功能
 */
public interface CommercialService {
    
    // ============ 订阅服务相关 ============    
    
    /**
     * 创建用户订阅
     * @param userId 用户ID
     * @param subscriptionType 订阅类型 (monthly/yearly)
     * @param paymentMethod 支付方式
     * @return 订阅结果，包含订阅ID和状态
     */
    Map<String, Object> createSubscription(String userId, String subscriptionType, String paymentMethod);
    
    /**
     * 取消用户订阅
     * @param userId 用户ID
     * @param subscriptionId 订阅ID
     * @return 取消结果
     */
    Map<String, Object> cancelSubscription(String userId, String subscriptionId);
    
    /**
     * 查询用户订阅状态
     * @param userId 用户ID
     * @return 订阅状态信息
     */
    Map<String, Object> getSubscriptionStatus(String userId);
    
    /**
     * 检查用户是否有权限使用高级功能
     * @param userId 用户ID
     * @param featureCode 功能代码
     * @return 是否有权限
     */
    boolean checkFeatureAccess(String userId, String featureCode);
    
    // ============ CPS分成相关 ============    
    
    /**
     * 记录CPS交易
     * @param userId 用户ID
     * @param serviceType 服务类型 (restaurant/hotel/gasStation/chargingStation)
     * @param transactionId 交易ID
     * @param amount 交易金额
     * @return 记录结果
     */
    Map<String, Object> recordCpsTransaction(String userId, String serviceType, String transactionId, double amount);
    
    /**
     * 查询CPS收益
     * @param serviceType 服务类型 (可选，为空时查询所有)
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 收益统计信息
     */
    Map<String, Object> getCpsRevenue(String serviceType, long startTime, long endTime);
    
    // ============ 支付相关 ============    
    
    /**
     * 创建支付订单
     * @param userId 用户ID
     * @param amount 金额
     * @param paymentType 支付类型
     * @param orderType 订单类型
     * @return 支付订单信息
     */
    Map<String, Object> createPaymentOrder(String userId, double amount, String paymentType, String orderType);
    
    /**
     * 查询支付状态
     * @param orderId 订单ID
     * @return 支付状态信息
     */
    Map<String, Object> queryPaymentStatus(String orderId);
    
    // ============ 异步方法 ============    
    
    /**
     * 异步创建订阅
     * @param userId 用户ID
     * @param subscriptionType 订阅类型
     * @param paymentMethod 支付方式
     * @return 包含订阅结果的CompletableFuture
     */
    CompletableFuture<Map<String, Object>> createSubscriptionAsync(String userId, String subscriptionType, String paymentMethod);
    
    /**
     * 异步记录CPS交易
     * @param userId 用户ID
     * @param serviceType 服务类型
     * @param transactionId 交易ID
     * @param amount 交易金额
     * @return 包含记录结果的CompletableFuture
     */
    CompletableFuture<Map<String, Object>> recordCpsTransactionAsync(String userId, String serviceType, String transactionId, double amount);
}
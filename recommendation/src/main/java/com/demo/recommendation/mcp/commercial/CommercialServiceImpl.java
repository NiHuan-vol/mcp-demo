package com.demo.recommendation.mcp.commercial;

import com.demo.recommendation.config.RecommendationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商业化服务实现类
 * 实现订阅管理、CPS分成、支付处理等商业化相关功能
 */
@Service
public class CommercialServiceImpl implements CommercialService {

    @Autowired
    private RecommendationConfig config;
    
    // 用户订阅信息存储
    private final Map<String, List<SubscriptionInfo>> userSubscriptions = new ConcurrentHashMap<>();
    
    // CPS交易记录存储
    private final List<CpsTransaction> cpsTransactions = new ArrayList<>();
    
    // 功能访问权限映射
    private final Map<String, Set<String>> featurePermissions = new HashMap<>();
    
    // 支付订单存储
    private final Map<String, PaymentOrder> paymentOrders = new ConcurrentHashMap<>();

    public CommercialServiceImpl() {
        // 初始化功能访问权限
        initFeaturePermissions();
    }
    
    /**
     * 初始化功能访问权限
     */
    private void initFeaturePermissions() {
        // 高级路线规划功能
        Set<String> routePlanningFeatures = new HashSet<>(Arrays.asList(
                "advanced_route_planning", 
                "real_time_traffic_avoidance", 
                "weather_avoidance"
        ));
        featurePermissions.put("route_planning", routePlanningFeatures);
        
        // 翻译与语音助手功能
        Set<String> translationFeatures = new HashSet<>(Arrays.asList(
                "offline_translation", 
                "vip_voice_assistant", 
                "cross_border_translation"
        ));
        featurePermissions.put("translation", translationFeatures);
        
        // 增值服务功能
        Set<String> premiumFeatures = new HashSet<>(Arrays.asList(
                "highway_exclusive_route", 
                "scenic_area_exclusive_route", 
                "customized_travel_plan"
        ));
        featurePermissions.put("premium", premiumFeatures);
    }

    @Override
    public Map<String, Object> createSubscription(String userId, String subscriptionType, String paymentMethod) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证参数
            if (userId == null || userId.isEmpty()) {
                result.put("status", "FAILED");
                result.put("message", "用户ID不能为空");
                return result;
            }
            
            if (!"monthly".equals(subscriptionType) && !"yearly".equals(subscriptionType)) {
                result.put("status", "FAILED");
                result.put("message", "不支持的订阅类型");
                return result;
            }
            
            // 创建支付订单
            double price = "monthly".equals(subscriptionType) ? 
                           config.getMonthlySubscriptionPrice() : config.getYearlySubscriptionPrice();
            
            Map<String, Object> paymentResult = createPaymentOrder(userId, price, paymentMethod, "subscription");
            
            if ("SUCCESS".equals(paymentResult.get("status"))) {
                // 创建订阅信息
                String subscriptionId = UUID.randomUUID().toString();
                long startTime = System.currentTimeMillis();
                long endTime = startTime + ("monthly".equals(subscriptionType) ? 30L * 24 * 60 * 60 * 1000 : 365L * 24 * 60 * 60 * 1000);
                
                SubscriptionInfo subscription = new SubscriptionInfo(
                        subscriptionId, userId, subscriptionType, startTime, endTime, "ACTIVE"
                );
                
                // 保存订阅信息
                userSubscriptions.computeIfAbsent(userId, k -> new ArrayList<>()).add(subscription);
                
                result.put("status", "SUCCESS");
                result.put("subscriptionId", subscriptionId);
                result.put("startTime", startTime);
                result.put("endTime", endTime);
                result.put("message", "订阅成功");
            } else {
                result.put("status", "FAILED");
                result.put("message", "支付失败: " + paymentResult.get("message"));
            }
            
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("message", "创建订阅时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> cancelSubscription(String userId, String subscriptionId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<SubscriptionInfo> subscriptions = userSubscriptions.get(userId);
            if (subscriptions == null || subscriptions.isEmpty()) {
                result.put("status", "FAILED");
                result.put("message", "用户没有订阅记录");
                return result;
            }
            
            // 查找并取消订阅
            boolean found = false;
            for (SubscriptionInfo subscription : subscriptions) {
                if (subscription.getSubscriptionId().equals(subscriptionId) && 
                    "ACTIVE".equals(subscription.getStatus())) {
                    subscription.setStatus("CANCELLED");
                    subscription.setCancelledAt(System.currentTimeMillis());
                    found = true;
                    break;
                }
            }
            
            if (found) {
                result.put("status", "SUCCESS");
                result.put("message", "订阅已取消");
            } else {
                result.put("status", "FAILED");
                result.put("message", "未找到有效的订阅记录");
            }
            
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("message", "取消订阅时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getSubscriptionStatus(String userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<SubscriptionInfo> subscriptions = userSubscriptions.get(userId);
            if (subscriptions == null || subscriptions.isEmpty()) {
                result.put("status", "NO_SUBSCRIPTION");
                result.put("message", "用户没有订阅记录");
                return result;
            }
            
            // 检查是否有活跃的订阅
            boolean hasActiveSubscription = false;
            List<Map<String, Object>> subscriptionList = new ArrayList<>();
            
            long currentTime = System.currentTimeMillis();
            
            for (SubscriptionInfo subscription : subscriptions) {
                Map<String, Object> subscriptionMap = new HashMap<>();
                subscriptionMap.put("subscriptionId", subscription.getSubscriptionId());
                subscriptionMap.put("type", subscription.getType());
                subscriptionMap.put("startTime", subscription.getStartTime());
                subscriptionMap.put("endTime", subscription.getEndTime());
                subscriptionMap.put("status", subscription.getStatus());
                
                // 检查是否活跃
                boolean isActive = "ACTIVE".equals(subscription.getStatus()) && 
                                  currentTime >= subscription.getStartTime() && 
                                  currentTime <= subscription.getEndTime();
                subscriptionMap.put("isActive", isActive);
                
                if (isActive) {
                    hasActiveSubscription = true;
                }
                
                subscriptionList.add(subscriptionMap);
            }
            
            result.put("status", hasActiveSubscription ? "HAS_ACTIVE_SUBSCRIPTION" : "NO_ACTIVE_SUBSCRIPTION");
            result.put("subscriptions", subscriptionList);
            result.put("totalSubscriptions", subscriptionList.size());
            
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("message", "查询订阅状态时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public boolean checkFeatureAccess(String userId, String featureCode) {
        try {
            // 检查用户是否有活跃的订阅
            Map<String, Object> subscriptionStatus = getSubscriptionStatus(userId);
            if (!"HAS_ACTIVE_SUBSCRIPTION".equals(subscriptionStatus.get("status"))) {
                return false;
            }
            
            // 检查功能代码是否在允许的范围内
            for (Map.Entry<String, Set<String>> entry : featurePermissions.entrySet()) {
                if (entry.getValue().contains(featureCode)) {
                    return true;
                }
            }
            
        } catch (Exception e) {
            // 发生异常时默认拒绝访问
            return false;
        }
        
        return false;
    }

    @Override
    public Map<String, Object> recordCpsTransaction(String userId, String serviceType, String transactionId, double amount) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证参数
            if (userId == null || userId.isEmpty() || transactionId == null || transactionId.isEmpty()) {
                result.put("status", "FAILED");
                result.put("message", "用户ID和交易ID不能为空");
                return result;
            }
            
            // 检查服务类型是否支持CPS
            double cpsRate = 0;
            switch (serviceType) {
                case "restaurant":
                    cpsRate = config.getRestaurantCpsRate();
                    break;
                case "hotel":
                    cpsRate = config.getHotelCpsRate();
                    break;
                case "gasStation":
                    cpsRate = config.getGasStationCpsRate();
                    break;
                case "chargingStation":
                    cpsRate = config.getChargingStationCpsRate();
                    break;
                default:
                    result.put("status", "FAILED");
                    result.put("message", "不支持的服务类型");
                    return result;
            }
            
            // 计算CPS收益
            double revenue = amount * cpsRate;
            
            // 记录交易
            synchronized (cpsTransactions) {
                CpsTransaction transaction = new CpsTransaction(
                        transactionId, userId, serviceType, amount, revenue, System.currentTimeMillis()
                );
                cpsTransactions.add(transaction);
            }
            
            result.put("status", "SUCCESS");
            result.put("revenue", revenue);
            result.put("cpsRate", cpsRate);
            result.put("message", "CPS交易记录成功");
            
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("message", "记录CPS交易时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getCpsRevenue(String serviceType, long startTime, long endTime) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            double totalRevenue = 0;
            int totalTransactions = 0;
            Map<String, Double> revenueByServiceType = new HashMap<>();
            Map<String, Integer> transactionsByServiceType = new HashMap<>();
            
            synchronized (cpsTransactions) {
                for (CpsTransaction transaction : cpsTransactions) {
                    // 按时间筛选
                    if (transaction.getTimestamp() >= startTime && transaction.getTimestamp() <= endTime) {
                        // 按服务类型筛选
                        if (serviceType == null || serviceType.isEmpty() || 
                            serviceType.equals(transaction.getServiceType())) {
                            totalRevenue += transaction.getRevenue();
                            totalTransactions++;
                            
                            // 按服务类型统计
                            revenueByServiceType.put(transaction.getServiceType(), 
                                    revenueByServiceType.getOrDefault(transaction.getServiceType(), 0.0) + transaction.getRevenue());
                            transactionsByServiceType.put(transaction.getServiceType(), 
                                    transactionsByServiceType.getOrDefault(transaction.getServiceType(), 0) + 1);
                        }
                    }
                }
            }
            
            result.put("status", "SUCCESS");
            result.put("totalRevenue", totalRevenue);
            result.put("totalTransactions", totalTransactions);
            result.put("revenueByServiceType", revenueByServiceType);
            result.put("transactionsByServiceType", transactionsByServiceType);
            
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("message", "查询CPS收益时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> createPaymentOrder(String userId, double amount, String paymentType, String orderType) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证参数
            if (userId == null || userId.isEmpty()) {
                result.put("status", "FAILED");
                result.put("message", "用户ID不能为空");
                return result;
            }
            
            if (amount <= 0) {
                result.put("status", "FAILED");
                result.put("message", "金额必须大于0");
                return result;
            }
            
            // 创建支付订单
            String orderId = UUID.randomUUID().toString();
            PaymentOrder order = new PaymentOrder(
                    orderId, userId, amount, paymentType, orderType, "PENDING", System.currentTimeMillis()
            );
            
            // 保存订单
            paymentOrders.put(orderId, order);
            
            // 模拟支付处理（实际应该调用支付网关）
            // 这里简单模拟支付成功
            order.setStatus("SUCCESS");
            order.setPaidAt(System.currentTimeMillis());
            
            result.put("status", "SUCCESS");
            result.put("orderId", orderId);
            result.put("amount", amount);
            result.put("message", "支付订单创建成功");
            
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("message", "创建支付订单时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> queryPaymentStatus(String orderId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            PaymentOrder order = paymentOrders.get(orderId);
            if (order == null) {
                result.put("status", "FAILED");
                result.put("message", "未找到订单");
                return result;
            }
            
            result.put("status", "SUCCESS");
            result.put("orderId", order.getOrderId());
            result.put("amount", order.getAmount());
            result.put("paymentType", order.getPaymentType());
            result.put("orderType", order.getOrderType());
            result.put("paymentStatus", order.getStatus());
            result.put("createdAt", order.getCreatedAt());
            result.put("paidAt", order.getPaidAt());
            
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("message", "查询支付状态时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public CompletableFuture<Map<String, Object>> createSubscriptionAsync(String userId, String subscriptionType, String paymentMethod) {
        return CompletableFuture.supplyAsync(() -> 
                createSubscription(userId, subscriptionType, paymentMethod)
        );
    }

    @Override
    public CompletableFuture<Map<String, Object>> recordCpsTransactionAsync(String userId, String serviceType, String transactionId, double amount) {
        return CompletableFuture.supplyAsync(() -> 
                recordCpsTransaction(userId, serviceType, transactionId, amount)
        );
    }
    
    // ============ 内部数据类 ============    
    
    /**
     * 订阅信息类
     */
    private static class SubscriptionInfo {
        private String subscriptionId;
        private String userId;
        private String type;
        private long startTime;
        private long endTime;
        private String status;
        private long cancelledAt;
        
        public SubscriptionInfo(String subscriptionId, String userId, String type, long startTime, long endTime, String status) {
            this.subscriptionId = subscriptionId;
            this.userId = userId;
            this.type = type;
            this.startTime = startTime;
            this.endTime = endTime;
            this.status = status;
        }
        
        public String getSubscriptionId() {
            return subscriptionId;
        }
        
        public String getUserId() {
            return userId;
        }
        
        public String getType() {
            return type;
        }
        
        public long getStartTime() {
            return startTime;
        }
        
        public long getEndTime() {
            return endTime;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public long getCancelledAt() {
            return cancelledAt;
        }
        
        public void setCancelledAt(long cancelledAt) {
            this.cancelledAt = cancelledAt;
        }
    }
    
    /**
     * CPS交易信息类
     */
    private static class CpsTransaction {
        private String transactionId;
        private String userId;
        private String serviceType;
        private double amount;
        private double revenue;
        private long timestamp;
        
        public CpsTransaction(String transactionId, String userId, String serviceType, double amount, double revenue, long timestamp) {
            this.transactionId = transactionId;
            this.userId = userId;
            this.serviceType = serviceType;
            this.amount = amount;
            this.revenue = revenue;
            this.timestamp = timestamp;
        }
        
        public String getTransactionId() {
            return transactionId;
        }
        
        public String getUserId() {
            return userId;
        }
        
        public String getServiceType() {
            return serviceType;
        }
        
        public double getAmount() {
            return amount;
        }
        
        public double getRevenue() {
            return revenue;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
    }
    
    /**
     * 支付订单类
     */
    private static class PaymentOrder {
        private String orderId;
        private String userId;
        private double amount;
        private String paymentType;
        private String orderType;
        private String status;
        private long createdAt;
        private long paidAt;
        
        public PaymentOrder(String orderId, String userId, double amount, String paymentType, String orderType, String status, long createdAt) {
            this.orderId = orderId;
            this.userId = userId;
            this.amount = amount;
            this.paymentType = paymentType;
            this.orderType = orderType;
            this.status = status;
            this.createdAt = createdAt;
        }
        
        public String getOrderId() {
            return orderId;
        }
        
        public String getUserId() {
            return userId;
        }
        
        public double getAmount() {
            return amount;
        }
        
        public String getPaymentType() {
            return paymentType;
        }
        
        public String getOrderType() {
            return orderType;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public long getCreatedAt() {
            return createdAt;
        }
        
        public long getPaidAt() {
            return paidAt;
        }
        
        public void setPaidAt(long paidAt) {
            this.paidAt = paidAt;
        }
    }
}
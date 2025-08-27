package com.demo.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 订阅实体类
 */
@Data
@TableName("sys_subscription")
public class Subscription implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private UUID id;
    
    private UUID userId;
    private String subscriptionType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String paymentMethod;
    private String orderId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
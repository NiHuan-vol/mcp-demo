package com.demo.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 收藏服务实体类
 */
@Data
@TableName("sys_favorite_service")
public class FavoriteService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private UUID id;
    
    private UUID userId;
    private String serviceId;
    private String serviceType;
    private String serviceName;
    private String serviceDescription;
    private String serviceUrl;
    private String serviceIcon;
    private LocalDateTime collectTime;
}
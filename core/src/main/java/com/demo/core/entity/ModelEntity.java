package com.demo.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 大模型实体类
 */
@Data
@TableName("sys_model")
public class ModelEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private UUID id;
    
    private String modelId;
    private String modelName;
    private String modelDescription;
    private String modelType;
    private String provider;
    private String apiUrl;
    private String apiKey;
    private String secretKey;
    private String configParams;
    private String embeddingDimension;
    private Boolean enabled;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String version;
}
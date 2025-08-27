package com.demo.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 搜索历史实体类
 */
@Data
@TableName("sys_search_history")
public class SearchHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private UUID id;
    
    private UUID userId;
    private String searchType;
    private String queryText;
    private String searchParams;
    private String searchResults;
    private LocalDateTime searchTime;
}
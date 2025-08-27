package com.demo.core.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.core.entity.Subscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 订阅Repository接口
 */
@Mapper
public interface SubscriptionRepository extends BaseMapper<Subscription> {
    
    /**
     * 根据用户ID查询订阅列表
     */
    @Select("SELECT * FROM sys_subscription WHERE user_id = #{userId}")
    List<Subscription> findByUserId(@Param("userId") UUID userId);
    
    /**
     * 根据用户ID和状态查询订阅
     */
    @Select("SELECT * FROM sys_subscription WHERE user_id = #{userId} AND status = #{status}")
    Subscription findByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") String status);
    
    /**
     * 查询用户有效的订阅（未过期）
     */
    @Select("SELECT * FROM sys_subscription WHERE user_id = #{userId} AND end_time > #{now} AND status = 'ACTIVE'")
    List<Subscription> findActiveSubscriptionsByUserId(@Param("userId") UUID userId, @Param("now") LocalDateTime now);
}
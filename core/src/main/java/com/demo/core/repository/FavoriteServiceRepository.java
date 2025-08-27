package com.demo.core.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.core.entity.FavoriteService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.UUID;

/**
 * 收藏服务Repository接口
 */
@Mapper
public interface FavoriteServiceRepository extends BaseMapper<FavoriteService> {
    
    /**
     * 根据用户ID查询收藏列表
     */
    @Select("SELECT * FROM sys_favorite_service WHERE user_id = #{userId}")
    List<FavoriteService> findByUserId(@Param("userId") UUID userId);
    
    /**
     * 根据用户ID和服务类型查询收藏
     */
    @Select("SELECT * FROM sys_favorite_service WHERE user_id = #{userId} AND service_type = #{serviceType}")
    List<FavoriteService> findByUserIdAndServiceType(@Param("userId") UUID userId, @Param("serviceType") String serviceType);
    
    /**
     * 检查用户是否已收藏某个服务
     */
    @Select("SELECT COUNT(1) FROM sys_favorite_service WHERE user_id = #{userId} AND service_id = #{serviceId}")
    int checkExists(@Param("userId") UUID userId, @Param("serviceId") String serviceId);
    
    /**
     * 根据用户ID和服务ID删除收藏
     */
    @Select("DELETE FROM sys_favorite_service WHERE user_id = #{userId} AND service_id = #{serviceId}")
    void deleteByUserIdAndServiceId(@Param("userId") UUID userId, @Param("serviceId") String serviceId);
}
package com.demo.core.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.core.entity.ModelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 大模型Repository接口
 */
@Mapper
public interface ModelRepository extends BaseMapper<ModelEntity> {
    
    /**
     * 根据模型ID查询模型
     */
    @Select("SELECT * FROM sys_model WHERE model_id = #{modelId}")
    ModelEntity findByModelId(@Param("modelId") String modelId);
    
    /**
     * 根据模型类型查询模型列表
     */
    @Select("SELECT * FROM sys_model WHERE model_type = #{modelType}")
    List<ModelEntity> findByModelType(@Param("modelType") String modelType);
    
    /**
     * 查询启用的模型
     */
    @Select("SELECT * FROM sys_model WHERE enabled = 1")
    List<ModelEntity> findEnabledModels();
    
    /**
     * 检查模型ID是否存在
     */
    @Select("SELECT COUNT(1) FROM sys_model WHERE model_id = #{modelId}")
    int checkModelIdExists(@Param("modelId") String modelId);
}
package com.demo.core.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.core.entity.SearchHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 搜索历史Repository接口
 */
@Mapper
public interface SearchHistoryRepository extends BaseMapper<SearchHistory> {
    
    /**
     * 根据用户ID查询搜索历史
     */
    @Select("SELECT * FROM sys_search_history WHERE user_id = #{userId} ORDER BY search_time DESC")
    List<SearchHistory> findByUserId(@Param("userId") UUID userId);
    
    /**
     * 根据用户ID和搜索类型查询搜索历史
     */
    @Select("SELECT * FROM sys_search_history WHERE user_id = #{userId} AND search_type = #{searchType} ORDER BY search_time DESC")
    List<SearchHistory> findByUserIdAndSearchType(@Param("userId") UUID userId, @Param("searchType") String searchType);
    
    /**
     * 查询用户的搜索历史，按时间倒序，分页
     */
    @Select("SELECT * FROM sys_search_history WHERE user_id = #{userId} ORDER BY search_time DESC LIMIT #{offset}, #{limit}")
    List<SearchHistory> findByUserIdPaged(@Param("userId") UUID userId, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 清空用户的搜索历史
     */
    @Select("DELETE FROM sys_search_history WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") UUID userId);
}
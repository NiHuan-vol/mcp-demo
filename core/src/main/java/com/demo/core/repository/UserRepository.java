package com.demo.core.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.core.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Repository接口
 */
@Mapper
public interface UserRepository extends BaseMapper<User> {
    
    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
    
    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT * FROM sys_user WHERE email = #{email}")
    User findByEmail(@Param("email") String email);
    
    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(1) FROM sys_user WHERE username = #{username}")
    int checkUsernameExists(@Param("username") String username);
    
    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(1) FROM sys_user WHERE email = #{email}")
    int checkEmailExists(@Param("email") String email);
}
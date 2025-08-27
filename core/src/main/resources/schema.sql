-- 创建production_db数据库
CREATE DATABASE IF NOT EXISTS production_db DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;
USE production_db;

-- 用户表
drop table if exists sys_user;
create table sys_user (
    id varchar(36) not null primary key,
    username varchar(50) not null unique comment '用户名',
    password varchar(100) not null comment '密码',
    email varchar(100) unique comment '邮箱',
    phone varchar(20) unique comment '手机号',
    nickname varchar(50) comment '昵称',
    avatar varchar(255) comment '头像',
    gender varchar(10) comment '性别',
    status varchar(20) default 'ACTIVE' comment '状态：ACTIVE/INACTIVE/LOCKED',
    create_time datetime not null comment '创建时间',
    update_time datetime not null comment '更新时间',
    last_login_time datetime comment '最后登录时间',
    index idx_username(username),
    index idx_email(email),
    index idx_phone(phone)
) comment='系统用户表';

-- 订阅表
drop table if exists sys_subscription;
create table sys_subscription (
    id varchar(36) not null primary key,
    user_id varchar(36) not null comment '用户ID',
    subscription_type varchar(50) not null comment '订阅类型',
    start_time datetime not null comment '开始时间',
    end_time datetime not null comment '结束时间',
    status varchar(20) default 'ACTIVE' comment '状态：ACTIVE/CANCELLED/EXPIRED',
    payment_method varchar(50) comment '支付方式',
    order_id varchar(100) comment '订单ID',
    create_time datetime not null comment '创建时间',
    update_time datetime not null comment '更新时间',
    index idx_user_id(user_id),
    index idx_status(status),
    index idx_end_time(end_time)
) comment='用户订阅表';

-- 搜索历史表
drop table if exists sys_search_history;
create table sys_search_history (
    id varchar(36) not null primary key,
    user_id varchar(36) not null comment '用户ID',
    search_type varchar(50) not null comment '搜索类型',
    query_text varchar(255) not null comment '搜索关键词',
    search_params text comment '搜索参数',
    search_results text comment '搜索结果',
    search_time datetime not null comment '搜索时间',
    index idx_user_id(user_id),
    index idx_search_type(search_type),
    index idx_search_time(search_time)
) comment='搜索历史表';

-- 收藏服务表
drop table if exists sys_favorite_service;
create table sys_favorite_service (
    id varchar(36) not null primary key,
    user_id varchar(36) not null comment '用户ID',
    service_id varchar(100) not null comment '服务ID',
    service_type varchar(50) not null comment '服务类型',
    service_name varchar(100) not null comment '服务名称',
    service_description text comment '服务描述',
    service_url varchar(255) comment '服务URL',
    service_icon varchar(255) comment '服务图标',
    collect_time datetime not null comment '收藏时间',
    index idx_user_id(user_id),
    index idx_service_id(service_id),
    index idx_service_type(service_type),
    unique key uk_user_service(user_id, service_id)
) comment='收藏服务表';

-- 大模型表
drop table if exists sys_model;
create table sys_model (
    id varchar(36) not null primary key,
    model_id varchar(100) not null unique comment '模型ID',
    model_name varchar(100) not null comment '模型名称',
    model_description text comment '模型描述',
    model_type varchar(50) not null comment '模型类型',
    provider varchar(50) not null comment '提供商',
    api_url varchar(255) comment 'API地址',
    api_key varchar(255) comment 'API Key',
    secret_key varchar(255) comment 'Secret Key',
    config_params text comment '配置参数(JSON格式)',
    embedding_dimension varchar(20) comment '嵌入维度',
    enabled tinyint(1) default 0 comment '是否启用',
    create_time datetime not null comment '创建时间',
    update_time datetime not null comment '更新时间',
    version varchar(50) comment '版本号',
    index idx_model_id(model_id),
    index idx_model_type(model_type),
    index idx_provider(provider),
    index idx_enabled(enabled)
) comment='大模型配置表';

-- 初始化一些基础数据
-- 插入一个管理员用户
INSERT INTO sys_user (id, username, password, email, nickname, create_time, update_time) 
VALUES (UUID(), 'admin', 'admin123', 'admin@demo.com', '管理员', NOW(), NOW());

-- 插入一些测试模型数据
INSERT INTO sys_model (id, model_id, model_name, model_description, model_type, provider, config_params, enabled, create_time, update_time, version)
VALUES 
(UUID(), 'gpt-3.5-turbo', 'GPT-3.5 Turbo', 'OpenAI的GPT-3.5 Turbo模型', 'LLM', 'OpenAI', '{"temperature": 0.7, "max_tokens": 2000}', 1, NOW(), NOW(), '3.5'),
(UUID(), 'gpt-4', 'GPT-4', 'OpenAI的GPT-4模型', 'LLM', 'OpenAI', '{"temperature": 0.7, "max_tokens": 4000}', 1, NOW(), NOW(), '4.0'),
(UUID(), 'text-embedding-ada-002', 'Text Embedding Ada 002', 'OpenAI的文本嵌入模型', 'EMBEDDING', 'OpenAI', '{"dimension": 1536}', 1, NOW(), NOW(), '002');
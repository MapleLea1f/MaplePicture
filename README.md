# MaplePicture 用户与图片空间管理系统

## 项目简介

本项目是一个基于 Spring Boot 和 MyBatis-Plus 的后端服务，采用 DDD 架构，提供用户管理、图片管理、空间管理等功能，集成了权限校验、分布式认证、分片、WebSocket 通信等能力，适合作为企业级应用的基础模块。

## 技术栈

## 技术栈

- Java 8+
- Spring Boot 2.7.x
- MyBatis-Plus（ORM、分页）
- MySQL
- Redis（缓存、分布式会话）
- Sa-Token（权限认证，含 Redis 集成）
- ShardingSphere-JDBC（分库分表/分片）
- Knife4j（Swagger 增强 API 文档）
- Hutool（工具库）
- Caffeine（本地缓存）
- 腾讯云 COS（对象存储）
- Jsoup（HTML 解析）
- Lombok（简化代码）
- Spring AOP & AspectJ（切面编程）
- WebSocket（实时通信）
- Gson（JSON 处理）
- Maven（项目管理与构建）

## 主要功能

- 用户注册、登录、登出
- 用户信息查询、修改、密码修改
- 管理员权限校验与接口保护
- 分页查询用户列表
- 图片上传、管理
- 空间（Space）管理
- 分布式认证与会话管理
- 统一异常处理与响应封装
- WebSocket 实时通信
- 分片与分布式支持

## 目录结构

- `application/service/`：应用服务层，业务编排
- `domain/`：领域层（user、picture、space），核心业务对象与逻辑
- `infrastructure/`：基础设施层（注解、AOP、异常、工具、配置、权限、认证等）
- `interfaces/`：接口层（controller、dto、vo、assembler）
- `shared/`：通用模块（认证、分片、websocket）
- `resources/`：配置文件、SQL 脚本、静态资源、MyBatis 映射文件
- `test/`：单元测试

## 快速开始

1. 克隆项目

   ```shell
   git clone https://github.com/your-repo/maple-picture-backend-ddd.git
   配置数据库  修改 src/main/resources/application.yml，填写你的数据库连接信息。可参考 sql/create_table.sql 初始化表结构。  
2. 构建并运行
   ```shell
   mvn clean install
   mvn spring-boot:run
已上线地址： maplelea1f.site



欢迎提交 issue 和 PR 参与项目维护与优化。  

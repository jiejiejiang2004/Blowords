# Blowords Backend

Blowords 项目的后端服务，基于 Spring Boot 4.0.2 开发

By [jiejiejiang](https://github.com/jiejiejiang2004)

## 技术栈

- **框架**: Spring Boot 4.0.2
- **Web**: Spring MVC
- **安全**: Spring Security + JWT
- **ORM**: MyBatis-Plus 3.5.15
- **数据库**: MySQL
- **API 文档**: Knife4j OpenAPI 3
- **构建工具**: Maven

## 项目结构

```
src/main/java/com/example/blowords/
├── config/           # 配置类（如 SecurityConfig）
├── entity/           # 实体类
├── filter/           # 过滤器（如 JWT 认证过滤器）
├── handler/          # 处理器（如认证失败处理器）
├── mapper/           # Mapper 接口
├── service/          # 服务层
├── util/             # 工具类（如 JWT 工具）
└── BlowordsApplication.java  # 启动类
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 安装依赖

```bash
mvn clean install
```

### 数据库配置

1. 在 MySQL 中创建数据库：

```sql
DROP DATABASE IF EXISTS blowords;
CREATE DATABASE IF NOT EXISTS blowords;

DROP TABLE IF EXISTS blowords.user;
CREATE TABLE IF NOT EXISTS blowords.user (
    userid INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(10) DEFAULT 'user',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

2. 修改 `src/main/resources/application.yml` 文件中的数据库连接信息：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/blowords?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: your-username
    password: your-password
```

### 运行

#### 开发环境

```bash
mvn spring-boot:run
```

或者在 IDE 中直接运行 `BlowordsApplication.java` 类。

#### 生产环境

```bash
# 构建可执行 JAR
mvn clean package

# 运行
java -jar target/blowords-0.0.1-SNAPSHOT.jar
```

## 构建

```bash
# 构建可执行 JAR
mvn clean package

# 构建并跳过测试
mvn clean package -DskipTests
```

构建产物将生成在 `target` 目录中。

## API 文档

项目使用 Knife4j 生成 API 文档，启动服务后可通过以下地址访问：

```
http://localhost:8083/doc.html
```

## 注意事项

## 许可证

暂无
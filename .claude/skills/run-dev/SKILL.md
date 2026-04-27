---
name: run-dev
description: 启动茶叶商城项目的所有服务（后端、管理后台、用户前台）
---

# run-dev

启动茶叶商城项目开发环境的所有服务。

## 启动顺序

1. **后端** (端口 8080)
   ```bash
   cd 1.JavaSpringBoot && ./mvnw spring-boot:run
   ```

2. **管理后台** (端口 8081)
   ```bash
   cd 2.VueAdmin && npm run serve
   ```

3. **用户前台** (端口 8082)
   ```bash
   cd 3.VueUser && npm run serve
   ```

## 验证服务

- 后端 API: http://localhost:8080
- 管理后台: http://localhost:8081
- 用户前台: http://localhost:8082

## 前提条件

- MySQL 已启动并运行
- 已导入数据库: `4.MysqlDatabase/springbooty2rp6.sql`
- 已安装 Node.js 依赖: `npm install`（在各前端目录）

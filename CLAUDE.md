# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

这是一个茶叶商城（TeaMall）电商系统，使用 Java Spring Boot + Vue 2 构建。

## 技术栈

| 模块 | 技术 | 端口 |
|------|------|------|
| 后端 | Java 1.8, Spring Boot 2.2.2, MyBatis-Plus | 8080 |
| 管理后台 | Vue 2.6, Element UI 2.13, vue-cli | 8081 |
| 用户前台 | Vue 2.6, Element UI 2.15 | 8082 |
| 数据库 | MySQL 8.0 | 3306 |

## 快速启动

### 后端
```bash
cd 1.JavaSpringBoot
./mvnw spring-boot:run
# 或使用 mvnw.cmd 在 Windows 上
```

### 管理后台
```bash
cd 2.VueAdmin
npm install
npm run serve
```

### 用户前台
```bash
cd 3.VueUser
npm install
npm run serve
```

### 数据库
```bash
# 导入数据库脚本
mysql -u root -p < 4.MysqlDatabase/springbooty2rp6.sql
```

## 目录结构

- `1.JavaSpringBoot/` — 后端 REST API（Controller/Service/Dao/Entity）
- `2.VueAdmin/` — 管理后台 Vue SPA
- `3.VueUser/` — 用户前台 Vue SPA
- `4.MysqlDatabase/` — MySQL 数据库脚本

## 注意事项

- 后端测试已禁用（`pom.xml` 中 `skipTests=true`）
- 前端 `vue.config.js` 中 `lintOnSave: false`，ESLint 未生效
- 后端数据库配置在 `src/main/resources/application.yml`
- 前端 API 代理配置在各自 `vue.config.js` 的 `devServer.proxy`

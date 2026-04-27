---
name: build
description: 构建茶叶商城项目的各个模块
---

# build

构建茶叶商城项目的各个模块。

## 构建后端

```bash
cd 1.JavaSpringBoot
./mvnw clean package -DskipTests
```

## 构建管理后台

```bash
cd 2.VueAdmin
npm run build
```

## 构建用户前台

```bash
cd 3.VueUser
npm run build
```

## 注意事项

- 后端构建产物在 `1.JavaSpringBoot/target/`
- 前端构建产物在各自 `dist/` 目录
- 所有模块建议在部署前分别构建

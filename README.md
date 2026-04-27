
# 高山茶叶产供销智能决策平台

## 🌿 项目简介

**高山茶叶产供销智能决策平台**是一个基于 **Java SpringBoot + Vue + MySQL** 构建的现代化茶叶全产业链数字化平台，专注于高山茶叶的生产管理、供应链协同、智能推荐与销售决策支持。

平台整合了茶叶生产管理、库存优化、智能推荐、销售预测等核心功能，通过数据驱动的方式助力高山茶叶产业实现数字化转型升级。

### 🎯 核心特色

- 🏔️ **高山茶叶专属** - 专为高山茶叶品类设计的全流程管理
- 🧠 **智能决策引擎** - 基于数据的销售预测与库存优化
- 🔗 **产供销一体化** - 打通生产、供应、销售全链路
- 📊 **数据可视化** - 直观的经营数据看板与分析图表
- 🤖 **个性化推荐** - 基于用户行为的智能商品推荐

---

## 🏗️ 系统架构

### 📦 核心功能模块

| 模块 | 功能说明 |
|------|----------|
| **茶园生产管理** | 茶叶种植记录、采摘管理、生产批次追踪 |
| **供应链管理** | 茶叶批次管理、库存记录、仓储信息管理、物流追踪 |
| **商品中心** | 茶叶分类、商品信息管理、商品上下架、规格管理 |
| **商家管理** | 商家入驻审核、店铺管理、商品发布、订单处理 |
| **会员管理** | 用户注册登录、个人中心、收货地址、积分管理 |
| **购物系统** | 购物车、在线下单、订单管理、物流查询 |
| **智能推荐系统** | 个性化商品推荐、热门商品推荐、新品推荐 |
| **智能决策中心** | 销售预测分析、库存风险预警、智能补货建议、定价优化、营销决策支持 |
| **数据看板** | 销售数据概览、库存分析、用户行为分析、经营报表 |
| **新闻资讯** | 茶叶行业资讯、茶文化传播、公告通知 |
| **系统管理** | 用户权限管理、系统配置、数据字典、日志审计 |

---

## 🛠️ 技术栈

### 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| **Spring Boot** | 2.2.2 | 核心应用框架 |
| **MyBatis** | - | ORM持久层框架 |
| **MyBatis-Plus** | 2.3 | MyBatis增强工具 |
| **Apache Shiro** | 1.3.2 | 安全认证与权限控制 |
| **MySQL** | 8.0+ | 关系型数据库 |
| **Hutool** | 4.0.12 | Java工具类库 |
| **FastJSON** | 1.2.8 | JSON序列化 |
| **Apache POI** | 3.11/3.9 | Excel文件处理 |
| **百度AI SDK** | 4.4.1 | AI能力集成 |
| **Apache Commons** | - | 通用工具库 |

### 前端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| **Vue.js** | 2.6.10 | 前端核心框架 |
| **Element UI** | 2.13.0 | UI组件库 |
| **Vue Router** | 3.1.5 | 路由管理 |
| **Axios** | 0.19.2 | HTTP客户端 |
| **ECharts** | 5.4.1 | 数据可视化图表 |
| **ECharts WordCloud** | 2.1.0 | 词云图组件 |
| **Vue ECharts** | 6.2.3 | Vue图表组件 |
| **Vue-Quill-Editor** | 3.0.6 | 富文本编辑器 |
| **Print-JS** | 1.5.0 | 打印功能 |
| **Vue-Json-Excel** | 0.3.0 | Excel导出 |
| **Vue-AMap** | 0.5.10 | 地图组件 |
| **JS-MD5** | 0.7.3 | MD5加密 |

### 开发工具

- **后端IDE**: IntelliJ IDEA 2022+
- **前端IDE**: VS Code
- **数据库管理**: Navicat / DBeaver / MySQL Workbench
- **版本控制**: Git
- **API调试**: Postman / Apifox

---

## 💻 运行环境

### 最低配置要求

- **操作系统**: Windows 10+ / macOS 10.14+ / Linux (Ubuntu 18.04+)
- **CPU**: 双核处理器
- **内存**: 4GB RAM
- **硬盘**: 10GB 可用空间

### 推荐配置

- **操作系统**: Windows 11 / macOS 12+ / Ubuntu 20.04+
- **CPU**: 四核或以上处理器
- **内存**: 8GB+ RAM
- **硬盘**: 20GB+ SSD可用空间

### 软件环境

- **JDK**: 1.8
- **MySQL**: 8.0+
- **Node.js**: 16.x
- **Maven**: 3.6+
- **浏览器**: Chrome 80+ / Firefox 75+ / Edge 80+

---

## 📁 项目目录结构

```
TeaMallSystem/
│
├── 1.JavaSpringBoot/                    # 后端服务模块
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/
│   │   │   │   ├── annotation/          # 自定义注解（登录验证、忽略鉴权等）
│   │   │   │   ├── config/             # 配置类（支付宝、拦截器、MyBatis-Plus等）
│   │   │   │   ├── controller/          # 控制器层（API接口入口）
│   │   │   │   │   ├── DecisionController.java      # 智能决策接口
│   │   │   │   │   ├── RecommendationController.java # 推荐系统接口
│   │   │   │   │   ├── TeabaseController.java       # 茶园/茶叶基础接口
│   │   │   │   │   ├── TeabatchController.java      # 茶叶批次接口
│   │   │   │   │   ├── OrdersController.java        # 订单接口
│   │   │   │   │   └── ...
│   │   │   │   ├── dao/                # 数据访问层（DAO接口）
│   │   │   │   ├── entity/              # 实体类
│   │   │   │   │   ├── model/           # 模型类
│   │   │   │   │   ├── view/            # 视图对象
│   │   │   │   │   ├── vo/              # 值对象/API返回对象
│   │   │   │   │   ├── TeabaseEntity.java      # 茶园/茶叶基础信息
│   │   │   │   │   ├── TeabatchEntity.java     # 茶叶批次
│   │   │   │   │   ├── WarehouseInfoEntity.java # 仓储信息
│   │   │   │   │   ├── InventoryrecordEntity.java # 库存记录
│   │   │   │   │   ├── DecisionTaskEntity.java   # 决策任务
│   │   │   │   │   ├── RecommendationEntity.java # 推荐记录
│   │   │   │   │   ├── UserBehaviorEntity.java   # 用户行为
│   │   │   │   │   └── ...
│   │   │   │   ├── service/             # 业务逻辑层
│   │   │   │   │   ├── impl/            # 服务实现类
│   │   │   │   │   ├── DecisionRuleService.java      # 决策规则服务
│   │   │   │   │   ├── DecisionTaskService.java      # 决策任务服务
│   │   │   │   │   ├── RecommendationService.java    # 推荐服务
│   │   │   │   │   ├── SalesForecastService.java     # 销售预测服务
│   │   │   │   │   ├── InventoryRiskService.java     # 库存风险服务
│   │   │   │   │   ├── TeabaseService.java          # 茶园服务
│   │   │   │   │   ├── TeabatchService.java         # 批次服务
│   │   │   │   │   └── ...
│   │   │   │   ├── task/                # 定时任务
│   │   │   │   │   ├── DecisionTaskScheduler.java   # 决策任务调度
│   │   │   │   │   └── RecommendationTask.java      # 推荐任务
│   │   │   │   └── utils/              # 工具类
│   │   │   └── resources/
│   │   │       ├── mapper/             # MyBatis XML映射文件
│   │   │       ├── static/             # 静态资源（上传文件等）
│   │   │       └── application.yml     # Spring Boot配置文件
│   │   └── test/                        # 单元测试
│   ├── pom.xml                          # Maven依赖配置
│   ├── mvnw / mvnw.cmd                  # Maven包装脚本
│   └── ...
│
├── 2.VueAdmin/                          # 管理端前端模块
│   ├── src/
│   │   ├── assets/                      # 静态资源（图片、样式、JS）
│   │   │   ├── css/
│   │   │   ├── img/
│   │   │   └── js/
│   │   ├── components/                  # 通用组件
│   │   │   ├── common/                  # 通用业务组件
│   │   │   ├── home/                    # 首页相关组件
│   │   │   ├── index/                   # 布局组件
│   │   │   └── SvgIcon/                 # 图标组件
│   │   ├── icons/                       # SVG图标资源
│   │   ├── App.vue                      # 根组件
│   │   └── ...
│   ├── public/                          # 公开资源
│   ├── package.json                     # NPM依赖配置
│   ├── package-lock.json
│   └── ...
│
├── 4.MysqlDatabase/                     # 数据库模块
│   ├── springbooty2rp6.sql              # 主数据库结构脚本
│   ├── recommendation_system.sql        # 推荐系统表结构
│   ├── decision_task.sql                # 智能决策表结构
│   ├── demo_incremental_seed.sql        # 演示数据增量脚本
│   ├── operation_decision_incremental.sql # 运营决策增量脚本
│   ├── fix-question-mark-data.sql      # 数据修复脚本
│   └── ...
│
├── run-logs/                            # 运行日志目录
│   └── ...
│
└── README.md                            # 项目说明文档
```

---

## 🚀 快速开始

### 1. 环境准备

确保您的开发环境已安装以下软件：

- [JDK 1.8](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html)
- [MySQL 8.0+](https://dev.mysql.com/downloads/mysql/)
- [Node.js 16.x](https://nodejs.org/dist/latest-v16.x/)
- [Maven 3.6+](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/downloads)

### 2. 克隆项目

```bash
git clone &lt;repository-url&gt;
cd TeaMallSystem
```

### 3. 数据库配置

#### 3.1 创建数据库

使用 MySQL 客户端或图形化工具（如 Navicat）创建数据库：

```sql
CREATE DATABASE springbooty2rp6 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3.2 导入数据库脚本

按顺序执行以下 SQL 脚本（使用 `source` 命令或图形化工具）：

```bash
# 进入数据库目录
cd 4.MysqlDatabase

# 1. 导入主数据库结构（必选）
mysql -u root -p springbooty2rp6 &lt; springbooty2rp6.sql

# 2. 导入推荐系统表（必选）
mysql -u root -p springbooty2rp6 &lt; recommendation_system.sql

# 3. 导入智能决策表（必选）
mysql -u root -p springbooty2rp6 &lt; decision_task.sql

# 4. 导入演示数据（可选，用于开发测试）
mysql -u root -p springbooty2rp6 &lt; demo_incremental_seed.sql
mysql -u root -p springbooty2rp6 &lt; operation_decision_incremental.sql
```

#### 3.3 修改数据库连接配置

编辑后端配置文件：`1.JavaSpringBoot/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/springbooty2rp6?useUnicode=true&amp;characterEncoding=utf-8&amp;useJDBCCompliantTimezoneShift=true&amp;useLegacyDatetimeCode=false&amp;serverTimezone=GMT%2B8
    username: root              # 改为您的MySQL用户名
    password: 123456           # 改为您的MySQL密码
```

### 4. 启动后端服务

#### 4.1 使用 IDEA 启动（推荐）

1. 打开 IntelliJ IDEA
2. 选择 `File` -&gt; `Open`，选择 `1.JavaSpringBoot` 目录
3. 等待 Maven 自动下载依赖（首次可能需要几分钟）
4. 找到主类 `SpringbootSchemaApplication.java`（通常在 `com` 包下）
5. 右键点击 -&gt; `Run 'SpringbootSchemaApplication'`
6. 观察控制台输出，看到 `Tomcat started on port(s): 8080` 表示启动成功

#### 4.2 使用 Maven 命令启动

```bash
cd 1.JavaSpringBoot

# Windows
mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

#### 4.3 后端服务验证

启动成功后，访问以下地址验证：

- 后端服务地址：`http://localhost:8080/springbooty2rp6`

### 5. 启动前端服务

#### 5.1 安装依赖

```bash
cd 2.VueAdmin

# 使用国内镜像加速（可选但推荐）
npm config set registry https://registry.npmmirror.com

# 安装依赖
npm install
```

#### 5.2 启动开发服务器

```bash
npm run serve
```

#### 5.3 访问平台

看到类似以下输出表示启动成功：

```
App running at:
- Local:   http://localhost:8081/
- Network: http://192.168.x.x:8081/
```

在浏览器中打开 `http://localhost:8081` 即可访问**高山茶叶产供销智能决策平台**。

---

## ⚙️ 配置说明

### 后端配置（application.yml）

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `server.port` | 后端服务端口 | 8080 |
| `server.servlet.context-path` | 应用上下文路径 | /springbooty2rp6 |
| `server.tomcat.uri-encoding` | URI编码 | UTF-8 |
| `spring.datasource.url` | 数据库连接地址 | - |
| `spring.datasource.username` | 数据库用户名 | root |
| `spring.datasource.password` | 数据库密码 | 123456 |
| `spring.datasource.driverClassName` | JDBC驱动类 | com.mysql.cj.jdbc.Driver |
| `spring.servlet.multipart.max-file-size` | 最大单个文件上传大小 | 300MB |
| `spring.servlet.multipart.max-request-size` | 最大请求大小 | 300MB |
| `spring.resources.static-locations` | 静态资源位置 | classpath:static/,file:static/ |
| `mybatis-plus.mapper-locations` | Mapper XML位置 | classpath*:mapper/*.xml |
| `mybatis-plus.type-aliases-package` | 实体类包路径 | com.entity |
| `mybatis-plus.global-config.id-type` | 主键生成策略 | 1（用户输入） |
| `mybatis-plus.global-config.field-strategy` | 字段策略 | 1（非NULL判断） |
| `mybatis-plus.global-config.db-column-underline` | 驼峰下划线转换 | true |
| `mybatis-plus.global-config.refresh-mapper` | 刷新Mapper调试 | true |
| `mybatis-plus.global-config.logic-delete-value` | 逻辑删除值 | -1 |
| `mybatis-plus.global-config.logic-not-delete-value` | 逻辑未删除值 | 0 |
| `mybatis-plus.configuration.map-underscore-to-camel-case` | 下划线转驼峰 | true |
| `mybatis-plus.configuration.cache-enabled` | 缓存启用 | false |
| `mybatis-plus.configuration.call-setters-on-nulls` | NULL值调用setter | true |

### 前端配置

前端 API 地址通常在以下文件中配置：
- `src/utils/request.js` 或 `src/api/index.js`
- `vue.config.js`（开发代理配置）

确保 API 地址指向正确的后端服务地址：`http://localhost:8080/springbooty2rp6`

---

## 📊 核心功能使用指南

### 1. 智能决策中心

智能决策中心是平台的核心模块，提供以下功能：

#### 1.1 销售预测分析
- 基于历史销售数据，预测未来一段时间的销量趋势
- 支持按茶叶品类、产地、时间周期等多维度预测
- 可视化展示预测结果与置信区间
- 自动生成预测报告

#### 1.2 库存风险预警
- 实时监控库存水平
- 自动识别滞销商品与库存不足商品
- 设置安全库存阈值
- 库存周转率分析
- 库存预警通知推送

#### 1.3 智能补货建议
- 基于销售预测与当前库存计算最优补货量
- 考虑采购周期、MOQ（最小起订量）等约束
- 生成补货建议清单
- 支持手动调整与审核

#### 1.4 定价优化
- 基于市场行情、历史数据、竞品价格分析
- 动态定价建议
- 价格弹性分析
- 促销活动效果评估

#### 1.5 营销决策支持
- 用户画像分析
- 精准营销方案推荐
- 营销活动效果追踪
- ROI（投资回报率）分析

### 2. 供应链管理

#### 2.1 茶叶批次管理
- 全程追踪茶叶从茶园到消费者的流转
- 批次信息录入（产地、采摘时间、等级、数量等）
- 批次溯源查询
- 质量检测记录

#### 2.2 库存管理
- 入库、出库、盘点、调拨
- 库存流水记录
- 库存实时查询
- 多仓库管理支持

#### 2.3 仓储信息管理
- 仓库信息维护
- 库区、库位管理
- 仓库容量监控
- 仓储人员管理

### 3. 智能推荐系统

#### 3.1 个性化推荐
- 基于用户浏览、购买、收藏等行为
- 协同过滤推荐算法
- 内容推荐算法
- 实时更新推荐结果

#### 3.2 热门推荐
- 实时热销榜单
- 新品推荐
- 限时特惠推荐
- 同类商品推荐

### 4. 数据看板

#### 4.1 销售概览
- 销售总额、订单量、客单价
- 销售趋势图
- 品类销售占比
- 区域销售分布

#### 4.2 库存分析
- 库存总价值
- 库存周转率
- 滞销库存分析
- 库存结构分析

#### 4.3 用户分析
- 用户增长趋势
- 用户活跃度分析
- 用户画像标签
- 复购率分析

#### 4.4 经营报表
- 日报、周报、月报
- 自定义报表
- 报表导出（Excel/PDF）
- 数据对比分析

---

## 🔌 API 接口文档

### 公共返回格式

所有 API 统一返回以下 JSON 格式：

```json
{
  "code": 0,
  "msg": "success",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | Number | 状态码，0表示成功，其他表示失败 |
| `msg` | String | 响应消息 |
| `data` | Object/Array | 响应数据 |

### 认证说明

大部分 API 需要登录认证，通过 Header 传递 Token：

```http
Authorization: Bearer &lt;your-token&gt;
```

### 主要 API 列表

#### 用户模块

| 接口 | 方法 | 说明 |
|------|------|------|
| `/users/login` | POST | 用户登录 |
| `/users/register` | POST | 用户注册 |
| `/users/info` | GET | 获取当前用户信息 |
| `/users/update` | POST | 更新用户信息 |
| `/users/page` | GET | 分页查询用户列表（管理员） |

#### 商品模块

| 接口 | 方法 | 说明 |
|------|------|------|
| `/shangpinxinxi/page` | GET | 分页查询商品列表 |
| `/shangpinxinxi/info/{id}` | GET | 获取商品详情 |
| `/shangpinxinxi/save` | POST | 新增/更新商品 |
| `/shangpinxinxi/delete` | POST | 删除商品 |
| `/shangpinfenlei/list` | GET | 获取商品分类列表 |

#### 订单模块

| 接口 | 方法 | 说明 |
|------|------|------|
| `/orders/page` | GET | 分页查询订单列表 |
| `/orders/info/{id}` | GET | 获取订单详情 |
| `/orders/save` | POST | 创建订单 |
| `/orders/update` | POST | 更新订单状态 |

#### 购物车模块

| 接口 | 方法 | 说明 |
|------|------|------|
| `/cart/page` | GET | 查询购物车列表 |
| `/cart/save` | POST | 添加购物车 |
| `/cart/update` | POST | 更新购物车 |
| `/cart/delete` | POST | 删除购物车商品 |

#### 推荐模块

| 接口 | 方法 | 说明 |
|------|------|------|
| `/recommendation/page` | GET | 获取推荐商品列表 |
| `/recommendation/personal` | GET | 获取个性化推荐 |
| `/recommendation/hot` | GET | 获取热门推荐 |
| `/userbehavior/list` | GET | 获取用户行为记录 |

#### 智能决策模块

| 接口 | 方法 | 说明 |
|------|------|------|
| `/decision/sales-forecast` | GET | 获取销售预测数据 |
| `/decision/inventory-alert` | GET | 获取库存预警列表 |
| `/decision/replenishment-suggest` | GET | 获取补货建议 |
| `/decision/pricing-optimize` | GET | 获取定价建议 |
| `/decision/dashboard` | GET | 获取决策看板数据 |
| `/decisiontask/page` | GET | 分页查询决策任务 |
| `/decisiontask/execute` | POST | 执行决策任务 |

#### 供应链模块

| 接口 | 方法 | 说明 |
|------|------|------|
| `/teabase/page` | GET | 分页查询茶园/茶叶基础信息 |
| `/teabatch/page` | GET | 分页查询茶叶批次 |
| `/teabatch/trace/{id}` | GET | 批次溯源查询 |
| `/inventoryrecord/page` | GET | 查询库存记录 |
| `/warehouseinfo/list` | GET | 获取仓库列表 |

#### 新闻资讯模块

| 接口 | 方法 | 说明 |
|------|------|------|
| `/news/page` | GET | 分页查询新闻列表 |
| `/news/info/{id}` | GET | 获取新闻详情 |

#### 文件上传

| 接口 | 方法 | 说明 |
|------|------|------|
| `/file/upload` | POST | 上传文件（图片/文档等） |

#### 通用接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/config/info` | GET | 获取系统配置 |
| `/aboutus/info` | GET | 获取关于我们信息 |

---

## 🔧 常见问题

### 1. 前端启动报错（OpenSSL 相关）

**问题**：启动前端时出现 `Error: error:0308010C:digital envelope routines::unsupported` 错误

**解决方案**：
项目已在 `package.json` 中配置了 `NODE_OPTIONS=--openssl-legacy-provider`，通常可以直接解决。如仍有问题，请：

1. 确保 Node.js 版本为 16.x
2. 或尝试使用 Node.js 14.x LTS
3. 或手动设置环境变量：
   ```bash
   # Windows
   set NODE_OPTIONS=--openssl-legacy-provider

   # macOS/Linux
   export NODE_OPTIONS=--openssl-legacy-provider
   ```

### 2. 后端启动报错（数据库连接失败）

**问题**：`Communications link failure` 或 `Access denied for user`

**解决方案**：
1. 确认 MySQL 服务已启动
2. 检查 `application.yml` 中的数据库连接信息是否正确
3. 确认数据库用户名和密码正确
4. 确认已创建数据库 `springbooty2rp6`
5. 确认防火墙未阻止 3306 端口
6. 如果使用 MySQL 8.0+，确认驱动类为 `com.mysql.cj.jdbc.Driver`
7. 确认 URL 中包含时区参数 `serverTimezone=GMT%2B8`

### 3. 端口被占用

**问题**：`Port 8080 was already in use` 或 `Port 8081 was already in use`

**解决方案**：
- **修改后端端口**：编辑 `application.yml` 中的 `server.port`
- **修改前端端口**：编辑 `vue.config.js` 中的 `devServer.port`
- **或关闭占用端口的程序**

### 4. Maven 依赖下载慢

**问题**：Maven 下载依赖速度很慢或超时

**解决方案**：配置阿里云 Maven 镜像

编辑 `~/.m2/settings.xml`（Windows 路径通常为 `C:\Users\你的用户名\.m2\settings.xml`）：

```xml
&lt;mirrors&gt;
  &lt;mirror&gt;
    &lt;id&gt;aliyunmaven&lt;/id&gt;
    &lt;mirrorOf&gt;*&lt;/mirrorOf&gt;
    &lt;name&gt;阿里云公共仓库&lt;/name&gt;
    &lt;url&gt;https://maven.aliyun.com/repository/public&lt;/url&gt;
  &lt;/mirror&gt;
&lt;/mirrors&gt;
```

### 5. NPM 依赖安装慢

**问题**：`npm install` 速度很慢或超时

**解决方案**：配置淘宝 NPM 镜像

```bash
npm config set registry https://registry.npmmirror.com
```

或临时使用：

```bash
npm install --registry=https://registry.npmmirror.com
```

### 6. 前端页面空白或报错

**问题**：打开页面是空白，或控制台有报错

**解决方案**：
1. 确认后端服务已正常启动
2. 确认前端 API 地址配置正确
3. 打开浏览器开发者工具（F12）查看 Console 和 Network 中的错误信息
4. 清除浏览器缓存后重试
5. 确认所有依赖已正确安装（重新运行 `npm install`）

---

## 📦 生产部署

### 后端部署

#### 1. 打包

```bash
cd 1.JavaSpringBoot

# 使用 Maven 打包
mvn clean package -DskipTests

# 或使用 Maven 包装器
mvnw.cmd clean package -DskipTests
```

打包完成后，在 `target` 目录下会生成 `.jar` 文件（如 `springbooty2rp6-0.0.1-SNAPSHOT.jar`）

#### 2. 部署到服务器

上传 JAR 文件到服务器，然后运行：

```bash
# 后台运行（推荐）
nohup java -jar springbooty2rp6-0.0.1-SNAPSHOT.jar &gt; app.log 2&gt;&amp;1 &amp;

# 或指定配置文件运行
java -jar springbooty2rp6-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

#### 3. 使用 Docker 部署（可选）

创建 `Dockerfile`：

```dockerfile
FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY target/springbooty2rp6-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
```

构建并运行：

```bash
docker build -t teamall-system .
docker run -d -p 8080:8080 --name teamall-backend teamall-system
```

### 前端部署

#### 1. 打包

```bash
cd 2.VueAdmin
npm run build
```

打包完成后，会在 `dist` 目录下生成静态文件。

#### 2. 部署 Nginx

将 `dist` 目录上传到服务器，配置 Nginx：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端静态文件
    location / {
        root /path/to/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    # 后端 API 代理
    location /springbooty2rp6/ {
        proxy_pass http://localhost:8080/springbooty2rp6/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

重启 Nginx：

```bash
nginx -s reload
```

---

## 🤝 贡献指南

我们欢迎所有形式的贡献！无论是报告 Bug、提交功能请求、改进文档还是贡献代码。

### 贡献流程

1. **Fork 本仓库**
   - 点击页面右上角的 "Fork" 按钮

2. **克隆您的 Fork**
   ```bash
   git clone https://github.com/&lt;your-username&gt;/TeaMallSystem.git
   cd TeaMallSystem
   ```

3. **创建特性分支**
   ```bash
   git checkout -b feature/your-feature-name
   # 或
   git checkout -b fix/your-bugfix-name
   ```

4. **进行更改**
   - 编写代码
   - 确保代码风格与项目一致
   - 添加必要的注释
   - 运行测试确保一切正常

5. **提交更改**
   ```bash
   git add .
   git commit -m "feat: 添加某某功能"
   # 或
   git commit -m "fix: 修复某某问题"
   ```
   请使用清晰的提交信息，建议遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范

6. **推送到您的 Fork**
   ```bash
   git push origin feature/your-feature-name
   ```

7. **创建 Pull Request**
   - 回到本仓库页面
   - 点击 "Pull requests" -&gt; "New pull request"
   - 选择您的分支，填写 PR 描述
   - 提交 PR

### 代码规范

#### Java 代码规范
- 遵循阿里巴巴 Java 开发手册
- 使用 4 空格缩进
- 类名使用大驼峰（UpperCamelCase）
- 方法名和变量名使用小驼峰（lowerCamelCase）
- 常量使用全大写下划线分隔（UPPER_SNAKE_CASE）
- 添加必要的 Javadoc 注释

#### Vue/JavaScript 代码规范
- 遵循 Vue 官方风格指南
- 使用 2 空格缩进
- 组件名使用大驼峰（PascalCase）
- 方法和变量名使用小驼峰（camelCase）
- 使用单引号字符串（除非是模板字符串）
- 使用 `const` 而不是 `let`（当值不会改变时）
- 避免使用 `var`

#### SQL 规范
- 关键字使用大写（SELECT, FROM, WHERE 等）
- 表名和字段名使用小写加下划线（snake_case）
- 编写清晰的注释

### 提交 Issue

如果您发现 Bug 或有功能建议，请通过 Issue 告诉我们：

1. 检查是否已存在相关 Issue
2. 创建新 Issue
3. 使用清晰的标题
4. 详细描述问题或建议
5. 如果是 Bug，请提供复现步骤、环境信息、截图或错误日志

---

## 📄 许可证

### 版权声明

本项目 **仅供个人学习与技术交流使用**。

- 本项目基于开源项目二次开发，原项目版权归原作者所有
- **禁止** 用于任何商业用途
- **禁止** 转售或冒充原创作品
- **禁止** 将本项目的全部或部分内容用于牟利
- 商业使用请联系原作者获得授权

### 免责声明

- 本项目按"现状"提供，不提供任何明示或暗示的保证
- 使用本项目所产生的任何直接或间接损失，项目维护者不承担责任
- 请自行承担使用本项目的风险

### 参考许可证

虽然本项目没有采用标准的开源许可证（如 MIT、Apache 等），但我们鼓励：

- 学习和参考本项目的代码
- 在个人项目中使用本项目的部分代码（请注明来源）
- 改进和优化本项目并反馈给社区

---

## 📞 联系方式

### 获取帮助

如果您在使用过程中有任何问题或建议：

- **提交 Issue**: 推荐通过 GitHub Issues 提问
- **技术交流**: 欢迎技术交流与学习探讨

### 项目信息

- **项目名称**: 高山茶叶产供销智能决策平台
- **项目类型**: Java SpringBoot + Vue 全栈项目
- **开发语言**: Java 1.8, JavaScript (Vue 2)
- **主要用途**: 学习参考、毕业设计、项目实战
- **当前版本**: v1.0.0

---

## 🙏 致谢

感谢以下开源项目和社区：

- [Spring Boot](https://spring.io/projects/spring-boot)
- [MyBatis](https://mybatis.org/)
- [MyBatis-Plus](https://baomidou.com/)
- [Apache Shiro](https://shiro.apache.org/)
- [Vue.js](https://vuejs.org/)
- [Element UI](https://element.eleme.cn/)
- [ECharts](https://echarts.apache.org/)
- [Hutool](https://hutool.cn/)
- 以及所有为本项目做出贡献的开发者！

---

## 📚 更多资源

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot#learn)
- [Vue.js 官方文档](https://cn.vuejs.org/v2/guide/)
- [Element UI 组件库](https://element.eleme.cn/#/zh-CN)
- [MyBatis-Plus 指南](https://baomidou.com/guide/)
- [ECharts 可视化](https://echarts.apache.org/zh/tutorial.html)
- [MySQL 官方文档](https://dev.mysql.com/doc/)

---

## 🌟 支持项目

如果本项目对您有帮助，欢迎：

- ⭐ 给项目点个 Star
- 🔗 分享给有需要的朋友
- 💬 提出宝贵的建议
- 🤝 贡献代码一起完善

---

## 📅 更新日志

### v1.0.0 (2026-04-27)

- ✨ 初始版本发布
- 🏔️ 高山茶叶产供销智能决策平台核心功能
- 📦 茶园生产管理模块
- 🔗 供应链管理模块
- 🤖 智能推荐系统模块
- 🧠 智能决策中心模块（销售预测、库存预警、补货建议、定价优化）
- 📊 数据看板与经营分析模块
- 🛒 电商购物模块（商品、购物车、订单）
- 👥 用户与商家管理模块
- 📰 新闻资讯模块
- ⚙️ 系统管理模块
- 📝 完善的 README 文档

---

<div align="center">
  <b>高山茶叶产供销智能决策平台</b>
  <br>
  让数据驱动决策，让茶香飘得更远 🍵
  <br><br>
  <sub>© 2026 TeaMallSystem Team. All rights reserved.</sub>
</div>


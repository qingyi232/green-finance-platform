# 林林通·绿融通 — 一站式生态银行综合服务网站

聚焦林业与碳汇领域的一站式综合服务网站，集 **收储登记、资产评估、供需交易、金融服务、法律服务** 五大核心业务于一体，立足生态银行实践，赋能绿色产业发展。

## 🌿 设计理念

- **清新生态绿** + **白色卡片** + **毛玻璃质感**，全程拒绝俗套蓝紫渐变
- 高级感 · 专业感 · 一目了然
- 完全围绕"生态"主题打造视觉语言

## 🛠 技术栈

**后端**：SpringBoot 2.7.18 + MyBatis-Plus + Druid + JWT + MySQL 8.0  
**前端**：Vue3 + Vite + Element Plus + Pinia + Vue Router + Axios

## 📂 项目结构

```
林林通·绿融通/
├── backend/                          # SpringBoot 后端
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/lly/
│       │   ├── GreenFinApplication.java
│       │   ├── config/               # 5 个配置类
│       │   ├── controller/           # 17 个 Controller
│       │   ├── entity/               # 19 个实体
│       │   ├── mapper/               # 19 个 Mapper
│       │   ├── interceptor/          # 鉴权拦截器
│       │   └── utils/                # 工具类
│       └── resources/
│           ├── application.yml
│           └── sql/init.sql          # 完整 schema + 示例数据
│
└── frontend/                         # Vue3 前端
    ├── package.json
    ├── vite.config.js
    ├── index.html
    └── src/
        ├── main.js, App.vue, style.css
        ├── api/request.js
        ├── store/index.js
        ├── router/index.js
        ├── components/
        │   ├── PageHeader.vue        # 通用页面头
        │   └── EasyCrud.vue          # 通用 CRUD 组件
        └── views/
            ├── auth/                 # 登录/注册
            ├── public/               # 首页/新闻/政策/反馈/披露/个人中心
            ├── resource/             # 收储登记中心
            ├── evaluation/           # 资产评估中心
            ├── trading/              # 供需交易中心
            ├── finance/              # 金融服务中心
            ├── legal/                # 法律服务中心
            └── admin/                # 后台管理中心
```

## 🗄 数据库表（共 20 张）

| 表名 | 用途 |
|---|---|
| t_user | 用户表（10 种角色） |
| t_news | 新闻动态 |
| t_policy | 政策解读 |
| t_feedback | 用户反馈 |
| t_disclosure | 企业信息披露 |
| t_resource | 生态资源（收储登记） |
| t_evaluation_org | 评估机构 |
| t_evaluator | 评估师 |
| t_evaluation_apply | 评估申请 |
| t_listing | 资源供给挂牌 |
| t_demand | 资源需求发布 |
| t_appointment | 预约洽谈 |
| t_farm_product | 地方特色农产品 |
| t_product_order | 农产品订单 |
| t_finance_product | 金融产品 |
| t_loan_apply | 贷款申请 |
| t_legal_org | 法律援助机构 |
| t_lawyer | 律师 |
| t_legal_consult | 法律咨询 |
| t_stats_daily | 每日统计 |

## 🚀 启动指南

### 1. 数据库

```bash
mysql -u root -p < backend/src/main/resources/sql/init.sql
```

### 2. 后端

```bash
cd backend
mvn clean package
java -jar target/linlintong-greenfin.jar
# 默认端口: 8080
# API 前缀: /api
```

### 3. 前端

```bash
cd frontend
npm install
npm run dev
# 默认端口: 5173
# 自动代理 /api 到后端 http://localhost:8080
```

### 生产构建

```bash
cd frontend
npm run build
# dist/ 目录部署到 nginx 即可
```

## 🔑 演示账号（密码均为 `123456`）

| 角色 | 账号 | 说明 |
|---|---|---|
| 管理员 | `admin` | 系统管理员，可访问后台 |
| 林农 | `linnong01` / `linnong02` | 张志强、李建华 |
| 合作社 | `coop01` / `coop02` | 五峰竹海合作社、恩施富硒茶合作社 |
| 企业 | `ent01` / `ent02` | 宜昌绿源、恩施林下经济 |
| 林业部门 | `forestry01` | 宜昌市林业局 |
| 金融机构 | `bank01` / `bank02` | 湖北宜昌农商银行、湖北恩施建设银行 |
| 评估机构 | `eorg01` | 中林评估事务所 |
| 评估师 | `evaluator01` | 王志远 |
| 法律援助机构 | `legalorg01` | 五峰公益法律援助中心 |
| 律师 | `lawyer01` | 陈律师 |

## 🎯 核心功能一览

### 首页综合
- Hero 区 · 实时数据快览（资源面积、碳汇储量、挂牌数、放款额）
- 6 大业务板块入口卡片
- 新闻政策双栏
- 问题反馈快捷入口（4 种类型）
- 企业信息披露列表
- 平台优势展示

### 收储登记中心
- 森林资源云图（地图标注 + 类型分布 + 实时统计）
- 我要登记表单（4 步流程：选类型→填信息→上传材料→提交审核）
- 资源列表 / 我的登记

### 资产评估中心
- 标准化评估流程图（材料上报→资质核实→勘察复评→结果公示）
- 评估机构 / 评估师列表
- 我要评估表单（按机构/按评估师选择）
- 我的评估申请

### 供需交易中心
- 资源供给市场（文化/供给/调节服务三大类）
- 资源需求市场（开发/采购/合作三类）
- 我要出售 / 我要购买表单
- 地方特色农产品展销
- 预约洽谈系统

### 金融服务中心
- 绿色金融产品列表（5 大类）
- **产品智能匹配**（按金额、期限、用途自动匹配）
- 我要申请贷款（农户端）
- 我要审批（银行端）
- 我的贷款申请

### 法律服务中心
- 区域法律援助库（援助机构 + 律师）
- 区域援助咨询匹配
- 专属法律咨询（收费服务）
- 我的咨询

### 后台管理（管理员专属）
- 数据看板（关键指标 + 资源分布 + 业务统计）
- 用户管理
- 内容中心（新闻、政策、反馈、披露）
- 收储登记审核
- 评估机构 / 评估师 / 评估申请管理
- 资源挂牌 / 需求 / 农产品管理
- 金融产品 / 贷款申请管理
- 法律援助机构 / 律师 / 咨询管理

## 📄 LICENSE

© 2026 林林通·绿融通 · 让绿水青山成为金山银山

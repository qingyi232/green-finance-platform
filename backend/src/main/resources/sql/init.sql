-- ============================================================
-- 林林通·绿融通 生态银行综合服务网站 数据库初始化脚本
-- 适用：MySQL 8.0+
-- ============================================================
CREATE DATABASE IF NOT EXISTS `linlintong_greenfin` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `linlintong_greenfin`;

-- ====================== 1. 用户与角色 ======================
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL COMMENT '用户名/账号',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '姓名/机构名称',
    `role` VARCHAR(30) NOT NULL DEFAULT '林农' COMMENT '角色：林农/合作社/企业/林业管理部门/金融机构/评估师/评估机构/律师/法律援助机构/管理员',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `id_card` VARCHAR(30) DEFAULT NULL COMMENT '身份证号/统一社会信用代码',
    `region` VARCHAR(100) DEFAULT NULL COMMENT '所在区域',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '详细地址',
    `org_type` VARCHAR(50) DEFAULT NULL COMMENT '机构类型',
    `business_scope` VARCHAR(500) DEFAULT NULL COMMENT '经营/业务范围',
    `qualification_url` VARCHAR(500) DEFAULT NULL COMMENT '资质证明',
    `intro` TEXT COMMENT '简介',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1启用 0禁用',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB COMMENT='用户表';

-- ====================== 2. 新闻动态 ======================
DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '分类：行业动态/平台公告/案例分享/技术资讯',
    `cover` VARCHAR(500) DEFAULT NULL COMMENT '封面',
    `summary` VARCHAR(500) DEFAULT NULL COMMENT '摘要',
    `content` LONGTEXT COMMENT '正文(支持markdown/html)',
    `author` VARCHAR(100) DEFAULT NULL COMMENT '作者',
    `source` VARCHAR(200) DEFAULT NULL COMMENT '来源',
    `view_count` INT DEFAULT 0,
    `is_top` TINYINT DEFAULT 0,
    `published_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`),
    KEY `idx_published_time` (`published_time`)
) ENGINE=InnoDB COMMENT='新闻动态表';

-- ====================== 3. 政策解读 ======================
DROP TABLE IF EXISTS `t_policy`;
CREATE TABLE `t_policy` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL COMMENT '政策标题',
    `level` VARCHAR(30) DEFAULT NULL COMMENT '级别：国家级/省级/市级/县级',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '分类：林业改革/碳汇交易/绿色金融/生态保护',
    `issue_org` VARCHAR(200) DEFAULT NULL COMMENT '发布机构',
    `issue_no` VARCHAR(100) DEFAULT NULL COMMENT '文号',
    `issue_date` DATE DEFAULT NULL COMMENT '发布日期',
    `summary` VARCHAR(1000) DEFAULT NULL COMMENT '摘要',
    `content` LONGTEXT COMMENT '正文',
    `interpretation` LONGTEXT COMMENT '政策解读',
    `attachment_url` VARCHAR(500) DEFAULT NULL COMMENT '附件',
    `view_count` INT DEFAULT 0,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_level` (`level`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB COMMENT='政策解读表';

-- ====================== 4. 用户问题反馈 ======================
DROP TABLE IF EXISTS `t_feedback`;
CREATE TABLE `t_feedback` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `type` VARCHAR(30) NOT NULL COMMENT '类型：操作咨询/意见建议/投诉举报/业务求助',
    `content` TEXT NOT NULL COMMENT '反馈内容',
    `attachment_url` VARCHAR(500) DEFAULT NULL COMMENT '佐证材料',
    `contact_name` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `contact_email` VARCHAR(100) DEFAULT NULL COMMENT '联系邮箱',
    `status` VARCHAR(20) DEFAULT '待处理' COMMENT '状态：待处理/处理中/已回复/已关闭',
    `reply_content` TEXT COMMENT '回复内容',
    `replied_by` VARCHAR(50) DEFAULT NULL COMMENT '回复人',
    `replied_time` DATETIME DEFAULT NULL,
    `user_id` BIGINT DEFAULT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB COMMENT='用户反馈表';

-- ====================== 5. 信息披露(企业项目) ======================
DROP TABLE IF EXISTS `t_disclosure`;
CREATE TABLE `t_disclosure` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `enterprise_id` BIGINT NOT NULL COMMENT '企业用户ID',
    `enterprise_name` VARCHAR(200) NOT NULL COMMENT '企业名称',
    `project_name` VARCHAR(200) NOT NULL COMMENT '项目名称',
    `project_period` VARCHAR(100) DEFAULT NULL COMMENT '项目周期',
    `cover` VARCHAR(500) DEFAULT NULL COMMENT '封面',
    `progress` VARCHAR(1000) DEFAULT NULL COMMENT '项目进展',
    `protection` TEXT COMMENT '生态保护措施',
    `operation` TEXT COMMENT '运营管理',
    `restoration` TEXT COMMENT '生态修复落实',
    `images` TEXT COMMENT '项目图片(逗号分隔URL)',
    `attachments` TEXT COMMENT '附件',
    `last_disclosure_time` DATETIME DEFAULT NULL COMMENT '上次披露时间',
    `next_due_time` DATETIME DEFAULT NULL COMMENT '下次披露截止',
    `warn_status` VARCHAR(20) DEFAULT '正常' COMMENT '预警状态：正常/即将到期/已逾期',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_enterprise_id` (`enterprise_id`),
    KEY `idx_warn_status` (`warn_status`)
) ENGINE=InnoDB COMMENT='企业信息披露表';

-- ====================== 6. 收储登记资源 ======================
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `resource_no` VARCHAR(50) NOT NULL COMMENT '资源编号',
    `resource_name` VARCHAR(200) NOT NULL COMMENT '资源名称',
    `resource_type` VARCHAR(50) NOT NULL COMMENT '类型：林地/碳汇/水库/林下产品',
    `forest_type` VARCHAR(50) DEFAULT NULL COMMENT '林木类型：商品林/公益林/经济林/混交林',
    `area_mu` DECIMAL(12,2) DEFAULT NULL COMMENT '面积(亩)',
    `carbon_storage` DECIMAL(12,2) DEFAULT NULL COMMENT '碳汇储量(吨)',
    `region` VARCHAR(100) DEFAULT NULL COMMENT '行政区域',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '详细地址',
    `longitude` DECIMAL(10,6) DEFAULT NULL,
    `latitude` DECIMAL(10,6) DEFAULT NULL,
    `boundary` TEXT COMMENT '边界(GeoJSON坐标串)',
    `owner_name` VARCHAR(100) DEFAULT NULL COMMENT '权属人',
    `owner_id_card` VARCHAR(30) DEFAULT NULL COMMENT '权属证号/身份证',
    `cert_no` VARCHAR(100) DEFAULT NULL COMMENT '权属证明编号',
    `contract_url` VARCHAR(500) DEFAULT NULL COMMENT '权属证明/承包合同',
    `cover` VARCHAR(500) DEFAULT NULL COMMENT '资源照片',
    `images` TEXT COMMENT '多图',
    `intro` TEXT COMMENT '资源介绍',
    `status` VARCHAR(20) DEFAULT '待审核' COMMENT '状态：待审核/已收储/已驳回',
    `audit_msg` VARCHAR(500) DEFAULT NULL COMMENT '审核意见',
    `audit_by` VARCHAR(50) DEFAULT NULL,
    `audit_time` DATETIME DEFAULT NULL,
    `user_id` BIGINT DEFAULT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_resource_no` (`resource_no`),
    KEY `idx_resource_type` (`resource_type`),
    KEY `idx_region` (`region`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB COMMENT='生态资源(收储登记)表';

-- ====================== 7. 评估机构 ======================
DROP TABLE IF EXISTS `t_evaluation_org`;
CREATE TABLE `t_evaluation_org` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `org_name` VARCHAR(200) NOT NULL COMMENT '机构名称',
    `business_license` VARCHAR(200) DEFAULT NULL COMMENT '营业执照',
    `business_scope` VARCHAR(500) DEFAULT NULL COMMENT '从业范围',
    `office_address` VARCHAR(200) DEFAULT NULL COMMENT '办公地址',
    `phone` VARCHAR(50) DEFAULT NULL,
    `email` VARCHAR(100) DEFAULT NULL,
    `website` VARCHAR(200) DEFAULT NULL COMMENT '官方网址',
    `logo` VARCHAR(500) DEFAULT NULL,
    `qualification_url` VARCHAR(500) DEFAULT NULL,
    `intro` TEXT COMMENT '机构简介',
    `expertise` VARCHAR(500) DEFAULT NULL COMMENT '专业领域',
    `evaluator_count` INT DEFAULT 0 COMMENT '评估师数量',
    `case_count` INT DEFAULT 0 COMMENT '案例数',
    `rating` DECIMAL(3,1) DEFAULT 5.0 COMMENT '评分',
    `status` TINYINT DEFAULT 1 COMMENT '入驻状态',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='评估机构表';

-- ====================== 8. 评估师 ======================
DROP TABLE IF EXISTS `t_evaluator`;
CREATE TABLE `t_evaluator` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `org_id` BIGINT DEFAULT NULL COMMENT '所属机构ID',
    `org_name` VARCHAR(200) DEFAULT NULL,
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` VARCHAR(10) DEFAULT NULL,
    `avatar` VARCHAR(500) DEFAULT NULL,
    `cert_no` VARCHAR(100) DEFAULT NULL COMMENT '执业证书编号',
    `years` INT DEFAULT 0 COMMENT '从业年限',
    `expertise` VARCHAR(500) DEFAULT NULL COMMENT '专业领域',
    `qualification` VARCHAR(500) DEFAULT NULL COMMENT '从业资质',
    `intro` TEXT,
    `phone` VARCHAR(20) DEFAULT NULL,
    `email` VARCHAR(100) DEFAULT NULL,
    `case_count` INT DEFAULT 0,
    `rating` DECIMAL(3,1) DEFAULT 5.0,
    `status` TINYINT DEFAULT 1,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_org_id` (`org_id`)
) ENGINE=InnoDB COMMENT='评估师表';

-- ====================== 9. 评估申请 ======================
DROP TABLE IF EXISTS `t_evaluation_apply`;
CREATE TABLE `t_evaluation_apply` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `apply_no` VARCHAR(50) NOT NULL COMMENT '申请编号',
    `applicant_id` BIGINT DEFAULT NULL,
    `applicant_name` VARCHAR(100) DEFAULT NULL,
    `applicant_phone` VARCHAR(20) DEFAULT NULL,
    `org_id` BIGINT DEFAULT NULL,
    `org_name` VARCHAR(200) DEFAULT NULL,
    `evaluator_id` BIGINT DEFAULT NULL,
    `evaluator_name` VARCHAR(50) DEFAULT NULL,
    `resource_type` VARCHAR(50) DEFAULT NULL,
    `resource_id` BIGINT DEFAULT NULL,
    `resource_name` VARCHAR(200) DEFAULT NULL,
    `purpose` VARCHAR(200) DEFAULT NULL COMMENT '评估用途',
    `requirement` TEXT COMMENT '需求描述',
    `attachments` TEXT COMMENT '附件',
    `step` VARCHAR(50) DEFAULT '材料上报' COMMENT '当前步骤：材料上报/资质核实/勘察复评/结果公示/已完成',
    `status` VARCHAR(20) DEFAULT '待对接' COMMENT '状态：待对接/对接中/评估中/已完成/已驳回',
    `result_value` DECIMAL(15,2) DEFAULT NULL COMMENT '评估初步价值',
    `result_summary` TEXT COMMENT '评估结果说明',
    `result_report_url` VARCHAR(500) DEFAULT NULL COMMENT '评估报告',
    `feedback` TEXT COMMENT '机构反馈',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_apply_no` (`apply_no`)
) ENGINE=InnoDB COMMENT='评估申请表';

-- ====================== 10. 资源供给挂牌 ======================
DROP TABLE IF EXISTS `t_listing`;
CREATE TABLE `t_listing` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `listing_no` VARCHAR(50) NOT NULL,
    `title` VARCHAR(200) NOT NULL COMMENT '挂牌标题',
    `category` VARCHAR(50) NOT NULL COMMENT '一级分类：文化服务/供给服务/调节服务',
    `sub_category` VARCHAR(50) DEFAULT NULL COMMENT '二级分类：林地/林下产品/碳汇/水库/主题乐园/医疗养老',
    `cover` VARCHAR(500) DEFAULT NULL,
    `images` TEXT,
    `intro` TEXT COMMENT '资源简介',
    `ownership` VARCHAR(500) DEFAULT NULL COMMENT '权属介绍',
    `manage_org` VARCHAR(200) DEFAULT NULL COMMENT '管理单位',
    `region` VARCHAR(100) DEFAULT NULL,
    `area_mu` DECIMAL(12,2) DEFAULT NULL,
    `price` DECIMAL(15,2) DEFAULT NULL COMMENT '挂牌价格',
    `price_unit` VARCHAR(20) DEFAULT '元' COMMENT '价格单位',
    `valuation_min` DECIMAL(15,2) DEFAULT NULL,
    `valuation_max` DECIMAL(15,2) DEFAULT NULL,
    `expire_time` DATETIME DEFAULT NULL COMMENT '挂牌到期时间',
    `accept_appointment` TINYINT DEFAULT 1 COMMENT '是否接受预约洽谈',
    `view_count` INT DEFAULT 0,
    `appointment_count` INT DEFAULT 0,
    `audit_status` VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态',
    `audit_msg` VARCHAR(500) DEFAULT NULL,
    `status` VARCHAR(20) DEFAULT '挂牌中' COMMENT '状态：挂牌中/已下架/已成交',
    `seller_id` BIGINT DEFAULT NULL,
    `seller_name` VARCHAR(100) DEFAULT NULL,
    `seller_phone` VARCHAR(20) DEFAULT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_listing_no` (`listing_no`),
    KEY `idx_category` (`category`),
    KEY `idx_status` (`status`),
    KEY `idx_seller_id` (`seller_id`)
) ENGINE=InnoDB COMMENT='资源供给挂牌表';

-- ====================== 11. 资源需求发布 ======================
DROP TABLE IF EXISTS `t_demand`;
CREATE TABLE `t_demand` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `demand_no` VARCHAR(50) NOT NULL,
    `title` VARCHAR(200) NOT NULL,
    `category` VARCHAR(50) NOT NULL COMMENT '类别：开发类/采购类/合作类',
    `sub_category` VARCHAR(50) DEFAULT NULL COMMENT '子类：林地开发/碳汇采购/林木收购/林下产品采购/生态项目合作',
    `cover` VARCHAR(500) DEFAULT NULL,
    `intro` TEXT COMMENT '需求详情',
    `target_region` VARCHAR(200) DEFAULT NULL COMMENT '目标区域',
    `budget_min` DECIMAL(15,2) DEFAULT NULL,
    `budget_max` DECIMAL(15,2) DEFAULT NULL,
    `area_demand` DECIMAL(12,2) DEFAULT NULL COMMENT '需求面积',
    `expire_time` DATETIME DEFAULT NULL,
    `accept_appointment` TINYINT DEFAULT 1,
    `view_count` INT DEFAULT 0,
    `appointment_count` INT DEFAULT 0,
    `audit_status` VARCHAR(20) DEFAULT '待审核',
    `status` VARCHAR(20) DEFAULT '征集中' COMMENT '状态：征集中/已截止/已对接',
    `buyer_id` BIGINT DEFAULT NULL,
    `buyer_name` VARCHAR(100) DEFAULT NULL,
    `buyer_phone` VARCHAR(20) DEFAULT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_demand_no` (`demand_no`),
    KEY `idx_category` (`category`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB COMMENT='资源需求发布表';

-- ====================== 12. 预约洽谈 ======================
DROP TABLE IF EXISTS `t_appointment`;
CREATE TABLE `t_appointment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `target_type` VARCHAR(20) NOT NULL COMMENT '目标类型：listing/demand',
    `target_id` BIGINT NOT NULL,
    `target_title` VARCHAR(200) DEFAULT NULL,
    `applicant_id` BIGINT DEFAULT NULL,
    `applicant_name` VARCHAR(100) DEFAULT NULL,
    `applicant_phone` VARCHAR(20) DEFAULT NULL,
    `applicant_org` VARCHAR(200) DEFAULT NULL,
    `meet_type` VARCHAR(20) DEFAULT NULL COMMENT '洽谈方式：线上/线下',
    `meet_time` DATETIME DEFAULT NULL,
    `meet_place` VARCHAR(200) DEFAULT NULL,
    `message` TEXT COMMENT '留言',
    `reply` TEXT,
    `status` VARCHAR(20) DEFAULT '待回复' COMMENT '状态：待回复/已接受/已拒绝/已完成',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_target` (`target_type`, `target_id`),
    KEY `idx_applicant_id` (`applicant_id`)
) ENGINE=InnoDB COMMENT='预约洽谈表';

-- ====================== 13. 地方特色农产品 ======================
DROP TABLE IF EXISTS `t_farm_product`;
CREATE TABLE `t_farm_product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(200) NOT NULL COMMENT '产品名称',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '类别：林下中药材/山林果蔬/山野特产/茶叶/食用菌',
    `cover` VARCHAR(500) DEFAULT NULL,
    `images` TEXT,
    `intro` TEXT,
    `origin` VARCHAR(200) DEFAULT NULL COMMENT '产地',
    `cooperative` VARCHAR(200) DEFAULT NULL COMMENT '所属合作社',
    `cooperative_id` BIGINT DEFAULT NULL,
    `unit` VARCHAR(20) DEFAULT '斤',
    `price` DECIMAL(10,2) DEFAULT NULL COMMENT '单价',
    `stock` INT DEFAULT 0,
    `sales` INT DEFAULT 0,
    `tags` VARCHAR(200) DEFAULT NULL COMMENT '标签：源头直供/产地直销/有机/绿色',
    `status` TINYINT DEFAULT 1 COMMENT '上架状态',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB COMMENT='地方特色农产品表';

-- ====================== 14. 农产品订单 ======================
DROP TABLE IF EXISTS `t_product_order`;
CREATE TABLE `t_product_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_no` VARCHAR(50) NOT NULL,
    `product_id` BIGINT NOT NULL,
    `product_name` VARCHAR(200) DEFAULT NULL,
    `cover` VARCHAR(500) DEFAULT NULL,
    `unit_price` DECIMAL(10,2) DEFAULT NULL,
    `quantity` INT DEFAULT 1,
    `total_price` DECIMAL(15,2) DEFAULT NULL,
    `buyer_id` BIGINT DEFAULT NULL,
    `buyer_name` VARCHAR(100) DEFAULT NULL,
    `buyer_phone` VARCHAR(20) DEFAULT NULL,
    `address` VARCHAR(300) DEFAULT NULL,
    `status` VARCHAR(20) DEFAULT '待发货' COMMENT '状态：待支付/待发货/已发货/已完成/已取消',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB COMMENT='农产品订单';

-- ====================== 15. 金融产品 ======================
DROP TABLE IF EXISTS `t_finance_product`;
CREATE TABLE `t_finance_product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `product_code` VARCHAR(50) DEFAULT NULL,
    `name` VARCHAR(200) NOT NULL COMMENT '产品名称',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '类别：碳汇收储贷/两山农林贷/绿色工厂贷/碳中和助力贷/两山乡居贷',
    `bank_name` VARCHAR(200) DEFAULT NULL COMMENT '所属银行',
    `bank_logo` VARCHAR(500) DEFAULT NULL,
    `bank_id` BIGINT DEFAULT NULL,
    `cover` VARCHAR(500) DEFAULT NULL,
    `loan_min` DECIMAL(15,2) DEFAULT NULL COMMENT '贷款额度下限',
    `loan_max` DECIMAL(15,2) DEFAULT NULL,
    `rate_min` DECIMAL(6,3) DEFAULT NULL COMMENT '年利率下限(%)',
    `rate_max` DECIMAL(6,3) DEFAULT NULL,
    `term_min` INT DEFAULT NULL COMMENT '贷款期限下限(月)',
    `term_max` INT DEFAULT NULL,
    `accept_time` VARCHAR(100) DEFAULT NULL COMMENT '受理时间',
    `apply_condition` TEXT COMMENT '申请条件',
    `apply_materials` TEXT COMMENT '所需材料',
    `apply_process` TEXT COMMENT '办理流程',
    `target_users` VARCHAR(200) DEFAULT NULL COMMENT '适用对象',
    `tags` VARCHAR(200) DEFAULT NULL,
    `intro` TEXT,
    `apply_count` INT DEFAULT 0,
    `status` TINYINT DEFAULT 1,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`),
    KEY `idx_bank_id` (`bank_id`)
) ENGINE=InnoDB COMMENT='金融产品表';

-- ====================== 16. 贷款申请 ======================
DROP TABLE IF EXISTS `t_loan_apply`;
CREATE TABLE `t_loan_apply` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `apply_no` VARCHAR(50) NOT NULL,
    `product_id` BIGINT DEFAULT NULL,
    `product_name` VARCHAR(200) DEFAULT NULL,
    `bank_id` BIGINT DEFAULT NULL,
    `bank_name` VARCHAR(200) DEFAULT NULL,
    `applicant_id` BIGINT DEFAULT NULL,
    `applicant_name` VARCHAR(100) DEFAULT NULL,
    `applicant_phone` VARCHAR(20) DEFAULT NULL,
    `applicant_role` VARCHAR(30) DEFAULT NULL,
    `loan_amount` DECIMAL(15,2) DEFAULT NULL,
    `loan_term` INT DEFAULT NULL COMMENT '期望期限(月)',
    `purpose` VARCHAR(500) DEFAULT NULL COMMENT '融资用途',
    `collateral` VARCHAR(500) DEFAULT NULL COMMENT '抵押物',
    `applicant_info` TEXT COMMENT '申请人基本信息',
    `materials` TEXT COMMENT '上传材料JSON',
    `step` VARCHAR(30) DEFAULT '已提交' COMMENT '步骤：已提交/初审中/复审中/审批中/已批准/已拒绝/已放款',
    `status` VARCHAR(20) DEFAULT '审批中',
    `review_msg` TEXT COMMENT '审批意见',
    `reviewer_id` BIGINT DEFAULT NULL,
    `reviewer_name` VARCHAR(50) DEFAULT NULL,
    `reviewed_time` DATETIME DEFAULT NULL,
    `approved_amount` DECIMAL(15,2) DEFAULT NULL,
    `approved_rate` DECIMAL(6,3) DEFAULT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_apply_no` (`apply_no`),
    KEY `idx_status` (`status`),
    KEY `idx_applicant_id` (`applicant_id`),
    KEY `idx_bank_id` (`bank_id`)
) ENGINE=InnoDB COMMENT='贷款申请表';

-- ====================== 17. 法律援助机构 ======================
DROP TABLE IF EXISTS `t_legal_org`;
CREATE TABLE `t_legal_org` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `org_name` VARCHAR(200) NOT NULL,
    `org_type` VARCHAR(50) DEFAULT NULL COMMENT '类型：公益法律援助中心/高校法律援助队/专业律所',
    `logo` VARCHAR(500) DEFAULT NULL,
    `address` VARCHAR(200) DEFAULT NULL,
    `service_area` VARCHAR(200) DEFAULT NULL COMMENT '服务区域',
    `expertise` VARCHAR(500) DEFAULT NULL,
    `phone` VARCHAR(50) DEFAULT NULL,
    `email` VARCHAR(100) DEFAULT NULL,
    `qualification_url` VARCHAR(500) DEFAULT NULL,
    `intro` TEXT,
    `lawyer_count` INT DEFAULT 0,
    `case_count` INT DEFAULT 0,
    `rating` DECIMAL(3,1) DEFAULT 5.0,
    `status` TINYINT DEFAULT 1,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='法律援助机构表';

-- ====================== 18. 律师 ======================
DROP TABLE IF EXISTS `t_lawyer`;
CREATE TABLE `t_lawyer` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `org_id` BIGINT DEFAULT NULL,
    `org_name` VARCHAR(200) DEFAULT NULL,
    `name` VARCHAR(50) NOT NULL,
    `gender` VARCHAR(10) DEFAULT NULL,
    `avatar` VARCHAR(500) DEFAULT NULL,
    `cert_no` VARCHAR(100) DEFAULT NULL COMMENT '执业证号',
    `years` INT DEFAULT 0,
    `expertise` VARCHAR(500) DEFAULT NULL,
    `service_area` VARCHAR(200) DEFAULT NULL,
    `intro` TEXT,
    `phone` VARCHAR(20) DEFAULT NULL,
    `email` VARCHAR(100) DEFAULT NULL,
    `case_count` INT DEFAULT 0,
    `rating` DECIMAL(3,1) DEFAULT 5.0,
    `status` TINYINT DEFAULT 1,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_org_id` (`org_id`)
) ENGINE=InnoDB COMMENT='律师表';

-- ====================== 19. 法律咨询申请 ======================
DROP TABLE IF EXISTS `t_legal_consult`;
CREATE TABLE `t_legal_consult` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `consult_no` VARCHAR(50) NOT NULL,
    `consult_type` VARCHAR(30) DEFAULT NULL COMMENT '咨询类型：区域援助/专属咨询',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '法律类型：合同审查/权属纠纷/侵权维权/林权登记',
    `urgency` VARCHAR(20) DEFAULT '一般' COMMENT '紧急程度：紧急/一般/不急',
    `applicant_id` BIGINT DEFAULT NULL,
    `applicant_name` VARCHAR(100) DEFAULT NULL,
    `applicant_phone` VARCHAR(20) DEFAULT NULL,
    `applicant_region` VARCHAR(200) DEFAULT NULL,
    `target_org_id` BIGINT DEFAULT NULL,
    `target_org_name` VARCHAR(200) DEFAULT NULL,
    `target_lawyer_id` BIGINT DEFAULT NULL,
    `target_lawyer_name` VARCHAR(50) DEFAULT NULL,
    `description` TEXT COMMENT '咨询详情',
    `attachments` TEXT,
    `prev_handling` TEXT COMMENT '此前对接情况(专属咨询)',
    `status` VARCHAR(20) DEFAULT '待对接' COMMENT '状态：待对接/已对接/服务中/已结案',
    `feedback` TEXT,
    `service_record` TEXT,
    `service_fee` DECIMAL(10,2) DEFAULT NULL COMMENT '服务费',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_consult_no` (`consult_no`),
    KEY `idx_status` (`status`),
    KEY `idx_applicant_id` (`applicant_id`)
) ENGINE=InnoDB COMMENT='法律咨询申请表';

-- ====================== 20. 系统统计快照（看板用） ======================
DROP TABLE IF EXISTS `t_stats_daily`;
CREATE TABLE `t_stats_daily` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `stats_date` DATE NOT NULL,
    `resource_count` INT DEFAULT 0,
    `resource_area` DECIMAL(15,2) DEFAULT 0,
    `evaluation_count` INT DEFAULT 0,
    `listing_count` INT DEFAULT 0,
    `demand_count` INT DEFAULT 0,
    `loan_count` INT DEFAULT 0,
    `loan_amount` DECIMAL(15,2) DEFAULT 0,
    `consult_count` INT DEFAULT 0,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_stats_date` (`stats_date`)
) ENGINE=InnoDB COMMENT='每日统计';

-- ============================================================
-- 示例数据
-- ============================================================

-- 用户：管理员、林农、合作社、企业、银行、评估师、律师
INSERT INTO `t_user` (`username`, `password`, `real_name`, `role`, `avatar`, `phone`, `email`, `region`, `address`, `org_type`, `business_scope`, `intro`) VALUES
('admin',     '123456', '系统管理员', '管理员', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', '13800000000', 'admin@lly.cn', '湖北省武汉市', '武汉市洪山区珞喻路 100 号', NULL, NULL, '平台运营管理员'),
('linnong01', '123456', '张志强', '林农', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhang', '13800000001', 'zhang@lly.cn', '湖北省宜昌市五峰县', '五峰县渔洋关镇汉马池村', NULL, NULL, '五峰本地林农，自营毛竹林 80 亩'),
('linnong02', '123456', '李建华', '林农', 'https://api.dicebear.com/7.x/avataaars/svg?seed=li', '13800000002', 'li@lly.cn', '湖北省恩施州', '恩施州恩施市舞阳街道', NULL, NULL, '恩施茶林经营户'),
('coop01',    '123456', '五峰竹海合作社', '合作社', 'https://api.dicebear.com/7.x/avataaars/svg?seed=coop', '13800000003', 'coop@lly.cn', '湖北省宜昌市五峰县', '五峰县长乐坪镇', '林业专业合作社', '毛竹销售、林下经济、生态旅游', '由 36 户林农组成的省级示范合作社'),
('coop02',    '123456', '恩施富硒茶合作社', '合作社', 'https://api.dicebear.com/7.x/avataaars/svg?seed=coop2', '13800000004', NULL, '湖北省恩施州', '恩施州恩施市芭蕉镇', '林业专业合作社', '恩施富硒茶种植销售', '恩施老牌富硒茶合作社，120 户成员'),
('ent01',     '123456', '宜昌绿源生态科技有限公司', '企业', 'https://api.dicebear.com/7.x/avataaars/svg?seed=ent', '13800000005', 'ent@lly.cn', '湖北省宜昌市五峰县', '五峰县渔洋关镇', '生态开发企业', '生态项目开发、碳汇交易', '专注林业碳汇开发的高新技术企业'),
('ent02',     '123456', '恩施林下经济产业有限公司', '企业', 'https://api.dicebear.com/7.x/avataaars/svg?seed=ent2', '13800000006', 'ent2@lly.cn', '湖北省恩施州', '恩施州利川市', '林下经济企业', '林下中药材种植与深加工', '省级农业龙头企业'),
('bank01',    '123456', '湖北宜昌农商银行', '金融机构', 'https://api.dicebear.com/7.x/avataaars/svg?seed=bank1', '0717-5012345', 'aj@bank.cn', '湖北省宜昌市五峰县', '五峰县渔洋关镇金融大厦', '银行', '绿色金融、涉农信贷', '宜昌本地农商银行'),
('bank02',    '123456', '湖北恩施建设银行', '金融机构', 'https://api.dicebear.com/7.x/avataaars/svg?seed=bank2', '0718-8612345', 'np@ccb.cn', '湖北省恩施州', '恩施州恩施市', '银行', '绿色金融、企业贷款', '恩施州重点合作银行'),
('forestry01','123456', '宜昌市林业局', '林业管理部门', 'https://api.dicebear.com/7.x/avataaars/svg?seed=forestry', '0717-5099999', NULL, '湖北省宜昌市五峰县', '五峰县政府大楼', '政府部门', '林业资源管理、监督审核', '宜昌市林业行政主管部门'),
('eorg01',    '123456', '华中林评估事务所', '评估机构', 'https://api.dicebear.com/7.x/avataaars/svg?seed=eorg1', '027-8888888', 'zlpg@eval.cn', '湖北省武汉市', '武汉市洪山区', '评估机构', '林木资源评估、碳汇价值评估', '国家级专业评估机构'),
('evaluator01','123456','王志远', '评估师', 'https://api.dicebear.com/7.x/avataaars/svg?seed=ev1', '13900000001', NULL, '湖北省武汉市', NULL, NULL, NULL, '资深林业评估师，从业 18 年'),
('legalorg01','123456','五峰公益法律援助中心', '法律援助机构', 'https://api.dicebear.com/7.x/avataaars/svg?seed=legal1', '0717-5099000', 'legal@aj.cn', '湖北省宜昌市五峰县', '五峰县司法局大楼', '公益法律援助中心', '林权纠纷、生态侵权', '五峰县司法局直属公益机构'),
('lawyer01',  '123456', '陈律师', '律师', 'https://api.dicebear.com/7.x/avataaars/svg?seed=lawyer1', '13900000002', NULL, '湖北省武汉市', NULL, NULL, NULL, '环境与林业法专业律师，执业 15 年');

-- 新闻动态
INSERT INTO `t_news` (`title`, `category`, `cover`, `summary`, `content`, `author`, `source`, `view_count`, `is_top`, `published_time`) VALUES
('宜昌市率先完成 3.2 万亩林业碳汇收储登记', '行业动态', 'https://images.unsplash.com/photo-1448375240586-882707db888b?w=800', '宜昌作为湖北省生态文明建设标杆，本月完成首批 3.2 万亩林业碳汇资源统一登记入库，成为全省样板。', '## 宜昌率先完成林业碳汇收储\n\n宜昌市在全省率先完成首批 3.2 万亩林业碳汇资源的统一登记入库……', '林林通编辑部', '林林通·绿融通', 1284, 1, '2026-04-20 09:00:00'),
('国家林业碳汇专项贷款额度提升至 500 亿元', '行业动态', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=800', '国家林草局联合人民银行近日发布通知，将林业碳汇专项贷款额度由 200 亿元提升至 500 亿元，惠及全国 28 个试点县。', '## 国家专项贷款额度提升\n\n为进一步推动林业生态产业发展……', '生态金融日报', '林业部官网', 982, 1, '2026-04-15 10:30:00'),
('林林通·绿融通正式接入恩施州"一县一数库"', '平台公告', 'https://images.unsplash.com/photo-1418065460487-3e41a6c84dc5?w=800', '本平台与恩施州林业一体化数据库完成对接，恩施州林农可通过本平台一站办理林权登记、评估、交易、融资。', '## 接入恩施一县一数库\n\n经多轮技术对接……', '林林通团队', '平台公告', 521, 0, '2026-04-10 14:00:00'),
('案例分享：恩施茶林户王大爷的"碳汇 + 茶"增收路', '案例分享', 'https://images.unsplash.com/photo-1465146344425-f00d5f5c8f07?w=800', '恩施市芭蕉镇林农王大爷依托 60 亩恩施玉露茶林申请碳汇收储贷，年增收 8 万元。', '## 茶林户的增收故事\n\n王大爷今年 62 岁，世代经营 60 亩恩施玉露茶林……', '记者陈蓉', '恩施日报', 765, 0, '2026-04-08 16:00:00'),
('技术专栏：基于 GIS 的森林资源一张图建设要点', '技术资讯', 'https://images.unsplash.com/photo-1502082553048-f009c37129b9?w=800', '本文从数据采集、坐标转换、图层叠加、性能优化四个方面，介绍森林资源一张图的建设要点。', '## GIS 一张图建设要点\n\n…', '技术团队', '林林通技术博客', 412, 0, '2026-04-05 11:00:00'),
('政策解读：新版《林业碳汇项目方法学》要点速览', '政策解读', 'https://images.unsplash.com/photo-1574482620881-484922f8b1c3?w=800', '解读新版方法学的 5 大变化，重点关注监测期和核证频次的调整。', '## 方法学要点解读\n\n…', '政策研究室', '林业部', 632, 0, '2026-04-03 09:00:00');

-- 政策解读
INSERT INTO `t_policy` (`title`, `level`, `category`, `issue_org`, `issue_no`, `issue_date`, `summary`, `content`, `interpretation`) VALUES
('关于深化集体林权制度改革的意见', '国家级', '林业改革', '中共中央办公厅、国务院办公厅', '中办发〔2026〕12 号', '2026-03-15', '深化"三权分置"，完善流转交易市场，激发林地经营活力。', '## 主要内容\n…', '## 政策解读\n本次改革核心是"三权分置"……'),
('林业碳汇交易管理暂行办法', '国家级', '碳汇交易', '国家林草局', '林草发〔2026〕8 号', '2026-02-28', '规范林业碳汇项目开发、核证与交易流程，明确各方责任。', '## 暂行办法\n…', '## 政策解读\n该办法首次系统规范了林业碳汇全链条……'),
('湖北省绿色金融服务乡村振兴专项行动方案', '省级', '绿色金融', '湖北省人民政府', '鄂政发〔2026〕5 号', '2026-03-01', '到 2027 年实现绿色信贷余额翻番，重点支持林业、海洋等生态产业。', '## 行动方案\n…', '## 政策解读\n方案聚焦"两山"实践、林业碳汇……'),
('五峰县关于支持林农参与生态银行的若干措施', '县级', '林业改革', '五峰县人民政府', '峰政办发〔2026〕18 号', '2026-03-20', '林农自主登记可享每亩 50 元一次性补贴，碳汇评估全程免费。', '## 若干措施\n…', '## 政策解读\n本次出台了三项实质性激励……'),
('湖北省林下经济高质量发展三年行动计划', '省级', '生态保护', '湖北省林业局', '鄂林〔2026〕10 号', '2026-02-10', '到 2028 年林下经济规模突破 1500 亿元，重点扶持中药材、食用菌等。', '## 行动计划\n…', '## 政策解读\n…');

-- 资源(已收储)
INSERT INTO `t_resource` (`resource_no`, `resource_name`, `resource_type`, `forest_type`, `area_mu`, `carbon_storage`, `region`, `address`, `longitude`, `latitude`, `owner_name`, `owner_id_card`, `cert_no`, `cover`, `intro`, `status`, `user_id`) VALUES
('LR2026001', '五峰汉马池村毛竹林一区', '林地', '商品林', 80.50, 320.80, '湖北省宜昌市递铺街道', '汉马池村二组', 110.6720, 30.1580, '张志强', '330523************0001', '林证字第 HB20260001 号', 'https://images.unsplash.com/photo-1448375240586-882707db888b?w=800', '位于汉马池村二组的毛竹林，林相整齐，树龄 8-12 年。', '已收储', 2),
('LR2026002', '五峰长乐坪镇集体毛竹林', '林地', '商品林', 320.00, 1280.00, '湖北省宜昌市孝丰镇', '长乐坪镇苏家河村', 110.5630, 30.1240, '五峰竹海合作社', '统一社会信用代码', '林证字第 HB20260002 号', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=800', '集体经营毛竹林，已通过 FSC 认证。', '已收储', 4),
('LR2026003', '恩施芭蕉恩施玉露茶林', '林地', '经济林', 60.00, 180.00, '湖北省恩施州恩施市', '芭蕉镇高拱桥村', 109.4880, 30.3000, '李建华', '350783************0002', '鄂鄂林证字第 ES20260003 号', 'https://images.unsplash.com/photo-1465146344425-f00d5f5c8f07?w=800', '百年玉露茶林，树龄 60+ 年，茶质上乘。', '已收储', 3),
('LR2026004', '宜昌绿源碳汇林项目区', '碳汇', NULL, 1200.00, 4800.00, '湖北省宜昌市', '渔洋关、长乐坪、湾潭三镇', 110.6000, 30.1500, '宜昌绿源生态科技有限公司', '统一社会信用代码', '碳证字第 HB20260004 号', 'https://images.unsplash.com/photo-1574482620881-484922f8b1c3?w=800', '已签发 CCER 林业碳汇 4800 吨/年。', '已收储', 6),
('LR2026005', '恩施林下经济示范基地', '林下产品', NULL, 480.00, NULL, '湖北省恩施州利川市', '利川市毛坝镇兰田村', 108.9400, 30.2900, '恩施林下经济产业有限公司', '统一社会信用代码', '鄂林证字第 ES20260005 号', 'https://images.unsplash.com/photo-1502082553048-f009c37129b9?w=800', '林下中药材(三叶青、灵芝)种植基地。', '已收储', 7),
('LR2026006', '五峰湾潭小水库', '水库', NULL, 95.00, NULL, '湖北省宜昌市天荒坪镇', '湾潭镇红渔坪村', 110.4500, 30.0800, '红渔坪村委会', '统一社会信用代码', '水证字第 HB20260006 号', 'https://images.unsplash.com/photo-1418065460487-3e41a6c84dc5?w=800', '红渔坪小水库，水质II类，可生态养殖。', '已收储', 4),
('LR2026007', '恩施芭蕉村经济林收储申请', '林地', '经济林', 38.00, 120.00, '湖北省恩施州恩施市', '芭蕉镇戍口村', 109.5200, 30.3200, '李建华', '350783************0002', NULL, 'https://images.unsplash.com/photo-1448375240586-882707db888b?w=800', '富林村油茶经济林，林农自主登记申请。', '待审核', 3);

-- 评估机构
INSERT INTO `t_evaluation_org` (`org_name`, `business_license`, `business_scope`, `office_address`, `phone`, `email`, `website`, `logo`, `intro`, `expertise`, `evaluator_count`, `case_count`, `rating`) VALUES
('华中林评估事务所', '91420500XXXX001', '林木资源、林权、碳汇资产评估', '武汉市洪山区珞喻路 152 号', '027-88888888', 'zlpg@eval.cn', 'https://www.zlpg.cn', 'https://api.dicebear.com/7.x/identicon/svg?seed=zlpg', '华中林评估事务所是国家级专业林业资产评估机构……', '林木资源评估、林权流转评估、碳汇价值评估', 28, 562, 4.9),
('鄂林资产评估有限公司', '91420000XXXX002', '森林资源资产评估、生态项目评估', '武汉市江汉区建设大道 568 号', '027-87654321', 'fl@eval.cn', 'https://www.flpg.cn', 'https://api.dicebear.com/7.x/identicon/svg?seed=flpg', '湖北省内领先的林业评估机构。', '林权评估、碳汇评估、生态项目评估', 18, 312, 4.8),
('华中生态资产评估中心', '91420100XXXX003', '生态资源价值评估、绿色金融评估', '武汉市东湖高新区光谷', '027-58888888', 'hd@eco.cn', 'https://www.hdeco.cn', 'https://api.dicebear.com/7.x/identicon/svg?seed=hdeco', '华中地区生态资产评估龙头。', '生态资源评估、ESG 评估', 35, 781, 4.95);

-- 评估师
INSERT INTO `t_evaluator` (`org_id`, `org_name`, `name`, `gender`, `avatar`, `cert_no`, `years`, `expertise`, `qualification`, `intro`, `phone`, `case_count`, `rating`) VALUES
(1, '华中林评估事务所', '王志远', '男', 'https://api.dicebear.com/7.x/avataaars/svg?seed=ev1', '林评字第 20060123 号', 18, '林木资源评估、碳汇评估', '注册资产评估师 (CPV)', '资深林业评估师，专注林业资产评估 18 年。', '13900000001', 156, 4.9),
(1, '华中林评估事务所', '刘晓敏', '女', 'https://api.dicebear.com/7.x/avataaars/svg?seed=ev2', '林评字第 20120456 号', 12, '林权流转评估、生态产品价值核算', '注册资产评估师 (CPV)、CCER 项目核证员', '专精林权流转及碳汇核算。', '13900000002', 98, 4.85),
(2, '鄂林资产评估有限公司', '陈建国', '男', 'https://api.dicebear.com/7.x/avataaars/svg?seed=ev3', '林评字第 20100789 号', 14, '鄂西山区林业资产评估', '注册资产评估师 (CPV)', '鄂西山区域专家。', '13900000003', 132, 4.8),
(3, '华中生态资产评估中心', '赵雪', '女', 'https://api.dicebear.com/7.x/avataaars/svg?seed=ev4', '林评字第 20140234 号', 10, '生态资源价值核算', '注册资产评估师 (CPV)', '生态资源价值核算专家。', '13900000004', 86, 4.92),
(3, '华中生态资产评估中心', '黄海涛', '男', 'https://api.dicebear.com/7.x/avataaars/svg?seed=ev5', '林评字第 20080567 号', 16, '碳汇评估、ESG 评估', '注册资产评估师 (CPV)、ESG 分析师', '碳汇与 ESG 双领域专家。', '13900000005', 174, 4.95);

-- 评估申请示例
INSERT INTO `t_evaluation_apply` (`apply_no`, `applicant_id`, `applicant_name`, `applicant_phone`, `org_id`, `org_name`, `evaluator_id`, `evaluator_name`, `resource_type`, `resource_id`, `resource_name`, `purpose`, `requirement`, `step`, `status`, `result_value`, `result_summary`) VALUES
('EA2026001', 2, '张志强', '13800000001', 1, '华中林评估事务所', 1, '王志远', '林地', 1, '五峰汉马池村毛竹林一区', '银行抵押贷款', '需评估当前市场价值用于贷款抵押', '已完成', '已完成', 1280000.00, '林相整齐，参考类似毛竹林近三年成交价格综合评定。'),
('EA2026002', 7, '恩施林下经济产业有限公司', '13800000006', 2, '鄂林资产评估有限公司', 3, '陈建国', '林下产品', 5, '恩施林下经济示范基地', 'CCER 项目申报', '林下中药材项目价值评估', '勘察复评', '评估中', NULL, NULL),
('EA2026003', 6, '宜昌绿源生态科技有限公司', '13800000005', 3, '华中生态资产评估中心', 5, '黄海涛', '碳汇', 4, '宜昌绿源碳汇林项目区', '碳汇交易上市', '碳汇资产价值核算', '资质核实', '评估中', NULL, NULL);

-- 资源供给挂牌
INSERT INTO `t_listing` (`listing_no`, `title`, `category`, `sub_category`, `cover`, `intro`, `ownership`, `manage_org`, `region`, `area_mu`, `price`, `valuation_min`, `valuation_max`, `expire_time`, `seller_id`, `seller_name`, `seller_phone`, `view_count`) VALUES
('LIST2026001', '五峰汉马池村毛竹林一区(80.5亩) - 经营权流转', '供给服务', '林地', 'https://images.unsplash.com/photo-1448375240586-882707db888b?w=800', '位于五峰汉马池村，林相整齐，年产毛竹约 1.6 万根。', '林农自有，权属清晰', '五峰竹海合作社', '湖北省宜昌市', 80.50, 1280000, 1200000, 1380000, '2026-12-31 23:59:59', 2, '张志强', '13800000001', 138),
('LIST2026002', '恩施玉露茶林(60亩) - 整体收购', '供给服务', '林地', 'https://images.unsplash.com/photo-1465146344425-f00d5f5c8f07?w=800', '60 亩百年玉露茶林，年产干茶约 480 斤。', '林农自有', '恩施富硒茶合作社', '湖北省恩施州恩施市', 60.00, 2880000, 2500000, 3200000, '2026-12-31 23:59:59', 3, '李建华', '13800000002', 215),
('LIST2026003', '宜昌绿源 CCER 林业碳汇 - 4800 吨/年', '调节服务', '碳汇', 'https://images.unsplash.com/photo-1574482620881-484922f8b1c3?w=800', '已签发 CCER 林业碳汇 4800 吨/年，可拆分销售。', '宜昌绿源生态科技有限公司', '宜昌市林业局', '湖北省宜昌市', NULL, 384000, 360000, 480000, '2026-10-31 23:59:59', 6, '宜昌绿源生态科技有限公司', '13800000005', 312),
('LIST2026004', '五峰湾潭小水库 - 生态养殖租赁', '供给服务', '水库', 'https://images.unsplash.com/photo-1418065460487-3e41a6c84dc5?w=800', '红渔坪小水库，水质II类，可生态养殖。', '红渔坪村委会', '五峰县湾潭镇政府', '湖北省宜昌市', 95.00, 180000, 150000, 200000, '2027-03-31 23:59:59', 4, '五峰竹海合作社', '13800000003', 92),
('LIST2026005', '恩施利川林下中药材基地 - 合作经营', '供给服务', '林下产品', 'https://images.unsplash.com/photo-1502082553048-f009c37129b9?w=800', '480 亩林下中药材基地，三叶青/灵芝/铁皮石斛。', '恩施林下经济产业有限公司', '利川市林业局', '湖北省恩施州', 480.00, 950000, 880000, 1050000, '2026-09-30 23:59:59', 7, '恩施林下经济产业有限公司', '13800000006', 158),
('LIST2026006', '五峰湾潭山地民宿主题乐园 - 项目转让', '文化服务', '主题乐园', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=800', '五峰湾潭 200 亩山地综合体，可改造为森林康养主题乐园。', '宜昌绿源生态科技有限公司', '宜昌绿源生态科技', '湖北省宜昌市', 200.00, 4500000, 4200000, 5000000, '2026-08-31 23:59:59', 6, '宜昌绿源生态科技有限公司', '13800000005', 78);

-- 资源需求发布
INSERT INTO `t_demand` (`demand_no`, `title`, `category`, `sub_category`, `cover`, `intro`, `target_region`, `budget_min`, `budget_max`, `area_demand`, `expire_time`, `buyer_id`, `buyer_name`, `buyer_phone`, `view_count`) VALUES
('DM2026001', '收购鄂西地区林业碳汇 5000 吨', '采购类', '碳汇采购', 'https://images.unsplash.com/photo-1574482620881-484922f8b1c3?w=800', '某 ESG 上市公司年度碳汇采购需求 5000 吨。', '湖北省西部', 350000, 450000, NULL, '2026-09-30 23:59:59', 6, '宜昌绿源生态科技', '13800000005', 158),
('DM2026002', '寻求鄂西地区 200 亩茶林合作经营', '合作类', '生态项目合作', 'https://images.unsplash.com/photo-1465146344425-f00d5f5c8f07?w=800', '茶企寻求鄂西地区 200 亩岩茶林合作经营，模式为长期租赁+品牌共建。', '湖北恩施', NULL, NULL, 200, '2026-12-31 23:59:59', 7, '恩施林下经济产业有限公司', '13800000006', 96),
('DM2026003', '采购林下中药材原料 (三叶青/灵芝)', '采购类', '林下产品采购', 'https://images.unsplash.com/photo-1502082553048-f009c37129b9?w=800', '中药企业采购原料，要求野生或近野生。', '全国', 500000, 1500000, NULL, '2026-08-31 23:59:59', 7, '恩施林下经济产业有限公司', '13800000006', 67),
('DM2026004', '500 亩林地开发森林康养项目', '开发类', '林地开发', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=800', '森林康养品牌寻求 500 亩林地开展森林浴/瑜伽/冥想项目。', '湖北全省', NULL, NULL, 500, '2027-03-31 23:59:59', 6, '宜昌绿源生态科技', '13800000005', 84);

-- 地方特色农产品
INSERT INTO `t_farm_product` (`name`, `category`, `cover`, `intro`, `origin`, `cooperative`, `cooperative_id`, `unit`, `price`, `stock`, `sales`, `tags`) VALUES
('恩施玉露(明前特级)', '茶叶', 'https://images.unsplash.com/photo-1582793988951-9aed5509eb97?w=800', '明前手工采摘，恩施玉露核心产区。', '湖北省宜昌市渔洋关镇', '五峰竹海合作社', 4, '斤', 880, 320, 156, '源头直供,产地直销,有机'),
('利川红茶(冷后浑)', '茶叶', 'https://images.unsplash.com/photo-1565799558224-3a5dac11d35a?w=800', '利川核心产区红茶，传统手工制茶。', '湖北省恩施州利川市', '恩施富硒茶合作社', 5, '斤', 680, 280, 132, '源头直供,产地直销'),
('野生灵芝孢子粉', '林下中药材', 'https://images.unsplash.com/photo-1502082553048-f009c37129b9?w=800', '林下野生灵芝孢子粉，破壁处理。', '湖北省恩施州', '恩施林下经济产业', 7, '克', 18, 5000, 423, '产地直销,有机'),
('野山三叶青(冻干)', '林下中药材', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=800', '林下野生三叶青冻干粉，珍稀药材。', '湖北省恩施州利川市', '恩施林下经济产业', 7, '克', 25, 1800, 167, '源头直供,有机'),
('五峰笋干', '山野特产', 'https://images.unsplash.com/photo-1542838132-92c53300491e?w=800', '春笋天然晾晒笋干，无添加。', '湖北省宜昌市', '五峰竹海合作社', 4, '斤', 68, 2200, 980, '源头直供,产地直销'),
('野生猴头菇(干货)', '食用菌', 'https://images.unsplash.com/photo-1582568927935-c4775db28dde?w=800', '原始森林野生猴头菇。', '湖北省恩施州', '恩施林下经济产业', 7, '斤', 320, 800, 286, '产地直销,绿色'),
('恩施林下土鸡蛋', '山野特产', 'https://images.unsplash.com/photo-1582722872445-44dc5f7e3c8f?w=800', '林下放养土鸡，每日鲜采。', '湖北省宜昌市', '五峰竹海合作社', 4, '枚', 4, 5000, 1820, '源头直供,绿色,新鲜'),
('野山蜂蜜(百花)', '山野特产', 'https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=800', '深山中蜂百花蜜，年产量低。', '湖北省恩施州', '恩施富硒茶合作社', 5, '斤', 168, 600, 312, '源头直供,有机');

-- 金融产品
INSERT INTO `t_finance_product` (`product_code`, `name`, `category`, `bank_name`, `bank_logo`, `bank_id`, `cover`, `loan_min`, `loan_max`, `rate_min`, `rate_max`, `term_min`, `term_max`, `accept_time`, `apply_condition`, `apply_materials`, `apply_process`, `target_users`, `tags`, `intro`) VALUES
('FP-CARBON-01', '碳汇收储贷', '碳汇收储贷', '湖北宜昌农商银行', 'https://api.dicebear.com/7.x/identicon/svg?seed=aj', 8, 'https://images.unsplash.com/photo-1574482620881-484922f8b1c3?w=800', 100000, 5000000, 3.85, 5.65, 6, 60, '工作日 9:00-17:00', '1.林权清晰\n2.年内已完成林业碳汇资源登记\n3.无重大违法记录', '林权证、碳汇核证报告、企业基本信息、收入证明', '1.线上提交申请\n2.银行初审\n3.评估机构评估\n4.复审\n5.放款', '林农、合作社、生态企业', '碳汇,林业,惠农', '面向林业碳汇资源持有方的专项绿色信贷。'),
('FP-LSNL-01', '两山农林贷', '两山农林贷', '湖北宜昌农商银行', 'https://api.dicebear.com/7.x/identicon/svg?seed=aj', 8, 'https://images.unsplash.com/photo-1448375240586-882707db888b?w=800', 50000, 3000000, 4.15, 6.20, 6, 60, '工作日 9:00-17:00', '1.林农或合作社\n2.持有合规林权\n3.信用良好', '林权证、身份证、银行流水', '线上申请→初审→现场尽调→放款', '林农、合作社', '惠农,普惠,绿色', '专为林农、合作社设计的普惠绿色信贷。'),
('FP-LZJC-01', '绿色工厂贷', '绿色工厂贷', '湖北恩施建设银行', 'https://api.dicebear.com/7.x/identicon/svg?seed=np', 9, 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=800', 1000000, 50000000, 3.65, 5.45, 12, 120, '工作日 9:00-17:00', '1.绿色工厂认证\n2.近三年盈利\n3.主营业务符合生态银行支持目录', '工商营业执照、绿色工厂证书、近三年财报、项目可研报告', '线上申请→银行初审→项目尽调→风控审批→签约放款', '生态开发企业、绿色制造企业', '企业,绿色制造', '面向"绿色工厂"认证企业的专项贷款。'),
('FP-TZHE-01', '碳中和助力贷', '碳中和助力贷', '湖北恩施建设银行', 'https://api.dicebear.com/7.x/identicon/svg?seed=np', 9, 'https://images.unsplash.com/photo-1574482620881-484922f8b1c3?w=800', 500000, 30000000, 3.95, 5.85, 12, 84, '工作日 9:00-17:00', '1.企业减排项目\n2.碳排放可监测可核证', '减排项目说明、第三方监测报告、企业财报', '申请→评估→尽调→审批→放款', '碳排放企业、减排项目主体', '碳中和,绿色,企业', '支持企业实施碳中和路径的金融产品。'),
('FP-LSNJ-01', '两山乡居贷', '两山乡居贷', '湖北宜昌农商银行', 'https://api.dicebear.com/7.x/identicon/svg?seed=aj', 8, 'https://images.unsplash.com/photo-1418065460487-3e41a6c84dc5?w=800', 50000, 1000000, 4.25, 6.45, 12, 60, '工作日 9:00-17:00', '1.乡村民宿/农家乐经营者\n2.持有合规营业执照', '营业执照、经营场所证明、近一年流水', '线上申请→线下尽调→放款', '乡村民宿、农家乐', '惠农,乡村振兴', '面向乡村民宿、农家乐的特色信贷。');

-- 法律援助机构
INSERT INTO `t_legal_org` (`org_name`, `org_type`, `logo`, `address`, `service_area`, `expertise`, `phone`, `email`, `intro`, `lawyer_count`, `case_count`, `rating`) VALUES
('五峰公益法律援助中心', '公益法律援助中心', 'https://api.dicebear.com/7.x/identicon/svg?seed=ajlegal', '五峰县司法局大楼 3 层', '五峰县全域', '林权纠纷、生态侵权、合同审查', '0717-5099000', 'legal@aj.cn', '五峰县司法局直属公益机构。', 12, 568, 4.85),
('武大法学院乡村振兴援助队', '高校法律援助队', 'https://api.dicebear.com/7.x/identicon/svg?seed=zjulegal', '武汉市武昌区武汉大学校区', '湖北省全省', '林权纠纷、土地承包、生态环境', '027-87951000', NULL, '武大法学院教师与研究生组成的公益法律援助队。', 25, 320, 4.92),
('北京XX律师事务所湖北分所', '专业律所', 'https://api.dicebear.com/7.x/identicon/svg?seed=xxlegal', '武汉市江汉区建设大道', '湖北省全省', '商事合同、环境维权、林权流转', '027-87654321', 'fj@xxlaw.cn', '业内知名律所，湖北分所。', 18, 762, 4.78),
('恩施州生态法律援助站', '公益法律援助中心', 'https://api.dicebear.com/7.x/identicon/svg?seed=nplegal', '恩施州恩施市司法局', '恩施州全域', '林业纠纷、土地流转、生态修复', '0718-8612000', NULL, '恩施州司法局牵头的公益法律援助站。', 8, 412, 4.83);

-- 律师
INSERT INTO `t_lawyer` (`org_id`, `org_name`, `name`, `gender`, `avatar`, `cert_no`, `years`, `expertise`, `service_area`, `intro`, `phone`, `case_count`, `rating`) VALUES
(1, '五峰公益法律援助中心', '陈律师', '男', 'https://api.dicebear.com/7.x/avataaars/svg?seed=lawyer1', '执业证号 13301201210001', 15, '林权纠纷、生态环境', '五峰县全域', '环境与林业法专业律师。', '13900000002', 124, 4.92),
(1, '五峰公益法律援助中心', '徐律师', '女', 'https://api.dicebear.com/7.x/avataaars/svg?seed=lawyer2', '执业证号 13301201310002', 12, '合同审查、土地承包', '五峰县全域', '合同审查领域专家。', '13900000003', 89, 4.88),
(2, '武大法学院乡村振兴援助队', '李教授', '男', 'https://api.dicebear.com/7.x/avataaars/svg?seed=lawyer3', '执业证号 13300100510001', 22, '生态环境法、林权理论与实务', '湖北省全省', '武大法学院教授，环境法专家。', '13900000004', 56, 4.95),
(3, '北京XX律师事务所湖北分所', '王律师', '男', 'https://api.dicebear.com/7.x/avataaars/svg?seed=lawyer4', '执业证号 13501201410001', 11, '商事合同、林权流转', '湖北省全省', '专注林权流转的资深律师。', '13900000005', 167, 4.86),
(4, '恩施州生态法律援助站', '高律师', '女', 'https://api.dicebear.com/7.x/avataaars/svg?seed=lawyer5', '执业证号 13501201510001', 9, '林业纠纷、生态修复', '恩施州全域', '生态修复案件专家。', '13900000006', 72, 4.79);

-- 法律咨询示例
INSERT INTO `t_legal_consult` (`consult_no`, `consult_type`, `category`, `urgency`, `applicant_id`, `applicant_name`, `applicant_phone`, `applicant_region`, `target_org_id`, `target_org_name`, `target_lawyer_id`, `target_lawyer_name`, `description`, `status`) VALUES
('LC2026001', '区域援助', '林权纠纷', '紧急', 2, '张志强', '13800000001', '湖北省宜昌市', 1, '五峰公益法律援助中心', 1, '陈律师', '相邻地块林农占用我家林地约 2 亩，多次协商未果。', '服务中'),
('LC2026002', '区域援助', '合同审查', '一般', 4, '五峰竹海合作社', '13800000003', '湖北省宜昌市', 1, '五峰公益法律援助中心', 2, '徐律师', '与某收购方签订毛竹长期供应合同，请求审核条款。', '已对接'),
('LC2026003', '专属咨询', '生态项目合作', '紧急', 6, '宜昌绿源生态科技', '13800000005', '湖北省宜昌市', NULL, NULL, NULL, NULL, '与某外资基金合作的复杂股权与生态收益分配方案，本地律师无法解决。', '待对接');

-- 信息披露示例
INSERT INTO `t_disclosure` (`enterprise_id`, `enterprise_name`, `project_name`, `project_period`, `cover`, `progress`, `protection`, `operation`, `restoration`, `last_disclosure_time`, `next_due_time`, `warn_status`) VALUES
(6, '宜昌绿源生态科技有限公司', '五峰湾潭 1200 亩碳汇林项目', '2024-2034', 'https://images.unsplash.com/photo-1574482620881-484922f8b1c3?w=800', '已完成造林 1200 亩，碳汇核证 4800 吨/年', '严格保护原生植被，禁伐区面积 240 亩', '专人管护，每月巡查 2 次', '修复退化林地 80 亩，补植乡土树种 1.2 万株', '2026-01-15 10:00:00', '2027-01-15 23:59:59', '正常'),
(7, '恩施林下经济产业有限公司', '延平 480 亩林下中药材基地', '2023-2033', 'https://images.unsplash.com/photo-1502082553048-f009c37129b9?w=800', '基地建设完成，年产中药材 80 吨', '林下间种，不破坏原有林相', '合作社+农户模式管护', '林下植被恢复良好', '2025-08-10 09:30:00', '2026-08-10 23:59:59', '即将到期');

-- 默认将所有种子挂牌/需求置为"已通过"，前台市场即可看到
UPDATE `t_listing` SET `audit_status` = '已通过' WHERE `audit_status` = '待审核';
UPDATE `t_demand` SET `audit_status` = '已通过' WHERE `audit_status` = '待审核';

-- 反馈示例
INSERT INTO `t_feedback` (`type`, `content`, `contact_name`, `contact_phone`, `status`, `reply_content`, `replied_by`, `replied_time`) VALUES
('操作咨询', '我要登记的时候系统提示文件过大，怎么处理？', '张志强', '13800000001', '已回复', '请将照片压缩到 5MB 以内，PDF 不超过 20MB。具体可参考帮助文档。', '管理员', '2026-04-22 10:00:00'),
('意见建议', '建议在云图上增加面积统计与导出功能', '五峰竹海合作社', '13800000003', '处理中', NULL, NULL, NULL),
('业务求助', '碳汇评估申请提交后多久反馈结果？', '宜昌绿源生态科技', '13800000005', '已回复', '一般评估机构会在 3 个工作日内对接，整体评估周期 5-15 天。', '管理员', '2026-04-21 14:30:00'),
('投诉举报', '某挂牌方发布的资源信息存疑', '匿名', '13800000099', '待处理', NULL, NULL, NULL);

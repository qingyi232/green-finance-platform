<template>
  <div class="home">
    <!-- ============ Hero 区 ============ -->
    <section class="hero">
      <div class="hero-bg">
        <div class="bg-leaf bg-leaf-1"></div>
        <div class="bg-leaf bg-leaf-2"></div>
        <div class="bg-leaf bg-leaf-3"></div>
      </div>
      <div class="hero-inner page-container">
        <div class="hero-content">
          <div class="hero-badge animate-fade-up">
            <el-icon><CircleCheck /></el-icon>
            <span>立足"两山"理念 · 服务生态银行实践</span>
          </div>
          <h1 class="hero-title animate-fade-up">
            <span class="text-gradient">林林通·绿融通</span><br />
            一站式生态银行综合服务网站
          </h1>
          <p class="hero-desc animate-fade-up">
            聚焦林业与碳汇领域，整合
            <strong>收储登记 · 资产评估 · 供需交易 · 金融服务 · 法律服务</strong>
            五大核心业务，打通生态资源全业务链条，让"绿水青山"高效转化为"金山银山"。
          </p>
          <div class="hero-actions animate-fade-up">
            <el-button type="primary" size="large" round @click="$router.push('/resource/map')">
              <el-icon><Location /></el-icon>查看森林资源云图
            </el-button>
            <el-button size="large" round @click="$router.push('/trading/listings')">
              <el-icon><Goods /></el-icon>浏览资源市场
            </el-button>
          </div>
          <!-- 关键数据快览 -->
          <div class="hero-stats animate-fade-up">
            <div class="stat-item">
              <div class="stat-value">{{ stats.resourceArea || 0 }}</div>
              <div class="stat-label">已收储面积(亩)</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-value">{{ stats.carbonStorage || 0 }}</div>
              <div class="stat-label">碳汇储量(吨)</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-value">{{ stats.listingCount || 0 }}</div>
              <div class="stat-label">挂牌资源(个)</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-value">{{ Math.round((stats.loanAmount || 0) / 10000) }}</div>
              <div class="stat-label">累计放款(万元)</div>
            </div>
          </div>
        </div>

        <div class="hero-visual">
          <div class="visual-grid">
            <div class="visual-item" @click="$router.push('/resource')">
              <div class="vi-icon" style="background: linear-gradient(135deg, #10b981, #34d399);"><el-icon :size="24"><Stamp /></el-icon></div>
              <h4>收储登记中心</h4>
              <p>资源收储信息登记与管理</p>
            </div>
            <div class="visual-item" @click="$router.push('/evaluation')">
              <div class="vi-icon" style="background: linear-gradient(135deg, #f59e0b, #fbbf24);"><el-icon :size="24"><DocumentChecked /></el-icon></div>
              <h4>资产评估中心</h4>
              <p>专业资产价值评估服务</p>
            </div>
            <div class="visual-item" @click="$router.push('/trading')">
              <div class="vi-icon" style="background: linear-gradient(135deg, #0ea5e9, #38bdf8);"><el-icon :size="24"><Goods /></el-icon></div>
              <h4>供需交易中心</h4>
              <p>供需双方交易匹配平台</p>
            </div>
            <div class="visual-item" @click="$router.push('/finance')">
              <div class="vi-icon" style="background: linear-gradient(135deg, #6366f1, #818cf8);"><el-icon :size="24"><Money /></el-icon></div>
              <h4>金融服务中心</h4>
              <p>定制化金融解决方案</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ============ 核心业务板块 ============ -->
    <section class="entry-section">
      <div class="entry-bg"></div>
      <div class="page-container" style="position: relative; z-index: 1;">
        <div class="section-title">
          <h2>核心业务一站直达</h2>
          <span class="more">每个入口都覆盖完整办理流程</span>
        </div>
        <div class="entries">
          <div
            v-for="(e, i) in entries"
            :key="e.title"
            class="entry-card"
            :class="`entry-${i}`"
            :style="{ '--card-gradient': e.cardBg }"
            @click="$router.push(e.path)"
          >
            <div class="entry-icon" :style="{ background: e.gradient }">
              <el-icon :size="28"><component :is="e.icon" /></el-icon>
            </div>
            <h3>{{ e.title }}</h3>
            <p>{{ e.desc }}</p>
            <ul class="entry-features">
              <li v-for="f in e.features" :key="f"><el-icon><Right /></el-icon>{{ f }}</li>
            </ul>
            <div class="entry-action">
              进入<el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ============ 新闻政策（首页内嵌展示） ============ -->
    <section class="news-section page-container" id="news-policy">
      <div class="section-title" style="margin-bottom: 8px;">
        <h2>新闻政策</h2>
        <span class="more">实时推送行业动态 · 政策精准解读</span>
      </div>
      <div class="news-grid">
        <!-- 新闻动态 -->
        <div class="news-block">
          <div class="section-title sub-title">
            <h3>新闻动态</h3>
            <span class="more" @click="$router.push('/news')">查看更多 →</span>
          </div>
          <div class="news-feature eco-card" v-if="topNews" @click="$router.push('/news/' + topNews.id)">
            <img :src="topNews.cover" />
            <div class="news-feature-body">
              <el-tag size="small" effect="dark" round>{{ topNews.category }}</el-tag>
              <h3>{{ topNews.title }}</h3>
              <p>{{ topNews.summary }}</p>
              <div class="news-meta">
                <span><el-icon><Clock /></el-icon>{{ formatDate(topNews.publishedTime) }}</span>
                <span><el-icon><View /></el-icon>{{ topNews.viewCount }} 阅读</span>
              </div>
            </div>
          </div>
          <div class="news-sub-grid" v-if="newsList.length > 1">
            <div v-for="n in newsList.slice(1, 4)" :key="n.id" class="news-sub-card eco-card" @click="$router.push('/news/' + n.id)">
              <img :src="n.cover" />
              <div class="news-sub-body">
                <el-tag size="small" type="success" effect="plain">{{ n.category }}</el-tag>
                <h4>{{ n.title }}</h4>
                <span class="news-time">{{ formatDate(n.publishedTime) }}</span>
              </div>
            </div>
          </div>
          <div class="news-list" v-if="newsList.length > 4">
            <div v-for="n in newsList.slice(4)" :key="n.id" class="news-item" @click="$router.push('/news/' + n.id)">
              <el-tag size="small" type="success" effect="plain">{{ n.category }}</el-tag>
              <span class="news-title">{{ n.title }}</span>
              <span class="news-time">{{ formatDate(n.publishedTime) }}</span>
            </div>
          </div>
        </div>

        <!-- 政策解读 -->
        <div class="news-block">
          <div class="section-title sub-title">
            <h3>政策解读</h3>
            <span class="more" @click="$router.push('/policy')">查看更多 →</span>
          </div>
          <div class="policy-list">
            <div v-for="p in policyList" :key="p.id" class="policy-item eco-card" @click="$router.push('/policy/' + p.id)">
              <div class="policy-level">
                <el-tag size="small" :type="levelTag(p.level)" effect="dark">{{ p.level }}</el-tag>
              </div>
              <div class="policy-body">
                <h4>{{ p.title }}</h4>
                <p>{{ p.summary }}</p>
                <div class="policy-meta">
                  <span>{{ p.issueOrg }}</span>
                  <span>{{ formatDate(p.issueDate) }}</span>
                </div>
              </div>
              <el-icon class="policy-arrow"><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ============ 问题反馈 ============ -->
    <section class="feedback-section">
      <div class="page-container">
        <div class="feedback-card eco-card">
          <div class="fb-left">
            <div class="fb-badge"><el-icon><ChatLineRound /></el-icon></div>
            <h2>问题反馈</h2>
            <p>遇到任何问题都可以告诉我们，我们会安排专人在 1 个工作日内响应。</p>
            <el-button type="primary" round size="large" @click="$router.push('/feedback')">
              立即反馈<el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
          <div class="fb-right">
            <div class="fb-types">
              <div v-for="t in feedbackTypes" :key="t.label" class="fb-type" @click="goFeedback(t.label)">
                <div class="fb-type-icon" :style="{ background: t.bg, color: t.color }">
                  <el-icon :size="20"><component :is="t.icon" /></el-icon>
                </div>
                <span>{{ t.label }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ============ 信息披露 ============ -->
    <section class="disclosure-section page-container">
      <div class="section-title">
        <h2>企业信息披露</h2>
        <span class="more" @click="$router.push('/disclosure')">查看全部 →</span>
      </div>
      <div class="disclosure-grid">
        <div v-for="d in disclosureList" :key="d.id" class="disclosure-card eco-card" @click="$router.push('/disclosure/' + d.id)">
          <img :src="d.cover" />
          <div class="dis-body">
            <div class="dis-meta">
              <el-tag size="small" :type="d.warnStatus === '已逾期' ? 'danger' : d.warnStatus === '即将到期' ? 'warning' : 'success'" effect="dark">
                {{ d.warnStatus }}
              </el-tag>
              <span class="dis-period">{{ d.projectPeriod }}</span>
            </div>
            <h4>{{ d.projectName }}</h4>
            <p class="dis-ent">{{ d.enterpriseName }}</p>
            <p class="dis-progress">{{ d.progress }}</p>
            <div class="dis-footer">
              <span><el-icon><Clock /></el-icon>下次披露：{{ formatDate(d.nextDueTime) }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ============ 平台优势 ============ -->
    <section class="advantage-section">
      <div class="page-container">
        <div class="adv-header">
          <h2 class="text-gradient">让生态价值，看得见、流得通、变得现</h2>
          <p>立足生态银行实践，整合分散业务，构建闭环服务体系</p>
        </div>
        <div class="adv-grid">
          <div v-for="a in advantages" :key="a.title" class="adv-card eco-card">
            <div class="adv-num">{{ a.num }}</div>
            <h3>{{ a.title }}</h3>
            <p>{{ a.desc }}</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../api/request'

const stats = reactive({})
const newsList = ref([])
const topNews = ref(null)
const policyList = ref([])
const disclosureList = ref([])
const orgCount = ref(0)
const legalOrgCount = ref(0)

const entries = [
  {
    title: '收储登记中心',
    desc: '森林资源一张图 · 在线自主登记，让生态资源底数清、可视化',
    icon: 'Stamp',
    gradient: 'linear-gradient(135deg, #10b981 0%, #34d399 100%)',
    cardBg: 'linear-gradient(135deg, rgba(236,253,245,0.85) 0%, rgba(209,250,229,0.7) 100%)',
    path: '/resource',
    features: ['全域资源云图', '林地/碳汇线上登记', '权属证明上传'],
  },
  {
    title: '资产评估中心',
    desc: '专业评估机构入驻 · 流程透明，价值核算有依据',
    icon: 'DocumentChecked',
    gradient: 'linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%)',
    cardBg: 'linear-gradient(135deg, rgba(255,251,235,0.85) 0%, rgba(254,243,199,0.7) 100%)',
    path: '/evaluation',
    features: ['标准化流程图', '机构/评估师资质公示', '在线评估办理'],
  },
  {
    title: '供需交易中心',
    desc: '资源供给+需求双市场 · 在线挂牌，预约洽谈高效对接',
    icon: 'Goods',
    gradient: 'linear-gradient(135deg, #0ea5e9 0%, #38bdf8 100%)',
    cardBg: 'linear-gradient(135deg, rgba(240,249,255,0.85) 0%, rgba(224,242,254,0.7) 100%)',
    path: '/trading',
    features: ['林地/碳汇/林下产品', '供需精准匹配', '特色农产品展销'],
  },
  {
    title: '金融服务中心',
    desc: '专属绿色金融产品 · 在线申请，银行端线上审批',
    icon: 'Money',
    gradient: 'linear-gradient(135deg, #6366f1 0%, #818cf8 100%)',
    cardBg: 'linear-gradient(135deg, rgba(238,242,255,0.85) 0%, rgba(224,231,255,0.7) 100%)',
    path: '/finance',
    features: ['碳汇收储贷', '两山农林贷', '产品在线匹配'],
  },
  {
    title: '法律服务中心',
    desc: '区域法律援助库 · 专属法律咨询，护航生态资源流转',
    icon: 'Service',
    gradient: 'linear-gradient(135deg, #8b5cf6 0%, #a78bfa 100%)',
    cardBg: 'linear-gradient(135deg, rgba(250,245,255,0.85) 0%, rgba(237,233,254,0.7) 100%)',
    path: '/legal',
    features: ['援助机构/律师库', '区域援助匹配', '专属咨询服务'],
  },
]

const feedbackTypes = [
  { label: '操作咨询', icon: 'QuestionFilled', color: '#0ea5e9', bg: '#f0f9ff' },
  { label: '意见建议', icon: 'EditPen', color: '#10b981', bg: '#ecfdf5' },
  { label: '投诉举报', icon: 'Warning', color: '#f43f5e', bg: '#fff1f2' },
  { label: '业务求助', icon: 'Headset', color: '#f59e0b', bg: '#fffbeb' },
]

const advantages = [
  { num: '01', title: '业务一站化', desc: '收储、评估、交易、金融、法律 5 大业务全部线上办理，告别"线下跑断腿"。' },
  { num: '02', title: '资源可视化', desc: '依托 GIS 林业大数据，全域森林资源一张图直观呈现，资源底数一目了然。' },
  { num: '03', title: '匹配智能化', desc: '供需双向智能匹配 + 金融产品智能推荐，对接精准、效率高。' },
  { num: '04', title: '风险可控化', desc: '专业评估 + 法律保障 + 银行审批三重把关，让每一笔生态交易都安心。' },
]

const formatDate = (d) => {
  if (!d) return ''
  const date = new Date(d)
  return `${date.getMonth() + 1}-${date.getDate()}`
}

const levelTag = (level) => {
  const m = { 国家级: 'danger', 省级: 'warning', 市级: '', 县级: 'info' }
  return m[level] || 'info'
}

const goFeedback = (type) => {
  sessionStorage.setItem('feedback-type', type)
  window.location.href = '/feedback'
}

onMounted(async () => {
  try {
    const [s, n, p, d, eorgs, lorgs] = await Promise.all([
      request.get('/stats/overview'),
      request.get('/news/list', { params: { page: 1, limit: 6 } }),
      request.get('/policy/list', { params: { page: 1, limit: 4 } }),
      request.get('/disclosure/list', { params: { page: 1, limit: 3 } }),
      request.get('/evaluation-org/list', { params: { page: 1, limit: 1 } }),
      request.get('/legal-org/list', { params: { page: 1, limit: 1 } }),
    ])
    Object.assign(stats, s.data || {})
    newsList.value = n.data?.list || []
    topNews.value = newsList.value[0]
    policyList.value = p.data?.list || []
    disclosureList.value = d.data?.list || []
    orgCount.value = eorgs.data?.totalCount || 0
    legalOrgCount.value = lorgs.data?.totalCount || 0
  } catch (e) {
    console.warn('首页数据加载失败', e)
  }
})
</script>

<style scoped>
/* ============ Hero ============ */
.hero {
  position: relative;
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 50%, #a7f3d0 100%);
  overflow: hidden;
}
.hero-bg { position: absolute; inset: 0; pointer-events: none; }
.bg-leaf {
  position: absolute;
  border-radius: 50%;
  filter: blur(70px);
  opacity: 0.45;
}
.bg-leaf-1 { width: 380px; height: 380px; background: #6ee7b7; top: -80px; right: -60px; }
.bg-leaf-2 { width: 280px; height: 280px; background: #34d399; bottom: -100px; left: 10%; opacity: 0.35; }
.bg-leaf-3 { width: 200px; height: 200px; background: #5eead4; top: 30%; left: 35%; opacity: 0.25; }

.hero-inner {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: 1.05fr 1fr;
  gap: 48px;
  padding: 80px 24px 100px;
  align-items: center;
}
.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  background: rgba(255, 255, 255, 0.78);
  border: 1px solid rgba(16, 185, 129, 0.3);
  border-radius: 999px;
  font-size: 13px;
  color: var(--primary-darker);
  margin-bottom: 20px;
  animation-delay: 0s;
}
.hero-title {
  font-size: 46px;
  font-weight: 800;
  line-height: 1.25;
  margin: 0 0 18px;
  color: var(--text-primary);
  letter-spacing: 1px;
  animation-delay: 0.1s;
  text-shadow: 0 2px 8px rgba(4, 120, 87, 0.1);
}
.hero-desc {
  font-size: 16px;
  line-height: 1.85;
  color: var(--text-secondary);
  margin-bottom: 32px;
  max-width: 560px;
  animation-delay: 0.2s;
}
.hero-desc strong { color: var(--primary-darker); font-weight: 600; }
.hero-actions {
  display: flex;
  gap: 14px;
  margin-bottom: 48px;
  animation-delay: 0.3s;
}
.hero-stats {
  display: flex;
  align-items: center;
  gap: 32px;
  padding: 22px 30px;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.55);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(16, 185, 129, 0.1), inset 0 1px 0 rgba(255, 255, 255, 0.7);
  animation-delay: 0.4s;
  width: fit-content;
}
.stat-item { text-align: center; min-width: 100px; }
.stat-value { font-size: 26px; font-weight: 800; color: var(--primary-darker); line-height: 1; }
.stat-label { font-size: 12px; color: var(--text-tertiary); margin-top: 6px; }
.stat-divider { width: 1px; height: 32px; background: var(--border); }

/* Hero 右侧服务网格 */
.hero-visual {
  display: flex;
  align-items: center;
  justify-content: center;
}
.visual-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 18px;
  width: 100%;
  max-width: 460px;
}
.visual-item {
  background: rgba(255, 255, 255, 0.82);
  backdrop-filter: blur(16px) saturate(180%);
  -webkit-backdrop-filter: blur(16px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.65);
  border-radius: 18px;
  padding: 28px 22px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 12px rgba(15, 23, 42, 0.04);
}
.visual-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(16, 185, 129, 0.12);
  border-color: rgba(16, 185, 129, 0.3);
}
.vi-icon {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin: 0 auto 14px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}
.visual-item h4 {
  font-size: 16px;
  font-weight: 700;
  margin: 0 0 6px;
  color: var(--text-primary);
}
.visual-item p {
  font-size: 13px;
  margin: 0;
  color: var(--text-tertiary);
  line-height: 1.5;
}

/* ============ 业务入口 ============ */
.entry-section {
  padding-top: 60px;
  padding-bottom: 20px;
  position: relative;
  overflow: hidden;
}
.entry-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(240, 253, 250, 0.3) 0%, rgba(236, 253, 245, 0.5) 100%);
  pointer-events: none;
}
.entries {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 22px;
}
.entry-card {
  position: relative;
  padding: 32px 28px;
  cursor: pointer;
  overflow: hidden;
  background: var(--card-gradient, rgba(255, 255, 255, 0.85));
  backdrop-filter: blur(16px) saturate(180%);
  -webkit-backdrop-filter: blur(16px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.65);
  border-radius: 20px;
  box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04), 0 8px 24px rgba(15, 23, 42, 0.04);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}
.entry-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 36px rgba(16, 185, 129, 0.12), 0 4px 16px rgba(15, 23, 42, 0.06);
  border-color: rgba(16, 185, 129, 0.25);
}
.entry-card::before {
  content: "";
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 4px;
  border-radius: 20px 20px 0 0;
}
.entry-0::before { background: linear-gradient(90deg, #10b981, #34d399); }
.entry-1::before { background: linear-gradient(90deg, #f59e0b, #fbbf24); }
.entry-2::before { background: linear-gradient(90deg, #0ea5e9, #38bdf8); }
.entry-3::before { background: linear-gradient(90deg, #6366f1, #818cf8); }
.entry-4::before { background: linear-gradient(90deg, #8b5cf6, #a78bfa); }
.entry-card::after {
  content: "";
  position: absolute;
  inset: auto -50px -50px auto;
  width: 160px;
  height: 160px;
  border-radius: 50%;
  opacity: 0.5;
  transition: all 0.5s;
}
.entry-0::after { background: radial-gradient(circle, rgba(16, 185, 129, 0.12) 0%, transparent 70%); }
.entry-1::after { background: radial-gradient(circle, rgba(245, 158, 11, 0.1) 0%, transparent 70%); }
.entry-2::after { background: radial-gradient(circle, rgba(14, 165, 233, 0.1) 0%, transparent 70%); }
.entry-3::after { background: radial-gradient(circle, rgba(99, 102, 241, 0.1) 0%, transparent 70%); }
.entry-4::after { background: radial-gradient(circle, rgba(139, 92, 246, 0.1) 0%, transparent 70%); }
.entry-card:hover::after { transform: scale(2); opacity: 0.8; }
.entry-icon {
  width: 60px;
  height: 60px;
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-bottom: 20px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15), 0 2px 4px rgba(0, 0, 0, 0.08);
}
.entry-card h3 {
  font-size: 19px;
  font-weight: 800;
  margin: 0 0 8px;
  color: var(--text-primary);
  text-shadow: 0 1px 3px rgba(15, 23, 42, 0.08);
  letter-spacing: 0.5px;
}
.entry-card p {
  font-size: 13px;
  color: var(--text-tertiary);
  margin: 0 0 16px;
  line-height: 1.6;
  min-height: 42px;
}
.entry-features { list-style: none; padding: 0; margin: 0 0 20px; }
.entry-features li {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 6px;
}
.entry-features li .el-icon { color: var(--primary); }
.entry-action {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 600;
  color: var(--primary-darker);
  position: relative;
  z-index: 1;
}
.entry-card:hover .entry-action { color: var(--primary); }

/* ============ 新闻政策 ============ */
.news-section { padding-top: 60px; }
.sub-title h3 {
  font-size: 17px;
  font-weight: 600;
  color: var(--text-secondary);
  margin: 0;
}
.sub-title h3::before { display: none; }
.news-grid {
  display: grid;
  grid-template-columns: 1.15fr 1fr;
  gap: 32px;
}
.news-feature {
  display: flex;
  flex-direction: column;
  gap: 0;
  padding: 0;
  cursor: pointer;
  margin-bottom: 18px;
  overflow: hidden;
}
.news-feature img {
  width: 100%;
  height: 260px;
  object-fit: cover;
  border-radius: 16px 16px 0 0;
  transition: transform 0.4s;
}
.news-feature:hover img { transform: scale(1.03); }
.news-feature-body { padding: 20px 22px; flex: 1; }
.news-feature-body h3 {
  font-size: 19px;
  font-weight: 700;
  margin: 10px 0;
  color: var(--primary-darker);
  line-height: 1.45;
}
.news-feature-body p { font-size: 14px; color: var(--text-tertiary); line-height: 1.7; margin: 0 0 12px;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.news-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: var(--text-light);
}
.news-meta span { display: flex; align-items: center; gap: 4px; }

.news-sub-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 14px;
  margin-bottom: 16px;
}
.news-sub-card {
  cursor: pointer;
  overflow: hidden;
  padding: 0;
}
.news-sub-card img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  transition: transform 0.4s;
}
.news-sub-card:hover img { transform: scale(1.05); }
.news-sub-body {
  padding: 12px 14px;
}
.news-sub-body h4 {
  font-size: 13px;
  font-weight: 600;
  margin: 6px 0 4px;
  color: var(--text-primary);
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-list {
  background: white;
  border-radius: 14px;
  padding: 8px 4px;
  box-shadow: var(--shadow-sm);
}
.news-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  cursor: pointer;
  transition: background 0.2s;
  border-radius: 8px;
}
.news-item:hover { background: var(--primary-bg); }
.news-title { flex: 1; font-size: 14px; color: var(--primary-darker); font-weight: 500; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.news-time { font-size: 12px; color: var(--text-light); }

.policy-list { display: flex; flex-direction: column; gap: 12px; }
.policy-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 18px 20px;
  cursor: pointer;
}
.policy-level { flex-shrink: 0; }
.policy-body { flex: 1; min-width: 0; }
.policy-body h4 {
  font-size: 14px; font-weight: 600; margin: 0 0 6px;
  color: var(--primary-darker);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.policy-body p {
  font-size: 12px; color: var(--text-tertiary); margin: 0 0 6px; line-height: 1.5;
  display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden;
}
.policy-meta { display: flex; gap: 12px; font-size: 11px; color: var(--text-light); }
.policy-arrow { color: var(--text-light); transition: transform 0.2s; }
.policy-item:hover .policy-arrow { color: var(--primary); transform: translateX(4px); }

/* ============ 反馈 ============ */
.feedback-section { padding-top: 60px; }
.feedback-card {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 48px;
  padding: 48px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(236, 253, 245, 0.85) 100%);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  align-items: center;
}
.fb-badge {
  width: 60px; height: 60px; border-radius: 16px;
  background: var(--gradient-forest);
  display: flex; align-items: center; justify-content: center; color: white;
  margin-bottom: 18px;
  box-shadow: var(--shadow-glow);
}
.fb-badge .el-icon { font-size: 26px; }
.fb-left h2 { font-size: 26px; font-weight: 800; margin: 0 0 12px; color: var(--text-primary); }
.fb-left p { font-size: 14px; color: var(--text-secondary); line-height: 1.7; margin: 0 0 22px; }

.fb-types {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}
.fb-type {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px 22px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 16px;
  cursor: pointer;
  border: 1px solid rgba(255, 255, 255, 0.6);
  box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
  transition: all 0.3s;
}
.fb-type:hover { border-color: var(--primary); transform: translateY(-3px); box-shadow: 0 8px 24px rgba(16, 185, 129, 0.1); }
.fb-type-icon {
  width: 46px; height: 46px; border-radius: 14px;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
}
.fb-type span { font-weight: 600; font-size: 15px; color: var(--text-primary); }

/* ============ 信息披露 ============ */
.disclosure-section { padding-top: 60px; }
.disclosure-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 22px;
}
.disclosure-card {
  cursor: pointer;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
.disclosure-card img {
  width: 100%;
  height: 180px;
  object-fit: cover;
}
.dis-body { padding: 18px; flex: 1; display: flex; flex-direction: column; gap: 8px; }
.dis-meta { display: flex; align-items: center; justify-content: space-between; }
.dis-period { font-size: 12px; color: var(--text-light); }
.dis-body h4 { font-size: 16px; font-weight: 700; margin: 4px 0; color: var(--text-primary); line-height: 1.4; }
.dis-ent { font-size: 13px; color: var(--primary-darker); margin: 0; }
.dis-progress {
  font-size: 13px; color: var(--text-tertiary); line-height: 1.7; margin: 0;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
}
.dis-footer {
  margin-top: auto;
  padding-top: 12px;
  border-top: 1px dashed var(--border-light);
  font-size: 12px;
  color: var(--text-light);
}
.dis-footer span { display: flex; align-items: center; gap: 4px; }

/* ============ 平台优势 ============ */
.advantage-section {
  padding: 80px 0 60px;
  margin-top: 60px;
  background: linear-gradient(180deg, #ffffff 0%, #f0fdfa 100%);
}
.adv-header { text-align: center; margin-bottom: 48px; }
.adv-header h2 { font-size: 32px; font-weight: 800; margin: 0 0 12px; text-shadow: 0 2px 6px rgba(4, 120, 87, 0.12); }
.adv-header p { font-size: 15px; color: var(--text-tertiary); margin: 0; }
.adv-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 22px;
}
.adv-card {
  padding: 28px 24px;
  background: white;
  position: relative;
  overflow: hidden;
}
.adv-num {
  font-size: 56px;
  font-weight: 900;
  color: var(--primary-bg-2);
  line-height: 1;
  margin-bottom: 6px;
  letter-spacing: -2px;
}
.adv-card h3 { font-size: 17px; font-weight: 700; margin: 0 0 8px; color: var(--text-primary); }
.adv-card p { font-size: 13px; color: var(--text-tertiary); margin: 0; line-height: 1.7; }

/* ============ 响应式 ============ */
@media (max-width: 1100px) {
  .hero-inner { grid-template-columns: 1fr; }
  .visual-grid { max-width: 100%; }
  .entries { grid-template-columns: repeat(2, 1fr); }
  .news-grid { grid-template-columns: 1fr; }
  .news-sub-grid { grid-template-columns: repeat(2, 1fr); }
  .feedback-card { grid-template-columns: 1fr; }
  .disclosure-grid { grid-template-columns: repeat(2, 1fr); }
  .adv-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>

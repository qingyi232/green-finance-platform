<template>
  <div class="dashboard">
    <h2 class="welcome">欢迎回来，{{ userStore.user?.realName || '管理员' }} 👋</h2>
    <p class="welcome-sub">林林通·绿融通 数据总览</p>

    <!-- 大数据卡片 -->
    <div class="big-cards">
      <div class="big-card eco-card" v-for="(c, i) in bigCards" :key="i" :class="`bc-${i}`">
        <div class="bc-icon"><el-icon :size="22"><component :is="c.icon" /></el-icon></div>
        <div class="bc-num">{{ c.value }}<small>{{ c.unit }}</small></div>
        <div class="bc-label">{{ c.label }}</div>
        <div class="bc-progress"><div class="bc-fill"></div></div>
      </div>
    </div>

    <!-- ECharts 图表区 -->
    <div class="chart-row">
      <div class="chart-card eco-card">
        <h3>资源类型分布</h3>
        <div ref="pieRef" class="chart-canvas"></div>
      </div>
      <div class="chart-card eco-card">
        <h3>核心业务量统计</h3>
        <div ref="barRef" class="chart-canvas"></div>
      </div>
    </div>

    <div class="chart-row chart-row-2">
      <div class="chart-card eco-card">
        <h3>业务关键指标</h3>
        <div class="biz-grid">
          <div class="biz-item" v-for="b in bizCards" :key="b.label">
            <div class="biz-icon" :style="{ background: b.bg, color: b.color }"><el-icon :size="18"><component :is="b.icon" /></el-icon></div>
            <div>
              <div class="biz-label">{{ b.label }}</div>
              <div class="biz-value">{{ b.value }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="chart-card eco-card">
        <h3>近期增长趋势</h3>
        <div ref="lineRef" class="chart-canvas"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import request from '../../api/request'
import { useUserStore } from '../../store'

const userStore = useUserStore()
const stats = reactive({})
const typeDist = ref({})

const pieRef = ref(null)
const barRef = ref(null)
const lineRef = ref(null)
let pieChart = null
let barChart = null
let lineChart = null

const bigCards = computed(() => [
  { label: '已收储资源', value: stats.resourceCount || 0, unit: '处', icon: 'Stamp' },
  { label: '收储面积', value: Math.round(stats.resourceArea || 0), unit: '亩', icon: 'Crop' },
  { label: '碳汇储量', value: Math.round(stats.carbonStorage || 0), unit: '吨', icon: 'Sunny' },
  { label: '累计放款', value: Math.round((stats.loanAmount || 0) / 10000), unit: '万元', icon: 'Money' },
])
const bizCards = computed(() => [
  { label: '挂牌资源', value: stats.listingCount || 0, icon: 'Goods', color: '#10b981', bg: '#ecfdf5' },
  { label: '资源需求', value: stats.demandCount || 0, icon: 'ShoppingCart', color: '#f59e0b', bg: '#fffbeb' },
  { label: '评估申请', value: stats.evaluationCount || 0, icon: 'DocumentChecked', color: '#0ea5e9', bg: '#f0f9ff' },
  { label: '贷款申请', value: stats.loanCount || 0, icon: 'CreditCard', color: '#6366f1', bg: '#eef2ff' },
  { label: '法律咨询', value: stats.consultCount || 0, icon: 'Service', color: '#8b5cf6', bg: '#faf5ff' },
  { label: '注册用户', value: stats.userCount || 0, icon: 'User', color: '#f43f5e', bg: '#fff1f2' },
  { label: '金融产品', value: stats.financeProductCount || 0, icon: 'Wallet', color: '#14b8a6', bg: '#f0fdfa' },
  { label: '特色农产品', value: stats.farmProductCount || 0, icon: 'IceCreamSquare', color: '#22c55e', bg: '#f0fdf4' },
])

const renderPie = () => {
  if (!pieRef.value) return
  pieChart = echarts.init(pieRef.value)
  const colorMap = { 林地: '#10b981', 碳汇: '#f59e0b', 水库: '#0ea5e9', 林下产品: '#8b5cf6' }
  const data = Object.entries(typeDist.value).map(([n, v]) => ({ name: n, value: v, itemStyle: { color: colorMap[n] || '#94a3b8' } }))
  pieChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} 处 ({d}%)' },
    legend: { bottom: 0, icon: 'circle' },
    series: [{
      type: 'pie',
      radius: ['45%', '72%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
      label: { show: true, formatter: '{b}\n{c}处' },
      emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
      data,
    }],
  })
}

const renderBar = () => {
  if (!barRef.value) return
  barChart = echarts.init(barRef.value)
  const items = bizCards.value.slice(0, 6)
  barChart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: 40, right: 20, top: 20, bottom: 30 },
    xAxis: {
      type: 'category',
      data: items.map((b) => b.label),
      axisLine: { lineStyle: { color: '#e2e8f0' } },
      axisLabel: { color: '#64748b', fontSize: 11, interval: 0, rotate: 0 },
    },
    yAxis: { type: 'value', axisLine: { show: false }, splitLine: { lineStyle: { color: '#f1f5f9' } } },
    series: [{
      type: 'bar',
      data: items.map((b) => ({ value: b.value, itemStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: b.color }, { offset: 1, color: b.color + '40' },
      ]) } })),
      barWidth: '50%',
      itemStyle: { borderRadius: [6, 6, 0, 0] },
      label: { show: true, position: 'top', color: '#475569', fontSize: 11 },
    }],
  })
}

const trendData = ref({ days: [], resource: [], listing: [], loan: [] })

const renderLine = () => {
  if (!lineRef.value) return
  lineChart = echarts.init(lineRef.value)
  const td = trendData.value
  lineChart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { bottom: 0, icon: 'roundRect', itemWidth: 14, itemHeight: 8 },
    grid: { left: 40, right: 20, top: 20, bottom: 50 },
    xAxis: { type: 'category', data: td.days, axisLine: { lineStyle: { color: '#e2e8f0' } }, axisLabel: { color: '#64748b', fontSize: 11 } },
    yAxis: { type: 'value', minInterval: 1, axisLine: { show: false }, splitLine: { lineStyle: { color: '#f1f5f9' } } },
    series: [
      { name: '资源登记', type: 'line', smooth: true, lineStyle: { width: 3, color: '#10b981' }, itemStyle: { color: '#10b981' }, areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#10b98166' }, { offset: 1, color: '#10b98100' }]) }, data: td.resource },
      { name: '资源挂牌', type: 'line', smooth: true, lineStyle: { width: 3, color: '#f59e0b' }, itemStyle: { color: '#f59e0b' }, data: td.listing },
      { name: '贷款申请', type: 'line', smooth: true, lineStyle: { width: 3, color: '#6366f1' }, itemStyle: { color: '#6366f1' }, data: td.loan },
    ],
  })
}

const onResize = () => {
  pieChart?.resize()
  barChart?.resize()
  lineChart?.resize()
}

onMounted(async () => {
  const [s, d, t] = await Promise.all([
    request.get('/stats/overview'),
    request.get('/stats/resource-distribution'),
    request.get('/stats/trend'),
  ])
  Object.assign(stats, s.data || {})
  typeDist.value = d.byType || {}
  trendData.value = t.data || { days: [], resource: [], listing: [], loan: [] }
  await nextTick()
  renderPie(); renderBar(); renderLine()
  window.addEventListener('resize', onResize)
})
onUnmounted(() => {
  window.removeEventListener('resize', onResize)
  pieChart?.dispose(); barChart?.dispose(); lineChart?.dispose()
})
</script>

<style scoped>
.welcome { font-size: 24px; font-weight: 800; margin: 0 0 4px; color: var(--text-primary); }
.welcome-sub { font-size: 13px; color: var(--text-tertiary); margin: 0 0 24px; }

.big-cards { display: grid; grid-template-columns: repeat(4, 1fr); gap: 18px; margin-bottom: 22px; }
.big-card { position: relative; padding: 22px; overflow: hidden; }
.bc-icon { width: 42px; height: 42px; border-radius: 12px; display: flex; align-items: center; justify-content: center; color: white; margin-bottom: 14px; }
.bc-0 .bc-icon { background: linear-gradient(135deg, #10b981, #34d399); }
.bc-1 .bc-icon { background: linear-gradient(135deg, #f59e0b, #fbbf24); }
.bc-2 .bc-icon { background: linear-gradient(135deg, #0ea5e9, #38bdf8); }
.bc-3 .bc-icon { background: linear-gradient(135deg, #6366f1, #818cf8); }
.bc-num { font-size: 32px; font-weight: 800; color: var(--text-primary); line-height: 1; display: flex; align-items: baseline; gap: 4px; }
.bc-num small { font-size: 13px; color: var(--text-tertiary); font-weight: 500; }
.bc-label { font-size: 13px; color: var(--text-tertiary); margin-top: 8px; }
.bc-progress { position: absolute; bottom: 0; left: 0; right: 0; height: 3px; background: var(--border-light); }
.bc-0 .bc-fill { background: #10b981; width: 70%; }
.bc-1 .bc-fill { background: #f59e0b; width: 60%; }
.bc-2 .bc-fill { background: #0ea5e9; width: 80%; }
.bc-3 .bc-fill { background: #6366f1; width: 50%; }
.bc-fill { display: block; height: 100%; transition: width 1s; }

.chart-row { display: grid; grid-template-columns: 1fr 1.4fr; gap: 18px; margin-bottom: 18px; }
.chart-row-2 { grid-template-columns: 1fr 1.4fr; }
.chart-card { padding: 22px 24px; }
.chart-card h3 { font-size: 15px; font-weight: 700; margin: 0 0 14px; color: var(--text-primary); display: flex; align-items: center; gap: 8px; }
.chart-card h3::before { content: ''; width: 3px; height: 14px; background: var(--primary); border-radius: 2px; }
.chart-canvas { width: 100%; height: 280px; }

.biz-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; }
.biz-item { display: flex; align-items: center; gap: 12px; padding: 12px; background: var(--bg-page); border-radius: 12px; }
.biz-icon { width: 36px; height: 36px; border-radius: 10px; display: flex; align-items: center; justify-content: center; }
.biz-label { font-size: 12px; color: var(--text-tertiary); }
.biz-value { font-size: 16px; font-weight: 800; color: var(--text-primary); margin-top: 2px; }
</style>

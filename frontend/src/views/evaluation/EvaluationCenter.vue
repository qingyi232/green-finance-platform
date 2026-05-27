<template>
  <div>
    <PageHeader
      title="资产评估中心"
      desc="标准化评估流程 · 评估机构与评估师资质全公示 · 线上自主评估办理"
      :gradient="'linear-gradient(120deg, #fffbeb 0%, #fef3c7 70%, #fde68a 100%)'"
    />
    <div class="page-container">
      <!-- 流程图 -->
      <div class="section-title">
        <h2>标准化评估流程</h2>
      </div>
      <div class="process-flow eco-card">
        <div class="process-step" v-for="(s, i) in processSteps" :key="i">
          <div class="ps-num">{{ i + 1 }}</div>
          <div class="ps-icon" :style="{ background: s.color }"><el-icon :size="20"><component :is="s.icon" /></el-icon></div>
          <div class="ps-content">
            <h4>{{ s.title }}</h4>
            <p>{{ s.desc }}</p>
          </div>
          <el-icon class="ps-arrow" v-if="i < processSteps.length - 1"><DArrowRight /></el-icon>
        </div>
      </div>

      <!-- 快捷入口 -->
      <div class="quick-grid">
        <div class="quick-card eco-card" @click="$router.push('/evaluation/orgs')">
          <div class="qc-icon" style="background: linear-gradient(135deg, #f59e0b, #fbbf24);"><el-icon :size="24"><OfficeBuilding /></el-icon></div>
          <h3>评估机构</h3>
          <p>{{ stats.orgCount }} 家专业机构</p>
          <span class="qc-action">查看 →</span>
        </div>
        <div class="quick-card eco-card" @click="$router.push('/evaluation/evaluators')">
          <div class="qc-icon" style="background: linear-gradient(135deg, #10b981, #34d399);"><el-icon :size="24"><User /></el-icon></div>
          <h3>专业评估师</h3>
          <p>{{ stats.evaluatorCount }} 位执业评估师</p>
          <span class="qc-action">查看 →</span>
        </div>
        <div class="quick-card eco-card primary" @click="$router.push('/evaluation/apply')">
          <div class="qc-icon"><el-icon :size="24"><DocumentChecked /></el-icon></div>
          <h3>我要评估</h3>
          <p>线上自主提交评估申请</p>
          <span class="qc-action">立即申请 →</span>
        </div>
      </div>

      <!-- 推荐评估师 -->
      <div class="section-title" style="margin-top: 48px;">
        <h2>明星评估师</h2>
        <span class="more" @click="$router.push('/evaluation/evaluators')">查看全部 →</span>
      </div>
      <div class="evaluator-grid">
        <div v-for="e in evaluators" :key="e.id" class="evaluator-card eco-card" @click="$router.push('/evaluation/apply?evaluator=' + e.id)">
          <el-avatar :size="64" :src="e.avatar" />
          <h4>{{ e.name }}<el-tag v-if="e.gender" size="small" :type="e.gender === '女' ? 'danger' : 'primary'" effect="plain">{{ e.gender }}</el-tag></h4>
          <p class="ev-org">{{ e.orgName }}</p>
          <div class="ev-tags">
            <el-tag v-for="t in (e.expertise || '').split('、')" :key="t" size="small" effect="plain" round>{{ t }}</el-tag>
          </div>
          <div class="ev-stats">
            <div><strong>{{ e.years }}</strong><small>年经验</small></div>
            <div><strong>{{ e.caseCount }}</strong><small>案例数</small></div>
            <div><strong>{{ e.rating }}</strong><small>评分</small></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const evaluators = ref([])
const stats = reactive({ orgCount: 0, evaluatorCount: 0 })

const processSteps = [
  { title: '材料上报', desc: '提交评估申请与基础材料', icon: 'EditPen', color: 'linear-gradient(135deg, #10b981, #34d399)' },
  { title: '资质核实', desc: '机构核验权属与材料合规性', icon: 'Stamp', color: 'linear-gradient(135deg, #f59e0b, #fbbf24)' },
  { title: '勘察复评', desc: '评估师现场勘察 + 复核数据', icon: 'View', color: 'linear-gradient(135deg, #0ea5e9, #38bdf8)' },
  { title: '结果公示', desc: '出具评估报告 + 在线公示', icon: 'CircleCheck', color: 'linear-gradient(135deg, #6366f1, #818cf8)' },
]

onMounted(async () => {
  const [evs, orgs] = await Promise.all([
    request.get('/evaluator/list', { params: { page: 1, limit: 6 } }),
    request.get('/evaluation-org/list', { params: { page: 1, limit: 1 } }),
  ])
  evaluators.value = evs.data?.list || []
  stats.evaluatorCount = evs.data?.totalCount || 0
  stats.orgCount = orgs.data?.totalCount || 0
})
</script>
<style scoped>
.process-flow {
  display: flex;
  align-items: stretch;
  padding: 36px 32px;
  gap: 12px;
  margin-bottom: 36px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.5);
}
.process-step {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 14px;
  position: relative;
  text-align: center;
  padding: 16px 12px;
}
.ps-num {
  font-size: 44px;
  font-weight: 900;
  color: var(--primary-bg-2);
  line-height: 1;
}
.ps-icon {
  width: 56px; height: 56px;
  border-radius: 16px;
  color: white;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
  z-index: 1;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
}
.ps-content { z-index: 1; }
.ps-content h4 { font-size: 16px; font-weight: 700; margin: 0 0 6px; color: var(--text-primary); }
.ps-content p { font-size: 13px; color: var(--text-tertiary); margin: 0; line-height: 1.5; }
.ps-arrow { color: var(--primary-light); font-size: 24px; flex-shrink: 0; position: absolute; right: -18px; top: 50%; transform: translateY(-50%); }

.quick-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 22px;
  margin-bottom: 16px;
}
.quick-card {
  padding: 28px 24px;
  cursor: pointer;
}
.quick-card.primary {
  background: linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%);
  color: white;
}
.quick-card.primary .qc-icon { background: rgba(255,255,255,0.18); color: white; }
.qc-icon {
  width: 48px; height: 48px;
  border-radius: 14px;
  display: flex; align-items: center; justify-content: center;
  color: white;
  margin-bottom: 14px;
}
.quick-card h3 { font-size: 18px; font-weight: 700; margin: 0 0 6px; }
.quick-card p { font-size: 13px; margin: 0 0 12px; opacity: 0.85; }
.quick-card.primary p { color: rgba(255,255,255,0.9); }
.qc-action { font-size: 13px; font-weight: 600; color: var(--primary-darker); }
.quick-card.primary .qc-action { color: white; }

.evaluator-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 18px; }
.evaluator-card { padding: 24px; cursor: pointer; text-align: center; }
.evaluator-card h4 {
  font-size: 16px; font-weight: 700; margin: 12px 0 4px;
  display: inline-flex; align-items: center; gap: 6px;
}
.ev-org { font-size: 12px; color: var(--text-tertiary); margin: 0 0 10px; }
.ev-tags { display: flex; gap: 4px; flex-wrap: wrap; justify-content: center; margin-bottom: 14px; min-height: 22px; }
.ev-stats { display: flex; justify-content: space-around; padding-top: 14px; border-top: 1px dashed var(--border-light); }
.ev-stats div { text-align: center; }
.ev-stats strong { display: block; font-size: 16px; color: var(--primary-darker); }
.ev-stats small { font-size: 11px; color: var(--text-light); }
</style>

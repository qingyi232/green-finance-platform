<template>
  <div>
    <PageHeader title="披露详情" parent="信息披露" />
    <div class="page-container">
      <div class="detail eco-card" v-if="data">
        <div class="detail-header">
          <h2>{{ data.projectName }}</h2>
          <div class="header-tags">
            <el-tag size="small" :type="data.warnStatus === '已逾期' ? 'danger' : data.warnStatus === '即将到期' ? 'warning' : 'success'" effect="dark">{{ data.warnStatus }}</el-tag>
            <span class="period">{{ data.projectPeriod }}</span>
          </div>
          <p class="enterprise">{{ data.enterpriseName }}</p>
        </div>

        <div class="info-grid">
          <div class="info-card" v-if="data.progress">
            <div class="info-icon" style="background: linear-gradient(135deg, #10b981, #34d399);"><el-icon :size="20"><TrendCharts /></el-icon></div>
            <h4>项目进展</h4>
            <p>{{ data.progress }}</p>
          </div>
          <div class="info-card" v-if="data.protection">
            <div class="info-icon" style="background: linear-gradient(135deg, #0ea5e9, #38bdf8);"><el-icon :size="20"><Umbrella /></el-icon></div>
            <h4>生态保护措施</h4>
            <p>{{ data.protection }}</p>
          </div>
          <div class="info-card" v-if="data.operation">
            <div class="info-icon" style="background: linear-gradient(135deg, #f59e0b, #fbbf24);"><el-icon :size="20"><Setting /></el-icon></div>
            <h4>运营管理情况</h4>
            <p>{{ data.operation }}</p>
          </div>
          <div class="info-card" v-if="data.restoration">
            <div class="info-icon" style="background: linear-gradient(135deg, #8b5cf6, #a78bfa);"><el-icon :size="20"><MagicStick /></el-icon></div>
            <h4>生态修复落实</h4>
            <p>{{ data.restoration }}</p>
          </div>
        </div>

        <div class="disclosure-timeline">
          <div class="timeline-item" v-if="data.lastDisclosureTime">
            <el-icon><Clock /></el-icon>
            <span>上次披露时间：{{ formatDate(data.lastDisclosureTime) }}</span>
          </div>
          <div class="timeline-item" v-if="data.nextDueTime">
            <el-icon><Calendar /></el-icon>
            <span>下次披露截止：{{ formatDate(data.nextDueTime) }}</span>
          </div>
        </div>

        <el-button @click="$router.back()" style="margin-top: 24px;">返回</el-button>
      </div>
      <el-empty v-else description="暂无披露信息" />
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'
const route = useRoute()
const data = ref(null)
const formatDate = (d) => d ? new Date(d).toLocaleDateString('zh-CN') : ''
onMounted(async () => {
  try { const res = await request.get('/disclosure/detail/' + route.params.id); data.value = res.data } catch (e) {}
})
</script>
<style scoped>
.detail { padding: 36px 44px; }
.detail-header { margin-bottom: 28px; padding-bottom: 20px; border-bottom: 1px dashed var(--border-light); }
.detail-header h2 { font-size: 26px; font-weight: 800; margin: 0 0 12px; color: var(--text-primary); }
.header-tags { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.period { font-size: 13px; color: var(--text-tertiary); }
.enterprise { font-size: 15px; color: var(--primary-darker); margin: 0; font-weight: 500; }
.info-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 18px; margin-bottom: 28px; }
.info-card { padding: 22px; background: var(--primary-bg); border-radius: 14px; border: 1px solid var(--border-light); }
.info-icon { width: 40px; height: 40px; border-radius: 10px; display: flex; align-items: center; justify-content: center; color: white; margin-bottom: 12px; }
.info-card h4 { font-size: 15px; font-weight: 700; margin: 0 0 8px; color: var(--text-primary); }
.info-card p { font-size: 14px; color: var(--text-secondary); margin: 0; line-height: 1.7; }
.disclosure-timeline { display: flex; gap: 28px; padding: 16px 0; }
.timeline-item { display: flex; align-items: center; gap: 6px; font-size: 14px; color: var(--text-tertiary); }
@media (max-width: 768px) { .info-grid { grid-template-columns: 1fr; } }
</style>

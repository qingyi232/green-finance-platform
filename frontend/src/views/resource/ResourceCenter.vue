<template>
  <div>
    <PageHeader
      title="收储登记中心"
      desc="森林生态资源一张图可视化展示 · 林农/合作社线上自主登记。让生态资源底数清、可视化、可统一归集，为后续资产评估、供需交易、金融服务提供完整合规的基础数据支撑。"
      :gradient="'linear-gradient(120deg, #ecfdf5 0%, #d1fae5 70%, #6ee7b7 100%)'"
    />

    <div class="page-container">
      <!-- 双 Quick Card -->
      <div class="quick-grid">
        <div class="quick-card eco-card primary" @click="$router.push('/resource/map')">
          <div class="qc-icon"><el-icon :size="28"><Location /></el-icon></div>
          <h3>森林资源云图</h3>
          <p>全域林地资源 · 一张图可视化呈现 · 实时统计分析</p>
          <span class="qc-action">立即查看 →</span>
        </div>
        <div class="quick-card eco-card secondary" @click="$router.push('/resource/register')">
          <div class="qc-icon"><el-icon :size="28"><EditPen /></el-icon></div>
          <h3>我要登记</h3>
          <p>林农/合作社线上自主登记 · 林地/碳汇 两大资源类型</p>
          <span class="qc-action">立即登记 →</span>
        </div>
      </div>

      <!-- 资源分布概览 -->
      <div class="section-title" style="margin-top: 48px;">
        <h2>已收储资源</h2>
        <span class="more" @click="$router.push('/resource/map')">查看云图 →</span>
      </div>
      <div class="filter-bar">
        <el-radio-group v-model="filterType" size="default" @change="loadData">
          <el-radio-button label="">全部类型</el-radio-button>
          <el-radio-button label="林地">林地</el-radio-button>
          <el-radio-button label="碳汇">碳汇</el-radio-button>
          <el-radio-button label="水库">水库</el-radio-button>
          <el-radio-button label="林下产品">林下产品</el-radio-button>
        </el-radio-group>
        <el-input v-model="keyword" placeholder="搜索资源名称" clearable style="width: 220px;" @change="loadData">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
      </div>

      <div class="resource-grid">
        <div v-for="r in list" :key="r.id" class="resource-card eco-card">
          <img :src="r.cover" />
          <div class="rc-tags">
            <el-tag size="small" effect="dark" round>{{ r.resourceType }}</el-tag>
            <el-tag v-if="r.forestType" size="small" type="success" effect="plain" round>{{ r.forestType }}</el-tag>
          </div>
          <div class="rc-body">
            <h4>{{ r.resourceName }}</h4>
            <p class="rc-region"><el-icon><Location /></el-icon> {{ r.region }}</p>
            <div class="rc-stats">
              <div class="rc-stat" v-if="r.areaMu">
                <span>{{ r.areaMu }}</span>
                <small>面积(亩)</small>
              </div>
              <div class="rc-stat" v-if="r.carbonStorage">
                <span>{{ r.carbonStorage }}</span>
                <small>碳汇(吨)</small>
              </div>
            </div>
            <p class="rc-intro">{{ r.intro }}</p>
            <div class="rc-meta">
              <span><el-icon><User /></el-icon>{{ r.ownerName }}</span>
              <span><el-icon><Stamp /></el-icon>{{ r.resourceNo }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const list = ref([])
const filterType = ref('')
const keyword = ref('')

const loadData = async () => {
  try {
    const res = await request.get('/resource/list', {
      params: { page: 1, limit: 20, status: '已收储', resourceType: filterType.value, keyword: keyword.value },
    })
    list.value = res.data?.list || []
  } catch (e) {}
}

onMounted(loadData)
</script>

<style scoped>
.quick-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 22px;
  margin-top: -50px;
  position: relative;
  z-index: 2;
}
.quick-card {
  padding: 36px 36px 30px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 22px;
}
.quick-card.primary {
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.92) 0%, rgba(52, 211, 153, 0.88) 100%);
  backdrop-filter: blur(16px) saturate(180%);
  -webkit-backdrop-filter: blur(16px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  box-shadow: 0 12px 40px rgba(16, 185, 129, 0.25), inset 0 1px 0 rgba(255, 255, 255, 0.2);
}
.quick-card.secondary {
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.55);
  box-shadow: 0 8px 32px rgba(16, 185, 129, 0.08), inset 0 1px 0 rgba(255, 255, 255, 0.6);
}
.qc-icon {
  width: 56px; height: 56px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.18);
  display: flex; align-items: center; justify-content: center;
  margin-bottom: 16px;
}
.quick-card.secondary .qc-icon {
  background: var(--gradient-forest);
  color: white;
}
.quick-card h3 { font-size: 22px; font-weight: 700; margin: 0 0 8px; }
.quick-card p { font-size: 14px; opacity: 0.92; margin: 0 0 16px; line-height: 1.6; }
.quick-card.secondary p { color: var(--text-tertiary); opacity: 1; }
.qc-action { font-size: 14px; font-weight: 600; }

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 12px;
}

.resource-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 22px;
}
.resource-card {
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
.resource-card img { width: 100%; height: 180px; object-fit: cover; }
.rc-tags {
  position: absolute;
  top: 14px; left: 14px;
  display: flex;
  gap: 6px;
}
.rc-body { padding: 16px 18px 18px; flex: 1; display: flex; flex-direction: column; gap: 8px; }
.rc-body h4 { font-size: 16px; font-weight: 700; margin: 0; color: var(--text-primary); line-height: 1.4; }
.rc-region {
  display: flex; align-items: center; gap: 4px;
  font-size: 13px; color: var(--text-tertiary); margin: 0;
}
.rc-stats { display: flex; gap: 20px; padding: 8px 0; border-top: 1px dashed var(--border-light); border-bottom: 1px dashed var(--border-light); }
.rc-stat { display: flex; flex-direction: column; }
.rc-stat span { font-size: 18px; font-weight: 700; color: var(--primary-darker); }
.rc-stat small { font-size: 11px; color: var(--text-light); margin-top: 2px; }
.rc-intro {
  font-size: 13px; color: var(--text-tertiary); margin: 0; line-height: 1.6;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
}
.rc-meta {
  display: flex; justify-content: space-between;
  font-size: 12px; color: var(--text-light);
  margin-top: auto;
}
.rc-meta span { display: inline-flex; align-items: center; gap: 4px; }

@media (max-width: 1100px) {
  .quick-grid { grid-template-columns: 1fr; }
  .resource-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>

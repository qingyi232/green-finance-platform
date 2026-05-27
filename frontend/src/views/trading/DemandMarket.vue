<template>
  <div>
    <PageHeader title="资源需求市场" parent="供需交易" desc="开发类、采购类、合作类需求精准对接" />
    <div class="page-container">
      <div class="filter-bar eco-card">
        <el-radio-group v-model="filterCat" @change="loadData">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="开发类">开发类</el-radio-button>
          <el-radio-button label="采购类">采购类</el-radio-button>
          <el-radio-button label="合作类">合作类</el-radio-button>
        </el-radio-group>
        <el-input v-model="keyword" placeholder="搜索需求" style="width: 240px;" clearable @change="loadData">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-button type="warning" @click="$router.push('/trading/buy')"><el-icon><Plus /></el-icon>我要购买</el-button>
      </div>
      <div class="grid">
        <div v-for="d in list" :key="d.id" class="card eco-card" @click="$router.push('/trading/demand/' + d.id)">
          <img :src="d.cover" />
          <div class="tag"><el-tag size="small" effect="dark" round type="warning">{{ d.category }}</el-tag></div>
          <div class="body">
            <h4>{{ d.title }}</h4>
            <p>{{ d.intro }}</p>
            <div class="meta">
              <span><el-icon><Location /></el-icon>{{ d.targetRegion }}</span>
            </div>
            <div class="budget" v-if="d.budgetMin || d.budgetMax">
              <small>预算</small>
              <strong>¥{{ formatPrice(d.budgetMin) }}-{{ formatPrice(d.budgetMax) }}</strong>
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
const filterCat = ref('')
const keyword = ref('')
const formatPrice = (n) => n >= 10000 ? (n / 10000).toFixed(0) + '万' : (n || 0)
const loadData = async () => {
  const res = await request.get('/demand/list', { params: { page: 1, limit: 30, category: filterCat.value, keyword: keyword.value } })
  list.value = res.data?.list || []
}
onMounted(loadData)
</script>
<style scoped>
.filter-bar { padding: 16px 20px; display: flex; gap: 16px; align-items: center; margin-bottom: 20px; flex-wrap: wrap; }
.filter-bar > :last-child { margin-left: auto; }
.grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 18px; }
.card { position: relative; cursor: pointer; overflow: hidden; }
.card img { width: 100%; height: 180px; object-fit: cover; }
.tag { position: absolute; top: 12px; left: 12px; }
.body { padding: 16px; }
.body h4 { font-size: 15px; font-weight: 700; margin: 0 0 8px; color: var(--text-primary); line-height: 1.4; }
.body p { font-size: 13px; color: var(--text-tertiary); margin: 0 0 10px; line-height: 1.6; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.meta { font-size: 12px; color: var(--text-light); margin-bottom: 8px; }
.meta span { display: inline-flex; align-items: center; gap: 4px; }
.budget { display: flex; align-items: baseline; gap: 6px; padding-top: 10px; border-top: 1px dashed var(--border-light); }
.budget small { font-size: 12px; color: var(--text-light); }
.budget strong { font-size: 16px; color: var(--accent-amber); font-weight: 700; }
</style>

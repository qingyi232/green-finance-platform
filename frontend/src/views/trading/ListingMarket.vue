<template>
  <div>
    <PageHeader title="资源供给市场" parent="供需交易" desc="生态资源分类整合 · 集中上架 · 文化服务/供给服务/调节服务三大类别" />
    <div class="page-container">
      <div class="filter-bar eco-card">
        <el-radio-group v-model="filterCat" @change="loadData">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="文化服务">文化服务</el-radio-button>
          <el-radio-button label="供给服务">供给服务</el-radio-button>
          <el-radio-button label="调节服务">调节服务</el-radio-button>
        </el-radio-group>
        <el-input v-model="keyword" placeholder="搜索资源" style="width: 240px;" clearable @change="loadData">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-button type="primary" @click="$router.push('/trading/sell')"><el-icon><Plus /></el-icon>我要出售</el-button>
      </div>
      <div class="grid">
        <div v-for="l in list" :key="l.id" class="card eco-card" @click="$router.push('/trading/listing/' + l.id)">
          <img :src="l.cover" />
          <div class="tag"><el-tag size="small" effect="dark" round>{{ l.category }}</el-tag></div>
          <div class="body">
            <h4>{{ l.title }}</h4>
            <p>{{ l.intro }}</p>
            <div class="meta"><span><el-icon><Location /></el-icon>{{ l.region }}</span></div>
            <div class="price"><strong>¥{{ formatPrice(l.price) }}</strong></div>
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
const formatPrice = (n) => n >= 10000 ? (n / 10000).toFixed(1) + '万' : (n || 0)
const loadData = async () => {
  const res = await request.get('/listing/list', { params: { page: 1, limit: 30, category: filterCat.value, keyword: keyword.value } })
  list.value = res.data?.list || []
}
onMounted(loadData)
</script>
<style scoped>
.filter-bar { padding: 16px 20px; display: flex; gap: 16px; align-items: center; margin-bottom: 20px; flex-wrap: wrap; }
.filter-bar > :last-child { margin-left: auto; }
.grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 18px; }
.card { position: relative; cursor: pointer; overflow: hidden; }
.card img { width: 100%; height: 180px; object-fit: cover; }
.tag { position: absolute; top: 12px; left: 12px; }
.body { padding: 16px; }
.body h4 { font-size: 15px; font-weight: 700; margin: 0 0 6px; color: var(--text-primary); }
.body p { font-size: 12px; color: var(--text-tertiary); margin: 0 0 8px; line-height: 1.6; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.meta { font-size: 12px; color: var(--text-light); margin-bottom: 8px; }
.meta span { display: inline-flex; align-items: center; gap: 4px; }
.price strong { font-size: 18px; color: var(--accent-rose); font-weight: 800; }
</style>

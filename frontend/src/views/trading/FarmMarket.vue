<template>
  <div>
    <PageHeader title="地方特色农产品" parent="供需交易" desc="源头直供 · 产地直销 · 林下中药材/山林果蔬/山野特产/茶叶/食用菌" />
    <div class="page-container">
      <div class="filter-bar eco-card">
        <el-radio-group v-model="filterCat" @change="loadData">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="茶叶">茶叶</el-radio-button>
          <el-radio-button label="林下中药材">林下中药材</el-radio-button>
          <el-radio-button label="山林果蔬">山林果蔬</el-radio-button>
          <el-radio-button label="山野特产">山野特产</el-radio-button>
          <el-radio-button label="食用菌">食用菌</el-radio-button>
        </el-radio-group>
        <el-input v-model="keyword" placeholder="搜索产品" style="width: 240px;" clearable @change="loadData">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
      </div>
      <div class="grid">
        <div v-for="f in list" :key="f.id" class="card eco-card" @click="$router.push('/trading/farm/' + f.id)">
          <img :src="f.cover" />
          <div class="body">
            <h4>{{ f.name }}</h4>
            <p>{{ f.intro }}</p>
            <div class="tags">
              <el-tag v-for="t in (f.tags || '').split(',').slice(0, 3)" :key="t" size="small" type="success" effect="plain" round>{{ t }}</el-tag>
            </div>
            <div class="bottom">
              <div class="price"><span>¥</span>{{ f.price }}<small>/{{ f.unit }}</small></div>
              <span class="sales">已售 {{ f.sales }}</span>
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
const loadData = async () => {
  const res = await request.get('/farm-product/list', { params: { page: 1, limit: 32, category: filterCat.value, keyword: keyword.value } })
  list.value = res.data?.list || []
}
onMounted(loadData)
</script>
<style scoped>
.filter-bar { padding: 16px 20px; display: flex; gap: 16px; align-items: center; margin-bottom: 20px; flex-wrap: wrap; }
.filter-bar > :last-child { margin-left: auto; }
.grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 18px; }
.card { cursor: pointer; overflow: hidden; }
.card img { width: 100%; height: 180px; object-fit: cover; }
.body { padding: 14px; }
.body h4 { font-size: 14px; font-weight: 700; margin: 0 0 4px; }
.body p { font-size: 12px; color: var(--text-tertiary); margin: 0 0 8px; display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden; }
.tags { display: flex; gap: 4px; flex-wrap: wrap; min-height: 22px; margin-bottom: 8px; }
.bottom { display: flex; align-items: baseline; justify-content: space-between; padding-top: 8px; border-top: 1px dashed var(--border-light); }
.price { font-size: 20px; color: var(--accent-rose); font-weight: 800; }
.price span { font-size: 12px; }
.price small { font-size: 11px; color: var(--text-light); font-weight: 400; }
.sales { font-size: 11px; color: var(--text-light); }
</style>

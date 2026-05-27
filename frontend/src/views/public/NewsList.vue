<template>
  <div>
    <PageHeader title="新闻动态 · 政策资讯" parent="首页" desc="实时推送行业动态、平台公告、案例分享、技术资讯，掌握最新生态银行实践" />
    <div class="page-container">
      <div class="filter-bar eco-card">
        <el-radio-group v-model="filterCat" @change="loadData">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="行业动态">行业动态</el-radio-button>
          <el-radio-button label="平台公告">平台公告</el-radio-button>
          <el-radio-button label="案例分享">案例分享</el-radio-button>
          <el-radio-button label="政策解读">政策解读</el-radio-button>
          <el-radio-button label="技术资讯">技术资讯</el-radio-button>
        </el-radio-group>
        <el-input v-model="keyword" placeholder="搜索标题" clearable @change="loadData" style="width: 240px;">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
      </div>

      <!-- 头条 -->
      <div class="news-feature eco-card" v-if="topNews" @click="$router.push('/news/' + topNews.id)">
        <img :src="topNews.cover" />
        <div class="nf-body">
          <el-tag effect="dark" round>{{ topNews.category }}</el-tag>
          <h2>{{ topNews.title }}</h2>
          <p>{{ topNews.summary }}</p>
          <div class="nf-meta">
            <span><el-icon><User /></el-icon>{{ topNews.author }}</span>
            <span><el-icon><Clock /></el-icon>{{ formatDate(topNews.publishedTime) }}</span>
            <span><el-icon><View /></el-icon>{{ topNews.viewCount }} 阅读</span>
          </div>
        </div>
      </div>

      <!-- 列表 -->
      <div class="news-grid">
        <div v-for="n in restNews" :key="n.id" class="news-card eco-card" @click="$router.push('/news/' + n.id)">
          <img :src="n.cover" />
          <div class="nc-body">
            <el-tag size="small" effect="plain" round>{{ n.category }}</el-tag>
            <h4>{{ n.title }}</h4>
            <p>{{ n.summary }}</p>
            <div class="nc-meta">
              <span>{{ formatDate(n.publishedTime) }}</span>
              <span><el-icon><View /></el-icon>{{ n.viewCount }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="pager">
        <el-pagination background :current-page="page" :page-size="limit" :total="total" layout="prev, pager, next" @current-change="onPage" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const list = ref([])
const total = ref(0)
const page = ref(1)
const limit = ref(13)
const filterCat = ref('')
const keyword = ref('')

const topNews = computed(() => list.value[0])
const restNews = computed(() => list.value.slice(1))

const formatDate = (d) => {
  if (!d) return ''
  const date = new Date(d)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

const loadData = async () => {
  const res = await request.get('/news/list', { params: { page: page.value, limit: limit.value, category: filterCat.value, keyword: keyword.value } })
  list.value = res.data?.list || []
  total.value = res.data?.totalCount || 0
}
const onPage = (p) => { page.value = p; loadData() }
onMounted(loadData)
</script>
<style scoped>
.filter-bar { padding: 16px 20px; display: flex; gap: 16px; align-items: center; margin-bottom: 22px; flex-wrap: wrap; }
.filter-bar > :last-child { margin-left: auto; }

.news-feature {
  display: grid;
  grid-template-columns: 1.1fr 1fr;
  gap: 24px;
  padding: 20px;
  cursor: pointer;
  margin-bottom: 22px;
}
.news-feature img { width: 100%; height: 280px; object-fit: cover; border-radius: 14px; }
.nf-body { display: flex; flex-direction: column; justify-content: center; gap: 14px; padding: 12px 8px; }
.nf-body h2 { font-size: 26px; font-weight: 800; line-height: 1.4; margin: 0; color: var(--text-primary); }
.nf-body p { font-size: 14px; color: var(--text-secondary); line-height: 1.8; margin: 0;
  display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden; }
.nf-meta { display: flex; gap: 18px; font-size: 13px; color: var(--text-light); }
.nf-meta span { display: inline-flex; align-items: center; gap: 4px; }

.news-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 22px; }
.news-card { cursor: pointer; overflow: hidden; }
.news-card img { width: 100%; height: 200px; object-fit: cover; }
.nc-body { padding: 16px 18px; display: flex; flex-direction: column; gap: 8px; }
.nc-body h4 { font-size: 15px; font-weight: 700; margin: 0; color: var(--text-primary); line-height: 1.5; }
.nc-body p { font-size: 13px; color: var(--text-tertiary); margin: 0; line-height: 1.7;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.nc-meta { display: flex; justify-content: space-between; font-size: 12px; color: var(--text-light); margin-top: auto; padding-top: 8px; border-top: 1px dashed var(--border-light); }
.nc-meta span { display: inline-flex; align-items: center; gap: 4px; }

.pager { margin: 32px 0 16px; display: flex; justify-content: center; }

@media (max-width: 1100px) {
  .news-feature { grid-template-columns: 1fr; }
  .news-feature img { height: 200px; }
  .news-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>

<template>
  <div>
    <div class="hero" v-if="data">
      <img :src="data.cover" class="hero-img" />
      <div class="hero-mask"></div>
      <div class="hero-content page-container">
        <el-tag effect="dark" round>{{ data.category }}</el-tag>
        <h1>{{ data.title }}</h1>
        <div class="hero-meta">
          <span><el-icon><User /></el-icon>{{ data.author }}</span>
          <span><el-icon><Clock /></el-icon>{{ formatDateTime(data.publishedTime) }}</span>
          <span><el-icon><View /></el-icon>{{ data.viewCount }} 阅读</span>
          <span v-if="data.source"><el-icon><Link /></el-icon>{{ data.source }}</span>
        </div>
      </div>
    </div>
    <div class="page-container">
      <div class="article-grid">
        <article class="article eco-card" v-if="data">
          <p class="summary">{{ data.summary }}</p>
          <div class="content" v-html="renderedContent"></div>
          <div class="article-footer">
            <el-button @click="$router.push('/news')"><el-icon><ArrowLeft /></el-icon>返回列表</el-button>
            <el-button type="primary" @click="$router.push('/news')">查看更多新闻 →</el-button>
          </div>
        </article>
        <aside class="aside">
          <div class="aside-card eco-card">
            <h4>热门资讯</h4>
            <div v-for="h in hotNews" :key="h.id" class="aside-item" @click="goNews(h.id)">
              <img :src="h.cover" />
              <div>
                <p>{{ h.title }}</p>
                <small>{{ formatDate(h.publishedTime) }} · {{ h.viewCount }} 阅读</small>
              </div>
            </div>
          </div>
          <div class="aside-card eco-card aside-cta">
            <el-icon :size="32"><ChatLineRound /></el-icon>
            <h4>有任何反馈？</h4>
            <p>立即告诉我们，平台 1 个工作日内响应</p>
            <el-button type="primary" round @click="$router.push('/feedback')">问题反馈</el-button>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { marked } from 'marked'
import request from '../../api/request'

const route = useRoute()
const router = useRouter()
const data = ref(null)
const hotNews = ref([])

const renderedContent = computed(() => data.value?.content ? marked(data.value.content.replace(/\\n/g, '\n')) : '')

const formatDate = (d) => d ? new Date(d).toLocaleDateString('zh-CN') : ''
const formatDateTime = (d) => d ? new Date(d).toLocaleString('zh-CN') : ''

const load = async () => {
  const res = await request.get('/news/detail/' + route.params.id)
  data.value = res.data
  const hot = await request.get('/news/hot', { params: { limit: 5 } })
  hotNews.value = (hot.data || []).filter((h) => h.id != route.params.id).slice(0, 4)
}
const goNews = (id) => router.push('/news/' + id)

onMounted(load)
watch(() => route.params.id, load)
</script>
<style scoped>
.hero {
  position: relative;
  height: 400px;
  overflow: hidden;
  margin-top: -32px;
  margin-bottom: 0;
}
.hero-img { width: 100%; height: 100%; object-fit: cover; filter: brightness(0.7); }
.hero-mask {
  position: absolute; inset: 0;
  background: linear-gradient(180deg, transparent 0%, rgba(0,0,0,0.5) 70%, rgba(15,23,42,0.85) 100%);
}
.hero-content {
  position: absolute;
  bottom: 50px; left: 50%;
  transform: translateX(-50%);
  width: 100%;
  color: white;
}
.hero-content h1 { font-size: 36px; font-weight: 800; line-height: 1.3; margin: 14px 0 18px; max-width: 900px; }
.hero-meta { display: flex; gap: 24px; font-size: 14px; opacity: 0.9; }
.hero-meta span { display: inline-flex; align-items: center; gap: 6px; }

.article-grid { display: grid; grid-template-columns: 1fr 320px; gap: 22px; margin-top: -50px; position: relative; z-index: 2; }
.article { padding: 40px 48px; }
.summary {
  font-size: 16px; line-height: 1.85;
  color: var(--text-secondary);
  padding: 16px 20px;
  background: var(--primary-bg);
  border-left: 4px solid var(--primary);
  border-radius: 6px;
  margin-bottom: 28px;
}
.content { line-height: 1.9; color: var(--text-primary); font-size: 15px; }
.content :deep(h1), .content :deep(h2), .content :deep(h3) {
  font-weight: 700;
  color: var(--text-primary);
  margin: 28px 0 14px;
}
.content :deep(h2) { font-size: 22px; padding-bottom: 8px; border-bottom: 2px solid var(--primary-bg-2); }
.content :deep(h3) { font-size: 18px; }
.content :deep(p) { margin: 14px 0; }
.content :deep(ul), .content :deep(ol) { padding-left: 24px; }
.content :deep(li) { margin: 6px 0; }
.content :deep(strong) { color: var(--primary-darker); }
.content :deep(a) { color: var(--primary); }
.content :deep(blockquote) { border-left: 3px solid var(--primary); padding-left: 14px; color: var(--text-tertiary); margin: 16px 0; }

.article-footer {
  margin-top: 36px;
  padding-top: 20px;
  border-top: 1px dashed var(--border-light);
  display: flex; justify-content: space-between;
}

.aside { display: flex; flex-direction: column; gap: 18px; }
.aside-card { padding: 22px; }
.aside-card h4 { font-size: 15px; font-weight: 700; margin: 0 0 16px; color: var(--text-primary); display: flex; align-items: center; gap: 8px; }
.aside-card h4::before { content: ''; width: 3px; height: 14px; background: var(--primary); border-radius: 2px; }
.aside-item {
  display: flex; gap: 12px; cursor: pointer;
  padding: 10px 0;
  border-bottom: 1px dashed var(--border-light);
}
.aside-item:last-child { border-bottom: none; }
.aside-item:hover p { color: var(--primary); }
.aside-item img { width: 80px; height: 60px; object-fit: cover; border-radius: 8px; flex-shrink: 0; }
.aside-item p { font-size: 13px; font-weight: 500; margin: 0 0 4px; line-height: 1.4;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.aside-item small { font-size: 11px; color: var(--text-light); }
.aside-cta { text-align: center; padding: 28px 22px; background: linear-gradient(135deg, #ecfdf5, #d1fae5); }
.aside-cta .el-icon { color: var(--primary); margin-bottom: 8px; }
.aside-cta h4 { justify-content: center; margin-bottom: 8px; }
.aside-cta h4::before { display: none; }
.aside-cta p { font-size: 13px; color: var(--text-tertiary); margin: 0 0 14px; }

@media (max-width: 1100px) { .article-grid { grid-template-columns: 1fr; } .article { padding: 28px; } }
</style>

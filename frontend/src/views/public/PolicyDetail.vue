<template>
  <div>
    <PageHeader title="政策详情" parent="政策解读" />
    <div class="page-container">
      <div class="detail eco-card" v-if="data">
        <div class="detail-header">
          <div class="detail-tags">
            <el-tag :type="levelTag(data.level)" effect="dark" round>{{ data.level }}</el-tag>
            <el-tag v-if="data.category" effect="plain" round>{{ data.category }}</el-tag>
          </div>
          <h2>{{ data.title }}</h2>
          <div class="detail-info">
            <span v-if="data.issueOrg"><el-icon><OfficeBuilding /></el-icon>{{ data.issueOrg }}</span>
            <span v-if="data.issueNo"><el-icon><Document /></el-icon>{{ data.issueNo }}</span>
            <span v-if="data.issueDate"><el-icon><Calendar /></el-icon>{{ data.issueDate }}</span>
          </div>
        </div>
        <div class="summary-box" v-if="data.summary">
          <h4>政策要点</h4>
          <p>{{ data.summary }}</p>
        </div>
        <div class="content-section" v-if="renderedContent">
          <h3>政策正文</h3>
          <div class="content" v-html="renderedContent"></div>
        </div>
        <div class="content-section" v-if="renderedInterpretation">
          <h3>政策解读</h3>
          <div class="content" v-html="renderedInterpretation"></div>
        </div>
        <el-button @click="$router.back()" style="margin-top: 24px;">返回列表</el-button>
      </div>
      <el-empty v-else description="政策信息加载中..." />
    </div>
  </div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { marked } from 'marked'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'
const route = useRoute()
const data = ref(null)
const renderedContent = computed(() => data.value?.content ? marked(data.value.content.replace(/\\n/g, '\n')) : '')
const renderedInterpretation = computed(() => data.value?.interpretation ? marked(data.value.interpretation.replace(/\\n/g, '\n')) : '')
const levelTag = (level) => {
  const m = { '国家级': 'danger', '省级': 'warning', '市级': '', '县级': 'info' }
  return m[level] || 'info'
}
onMounted(async () => {
  try { const res = await request.get('/policy/detail/' + route.params.id); data.value = res.data } catch (e) {}
})
</script>
<style scoped>
.detail { padding: 40px 48px; }
.detail-header { margin-bottom: 28px; padding-bottom: 20px; border-bottom: 1px dashed var(--border-light); }
.detail-tags { display: flex; gap: 8px; margin-bottom: 14px; }
.detail h2 { font-size: 28px; font-weight: 800; margin: 0 0 14px; color: var(--text-primary); line-height: 1.4; }
.detail-info { display: flex; gap: 20px; font-size: 14px; color: var(--text-tertiary); }
.detail-info span { display: inline-flex; align-items: center; gap: 5px; }
.summary-box { padding: 20px 24px; background: var(--primary-bg); border-left: 4px solid var(--primary); border-radius: 8px; margin-bottom: 28px; }
.summary-box h4 { font-size: 15px; font-weight: 700; color: var(--primary-darker); margin: 0 0 8px; }
.summary-box p { font-size: 15px; color: var(--text-secondary); margin: 0; line-height: 1.8; }
.content-section { margin-bottom: 28px; }
.content-section h3 { font-size: 18px; font-weight: 700; color: var(--text-primary); margin: 0 0 16px; padding-left: 12px; border-left: 3px solid var(--primary); }
.content { line-height: 1.9; color: var(--text-secondary); font-size: 15px; }
.content :deep(h2) { font-size: 18px; font-weight: 700; margin: 24px 0 12px; color: var(--primary-darker); }
.content :deep(ul) { padding-left: 20px; }
.content :deep(p) { margin: 12px 0; }
</style>

<template>
  <div>
    <PageHeader title="农产品详情" parent="供需交易" />
    <div class="page-container">
      <div class="detail eco-card" v-if="data">
        <h2>{{ data.title || data.name || data.orgName || data.realName || data.projectName }}</h2>
        <p class="meta">{{ data.summary || data.intro }}</p>
        <div class="content" v-html="renderedContent"></div>
        <el-button @click="$router.back()">返回</el-button>
      </div>
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
const renderedContent = computed(() => data.value?.content ? marked(data.value.content) : '')
onMounted(async () => {
  try { const res = await request.get('/farm-product/detail/' + route.params.id); data.value = res.data } catch (e) {}
})
</script>
<style scoped>
.detail { padding: 36px 44px; }
.detail h2 { font-size: 26px; font-weight: 800; margin: 0 0 12px; }
.meta { font-size: 14px; color: var(--text-tertiary); margin-bottom: 24px; padding-bottom: 16px; border-bottom: 1px dashed var(--border-light); }
.content { line-height: 1.9; color: var(--text-secondary); }
.content :deep(h2) { font-size: 18px; font-weight: 700; margin: 24px 0 12px; color: var(--primary-darker); }
.content :deep(ul) { padding-left: 20px; }
</style>

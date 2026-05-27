<template>
  <div>
    <PageHeader title="政策解读" parent="首页" />
    <div class="page-container">
      <div class="filter-bar eco-card">
        <el-input v-model="keyword" placeholder="搜索" clearable style="width: 240px;" @change="loadData">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <div class="grid">
        <div v-for="item in list" :key="item.id" class="grid-card eco-card" @click="onClick(item)">
          <div class="gc-header">
            <el-tag size="small" :type="levelTag(item.level)" effect="dark" round>{{ item.level }}</el-tag>
            <el-tag size="small" v-if="item.category" effect="plain" round>{{ item.category }}</el-tag>
          </div>
          <div class="gc-title">{{ item.title }}</div>
          <div class="gc-meta" v-if="item.summary">{{ item.summary }}</div>
          <div class="gc-footer">
            <span v-if="item.issueOrg">{{ item.issueOrg }}</span>
            <span v-if="item.issueDate">{{ item.issueDate }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'
const router = useRouter()
const list = ref([])
const keyword = ref('')
const loadData = async () => {
  try {
    const res = await request.get('/policy/list', { params: { page: 1, limit: 30, keyword: keyword.value } })
    list.value = res.data?.list || []
  } catch (e) {}
}
const onClick = (item) => { router.push('/policy/' + item.id) }
const levelTag = (level) => {
  const m = { '国家级': 'danger', '省级': 'warning', '市级': '', '县级': 'info' }
  return m[level] || 'info'
}
onMounted(loadData)
</script>
<style scoped>
.filter-bar { padding: 16px 20px; display: flex; gap: 12px; margin-bottom: 20px; }
.grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 18px; }
.grid-card { padding: 22px; cursor: pointer; display: flex; flex-direction: column; }
.gc-title { font-size: 16px; font-weight: 700; color: var(--text-primary); margin-bottom: 8px; }
.gc-meta { font-size: 13px; color: var(--text-tertiary); line-height: 1.6; margin-bottom: 12px;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.gc-header { display: flex; gap: 8px; margin-bottom: 12px; }
.gc-footer { display: flex; justify-content: space-between; font-size: 12px; color: var(--text-light); padding-top: 12px; border-top: 1px dashed var(--border-light); margin-top: auto; }
</style>

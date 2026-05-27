<template>
  <div>
    <PageHeader title="信息披露" parent="首页" />
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
            <el-tag size="small" :type="item.warnStatus === '已逾期' ? 'danger' : item.warnStatus === '即将到期' ? 'warning' : 'success'" effect="dark">{{ item.warnStatus }}</el-tag>
            <span class="gc-period">{{ item.projectPeriod }}</span>
          </div>
          <div class="gc-title">{{ item.projectName }}</div>
          <div class="gc-enterprise">{{ item.enterpriseName }}</div>
          <div class="gc-meta" v-if="item.progress">{{ item.progress }}</div>
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
    const res = await request.get('/disclosure/list', { params: { page: 1, limit: 30, keyword: keyword.value } })
    list.value = res.data?.list || []
  } catch (e) {}
}
const onClick = (item) => { router.push('/disclosure/' + item.id) }
onMounted(loadData)
</script>
<style scoped>
.filter-bar { padding: 16px 20px; display: flex; gap: 12px; margin-bottom: 20px; }
.grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 18px; }
.grid-card { padding: 22px; cursor: pointer; }
.gc-title { font-size: 16px; font-weight: 700; color: var(--text-primary); margin-bottom: 8px; }
.gc-meta { font-size: 13px; color: var(--text-tertiary); line-height: 1.6; margin-bottom: 12px;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.gc-header { display: flex; align-items: center; gap: 10px; margin-bottom: 10px; }
.gc-period { font-size: 12px; color: var(--text-light); }
.gc-enterprise { font-size: 13px; color: var(--primary-darker); margin-bottom: 8px; font-weight: 500; }
</style>

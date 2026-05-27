<template>
  <div>
    <PageHeader title="法律援助机构列表" parent="法律服务" />
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
            <el-avatar :size="48" :src="item.logo" shape="square" />
            <div>
              <div class="gc-title">{{ item.orgName }}</div>
              <el-tag size="small" :type="(item.orgType || '').includes('公益') ? 'success' : (item.orgType || '').includes('高校') ? 'warning' : ''" effect="plain" round>{{ item.orgType }}</el-tag>
            </div>
          </div>
          <div class="gc-meta" v-if="item.intro">{{ item.intro }}</div>
          <div class="gc-info">
            <span><el-icon><Location /></el-icon>{{ item.serviceArea }}</span>
            <span><el-icon><Phone /></el-icon>{{ item.phone }}</span>
          </div>
          <div class="gc-tags">
            <el-tag size="small" v-for="t in (item.expertise || '').split('、')" :key="t" effect="plain" round>{{ t }}</el-tag>
          </div>
          <div class="gc-stats">
            <span>{{ item.lawyerCount }} 位律师</span>
            <span>{{ item.caseCount }} 个案件</span>
            <span>评分 {{ item.rating }}</span>
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
    const res = await request.get('/legal-org/list', { params: { page: 1, limit: 30, keyword: keyword.value } })
    list.value = res.data?.list || []
  } catch (e) {}
}
const onClick = (item) => { router.push('/legal/consult?orgId=' + item.id) }
onMounted(loadData)
</script>
<style scoped>
.filter-bar { padding: 16px 20px; display: flex; gap: 12px; margin-bottom: 20px; }
.grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 18px; }
.grid-card { padding: 22px; cursor: pointer; }
.gc-title { font-size: 16px; font-weight: 700; color: var(--text-primary); margin-bottom: 8px; }
.gc-meta { font-size: 13px; color: var(--text-tertiary); line-height: 1.6; margin-bottom: 12px;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.gc-header { display: flex; gap: 14px; align-items: center; margin-bottom: 12px; }
.gc-info { display: flex; gap: 16px; font-size: 12px; color: var(--text-light); margin-bottom: 10px; }
.gc-info span { display: inline-flex; align-items: center; gap: 4px; }
.gc-tags { display: flex; gap: 6px; flex-wrap: wrap; margin-bottom: 12px; }
.gc-stats { display: flex; gap: 16px; font-size: 12px; color: var(--text-tertiary); padding-top: 12px; border-top: 1px dashed var(--border-light); }
</style>

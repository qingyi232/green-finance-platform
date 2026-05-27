<template>
  <div>
    <PageHeader title="评估师列表" parent="资产评估" />
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
            <el-avatar :size="56" :src="item.avatar" />
            <div>
              <div class="gc-title">{{ item.name }} <el-tag size="small" :type="item.gender === '女' ? 'danger' : 'primary'" effect="plain">{{ item.gender }}</el-tag></div>
              <div class="gc-sub">{{ item.orgName }}</div>
            </div>
          </div>
          <div class="gc-cert">{{ item.qualification }}</div>
          <div class="gc-meta" v-if="item.intro">{{ item.intro }}</div>
          <div class="gc-tags">
            <el-tag size="small" v-for="t in (item.expertise || '').split('、')" :key="t" effect="plain" round>{{ t }}</el-tag>
          </div>
          <div class="gc-stats">
            <div><strong>{{ item.years }}</strong><small>年经验</small></div>
            <div><strong>{{ item.caseCount }}</strong><small>案例</small></div>
            <div><strong>{{ item.rating }}</strong><small>评分</small></div>
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
    const res = await request.get('/evaluator/list', { params: { page: 1, limit: 30, keyword: keyword.value } })
    list.value = res.data?.list || []
  } catch (e) {}
}
const onClick = (item) => { router.push('/evaluation/apply?evaluatorId=' + item.id) }
onMounted(loadData)
</script>
<style scoped>
.filter-bar { padding: 16px 20px; display: flex; gap: 12px; margin-bottom: 20px; }
.grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 18px; }
.grid-card { padding: 22px; cursor: pointer; }
.gc-title { font-size: 16px; font-weight: 700; color: var(--text-primary); margin-bottom: 8px; }
.gc-meta { font-size: 13px; color: var(--text-tertiary); line-height: 1.6; margin-bottom: 12px;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.gc-header { display: flex; gap: 14px; align-items: center; margin-bottom: 10px; }
.gc-sub { font-size: 12px; color: var(--text-light); margin-top: 4px; }
.gc-cert { font-size: 12px; color: var(--primary-darker); background: var(--primary-bg); padding: 4px 10px; border-radius: 6px; margin-bottom: 10px; display: inline-block; }
.gc-tags { display: flex; gap: 6px; flex-wrap: wrap; margin-bottom: 14px; }
.gc-stats { display: flex; justify-content: space-around; padding-top: 14px; border-top: 1px dashed var(--border-light); text-align: center; }
.gc-stats strong { display: block; font-size: 16px; color: var(--primary-darker); }
.gc-stats small { font-size: 11px; color: var(--text-light); }
</style>

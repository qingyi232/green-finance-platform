<template>
  <div>
    <PageHeader title="金融产品列表" parent="金融服务" />
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
            <el-avatar :size="44" :src="item.bankLogo" shape="square" />
            <div>
              <div class="gc-title">{{ item.name }}</div>
              <div class="gc-sub">{{ item.bankName }}</div>
            </div>
          </div>
          <el-tag size="small" effect="dark" round>{{ item.category }}</el-tag>
          <div class="gc-meta" v-if="item.intro">{{ item.intro }}</div>
          <div class="gc-info-grid">
            <div class="gc-info-item">
              <span class="label">额度</span>
              <span class="value">{{ (item.loanMin / 10000).toFixed(0) }}-{{ (item.loanMax / 10000).toFixed(0) }}万</span>
            </div>
            <div class="gc-info-item">
              <span class="label">利率</span>
              <span class="value">{{ item.rateMin }}%-{{ item.rateMax }}%</span>
            </div>
            <div class="gc-info-item">
              <span class="label">期限</span>
              <span class="value">{{ item.termMin }}-{{ item.termMax }}月</span>
            </div>
          </div>
          <div class="gc-action">查看详情 →</div>
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
    const res = await request.get('/finance-product/list', { params: { page: 1, limit: 30, keyword: keyword.value } })
    list.value = res.data?.list || []
  } catch (e) {}
}
const onClick = (item) => { router.push('/finance/product/' + item.id) }
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
.gc-sub { font-size: 12px; color: var(--text-light); margin-top: 4px; }
.gc-info-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; margin: 14px 0; }
.gc-info-item { text-align: center; padding: 8px; background: var(--primary-bg); border-radius: 8px; }
.gc-info-item .label { display: block; font-size: 11px; color: var(--text-light); margin-bottom: 2px; }
.gc-info-item .value { font-size: 13px; font-weight: 600; color: var(--primary-darker); }
.gc-action { font-size: 13px; font-weight: 600; color: var(--primary-darker); margin-top: auto; }
</style>

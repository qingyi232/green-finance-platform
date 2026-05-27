<template>
  <div>
    <PageHeader
      title="金融服务中心"
      desc="绿色金融产品集中展示 · 产品在线智能匹配 · 农户线上申请 · 银行端线上审批"
      :gradient="'linear-gradient(120deg, #eff6ff 0%, #dbeafe 70%, #bfdbfe 100%)'"
    />
    <div class="page-container">
      <!-- 入口 -->
      <div class="entry-grid">
        <div class="entry-card eco-card primary" @click="$router.push('/finance/products')">
          <div class="ec-icon"><el-icon :size="26"><Money /></el-icon></div>
          <h3>{{ stats.financeProductCount }} 款<small>专属绿色金融产品</small></h3>
          <p>碳汇收储贷 · 两山农林贷 · 绿色工厂贷 · 碳中和助力贷</p>
          <span class="ec-action">浏览产品 →</span>
        </div>
        <div class="entry-card eco-card" @click="$router.push('/finance/match')">
          <div class="ec-icon" style="background: linear-gradient(135deg, #6366f1, #818cf8);"><el-icon :size="26"><Compass /></el-icon></div>
          <h3>我要匹配</h3>
          <p>输入金额、期限、用途，1 秒匹配最适合的金融产品</p>
          <span class="ec-action">立即匹配 →</span>
        </div>
        <div class="entry-card eco-card" @click="$router.push('/finance/apply')">
          <div class="ec-icon" style="background: linear-gradient(135deg, #10b981, #34d399);"><el-icon :size="26"><EditPen /></el-icon></div>
          <h3>我要申请</h3>
          <p>线上提交贷款申请，无需银行排队</p>
          <span class="ec-action">立即申请 →</span>
        </div>
        <div class="entry-card eco-card" @click="$router.push('/finance/review')" v-if="userStore.role === '金融机构'">
          <div class="ec-icon" style="background: linear-gradient(135deg, #f59e0b, #fbbf24);"><el-icon :size="26"><CircleCheckFilled /></el-icon></div>
          <h3>我要审批</h3>
          <p>银行端在线审批 · 简化审核流程</p>
          <span class="ec-action">进入审批 →</span>
        </div>
      </div>

      <!-- 产品分类 -->
      <div class="section-title" style="margin-top: 48px;">
        <h2>专属绿色金融产品</h2>
        <span class="more" @click="$router.push('/finance/products')">全部产品 →</span>
      </div>
      <div class="filter-row">
        <el-radio-group v-model="filterCat" @change="loadProducts" size="default">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="碳汇收储贷">碳汇收储贷</el-radio-button>
          <el-radio-button label="两山农林贷">两山农林贷</el-radio-button>
          <el-radio-button label="绿色工厂贷">绿色工厂贷</el-radio-button>
          <el-radio-button label="碳中和助力贷">碳中和助力贷</el-radio-button>
          <el-radio-button label="两山乡居贷">两山乡居贷</el-radio-button>
        </el-radio-group>
      </div>

      <div class="product-grid">
        <div v-for="p in products" :key="p.id" class="product-card eco-card" @click="$router.push('/finance/product/' + p.id)">
          <div class="pc-header">
            <el-avatar :size="44" :src="p.bankLogo" shape="square" />
            <div>
              <h4>{{ p.name }}</h4>
              <p>{{ p.bankName }}</p>
            </div>
            <el-tag size="small" effect="dark" round>{{ p.category }}</el-tag>
          </div>
          <p class="pc-intro">{{ p.intro }}</p>
          <div class="pc-stats">
            <div class="pc-stat">
              <small>额度范围</small>
              <span>¥{{ formatRange(p.loanMin, p.loanMax) }}</span>
            </div>
            <div class="pc-stat">
              <small>年利率</small>
              <span>{{ p.rateMin }}% - {{ p.rateMax }}%</span>
            </div>
            <div class="pc-stat">
              <small>期限</small>
              <span>{{ p.termMin }}-{{ p.termMax }} 个月</span>
            </div>
          </div>
          <div class="pc-bottom">
            <span class="pc-tag" v-for="t in (p.tags || '').split(',').slice(0, 3)" :key="t">{{ t }}</span>
            <el-button type="primary" size="small" round>立即申请</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../api/request'
import { useUserStore } from '../../store'
import PageHeader from '../../components/PageHeader.vue'

const userStore = useUserStore()
const products = ref([])
const filterCat = ref('')
const stats = reactive({ financeProductCount: 0 })

const formatRange = (a, b) => {
  if (!a && !b) return '-'
  const fmt = (n) => n >= 10000 ? (n / 10000) + '万' : n
  return `${fmt(a)} - ${fmt(b)}`
}

const loadProducts = async () => {
  const res = await request.get('/finance-product/list', { params: { page: 1, limit: 12, category: filterCat.value } })
  products.value = res.data?.list || []
  stats.financeProductCount = res.data?.totalCount || 0
}

onMounted(loadProducts)
</script>
<style scoped>
.entry-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 18px; }
.entry-card { padding: 24px 22px; cursor: pointer; min-height: 180px; display: flex; flex-direction: column; }
.entry-card.primary {
  background: linear-gradient(135deg, #047857 0%, #10b981 100%);
  color: white;
}
.entry-card .ec-icon {
  width: 50px; height: 50px;
  border-radius: 14px;
  background: rgba(16,185,129,0.1);
  color: var(--primary-darker);
  display: flex; align-items: center; justify-content: center;
  margin-bottom: 14px;
}
.entry-card.primary .ec-icon { background: rgba(255,255,255,0.18); color: white; }
.entry-card h3 { font-size: 24px; font-weight: 800; margin: 0 0 6px; display: flex; align-items: baseline; gap: 6px; }
.entry-card h3 small { font-size: 13px; font-weight: 500; opacity: 0.85; }
.entry-card p { font-size: 13px; margin: 0 0 14px; opacity: 0.85; line-height: 1.6; }
.entry-card.primary p { color: rgba(255,255,255,0.92); }
.ec-action { font-size: 13px; font-weight: 600; color: var(--primary-darker); margin-top: auto; }
.entry-card.primary .ec-action { color: white; }

.filter-row { margin-bottom: 20px; }

.product-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 22px; }
.product-card { padding: 22px; cursor: pointer; }
.pc-header { display: flex; gap: 12px; align-items: flex-start; margin-bottom: 14px; }
.pc-header > div:nth-child(2) { flex: 1; }
.pc-header h4 { font-size: 15px; font-weight: 700; margin: 0 0 4px; color: var(--text-primary); }
.pc-header p { font-size: 12px; color: var(--text-tertiary); margin: 0; }

.pc-intro { font-size: 13px; color: var(--text-tertiary); margin: 0 0 14px; line-height: 1.6;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; min-height: 42px; }

.pc-stats { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; padding: 12px; background: var(--primary-bg); border-radius: 10px; margin-bottom: 14px; }
.pc-stat { display: flex; flex-direction: column; align-items: center; }
.pc-stat small { font-size: 11px; color: var(--text-tertiary); }
.pc-stat span { font-size: 13px; font-weight: 700; color: var(--primary-darker); margin-top: 2px; text-align: center; }

.pc-bottom { display: flex; align-items: center; gap: 6px; }
.pc-tag {
  font-size: 11px; padding: 2px 8px;
  background: white;
  border: 1px dashed var(--primary-light);
  color: var(--primary-darker);
  border-radius: 4px;
}
.pc-bottom > .el-button { margin-left: auto; }

@media (max-width: 1100px) {
  .entry-grid { grid-template-columns: repeat(2, 1fr); }
  .product-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>

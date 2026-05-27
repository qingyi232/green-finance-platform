<template>
  <div>
    <PageHeader
      title="供需交易中心"
      desc="资源供给 + 资源需求双市场 · 自主挂牌、自主购买 · 在线预约洽谈，盘活生态资源价值"
      :gradient="'linear-gradient(120deg, #ecfdf5 0%, #d1fae5 50%, #a7f3d0 100%)'"
    />
    <div class="page-container">
      <!-- 四大入口 -->
      <div class="entry-grid">
        <div class="entry-card eco-card supply" @click="$router.push('/trading/listings')">
          <div class="ec-tag">资源供给市场</div>
          <h3>{{ stats.listingCount }}<small>个挂牌资源</small></h3>
          <p>林地、碳汇、林下产品、水库 · 整合上架</p>
          <div class="ec-cats">
            <el-tag size="small" round>文化服务</el-tag>
            <el-tag size="small" round type="success">供给服务</el-tag>
            <el-tag size="small" round type="warning">调节服务</el-tag>
          </div>
          <span class="ec-action">浏览市场 →</span>
        </div>
        <div class="entry-card eco-card demand" @click="$router.push('/trading/demands')">
          <div class="ec-tag">资源需求市场</div>
          <h3>{{ stats.demandCount }}<small>条征集中需求</small></h3>
          <p>开发类、采购类、合作类 · 精准对接</p>
          <div class="ec-cats">
            <el-tag size="small" round>开发类</el-tag>
            <el-tag size="small" round type="success">采购类</el-tag>
            <el-tag size="small" round type="warning">合作类</el-tag>
          </div>
          <span class="ec-action">浏览需求 →</span>
        </div>
        <div class="entry-card eco-card sell" @click="$router.push('/trading/sell')">
          <div class="ec-icon"><el-icon :size="28"><Sell /></el-icon></div>
          <h3>我要出售</h3>
          <p>林农/合作社线上自主挂牌</p>
          <span class="ec-action">立即挂牌 →</span>
        </div>
        <div class="entry-card eco-card buy" @click="$router.push('/trading/buy')">
          <div class="ec-icon"><el-icon :size="28"><ShoppingCart /></el-icon></div>
          <h3>我要购买</h3>
          <p>企业自主发布采购需求</p>
          <span class="ec-action">立即发布 →</span>
        </div>
      </div>

      <!-- 热门挂牌 -->
      <div class="section-title" style="margin-top: 48px;">
        <h2>热门资源挂牌</h2>
        <span class="more" @click="$router.push('/trading/listings')">查看更多 →</span>
      </div>
      <div class="card-grid">
        <div v-for="l in listings" :key="l.id" class="listing-card eco-card" @click="$router.push('/trading/listing/' + l.id)">
          <img :src="l.cover" />
          <div class="lc-tag"><el-tag size="small" effect="dark" round>{{ l.category }}</el-tag></div>
          <div class="lc-body">
            <h4>{{ l.title }}</h4>
            <p>{{ l.intro }}</p>
            <div class="lc-meta">
              <span><el-icon><Location /></el-icon>{{ l.region }}</span>
              <span v-if="l.areaMu"><el-icon><Crop /></el-icon>{{ l.areaMu }} 亩</span>
            </div>
            <div class="lc-price">
              <strong>¥{{ formatPrice(l.price) }}</strong>
              <span>{{ l.priceUnit || '元' }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 特色农产品 -->
      <div class="section-title" style="margin-top: 48px;">
        <h2>地方特色农产品 <small>源头直供 · 产地直销</small></h2>
        <span class="more" @click="$router.push('/trading/farm')">查看全部 →</span>
      </div>
      <div class="farm-grid">
        <div v-for="f in farms" :key="f.id" class="farm-card eco-card" @click="$router.push('/trading/farm/' + f.id)">
          <img :src="f.cover" />
          <div class="fc-body">
            <h4>{{ f.name }}</h4>
            <p>{{ f.intro }}</p>
            <div class="fc-tags">
              <el-tag v-for="t in (f.tags || '').split(',')" :key="t" size="small" type="success" effect="plain" round>{{ t }}</el-tag>
            </div>
            <div class="fc-bottom">
              <div class="fc-price"><span>¥</span>{{ f.price }}<small>/{{ f.unit }}</small></div>
              <span class="fc-sales">已售 {{ f.sales }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const listings = ref([])
const farms = ref([])
const stats = reactive({ listingCount: 0, demandCount: 0 })

const formatPrice = (n) => {
  if (!n) return 0
  if (n >= 10000) return (n / 10000).toFixed(1) + '万'
  return Number(n).toLocaleString()
}

onMounted(async () => {
  const [l, d, f] = await Promise.all([
    request.get('/listing/list', { params: { page: 1, limit: 6 } }),
    request.get('/demand/list', { params: { page: 1, limit: 1 } }),
    request.get('/farm-product/list', { params: { page: 1, limit: 8 } }),
  ])
  listings.value = l.data?.list || []
  stats.listingCount = l.data?.totalCount || 0
  stats.demandCount = d.data?.totalCount || 0
  farms.value = f.data?.list || []
})
</script>
<style scoped>
.entry-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 18px; }
.entry-card { padding: 24px 22px; cursor: pointer; position: relative; min-height: 200px; display: flex; flex-direction: column; }
.ec-tag {
  display: inline-block;
  padding: 4px 10px;
  background: var(--primary-bg);
  color: var(--primary-darker);
  font-size: 12px;
  font-weight: 600;
  border-radius: 6px;
  margin-bottom: 12px;
  align-self: flex-start;
}
.entry-card.demand .ec-tag { background: #fffbeb; color: #b45309; }
.entry-card h3 {
  font-size: 30px; font-weight: 800; margin: 0 0 8px;
  color: var(--primary-darker);
  display: flex; align-items: baseline; gap: 6px;
}
.entry-card h3 small { font-size: 13px; color: var(--text-tertiary); font-weight: 500; }
.entry-card.demand h3 { color: #b45309; }
.entry-card p { font-size: 13px; color: var(--text-tertiary); margin: 0 0 12px; line-height: 1.6; }
.ec-cats { display: flex; gap: 6px; flex-wrap: wrap; margin-bottom: 14px; }
.ec-action { font-size: 13px; color: var(--primary-darker); font-weight: 600; margin-top: auto; }

.entry-card.sell, .entry-card.buy { background: linear-gradient(135deg, #10b981 0%, #34d399 100%); color: white; align-items: center; text-align: center; justify-content: center; }
.entry-card.buy { background: linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%); }
.entry-card.sell .ec-icon, .entry-card.buy .ec-icon {
  width: 56px; height: 56px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  display: flex; align-items: center; justify-content: center;
  margin-bottom: 14px;
}
.entry-card.sell h3, .entry-card.buy h3 { color: white; font-size: 22px; }
.entry-card.sell p, .entry-card.buy p { color: rgba(255,255,255,0.9); }
.entry-card.sell .ec-action, .entry-card.buy .ec-action { color: white; }

.card-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 22px; }
.listing-card { position: relative; cursor: pointer; overflow: hidden; }
.listing-card img { width: 100%; height: 200px; object-fit: cover; }
.lc-tag { position: absolute; top: 14px; left: 14px; }
.lc-body { padding: 18px; }
.lc-body h4 { font-size: 16px; font-weight: 700; margin: 0 0 8px; color: var(--text-primary); }
.lc-body p { font-size: 13px; color: var(--text-tertiary); margin: 0 0 10px; line-height: 1.6;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.lc-meta { display: flex; gap: 14px; font-size: 12px; color: var(--text-light); margin-bottom: 12px; }
.lc-meta span { display: inline-flex; align-items: center; gap: 4px; }
.lc-price { padding-top: 10px; border-top: 1px dashed var(--border-light); }
.lc-price strong { font-size: 22px; color: var(--accent-rose); font-weight: 800; }
.lc-price span { font-size: 12px; color: var(--text-tertiary); margin-left: 4px; }

.farm-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 18px; }
.farm-card { cursor: pointer; overflow: hidden; }
.farm-card img { width: 100%; height: 180px; object-fit: cover; }
.fc-body { padding: 14px; }
.fc-body h4 { font-size: 14px; font-weight: 700; margin: 0 0 4px; color: var(--text-primary); }
.fc-body p { font-size: 12px; color: var(--text-tertiary); margin: 0 0 8px; line-height: 1.5;
  display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden; }
.fc-tags { display: flex; gap: 4px; flex-wrap: wrap; margin-bottom: 8px; min-height: 22px; }
.fc-bottom { display: flex; justify-content: space-between; align-items: baseline; padding-top: 8px; border-top: 1px dashed var(--border-light); }
.fc-price { font-size: 20px; color: var(--accent-rose); font-weight: 800; }
.fc-price span { font-size: 13px; }
.fc-price small { font-size: 11px; color: var(--text-light); font-weight: 400; }
.fc-sales { font-size: 11px; color: var(--text-light); }

.section-title h2 small { font-size: 13px; color: var(--text-tertiary); font-weight: 400; margin-left: 6px; }

@media (max-width: 1100px) {
  .entry-grid { grid-template-columns: repeat(2, 1fr); }
  .card-grid { grid-template-columns: repeat(2, 1fr); }
  .farm-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>

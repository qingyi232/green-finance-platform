<template>
  <div v-if="data">
    <PageHeader :title="data.name" parent="金融服务" />
    <div class="page-container">
      <div class="detail-grid">
        <div class="left eco-card">
          <div class="head">
            <el-avatar :size="56" :src="data.bankLogo" shape="square" />
            <div class="head-body">
              <h2>{{ data.name }}</h2>
              <p>{{ data.bankName }} · <el-tag size="small" effect="dark" round>{{ data.category }}</el-tag></p>
            </div>
          </div>
          <p class="intro">{{ data.intro }}</p>

          <div class="key-stats">
            <div class="ks-item ks-amount">
              <small>贷款额度</small>
              <strong>¥{{ formatRange(data.loanMin, data.loanMax) }}</strong>
            </div>
            <div class="ks-item ks-rate">
              <small>年利率</small>
              <strong>{{ data.rateMin }}% - {{ data.rateMax }}%</strong>
            </div>
            <div class="ks-item ks-term">
              <small>贷款期限</small>
              <strong>{{ data.termMin }} - {{ data.termMax }} 月</strong>
            </div>
            <div class="ks-item ks-time">
              <small>受理时间</small>
              <strong>{{ data.acceptTime }}</strong>
            </div>
          </div>

          <div class="section" v-if="data.targetUsers">
            <h4>适用对象</h4>
            <div class="tags">
              <el-tag v-for="t in (data.targetUsers || '').split('、')" :key="t" round>{{ t }}</el-tag>
            </div>
          </div>

          <div class="section" v-if="data.applyCondition">
            <h4>申请条件</h4>
            <div class="content-box">{{ data.applyCondition }}</div>
          </div>

          <div class="section" v-if="data.applyMaterials">
            <h4>所需申请材料</h4>
            <div class="content-box">{{ data.applyMaterials }}</div>
          </div>

          <div class="section" v-if="data.applyProcess">
            <h4>办理流程</h4>
            <div class="content-box">{{ data.applyProcess }}</div>
          </div>
        </div>

        <div class="right">
          <div class="cta eco-card">
            <h3>立即申请此产品</h3>
            <p>线上提交申请，无需前往银行排队</p>
            <ul class="cta-list">
              <li><el-icon><CircleCheck /></el-icon>线上申请，全程电子化</li>
              <li><el-icon><CircleCheck /></el-icon>3 个工作日内反馈</li>
              <li><el-icon><CircleCheck /></el-icon>专人对接，无需操心</li>
            </ul>
            <el-button type="primary" round size="large" style="width:100%;" @click="$router.push('/finance/apply?product=' + data.id)">
              <el-icon><Promotion /></el-icon>我要申请
            </el-button>
            <el-button round size="large" style="width:100%; margin-top: 10px;" @click="$router.push('/finance/match')">
              <el-icon><Compass /></el-icon>查看其他产品
            </el-button>
          </div>
          <div class="hot-card eco-card">
            <h4>近期申请</h4>
            <div class="hot-stat">
              <div class="hs-item"><strong>{{ data.applyCount || 0 }}</strong><small>已申请</small></div>
              <div class="hs-item"><strong>{{ (data.applyCount > 0 ? (4.5 + (data.id % 5) * 0.1).toFixed(1) : '暂无') }}</strong><small>评分</small></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const route = useRoute()
const data = ref(null)

const formatRange = (a, b) => {
  if (!a && !b) return '-'
  const fmt = (n) => n >= 10000 ? (n / 10000).toLocaleString() + ' 万' : Number(n).toLocaleString()
  return `${fmt(a)} - ${fmt(b)}`
}

onMounted(async () => {
  const res = await request.get('/finance-product/detail/' + route.params.id)
  data.value = res.data
})
</script>
<style scoped>
.detail-grid { display: grid; grid-template-columns: 1.4fr 1fr; gap: 22px; }
.left { padding: 32px 36px; }
.head { display: flex; gap: 16px; align-items: flex-start; margin-bottom: 16px; }
.head h2 { font-size: 24px; font-weight: 800; margin: 0 0 6px; color: var(--text-primary); }
.head p { font-size: 13px; color: var(--text-tertiary); margin: 0; display: flex; align-items: center; gap: 6px; }
.intro { font-size: 14px; color: var(--text-secondary); line-height: 1.8; padding-bottom: 24px; border-bottom: 1px dashed var(--border-light); margin: 0 0 24px; }

.key-stats { display: grid; grid-template-columns: repeat(2, 1fr); gap: 14px; margin-bottom: 28px; }
.ks-item {
  padding: 18px 20px;
  border-radius: 12px;
  background: var(--primary-bg);
}
.ks-amount { background: linear-gradient(135deg, #ecfdf5, #d1fae5); }
.ks-rate { background: linear-gradient(135deg, #fffbeb, #fef3c7); }
.ks-term { background: linear-gradient(135deg, #eff6ff, #dbeafe); }
.ks-time { background: linear-gradient(135deg, #faf5ff, #ede9fe); }
.ks-item small { font-size: 12px; color: var(--text-tertiary); }
.ks-item strong { display: block; font-size: 18px; font-weight: 800; color: var(--text-primary); margin-top: 4px; }

.section { margin-bottom: 24px; }
.section h4 { font-size: 15px; font-weight: 700; margin: 0 0 12px; color: var(--text-primary); display: flex; align-items: center; gap: 8px; }
.section h4::before { content: ''; width: 3px; height: 14px; background: var(--primary); border-radius: 2px; }
.tags { display: flex; gap: 8px; flex-wrap: wrap; }
.content-box {
  font-size: 14px;
  line-height: 1.85;
  color: var(--text-secondary);
  padding: 16px 20px;
  background: var(--bg-page);
  border-radius: 10px;
  white-space: pre-wrap;
}

.right { display: flex; flex-direction: column; gap: 18px; position: sticky; top: 90px; height: fit-content; }
.cta {
  padding: 28px 30px;
  background: linear-gradient(135deg, #047857 0%, #10b981 100%);
  color: white;
}
.cta h3 { font-size: 20px; font-weight: 800; margin: 0 0 8px; }
.cta p { font-size: 13px; opacity: 0.9; margin: 0 0 18px; }
.cta-list { list-style: none; padding: 0; margin: 0 0 22px; }
.cta-list li { display: flex; align-items: center; gap: 8px; font-size: 14px; margin-bottom: 8px; opacity: 0.95; }
.cta-list li .el-icon { color: #d1fae5; }
.cta .el-button.is-round { background: white; color: var(--primary-darker); border-color: white; }
.cta .el-button.is-round:not(.el-button--primary) { background: rgba(255,255,255,0.18); color: white; border-color: rgba(255,255,255,0.4); }

.hot-card { padding: 22px 24px; }
.hot-card h4 { font-size: 14px; font-weight: 700; margin: 0 0 14px; color: var(--text-primary); }
.hot-stat { display: flex; gap: 24px; }
.hs-item { flex: 1; text-align: center; padding: 16px; background: var(--bg-page); border-radius: 10px; }
.hs-item strong { display: block; font-size: 22px; font-weight: 800; color: var(--primary-darker); }
.hs-item small { font-size: 12px; color: var(--text-tertiary); }

@media (max-width: 1100px) { .detail-grid { grid-template-columns: 1fr; } .right { position: static; } }
</style>

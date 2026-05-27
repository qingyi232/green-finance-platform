<template>
  <div>
    <PageHeader title="金融产品智能匹配" parent="金融服务" desc="输入您的融资需求，1 秒匹配最适合的绿色金融产品" />
    <div class="page-container">
      <div class="match-grid">
        <!-- 左：表单 -->
        <div class="match-form eco-card">
          <h3>填写融资需求</h3>
          <el-form :model="form" label-position="top">
            <el-form-item label="目标融资金额(元)">
              <el-input-number v-model="form.amount" :min="1000" :step="10000" style="width:100%;" controls-position="right" />
              <div class="quick-amount">
                <el-tag v-for="a in [50000, 200000, 500000, 1000000, 5000000]" :key="a" @click="form.amount = a" effect="plain" round style="cursor:pointer;">{{ formatAmount(a) }}</el-tag>
              </div>
            </el-form-item>
            <el-form-item label="期望融资期限(月)">
              <el-input-number v-model="form.term" :min="1" :max="120" style="width:100%;" controls-position="right" />
              <div class="quick-amount">
                <el-tag v-for="t in [6, 12, 24, 36, 60, 84]" :key="t" @click="form.term = t" effect="plain" round style="cursor:pointer;">{{ t }} 月</el-tag>
              </div>
            </el-form-item>
            <el-form-item label="您的角色">
              <el-select v-model="form.role" style="width:100%;">
                <el-option label="林农" value="林农" />
                <el-option label="合作社" value="合作社" />
                <el-option label="企业" value="企业" />
                <el-option label="碳排放企业" value="碳排放企业" />
                <el-option label="减排项目主体" value="减排项目主体" />
                <el-option label="乡村民宿/农家乐" value="乡村民宿" />
              </el-select>
            </el-form-item>
            <el-form-item label="融资用途">
              <el-input v-model="form.purpose" type="textarea" :rows="3" placeholder="如：林业扩大经营、生态项目开发、设备购置..." />
            </el-form-item>
            <el-button type="primary" size="large" :loading="loading" @click="match" round style="width:100%;">
              <el-icon><Compass /></el-icon>立即匹配
            </el-button>
          </el-form>
        </div>

        <!-- 右：结果 -->
        <div class="match-result">
          <div class="result-header">
            <h3>匹配结果</h3>
            <span v-if="matched">为您找到 <strong>{{ matched.length }}</strong> 款匹配产品</span>
          </div>
          <div v-if="!matched" class="empty-tip">
            <el-icon :size="48"><Compass /></el-icon>
            <p>请填写左侧融资需求并点击"立即匹配"</p>
          </div>
          <div v-else-if="matched.length === 0" class="empty-tip">
            <el-icon :size="48"><Warning /></el-icon>
            <p>暂无完全匹配的产品，请放宽条件再试</p>
          </div>
          <div v-else class="result-list">
            <div v-for="(p, i) in matched" :key="p.id" class="result-card eco-card">
              <div class="rc-rank" v-if="i < 3"><el-icon><Medal /></el-icon> 推荐 #{{ i + 1 }}</div>
              <div class="rc-head">
                <el-avatar :size="40" :src="p.bankLogo" shape="square" />
                <div>
                  <h4>{{ p.name }}</h4>
                  <p>{{ p.bankName }} · <el-tag size="small" effect="plain" round>{{ p.category }}</el-tag></p>
                </div>
              </div>
              <div class="rc-stats">
                <div><small>额度</small><strong>{{ formatRange(p.loanMin, p.loanMax) }}</strong></div>
                <div><small>利率</small><strong>{{ p.rateMin }}-{{ p.rateMax }}%</strong></div>
                <div><small>期限</small><strong>{{ p.termMin }}-{{ p.termMax }} 月</strong></div>
              </div>
              <div class="rc-reason">
                <el-icon><CircleCheck /></el-icon>
                匹配理由：金额 {{ formatAmount(form.amount) }} 在产品额度范围内 · 期限 {{ form.term }} 月符合产品要求
              </div>
              <div class="rc-actions">
                <el-button @click="$router.push('/finance/product/' + p.id)">查看详情</el-button>
                <el-button type="primary" @click="$router.push('/finance/apply?product=' + p.id)">立即申请</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const form = reactive({ amount: 200000, term: 24, role: '林农', purpose: '' })
const matched = ref(null)
const loading = ref(false)

const formatAmount = (n) => n >= 10000 ? (n / 10000) + ' 万' : n + ' 元'
const formatRange = (a, b) => {
  if (!a && !b) return '-'
  return `${formatAmount(a)} - ${formatAmount(b)}`
}

const match = async () => {
  if (!form.amount || !form.term) { ElMessage.warning('请填写完整'); return }
  loading.value = true
  try {
    const res = await request.post('/finance-product/match', form)
    matched.value = res.data || []
    if (matched.value.length === 0) ElMessage.info('暂无完全匹配的产品')
  } catch (e) {} finally {
    loading.value = false
  }
}
</script>
<style scoped>
.match-grid { display: grid; grid-template-columns: 380px 1fr; gap: 24px; }
.match-form { padding: 28px; height: fit-content; position: sticky; top: 90px; }
.match-form h3 { font-size: 16px; font-weight: 700; margin: 0 0 22px; color: var(--text-primary); }
.match-form h3::before { content: ''; display: inline-block; width: 3px; height: 14px; background: var(--primary); border-radius: 2px; margin-right: 8px; vertical-align: middle; }
.quick-amount { display: flex; gap: 6px; flex-wrap: wrap; margin-top: 8px; }

.match-result { display: flex; flex-direction: column; gap: 18px; }
.result-header {
  display: flex; align-items: baseline; justify-content: space-between;
  padding: 0 4px;
}
.result-header h3 { font-size: 18px; font-weight: 700; margin: 0; color: var(--text-primary); }
.result-header span { font-size: 13px; color: var(--text-tertiary); }
.result-header strong { color: var(--primary-darker); font-size: 16px; }

.empty-tip {
  background: white;
  border-radius: 16px;
  padding: 60px 0;
  text-align: center;
  color: var(--text-light);
  border: 1px dashed var(--border);
}
.empty-tip .el-icon { color: var(--primary-light); margin-bottom: 12px; }

.result-list { display: flex; flex-direction: column; gap: 16px; }
.result-card { padding: 22px 26px; position: relative; }
.rc-rank {
  position: absolute; top: 14px; right: 18px;
  font-size: 13px; font-weight: 700;
  color: var(--primary-darker);
  display: flex; align-items: center; gap: 4px;
}
.rc-head { display: flex; gap: 12px; align-items: flex-start; margin-bottom: 16px; }
.rc-head h4 { font-size: 16px; font-weight: 700; margin: 0 0 4px; color: var(--text-primary); }
.rc-head p { font-size: 12px; color: var(--text-tertiary); margin: 0; }
.rc-stats { display: flex; gap: 24px; padding: 12px 16px; background: var(--primary-bg); border-radius: 10px; margin-bottom: 14px; }
.rc-stats > div { display: flex; flex-direction: column; gap: 2px; }
.rc-stats small { font-size: 11px; color: var(--text-tertiary); }
.rc-stats strong { font-size: 14px; color: var(--primary-darker); }
.rc-reason {
  font-size: 12px; color: var(--text-tertiary);
  display: flex; align-items: center; gap: 4px;
  margin-bottom: 14px;
}
.rc-reason .el-icon { color: var(--primary); }
.rc-actions { display: flex; gap: 10px; justify-content: flex-end; }

@media (max-width: 1100px) { .match-grid { grid-template-columns: 1fr; } .match-form { position: static; } }
</style>

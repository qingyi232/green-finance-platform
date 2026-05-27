<template>
  <div>
    <PageHeader
      title="法律服务中心"
      desc="区域法律援助库 · 区域援助匹配服务 · 专属法律咨询 · 全程为生态资源流转交易保驾护航"
      :gradient="'linear-gradient(120deg, #faf5ff 0%, #ede9fe 70%, #ddd6fe 100%)'"
    />
    <div class="page-container">
      <!-- 三大入口 -->
      <div class="entry-grid">
        <div class="entry-card eco-card" @click="$router.push('/legal/orgs')">
          <div class="ec-icon" style="background: linear-gradient(135deg, #8b5cf6, #a78bfa);"><el-icon :size="26"><Briefcase /></el-icon></div>
          <h3>区域法律援助库</h3>
          <p>{{ stats.orgCount }} 家援助机构 · {{ stats.lawyerCount }} 位执业律师</p>
          <ul class="ec-list">
            <li><el-icon><Right /></el-icon>公益法律援助中心</li>
            <li><el-icon><Right /></el-icon>高校法律援助队</li>
            <li><el-icon><Right /></el-icon>专业律所</li>
          </ul>
          <span class="ec-action">查看援助库 →</span>
        </div>
        <div class="entry-card eco-card primary" @click="$router.push('/legal/consult')">
          <div class="ec-icon"><el-icon :size="26"><ChatLineRound /></el-icon></div>
          <h3>区域援助咨询匹配</h3>
          <p>从援助库中自主选择当地机构/律师，平台辅助对接</p>
          <ul class="ec-list">
            <li><el-icon><Right /></el-icon>合同审查</li>
            <li><el-icon><Right /></el-icon>权属纠纷调解</li>
            <li><el-icon><Right /></el-icon>侵权维权协助</li>
          </ul>
          <span class="ec-action">立即咨询 →</span>
        </div>
        <div class="entry-card eco-card" @click="$router.push('/legal/exclusive')">
          <div class="ec-icon" style="background: linear-gradient(135deg, #f59e0b, #fbbf24);"><el-icon :size="26"><Star /></el-icon></div>
          <h3>专属法律咨询</h3>
          <p>当地无法解决的复杂法律问题 · 平台专属团队介入</p>
          <ul class="ec-list">
            <li><el-icon><Right /></el-icon>跨区域纠纷</li>
            <li><el-icon><Right /></el-icon>复杂合同审查</li>
            <li><el-icon><Right /></el-icon>线下专属服务</li>
          </ul>
          <span class="ec-action">申请专属服务 →</span>
        </div>
      </div>

      <!-- 援助机构 -->
      <div class="section-title" style="margin-top: 48px;">
        <h2>入驻法律援助机构</h2>
        <span class="more" @click="$router.push('/legal/orgs')">查看全部 →</span>
      </div>
      <div class="org-grid">
        <div v-for="o in orgs" :key="o.id" class="org-card eco-card" @click="$router.push('/legal/orgs')">
          <el-avatar :size="56" :src="o.logo" shape="square" />
          <div class="oc-body">
            <div class="oc-head">
              <h4>{{ o.orgName }}</h4>
              <el-tag size="small" :type="o.orgType.includes('公益') ? 'success' : o.orgType.includes('高校') ? 'warning' : ''" effect="plain" round>{{ o.orgType }}</el-tag>
            </div>
            <p>{{ o.intro }}</p>
            <div class="oc-meta">
              <span><el-icon><Location /></el-icon>{{ o.serviceArea }}</span>
              <span><el-icon><Service /></el-icon>{{ o.lawyerCount }} 位律师</span>
              <span><el-icon><Star /></el-icon>评分 {{ o.rating }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 推荐律师 -->
      <div class="section-title" style="margin-top: 48px;">
        <h2>专业律师推荐</h2>
        <span class="more" @click="$router.push('/legal/lawyers')">查看全部 →</span>
      </div>
      <div class="lawyer-grid">
        <div v-for="l in lawyers" :key="l.id" class="lawyer-card eco-card">
          <el-avatar :size="64" :src="l.avatar" />
          <h4>{{ l.name }}</h4>
          <p class="ly-org">{{ l.orgName }}</p>
          <div class="ly-tags">
            <el-tag v-for="t in (l.expertise || '').split('、')" :key="t" size="small" effect="plain" round>{{ t }}</el-tag>
          </div>
          <div class="ly-stats">
            <div><strong>{{ l.years }}</strong><small>年执业</small></div>
            <div><strong>{{ l.caseCount }}</strong><small>案件</small></div>
            <div><strong>{{ l.rating }}</strong><small>评分</small></div>
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

const orgs = ref([])
const lawyers = ref([])
const stats = reactive({ orgCount: 0, lawyerCount: 0 })

onMounted(async () => {
  const [o, l] = await Promise.all([
    request.get('/legal-org/list', { params: { page: 1, limit: 4 } }),
    request.get('/lawyer/list', { params: { page: 1, limit: 5 } }),
  ])
  orgs.value = o.data?.list || []
  stats.orgCount = o.data?.totalCount || 0
  lawyers.value = l.data?.list || []
  stats.lawyerCount = l.data?.totalCount || 0
})
</script>
<style scoped>
.entry-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 22px; }
.entry-card { padding: 28px 26px; cursor: pointer; min-height: 280px; display: flex; flex-direction: column; }
.entry-card.primary { background: linear-gradient(135deg, #7c3aed 0%, #a78bfa 100%); color: white; }
.entry-card.primary .ec-icon { background: rgba(255,255,255,0.18); color: white; }
.ec-icon { width: 56px; height: 56px; border-radius: 16px; color: white; display: flex; align-items: center; justify-content: center; margin-bottom: 16px; }
.entry-card h3 { font-size: 20px; font-weight: 800; margin: 0 0 10px; }
.entry-card p { font-size: 13px; margin: 0 0 14px; opacity: 0.9; line-height: 1.6; }
.entry-card.primary p { color: rgba(255,255,255,0.92); }
.ec-list { list-style: none; padding: 0; margin: 0 0 18px; }
.ec-list li { display: flex; align-items: center; gap: 6px; font-size: 13px; margin-bottom: 6px; }
.ec-list li .el-icon { color: var(--primary); }
.entry-card.primary .ec-list li .el-icon { color: #d1fae5; }
.ec-action { font-size: 14px; font-weight: 600; color: var(--primary-darker); margin-top: auto; }
.entry-card.primary .ec-action { color: white; }

.org-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 18px; }
.org-card { display: flex; gap: 16px; padding: 22px; cursor: pointer; }
.oc-body { flex: 1; }
.oc-head { display: flex; align-items: center; gap: 8px; margin-bottom: 6px; }
.oc-head h4 { font-size: 15px; font-weight: 700; margin: 0; color: var(--text-primary); }
.org-card p { font-size: 13px; color: var(--text-tertiary); margin: 0 0 10px; line-height: 1.6;
  display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden; }
.oc-meta { display: flex; gap: 14px; font-size: 12px; color: var(--text-light); }
.oc-meta span { display: inline-flex; align-items: center; gap: 4px; }

.lawyer-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 16px; }
.lawyer-card { padding: 22px; text-align: center; }
.lawyer-card h4 { font-size: 15px; font-weight: 700; margin: 12px 0 4px; }
.ly-org { font-size: 12px; color: var(--text-tertiary); margin: 0 0 10px; }
.ly-tags { display: flex; gap: 4px; flex-wrap: wrap; justify-content: center; min-height: 22px; margin-bottom: 14px; }
.ly-stats { display: flex; justify-content: space-around; padding-top: 12px; border-top: 1px dashed var(--border-light); }
.ly-stats div { text-align: center; }
.ly-stats strong { display: block; font-size: 14px; color: var(--primary-darker); }
.ly-stats small { font-size: 10px; color: var(--text-light); }
</style>

<template>
  <div>
    <PageHeader title="我要评估" parent="资产评估" desc="选择评估机构与评估师，线上提交评估申请" />
    <div class="page-container">
      <div class="apply-grid">
        <div class="left eco-card">
          <h4>选择评估机构 / 评估师</h4>
          <el-tabs v-model="tab" class="ev-tabs">
            <el-tab-pane label="按机构" name="org">
              <div class="ev-list">
                <div v-for="o in orgs" :key="o.id" class="ev-item" :class="{ active: form.orgId === o.id && !form.evaluatorId }" @click="selectOrg(o)">
                  <h5>{{ o.orgName }}</h5>
                  <p>{{ o.expertise || '专业评估机构' }} · 评分 {{ o.rating }}</p>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="按评估师" name="evaluator">
              <div class="ev-list">
                <div v-for="ev in evaluators" :key="ev.id" class="ev-item" :class="{ active: form.evaluatorId === ev.id }" @click="selectEvaluator(ev)">
                  <h5>{{ ev.name }}<small> · {{ ev.years }} 年</small></h5>
                  <p>{{ ev.orgName }} · {{ ev.expertise }}</p>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <div class="right eco-card">
          <h4>填写评估申请</h4>
          <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
            <el-row :gutter="14">
              <el-col :span="12"><el-form-item label="资源类型" prop="resourceType"><el-select v-model="form.resourceType" style="width:100%;"><el-option v-for="t in types" :key="t" :label="t" :value="t" /></el-select></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="资源名称"><el-input v-model="form.resourceName" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="评估用途" prop="purpose">
                <el-radio-group v-model="form.purpose">
                  <el-radio v-for="p in purposes" :key="p" :value="p">{{ p }}</el-radio>
                </el-radio-group>
              </el-form-item></el-col>
              <el-col :span="24"><el-form-item label="申请人姓名/单位" prop="applicantName"><el-input v-model="form.applicantName" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="联系电话"><el-input v-model="form.applicantPhone" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="评估需求详情"><el-input v-model="form.requirement" type="textarea" :rows="4" placeholder="请详细描述评估需求、关注重点、紧急程度等..." /></el-form-item></el-col>
            </el-row>
            <div class="selected-tag" v-if="form.orgName || form.evaluatorName">
              已选择：<el-tag type="success" size="small" effect="dark">{{ form.evaluatorName ? form.evaluatorName + '（' + form.orgName + '）' : form.orgName }}</el-tag>
            </div>
            <div class="actions">
              <el-button @click="$router.push('/evaluation/my')">查看我的申请</el-button>
              <el-button type="primary" :loading="loading" size="large" @click="submit"><el-icon><Promotion /></el-icon>提交申请</el-button>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const tab = ref('org')
const orgs = ref([])
const evaluators = ref([])
const loading = ref(false)
const types = ['林地', '碳汇', '水库', '林下产品']
const purposes = ['银行抵押贷款', 'CCER 项目申报', '碳汇交易上市', '资产剥离', '征收补偿', '其他']

const form = reactive({
  resourceType: '林地', resourceName: '',
  purpose: '银行抵押贷款',
  applicantName: '', applicantPhone: '', requirement: '',
  orgId: null, orgName: '', evaluatorId: null, evaluatorName: '',
})

const rules = {
  resourceType: [{ required: true }],
  applicantName: [{ required: true, message: '请输入申请人' }],
  purpose: [{ required: true, message: '请选择用途' }],
}

const selectOrg = (o) => { form.orgId = o.id; form.orgName = o.orgName; form.evaluatorId = null; form.evaluatorName = '' }
const selectEvaluator = (ev) => { form.evaluatorId = ev.id; form.evaluatorName = ev.name; form.orgId = ev.orgId; form.orgName = ev.orgName }

const submit = async () => {
  if (!userStore.isLogin) { ElMessage.warning('请先登录'); router.push('/login'); return }
  await formRef.value.validate()
  if (!form.orgId) { ElMessage.warning('请选择评估机构或评估师'); return }
  if (!form.applicantPhone) form.applicantPhone = userStore.user?.phone
  loading.value = true
  try {
    await request.post('/evaluation-apply/save', form)
    ElMessage.success('评估申请已提交')
    setTimeout(() => router.push('/evaluation/my'), 1000)
  } catch (e) {} finally { loading.value = false }
}

onMounted(async () => {
  const [o, e] = await Promise.all([
    request.get('/evaluation-org/list', { params: { page: 1, limit: 30 } }),
    request.get('/evaluator/list', { params: { page: 1, limit: 30 } }),
  ])
  orgs.value = o.data?.list || []
  evaluators.value = e.data?.list || []
  if (route.query.evaluator) {
    const ev = evaluators.value.find((x) => x.id == route.query.evaluator)
    if (ev) { tab.value = 'evaluator'; selectEvaluator(ev) }
  }
})
</script>
<style scoped>
.apply-grid { display: grid; grid-template-columns: 320px 1fr; gap: 22px; }
.left, .right { padding: 22px; }
.left h4, .right h4 { font-size: 15px; font-weight: 700; margin: 0 0 16px; color: var(--text-primary); }
.left h4::before, .right h4::before { content: ''; display: inline-block; width: 3px; height: 14px; background: var(--primary); border-radius: 2px; margin-right: 8px; vertical-align: middle; }
.ev-list { display: flex; flex-direction: column; gap: 8px; max-height: 540px; overflow-y: auto; padding-right: 4px; }
.ev-item { padding: 12px 14px; border: 2px solid var(--border-light); border-radius: 10px; cursor: pointer; transition: all 0.2s; }
.ev-item:hover { border-color: var(--primary-light); }
.ev-item.active { border-color: var(--primary); background: var(--primary-bg); }
.ev-item h5 { font-size: 13px; font-weight: 600; margin: 0 0 4px; color: var(--text-primary); }
.ev-item h5 small { font-weight: 400; color: var(--text-tertiary); }
.ev-item p { font-size: 12px; color: var(--text-tertiary); margin: 0; line-height: 1.5; }
.selected-tag { padding: 12px 16px; background: var(--primary-bg); border-radius: 10px; margin: 8px 0 16px; font-size: 13px; }
.actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 12px; }
@media (max-width: 900px) { .apply-grid { grid-template-columns: 1fr; } }
</style>

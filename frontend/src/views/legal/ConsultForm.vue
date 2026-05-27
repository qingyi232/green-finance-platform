<template>
  <div>
    <PageHeader title="区域援助咨询匹配" parent="法律服务" desc="从援助库中自主选择当地法律援助机构或专业律师，平台辅助对接" />
    <div class="page-container">
      <div class="grid">
        <div class="left eco-card">
          <h4>选择援助主体</h4>
          <el-tabs v-model="tab">
            <el-tab-pane label="援助机构" name="org">
              <div class="lg-list">
                <div v-for="o in orgs" :key="o.id" class="lg-item" :class="{ active: form.targetOrgId === o.id && !form.targetLawyerId }" @click="selectOrg(o)">
                  <h5>{{ o.orgName }}</h5>
                  <p>{{ o.serviceArea }} · {{ o.expertise }}</p>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="律师" name="lawyer">
              <div class="lg-list">
                <div v-for="l in lawyers" :key="l.id" class="lg-item" :class="{ active: form.targetLawyerId === l.id }" @click="selectLawyer(l)">
                  <h5>{{ l.name }} <small>· {{ l.years }} 年</small></h5>
                  <p>{{ l.orgName }} · {{ l.expertise }}</p>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <div class="right eco-card">
          <h4>填写咨询信息</h4>
          <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
            <el-row :gutter="14">
              <el-col :span="12"><el-form-item label="法律类型" prop="category"><el-select v-model="form.category" style="width:100%;"><el-option v-for="c in cats" :key="c" :label="c" :value="c" /></el-select></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="紧急程度"><el-radio-group v-model="form.urgency"><el-radio value="紧急">紧急</el-radio><el-radio value="一般">一般</el-radio><el-radio value="不急">不急</el-radio></el-radio-group></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="联系人姓名" prop="applicantName"><el-input v-model="form.applicantName" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="联系电话" prop="applicantPhone"><el-input v-model="form.applicantPhone" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="所在区域"><el-input v-model="form.applicantRegion" placeholder="如：湖北省宜昌市五峰县" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="咨询详情" prop="description"><el-input v-model="form.description" type="textarea" :rows="5" placeholder="请详细描述您的法律问题..." /></el-form-item></el-col>
            </el-row>
            <div class="actions">
              <el-button @click="$router.push('/legal/my')">查看我的咨询</el-button>
              <el-button type="primary" :loading="loading" size="large" @click="submit"><el-icon><Promotion /></el-icon>提交咨询</el-button>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const tab = ref('org')
const orgs = ref([])
const lawyers = ref([])
const loading = ref(false)
const cats = ['合同审查', '权属纠纷', '侵权维权', '林权登记', '生态侵权', '土地承包', '其他']

const form = reactive({
  consultType: '区域援助',
  category: '合同审查', urgency: '一般',
  applicantName: '', applicantPhone: '', applicantRegion: '',
  description: '',
  targetOrgId: null, targetOrgName: '',
  targetLawyerId: null, targetLawyerName: '',
})

const rules = {
  category: [{ required: true }],
  applicantName: [{ required: true, message: '请输入姓名' }],
  applicantPhone: [{ required: true, message: '请输入电话' }],
  description: [{ required: true, message: '请输入咨询详情' }],
}

const selectOrg = (o) => { form.targetOrgId = o.id; form.targetOrgName = o.orgName; form.targetLawyerId = null; form.targetLawyerName = '' }
const selectLawyer = (l) => { form.targetLawyerId = l.id; form.targetLawyerName = l.name; form.targetOrgId = l.orgId; form.targetOrgName = l.orgName }

const submit = async () => {
  if (!userStore.isLogin) { ElMessage.warning('请先登录'); router.push('/login'); return }
  await formRef.value.validate()
  if (!form.targetOrgId) { ElMessage.warning('请选择援助机构或律师'); return }
  loading.value = true
  try {
    await request.post('/legal-consult/save', form)
    ElMessage.success('咨询已提交')
    setTimeout(() => router.push('/legal/my'), 1000)
  } catch (e) {} finally { loading.value = false }
}

onMounted(async () => {
  const [o, l] = await Promise.all([
    request.get('/legal-org/list', { params: { page: 1, limit: 30 } }),
    request.get('/lawyer/list', { params: { page: 1, limit: 30 } }),
  ])
  orgs.value = o.data?.list || []
  lawyers.value = l.data?.list || []
  if (userStore.user) {
    form.applicantName = userStore.user.realName || userStore.user.username
    form.applicantPhone = userStore.user.phone || ''
    form.applicantRegion = userStore.user.region || ''
  }
})
</script>
<style scoped>
.grid { display: grid; grid-template-columns: 320px 1fr; gap: 22px; }
.left, .right { padding: 22px; }
.left h4, .right h4 { font-size: 15px; font-weight: 700; margin: 0 0 16px; color: var(--text-primary); }
.left h4::before, .right h4::before { content: ''; display: inline-block; width: 3px; height: 14px; background: var(--primary); border-radius: 2px; margin-right: 8px; vertical-align: middle; }
.lg-list { display: flex; flex-direction: column; gap: 8px; max-height: 520px; overflow-y: auto; }
.lg-item { padding: 12px 14px; border: 2px solid var(--border-light); border-radius: 10px; cursor: pointer; transition: all 0.2s; }
.lg-item:hover { border-color: var(--primary-light); }
.lg-item.active { border-color: var(--primary); background: var(--primary-bg); }
.lg-item h5 { font-size: 13px; font-weight: 600; margin: 0 0 4px; }
.lg-item h5 small { color: var(--text-tertiary); font-weight: 400; }
.lg-item p { font-size: 11px; color: var(--text-tertiary); margin: 0; line-height: 1.5; }
.actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 12px; }
@media (max-width: 900px) { .grid { grid-template-columns: 1fr; } }
</style>

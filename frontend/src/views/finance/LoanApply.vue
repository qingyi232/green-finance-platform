<template>
  <div>
    <PageHeader title="贷款申请" parent="金融服务" desc="在线提交贷款申请，无需前往银行线下网点" />
    <div class="page-container">
      <div class="apply-grid">
        <!-- 左：选择产品 -->
        <div class="left eco-card">
          <h4>选择金融产品</h4>
          <div class="product-list">
            <div v-for="p in products" :key="p.id" class="p-item" :class="{ active: selectedId === p.id }" @click="selectedId = p.id">
              <el-avatar :size="36" :src="p.bankLogo" shape="square" />
              <div class="pi-body">
                <h5>{{ p.name }}</h5>
                <p>{{ p.bankName }} · {{ p.rateMin }}-{{ p.rateMax }}%</p>
              </div>
              <el-icon v-if="selectedId === p.id" class="check"><Select /></el-icon>
            </div>
          </div>
        </div>

        <!-- 右：表单 -->
        <div class="right eco-card">
          <h4>填写申请信息</h4>
          <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
            <el-row :gutter="14">
              <el-col :span="12"><el-form-item label="申请金额(元)" prop="loanAmount"><el-input-number v-model="form.loanAmount" :min="1000" :step="10000" style="width:100%;" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="期望期限(月)" prop="loanTerm"><el-input-number v-model="form.loanTerm" :min="1" :max="120" style="width:100%;" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="融资用途" prop="purpose"><el-input v-model="form.purpose" placeholder="如：扩大林木经营、设备购置、流动资金等" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="抵押物/担保"><el-input v-model="form.collateral" placeholder="如：林权抵押、保证人担保等" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="申请人基本信息"><el-input v-model="form.applicantInfo" type="textarea" :rows="3" placeholder="经营情况、收入来源、其他必要信息..." /></el-form-item></el-col>
            </el-row>
            <el-form-item label="贷款申请材料">
              <el-upload action="/api/file/upload" name="file" :headers="{ Token: token }" :on-success="onUpload" v-model:file-list="files" :limit="6">
                <el-button><el-icon><UploadFilled /></el-icon>选择文件</el-button>
                <template #tip>身份证/营业执照、收入证明、林权证、抵押物相关材料等</template>
              </el-upload>
            </el-form-item>
            <div class="actions">
              <el-button @click="$router.push('/finance/products')">返回产品列表</el-button>
              <el-button type="primary" :loading="loading" size="large" @click="submit">
                <el-icon><Promotion /></el-icon>提交申请
              </el-button>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const products = ref([])
const selectedId = ref(null)
const files = ref([])
const loading = ref(false)
const token = computed(() => userStore.token)

const form = reactive({ loanAmount: 100000, loanTerm: 24, purpose: '', collateral: '', applicantInfo: '', materials: '' })

const rules = {
  loanAmount: [{ required: true, message: '请输入申请金额' }],
  loanTerm: [{ required: true, message: '请输入期望期限' }],
  purpose: [{ required: true, message: '请输入融资用途' }],
}

const onUpload = (res) => {
  if (res.code === 0) {
    const arr = (form.materials || '').split(',').filter(Boolean)
    arr.push(res.url)
    form.materials = arr.join(',')
  }
}

const submit = async () => {
  if (!userStore.isLogin) { ElMessage.warning('请先登录'); router.push('/login'); return }
  if (!selectedId.value) { ElMessage.warning('请先选择金融产品'); return }
  await formRef.value.validate()
  const p = products.value.find((x) => x.id === selectedId.value)
  loading.value = true
  try {
    await request.post('/loan-apply/save', {
      ...form,
      productId: p.id,
      productName: p.name,
      bankId: p.bankId,
      bankName: p.bankName,
      applicantName: userStore.user?.realName || userStore.user?.username,
      applicantPhone: userStore.user?.phone,
    })
    ElMessage.success('贷款申请已提交')
    setTimeout(() => router.push('/finance/my'), 1000)
  } catch (e) {} finally { loading.value = false }
}

onMounted(async () => {
  const res = await request.get('/finance-product/list', { params: { page: 1, limit: 30 } })
  products.value = res.data?.list || []
  const pid = parseInt(route.query.product || '0')
  if (pid && products.value.find((p) => p.id === pid)) selectedId.value = pid
  else if (products.value.length) selectedId.value = products.value[0].id
})
</script>
<style scoped>
.apply-grid { display: grid; grid-template-columns: 320px 1fr; gap: 22px; }
.left, .right { padding: 22px; }
.left h4, .right h4 { font-size: 15px; font-weight: 700; margin: 0 0 16px; color: var(--text-primary); }
.left h4::before, .right h4::before { content: ''; display: inline-block; width: 3px; height: 14px; background: var(--primary); border-radius: 2px; margin-right: 8px; vertical-align: middle; }
.product-list { display: flex; flex-direction: column; gap: 8px; max-height: 600px; overflow-y: auto; }
.p-item { display: flex; align-items: center; gap: 10px; padding: 10px 12px; border: 2px solid var(--border-light); border-radius: 10px; cursor: pointer; transition: all 0.2s; }
.p-item:hover { border-color: var(--primary-light); }
.p-item.active { border-color: var(--primary); background: var(--primary-bg); }
.pi-body { flex: 1; min-width: 0; }
.pi-body h5 { font-size: 13px; font-weight: 600; margin: 0 0 2px; color: var(--text-primary); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.pi-body p { font-size: 11px; color: var(--text-tertiary); margin: 0; }
.check { color: var(--primary); }
.actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 12px; }

@media (max-width: 900px) { .apply-grid { grid-template-columns: 1fr; } }
</style>

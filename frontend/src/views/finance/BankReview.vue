<template>
  <div>
    <PageHeader title="贷款审批中心" parent="金融服务" desc="银行端线上审批 · 实时接收申请 · 简化审核流程" />
    <div class="page-container">
      <div class="filter-bar eco-card">
        <el-radio-group v-model="filterStatus" @change="loadData">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="审批中">待审批</el-radio-button>
          <el-radio-button label="已批准">已批准</el-radio-button>
          <el-radio-button label="已拒绝">已拒绝</el-radio-button>
        </el-radio-group>
      </div>
      <div class="eco-card" style="padding: 16px 20px;">
        <el-table :data="list" stripe>
          <el-table-column prop="applyNo" label="申请编号" width="160" />
          <el-table-column prop="productName" label="金融产品" min-width="180" />
          <el-table-column prop="applicantName" label="申请人" width="120" />
          <el-table-column prop="loanAmount" label="申请金额" width="130">
            <template #default="{ row }">¥{{ formatAmount(row.loanAmount) }}</template>
          </el-table-column>
          <el-table-column prop="loanTerm" label="期限(月)" width="100" />
          <el-table-column prop="step" label="步骤" width="110" />
          <el-table-column prop="status" label="状态" width="110">
            <template #default="{ row }"><el-tag size="small" :type="row.status === '已批准' ? 'success' : row.status === '已拒绝' ? 'danger' : 'warning'" effect="plain">{{ row.status }}</el-tag></template>
          </el-table-column>
          <el-table-column prop="createdTime" label="提交时间" width="170" />
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }"><el-button size="small" type="primary" link @click="open(row)">审批</el-button></template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog v-model="visible" title="贷款审批" width="640px">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="申请编号">{{ current?.applyNo }}</el-descriptions-item>
        <el-descriptions-item label="金融产品">{{ current?.productName }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ current?.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ current?.applicantRole }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ current?.applicantPhone }}</el-descriptions-item>
        <el-descriptions-item label="期限">{{ current?.loanTerm }} 月</el-descriptions-item>
        <el-descriptions-item :span="2" label="申请金额">¥{{ formatAmount(current?.loanAmount) }}</el-descriptions-item>
        <el-descriptions-item :span="2" label="融资用途">{{ current?.purpose }}</el-descriptions-item>
        <el-descriptions-item :span="2" label="抵押/担保">{{ current?.collateral }}</el-descriptions-item>
        <el-descriptions-item :span="2" label="申请人信息">{{ current?.applicantInfo }}</el-descriptions-item>
      </el-descriptions>
      <el-divider />
      <el-form label-width="100px">
        <el-form-item label="审批步骤"><el-select v-model="form.step"><el-option v-for="s in steps" :key="s" :label="s" :value="s" /></el-select></el-form-item>
        <el-form-item label="审批状态"><el-radio-group v-model="form.status"><el-radio value="审批中">审批中</el-radio><el-radio value="已批准">已批准</el-radio><el-radio value="已拒绝">已拒绝</el-radio></el-radio-group></el-form-item>
        <el-form-item label="批准额度" v-if="form.status === '已批准'"><el-input-number v-model="form.approvedAmount" style="width:100%;" /></el-form-item>
        <el-form-item label="批准利率" v-if="form.status === '已批准'"><el-input-number v-model="form.approvedRate" :precision="3" style="width:100%;" /></el-form-item>
        <el-form-item label="审批意见"><el-input v-model="form.reviewMsg" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">关闭</el-button>
        <el-button type="primary" @click="submit">提交审批</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const list = ref([])
const filterStatus = ref('')
const visible = ref(false)
const current = ref(null)
const form = reactive({})
const steps = ['初审中', '复审中', '审批中', '已批准', '已拒绝', '已放款']

const formatAmount = (n) => n >= 10000 ? (n / 10000) + ' 万' : (n || 0)

const loadData = async () => {
  const res = await request.get('/loan-apply/banklist', { params: { page: 1, limit: 30, status: filterStatus.value } })
  list.value = res.data?.list || []
}
const open = (row) => { current.value = row; Object.assign(form, { id: row.id, step: row.step || '初审中', status: row.status, approvedAmount: row.approvedAmount, approvedRate: row.approvedRate, reviewMsg: row.reviewMsg }); visible.value = true }
const submit = async () => {
  await request.post('/loan-apply/review', form)
  ElMessage.success('审批已提交')
  visible.value = false
  loadData()
}
onMounted(loadData)
</script>
<style scoped>
.filter-bar { padding: 16px 20px; margin-bottom: 16px; }
</style>

<template>
  <EasyCrud ref="crudRef" api="/loan-apply" page-api="page" :search-fields="search" :columns="columns" :show-add="false" :show-edit="false">
    <template #status="{ row }">
      <el-tag size="small" :type="row.status === '已批准' ? 'success' : row.status === '已拒绝' ? 'danger' : 'warning'" effect="plain">{{ row.status }}</el-tag>
    </template>
    <template #ops="{ row }">
      <el-button size="small" type="primary" link @click="open(row)">审批</el-button>
      <el-button size="small" type="danger" link @click="del(row)">删除</el-button>
    </template>
  </EasyCrud>
  <el-dialog v-model="visible" title="贷款审批" width="640px">
    <el-descriptions :column="2" border size="small">
      <el-descriptions-item label="编号">{{ current?.applyNo }}</el-descriptions-item>
      <el-descriptions-item label="产品">{{ current?.productName }}</el-descriptions-item>
      <el-descriptions-item label="申请人">{{ current?.applicantName }}</el-descriptions-item>
      <el-descriptions-item label="电话">{{ current?.applicantPhone }}</el-descriptions-item>
      <el-descriptions-item label="金额">¥{{ current?.loanAmount }}</el-descriptions-item>
      <el-descriptions-item label="期限">{{ current?.loanTerm }} 月</el-descriptions-item>
      <el-descriptions-item :span="2" label="融资用途">{{ current?.purpose }}</el-descriptions-item>
    </el-descriptions>
    <el-divider />
    <el-form label-width="100px">
      <el-form-item label="步骤"><el-select v-model="form.step"><el-option v-for="s in steps" :key="s" :label="s" :value="s" /></el-select></el-form-item>
      <el-form-item label="状态"><el-select v-model="form.status"><el-option v-for="s in statuses" :key="s" :label="s" :value="s" /></el-select></el-form-item>
      <el-form-item label="批准额度" v-if="form.status === '已批准'"><el-input-number v-model="form.approvedAmount" style="width:100%;" /></el-form-item>
      <el-form-item label="批准利率" v-if="form.status === '已批准'"><el-input-number v-model="form.approvedRate" :precision="3" style="width:100%;" /></el-form-item>
      <el-form-item label="审批意见"><el-input v-model="form.reviewMsg" type="textarea" :rows="3" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">关闭</el-button>
      <el-button type="primary" @click="submit">提交审批</el-button>
    </template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import EasyCrud from '../../components/EasyCrud.vue'
import request from '../../api/request'
const crudRef = ref()
const visible = ref(false)
const current = ref(null)
const form = reactive({})
const steps = ['已提交', '初审中', '复审中', '审批中', '已批准', '已拒绝', '已放款']
const statuses = ['审批中', '已批准', '已拒绝']
const search = [
  { prop: 'status', label: '状态', type: 'select', options: statuses },
]
const columns = [
  { prop: 'applyNo', label: '编号', width: 160 },
  { prop: 'productName', label: '产品', minWidth: 180 },
  { prop: 'applicantName', label: '申请人', width: 120 },
  { prop: 'loanAmount', label: '申请金额', width: 130 },
  { prop: 'loanTerm', label: '期限(月)', width: 100 },
  { prop: 'step', label: '步骤', width: 110 },
  { prop: 'status', label: '状态', width: 100, slot: 'status' },
]
const open = (row) => { current.value = row; Object.assign(form, { id: row.id, step: row.step, status: row.status, approvedAmount: row.approvedAmount, approvedRate: row.approvedRate, reviewMsg: row.reviewMsg }); visible.value = true }
const submit = async () => {
  await request.post('/loan-apply/review', form)
  ElMessage.success('已提交审批'); visible.value = false; crudRef.value.loadData()
}
const del = async (row) => {
  await ElMessageBox.confirm('确定删除？')
  await request.delete('/loan-apply/delete', { data: [row.id] })
  ElMessage.success('已删除'); crudRef.value.loadData()
}
</script>

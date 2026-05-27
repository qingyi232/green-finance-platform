<template>
  <EasyCrud ref="crudRef" api="/evaluation-apply" page-api="page" :search-fields="search" :columns="columns" :show-add="false" :show-edit="false">
    <template #status="{ row }">
      <el-tag size="small" :type="row.status === '已完成' ? 'success' : row.status === '已驳回' ? 'danger' : 'warning'" effect="plain">{{ row.status }}</el-tag>
    </template>
    <template #ops="{ row }">
      <el-button size="small" type="primary" link @click="open(row)"><el-icon><Edit /></el-icon>处理</el-button>
      <el-button size="small" type="danger" link @click="del(row)"><el-icon><Delete /></el-icon>删除</el-button>
    </template>
  </EasyCrud>
  <el-dialog v-model="visible" :title="current?.applyNo" width="600px">
    <el-descriptions :column="2" border size="small">
      <el-descriptions-item label="申请人">{{ current?.applicantName }}</el-descriptions-item>
      <el-descriptions-item label="电话">{{ current?.applicantPhone }}</el-descriptions-item>
      <el-descriptions-item label="评估机构">{{ current?.orgName }}</el-descriptions-item>
      <el-descriptions-item label="评估师">{{ current?.evaluatorName }}</el-descriptions-item>
      <el-descriptions-item label="资源">{{ current?.resourceName }}</el-descriptions-item>
      <el-descriptions-item label="用途">{{ current?.purpose }}</el-descriptions-item>
      <el-descriptions-item :span="2" label="需求">{{ current?.requirement }}</el-descriptions-item>
    </el-descriptions>
    <el-divider />
    <el-form label-width="100px">
      <el-form-item label="当前步骤"><el-select v-model="form.step"><el-option v-for="s in steps" :key="s" :label="s" :value="s" /></el-select></el-form-item>
      <el-form-item label="状态"><el-select v-model="form.status"><el-option v-for="s in statuses" :key="s" :label="s" :value="s" /></el-select></el-form-item>
      <el-form-item label="评估价值"><el-input-number v-model="form.resultValue" :precision="2" style="width:100%;" /></el-form-item>
      <el-form-item label="评估说明"><el-input v-model="form.resultSummary" type="textarea" :rows="3" /></el-form-item>
      <el-form-item label="机构反馈"><el-input v-model="form.feedback" type="textarea" :rows="2" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">关闭</el-button>
      <el-button type="primary" @click="submit">提交</el-button>
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
const steps = ['材料上报', '资质核实', '勘察复评', '结果公示', '已完成']
const statuses = ['待对接', '对接中', '评估中', '已完成', '已驳回']
const search = [
  { prop: 'status', label: '状态', type: 'select', options: statuses },
  { prop: 'step', label: '步骤', type: 'select', options: steps },
]
const columns = [
  { prop: 'applyNo', label: '编号', width: 160 },
  { prop: 'applicantName', label: '申请人', width: 120 },
  { prop: 'orgName', label: '评估机构', minWidth: 180 },
  { prop: 'resourceName', label: '资源', minWidth: 180, showOverflowTooltip: true },
  { prop: 'purpose', label: '用途', width: 130 },
  { prop: 'step', label: '步骤', width: 110 },
  { prop: 'status', label: '状态', width: 100, slot: 'status' },
]
const open = (row) => { current.value = row; Object.assign(form, { id: row.id, step: row.step, status: row.status, resultValue: row.resultValue, resultSummary: row.resultSummary, feedback: row.feedback }); visible.value = true }
const submit = async () => {
  await request.post('/evaluation-apply/handle', form)
  ElMessage.success('已更新')
  visible.value = false
  crudRef.value.loadData()
}
const del = async (row) => {
  await ElMessageBox.confirm('确定删除？')
  await request.delete('/evaluation-apply/delete', { data: [row.id] })
  ElMessage.success('已删除')
  crudRef.value.loadData()
}
</script>

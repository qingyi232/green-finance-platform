<template>
  <EasyCrud ref="crudRef" api="/legal-consult" page-api="page" :search-fields="search" :columns="columns" :show-add="false" :show-edit="false">
    <template #status="{ row }">
      <el-tag size="small" :type="row.status === '已结案' ? 'success' : row.status === '服务中' ? 'warning' : 'info'" effect="plain">{{ row.status }}</el-tag>
    </template>
    <template #urgency="{ row }">
      <el-tag size="small" :type="row.urgency === '紧急' ? 'danger' : row.urgency === '一般' ? 'warning' : 'info'" effect="plain">{{ row.urgency }}</el-tag>
    </template>
    <template #ops="{ row }">
      <el-button size="small" type="primary" link @click="open(row)">处理</el-button>
      <el-button size="small" type="danger" link @click="del(row)">删除</el-button>
    </template>
  </EasyCrud>
  <el-dialog v-model="visible" title="法律咨询处理" width="640px">
    <el-descriptions :column="2" border size="small">
      <el-descriptions-item label="编号">{{ current?.consultNo }}</el-descriptions-item>
      <el-descriptions-item label="类型">{{ current?.consultType }}</el-descriptions-item>
      <el-descriptions-item label="申请人">{{ current?.applicantName }}</el-descriptions-item>
      <el-descriptions-item label="电话">{{ current?.applicantPhone }}</el-descriptions-item>
      <el-descriptions-item label="对接机构">{{ current?.targetOrgName }}</el-descriptions-item>
      <el-descriptions-item label="对接律师">{{ current?.targetLawyerName }}</el-descriptions-item>
      <el-descriptions-item :span="2" label="咨询详情">{{ current?.description }}</el-descriptions-item>
    </el-descriptions>
    <el-divider />
    <el-form label-width="100px">
      <el-form-item label="状态"><el-select v-model="form.status"><el-option v-for="s in statuses" :key="s" :label="s" :value="s" /></el-select></el-form-item>
      <el-form-item label="服务记录"><el-input v-model="form.serviceRecord" type="textarea" :rows="3" /></el-form-item>
      <el-form-item label="反馈"><el-input v-model="form.feedback" type="textarea" :rows="2" /></el-form-item>
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
const statuses = ['待对接', '已对接', '服务中', '已结案']
const search = [
  { prop: 'consultType', label: '咨询类型', type: 'select', options: ['区域援助', '专属咨询'] },
  { prop: 'status', label: '状态', type: 'select', options: statuses },
]
const columns = [
  { prop: 'consultNo', label: '编号', width: 160 },
  { prop: 'consultType', label: '类型', width: 110 },
  { prop: 'applicantName', label: '申请人', width: 120 },
  { prop: 'urgency', label: '紧急', width: 90, slot: 'urgency' },
  { prop: 'category', label: '法律类型', width: 130 },
  { prop: 'description', label: '内容', minWidth: 240, showOverflowTooltip: true },
  { prop: 'status', label: '状态', width: 100, slot: 'status' },
]
const open = (row) => { current.value = row; Object.assign(form, { id: row.id, status: row.status, serviceRecord: row.serviceRecord, feedback: row.feedback }); visible.value = true }
const submit = async () => {
  await request.post('/legal-consult/handle', form)
  ElMessage.success('已更新'); visible.value = false; crudRef.value.loadData()
}
const del = async (row) => {
  await ElMessageBox.confirm('确定删除？')
  await request.delete('/legal-consult/delete', { data: [row.id] })
  ElMessage.success('已删除'); crudRef.value.loadData()
}
</script>

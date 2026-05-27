<template>
  <EasyCrud ref="crudRef" api="/resource" page-api="list" :search-fields="search" :columns="columns" :show-add="false">
    <template #cover="{ row }"><el-image :src="row.cover" style="width:60px;height:40px;border-radius:4px;" /></template>
    <template #status="{ row }">
      <el-tag size="small" :type="row.status === '已收储' ? 'success' : row.status === '已驳回' ? 'danger' : 'warning'" effect="dark">{{ row.status }}</el-tag>
    </template>
    <template #ops="{ row }">
      <el-button v-if="row.status === '待审核'" type="success" size="small" link @click="audit(row, '已收储')"><el-icon><Check /></el-icon>通过</el-button>
      <el-button v-if="row.status === '待审核'" type="danger" size="small" link @click="audit(row, '已驳回')"><el-icon><Close /></el-icon>驳回</el-button>
      <el-button type="danger" size="small" link @click="onDel(row)"><el-icon><Delete /></el-icon>删除</el-button>
    </template>
  </EasyCrud>
</template>
<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import EasyCrud from '../../components/EasyCrud.vue'
import request from '../../api/request'
const crudRef = ref()
const search = [
  { prop: 'keyword', label: '资源名称' },
  { prop: 'resourceType', label: '类型', type: 'select', options: ['林地', '碳汇', '水库', '林下产品'] },
  { prop: 'status', label: '状态', type: 'select', options: ['待审核', '已收储', '已驳回'] },
]
const columns = [
  { prop: 'cover', label: '封面', width: 80, slot: 'cover' },
  { prop: 'resourceNo', label: '编号', width: 130 },
  { prop: 'resourceName', label: '名称', minWidth: 200 },
  { prop: 'resourceType', label: '类型', width: 110 },
  { prop: 'areaMu', label: '面积(亩)', width: 100 },
  { prop: 'region', label: '区域', minWidth: 180 },
  { prop: 'ownerName', label: '权属人', width: 130 },
  { prop: 'status', label: '状态', width: 100, slot: 'status' },
]
const audit = async (row, status) => {
  const { value } = await ElMessageBox.prompt('请输入审核意见', '审核', { inputValue: status === '已收储' ? '资料齐全，审核通过' : '资料不全，请补充' })
  await request.post('/resource/audit', { id: row.id, status, auditMsg: value })
  ElMessage.success('已处理')
  crudRef.value.loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确定删除？')
  await request.delete('/resource/delete', { data: [row.id] })
  ElMessage.success('已删除')
  crudRef.value.loadData()
}
</script>

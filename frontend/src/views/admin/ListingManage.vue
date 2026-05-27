<template>
  <EasyCrud ref="crudRef" api="/listing" page-api="list" :search-fields="search" :columns="columns" :show-add="false" :show-edit="false">
    <template #cover="{ row }"><el-image :src="row.cover" style="width:60px;height:40px;border-radius:4px;" /></template>
    <template #auditStatus="{ row }">
      <el-tag size="small" :type="row.auditStatus === '已通过' ? 'success' : row.auditStatus === '已驳回' ? 'danger' : 'warning'" effect="plain">{{ row.auditStatus }}</el-tag>
    </template>
    <template #ops="{ row }">
      <el-button v-if="row.auditStatus === '待审核'" type="success" size="small" link @click="audit(row, '已通过')">通过</el-button>
      <el-button v-if="row.auditStatus === '待审核'" type="danger" size="small" link @click="audit(row, '已驳回')">驳回</el-button>
      <el-button type="danger" size="small" link @click="del(row)">删除</el-button>
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
  { prop: 'keyword', label: '搜索标题' },
  { prop: 'category', label: '类别', type: 'select', options: ['文化服务', '供给服务', '调节服务'] },
]
const columns = [
  { prop: 'cover', label: '封面', width: 80, slot: 'cover' },
  { prop: 'title', label: '挂牌标题', minWidth: 220, showOverflowTooltip: true },
  { prop: 'category', label: '类别', width: 100 },
  { prop: 'subCategory', label: '子类', width: 100 },
  { prop: 'region', label: '区域', minWidth: 160 },
  { prop: 'price', label: '价格', width: 120 },
  { prop: 'sellerName', label: '挂牌方', width: 140 },
  { prop: 'auditStatus', label: '审核', width: 100, slot: 'auditStatus' },
]
const audit = async (row, status) => {
  await request.post('/listing/audit', { id: row.id, auditStatus: status })
  ElMessage.success('已处理')
  crudRef.value.loadData()
}
const del = async (row) => {
  await ElMessageBox.confirm('确定删除？')
  await request.delete('/listing/delete', { data: [row.id] })
  ElMessage.success('已删除')
  crudRef.value.loadData()
}
</script>

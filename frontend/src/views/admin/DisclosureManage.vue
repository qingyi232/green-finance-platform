<template>
  <EasyCrud api="/disclosure" page-api="list" :search-fields="search" :columns="columns" :show-add="false">
    <template #warnStatus="{ row }">
      <el-tag size="small" :type="row.warnStatus === '已逾期' ? 'danger' : row.warnStatus === '即将到期' ? 'warning' : 'success'" effect="dark">{{ row.warnStatus }}</el-tag>
    </template>
    <template #form="{ form }">
      <el-form-item label="项目名称"><el-input v-model="form.projectName" /></el-form-item>
      <el-form-item label="项目周期"><el-input v-model="form.projectPeriod" /></el-form-item>
      <el-form-item label="封面"><el-input v-model="form.cover" /></el-form-item>
      <el-form-item label="项目进展"><el-input v-model="form.progress" type="textarea" :rows="2" /></el-form-item>
      <el-form-item label="生态保护措施"><el-input v-model="form.protection" type="textarea" :rows="2" /></el-form-item>
      <el-form-item label="运营管理"><el-input v-model="form.operation" type="textarea" :rows="2" /></el-form-item>
      <el-form-item label="生态修复"><el-input v-model="form.restoration" type="textarea" :rows="2" /></el-form-item>
      <el-form-item label="预警状态"><el-select v-model="form.warnStatus"><el-option label="正常" value="正常" /><el-option label="即将到期" value="即将到期" /><el-option label="已逾期" value="已逾期" /></el-select></el-form-item>
    </template>
  </EasyCrud>
</template>
<script setup>
import EasyCrud from '../../components/EasyCrud.vue'
const search = [
  { prop: 'enterpriseName', label: '企业名称' },
  { prop: 'warnStatus', label: '预警状态', type: 'select', options: ['正常', '即将到期', '已逾期'] },
]
const columns = [
  { prop: 'enterpriseName', label: '企业', minWidth: 220, showOverflowTooltip: true },
  { prop: 'projectName', label: '项目名称', minWidth: 220, showOverflowTooltip: true },
  { prop: 'projectPeriod', label: '周期', width: 140 },
  { prop: 'lastDisclosureTime', label: '上次披露', width: 170 },
  { prop: 'nextDueTime', label: '下次到期', width: 170 },
  { prop: 'warnStatus', label: '预警', width: 100, slot: 'warnStatus' },
]
</script>

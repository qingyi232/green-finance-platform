<template>
  <EasyCrud api="/evaluation-org" page-api="list" :search-fields="search" :columns="columns">
    <template #form="{ form }">
      <el-row :gutter="14">
        <el-col :span="12"><el-form-item label="机构名称" prop="orgName"><el-input v-model="form.orgName" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="LOGO"><el-input v-model="form.logo" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="营业执照号"><el-input v-model="form.businessLicense" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="官网"><el-input v-model="form.website" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="从业范围"><el-input v-model="form.businessScope" type="textarea" :rows="2" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="办公地址"><el-input v-model="form.officeAddress" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="机构简介"><el-input v-model="form.intro" type="textarea" :rows="3" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="评估师数"><el-input-number v-model="form.evaluatorCount" :min="0" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="案例数"><el-input-number v-model="form.caseCount" :min="0" /></el-form-item></el-col>
      </el-row>
    </template>
  </EasyCrud>
</template>
<script setup>
import EasyCrud from '../../components/EasyCrud.vue'
const search = [{ prop: 'keyword', label: '机构名称' }]
const columns = [
  { prop: 'orgName', label: '机构名称', minWidth: 220 },
  { prop: 'businessScope', label: '从业范围', minWidth: 240, showOverflowTooltip: true },
  { prop: 'phone', label: '电话', width: 140 },
  { prop: 'evaluatorCount', label: '评估师', width: 90 },
  { prop: 'caseCount', label: '案例', width: 80 },
  { prop: 'rating', label: '评分', width: 80 },
]
</script>

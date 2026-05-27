<template>
  <EasyCrud api="/legal-org" page-api="list" :search-fields="search" :columns="columns">
    <template #form="{ form }">
      <el-row :gutter="14">
        <el-col :span="14"><el-form-item label="机构名称"><el-input v-model="form.orgName" /></el-form-item></el-col>
        <el-col :span="10"><el-form-item label="类型"><el-select v-model="form.orgType"><el-option v-for="t in types" :key="t" :label="t" :value="t" /></el-select></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="LOGO"><el-input v-model="form.logo" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="地址"><el-input v-model="form.address" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="服务区域"><el-input v-model="form.serviceArea" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="专业领域"><el-input v-model="form.expertise" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="简介"><el-input v-model="form.intro" type="textarea" :rows="3" /></el-form-item></el-col>
      </el-row>
    </template>
  </EasyCrud>
</template>
<script setup>
import EasyCrud from '../../components/EasyCrud.vue'
const types = ['公益法律援助中心', '高校法律援助队', '专业律所']
const search = [
  { prop: 'keyword', label: '机构名称' },
  { prop: 'orgType', label: '类型', type: 'select', options: types },
]
const columns = [
  { prop: 'orgName', label: '机构名称', minWidth: 220 },
  { prop: 'orgType', label: '类型', width: 150 },
  { prop: 'serviceArea', label: '服务区域', minWidth: 160 },
  { prop: 'phone', label: '电话', width: 150 },
  { prop: 'lawyerCount', label: '律师数', width: 90 },
  { prop: 'caseCount', label: '案件数', width: 90 },
]
</script>

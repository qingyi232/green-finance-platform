<template>
  <EasyCrud api="/finance-product" page-api="list" :search-fields="search" :columns="columns" :initial-data="{ status: 1, applyCount: 0 }">
    <template #form="{ form }">
      <el-row :gutter="14">
        <el-col :span="12"><el-form-item label="产品名称"><el-input v-model="form.name" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="类别"><el-select v-model="form.category"><el-option v-for="c in cats" :key="c" :label="c" :value="c" /></el-select></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="所属银行"><el-input v-model="form.bankName" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="LOGO"><el-input v-model="form.bankLogo" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="额度下限"><el-input-number v-model="form.loanMin" :precision="0" style="width:100%;" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="额度上限"><el-input-number v-model="form.loanMax" :precision="0" style="width:100%;" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="利率下限(%)"><el-input-number v-model="form.rateMin" :precision="3" style="width:100%;" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="利率上限(%)"><el-input-number v-model="form.rateMax" :precision="3" style="width:100%;" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="期限下限(月)"><el-input-number v-model="form.termMin" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="期限上限(月)"><el-input-number v-model="form.termMax" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="适用对象"><el-input v-model="form.targetUsers" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="申请条件"><el-input v-model="form.applyCondition" type="textarea" :rows="2" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="所需材料"><el-input v-model="form.applyMaterials" type="textarea" :rows="2" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="办理流程"><el-input v-model="form.applyProcess" type="textarea" :rows="2" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="产品介绍"><el-input v-model="form.intro" type="textarea" :rows="2" /></el-form-item></el-col>
      </el-row>
    </template>
  </EasyCrud>
</template>
<script setup>
import EasyCrud from '../../components/EasyCrud.vue'
const cats = ['碳汇收储贷', '两山农林贷', '绿色工厂贷', '碳中和助力贷', '两山乡居贷']
const search = [
  { prop: 'keyword', label: '产品名称' },
  { prop: 'category', label: '类别', type: 'select', options: cats },
]
const columns = [
  { prop: 'name', label: '产品名称', minWidth: 200 },
  { prop: 'category', label: '类别', width: 130 },
  { prop: 'bankName', label: '银行', minWidth: 180 },
  { prop: 'loanMin', label: '额度下限', width: 110 },
  { prop: 'loanMax', label: '额度上限', width: 110 },
  { prop: 'rateMin', label: '利率', width: 90 },
  { prop: 'applyCount', label: '申请数', width: 90 },
]
</script>

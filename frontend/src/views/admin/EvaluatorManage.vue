<template>
  <EasyCrud api="/evaluator" page-api="list" :search-fields="search" :columns="columns">
    <template #avatar="{ row }"><el-avatar :size="36" :src="row.avatar" /></template>
    <template #form="{ form }">
      <el-row :gutter="14">
        <el-col :span="12"><el-form-item label="姓名" prop="name"><el-input v-model="form.name" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="性别"><el-select v-model="form.gender"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="所属机构"><el-input v-model="form.orgName" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="头像"><el-input v-model="form.avatar" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="执业证号"><el-input v-model="form.certNo" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="从业年限"><el-input-number v-model="form.years" :min="0" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="专业领域"><el-input v-model="form.expertise" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="资质"><el-input v-model="form.qualification" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="简介"><el-input v-model="form.intro" type="textarea" :rows="2" /></el-form-item></el-col>
      </el-row>
    </template>
  </EasyCrud>
</template>
<script setup>
import EasyCrud from '../../components/EasyCrud.vue'
const search = [{ prop: 'keyword', label: '姓名' }]
const columns = [
  { prop: 'avatar', label: '头像', width: 70, slot: 'avatar' },
  { prop: 'name', label: '姓名', width: 100 },
  { prop: 'gender', label: '性别', width: 70 },
  { prop: 'orgName', label: '所属机构', minWidth: 180 },
  { prop: 'expertise', label: '专业', minWidth: 220 },
  { prop: 'years', label: '年限', width: 80 },
  { prop: 'caseCount', label: '案例', width: 80 },
  { prop: 'rating', label: '评分', width: 80 },
]
</script>

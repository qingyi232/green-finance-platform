<template>
  <EasyCrud api="/farm-product" page-api="list" :search-fields="search" :columns="columns" :initial-data="{ status: 1, stock: 0, sales: 0 }">
    <template #cover="{ row }"><el-image :src="row.cover" style="width:60px;height:40px;border-radius:4px;" /></template>
    <template #form="{ form }">
      <el-row :gutter="14">
        <el-col :span="12"><el-form-item label="名称"><el-input v-model="form.name" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="类别"><el-select v-model="form.category"><el-option v-for="c in cats" :key="c" :label="c" :value="c" /></el-select></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="封面"><el-input v-model="form.cover" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="简介"><el-input v-model="form.intro" type="textarea" :rows="2" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="产地"><el-input v-model="form.origin" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="合作社"><el-input v-model="form.cooperative" /></el-form-item></el-col>
        <el-col :span="8"><el-form-item label="单位"><el-input v-model="form.unit" /></el-form-item></el-col>
        <el-col :span="8"><el-form-item label="单价"><el-input-number v-model="form.price" :precision="2" /></el-form-item></el-col>
        <el-col :span="8"><el-form-item label="库存"><el-input-number v-model="form.stock" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="标签"><el-input v-model="form.tags" placeholder="逗号分隔，如：源头直供,有机" /></el-form-item></el-col>
      </el-row>
    </template>
  </EasyCrud>
</template>
<script setup>
import EasyCrud from '../../components/EasyCrud.vue'
const cats = ['茶叶', '林下中药材', '山林果蔬', '山野特产', '食用菌']
const search = [
  { prop: 'keyword', label: '产品名称' },
  { prop: 'category', label: '类别', type: 'select', options: cats },
]
const columns = [
  { prop: 'cover', label: '封面', width: 80, slot: 'cover' },
  { prop: 'name', label: '名称', minWidth: 200 },
  { prop: 'category', label: '类别', width: 110 },
  { prop: 'origin', label: '产地', minWidth: 160 },
  { prop: 'price', label: '单价', width: 100 },
  { prop: 'unit', label: '单位', width: 70 },
  { prop: 'stock', label: '库存', width: 80 },
  { prop: 'sales', label: '销量', width: 80 },
]
</script>

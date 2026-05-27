<template>
  <EasyCrud api="/policy" :search-fields="search" :columns="columns">
    <template #form="{ form }">
      <el-form-item label="标题" prop="title"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="级别">
        <el-select v-model="form.level" style="width:100%;">
          <el-option v-for="l in levels" :key="l" :label="l" :value="l" />
        </el-select>
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="form.category" style="width:100%;">
          <el-option v-for="c in cats" :key="c" :label="c" :value="c" />
        </el-select>
      </el-form-item>
      <el-form-item label="发布机构"><el-input v-model="form.issueOrg" /></el-form-item>
      <el-form-item label="文号"><el-input v-model="form.issueNo" /></el-form-item>
      <el-form-item label="发布日期"><el-date-picker v-model="form.issueDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
      <el-form-item label="摘要"><el-input v-model="form.summary" type="textarea" :rows="2" /></el-form-item>
      <el-form-item label="政策正文"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
      <el-form-item label="政策解读"><el-input v-model="form.interpretation" type="textarea" :rows="4" /></el-form-item>
    </template>
  </EasyCrud>
</template>
<script setup>
import EasyCrud from '../../components/EasyCrud.vue'
const levels = ['国家级', '省级', '市级', '县级']
const cats = ['林业改革', '碳汇交易', '绿色金融', '生态保护']
const search = [
  { prop: 'keyword', label: '搜索标题' },
  { prop: 'level', label: '级别', type: 'select', options: levels },
  { prop: 'category', label: '分类', type: 'select', options: cats },
]
const columns = [
  { prop: 'title', label: '政策标题', minWidth: 240, showOverflowTooltip: true },
  { prop: 'level', label: '级别', width: 90 },
  { prop: 'category', label: '分类', width: 110 },
  { prop: 'issueOrg', label: '发布机构', minWidth: 180, showOverflowTooltip: true },
  { prop: 'issueDate', label: '发布日期', width: 130 },
  { prop: 'viewCount', label: '阅读', width: 80 },
]
</script>

<template>
  <EasyCrud api="/news" :search-fields="search" :columns="columns" :initial-data="{ viewCount: 0, isTop: 0 }">
    <template #cover="{ row }"><el-image :src="row.cover" :preview-src-list="[row.cover]" style="width:60px;height:40px;border-radius:4px;" /></template>
    <template #isTop="{ row }"><el-tag size="small" v-if="row.isTop" type="warning" effect="dark">置顶</el-tag></template>
    <template #form="{ form }">
      <el-form-item label="标题" prop="title"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="分类">
        <el-select v-model="form.category" style="width:100%;">
          <el-option v-for="c in cats" :key="c" :label="c" :value="c" />
        </el-select>
      </el-form-item>
      <el-form-item label="封面图"><el-input v-model="form.cover" placeholder="图片URL" /></el-form-item>
      <el-form-item label="摘要"><el-input v-model="form.summary" type="textarea" :rows="2" /></el-form-item>
      <el-form-item label="正文"><el-input v-model="form.content" type="textarea" :rows="6" placeholder="支持 Markdown" /></el-form-item>
      <el-form-item label="作者"><el-input v-model="form.author" /></el-form-item>
      <el-form-item label="是否置顶"><el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" /></el-form-item>
    </template>
  </EasyCrud>
</template>
<script setup>
import EasyCrud from '../../components/EasyCrud.vue'
const cats = ['行业动态', '平台公告', '案例分享', '技术资讯', '政策解读']
const search = [
  { prop: 'keyword', label: '搜索标题' },
  { prop: 'category', label: '分类', type: 'select', options: cats },
]
const columns = [
  { prop: 'cover', label: '封面', width: 80, slot: 'cover' },
  { prop: 'title', label: '标题', minWidth: 250, showOverflowTooltip: true },
  { prop: 'category', label: '分类', width: 110 },
  { prop: 'isTop', label: '', width: 60, slot: 'isTop' },
  { prop: 'viewCount', label: '阅读', width: 80 },
  { prop: 'author', label: '作者', width: 120 },
  { prop: 'publishedTime', label: '发布时间', width: 170 },
]
</script>

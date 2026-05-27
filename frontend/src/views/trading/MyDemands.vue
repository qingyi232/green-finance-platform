<template>
  <div>
    <PageHeader title="我的需求" parent="供需交易" />
    <div class="page-container">
      <div class="eco-card" style="padding: 16px;">
        <el-table :data="list" stripe>
          <el-table-column type="index" label="#" width="60" />
          <el-table-column v-for="c in columns" :key="c.prop" v-bind="c" />
        </el-table>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'
const list = ref([])
const columns = [
  { prop: 'title', label: '标题/名称', minWidth: 240 },
  { prop: 'status', label: '状态', width: 110 },
  { prop: 'createdTime', label: '提交时间', width: 170 },
]
onMounted(async () => {
  try { const res = await request.get('/demand/mylist'); list.value = Array.isArray(res.data) ? res.data : (res.data?.list || []) } catch (e) {}
})
</script>

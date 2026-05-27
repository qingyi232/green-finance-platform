<template>
  <EasyCrud ref="crudRef" api="/feedback" page-api="page" :show-add="false" :show-edit="false" :search-fields="search" :columns="columns">
    <template #status="{ row }">
      <el-tag size="small" :type="row.status === '已回复' ? 'success' : row.status === '处理中' ? 'warning' : 'info'" effect="plain">{{ row.status }}</el-tag>
    </template>
    <template #ops="{ row }">
      <el-button size="small" type="primary" link @click="onReply(row)" v-if="row.status !== '已回复'"><el-icon><ChatLineRound /></el-icon>回复</el-button>
      <el-button size="small" type="primary" link @click="onView(row)" v-else><el-icon><View /></el-icon>查看</el-button>
      <el-button size="small" type="danger" link @click="onDelete(row)"><el-icon><Delete /></el-icon>删除</el-button>
    </template>
  </EasyCrud>
  <el-dialog v-model="dialogVisible" :title="current?.type" width="600px">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="类型">{{ current?.type }}</el-descriptions-item>
      <el-descriptions-item label="联系人">{{ current?.contactName }} · {{ current?.contactPhone }}</el-descriptions-item>
      <el-descriptions-item label="内容">{{ current?.content }}</el-descriptions-item>
      <el-descriptions-item label="提交时间">{{ current?.createdTime }}</el-descriptions-item>
    </el-descriptions>
    <el-divider />
    <el-form-item label="回复内容">
      <el-input v-model="reply" type="textarea" :rows="4" :readonly="current?.status === '已回复'" />
    </el-form-item>
    <template #footer>
      <el-button @click="dialogVisible = false">关闭</el-button>
      <el-button v-if="current?.status !== '已回复'" type="primary" :loading="loading" @click="submitReply">回复</el-button>
    </template>
  </el-dialog>
</template>
<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import EasyCrud from '../../components/EasyCrud.vue'
import request from '../../api/request'

const crudRef = ref()
const dialogVisible = ref(false)
const current = ref(null)
const reply = ref('')
const loading = ref(false)

const search = [
  { prop: 'status', label: '状态', type: 'select', options: ['待处理', '处理中', '已回复', '已关闭'] },
  { prop: 'type', label: '类型', type: 'select', options: ['操作咨询', '意见建议', '投诉举报', '业务求助'] },
]
const columns = [
  { prop: 'type', label: '类型', width: 110 },
  { prop: 'content', label: '内容', minWidth: 280, showOverflowTooltip: true },
  { prop: 'contactName', label: '联系人', width: 100 },
  { prop: 'contactPhone', label: '电话', width: 130 },
  { prop: 'status', label: '状态', width: 100, slot: 'status' },
  { prop: 'createdTime', label: '提交时间', width: 170 },
]

const onReply = (row) => { current.value = row; reply.value = row.replyContent || ''; dialogVisible.value = true }
const onView = (row) => { current.value = row; reply.value = row.replyContent || ''; dialogVisible.value = true }
const onDelete = async (row) => {
  await ElMessageBox.confirm('确定删除此反馈？')
  await request.delete('/feedback/delete', { data: [row.id] })
  ElMessage.success('已删除')
  crudRef.value.loadData()
}
const submitReply = async () => {
  if (!reply.value) { ElMessage.warning('请输入回复内容'); return }
  loading.value = true
  try {
    await request.post('/feedback/reply', { id: current.value.id, replyContent: reply.value })
    ElMessage.success('回复成功')
    dialogVisible.value = false
    crudRef.value.loadData()
  } catch (e) {} finally {
    loading.value = false
  }
}
</script>

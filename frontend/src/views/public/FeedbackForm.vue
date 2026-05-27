<template>
  <div>
    <PageHeader title="问题反馈" parent="首页" desc="您的每一条建议都将被认真处理 · 平台 1 个工作日内响应" />
    <div class="page-container">
      <div class="fb-card eco-card">
        <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
          <el-form-item label="反馈类型" prop="type">
            <div class="type-grid">
              <div v-for="t in types" :key="t.value" class="type-item" :class="{ active: form.type === t.value }" @click="form.type = t.value">
                <div class="ti-icon" :style="{ background: t.bg, color: t.color }"><el-icon :size="22"><component :is="t.icon" /></el-icon></div>
                <span>{{ t.label }}</span>
              </div>
            </div>
          </el-form-item>
          <el-form-item label="反馈内容" prop="content">
            <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请详细描述您遇到的问题或建议..." />
          </el-form-item>
          <el-form-item label="佐证材料(可选)">
            <el-upload action="/api/file/upload" name="file" :on-success="onUpload" v-model:file-list="files" :limit="3">
              <el-button><el-icon><UploadFilled /></el-icon>上传图片/文件</el-button>
            </el-upload>
          </el-form-item>
          <el-row :gutter="14">
            <el-col :span="8"><el-form-item label="联系人" prop="contactName"><el-input v-model="form.contactName" /></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="联系电话" prop="contactPhone"><el-input v-model="form.contactPhone" /></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="邮箱"><el-input v-model="form.contactEmail" /></el-form-item></el-col>
          </el-row>
          <div class="actions">
            <el-button :loading="loading" type="primary" size="large" round @click="submit"><el-icon><Promotion /></el-icon>提交反馈</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'
const formRef = ref()
const files = ref([])
const loading = ref(false)
const form = reactive({ type: '', content: '', contactName: '', contactPhone: '', contactEmail: '', attachmentUrl: '' })
const rules = {
  type: [{ required: true, message: '请选择类型' }],
  content: [{ required: true, message: '请输入反馈内容' }],
  contactName: [{ required: true, message: '请输入联系人' }],
  contactPhone: [{ required: true, message: '请输入电话' }],
}
const types = [
  { value: '操作咨询', label: '操作咨询', icon: 'QuestionFilled', color: '#0ea5e9', bg: '#f0f9ff' },
  { value: '意见建议', label: '意见建议', icon: 'EditPen', color: '#10b981', bg: '#ecfdf5' },
  { value: '投诉举报', label: '投诉举报', icon: 'Warning', color: '#f43f5e', bg: '#fff1f2' },
  { value: '业务求助', label: '业务求助', icon: 'Headset', color: '#f59e0b', bg: '#fffbeb' },
]
const onUpload = (res) => {
  if (res.code === 0) {
    const arr = (form.attachmentUrl || '').split(',').filter(Boolean)
    arr.push(res.url)
    form.attachmentUrl = arr.join(',')
  }
}
const submit = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await request.post('/feedback/save', form)
    ElMessage.success('反馈已提交，我们将尽快处理')
    Object.keys(form).forEach((k) => form[k] = k === 'type' ? '' : '')
    files.value = []
  } catch (e) {} finally { loading.value = false }
}
onMounted(() => {
  const t = sessionStorage.getItem('feedback-type')
  if (t) { form.type = t; sessionStorage.removeItem('feedback-type') }
})
</script>
<style scoped>
.fb-card { padding: 36px 40px; }
.type-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; width: 100%; }
.type-item { display: flex; align-items: center; gap: 12px; padding: 16px; border: 2px solid var(--border-light); border-radius: 12px; cursor: pointer; transition: all 0.2s; }
.type-item:hover { border-color: var(--primary-light); }
.type-item.active { border-color: var(--primary); background: var(--primary-bg); }
.ti-icon { width: 38px; height: 38px; border-radius: 10px; display: flex; align-items: center; justify-content: center; }
.type-item span { font-weight: 600; }
.actions { text-align: center; margin-top: 12px; }
@media (max-width: 700px) { .type-grid { grid-template-columns: repeat(2, 1fr); } }
</style>

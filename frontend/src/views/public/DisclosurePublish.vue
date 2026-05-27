<template>
  <div>
    <PageHeader title="信息披露发布" parent="信息披露" desc="生态开发企业每年须披露项目概况，确保运营透明、可追溯" />
    <div class="page-container">
      <div class="eco-card" style="padding: 32px 40px;">
        <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
          <el-row :gutter="14">
            <el-col :span="14"><el-form-item label="项目名称" prop="projectName"><el-input v-model="form.projectName" /></el-form-item></el-col>
            <el-col :span="10"><el-form-item label="项目周期"><el-input v-model="form.projectPeriod" placeholder="如：2024-2034" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item label="项目封面"><el-input v-model="form.cover" placeholder="图片 URL" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item label="项目进展" prop="progress"><el-input v-model="form.progress" type="textarea" :rows="3" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="生态保护措施"><el-input v-model="form.protection" type="textarea" :rows="3" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="运营管理"><el-input v-model="form.operation" type="textarea" :rows="3" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item label="生态修复落实"><el-input v-model="form.restoration" type="textarea" :rows="3" /></el-form-item></el-col>
          </el-row>
          <div style="text-align: right; margin-top: 16px;">
            <el-button @click="$router.push('/disclosure')">返回列表</el-button>
            <el-button type="primary" :loading="loading" size="large" @click="submit"><el-icon><Promotion /></el-icon>提交披露</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'
const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)
const form = reactive({
  enterpriseName: userStore.user?.realName || '',
  projectName: '', projectPeriod: '', cover: '',
  progress: '', protection: '', operation: '', restoration: '',
})
const rules = {
  projectName: [{ required: true, message: '请输入项目名称' }],
  progress: [{ required: true, message: '请输入项目进展' }],
}
const submit = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await request.post('/disclosure/save', form)
    ElMessage.success('披露信息已提交')
    setTimeout(() => router.push('/disclosure'), 1000)
  } catch (e) {} finally { loading.value = false }
}
</script>

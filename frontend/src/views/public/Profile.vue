<template>
  <div>
    <PageHeader title="个人中心" parent="首页" />
    <div class="page-container">
      <div class="profile-grid">
        <div class="left eco-card">
          <el-avatar :size="100" :src="userStore.user?.avatar" style="margin: 0 auto; display: block;" />
          <h3>{{ userStore.user?.realName || userStore.user?.username }}</h3>
          <p class="role"><el-tag effect="dark" round>{{ userStore.role }}</el-tag></p>
          <p class="info"><el-icon><Phone /></el-icon> {{ userStore.user?.phone || '未填写' }}</p>
          <p class="info"><el-icon><Message /></el-icon> {{ userStore.user?.email || '未填写' }}</p>
          <p class="info"><el-icon><Location /></el-icon> {{ userStore.user?.region || '未填写' }}</p>
        </div>
        <div class="right eco-card">
          <h4>修改个人信息</h4>
          <el-form :model="form" label-width="110px">
            <el-row :gutter="14">
              <el-col :span="12"><el-form-item label="账号"><el-input v-model="form.username" disabled /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="姓名/机构名"><el-input v-model="form.realName" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="手机"><el-input v-model="form.phone" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="所在区域"><el-input v-model="form.region" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="详细地址"><el-input v-model="form.address" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="头像"><el-input v-model="form.avatar" placeholder="头像 URL" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="个人简介"><el-input v-model="form.intro" type="textarea" :rows="3" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="新密码"><el-input v-model="form.password" type="password" show-password placeholder="留空则不修改" /></el-form-item></el-col>
            </el-row>
            <div style="text-align: right;"><el-button type="primary" :loading="loading" @click="submit">保存修改</el-button></div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'
const userStore = useUserStore()
const loading = ref(false)
const form = reactive({})
onMounted(async () => {
  const res = await request.get('/auth/session')
  Object.assign(form, res.data || {})
})
const submit = async () => {
  loading.value = true
  try {
    await request.put('/auth/profile', form)
    userStore.updateUser(form)
    ElMessage.success('保存成功')
  } catch (e) {} finally { loading.value = false }
}
</script>
<style scoped>
.profile-grid { display: grid; grid-template-columns: 320px 1fr; gap: 22px; }
.left { padding: 28px; text-align: center; }
.left h3 { font-size: 20px; font-weight: 800; margin: 16px 0 6px; color: var(--text-primary); }
.role { margin: 0 0 16px; }
.info { font-size: 13px; color: var(--text-tertiary); margin: 6px 0; display: flex; align-items: center; gap: 6px; justify-content: center; }
.right { padding: 28px 32px; }
.right h4 { font-size: 16px; font-weight: 700; margin: 0 0 22px; }
@media (max-width: 900px) { .profile-grid { grid-template-columns: 1fr; } }
</style>

<template>
  <div>
    <PageHeader title="我要购买（发布需求）" parent="供需交易" />
    <div class="page-container">
      <div class="eco-card" style="padding: 32px 40px;">
        <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
          <el-row :gutter="14">
            <el-col :span="16"><el-form-item label="需求标题" prop="title"><el-input v-model="form.title" /></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="类别" prop="category"><el-select v-model="form.category" style="width:100%;"><el-option label="开发类" value="开发类" /><el-option label="采购类" value="采购类" /><el-option label="合作类" value="合作类" /></el-select></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="子类"><el-select v-model="form.subCategory" style="width:100%;"><el-option v-for="c in subCats" :key="c" :label="c" :value="c" /></el-select></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="目标区域"><el-input v-model="form.targetRegion" /></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="需求面积"><el-input-number v-model="form.areaDemand" :precision="2" style="width:100%;" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item label="封面图"><el-input v-model="form.cover" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item label="需求详情" prop="intro"><el-input v-model="form.intro" type="textarea" :rows="4" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="预算下限"><el-input-number v-model="form.budgetMin" :precision="2" style="width:100%;" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="预算上限"><el-input-number v-model="form.budgetMax" :precision="2" style="width:100%;" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="征集截止"><el-date-picker v-model="form.expireTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%;" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item><el-checkbox v-model="form.acceptAppointment" :true-value="1" :false-value="0">接受供给方在线预约洽谈</el-checkbox></el-form-item></el-col>
          </el-row>
          <div style="text-align: right;">
            <el-button @click="$router.push('/trading/demands')">返回需求市场</el-button>
            <el-button type="primary" :loading="loading" size="large" @click="submit"><el-icon><Promotion /></el-icon>提交需求</el-button>
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
const subCats = ['林地开发', '碳汇采购', '林木收购', '林下产品采购', '生态项目合作']
const form = reactive({
  title: '', category: '采购类', subCategory: '', targetRegion: '', areaDemand: null,
  cover: '', intro: '', budgetMin: null, budgetMax: null, expireTime: '', acceptAppointment: 1,
  buyerName: userStore.user?.realName, buyerPhone: userStore.user?.phone,
})
const rules = {
  title: [{ required: true, message: '请输入需求标题' }],
  category: [{ required: true }],
  intro: [{ required: true, message: '请输入需求详情' }],
}
const submit = async () => {
  if (!userStore.isLogin) { ElMessage.warning('请先登录'); router.push('/login'); return }
  await formRef.value.validate()
  loading.value = true
  try {
    await request.post('/demand/save', form)
    ElMessage.success('需求已发布，等待审核')
    setTimeout(() => router.push('/trading/my-demands'), 1000)
  } catch (e) {} finally { loading.value = false }
}
</script>

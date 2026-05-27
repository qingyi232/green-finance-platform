<template>
  <div>
    <PageHeader title="我要出售（资源挂牌）" parent="供需交易" />
    <div class="page-container">
      <div class="eco-card" style="padding: 32px 40px;">
        <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
          <el-row :gutter="14">
            <el-col :span="16"><el-form-item label="挂牌标题" prop="title"><el-input v-model="form.title" placeholder="如：五峰汉马池村毛竹林一区(80.5亩) - 经营权流转" /></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="一级分类" prop="category"><el-select v-model="form.category" style="width:100%;"><el-option label="文化服务" value="文化服务" /><el-option label="供给服务" value="供给服务" /><el-option label="调节服务" value="调节服务" /></el-select></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="子分类"><el-select v-model="form.subCategory" style="width:100%;"><el-option v-for="c in subCats" :key="c" :label="c" :value="c" /></el-select></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="所在区域"><el-input v-model="form.region" /></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="面积(亩)"><el-input-number v-model="form.areaMu" :precision="2" style="width:100%;" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item label="封面图"><el-input v-model="form.cover" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item label="资源简介" prop="intro"><el-input v-model="form.intro" type="textarea" :rows="3" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item label="权属说明"><el-input v-model="form.ownership" type="textarea" :rows="2" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="挂牌价格(元)"><el-input-number v-model="form.price" :precision="2" style="width:100%;" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="挂牌到期"><el-date-picker v-model="form.expireTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%;" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item><el-checkbox v-model="form.acceptAppointment" :true-value="1" :false-value="0">接受需求方在线预约洽谈</el-checkbox></el-form-item></el-col>
          </el-row>
          <div style="text-align: right;">
            <el-button @click="$router.push('/trading/listings')">返回市场</el-button>
            <el-button type="primary" :loading="loading" size="large" @click="submit"><el-icon><Promotion /></el-icon>提交挂牌</el-button>
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
const subCats = ['林地', '林下产品', '碳汇', '水库', '主题乐园', '医疗养老']
const form = reactive({
  title: '', category: '供给服务', subCategory: '', region: '', areaMu: null,
  cover: '', intro: '', ownership: '', price: null, expireTime: '', acceptAppointment: 1,
  sellerName: userStore.user?.realName, sellerPhone: userStore.user?.phone,
})
const rules = {
  title: [{ required: true, message: '请输入挂牌标题' }],
  category: [{ required: true }],
  intro: [{ required: true, message: '请输入资源简介' }],
}
const submit = async () => {
  if (!userStore.isLogin) { ElMessage.warning('请先登录'); router.push('/login'); return }
  await formRef.value.validate()
  loading.value = true
  try {
    await request.post('/listing/save', form)
    ElMessage.success('挂牌已提交，等待审核')
    setTimeout(() => router.push('/trading/my-listings'), 1000)
  } catch (e) {} finally { loading.value = false }
}
</script>

<template>
  <div>
    <PageHeader title="专属法律咨询" parent="法律服务" desc="平台专属法律服务团队介入复杂法律问题（收费服务）" />
    <div class="page-container">
      <div class="exclusive-card eco-card">
        <div class="alert">
          <el-icon><Star /></el-icon>
          <div>
            <h4>专属法律咨询服务</h4>
            <p>本服务为收费板块，针对当地法律援助机构、律师无法解决的复杂法律问题，平台将派出专属团队前往当地深入了解情况、协助解决纠纷、完善文书、提供维权指导。</p>
          </div>
        </div>
        <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
          <el-row :gutter="14">
            <el-col :span="12"><el-form-item label="法律类型" prop="category"><el-select v-model="form.category" style="width:100%;"><el-option v-for="c in cats" :key="c" :label="c" :value="c" /></el-select></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="紧急程度"><el-radio-group v-model="form.urgency"><el-radio value="紧急">紧急</el-radio><el-radio value="一般">一般</el-radio></el-radio-group></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="联系人姓名" prop="applicantName"><el-input v-model="form.applicantName" /></el-form-item></el-col>
            <el-col :span="12"><el-form-item label="联系电话" prop="applicantPhone"><el-input v-model="form.applicantPhone" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item label="所在区域"><el-input v-model="form.applicantRegion" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item label="详细咨询诉求" prop="description"><el-input v-model="form.description" type="textarea" :rows="4" /></el-form-item></el-col>
            <el-col :span="24"><el-form-item label="此前对接情况"><el-input v-model="form.prevHandling" type="textarea" :rows="3" placeholder="请说明已对接过的当地机构/律师、未解决的核心问题" /></el-form-item></el-col>
          </el-row>
          <div class="actions">
            <el-button @click="$router.push('/legal/my')">查看我的咨询</el-button>
            <el-button type="warning" :loading="loading" size="large" @click="submit"><el-icon><Star /></el-icon>提交专属服务申请</el-button>
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
const cats = ['跨区域纠纷', '复杂合同审查', '生态项目合作', '碳汇交易', '股权与收益分配', '其他']

const form = reactive({
  consultType: '专属咨询',
  category: '跨区域纠纷', urgency: '一般',
  applicantName: userStore.user?.realName || '',
  applicantPhone: userStore.user?.phone || '',
  applicantRegion: userStore.user?.region || '',
  description: '', prevHandling: '',
})
const rules = {
  category: [{ required: true }],
  applicantName: [{ required: true }],
  applicantPhone: [{ required: true }],
  description: [{ required: true, message: '请输入咨询诉求' }],
}
const submit = async () => {
  if (!userStore.isLogin) { ElMessage.warning('请先登录'); router.push('/login'); return }
  await formRef.value.validate()
  loading.value = true
  try {
    await request.post('/legal-consult/save', form)
    ElMessage.success('专属咨询申请已提交')
    setTimeout(() => router.push('/legal/my'), 1000)
  } catch (e) {} finally { loading.value = false }
}
</script>
<style scoped>
.exclusive-card { padding: 32px 36px; }
.alert {
  background: linear-gradient(135deg, #fffbeb, #fef3c7);
  border-left: 4px solid #f59e0b;
  border-radius: 12px;
  padding: 18px 20px;
  display: flex;
  gap: 14px;
  margin-bottom: 28px;
}
.alert .el-icon { font-size: 28px; color: #f59e0b; flex-shrink: 0; }
.alert h4 { font-size: 15px; font-weight: 700; margin: 0 0 6px; color: #92400e; }
.alert p { font-size: 13px; color: #78350f; margin: 0; line-height: 1.7; }
.actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 12px; }
</style>

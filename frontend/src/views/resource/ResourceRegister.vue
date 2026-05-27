<template>
  <div>
    <PageHeader title="我要登记" parent="收储登记" desc="林农、合作社线上自主登记林地资源与碳汇资源 · 无需线下跑腿，电子材料一键提交" />

    <div class="page-container">
      <div class="register-grid">
        <!-- 左侧步骤 -->
        <div class="steps-panel eco-card">
          <h4>登记流程</h4>
          <el-steps direction="vertical" :active="step" finish-status="success">
            <el-step title="选择资源类型" description="林地/碳汇/水库/林下产品" />
            <el-step title="填写资源信息" description="名称、面积、权属、位置" />
            <el-step title="上传权属证明" description="林权证/承包合同等" />
            <el-step title="提交等待审核" description="林业部门 1-3 工作日内审核" />
          </el-steps>
          <div class="tips">
            <h5><el-icon><InfoFilled /></el-icon>温馨提示</h5>
            <ul>
              <li>登记免费，林农自主登记可享每亩 50 元一次性补贴</li>
              <li>权属证明需清晰完整，单文件不超过 20MB</li>
              <li>已成功登记后会同步至"森林资源云图"</li>
            </ul>
          </div>
        </div>

        <!-- 右侧表单 -->
        <div class="form-panel eco-card">
          <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" label-position="top">
            <div class="form-section">
              <h4>① 资源基本信息</h4>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="资源类型" prop="resourceType">
                    <el-select v-model="form.resourceType" style="width: 100%;" placeholder="请选择" @change="step = 1">
                      <el-option label="林地" value="林地" />
                      <el-option label="碳汇" value="碳汇" />
                      <el-option label="水库" value="水库" />
                      <el-option label="林下产品" value="林下产品" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="资源名称" prop="resourceName">
                    <el-input v-model="form.resourceName" placeholder="如：双一村毛竹林一区" />
                  </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.resourceType === '林地'">
                  <el-form-item label="林木类型">
                    <el-select v-model="form.forestType" style="width: 100%;">
                      <el-option label="商品林" value="商品林" />
                      <el-option label="公益林" value="公益林" />
                      <el-option label="经济林" value="经济林" />
                      <el-option label="混交林" value="混交林" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="资源面积(亩)" v-if="['林地', '林下产品', '水库'].includes(form.resourceType)">
                    <el-input-number v-model="form.areaMu" :min="0" :precision="2" style="width: 100%;" controls-position="right" />
                  </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.resourceType === '碳汇' || form.resourceType === '林地'">
                  <el-form-item label="碳汇储量(吨)">
                    <el-input-number v-model="form.carbonStorage" :min="0" :precision="2" style="width: 100%;" controls-position="right" />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>

            <div class="form-section">
              <h4>② 位置与权属</h4>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="行政区域" prop="region">
                    <el-input v-model="form.region" placeholder="如：湖北省宜昌市五峰县" @input="step = Math.max(step, 1)" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="详细地址">
                    <el-input v-model="form.address" placeholder="街道/村组" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="经度">
                    <el-input-number v-model="form.longitude" :precision="6" style="width: 100%;" controls-position="right" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="纬度">
                    <el-input-number v-model="form.latitude" :precision="6" style="width: 100%;" controls-position="right" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="权属人姓名/单位" prop="ownerName">
                    <el-input v-model="form.ownerName" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="证号(身份证/统一社会信用代码)">
                    <el-input v-model="form.ownerIdCard" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="权属证明编号">
                    <el-input v-model="form.certNo" placeholder="林证字/碳证字第 XXX 号" />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>

            <div class="form-section">
              <h4>③ 上传材料</h4>
              <el-form-item label="资源照片(封面)">
                <el-upload
                  action="/api/file/upload"
                  name="file"
                  :headers="{ Token: token }"
                  :on-success="onCoverSuccess"
                  :show-file-list="false"
                  list-type="picture-card"
                >
                  <img v-if="form.cover" :src="form.cover" style="width:100%;height:100%;object-fit:cover;border-radius:8px;" />
                  <el-icon v-else><Plus /></el-icon>
                </el-upload>
              </el-form-item>
              <el-form-item label="权属证明/承包合同(支持 jpg/pdf)">
                <el-upload
                  action="/api/file/upload"
                  name="file"
                  :headers="{ Token: token }"
                  :on-success="onContractSuccess"
                  v-model:file-list="contractFiles"
                  :limit="3"
                >
                  <el-button><el-icon><UploadFilled /></el-icon>选择文件</el-button>
                  <template #tip>最多 3 个文件，单文件不超过 20MB</template>
                </el-upload>
              </el-form-item>
              <el-form-item label="资源介绍">
                <el-input v-model="form.intro" type="textarea" :rows="3" placeholder="请简要介绍资源情况，如林相、树龄、林分密度等..." />
              </el-form-item>
            </div>

            <div class="form-actions">
              <el-button @click="$router.push('/resource/my')">查看我的登记</el-button>
              <el-button type="primary" :loading="loading" size="large" @click="submit">
                <el-icon><Promotion /></el-icon>提交登记申请
              </el-button>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)
const contractFiles = ref([])
const step = ref(0)

const token = computed(() => userStore.token)

const form = reactive({
  resourceType: '林地',
  resourceName: '',
  forestType: '',
  areaMu: null,
  carbonStorage: null,
  region: '',
  address: '',
  longitude: null,
  latitude: null,
  ownerName: '',
  ownerIdCard: '',
  certNo: '',
  cover: '',
  contractUrl: '',
  intro: '',
})

const rules = {
  resourceType: [{ required: true, message: '请选择资源类型' }],
  resourceName: [{ required: true, message: '请输入资源名称' }],
  region: [{ required: true, message: '请输入行政区域' }],
  ownerName: [{ required: true, message: '请输入权属人' }],
}

const onCoverSuccess = (res) => {
  if (res.code === 0) {
    form.cover = res.url
    step.value = Math.max(step.value, 2)
    ElMessage.success('封面上传成功')
  }
}
const onContractSuccess = (res) => {
  if (res.code === 0) {
    const arr = (form.contractUrl || '').split(',').filter(Boolean)
    arr.push(res.url)
    form.contractUrl = arr.join(',')
    step.value = Math.max(step.value, 2)
  }
}

const submit = async () => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  await formRef.value.validate()
  loading.value = true
  try {
    await request.post('/resource/save', form)
    step.value = 4
    ElMessage.success('登记申请已提交，等待审核')
    setTimeout(() => router.push('/resource/my'), 1200)
  } catch (e) {} finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-grid {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 22px;
}
.steps-panel { padding: 28px 24px; height: fit-content; position: sticky; top: 90px; }
.steps-panel h4 { font-size: 15px; font-weight: 700; margin: 0 0 18px; color: var(--text-primary); }
.tips {
  margin-top: 24px; padding-top: 20px;
  border-top: 1px dashed var(--border-light);
}
.tips h5 {
  font-size: 13px; font-weight: 600; color: var(--primary-darker);
  margin: 0 0 10px;
  display: flex; align-items: center; gap: 4px;
}
.tips ul { padding-left: 18px; margin: 0; }
.tips li { font-size: 12px; color: var(--text-tertiary); margin-bottom: 6px; line-height: 1.6; }

.form-panel { padding: 32px 36px; }
.form-section { margin-bottom: 30px; }
.form-section h4 {
  font-size: 16px; font-weight: 700; margin: 0 0 18px;
  color: var(--primary-darker);
  padding-bottom: 8px;
  border-bottom: 2px solid var(--primary-bg-2);
}
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 20px;
  border-top: 1px dashed var(--border-light);
}

@media (max-width: 1100px) {
  .register-grid { grid-template-columns: 1fr; }
  .steps-panel { position: static; }
}
</style>

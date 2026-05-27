<template>
  <div v-if="data">
    <PageHeader :title="data.title" parent="供需交易" />
    <div class="page-container">
      <div class="detail-grid">
        <div class="left">
          <div class="cover-card eco-card">
            <img :src="data.cover" />
            <div class="cover-tag"><el-tag size="default" effect="dark" round>{{ data.category }}</el-tag></div>
          </div>
          <div class="info-card eco-card">
            <h3>资源详情</h3>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="挂牌编号">{{ data.listingNo }}</el-descriptions-item>
              <el-descriptions-item label="子分类">{{ data.subCategory }}</el-descriptions-item>
              <el-descriptions-item label="所在区域">{{ data.region }}</el-descriptions-item>
              <el-descriptions-item label="资源面积">{{ data.areaMu || '-' }} 亩</el-descriptions-item>
              <el-descriptions-item label="管理单位">{{ data.manageOrg || '-' }}</el-descriptions-item>
              <el-descriptions-item label="挂牌到期">{{ formatDateTime(data.expireTime) }}</el-descriptions-item>
              <el-descriptions-item label="权属说明" :span="2">{{ data.ownership || '-' }}</el-descriptions-item>
            </el-descriptions>
            <h4 style="margin-top: 24px;">资源简介</h4>
            <p class="intro">{{ data.intro }}</p>
          </div>
        </div>

        <div class="right">
          <div class="price-card eco-card">
            <div class="pc-label">挂牌价格</div>
            <div class="pc-price">¥<span>{{ formatPrice(data.price) }}</span></div>
            <div class="pc-valuation" v-if="data.valuationMin">
              评估区间：¥{{ formatPrice(data.valuationMin) }} - ¥{{ formatPrice(data.valuationMax) }}
            </div>
            <div class="pc-stats">
              <div><strong>{{ data.viewCount }}</strong><small>浏览次数</small></div>
              <div><strong>{{ data.appointmentCount }}</strong><small>预约次数</small></div>
            </div>
            <el-button type="primary" round size="large" style="width:100%; margin-top: 16px;" @click="showAppoint = true" :disabled="!data.acceptAppointment">
              <el-icon><Calendar /></el-icon>{{ data.acceptAppointment ? '在线预约洽谈' : '暂不接受预约' }}
            </el-button>
            <el-button round size="large" style="width:100%; margin-top: 10px;" @click="callPhone">
              <el-icon><Phone /></el-icon>电话联系挂牌方
            </el-button>
          </div>
          <div class="seller-card eco-card">
            <h4>挂牌方信息</h4>
            <div class="seller-row">
              <el-avatar :size="48" :src="`https://api.dicebear.com/7.x/identicon/svg?seed=${data.sellerId}`" />
              <div>
                <h5>{{ data.sellerName }}</h5>
                <p>📞 {{ data.sellerPhone || '联系电话保密' }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 预约洽谈对话框 -->
    <el-dialog v-model="showAppoint" title="预约洽谈" width="520px">
      <el-form :model="form" label-position="top">
        <el-form-item label="洽谈方式">
          <el-radio-group v-model="form.meetType"><el-radio value="线上">线上沟通</el-radio><el-radio value="线下">线下勘查</el-radio></el-radio-group>
        </el-form-item>
        <el-row :gutter="14">
          <el-col :span="12"><el-form-item label="期望时间"><el-date-picker v-model="form.meetTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%;" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系电话"><el-input v-model="form.applicantPhone" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="洽谈地点" v-if="form.meetType === '线下'"><el-input v-model="form.meetPlace" /></el-form-item>
        <el-form-item label="留言"><el-input v-model="form.message" type="textarea" :rows="3" placeholder="请简要说明您的需求..." /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAppoint = false">取消</el-button>
        <el-button type="primary" :loading="loading" @click="submitAppoint">提交预约</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const route = useRoute()
const userStore = useUserStore()
const data = ref(null)
const showAppoint = ref(false)
const loading = ref(false)
const form = reactive({
  targetType: 'listing',
  targetId: null, targetTitle: '',
  applicantName: userStore.user?.realName || '',
  applicantPhone: userStore.user?.phone || '',
  applicantOrg: userStore.user?.realName || '',
  meetType: '线上', meetTime: '', meetPlace: '',
  message: '',
})

const formatPrice = (n) => n >= 10000 ? (n / 10000).toFixed(2) + ' 万' : (Number(n) || 0).toLocaleString()
const formatDateTime = (d) => d ? new Date(d).toLocaleString('zh-CN') : '-'

const callPhone = () => {
  if (data.value.sellerPhone) window.location.href = 'tel:' + data.value.sellerPhone
  else ElMessage.info('挂牌方未公开电话，请使用在线预约')
}

const submitAppoint = async () => {
  if (!userStore.isLogin) { ElMessage.warning('请先登录'); return }
  if (!form.meetTime) { ElMessage.warning('请选择期望时间'); return }
  loading.value = true
  try {
    await request.post('/appointment/save', form)
    ElMessage.success('预约已提交，等待对方回复')
    showAppoint.value = false
  } catch (e) {} finally { loading.value = false }
}

onMounted(async () => {
  const res = await request.get('/listing/detail/' + route.params.id)
  data.value = res.data
  form.targetId = data.value.id
  form.targetTitle = data.value.title
})
</script>
<style scoped>
.detail-grid { display: grid; grid-template-columns: 1.4fr 1fr; gap: 22px; }
.left { display: flex; flex-direction: column; gap: 22px; }
.cover-card { padding: 0; overflow: hidden; position: relative; }
.cover-card img { width: 100%; height: 380px; object-fit: cover; display: block; }
.cover-tag { position: absolute; top: 16px; left: 16px; }

.info-card { padding: 28px 32px; }
.info-card h3, .info-card h4 { font-size: 16px; font-weight: 700; margin: 0 0 16px; color: var(--text-primary); }
.info-card h3::before, .info-card h4::before {
  content: ''; display: inline-block; width: 3px; height: 14px;
  background: var(--primary); border-radius: 2px; margin-right: 8px; vertical-align: middle;
}
.intro { font-size: 14px; line-height: 1.85; color: var(--text-secondary); margin: 0; }

.right { display: flex; flex-direction: column; gap: 22px; position: sticky; top: 90px; height: fit-content; }
.price-card {
  padding: 28px;
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
}
.pc-label { font-size: 14px; color: var(--text-tertiary); }
.pc-price {
  font-size: 16px;
  color: var(--accent-rose);
  font-weight: 800;
  margin: 6px 0 4px;
}
.pc-price span { font-size: 36px; }
.pc-valuation { font-size: 12px; color: var(--text-tertiary); }
.pc-stats {
  display: flex; gap: 24px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px dashed rgba(15, 23, 42, 0.1);
}
.pc-stats > div { display: flex; flex-direction: column; }
.pc-stats strong { font-size: 18px; color: var(--text-primary); font-weight: 700; }
.pc-stats small { font-size: 12px; color: var(--text-tertiary); }

.seller-card { padding: 24px; }
.seller-card h4 { font-size: 15px; font-weight: 700; margin: 0 0 14px; }
.seller-row { display: flex; gap: 14px; align-items: center; }
.seller-row h5 { font-size: 14px; font-weight: 700; margin: 0 0 4px; color: var(--text-primary); }
.seller-row p { font-size: 12px; color: var(--text-tertiary); margin: 0; }

@media (max-width: 1100px) {
  .detail-grid { grid-template-columns: 1fr; }
  .right { position: static; }
}
</style>

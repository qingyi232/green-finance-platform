<template>
  <div class="login-page">
    <div class="bg-decor">
      <div class="leaf leaf-1"></div>
      <div class="leaf leaf-2"></div>
      <div class="leaf leaf-3"></div>
    </div>

    <div class="login-container">
      <!-- 左侧品牌 -->
      <div class="brand-side">
        <div class="brand-header" @click="$router.push('/home')">
          <div class="brand-logo"><el-icon :size="24"><Stamp /></el-icon></div>
          <div>
            <h1>林林通·绿融通</h1>
            <p>一站式生态银行综合服务网站</p>
          </div>
        </div>

        <div class="brand-content">
          <h2>欢迎来到<br />绿色生态服务平台</h2>
          <p>聚焦林业与碳汇 · 五大核心业务一站直达</p>
          <ul class="brand-features">
            <li><el-icon><CircleCheckFilled /></el-icon> 资源收储一张图可视化展示</li>
            <li><el-icon><CircleCheckFilled /></el-icon> 专业评估机构线上对接</li>
            <li><el-icon><CircleCheckFilled /></el-icon> 供需精准匹配 · 预约洽谈</li>
            <li><el-icon><CircleCheckFilled /></el-icon> 绿色金融产品智能匹配</li>
            <li><el-icon><CircleCheckFilled /></el-icon> 法律援助护航资源流转</li>
          </ul>
        </div>

        <div class="brand-footer">
          © 2026 林林通·绿融通 · 让绿水青山成为金山银山
        </div>
      </div>

      <!-- 右侧表单 -->
      <div class="form-side">
        <div class="form-box">
          <h3>账号登录</h3>
          <p class="sub">使用您的账号登录林林通·绿融通</p>

          <div class="role-grid">
            <div
              v-for="r in roles"
              :key="r.value"
              class="role-card"
              :class="{ active: form.role === r.value }"
              @click="form.role = r.value"
            >
              <el-icon :size="18"><component :is="r.icon" /></el-icon>
              <span>{{ r.label }}</span>
            </div>
          </div>

          <el-form :model="form" @submit.prevent>
            <el-form-item>
              <el-input v-model="form.username" size="large" placeholder="请输入账号" prefix-icon="User" clearable />
            </el-form-item>
            <el-form-item>
              <el-input v-model="form.password" type="password" size="large" placeholder="请输入密码" prefix-icon="Lock" show-password @keyup.enter="handleLogin" />
            </el-form-item>
            <el-button type="primary" size="large" :loading="loading" @click="handleLogin" class="submit-btn">
              登 录
            </el-button>
          </el-form>

          <div class="form-footer">
            <span>还没有账号？</span>
            <router-link to="/register">立即注册</router-link>
          </div>

          <div class="demo-card">
            <div class="demo-title"><el-icon><Promotion /></el-icon> 演示账号 · 密码均为 123456</div>
            <div class="demo-list">
              <span @click="fillDemo('admin')">管理员: admin</span>
              <span @click="fillDemo('linnong01')">林农: linnong01</span>
              <span @click="fillDemo('coop01')">合作社: coop01</span>
              <span @click="fillDemo('ent01')">企业: ent01</span>
              <span @click="fillDemo('bank01')">银行: bank01</span>
              <span @click="fillDemo('eorg01')">评估机构: eorg01</span>
              <span @click="fillDemo('legalorg01')">法律机构: legalorg01</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'
import request from '../../api/request'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  role: '林农',
})

const roles = [
  { value: '林农', label: '林农', icon: 'Avatar' },
  { value: '合作社', label: '合作社', icon: 'House' },
  { value: '企业', label: '企业', icon: 'OfficeBuilding' },
  { value: '林业管理部门', label: '林业部门', icon: 'Stamp' },
  { value: '金融机构', label: '金融机构', icon: 'Money' },
  { value: '评估师', label: '评估师', icon: 'DocumentChecked' },
  { value: '评估机构', label: '评估机构', icon: 'Postcard' },
  { value: '律师', label: '律师', icon: 'Service' },
  { value: '法律援助机构', label: '法律机构', icon: 'Service' },
  { value: '管理员', label: '管理员', icon: 'Setting' },
]

const fillDemo = (username) => {
  form.username = username
  form.password = '123456'
  form.role = ''
}

const handleLogin = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入账号和密码')
    return
  }
  loading.value = true
  try {
    const res = await request.post('/auth/login', {
      username: form.username,
      password: form.password,
      role: form.role,
    })
    userStore.setLogin(res.token, res.user)
    ElMessage.success('登录成功，欢迎回来')
    if (res.user.role === '管理员') router.push('/admin')
    else router.push('/home')
  } catch (e) {
    // intercepted
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 50%, #a7f3d0 100%);
  overflow: hidden;
  padding: 24px;
}
.bg-decor { position: absolute; inset: 0; pointer-events: none; }
.leaf { position: absolute; border-radius: 50%; filter: blur(80px); opacity: 0.4; }
.leaf-1 { width: 480px; height: 480px; background: #6ee7b7; top: -120px; right: -100px; }
.leaf-2 { width: 360px; height: 360px; background: #34d399; bottom: -80px; left: -60px; opacity: 0.35; }
.leaf-3 { width: 220px; height: 220px; background: #5eead4; top: 30%; right: 30%; opacity: 0.2; }

.login-container {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: 1.05fr 1fr;
  width: 1080px;
  max-width: 100%;
  min-height: 620px;
  background: white;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 30px 80px rgba(15, 23, 42, 0.16);
}

.brand-side {
  background: linear-gradient(160deg, #047857 0%, #10b981 50%, #34d399 100%);
  padding: 48px 44px;
  display: flex;
  flex-direction: column;
  color: white;
  position: relative;
  overflow: hidden;
}
.brand-side::before {
  content: "";
  position: absolute;
  inset: -50px;
  background: radial-gradient(circle at 30% 30%, rgba(255, 255, 255, 0.12) 0%, transparent 60%);
  pointer-events: none;
}

.brand-header {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  position: relative;
  z-index: 1;
}
.brand-logo {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.18);
  border: 1px solid rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}
.brand-header h1 { font-size: 18px; font-weight: 800; margin: 0; letter-spacing: 0.5px; }
.brand-header p { font-size: 11px; margin: 2px 0 0; opacity: 0.85; letter-spacing: 1px; }

.brand-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  z-index: 1;
  margin: 30px 0;
}
.brand-content h2 {
  font-size: 30px;
  font-weight: 800;
  line-height: 1.4;
  margin: 0 0 12px;
  letter-spacing: 0.5px;
}
.brand-content p {
  font-size: 14px;
  opacity: 0.9;
  margin: 0 0 28px;
}
.brand-features { list-style: none; padding: 0; margin: 0; }
.brand-features li {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  margin-bottom: 14px;
  opacity: 0.95;
}
.brand-features li .el-icon { color: #d1fae5; }

.brand-footer {
  position: relative;
  z-index: 1;
  font-size: 12px;
  opacity: 0.8;
}

/* 表单侧 */
.form-side {
  padding: 48px 56px;
  display: flex;
  align-items: center;
}
.form-box { width: 100%; }
.form-box h3 {
  font-size: 26px;
  font-weight: 800;
  margin: 0 0 6px;
  color: var(--text-primary);
}
.sub {
  font-size: 13px;
  color: var(--text-tertiary);
  margin: 0 0 24px;
}
.role-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 8px;
  margin-bottom: 24px;
}
.role-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 10px 4px;
  border: 1.5px solid var(--border);
  border-radius: 10px;
  cursor: pointer;
  font-size: 11px;
  color: var(--text-tertiary);
  transition: all 0.2s;
}
.role-card:hover { border-color: var(--primary-light); }
.role-card.active {
  border-color: var(--primary);
  background: var(--primary-bg);
  color: var(--primary-darker);
  font-weight: 600;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 12px;
  margin-top: 4px;
  background: var(--gradient-forest);
  border: none;
  box-shadow: 0 8px 20px rgba(16, 185, 129, 0.32);
}
.submit-btn:hover {
  background: var(--gradient-forest);
  filter: brightness(1.05);
}

.form-footer {
  text-align: center;
  margin-top: 18px;
  font-size: 13px;
  color: var(--text-tertiary);
}
.form-footer a { color: var(--primary-darker); font-weight: 600; margin-left: 4px; }

.demo-card {
  margin-top: 24px;
  padding: 14px 16px;
  background: var(--primary-bg);
  border: 1px dashed var(--primary-light);
  border-radius: 12px;
}
.demo-title {
  font-size: 12px;
  color: var(--primary-darker);
  font-weight: 600;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 4px;
}
.demo-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}
.demo-list span {
  font-size: 12px;
  padding: 4px 10px;
  background: white;
  border: 1px solid var(--primary-bg-2);
  color: var(--text-secondary);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
}
.demo-list span:hover {
  border-color: var(--primary);
  color: var(--primary-darker);
  background: var(--primary-bg-2);
}

@media (max-width: 900px) {
  .login-container { grid-template-columns: 1fr; }
  .brand-side { display: none; }
  .form-side { padding: 36px 24px; }
}
</style>

<template>
  <div class="reg-page">
    <div class="bg-decor"><div class="leaf leaf-1"></div><div class="leaf leaf-2"></div></div>
    <div class="reg-card">
      <div class="reg-header">
        <div class="logo" @click="$router.push('/home')">
          <div class="logo-mark"><el-icon :size="20"><Stamp /></el-icon></div>
          <span>林林通·绿融通</span>
        </div>
        <h2>注册账号</h2>
        <p>欢迎加入林林通·绿融通生态银行服务网站</p>
      </div>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" size="large">
        <el-form-item label="用户角色" prop="role">
          <el-select v-model="form.role" style="width: 100%;">
            <el-option v-for="r in roles" :key="r" :label="r" :value="r" />
          </el-select>
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" placeholder="6-20 位英文/数字" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="6 位以上" />
        </el-form-item>
        <el-form-item label="姓名/机构名" prop="realName">
          <el-input v-model="form.realName" placeholder="个人填写真实姓名/机构填写完整名称" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="所在区域">
          <el-input v-model="form.region" placeholder="如：湖北省宜昌市五峰县" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="个人简介">
          <el-input v-model="form.intro" type="textarea" :rows="2" />
        </el-form-item>

        <div class="reg-actions">
          <el-button @click="$router.push('/login')">返回登录</el-button>
          <el-button type="primary" :loading="loading" @click="submit">立即注册</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../api/request'

const router = useRouter()
const loading = ref(false)
const formRef = ref()

const roles = ['林农', '合作社', '企业', '金融机构', '评估师', '评估机构', '律师', '法律援助机构']

const form = reactive({
  role: '林农',
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  region: '',
  address: '',
  intro: '',
})

const rules = {
  role: [{ required: true, message: '请选择角色' }],
  username: [{ required: true, message: '请输入账号' }, { min: 4, max: 20, message: '4-20 位' }],
  password: [{ required: true, message: '请输入密码' }, { min: 6, message: '至少 6 位' }],
  realName: [{ required: true, message: '请输入姓名/机构名' }],
  phone: [{ required: true, message: '请输入手机号' }, { pattern: /^1\d{10}$/, message: '手机号格式不正确' }],
}

const submit = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await request.post('/auth/register', form)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (e) {} finally {
    loading.value = false
  }
}
</script>

<style scoped>
.reg-page {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px;
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 60%, #a7f3d0 100%);
  overflow: hidden;
}
.bg-decor { position: absolute; inset: 0; pointer-events: none; }
.leaf { position: absolute; border-radius: 50%; filter: blur(80px); opacity: 0.4; }
.leaf-1 { width: 460px; height: 460px; background: #6ee7b7; top: -120px; right: -120px; }
.leaf-2 { width: 320px; height: 320px; background: #34d399; bottom: -80px; left: -60px; }

.reg-card {
  position: relative;
  z-index: 1;
  width: 580px;
  max-width: 100%;
  background: white;
  border-radius: 24px;
  padding: 36px 44px;
  box-shadow: 0 30px 80px rgba(15, 23, 42, 0.14);
}
.reg-header { text-align: center; margin-bottom: 24px; }
.logo {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  margin-bottom: 20px;
}
.logo-mark {
  width: 38px; height: 38px;
  border-radius: 10px;
  background: var(--gradient-forest);
  color: white;
  display: flex; align-items: center; justify-content: center;
}
.logo span { font-size: 16px; font-weight: 800; color: var(--text-primary); }
.reg-header h2 { font-size: 24px; font-weight: 800; margin: 0 0 6px; color: var(--text-primary); }
.reg-header p { font-size: 13px; color: var(--text-tertiary); margin: 0; }

.reg-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 20px;
}
</style>

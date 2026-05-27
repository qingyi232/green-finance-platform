<template>
  <div class="public-layout">
    <!-- 顶部导航 -->
    <header class="top-header" :class="{ scrolled: scrolled }">
      <div class="header-inner">
        <div class="logo" @click="$router.push('/home')">
          <div class="logo-mark">
            <el-icon :size="22"><Stamp /></el-icon>
          </div>
          <div class="logo-text">
            <h1>林林通<span class="dot">·</span>绿融通</h1>
            <p>一站式生态银行综合服务网站</p>
          </div>
        </div>

        <nav class="main-nav">
          <router-link to="/home" :class="{ active: $route.path === '/home' }">首页</router-link>
          <router-link to="/resource" :class="{ active: $route.path.startsWith('/resource') }">收储登记</router-link>
          <router-link to="/evaluation" :class="{ active: $route.path.startsWith('/evaluation') }">资产评估</router-link>
          <router-link to="/trading" :class="{ active: $route.path.startsWith('/trading') }">供需交易</router-link>
          <router-link to="/finance" :class="{ active: $route.path.startsWith('/finance') }">金融服务</router-link>
          <router-link to="/legal" :class="{ active: $route.path.startsWith('/legal') }">法律服务</router-link>
        </nav>

        <div class="header-right">
          <template v-if="userStore.isLogin">
            <el-dropdown trigger="click" placement="bottom-end">
              <div class="user-avatar">
                <el-avatar :size="34" :src="userStore.user?.avatar || defaultAvatar" />
                <span>{{ userStore.user?.realName || userStore.user?.username }}</span>
                <el-icon class="caret"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/profile')"><el-icon><User /></el-icon>个人中心</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/resource/my')"><el-icon><Notebook /></el-icon>我的登记</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/evaluation/my')"><el-icon><DocumentChecked /></el-icon>我的评估</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/trading/my-listings')"><el-icon><Goods /></el-icon>我的挂牌</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/trading/my-demands')"><el-icon><ShoppingCart /></el-icon>我的需求</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/trading/my-appointments')"><el-icon><Calendar /></el-icon>我的预约</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/finance/my')"><el-icon><Money /></el-icon>我的贷款</el-dropdown-item>
                  <el-dropdown-item v-if="userStore.role === '金融机构'" @click="$router.push('/finance/review')"><el-icon><CircleCheck /></el-icon>贷款审批</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/legal/my')"><el-icon><Service /></el-icon>我的咨询</el-dropdown-item>
                  <el-dropdown-item v-if="userStore.role === '企业'" @click="$router.push('/disclosure-publish')" divided><el-icon><Document /></el-icon>信息披露</el-dropdown-item>
                  <el-dropdown-item v-if="userStore.role === '管理员'" divided @click="$router.push('/admin')"><el-icon><Setting /></el-icon>后台管理</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout"><el-icon><SwitchButton /></el-icon>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button text @click="$router.push('/login')">登录</el-button>
            <el-button type="primary" round @click="$router.push('/register')">立即注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="fade-slide" mode="out-in">
          <component :is="Component" :key="$route.fullPath" />
        </transition>
      </router-view>
    </main>

    <footer class="page-footer">
      <div class="footer-inner">
        <div class="footer-brand">
          <div class="logo-mark mini"><el-icon :size="18"><Stamp /></el-icon></div>
          <div>
            <h3>林林通·绿融通</h3>
            <p>聚焦林业与碳汇 · 一站式生态银行服务</p>
          </div>
        </div>
        <div class="footer-cols">
          <div class="footer-col">
            <h4>核心业务</h4>
            <a @click="$router.push('/resource')">收储登记</a>
            <a @click="$router.push('/evaluation')">资产评估</a>
            <a @click="$router.push('/trading')">供需交易</a>
            <a @click="$router.push('/finance')">金融服务</a>
            <a @click="$router.push('/legal')">法律服务</a>
          </div>
          <div class="footer-col">
            <h4>关于</h4>
            <a @click="$router.push('/news')">新闻动态</a>
            <a @click="$router.push('/policy')">政策解读</a>
            <a @click="$router.push('/disclosure')">信息披露</a>
            <a @click="$router.push('/feedback')">问题反馈</a>
          </div>
          <div class="footer-col">
            <h4>用户入口</h4>
            <a @click="$router.push('/login')">登录</a>
            <a @click="$router.push('/register')">注册</a>
            <a @click="$router.push('/profile')">个人中心</a>
          </div>
          <div class="footer-col contact-col">
            <h4>联系我们</h4>
            <p><el-icon><Phone /></el-icon> 400-888-0010</p>
            <p><el-icon><Message /></el-icon> service@linlintong.cn</p>
            <p><el-icon><Location /></el-icon> 湖北省武汉市洪山区</p>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        © 2026 林林通·绿融通 ALL Rights Reserved · 鄂ICP备20260001号 · 立足生态银行实践，赋能绿色产业发展
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'

const router = useRouter()
const userStore = useUserStore()
const scrolled = ref(false)
const defaultAvatar = 'https://api.dicebear.com/7.x/avataaars/svg?seed=default'

const onScroll = () => { scrolled.value = window.scrollY > 20 }
onMounted(() => window.addEventListener('scroll', onScroll))
onUnmounted(() => window.removeEventListener('scroll', onScroll))

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.public-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-page);
}

/* ===== 顶部导航 ===== */
.top-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.78);
  backdrop-filter: blur(24px) saturate(180%);
  -webkit-backdrop-filter: blur(24px) saturate(180%);
  border-bottom: 1px solid rgba(255, 255, 255, 0.5);
  transition: all 0.3s;
}
.top-header.scrolled {
  box-shadow: 0 2px 12px rgba(15, 23, 42, 0.06);
  background: rgba(255, 255, 255, 0.92);
  border-bottom-color: rgba(226, 232, 240, 0.6);
}
.header-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
  height: 72px;
  display: flex;
  align-items: center;
  gap: 32px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  flex-shrink: 0;
}
.logo-mark {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  background: var(--gradient-forest);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 4px 14px rgba(16, 185, 129, 0.32);
}
.logo-mark.mini { width: 36px; height: 36px; border-radius: 10px; }
.logo-text h1 {
  font-size: 18px;
  font-weight: 800;
  margin: 0;
  color: var(--text-primary);
  letter-spacing: 0.5px;
}
.logo-text h1 .dot { color: var(--primary); margin: 0 2px; }
.logo-text p {
  margin: 0;
  font-size: 11px;
  color: var(--text-tertiary);
  letter-spacing: 1px;
}

.main-nav {
  flex: 1;
  display: flex;
  gap: 4px;
  justify-content: center;
}
.main-nav a {
  padding: 8px 16px;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
  border-radius: 10px;
  transition: all 0.2s;
  position: relative;
}
.main-nav a:hover {
  color: var(--primary-darker);
  background: var(--primary-bg);
}
.main-nav a.active {
  color: var(--primary-darker);
  background: var(--primary-bg);
}
.main-nav a.active::after {
  content: "";
  position: absolute;
  bottom: -2px;
  left: 50%;
  transform: translateX(-50%);
  width: 18px;
  height: 3px;
  border-radius: 3px;
  background: var(--primary);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}
.user-avatar {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 4px 12px 4px 4px;
  border-radius: 999px;
  transition: background 0.2s;
}
.user-avatar:hover { background: var(--primary-bg); }
.user-avatar span { font-size: 14px; font-weight: 500; color: var(--text-primary); }
.user-avatar .caret { color: var(--text-tertiary); font-size: 12px; }

/* ===== 主内容区 ===== */
.main-content {
  flex: 1;
}

/* ===== 底部 ===== */
.page-footer {
  margin-top: 48px;
  background: linear-gradient(180deg, #ffffff 0%, #ecfdf5 100%);
  border-top: 1px solid var(--border-light);
}
.footer-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 48px 24px 32px;
  display: flex;
  gap: 60px;
  flex-wrap: wrap;
}
.footer-brand {
  display: flex;
  gap: 12px;
  align-items: flex-start;
  flex: 0 0 280px;
}
.footer-brand h3 { font-size: 16px; font-weight: 700; margin: 0 0 6px; color: var(--text-primary); }
.footer-brand p { margin: 0; font-size: 13px; color: var(--text-tertiary); line-height: 1.6; }
.footer-cols {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 36px;
}
.footer-col h4 {
  font-size: 14px;
  font-weight: 600;
  margin: 0 0 14px;
  color: var(--text-primary);
}
.footer-col a {
  display: block;
  font-size: 13px;
  color: var(--text-tertiary);
  margin-bottom: 8px;
  cursor: pointer;
}
.footer-col a:hover { color: var(--primary); }
.footer-col p {
  font-size: 13px;
  color: var(--text-tertiary);
  margin: 0 0 8px;
  display: flex;
  align-items: center;
  gap: 6px;
}
.footer-bottom {
  border-top: 1px solid var(--border-light);
  padding: 18px 24px;
  text-align: center;
  font-size: 12px;
  color: var(--text-light);
}

/* ===== 路由切换动画 ===== */
.fade-slide-enter-active, .fade-slide-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.fade-slide-enter-from { opacity: 0; transform: translateY(8px); }
.fade-slide-leave-to { opacity: 0; transform: translateY(-8px); }

@media (max-width: 1100px) {
  .main-nav { display: none; }
  .header-inner { gap: 20px; }
  .footer-cols { grid-template-columns: repeat(2, 1fr); }
}
</style>

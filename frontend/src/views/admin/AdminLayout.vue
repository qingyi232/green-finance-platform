<template>
  <el-container class="admin-layout">
    <el-aside :width="collapsed ? '64px' : '240px'" class="sider">
      <div class="brand" @click="$router.push('/admin')">
        <div class="brand-icon"><el-icon :size="20"><Stamp /></el-icon></div>
        <transition name="fade"><div v-if="!collapsed" class="brand-text">
          <h3>林林通·绿融通</h3>
          <p>管理后台</p>
        </div></transition>
      </div>

      <el-menu
        :default-active="$route.path"
        :collapse="collapsed"
        :router="true"
        class="side-menu"
        background-color="transparent"
        text-color="#cbd5e1"
        active-text-color="#34d399"
      >
        <el-menu-item index="/admin"><el-icon><DataLine /></el-icon><template #title>数据看板</template></el-menu-item>
        <el-sub-menu index="content">
          <template #title><el-icon><Document /></el-icon>内容中心</template>
          <el-menu-item index="/admin/news">新闻动态</el-menu-item>
          <el-menu-item index="/admin/policy">政策解读</el-menu-item>
          <el-menu-item index="/admin/feedback">问题反馈</el-menu-item>
          <el-menu-item index="/admin/disclosure">信息披露</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/admin/resource"><el-icon><Stamp /></el-icon><template #title>收储登记</template></el-menu-item>
        <el-sub-menu index="evaluation">
          <template #title><el-icon><DocumentChecked /></el-icon>资产评估</template>
          <el-menu-item index="/admin/evaluation-org">评估机构</el-menu-item>
          <el-menu-item index="/admin/evaluator">评估师</el-menu-item>
          <el-menu-item index="/admin/evaluation-apply">评估申请</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="trading">
          <template #title><el-icon><Goods /></el-icon>供需交易</template>
          <el-menu-item index="/admin/listing">资源挂牌</el-menu-item>
          <el-menu-item index="/admin/demand">资源需求</el-menu-item>
          <el-menu-item index="/admin/farm-product">特色农产品</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="finance">
          <template #title><el-icon><Money /></el-icon>金融服务</template>
          <el-menu-item index="/admin/finance-product">金融产品</el-menu-item>
          <el-menu-item index="/admin/loan-apply">贷款申请</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="legal">
          <template #title><el-icon><Service /></el-icon>法律服务</template>
          <el-menu-item index="/admin/legal-org">援助机构</el-menu-item>
          <el-menu-item index="/admin/lawyer">律师</el-menu-item>
          <el-menu-item index="/admin/legal-consult">法律咨询</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/admin/users"><el-icon><User /></el-icon><template #title>用户管理</template></el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="top-bar">
        <el-button text @click="collapsed = !collapsed"><el-icon :size="18"><Expand v-if="collapsed" /><Fold v-else /></el-icon></el-button>
        <el-breadcrumb separator-icon="ArrowRight">
          <el-breadcrumb-item :to="{ path: '/admin' }">后台</el-breadcrumb-item>
          <el-breadcrumb-item>{{ currentName }}</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="top-right">
          <el-button text @click="$router.push('/home')"><el-icon><Promotion /></el-icon>前台首页</el-button>
          <el-dropdown>
            <div class="user-info">
              <el-avatar :size="32" :src="userStore.user?.avatar" />
              <span>{{ userStore.user?.realName || userStore.user?.username }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="logout"><el-icon><SwitchButton /></el-icon>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main"><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const collapsed = ref(false)

const nameMap = {
  '/admin': '数据看板',
  '/admin/users': '用户管理',
  '/admin/news': '新闻动态',
  '/admin/policy': '政策解读',
  '/admin/feedback': '问题反馈',
  '/admin/disclosure': '信息披露',
  '/admin/resource': '收储登记',
  '/admin/evaluation-org': '评估机构',
  '/admin/evaluator': '评估师',
  '/admin/evaluation-apply': '评估申请',
  '/admin/listing': '资源挂牌',
  '/admin/demand': '资源需求',
  '/admin/farm-product': '特色农产品',
  '/admin/finance-product': '金融产品',
  '/admin/loan-apply': '贷款申请',
  '/admin/legal-org': '援助机构',
  '/admin/lawyer': '律师',
  '/admin/legal-consult': '法律咨询',
}
const currentName = computed(() => nameMap[route.path] || '管理后台')

const logout = () => {
  userStore.logout()
  ElMessage.success('已退出')
  router.push('/login')
}
</script>

<style scoped>
.admin-layout { height: 100vh; }
.sider {
  background: linear-gradient(180deg, #064e3b 0%, #065f46 100%);
  transition: width 0.3s;
  overflow-x: hidden;
}
.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 18px 16px;
  border-bottom: 1px solid rgba(255,255,255,0.08);
  cursor: pointer;
}
.brand-icon {
  width: 36px; height: 36px;
  background: linear-gradient(135deg, #10b981, #34d399);
  border-radius: 10px;
  display: flex; align-items: center; justify-content: center;
  color: white;
  flex-shrink: 0;
}
.brand-text h3 { font-size: 14px; font-weight: 800; margin: 0; color: white; white-space: nowrap; }
.brand-text p { font-size: 11px; margin: 2px 0 0; color: #6ee7b7; opacity: 0.8; }
.fade-enter-active, .fade-leave-active { transition: opacity 0.3s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }

.side-menu { border-right: none; padding: 8px; }
:deep(.el-menu-item), :deep(.el-sub-menu__title) { border-radius: 8px; margin-bottom: 4px; }
:deep(.el-menu-item:hover), :deep(.el-sub-menu__title:hover) { background: rgba(16, 185, 129, 0.16) !important; }
:deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(16,185,129,0.32), rgba(16,185,129,0.1)) !important;
  color: #34d399 !important;
}

.top-bar {
  display: flex;
  align-items: center;
  gap: 16px;
  background: white;
  box-shadow: 0 1px 3px rgba(0,0,0,0.06);
  padding: 0 24px;
}
.top-right { margin-left: auto; display: flex; align-items: center; gap: 12px; }
.user-info { display: flex; align-items: center; gap: 8px; cursor: pointer; padding: 4px 12px 4px 4px; border-radius: 999px; }
.user-info:hover { background: var(--primary-bg); }
.user-info span { font-size: 13px; }

.main { background: #f0fdfa; padding: 24px; overflow-y: auto; }
</style>

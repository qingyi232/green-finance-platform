<template>
  <div class="page-header" :style="bgStyle">
    <div class="page-header-inner">
      <div class="ph-breadcrumb">
        <span @click="$router.push('/home')">首页</span>
        <el-icon><ArrowRight /></el-icon>
        <span v-if="parent">{{ parent }}</span>
        <template v-if="parent"><el-icon><ArrowRight /></el-icon></template>
        <span class="cur">{{ title }}</span>
      </div>
      <h1 class="ph-title">{{ title }}</h1>
      <p v-if="desc" class="ph-desc">{{ desc }}</p>
      <slot name="extra" />
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({
  title: String,
  desc: String,
  parent: String,
  gradient: { type: String, default: 'linear-gradient(120deg, #ecfdf5 0%, #d1fae5 70%, #a7f3d0 100%)' },
})
const bgStyle = computed(() => ({ background: props.gradient }))
</script>
<style scoped>
.page-header {
  position: relative;
  padding: 60px 0 50px;
  margin-bottom: 32px;
  overflow: hidden;
}
.page-header::before, .page-header::after {
  content: "";
  position: absolute;
  border-radius: 50%;
  filter: blur(70px);
  opacity: 0.45;
  pointer-events: none;
}
.page-header::before {
  width: 360px; height: 360px; background: #6ee7b7;
  top: -120px; right: 6%;
}
.page-header::after {
  width: 240px; height: 240px; background: #5eead4;
  bottom: -100px; left: 8%; opacity: 0.3;
}
.page-header-inner {
  position: relative;
  z-index: 1;
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
}
.ph-breadcrumb {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-tertiary);
  margin-bottom: 14px;
}
.ph-breadcrumb span { cursor: pointer; }
.ph-breadcrumb span:hover { color: var(--primary); }
.ph-breadcrumb .cur { color: var(--primary-darker); font-weight: 500; cursor: default; }
.ph-title {
  font-size: 34px;
  font-weight: 800;
  margin: 0 0 10px;
  color: var(--text-primary);
  letter-spacing: 0.5px;
  text-shadow: 0 2px 6px rgba(4, 120, 87, 0.1);
}
.ph-desc {
  font-size: 15px;
  color: var(--text-secondary);
  margin: 0;
  max-width: 800px;
  line-height: 1.7;
}
</style>

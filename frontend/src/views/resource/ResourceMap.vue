<template>
  <div>
    <PageHeader
      title="森林生态资源云图"
      parent="收储登记"
      desc="依托高德地图 GIS · 全域林地、碳汇、水库、林下产品资源一张图可视化呈现"
    />
    <div class="page-container">
      <div class="map-grid">
        <!-- 左侧 -->
        <div class="left-panel">
          <div class="stat-card eco-card">
            <h4>资源总览</h4>
            <div class="big-stat">
              <div class="bs-item"><span>{{ list.length }}</span><small>资源(处)</small></div>
              <div class="bs-item"><span>{{ totalArea.toFixed(0) }}</span><small>面积(亩)</small></div>
              <div class="bs-item"><span>{{ totalCarbon.toFixed(0) }}</span><small>碳汇(吨)</small></div>
            </div>
          </div>

          <div class="filter-card eco-card">
            <h4>资源筛选</h4>
            <div class="type-filter">
              <div v-for="t in types" :key="t.value" class="type-btn" :class="{ active: filterType === t.value }" @click="setFilter(t.value)">
                <span class="dot" :style="{ background: t.color }"></span>
                <span>{{ t.label }}</span>
                <span class="cnt">{{ typeDistribution[t.value] || 0 }}</span>
              </div>
            </div>
          </div>

          <div class="distrib-card eco-card">
            <h4>资源列表</h4>
            <div class="resource-list">
              <div v-for="r in filteredList" :key="r.id" class="rl-item" @click="focusResource(r)">
                <span class="dot" :style="{ background: typeColor(r.resourceType) }"></span>
                <div class="rl-body">
                  <h5>{{ r.resourceName }}</h5>
                  <p>{{ r.region }} · {{ r.areaMu || '-' }}亩</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 中部地图 -->
        <div class="map-panel eco-card">
          <div class="map-toolbar">
            <h3><el-icon><LocationFilled /></el-icon> 森林生态资源可视化展示</h3>
            <div class="map-actions">
              <el-button-group>
                <el-button size="small" :type="mapStyle === 'amap://styles/normal' ? 'primary' : ''" @click="changeStyle('amap://styles/normal')">标准</el-button>
                <el-button size="small" :type="mapStyle === 'amap://styles/satellite' ? 'primary' : ''" @click="changeStyle('amap://styles/satellite')">卫星</el-button>
                <el-button size="small" :type="mapStyle === 'amap://styles/whitesmoke' ? 'primary' : ''" @click="changeStyle('amap://styles/whitesmoke')">简洁</el-button>
              </el-button-group>
              <el-button size="small" type="success" @click="$router.push('/resource/register')">
                <el-icon><EditPen /></el-icon>线上自主登记
              </el-button>
            </div>
          </div>
          <div id="amap-container" ref="mapEl"></div>
          <!-- 当前选中的资源弹窗 -->
          <div v-if="selectedResource" class="info-popup">
            <div class="ip-close" @click="selectedResource = null"><el-icon><Close /></el-icon></div>
            <img :src="selectedResource.cover" />
            <div class="ip-body">
              <el-tag size="small" effect="dark" round>{{ selectedResource.resourceType }}</el-tag>
              <h4>{{ selectedResource.resourceName }}</h4>
              <p class="ip-region"><el-icon><Location /></el-icon>{{ selectedResource.region }}</p>
              <div class="ip-stats">
                <span v-if="selectedResource.areaMu"><strong>{{ selectedResource.areaMu }}</strong> 亩</span>
                <span v-if="selectedResource.carbonStorage"><strong>{{ selectedResource.carbonStorage }}</strong> 吨碳汇</span>
              </div>
              <p class="ip-owner"><el-icon><User /></el-icon>{{ selectedResource.ownerName }}</p>
              <p class="ip-cert" v-if="selectedResource.certNo">{{ selectedResource.certNo }}</p>
              <p class="ip-intro">{{ selectedResource.intro }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import request from '../../api/request'
import PageHeader from '../../components/PageHeader.vue'

const list = ref([])
const selectedResource = ref(null)
const filterType = ref('')
const mapStyle = ref('amap://styles/normal')
const mapEl = ref(null)
let amap = null
let markers = []

const types = [
  { value: '', label: '全部', color: '#94a3b8' },
  { value: '林地', label: '林地', color: '#10b981' },
  { value: '碳汇', label: '碳汇', color: '#f59e0b' },
  { value: '水库', label: '水库', color: '#0ea5e9' },
  { value: '林下产品', label: '林下产品', color: '#8b5cf6' },
]

const typeColor = (t) => {
  const map = { 林地: '#10b981', 碳汇: '#f59e0b', 水库: '#0ea5e9', 林下产品: '#8b5cf6' }
  return map[t] || '#94a3b8'
}

const filteredList = computed(() => {
  if (!filterType.value) return list.value
  return list.value.filter((r) => r.resourceType === filterType.value)
})
const totalArea = computed(() => list.value.reduce((s, r) => s + (parseFloat(r.areaMu) || 0), 0))
const totalCarbon = computed(() => list.value.reduce((s, r) => s + (parseFloat(r.carbonStorage) || 0), 0))
const typeDistribution = computed(() => {
  const m = {}
  list.value.forEach((r) => { m[r.resourceType] = (m[r.resourceType] || 0) + 1 })
  return m
})

const setFilter = (t) => {
  filterType.value = t
  selectedResource.value = null
  renderMarkers()
}

const changeStyle = (style) => {
  mapStyle.value = style
  if (amap) amap.setMapStyle(style)
}

// 初始化地图
const initMap = () => {
  if (!window.AMap) { console.warn('AMap 未加载'); return }
  amap = new window.AMap.Map(mapEl.value, {
    zoom: 8,
    center: [110.6, 30.15], // 湖北五峰
    mapStyle: mapStyle.value,
    viewMode: '3D',
    pitch: 30,
  })
  // 控件
  amap.addControl(new window.AMap.Scale())
  amap.addControl(new window.AMap.ToolBar({ position: 'RB' }))
  renderMarkers()
}

// 自定义 Marker SVG
const buildMarkerIcon = (type) => {
  const color = typeColor(type)
  const svg = `<svg xmlns="http://www.w3.org/2000/svg" width="32" height="40" viewBox="0 0 32 40">
    <path d="M16 0C7.2 0 0 7.2 0 16c0 12 16 24 16 24s16-12 16-24C32 7.2 24.8 0 16 0z" fill="${color}"/>
    <circle cx="16" cy="16" r="6" fill="white"/>
  </svg>`
  return 'data:image/svg+xml;base64,' + window.btoa(unescape(encodeURIComponent(svg)))
}

const renderMarkers = () => {
  if (!amap) return
  // 清除旧标注
  markers.forEach((m) => amap.remove(m))
  markers = []
  // 添加新标注
  filteredList.value.forEach((r) => {
    if (!r.longitude || !r.latitude) return
    const marker = new window.AMap.Marker({
      position: [parseFloat(r.longitude), parseFloat(r.latitude)],
      icon: new window.AMap.Icon({
        size: new window.AMap.Size(32, 40),
        image: buildMarkerIcon(r.resourceType),
        imageSize: new window.AMap.Size(32, 40),
      }),
      offset: new window.AMap.Pixel(-16, -40),
      title: r.resourceName,
    })
    marker.on('click', () => {
      selectedResource.value = r
      amap.setCenter([parseFloat(r.longitude), parseFloat(r.latitude)])
    })
    amap.add(marker)
    markers.push(marker)
  })
  // 自适应
  if (markers.length > 0) {
    amap.setFitView(markers, false, [80, 80, 80, 80])
  }
}

const focusResource = (r) => {
  if (!amap || !r.longitude || !r.latitude) return
  selectedResource.value = r
  amap.setZoomAndCenter(13, [parseFloat(r.longitude), parseFloat(r.latitude)], false, 600)
}

onMounted(async () => {
  try {
    const res = await request.get('/resource/map')
    list.value = res.data || []
  } catch (e) {}
  // 等高德加载
  let tries = 0
  const wait = () => {
    if (window.AMap) initMap()
    else if (tries++ < 20) setTimeout(wait, 200)
  }
  wait()
})

onUnmounted(() => {
  if (amap) amap.destroy()
})
</script>

<style scoped>
.map-grid { display: grid; grid-template-columns: 320px 1fr; gap: 22px; }
.left-panel { display: flex; flex-direction: column; gap: 18px; }

.stat-card, .filter-card, .distrib-card { padding: 20px 22px; }
.stat-card h4, .filter-card h4, .distrib-card h4 {
  font-size: 14px; font-weight: 700; margin: 0 0 14px;
  color: var(--text-primary);
  display: flex; align-items: center; gap: 8px;
}
.stat-card h4::before, .filter-card h4::before, .distrib-card h4::before {
  content: ''; width: 3px; height: 14px; background: var(--primary); border-radius: 2px;
}

.big-stat { display: flex; gap: 12px; }
.bs-item { flex: 1; text-align: center; padding: 12px 0; background: var(--primary-bg); border-radius: 10px; }
.bs-item span { display: block; font-size: 22px; font-weight: 800; color: var(--primary-darker); }
.bs-item small { font-size: 11px; color: var(--text-tertiary); }

.type-filter { display: flex; flex-direction: column; gap: 6px; }
.type-btn {
  display: flex; align-items: center; gap: 8px;
  padding: 8px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 13px;
}
.type-btn:hover { background: var(--primary-bg); }
.type-btn.active { background: var(--primary-bg); color: var(--primary-darker); font-weight: 600; }
.type-btn .dot { width: 10px; height: 10px; border-radius: 50%; }
.type-btn .cnt {
  margin-left: auto;
  background: var(--bg-page);
  color: var(--text-tertiary);
  font-size: 11px;
  padding: 1px 8px;
  border-radius: 999px;
}
.type-btn.active .cnt { background: var(--primary); color: white; }

.resource-list {
  display: flex; flex-direction: column; gap: 8px;
  max-height: 320px;
  overflow-y: auto;
}
.rl-item {
  display: flex; align-items: center; gap: 10px;
  padding: 8px 10px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}
.rl-item:hover { background: var(--bg-page); }
.rl-item .dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
.rl-body { flex: 1; min-width: 0; }
.rl-body h5 {
  font-size: 13px; font-weight: 600; margin: 0 0 2px;
  color: var(--text-primary);
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
}
.rl-body p { font-size: 11px; color: var(--text-tertiary); margin: 0; }

.map-panel { padding: 0; overflow: hidden; min-height: 640px; display: flex; flex-direction: column; position: relative; }
.map-toolbar {
  display: flex; align-items: center; justify-content: space-between;
  padding: 14px 20px;
  background: linear-gradient(90deg, #ecfdf5, transparent);
  border-bottom: 1px solid var(--border-light);
}
.map-toolbar h3 {
  font-size: 16px; font-weight: 700; margin: 0;
  color: var(--primary-darker);
  display: flex; align-items: center; gap: 6px;
}
.map-actions { display: flex; gap: 12px; align-items: center; }

#amap-container {
  flex: 1;
  width: 100%;
  min-height: 600px;
}

.info-popup {
  position: absolute;
  right: 24px; top: 80px;
  width: 320px;
  z-index: 100;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.96);
  backdrop-filter: blur(14px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  box-shadow: var(--shadow-xl);
  overflow: hidden;
}
.ip-close {
  position: absolute; top: 8px; right: 8px;
  width: 28px; height: 28px;
  background: rgba(0, 0, 0, 0.4);
  color: white;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  cursor: pointer; z-index: 2;
}
.info-popup img { width: 100%; height: 140px; object-fit: cover; }
.ip-body { padding: 16px 18px; display: flex; flex-direction: column; gap: 8px; }
.ip-body h4 { font-size: 16px; font-weight: 700; margin: 4px 0 0; color: var(--text-primary); }
.ip-region { display: flex; align-items: center; gap: 4px; font-size: 13px; color: var(--text-tertiary); margin: 0; }
.ip-stats { display: flex; gap: 16px; font-size: 13px; padding-top: 8px; border-top: 1px dashed var(--border-light); }
.ip-stats strong { color: var(--primary-darker); }
.ip-owner, .ip-cert { font-size: 12px; color: var(--text-tertiary); margin: 0; display: flex; align-items: center; gap: 4px; }
.ip-intro { font-size: 12px; color: var(--text-tertiary); margin: 6px 0 0; line-height: 1.6; }

@media (max-width: 1100px) { .map-grid { grid-template-columns: 1fr; } #amap-container { min-height: 480px; } }
</style>

import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/login', component: () => import('../views/auth/Login.vue') },
  { path: '/register', component: () => import('../views/auth/Register.vue') },
  {
    path: '/home',
    component: () => import('../views/public/PublicLayout.vue'),
    children: [
      { path: '', component: () => import('../views/public/HomePage.vue') },
      // 新闻政策
      { path: '/news', component: () => import('../views/public/NewsList.vue') },
      { path: '/news/:id', component: () => import('../views/public/NewsDetail.vue') },
      { path: '/policy', component: () => import('../views/public/PolicyList.vue') },
      { path: '/policy/:id', component: () => import('../views/public/PolicyDetail.vue') },
      // 信息披露
      { path: '/disclosure', component: () => import('../views/public/DisclosureList.vue') },
      { path: '/disclosure/:id', component: () => import('../views/public/DisclosureDetail.vue') },
      { path: '/disclosure-publish', component: () => import('../views/public/DisclosurePublish.vue') },
      // 反馈
      { path: '/feedback', component: () => import('../views/public/FeedbackForm.vue') },
      // 收储登记
      { path: '/resource', component: () => import('../views/resource/ResourceCenter.vue') },
      { path: '/resource/map', component: () => import('../views/resource/ResourceMap.vue') },
      { path: '/resource/register', component: () => import('../views/resource/ResourceRegister.vue') },
      { path: '/resource/my', component: () => import('../views/resource/MyResources.vue') },
      // 资产评估
      { path: '/evaluation', component: () => import('../views/evaluation/EvaluationCenter.vue') },
      { path: '/evaluation/orgs', component: () => import('../views/evaluation/OrgList.vue') },
      { path: '/evaluation/evaluators', component: () => import('../views/evaluation/EvaluatorList.vue') },
      { path: '/evaluation/apply', component: () => import('../views/evaluation/ApplyForm.vue') },
      { path: '/evaluation/my', component: () => import('../views/evaluation/MyApplications.vue') },
      // 供需交易
      { path: '/trading', component: () => import('../views/trading/TradingCenter.vue') },
      { path: '/trading/listings', component: () => import('../views/trading/ListingMarket.vue') },
      { path: '/trading/listing/:id', component: () => import('../views/trading/ListingDetail.vue') },
      { path: '/trading/demands', component: () => import('../views/trading/DemandMarket.vue') },
      { path: '/trading/demand/:id', component: () => import('../views/trading/DemandDetail.vue') },
      { path: '/trading/sell', component: () => import('../views/trading/SellForm.vue') },
      { path: '/trading/buy', component: () => import('../views/trading/BuyForm.vue') },
      { path: '/trading/farm', component: () => import('../views/trading/FarmMarket.vue') },
      { path: '/trading/farm/:id', component: () => import('../views/trading/FarmDetail.vue') },
      { path: '/trading/my-listings', component: () => import('../views/trading/MyListings.vue') },
      { path: '/trading/my-demands', component: () => import('../views/trading/MyDemands.vue') },
      { path: '/trading/my-appointments', component: () => import('../views/trading/MyAppointments.vue') },
      // 金融服务
      { path: '/finance', component: () => import('../views/finance/FinanceCenter.vue') },
      { path: '/finance/products', component: () => import('../views/finance/ProductList.vue') },
      { path: '/finance/product/:id', component: () => import('../views/finance/ProductDetail.vue') },
      { path: '/finance/match', component: () => import('../views/finance/ProductMatch.vue') },
      { path: '/finance/apply', component: () => import('../views/finance/LoanApply.vue') },
      { path: '/finance/my', component: () => import('../views/finance/MyLoans.vue') },
      { path: '/finance/review', component: () => import('../views/finance/BankReview.vue') },
      // 法律服务
      { path: '/legal', component: () => import('../views/legal/LegalCenter.vue') },
      { path: '/legal/orgs', component: () => import('../views/legal/OrgList.vue') },
      { path: '/legal/lawyers', component: () => import('../views/legal/LawyerList.vue') },
      { path: '/legal/consult', component: () => import('../views/legal/ConsultForm.vue') },
      { path: '/legal/exclusive', component: () => import('../views/legal/ExclusiveForm.vue') },
      { path: '/legal/my', component: () => import('../views/legal/MyConsults.vue') },
      // 个人中心
      { path: '/profile', component: () => import('../views/public/Profile.vue') },
    ],
  },
  // 后台管理
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { role: '管理员' },
    children: [
      { path: '', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'users', component: () => import('../views/admin/UserManage.vue') },
      { path: 'news', component: () => import('../views/admin/NewsManage.vue') },
      { path: 'policy', component: () => import('../views/admin/PolicyManage.vue') },
      { path: 'feedback', component: () => import('../views/admin/FeedbackManage.vue') },
      { path: 'disclosure', component: () => import('../views/admin/DisclosureManage.vue') },
      { path: 'resource', component: () => import('../views/admin/ResourceManage.vue') },
      { path: 'evaluation-org', component: () => import('../views/admin/EvaluationOrgManage.vue') },
      { path: 'evaluator', component: () => import('../views/admin/EvaluatorManage.vue') },
      { path: 'evaluation-apply', component: () => import('../views/admin/EvaluationApplyManage.vue') },
      { path: 'listing', component: () => import('../views/admin/ListingManage.vue') },
      { path: 'demand', component: () => import('../views/admin/DemandManage.vue') },
      { path: 'farm-product', component: () => import('../views/admin/FarmProductManage.vue') },
      { path: 'finance-product', component: () => import('../views/admin/FinanceProductManage.vue') },
      { path: 'loan-apply', component: () => import('../views/admin/LoanApplyManage.vue') },
      { path: 'legal-org', component: () => import('../views/admin/LegalOrgManage.vue') },
      { path: 'lawyer', component: () => import('../views/admin/LawyerManage.vue') },
      { path: 'legal-consult', component: () => import('../views/admin/LegalConsultManage.vue') },
    ],
  },
  { path: '/:pathMatch(.*)*', redirect: '/home' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role') || ''

  // 完全公开访问的路径
  const publicPaths = [
    '/login', '/register',
    '/home', '/news', '/policy',
    '/resource', '/disclosure',
    '/evaluation', '/trading', '/finance', '/legal',
    '/feedback',
  ]
  const isPublicPath = publicPaths.some((p) => to.path === p || to.path.startsWith(p + '/'))

  if (!token && !isPublicPath && to.path !== '/login') {
    next('/login')
    return
  }
  if (to.meta?.role && to.meta.role !== role) {
    next('/home')
    return
  }
  next()
})

export default router

<template>
  <EasyCrud
    api="/user"
    page-api="page"
    :search-fields="searchFields"
    :columns="columns"
    :initial-data="{ status: 1, role: '林农' }"
    :form-rules="rules"
    dialog-title="用户信息"
  >
    <template #status="{ row }">
      <el-tag :type="row.status === 1 ? 'success' : 'danger'" effect="plain" size="small">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
    </template>
    <template #avatar="{ row }">
      <el-avatar :size="32" :src="row.avatar" />
    </template>
    <template #form="{ form }">
      <el-row :gutter="14">
        <el-col :span="12"><el-form-item label="账号" prop="username"><el-input v-model="form.username" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="密码"><el-input v-model="form.password" type="password" show-password placeholder="留空则不修改" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="姓名/机构名"><el-input v-model="form.realName" /></el-form-item></el-col>
        <el-col :span="12">
          <el-form-item label="角色" prop="role">
            <el-select v-model="form.role" style="width: 100%;">
              <el-option v-for="r in roles" :key="r" :label="r" :value="r" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12"><el-form-item label="手机"><el-input v-model="form.phone" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="所在区域"><el-input v-model="form.region" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="详细地址"><el-input v-model="form.address" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio :value="1">启用</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group></el-form-item></el-col>
      </el-row>
    </template>
  </EasyCrud>
</template>
<script setup>
import EasyCrud from '../../components/EasyCrud.vue'
const roles = ['管理员', '林农', '合作社', '企业', '林业管理部门', '金融机构', '评估师', '评估机构', '律师', '法律援助机构']
const searchFields = [
  { prop: 'keyword', label: '搜索账号/姓名/手机' },
  { prop: 'role', label: '角色', type: 'select', options: roles },
]
const columns = [
  { prop: 'avatar', label: '头像', width: 70, slot: 'avatar' },
  { prop: 'username', label: '账号', width: 120 },
  { prop: 'realName', label: '姓名/机构', minWidth: 150 },
  { prop: 'role', label: '角色', width: 110 },
  { prop: 'phone', label: '手机', width: 130 },
  { prop: 'region', label: '区域', minWidth: 160 },
  { prop: 'status', label: '状态', width: 80, slot: 'status' },
  { prop: 'createdTime', label: '注册时间', width: 170 },
]
const rules = {
  username: [{ required: true, message: '请输入账号' }],
  role: [{ required: true, message: '请选择角色' }],
}
</script>

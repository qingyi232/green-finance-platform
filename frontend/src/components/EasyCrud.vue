<template>
  <div class="easy-crud">
    <!-- 顶部操作栏 -->
    <div class="ec-toolbar eco-card" v-if="searchFields.length || showAdd">
      <div class="ec-search" v-if="searchFields.length">
        <template v-for="f in searchFields" :key="f.prop">
          <el-input
            v-if="f.type !== 'select'"
            v-model="searchForm[f.prop]"
            :placeholder="f.label"
            clearable
            style="width: 180px;"
            @keyup.enter="search"
          >
            <template #prefix><el-icon><Search /></el-icon></template>
          </el-input>
          <el-select v-else v-model="searchForm[f.prop]" :placeholder="f.label" clearable style="width: 160px;">
            <el-option v-for="o in f.options" :key="o.value || o" :label="o.label || o" :value="o.value || o" />
          </el-select>
        </template>
        <el-button type="primary" @click="search"><el-icon><Search /></el-icon>查询</el-button>
        <el-button @click="reset">重置</el-button>
      </div>
      <div class="ec-actions">
        <slot name="extra-actions" />
        <el-button v-if="showAdd" type="success" @click="onAdd"><el-icon><Plus /></el-icon>{{ addText || '新增' }}</el-button>
      </div>
    </div>

    <!-- 表格 -->
    <div class="ec-table eco-card">
      <el-table :data="list" v-loading="loading" stripe row-key="id" :height="height">
        <el-table-column type="index" label="#" width="60" align="center" />
        <el-table-column v-for="c in columns" :key="c.prop" v-bind="c">
          <template v-if="c.slot" #default="scope"><slot :name="c.slot" v-bind="scope" /></template>
        </el-table-column>
        <el-table-column v-if="showOps" label="操作" :width="opsWidth" fixed="right">
          <template #default="{ row }">
            <slot name="ops" :row="row">
              <el-button v-if="showEdit" type="primary" size="small" link @click="onEdit(row)"><el-icon><Edit /></el-icon>编辑</el-button>
              <el-button v-if="showDelete" type="danger" size="small" link @click="onDelete(row)"><el-icon><Delete /></el-icon>删除</el-button>
            </slot>
          </template>
        </el-table-column>
      </el-table>
      <div class="ec-pagination" v-if="total > 0">
        <el-pagination
          background
          :current-page="page"
          :page-size="limit"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="onPageChange"
          @size-change="onSizeChange"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" :width="dialogWidth" destroy-on-close>
      <el-form :model="form" :rules="formRules" ref="formRef" :label-width="labelWidth">
        <slot name="form" :form="form" />
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submit">{{ submitText || '保存' }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api/request'

const props = defineProps({
  api: String,
  pageApi: { type: String, default: 'list' },
  searchFields: { type: Array, default: () => [] },
  columns: { type: Array, default: () => [] },
  showAdd: { type: Boolean, default: true },
  addText: String,
  showEdit: { type: Boolean, default: true },
  showDelete: { type: Boolean, default: true },
  showOps: { type: Boolean, default: true },
  opsWidth: { type: [String, Number], default: 180 },
  initialData: { type: Object, default: () => ({}) },
  formRules: { type: Object, default: () => ({}) },
  dialogTitle: String,
  dialogWidth: { type: String, default: '600px' },
  labelWidth: { type: String, default: '110px' },
  submitText: String,
  height: [String, Number],
  saveApi: String,
  updateApi: String,
  deleteApi: String,
  initParams: { type: Object, default: () => ({}) },
})

const emit = defineEmits(['edit', 'add', 'success'])

const list = ref([])
const total = ref(0)
const page = ref(1)
const limit = ref(10)
const loading = ref(false)
const searchForm = reactive({ ...props.initParams })

const dialogVisible = ref(false)
const dialogTitle = ref('新增')
const form = reactive({})
const formRef = ref()
const submitting = ref(false)
const editing = ref(false)

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get(`${props.api}/${props.pageApi}`, {
      params: { page: page.value, limit: limit.value, ...searchForm },
    })
    list.value = res.data?.list || []
    total.value = res.data?.totalCount || 0
  } catch (e) {} finally {
    loading.value = false
  }
}

const search = () => { page.value = 1; loadData() }
const reset = () => {
  Object.keys(searchForm).forEach((k) => searchForm[k] = '')
  page.value = 1
  loadData()
}
const onPageChange = (p) => { page.value = p; loadData() }
const onSizeChange = (s) => { limit.value = s; page.value = 1; loadData() }

const onAdd = () => {
  Object.keys(form).forEach((k) => delete form[k])
  Object.assign(form, JSON.parse(JSON.stringify(props.initialData)))
  editing.value = false
  dialogTitle.value = props.dialogTitle || '新增'
  dialogVisible.value = true
  emit('add', form)
}

const onEdit = (row) => {
  Object.keys(form).forEach((k) => delete form[k])
  Object.assign(form, JSON.parse(JSON.stringify(row)))
  editing.value = true
  dialogTitle.value = '编辑'
  dialogVisible.value = true
  emit('edit', form)
}

const submit = async () => {
  if (formRef.value) await formRef.value.validate()
  submitting.value = true
  try {
    if (editing.value) {
      await request.put(props.updateApi || `${props.api}/update`, form)
    } else {
      await request.post(props.saveApi || `${props.api}/save`, form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
    emit('success')
  } catch (e) {} finally {
    submitting.value = false
  }
}

const onDelete = async (row) => {
  await ElMessageBox.confirm('确定删除这条数据？', '提示', { type: 'warning' })
  try {
    await request.delete(props.deleteApi || `${props.api}/delete`, { data: [row.id] })
    ElMessage.success('已删除')
    loadData()
  } catch (e) {}
}

defineExpose({ loadData, onAdd, onEdit, list, form, dialogVisible })

onMounted(loadData)
</script>

<style scoped>
.easy-crud { display: flex; flex-direction: column; gap: 16px; }
.ec-toolbar {
  display: flex; align-items: center; justify-content: space-between;
  padding: 16px 20px;
  flex-wrap: wrap;
  gap: 12px;
}
.ec-search { display: flex; gap: 12px; flex-wrap: wrap; align-items: center; }
.ec-actions { display: flex; gap: 10px; }
.ec-table { padding: 16px 20px; overflow: hidden; }
.ec-pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>

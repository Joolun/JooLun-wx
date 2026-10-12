<template>
  <div class="app-container">
    <el-form :inline="true" :model="query">
      <el-form-item label="名称"><el-input v-model="query.name" clearable /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable style="width: 120px">
          <el-option label="启用" value="1" /><el-option label="停用" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
      <el-form-item><el-button v-hasPermi="['mall:coupon:add']" type="success" @click="open()">新增满减券</el-button></el-form-item>
    </el-form>

    <el-alert title="第一版仅支持全场固定金额满减；发行量填 0 表示不限量，每位会员限领一张。" type="info" :closable="false" show-icon />
    <el-table v-loading="loading" :data="rows" border style="margin-top: 16px">
      <el-table-column prop="name" label="名称" min-width="160" />
      <el-table-column label="优惠" min-width="160"><template #default="s">满 {{ money(s.row.thresholdAmount) }} 减 {{ money(s.row.discountAmount) }}</template></el-table-column>
      <el-table-column label="领取/使用/发行" width="160"><template #default="s">{{ s.row.receivedQuantity }}/{{ s.row.usedQuantity }}/{{ s.row.totalQuantity || '不限' }}</template></el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="180" />
      <el-table-column prop="endTime" label="结束时间" width="180" />
      <el-table-column label="状态" width="90"><template #default="s"><el-tag :type="s.row.status === '1' ? 'success' : 'info'">{{ s.row.status === '1' ? '启用' : '停用' }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="100"><template #default="s"><el-button v-hasPermi="['mall:coupon:edit']" link type="primary" @click="open(s.row)">编辑</el-button></template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page.current" v-model:page-size="page.size" :total="page.total" layout="total, prev, pager, next" style="margin-top: 16px" @current-change="load" />

    <el-dialog v-model="visible" :title="form.id ? '编辑优惠券' : '新增优惠券'" width="560px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="名称"><el-input v-model="form.name" maxlength="100" /></el-form-item>
        <el-form-item label="使用门槛"><el-input-number v-model="form.thresholdAmount" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="减免金额"><el-input-number v-model="form.discountAmount" :min="0.01" :precision="2" /></el-form-item>
        <el-form-item label="发行量"><el-input-number v-model="form.totalQuantity" :min="0" :precision="0" /></el-form-item>
        <el-form-item label="有效期"><el-date-picker v-model="dateRange" type="datetimerange" value-format="YYYY-MM-DD HH:mm:ss" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio value="1">启用</el-radio><el-radio value="0">停用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="说明"><el-input v-model="form.description" type="textarea" maxlength="500" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible = false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup name="MallCoupon">
import { addCoupon, getCouponPage, updateCoupon } from '@/api/mall/coupon'

const { proxy } = getCurrentInstance()
const loading = ref(false)
const visible = ref(false)
const rows = ref([])
const query = reactive({ name: '', status: '' })
const page = reactive({ current: 1, size: 10, total: 0 })
const form = reactive({})
const dateRange = ref([])

function money(value) { return `¥${Number(value || 0).toFixed(2)}` }
function load() {
  loading.value = true
  getCouponPage({ ...query, current: page.current, size: page.size }).then(({ data }) => {
    rows.value = data.records || []
    page.total = data.total || 0
  }).finally(() => { loading.value = false })
}
function open(row) {
  Object.keys(form).forEach(key => delete form[key])
  Object.assign(form, row ? { ...row } : { thresholdAmount: 0, discountAmount: 1, totalQuantity: 0, status: '1' })
  dateRange.value = row ? [row.startTime, row.endTime] : []
  visible.value = true
}
function save() {
  if (!form.name || dateRange.value.length !== 2) return proxy.$message.warning('请填写名称和有效期')
  form.startTime = dateRange.value[0]
  form.endTime = dateRange.value[1]
  const request = form.id ? updateCoupon(form) : addCoupon(form)
  request.then(() => { proxy.$message.success('保存成功'); visible.value = false; load() })
}
onMounted(load)
</script>

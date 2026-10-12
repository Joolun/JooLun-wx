<template>
  <div class="app-container">
    <el-alert title="支付成功按实付整数金额累计成长值与积分，退款时扣回；积分第一版只展示，不参与支付抵扣。" type="info" :closable="false" show-icon />
    <div style="margin: 16px 0"><el-button v-hasPermi="['mall:memberlevel:add']" type="primary" @click="open()">新增等级</el-button></div>
    <el-table v-loading="loading" :data="rows" border>
      <el-table-column prop="levelValue" label="等级" width="90" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="minGrowth" label="最低成长值" />
      <el-table-column label="会员折扣"><template #default="s">{{ s.row.discountRate }}%</template></el-table-column>
      <el-table-column label="状态"><template #default="s"><el-tag :type="s.row.status === '1' ? 'success' : 'info'">{{ s.row.status === '1' ? '启用' : '停用' }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="100"><template #default="s"><el-button v-hasPermi="['mall:memberlevel:edit']" link type="primary" @click="open(s.row)">编辑</el-button></template></el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="form.id ? '编辑会员等级' : '新增会员等级'" width="520px">
      <el-form :model="form" label-width="110px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="等级序号"><el-input-number v-model="form.levelValue" :min="1" /></el-form-item>
        <el-form-item label="最低成长值"><el-input-number v-model="form.minGrowth" :min="0" /></el-form-item>
        <el-form-item label="折扣百分比"><el-input-number v-model="form.discountRate" :min="1" :max="100" :precision="2" /><span style="margin-left: 8px">95 表示九五折</span></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio value="1">启用</el-radio><el-radio value="0">停用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible = false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup name="MallMemberLevel">
import { addMemberLevel, getMemberLevels, updateMemberLevel } from '@/api/mall/memberlevel'
const { proxy } = getCurrentInstance()
const loading = ref(false)
const visible = ref(false)
const rows = ref([])
const form = reactive({})
function load() { loading.value = true; getMemberLevels().then(({ data }) => { rows.value = data || [] }).finally(() => { loading.value = false }) }
function open(row) { Object.keys(form).forEach(key => delete form[key]); Object.assign(form, row ? { ...row } : { levelValue: 1, minGrowth: 0, discountRate: 100, status: '1' }); visible.value = true }
function save() {
  if (!form.name) return proxy.$message.warning('请填写等级名称')
  const request = form.id ? updateMemberLevel(form) : addMemberLevel(form)
  request.then(() => { proxy.$message.success('保存成功'); visible.value = false; load() })
}
onMounted(load)
</script>

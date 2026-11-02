<template>
  <div class="app-container">
    <el-alert
      title="运费模板按商品绑定。同一模板下的商品合并计算首件/续件，不同模板各自收取首运费；模板被商品引用时无法删除。"
      type="info"
      :closable="false"
      show-icon
    />
    <div class="freight-templat__toolbar">
      <el-input v-model="query.name" placeholder="模板名称" clearable class="freight-templat__search" @keyup.enter="reload" />
      <el-button type="primary" plain @click="reload">查询</el-button>
      <el-button v-hasPermi="['mall:freighttemplat:add']" type="primary" @click="open()">新增模板</el-button>
    </div>
    <el-table v-loading="loading" :data="rows" border>
      <el-table-column prop="name" label="模板名称" min-width="160" />
      <el-table-column label="模板类型" width="120">
        <template #default="scope">
          <el-tag :type="scope.row.type === '2' ? 'success' : 'warning'">
            {{ scope.row.type === '2' ? '卖家包邮' : '买家承担运费' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="计费规则" min-width="300">
        <template #default="scope">{{ ruleSummary(scope.row) }}</template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column label="状态" width="90">
        <template #default="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'info'">
            {{ scope.row.status === '1' ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="scope">
          <el-button v-hasPermi="['mall:freighttemplat:edit']" link type="primary" @click="open(scope.row)">编辑</el-button>
          <el-button v-hasPermi="['mall:freighttemplat:del']" link type="danger" @click="remove(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="freight-templat__pagination"
      layout="total, prev, pager, next, sizes"
      :total="total"
      :current-page="query.current"
      :page-size="query.size"
      :page-sizes="[10, 20, 50]"
      @current-change="changePage"
      @size-change="changeSize"
    />

    <el-dialog v-model="visible" :title="form.id ? '编辑运费模板' : '新增运费模板'" width="560px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="模板名称">
          <el-input v-model="form.name" maxlength="100" placeholder="例如：全国按件收费" />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-radio-group v-model="form.type">
            <el-radio value="1">买家承担运费</el-radio>
            <el-radio value="2">卖家包邮</el-radio>
          </el-radio-group>
        </el-form-item>
        <template v-if="form.type === '1'">
          <el-form-item label="计费方式">
            <el-radio-group v-model="form.chargeType">
              <el-radio value="1">按件数</el-radio>
              <el-radio value="2">按重量</el-radio>
              <el-radio value="3">按体积</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item :label="firstLabel">
            <el-input-number v-model="form.firstNum" :min="0.01" :precision="2" :step="1" />
            <span class="freight-templat__unit">{{ unitLabel }}</span>
          </el-form-item>
          <el-form-item label="首运费(元)">
            <el-input-number v-model="form.firstFreight" :min="0" :precision="2" />
          </el-form-item>
          <el-form-item :label="continueLabel">
            <el-input-number v-model="form.continueNum" :min="0.01" :precision="2" :step="1" />
            <span class="freight-templat__unit">{{ unitLabel }}</span>
          </el-form-item>
          <el-form-item label="续运费(元)">
            <el-input-number v-model="form.continueFreight" :min="0" :precision="2" />
            <div class="freight-templat__tip">{{ ruleExample }}</div>
          </el-form-item>
        </template>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :precision="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio value="1">启用</el-radio>
            <el-radio value="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" maxlength="255" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="MallFreightTemplat">
import { addFreightTemplat, delFreightTemplat, getFreightTemplatPage, updateFreightTemplat } from '@/api/mall/freighttemplat'

const { proxy } = getCurrentInstance()
const loading = ref(false)
const saving = ref(false)
const visible = ref(false)
const rows = ref([])
const total = ref(0)
const query = reactive({ current: 1, size: 10, name: '' })
const form = reactive({})

// 计费方式决定“首件/首重量/首体积”等文案与单位。
const chargeTypeMeta = computed(() => {
  if (form.chargeType === '2') {
    return { first: '首重量', continue: '续重量', unit: 'kg' }
  }
  if (form.chargeType === '3') {
    return { first: '首体积', continue: '续体积', unit: 'm³' }
  }
  return { first: '首件', continue: '续件', unit: '件' }
})
const firstLabel = computed(() => chargeTypeMeta.value.first)
const continueLabel = computed(() => chargeTypeMeta.value.continue)
const unitLabel = computed(() => chargeTypeMeta.value.unit)
const ruleExample = computed(() => {
  const first = Number(form.firstNum || 0)
  const firstFreight = Number(form.firstFreight || 0)
  const continueNum = Number(form.continueNum || 0)
  const continueFreight = Number(form.continueFreight || 0)
  if (first <= 0 || continueNum <= 0) {
    return ''
  }
  const example = first + continueNum
  const amount = firstFreight + continueFreight
  return `示例：${first}${unitLabel.value}以内 ${firstFreight} 元，${example}${unitLabel.value}为 ${amount.toFixed(2)} 元`
})

function ruleSummary(row) {
  if (row.type === '2') {
    return '全国包邮，不收取运费'
  }
  const typeLabel = row.chargeType === '2' ? '按重量' : row.chargeType === '3' ? '按体积' : '按件数'
  const unit = row.chargeType === '2' ? 'kg' : row.chargeType === '3' ? 'm³' : '件'
  return `${typeLabel}：首 ${Number(row.firstNum || 0)}${unit} ${Number(row.firstFreight || 0).toFixed(2)} 元，`
    + `每续 ${Number(row.continueNum || 0)}${unit} ${Number(row.continueFreight || 0).toFixed(2)} 元`
}

function load() {
  loading.value = true
  return getFreightTemplatPage({ current: query.current, size: query.size, name: query.name })
    .then(({ data }) => {
      rows.value = data.records || []
      total.value = data.total || 0
    })
    .finally(() => {
      loading.value = false
    })
}

function reload() {
  query.current = 1
  load()
}

function changePage(current) {
  query.current = current
  load()
}

function changeSize(size) {
  query.size = size
  query.current = 1
  load()
}

function open(row) {
  Object.keys(form).forEach((key) => delete form[key])
  Object.assign(form, row
    ? { ...row }
    : { type: '1', chargeType: '1', firstNum: 1, firstFreight: 0, continueNum: 1, continueFreight: 0, sort: 0, status: '1' })
  visible.value = true
}

function save() {
  if (!form.name) {
    proxy.$message.warning('请填写运费模板名称')
    return
  }
  if (form.type === '1') {
    if (!form.firstNum || !form.continueNum) {
      proxy.$message.warning('首件/首重量/首体积和续件/续重量/续体积都必须大于 0')
      return
    }
  }
  saving.value = true
  const request = form.id ? updateFreightTemplat(form) : addFreightTemplat(form)
  request
    .then(() => {
      proxy.$message.success('保存成功')
      visible.value = false
      load()
    })
    .catch(() => {})
    .finally(() => {
      saving.value = false
    })
}

function remove(row) {
  proxy.$confirm(`是否确认删除运费模板「${row.name}」？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => delFreightTemplat(row.id))
    .then(() => {
      proxy.$message.success('删除成功')
      load()
    })
    .catch(() => {})
}

onMounted(load)
</script>

<style scoped>
.freight-templat__toolbar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 16px 0;
}
.freight-templat__search {
  width: 220px;
}
.freight-templat__pagination {
  margin-top: 16px;
  justify-content: flex-end;
}
.freight-templat__unit {
  margin-left: 8px;
  color: #909399;
}
.freight-templat__tip {
  color: #909399;
  font-size: 12px;
  line-height: 20px;
}
</style>

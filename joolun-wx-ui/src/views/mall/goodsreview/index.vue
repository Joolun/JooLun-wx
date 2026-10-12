<template>
  <div class="app-container">
    <el-form :inline="true" :model="query">
      <el-form-item label="商品"><el-input v-model="query.spuName" clearable /></el-form-item>
      <el-form-item label="评分"><el-select v-model="query.rating" clearable style="width: 100px"><el-option v-for="n in 5" :key="n" :label="`${n} 星`" :value="n" /></el-select></el-form-item>
      <el-form-item label="状态"><el-select v-model="query.status" clearable style="width: 110px"><el-option label="显示" value="1" /><el-option label="隐藏" value="0" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="rows" border>
      <el-table-column prop="spuName" label="商品" min-width="180" />
      <el-table-column label="会员" width="140"><template #default="s">{{ s.row.anonymousFlag === '1' ? '匿名用户' : s.row.nickName }}</template></el-table-column>
      <el-table-column label="评分" width="150"><template #default="s"><el-rate :model-value="s.row.rating" disabled /></template></el-table-column>
      <el-table-column prop="content" label="评价内容" min-width="220" show-overflow-tooltip />
      <el-table-column prop="replyContent" label="商家回复" min-width="220" show-overflow-tooltip />
      <el-table-column prop="createTime" label="评价时间" width="180" />
      <el-table-column label="状态" width="80"><template #default="s"><el-tag :type="s.row.status === '1' ? 'success' : 'info'">{{ s.row.status === '1' ? '显示' : '隐藏' }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="100"><template #default="s"><el-button v-hasPermi="['mall:goodsreview:edit']" link type="primary" @click="open(s.row)">回复</el-button></template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page.current" v-model:page-size="page.size" :total="page.total" layout="total, prev, pager, next" style="margin-top: 16px" @current-change="load" />
    <el-dialog v-model="visible" title="评价处理" width="560px">
      <el-form label-width="90px"><el-form-item label="用户评价"><div>{{ current.content || '用户未填写文字' }}</div></el-form-item><el-form-item label="显示状态"><el-radio-group v-model="current.status"><el-radio value="1">显示</el-radio><el-radio value="0">隐藏</el-radio></el-radio-group></el-form-item><el-form-item label="商家回复"><el-input v-model="current.replyContent" type="textarea" maxlength="500" show-word-limit /></el-form-item></el-form>
      <template #footer><el-button @click="visible = false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup name="MallGoodsReview">
import { getReviewPage, replyReview } from '@/api/mall/goodsreview'
const { proxy } = getCurrentInstance()
const loading = ref(false)
const visible = ref(false)
const rows = ref([])
const current = reactive({})
const query = reactive({ spuName: '', rating: '', status: '' })
const page = reactive({ current: 1, size: 10, total: 0 })
function load() { loading.value = true; getReviewPage({ ...query, current: page.current, size: page.size }).then(({ data }) => { rows.value = data.records || []; page.total = data.total || 0 }).finally(() => { loading.value = false }) }
function open(row) { Object.assign(current, { ...row }); visible.value = true }
function save() { replyReview(current.id, { replyContent: current.replyContent, status: current.status }).then(() => { proxy.$message.success('保存成功'); visible.value = false; load() }) }
onMounted(load)
</script>

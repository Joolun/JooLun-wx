<!--
  Copyright (C) 2026
  All rights reserved, Designed By www.joolun.com
-->
<template>
  <div class="app-container shipping-center">
    <el-alert
      title="这里直接读取商城订单和物流信息。订单管理完成发货后会自动同步微信，失败订单可在此重试。"
      type="success"
      show-icon
      :closable="false"
    />

    <el-row :gutter="14" class="summary-row">
      <el-col v-for="item in summaryCards" :key="item.key" :xs="12" :sm="8" :lg="4">
        <el-card shadow="hover" class="summary-card" :class="`summary-${item.type}`">
          <div class="summary-label">{{ item.label }}</div>
          <div class="summary-value">{{ summary[item.key] || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="capability-card">
      <div class="capability-bar">
        <div class="capability-item">
          <span class="capability-label">微信发货信息管理</span>
          <el-tag :type="shippingEnabled === true ? 'success' : shippingEnabled === false ? 'danger' : 'info'">
            {{ shippingEnabled === true ? "已开通" : shippingEnabled === false ? "未开通" : "待查询" }}
          </el-tag>
          <el-button link type="primary" :loading="capabilityLoading" v-hasPermi="['wxma:operations:query']" @click="checkShippingEnabled">
            刷新状态
          </el-button>
        </div>
        <div class="jump-path">
          <span class="capability-label">发货消息跳转页面</span>
          <el-input v-model="jumpPath" placeholder="pages/order/order-detail/index" />
          <el-button type="primary" :loading="savingPath" v-hasPermi="['wxma:operations:edit']" @click="saveJumpPath">
            保存
          </el-button>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" class="filter-card">
      <el-form :model="query" inline label-width="80px">
        <el-form-item label="综合搜索">
          <el-input
            v-model="query.shippingKeyword"
            clearable
            placeholder="订单号、支付单号、会员、运单号"
            style="width: 290px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="query.status" clearable placeholder="全部" style="width: 140px">
            <el-option label="待发货" value="1" />
            <el-option label="待收货" value="2" />
            <el-option label="已完成" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="同步状态">
          <el-select v-model="query.wxShippingStatus" clearable placeholder="全部" style="width: 140px">
            <el-option label="未同步" value="0" />
            <el-option label="同步成功" value="1" />
            <el-option label="同步失败" value="2" />
            <el-option label="已跳过" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付时间">
          <el-date-picker
            v-model="payTimeRange"
            type="datetimerange"
            value-format="YYYY-MM-DD HH:mm:ss"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            style="width: 350px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="table-header">
          <div>
            <span class="table-title">商城订单微信发货同步</span>
            <span class="selection-tip">已选择 {{ selectedRows.length }} 个订单</span>
          </div>
          <div>
            <el-button
              type="primary"
              :disabled="batchReadyRows.length === 0"
              :loading="batchLoading"
              v-hasPermi="['wxma:operations:edit']"
              @click="batchSync"
            >
              批量同步微信
            </el-button>
            <el-button :loading="loading" @click="loadData">刷新</el-button>
          </div>
        </div>
      </template>

      <el-table v-loading="loading" :data="orderRows" border row-key="id" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="48" :selectable="canSelect" />
        <el-table-column label="订单信息" min-width="230">
          <template #default="scope">
            <div class="primary-text">{{ scope.row.orderNo }}</div>
            <div class="secondary-text">支付单号：{{ scope.row.transactionId || "未记录" }}</div>
            <div class="secondary-text">支付时间：{{ scope.row.paymentTime || "-" }}</div>
          </template>
        </el-table-column>
        <el-table-column label="会员" min-width="160">
          <template #default="scope">
            <div class="primary-text">{{ scope.row.userNickName || scope.row.userRealName || "商城用户" }}</div>
            <div class="secondary-text">{{ scope.row.userNo || "无会员编号" }}</div>
            <div class="secondary-text">{{ scope.row.userMobile || "未绑定手机号" }}</div>
          </template>
        </el-table-column>
        <el-table-column label="金额" width="110" align="right">
          <template #default="scope"><span class="amount-text">￥{{ formatAmount(scope.row.paymentPrice) }}</span></template>
        </el-table-column>
        <el-table-column label="本地订单" width="145" align="center">
          <template #default="scope">
            <el-tag :type="localStatusMeta(scope.row.status).type">{{ localStatusMeta(scope.row.status).label }}</el-tag>
            <div class="secondary-text status-time">{{ scope.row.deliveryTime || "尚未发货" }}</div>
          </template>
        </el-table-column>
        <el-table-column label="物流信息" min-width="175">
          <template #default="scope">
            <template v-if="scope.row.orderLogistics && scope.row.orderLogistics.logisticsNo">
              <div class="primary-text">{{ expressName(scope.row.orderLogistics.logistics) }}</div>
              <div class="secondary-text copy-text">{{ scope.row.orderLogistics.logisticsNo }}</div>
            </template>
            <span v-else class="empty-text">等待订单发货</span>
          </template>
        </el-table-column>
        <el-table-column label="微信同步" min-width="210">
          <template #default="scope">
            <div class="sync-status-line">
              <el-tag :type="syncStatusMeta(scope.row.wxShippingStatus).type">{{ syncStatusMeta(scope.row.wxShippingStatus).label }}</el-tag>
              <span class="retry-count">尝试 {{ scope.row.wxShippingRetryCount || 0 }} 次</span>
            </div>
            <div class="secondary-text">{{ scope.row.wxShippingSyncTime || "尚未执行同步" }}</div>
            <el-tooltip v-if="scope.row.wxShippingError" :content="scope.row.wxShippingError" placement="top">
              <div class="error-text">{{ scope.row.wxShippingError }}</div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="190" fixed="right">
          <template #default="scope">
            <el-button
              v-if="canSync(scope.row)"
              link
              type="primary"
              :loading="syncingId === scope.row.id"
              v-hasPermi="['wxma:operations:edit']"
              @click="syncOne(scope.row)"
            >
              {{ scope.row.wxShippingStatus === "1" ? "重新同步" : "同步微信" }}
            </el-button>
            <el-button
              v-if="scope.row.deliveryTime"
              link
              type="success"
              :loading="queryingId === scope.row.id"
              v-hasPermi="['wxma:operations:query']"
              @click="queryWechat(scope.row)"
            >
              微信状态
            </el-button>
            <el-button link @click="openOrder(scope.row)">订单详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.current"
          v-model:page-size="query.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <el-dialog v-model="wechatDialogVisible" title="微信官方发货状态" width="760px" destroy-on-close>
      <template v-if="wechatOrder">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="商城订单号">{{ currentOrder?.orderNo || "-" }}</el-descriptions-item>
          <el-descriptions-item label="微信支付单号">{{ wechatOrder.transactionId || currentOrder?.transactionId || "-" }}</el-descriptions-item>
          <el-descriptions-item label="微信订单状态">
            <el-tag :type="wechatStatusMeta(wechatOrder.orderState).type">{{ wechatStatusMeta(wechatOrder.orderState).label }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="是否投诉">{{ wechatOrder.inComplaint ? "是" : "否" }}</el-descriptions-item>
          <el-descriptions-item label="支付金额">￥{{ formatCentAmount(wechatOrder.paidAmount) }}</el-descriptions-item>
          <el-descriptions-item label="支付时间">{{ formatTimestamp(wechatOrder.payTime) }}</el-descriptions-item>
          <el-descriptions-item label="发货模式">{{ deliveryModeText(wechatOrder.shipping?.deliveryMode) }}</el-descriptions-item>
          <el-descriptions-item label="是否完成发货">{{ wechatOrder.shipping?.finishShipping ? "是" : "否" }}</el-descriptions-item>
        </el-descriptions>

        <el-table :data="wechatOrder.shipping?.shippingList || []" border class="wechat-logistics-table">
          <el-table-column label="快递公司编码" prop="expressCompany" min-width="140" />
          <el-table-column label="运单号" prop="trackingNo" min-width="180" />
          <el-table-column label="商品描述" min-width="180"><template #default="scope">{{ scope.row.goodsDesc || "-" }}</template></el-table-column>
          <el-table-column label="上传时间" min-width="170"><template #default="scope">{{ formatTimestamp(scope.row.uploadTime) }}</template></el-table-column>
        </el-table>

        <el-alert class="reconcile-alert" :title="reconcileMessage" :type="reconcileType" show-icon :closable="false" />
      </template>
      <el-empty v-else description="微信未返回订单发货信息" />
      <template #footer><el-button @click="wechatDialogVisible = false">关闭</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup name="WxMaShipping">
import { computed, getCurrentInstance, onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  batchSyncShippingOrders,
  getShippingEnabled,
  getShippingOrderPage,
  getShippingOrderSummary,
  getWechatShippingOrder,
  setShippingJumpPath,
  syncShippingOrder,
} from "@/api/wxma/wxoperations";

const { proxy } = getCurrentInstance();
const route = useRoute();
const router = useRouter();

const loading = ref(false);
const batchLoading = ref(false);
const capabilityLoading = ref(false);
const savingPath = ref(false);
const syncingId = ref("");
const queryingId = ref("");
const shippingEnabled = ref(null);
const jumpPath = ref("pages/order/order-detail/index");
const orderRows = ref([]);
const selectedRows = ref([]);
const total = ref(0);
const payTimeRange = ref([]);
const wechatDialogVisible = ref(false);
const wechatOrder = ref(null);
const currentOrder = ref(null);

const query = reactive({
  current: 1,
  size: 20,
  shippingKeyword: "",
  status: "",
  wxShippingStatus: "",
});

const summary = reactive({
  totalCount: 0,
  waitDeliveryCount: 0,
  pendingSyncCount: 0,
  successCount: 0,
  failedCount: 0,
  skippedCount: 0,
});

const summaryCards = [
  { key: "totalCount", label: "可管理订单", type: "total" },
  { key: "waitDeliveryCount", label: "等待发货", type: "warning" },
  { key: "pendingSyncCount", label: "待同步微信", type: "info" },
  { key: "successCount", label: "同步成功", type: "success" },
  { key: "failedCount", label: "同步失败", type: "danger" },
  { key: "skippedCount", label: "已跳过", type: "muted" },
];

const expressMap = {
  tiantian: "天天快递",
  huitongkuaidi: "百世快递",
  yunda: "韵达快递",
  yuantong: "圆通速递",
  debangwuliu: "德邦",
  ems: "EMS",
  shunfeng: "顺丰速运",
  zhongtong: "中通快递",
  shentong: "申通快递",
};

const batchReadyRows = computed(() => selectedRows.value.filter(canSync));
const reconcileType = computed(() => {
  if (!wechatOrder.value) return "info";
  const officialShipped = Number(wechatOrder.value.orderState) >= 2;
  const localShipped = Boolean(currentOrder.value?.deliveryTime);
  return officialShipped === localShipped ? "success" : "warning";
});
const reconcileMessage = computed(() => {
  if (!wechatOrder.value) return "暂时无法核对微信订单状态";
  const officialShipped = Number(wechatOrder.value.orderState) >= 2;
  const localShipped = Boolean(currentOrder.value?.deliveryTime);
  if (officialShipped === localShipped) return "微信状态与商城本地发货状态一致";
  return localShipped ? "商城已发货，但微信仍未记录发货，请重新同步" : "微信已记录发货，但商城本地订单尚未发货，请检查订单数据";
});

function buildParams(includePage = true) {
  const params = {
    shippingKeyword: query.shippingKeyword || undefined,
    status: query.status || undefined,
    wxShippingStatus: query.wxShippingStatus || undefined,
    beginTime: payTimeRange.value?.[0] || undefined,
    endTime: payTimeRange.value?.[1] || undefined,
  };
  if (includePage) Object.assign(params, { current: query.current, size: query.size });
  return params;
}

async function loadData() {
  loading.value = true;
  try {
    const [pageResponse, summaryResponse] = await Promise.all([
      getShippingOrderPage(buildParams(true)),
      getShippingOrderSummary(buildParams(false)),
    ]);
    orderRows.value = pageResponse.data?.records || [];
    total.value = Number(pageResponse.data?.total || 0);
    Object.assign(summary, summaryResponse.data || {});
    selectedRows.value = [];
  } finally {
    loading.value = false;
  }
}

function handleQuery() {
  query.current = 1;
  loadData();
}

function resetQuery() {
  Object.assign(query, { current: 1, size: 20, shippingKeyword: "", status: "", wxShippingStatus: "" });
  payTimeRange.value = [];
  router.replace({ query: {} });
  loadData();
}

function handleSelectionChange(rows) {
  selectedRows.value = rows;
}

function canSync(row) {
  return Boolean(row?.deliveryTime && row?.orderLogistics?.logistics && row?.orderLogistics?.logisticsNo);
}

function canSelect(row) {
  return canSync(row);
}

async function syncOne(row) {
  await proxy.$modal.confirm(`确认将订单 ${row.orderNo} 的发货信息同步到微信吗？`);
  syncingId.value = row.id;
  try {
    const response = await syncShippingOrder(row.id);
    proxy.$modal.msgSuccess(response.data || "微信发货信息同步成功");
    await loadData();
  } finally {
    syncingId.value = "";
  }
}

async function batchSync() {
  const rows = batchReadyRows.value;
  if (!rows.length) return proxy.$modal.msgWarning("请选择已经完成发货并填写物流信息的订单");
  await proxy.$modal.confirm(`确认同步选中的 ${rows.length} 个订单吗？`);
  batchLoading.value = true;
  try {
    const response = await batchSyncShippingOrders(rows.map(item => item.id));
    const result = response.data || {};
    const message = `成功 ${result.successCount || 0} 个，失败 ${result.failedCount || 0} 个，跳过 ${result.skippedCount || 0} 个`;
    if (result.failedCount || result.skippedCount) proxy.$modal.msgWarning(message);
    else proxy.$modal.msgSuccess(message);
    await loadData();
  } finally {
    batchLoading.value = false;
  }
}

async function queryWechat(row) {
  queryingId.value = row.id;
  currentOrder.value = row;
  try {
    const response = await getWechatShippingOrder(row.id);
    wechatOrder.value = response.data?.order || null;
    wechatDialogVisible.value = true;
  } finally {
    queryingId.value = "";
  }
}

function openOrder(row) {
  router.push({ path: "/mall/orderinfo", query: { orderNo: row.orderNo } });
}

async function checkShippingEnabled() {
  capabilityLoading.value = true;
  try {
    const response = await getShippingEnabled();
    const value = response.data;
    shippingEnabled.value = typeof value === "boolean" ? value : Boolean(value?.isTradeManaged ?? value?.enabled);
  } finally {
    capabilityLoading.value = false;
  }
}

async function saveJumpPath() {
  const path = jumpPath.value.trim();
  if (!path) return proxy.$modal.msgWarning("请填写消息跳转页面");
  if (path.startsWith("/")) return proxy.$modal.msgWarning("小程序页面路径不能以“/”开头");
  savingPath.value = true;
  try {
    await setShippingJumpPath(path);
    proxy.$modal.msgSuccess("发货消息跳转页面已保存");
  } finally {
    savingPath.value = false;
  }
}

function localStatusMeta(status) {
  return ({
    1: { label: "待发货", type: "warning" },
    2: { label: "待收货", type: "primary" },
    3: { label: "已完成", type: "success" },
  })[status] || { label: "未知", type: "info" };
}

function syncStatusMeta(status) {
  return ({
    1: { label: "同步成功", type: "success" },
    2: { label: "同步失败", type: "danger" },
    3: { label: "已跳过", type: "warning" },
  })[status] || { label: "未同步", type: "info" };
}

function wechatStatusMeta(status) {
  return ({
    1: { label: "待发货", type: "warning" },
    2: { label: "已发货", type: "primary" },
    3: { label: "确认收货", type: "success" },
    4: { label: "交易完成", type: "success" },
    5: { label: "已退款", type: "info" },
  })[status] || { label: "未知状态", type: "info" };
}

function deliveryModeText(mode) {
  return Number(mode) === 2 ? "分拆发货" : Number(mode) === 1 ? "统一发货" : "未记录";
}

function expressName(code) {
  return expressMap[code] || code || "未填写物流公司";
}

function formatAmount(value) {
  return Number(value || 0).toFixed(2);
}

function formatCentAmount(value) {
  return (Number(value || 0) / 100).toFixed(2);
}

function formatTimestamp(value) {
  if (!value) return "-";
  const date = new Date(Number(value) * 1000);
  if (Number.isNaN(date.getTime())) return "-";
  const pad = item => String(item).padStart(2, "0");
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
}

onMounted(() => {
  if (route.query.orderNo) query.shippingKeyword = String(route.query.orderNo);
  loadData();
  checkShippingEnabled();
});
</script>

<style scoped>
.shipping-center { background: var(--el-bg-color-page); min-height: calc(100vh - 84px); }
.summary-row { margin-top: 16px; }
.summary-card { margin-bottom: 14px; border-top: 3px solid #dcdfe6; }
.summary-card.summary-total { border-top-color: #606266; }
.summary-card.summary-warning { border-top-color: #e6a23c; }
.summary-card.summary-info { border-top-color: #909399; }
.summary-card.summary-success { border-top-color: #67c23a; }
.summary-card.summary-danger { border-top-color: #f56c6c; }
.summary-card.summary-muted { border-top-color: #b1b3b8; }
.summary-label { color: var(--el-text-color-secondary); font-size: 13px; }
.summary-value { margin-top: 8px; color: var(--el-text-color-primary); font-size: 28px; font-weight: 700; }
.capability-card, .filter-card, .table-card { margin-top: 14px; }
.capability-bar, .capability-item, .jump-path, .table-header { display: flex; align-items: center; }
.capability-bar, .table-header { justify-content: space-between; gap: 20px; }
.capability-item, .jump-path { gap: 10px; }
.capability-label { color: var(--el-text-color-regular); white-space: nowrap; }
.jump-path .el-input { width: 330px; }
.filter-card :deep(.el-card__body) { padding-bottom: 2px; }
.table-title { font-size: 16px; font-weight: 600; }
.selection-tip { margin-left: 12px; color: var(--el-text-color-secondary); font-size: 13px; }
.primary-text { color: var(--el-text-color-primary); font-weight: 500; line-height: 22px; }
.secondary-text { color: var(--el-text-color-secondary); font-size: 12px; line-height: 20px; }
.amount-text { color: #f56c6c; font-weight: 600; }
.status-time { margin-top: 5px; }
.empty-text { color: var(--el-text-color-placeholder); }
.copy-text { font-family: Consolas, monospace; }
.sync-status-line { display: flex; align-items: center; gap: 8px; margin-bottom: 4px; }
.retry-count { color: var(--el-text-color-secondary); font-size: 12px; }
.error-text { max-width: 260px; overflow: hidden; color: #f56c6c; font-size: 12px; text-overflow: ellipsis; white-space: nowrap; }
.pagination-wrap { display: flex; justify-content: flex-end; margin-top: 18px; }
.wechat-logistics-table { margin-top: 16px; }
.reconcile-alert { margin-top: 16px; }
@media (max-width: 992px) {
  .capability-bar, .table-header { align-items: flex-start; flex-direction: column; }
  .jump-path { align-items: flex-start; flex-wrap: wrap; }
  .jump-path .el-input { width: min(330px, 70vw); }
}
</style>

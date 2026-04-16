<!--
  Copyright (C) 2026
  All rights reserved, Designed By www.joolun.com
  注意：
  本软件为www.joolun.com开发研制，项目使用请保留此说明
-->

<template>
  <div v-loading="loading" class="app-container mall-dashboard-page">
    <div class="dashboard-hero">
      <div>
        <div class="hero-tag">商城经营总览</div>
        <h2 class="hero-title">商城数据看板</h2>
        <p class="hero-desc">
          聚合订单、会员、商品和售后四条主线，方便运营、客服和管理者快速判断当前经营状态。
        </p>
      </div>
      <div class="hero-actions">
        <el-radio-group v-model="dayRange" size="large" @change="handleRangeChange">
          <el-radio-button
            v-for="item in dayRangeOptions"
            :key="item.value"
            :label="item.value"
          >
            {{ item.label }}
          </el-radio-button>
        </el-radio-group>
        <el-button type="primary" plain @click="loadDashboardData">刷新数据</el-button>
      </div>
    </div>

    <div class="summary-grid">
      <div
        v-for="item in summaryCards"
        :key="item.label"
        class="summary-card"
        :class="`is-${item.theme}`"
      >
        <div class="summary-label">{{ item.label }}</div>
        <div class="summary-value">{{ item.value }}</div>
        <div class="summary-tip">{{ item.tip }}</div>
      </div>
    </div>

    <el-row :gutter="20" class="chart-row">
      <el-col :xl="16" :lg="16" :md="24" :sm="24" :xs="24">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <div>
                <div class="card-title">近 {{ dayRange }} 天成交趋势</div>
                <div class="card-sub-title">同时观察订单量、支付订单量、成交额和退款金额。</div>
              </div>
            </div>
          </template>
          <div ref="trendChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xl="8" :lg="8" :md="24" :sm="24" :xs="24">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <div>
                <div class="card-title">订单状态分布</div>
                <div class="card-sub-title">看当前待处理订单积压和售后压力。</div>
              </div>
            </div>
          </template>
          <div ref="orderStatusChartRef" class="chart-box chart-box-small"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :xl="10" :lg="10" :md="24" :sm="24" :xs="24">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <div>
                <div class="card-title">用户增长趋势</div>
                <div class="card-sub-title">观察注册拉新和会员转化的节奏变化。</div>
              </div>
            </div>
          </template>
          <div ref="memberTrendChartRef" class="chart-box chart-box-middle"></div>
        </el-card>
      </el-col>
      <el-col :xl="14" :lg="14" :md="24" :sm="24" :xs="24">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <div>
                <div class="card-title">近 30 天热销商品</div>
                <div class="card-sub-title">按销量和成交额排序，快速定位当前主卖商品。</div>
              </div>
            </div>
          </template>
          <el-table :data="topGoodsRows" size="small" class="top-goods-table">
            <el-table-column label="排名" width="72" align="center">
              <template #default="scope">
                <span class="rank-badge">{{ scope.row.rank }}</span>
              </template>
            </el-table-column>
            <el-table-column label="商品" min-width="260">
              <template #default="scope">
                <div class="goods-cell">
                  <el-image
                    v-if="scope.row.picUrl"
                    :src="resolveDashboardImage(scope.row.picUrl)"
                    fit="cover"
                    class="goods-cover"
                  />
                  <div v-else class="goods-cover goods-cover-fallback">无图</div>
                  <div class="goods-meta">
                    <div class="goods-name">{{ scope.row.spuName || "未命名商品" }}</div>
                    <div class="goods-id">SPU：{{ scope.row.spuId || "未知" }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="销量" width="100" align="right">
              <template #default="scope">{{ formatNumber(scope.row.saleQuantity) }}</template>
            </el-table-column>
            <el-table-column label="支付订单" width="110" align="right">
              <template #default="scope">{{ formatNumber(scope.row.paidOrderCount) }}</template>
            </el-table-column>
            <el-table-column label="成交额" width="140" align="right">
              <template #default="scope">{{ formatAmount(scope.row.paidAmount) }}</template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <div class="section-grid">
      <el-card class="section-card" shadow="never">
        <template #header>
          <div class="card-header">
            <div>
              <div class="card-title">订单经营</div>
              <div class="card-sub-title">支付转化、履约进度和售后处理中订单。</div>
            </div>
          </div>
        </template>
        <div class="section-stats">
          <div v-for="item in orderStatCards" :key="item.label" class="section-stat-item">
            <div class="section-stat-label">{{ item.label }}</div>
            <div class="section-stat-value">{{ item.value }}</div>
            <div class="section-stat-tip">{{ item.tip }}</div>
          </div>
        </div>
      </el-card>

      <el-card class="section-card" shadow="never">
        <template #header>
          <div class="card-header">
            <div>
              <div class="card-title">会员概况</div>
              <div class="card-sub-title">商城用户沉淀、会员转化和活跃度。</div>
            </div>
          </div>
        </template>
        <div class="section-stats">
          <div v-for="item in memberStatCards" :key="item.label" class="section-stat-item">
            <div class="section-stat-label">{{ item.label }}</div>
            <div class="section-stat-value">{{ item.value }}</div>
            <div class="section-stat-tip">{{ item.tip }}</div>
          </div>
        </div>
      </el-card>

      <el-card class="section-card" shadow="never">
        <template #header>
          <div class="card-header">
            <div>
              <div class="card-title">商品供给</div>
              <div class="card-sub-title">商品规模、上架率和库存风险。</div>
            </div>
          </div>
        </template>
        <div class="section-stats">
          <div v-for="item in goodsStatCards" :key="item.label" class="section-stat-item">
            <div class="section-stat-label">{{ item.label }}</div>
            <div class="section-stat-value">{{ item.value }}</div>
            <div class="section-stat-tip">{{ item.tip }}</div>
          </div>
        </div>
      </el-card>

      <el-card class="section-card" shadow="never">
        <template #header>
          <div class="card-header">
            <div>
              <div class="card-title">售后概况</div>
              <div class="card-sub-title">售后池规模、退款进度和退款金额。</div>
            </div>
          </div>
        </template>
        <div class="section-stats">
          <div v-for="item in afterSaleStatCards" :key="item.label" class="section-stat-item">
            <div class="section-stat-label">{{ item.label }}</div>
            <div class="section-stat-value">{{ item.value }}</div>
            <div class="section-stat-tip">{{ item.tip }}</div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup name="MallDashboard">
import * as echarts from "echarts";
import { computed, getCurrentInstance, nextTick, onBeforeUnmount, onMounted, reactive, ref } from "vue";
import { getDashboardOverview } from "@/api/mall/dashboard";
import { resolveMallImageUrl } from "@/utils/mall-image";

const { proxy } = getCurrentInstance();

/**
 * 看板支持的趋势区间。
 * 这里和后端保持一致，避免前端传出后端未支持的统计跨度。
 */
const dayRangeOptions = [
  { label: "近 7 天", value: 7 },
  { label: "近 15 天", value: 15 },
  { label: "近 30 天", value: 30 },
];

const loading = ref(false);
const dayRange = ref(15);
const trendChartRef = ref(null);
const orderStatusChartRef = ref(null);
const memberTrendChartRef = ref(null);

/**
 * 统一处理数据看板中的商品图片地址。
 *
 * @param {string} imageUrl 图片地址
 * @returns {string}
 */
function resolveDashboardImage(imageUrl) {
  return resolveMallImageUrl(imageUrl);
}

/**
 * 页面所有统计结果统一放在一个响应式对象中。
 * 这样图表和卡片都从同一份数据读取，后续扩展字段时比较集中。
 */
const dashboard = reactive({
  overview: {},
  orderStats: {},
  memberStats: {},
  goodsStats: {},
  afterSaleStats: {},
  trend: [],
  topGoods: [],
});

let trendChartInstance = null;
let orderStatusChartInstance = null;
let memberTrendChartInstance = null;

/**
 * 统一的窗口缩放处理器。
 * 页面里有多张图表，尺寸变化时一起重绘，避免个别图表宽高错位。
 */
const handleWindowResize = () => {
  trendChartInstance && trendChartInstance.resize();
  orderStatusChartInstance && orderStatusChartInstance.resize();
  memberTrendChartInstance && memberTrendChartInstance.resize();
};

const summaryCards = computed(() => [
  {
    label: "累计成交额",
    value: formatAmount(dashboard.overview.totalPaidAmount),
    tip: `累计支付订单 ${formatNumber(dashboard.overview.paidOrderCount)} 笔`,
    theme: "emerald",
  },
  {
    label: "今日成交额",
    value: formatAmount(dashboard.overview.todayPaidAmount),
    tip: `今日支付订单 ${formatNumber(dashboard.overview.todayPaidOrderCount)} 笔`,
    theme: "orange",
  },
  {
    label: "累计订单数",
    value: formatNumber(dashboard.overview.totalOrderCount),
    tip: `支付转化率 ${formatPercent(dashboard.overview.paidOrderRate)}`,
    theme: "blue",
  },
  {
    label: "今日订单数",
    value: formatNumber(dashboard.overview.todayOrderCount),
    tip: `今日支付率 ${formatPercent(dashboard.overview.todayPaidOrderRate)}`,
    theme: "navy",
  },
  {
    label: "商城会员数",
    value: formatNumber(dashboard.memberStats.memberCount),
    tip: `会员渗透率 ${formatPercent(dashboard.memberStats.memberRate)}`,
    theme: "green",
  },
  {
    label: "今日新增会员",
    value: formatNumber(dashboard.memberStats.todayNewMemberCount),
    tip: `今日新增用户 ${formatNumber(dashboard.memberStats.todayNewUserCount)} 人`,
    theme: "olive",
  },
  {
    label: "在架商品数",
    value: formatNumber(dashboard.goodsStats.onShelfGoodsCount),
    tip: `上架率 ${formatPercent(dashboard.goodsStats.onShelfRate)}`,
    theme: "cyan",
  },
  {
    label: "库存预警 SKU",
    value: formatNumber(dashboard.goodsStats.warningSkuCount),
    tip: `售后退款 ${formatAmount(dashboard.afterSaleStats.todayRefundAmount)}`,
    theme: "red",
  },
]);

const orderStatCards = computed(() => [
  {
    label: "待付款",
    value: formatNumber(dashboard.orderStats.waitPayCount),
    tip: "需要关注支付转化",
  },
  {
    label: "待发货",
    value: formatNumber(dashboard.orderStats.waitDeliveryCount),
    tip: "履约压力主要集中在这里",
  },
  {
    label: "待收货",
    value: formatNumber(dashboard.orderStats.waitReceiveCount),
    tip: "可结合物流链路继续跟踪",
  },
  {
    label: "已完成",
    value: formatNumber(dashboard.orderStats.finishedCount),
    tip: `完成率 ${formatPercent(dashboard.orderStats.finishRate)}`,
  },
  {
    label: "处理中售后",
    value: formatNumber(dashboard.orderStats.processingAfterSaleCount),
    tip: "客服需要重点关注",
  },
  {
    label: "平均客单价",
    value: formatAmount(dashboard.orderStats.averagePaidAmount),
    tip: `支付率 ${formatPercent(dashboard.orderStats.paidOrderRate)}`,
  },
]);

const memberStatCards = computed(() => [
  {
    label: "商城用户总数",
    value: formatNumber(dashboard.memberStats.totalUserCount),
    tip: "商城域主用户池规模",
  },
  {
    label: "会员总数",
    value: formatNumber(dashboard.memberStats.memberCount),
    tip: `会员渗透率 ${formatPercent(dashboard.memberStats.memberRate)}`,
  },
  {
    label: "今日新增用户",
    value: formatNumber(dashboard.memberStats.todayNewUserCount),
    tip: "观察拉新效果",
  },
  {
    label: "今日新增会员",
    value: formatNumber(dashboard.memberStats.todayNewMemberCount),
    tip: `今日转化率 ${formatPercent(dashboard.memberStats.todayMemberRate)}`,
  },
  {
    label: "近 7 天活跃",
    value: formatNumber(dashboard.memberStats.activeUser7Count),
    tip: "最近一周有登录行为的用户",
  },
  {
    label: "近 30 天活跃",
    value: formatNumber(dashboard.memberStats.activeUser30Count),
    tip: `已消费用户 ${formatNumber(dashboard.memberStats.consumeUserCount)} 人`,
  },
]);

const goodsStatCards = computed(() => [
  {
    label: "商品总数",
    value: formatNumber(dashboard.goodsStats.totalGoodsCount),
    tip: "SPU 维度统计",
  },
  {
    label: "在架商品",
    value: formatNumber(dashboard.goodsStats.onShelfGoodsCount),
    tip: `下架商品 ${formatNumber(dashboard.goodsStats.offShelfGoodsCount)} 个`,
  },
  {
    label: "SKU 总数",
    value: formatNumber(dashboard.goodsStats.totalSkuCount),
    tip: `可售 SKU 占比 ${formatPercent(dashboard.goodsStats.enabledSkuRate)}`,
  },
  {
    label: "库存总量",
    value: formatNumber(dashboard.goodsStats.totalStock),
    tip: "按启用 SKU 累计",
  },
  {
    label: "缺货商品",
    value: formatNumber(dashboard.goodsStats.soldOutGoodsCount),
    tip: "可售 SKU 总库存为 0 的商品",
  },
  {
    label: "库存预警 SKU",
    value: formatNumber(dashboard.goodsStats.warningSkuCount),
    tip: "阈值按 10 件预警",
  },
]);

const afterSaleStatCards = computed(() => [
  {
    label: "售后池总量",
    value: formatNumber(dashboard.afterSaleStats.totalCount),
    tip: "所有进入售后链路的订单项",
  },
  {
    label: "退款申请中",
    value: formatNumber(dashboard.afterSaleStats.applyingCount),
    tip: "待客服审核",
  },
  {
    label: "待退款",
    value: formatNumber(dashboard.afterSaleStats.waitingRefundCount),
    tip: "已审核通过，等待退款完成",
  },
  {
    label: "已退款",
    value: formatNumber(dashboard.afterSaleStats.refundedCount),
    tip: `退款完成率 ${formatPercent(dashboard.afterSaleStats.refundFinishRate)}`,
  },
  {
    label: "累计退款金额",
    value: formatAmount(dashboard.afterSaleStats.refundAmount),
    tip: `今日退款 ${formatAmount(dashboard.afterSaleStats.todayRefundAmount)}`,
  },
  {
    label: "已拒绝售后",
    value: formatNumber(dashboard.afterSaleStats.rejectCount),
    tip: "可结合售后明细继续排查原因",
  },
]);

const topGoodsRows = computed(() =>
  (dashboard.topGoods || []).map((item, index) => ({
    ...item,
    rank: index + 1,
  }))
);

onMounted(() => {
  loadDashboardData();
  window.addEventListener("resize", handleWindowResize);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", handleWindowResize);
  disposeCharts();
});

/**
 * 切换趋势区间时重新拉取看板。
 * 数据口径和图表渲染同时更新，避免本地二次计算带来歧义。
 */
function handleRangeChange() {
  loadDashboardData();
}

/**
 * 读取后台看板数据。
 * 当前页面所有卡片和图表都依赖这一个接口返回，加载完成后统一重绘图表。
 */
function loadDashboardData() {
  loading.value = true;
  proxy?.$modal?.loading("正在加载商城数据看板，请稍候...");
  getDashboardOverview({ dayRange: dayRange.value })
    .then(async (response) => {
      const data = response?.data || {};
      dashboard.overview = data.overview || {};
      dashboard.orderStats = data.orderStats || {};
      dashboard.memberStats = data.memberStats || {};
      dashboard.goodsStats = data.goodsStats || {};
      dashboard.afterSaleStats = data.afterSaleStats || {};
      dashboard.trend = data.trend || [];
      dashboard.topGoods = data.topGoods || [];
      await nextTick();
      renderCharts();
    })
    .finally(() => {
      loading.value = false;
      proxy?.$modal?.closeLoading();
    });
}

/**
 * 统一重绘三张图表。
 * 页面只要主数据发生变化，就整体刷新图表，逻辑更稳定。
 */
function renderCharts() {
  renderTrendChart();
  renderOrderStatusChart();
  renderMemberTrendChart();
}

/**
 * 渲染成交趋势图。
 * 采用柱线混合图，把金额和订单量放在不同坐标轴上，便于同时观察规模和转化。
 */
function renderTrendChart() {
  if (!trendChartRef.value) {
    return;
  }
  trendChartInstance && trendChartInstance.dispose();
  trendChartInstance = echarts.init(trendChartRef.value);
  const xAxisData = (dashboard.trend || []).map((item) => item.dateLabel);
  trendChartInstance.setOption({
    color: ["#0f766e", "#f97316", "#1d4ed8", "#dc2626"],
    tooltip: {
      trigger: "axis",
    },
    legend: {
      top: 0,
      data: ["成交额", "退款金额", "订单数", "支付订单"],
    },
    grid: {
      left: 56,
      right: 56,
      top: 48,
      bottom: 24,
    },
    xAxis: {
      type: "category",
      data: xAxisData,
      axisTick: {
        alignWithLabel: true,
      },
    },
    yAxis: [
      {
        type: "value",
        name: "金额",
      },
      {
        type: "value",
        name: "订单量",
      },
    ],
    series: [
      {
        name: "成交额",
        type: "bar",
        barMaxWidth: 28,
        data: (dashboard.trend || []).map((item) => toNumber(item.paidAmount)),
      },
      {
        name: "退款金额",
        type: "bar",
        barMaxWidth: 28,
        data: (dashboard.trend || []).map((item) => toNumber(item.refundAmount)),
      },
      {
        name: "订单数",
        type: "line",
        yAxisIndex: 1,
        smooth: true,
        data: (dashboard.trend || []).map((item) => toNumber(item.orderCount)),
      },
      {
        name: "支付订单",
        type: "line",
        yAxisIndex: 1,
        smooth: true,
        data: (dashboard.trend || []).map((item) => toNumber(item.paidOrderCount)),
      },
    ],
  });
}

/**
 * 渲染订单状态分布图。
 * 用环形图表现待处理订单与已完成订单的结构，后台一眼就能看出压力集中在哪。
 */
function renderOrderStatusChart() {
  if (!orderStatusChartRef.value) {
    return;
  }
  orderStatusChartInstance && orderStatusChartInstance.dispose();
  orderStatusChartInstance = echarts.init(orderStatusChartRef.value);
  orderStatusChartInstance.setOption({
    color: ["#ef4444", "#f97316", "#0ea5e9", "#10b981", "#64748b", "#8b5cf6"],
    tooltip: {
      trigger: "item",
      formatter: "{b}: {c} ({d}%)",
    },
    legend: {
      bottom: 0,
      itemWidth: 10,
      itemHeight: 10,
    },
    series: [
      {
        type: "pie",
        radius: ["46%", "72%"],
        center: ["50%", "44%"],
        label: {
          formatter: "{b}\n{c}",
        },
        data: [
          { name: "待付款", value: toNumber(dashboard.orderStats.waitPayCount) },
          { name: "待发货", value: toNumber(dashboard.orderStats.waitDeliveryCount) },
          { name: "待收货", value: toNumber(dashboard.orderStats.waitReceiveCount) },
          { name: "已完成", value: toNumber(dashboard.orderStats.finishedCount) },
          { name: "已取消", value: toNumber(dashboard.orderStats.cancelCount) },
          { name: "售后处理中", value: toNumber(dashboard.orderStats.processingAfterSaleCount) },
        ],
      },
    ],
  });
}

/**
 * 渲染用户增长趋势图。
 * 同时展示新增用户和新增会员，方便运营观察拉新和手机号入会是否同步增长。
 */
function renderMemberTrendChart() {
  if (!memberTrendChartRef.value) {
    return;
  }
  memberTrendChartInstance && memberTrendChartInstance.dispose();
  memberTrendChartInstance = echarts.init(memberTrendChartRef.value);
  memberTrendChartInstance.setOption({
    color: ["#16a34a", "#2563eb"],
    tooltip: {
      trigger: "axis",
    },
    legend: {
      top: 0,
      data: ["新增用户", "新增会员"],
    },
    grid: {
      left: 48,
      right: 24,
      top: 48,
      bottom: 24,
    },
    xAxis: {
      type: "category",
      data: (dashboard.trend || []).map((item) => item.dateLabel),
    },
    yAxis: {
      type: "value",
      minInterval: 1,
    },
    series: [
      {
        name: "新增用户",
        type: "bar",
        barMaxWidth: 24,
        data: (dashboard.trend || []).map((item) => toNumber(item.newUserCount)),
      },
      {
        name: "新增会员",
        type: "line",
        smooth: true,
        data: (dashboard.trend || []).map((item) => toNumber(item.newMemberCount)),
      },
    ],
  });
}

/**
 * 销毁图表实例。
 * 页面切走后及时释放资源，避免二次进入时出现重复挂载或内存占用。
 */
function disposeCharts() {
  trendChartInstance && trendChartInstance.dispose();
  orderStatusChartInstance && orderStatusChartInstance.dispose();
  memberTrendChartInstance && memberTrendChartInstance.dispose();
  trendChartInstance = null;
  orderStatusChartInstance = null;
  memberTrendChartInstance = null;
}

/**
 * 格式化数字。
 *
 * @param {number|string} value 原始值
 * @returns {string}
 */
function formatNumber(value) {
  return new Intl.NumberFormat("zh-CN").format(toNumber(value));
}

/**
 * 格式化金额。
 *
 * @param {number|string} value 原始值
 * @returns {string}
 */
function formatAmount(value) {
  return `￥${toNumber(value).toFixed(2)}`;
}

/**
 * 格式化百分比。
 *
 * @param {number|string} value 原始值
 * @returns {string}
 */
function formatPercent(value) {
  return `${toNumber(value).toFixed(2)}%`;
}

/**
 * 安全转数字。
 * 后端聚合字段可能是字符串或数值，这里统一转成 number 供页面显示和绘图。
 *
 * @param {number|string} value 原始值
 * @returns {number}
 */
function toNumber(value) {
  if (value === null || value === undefined || value === "") {
    return 0;
  }
  const numberValue = Number(value);
  return Number.isNaN(numberValue) ? 0 : numberValue;
}
</script>

<style lang="scss" scoped>
.mall-dashboard-page {
  background:
    radial-gradient(circle at top left, rgba(15, 118, 110, 0.08), transparent 26%),
    radial-gradient(circle at top right, rgba(249, 115, 22, 0.1), transparent 22%),
    linear-gradient(180deg, #f6fbfb 0%, #f8fafc 100%);
  min-height: calc(100vh - 84px);
}

.dashboard-hero {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  padding: 28px 30px;
  margin-bottom: 20px;
  border-radius: 24px;
  background: linear-gradient(135deg, #0f766e 0%, #115e59 45%, #1d4ed8 100%);
  color: #fff;
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.16);
}

.hero-tag {
  display: inline-flex;
  align-items: center;
  height: 28px;
  padding: 0 12px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.16);
  font-size: 13px;
  letter-spacing: 1px;
}

.hero-title {
  margin: 14px 0 10px;
  font-size: 32px;
  font-weight: 700;
  line-height: 1.2;
}

.hero-desc {
  max-width: 620px;
  margin: 0;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  line-height: 1.8;
}

.hero-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
  gap: 18px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
  margin-bottom: 20px;
}

.summary-card {
  position: relative;
  overflow: hidden;
  padding: 22px 20px;
  border-radius: 20px;
  background: #fff;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.08);
}

.summary-card::after {
  content: "";
  position: absolute;
  right: -32px;
  bottom: -36px;
  width: 110px;
  height: 110px;
  border-radius: 50%;
  opacity: 0.14;
}

.summary-card.is-emerald::after {
  background: #059669;
}

.summary-card.is-orange::after {
  background: #ea580c;
}

.summary-card.is-blue::after {
  background: #2563eb;
}

.summary-card.is-navy::after {
  background: #1e3a8a;
}

.summary-card.is-green::after {
  background: #16a34a;
}

.summary-card.is-olive::after {
  background: #65a30d;
}

.summary-card.is-cyan::after {
  background: #0891b2;
}

.summary-card.is-red::after {
  background: #dc2626;
}

.summary-label {
  color: #64748b;
  font-size: 14px;
}

.summary-value {
  margin: 12px 0 10px;
  color: #0f172a;
  font-size: 30px;
  font-weight: 700;
  line-height: 1.1;
}

.summary-tip {
  color: #94a3b8;
  font-size: 13px;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card,
.section-card {
  border: none;
  border-radius: 22px;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 14px 34px rgba(15, 23, 42, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  color: #0f172a;
  font-size: 18px;
  font-weight: 700;
}

.card-sub-title {
  margin-top: 6px;
  color: #94a3b8;
  font-size: 13px;
}

.chart-box {
  height: 360px;
}

.chart-box-small {
  height: 360px;
}

.chart-box-middle {
  height: 320px;
}

.section-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
}

.section-stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14px;
}

.section-stat-item {
  padding: 16px 14px;
  border-radius: 18px;
  background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
}

.section-stat-label {
  color: #64748b;
  font-size: 13px;
}

.section-stat-value {
  margin: 10px 0 8px;
  color: #0f172a;
  font-size: 24px;
  font-weight: 700;
}

.section-stat-tip {
  color: #94a3b8;
  font-size: 12px;
  line-height: 1.6;
}

.top-goods-table {
  margin-top: 4px;
}

.rank-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #f59e0b 0%, #ea580c 100%);
  color: #fff;
  font-weight: 700;
}

.goods-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.goods-cover {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: #f1f5f9;
  flex-shrink: 0;
}

.goods-cover-fallback {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #94a3b8;
  font-size: 12px;
}

.goods-meta {
  min-width: 0;
}

.goods-name {
  color: #0f172a;
  font-weight: 600;
  line-height: 1.5;
}

.goods-id {
  margin-top: 4px;
  color: #94a3b8;
  font-size: 12px;
}

@media (max-width: 1400px) {
  .summary-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .section-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 992px) {
  .dashboard-hero {
    flex-direction: column;
    padding: 24px 22px;
  }

  .hero-actions {
    align-items: flex-start;
  }

  .section-stats {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 768px) {
  .summary-grid,
  .section-stats {
    grid-template-columns: 1fr;
  }

  .hero-title {
    font-size: 26px;
  }
}
</style>

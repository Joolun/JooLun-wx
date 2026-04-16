<!--
  Copyright (C) 2026
  All rights reserved, Designed By www.joolun.com
  注意：
  本软件为www.joolun.com开发研制，项目使用请保留此说明
-->

<template>
  <div class="app-container after-sale-page">
    <div class="summary-panel">
      <div v-for="item in summaryCards" :key="item.label" class="summary-card">
        <div class="summary-label">{{ item.label }}</div>
        <div class="summary-value" :class="item.emphasisClass">{{ item.value }}</div>
        <div class="summary-tip">{{ item.tip }}</div>
      </div>
    </div>

    <div class="quick-filter-panel">
      <div class="quick-filter-title">售后状态快捷筛选</div>
      <div class="quick-filter-list">
        <el-button
          v-for="item in quickFilters"
          :key="item.value"
          :type="status === item.value ? 'primary' : 'default'"
          plain
          size="small"
          @click="handleQuickFilter(item.value)"
        >
          {{ item.label }}
        </el-button>
      </div>
    </div>

    <div class="sop-panel">
      <div class="sop-panel-header">
        <div class="sop-panel-title">客服处理 SOP</div>
        <div class="sop-panel-tip">按当前筛选状态给客服明确处理动作、核对项和沟通话术，减少审核口径不一致。</div>
      </div>
      <div class="sop-card-grid">
        <div v-for="item in serviceSopCards" :key="item.title" class="sop-card">
          <div class="sop-card-title">{{ item.title }}</div>
          <div class="sop-card-desc">{{ item.desc }}</div>
          <div v-for="subItem in item.items" :key="`${item.title}-${subItem}`" class="sop-card-item">
            {{ subItem }}
          </div>
        </div>
      </div>
    </div>

    <div class="search-panel">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" clearable placeholder="支持模糊搜索" />
        </el-form-item>
        <el-form-item label="会员编号">
          <el-input v-model="searchForm.userNo" clearable placeholder="输入会员编号" />
        </el-form-item>
        <el-form-item label="会员昵称">
          <el-input v-model="searchForm.userNickName" clearable placeholder="输入会员昵称" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.userMobile" clearable placeholder="输入手机号" />
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.spuNameKeyword" clearable placeholder="输入商品名称" />
        </el-form-item>
        <el-form-item label="申请时间">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            value-format="YYYY-MM-DD HH:mm:ss"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-panel" v-loading="loading">
      <div class="table-panel-header">
        <div class="table-panel-title">售后工作台</div>
        <div class="table-panel-tip">按订单项维度处理售后，避免一个订单里多个商品混在一起审核。</div>
      </div>

      <el-table :data="tableData" border>
        <el-table-column label="商品信息" min-width="280">
          <template #default="scope">
            <div class="goods-cell">
              <el-image :src="resolveAfterSaleImage(scope.row.picUrl)" class="goods-image" fit="cover" />
              <div class="goods-meta">
                <div class="goods-name">{{ scope.row.spuName || "未命名商品" }}</div>
                <div class="goods-sub">{{ scope.row.specInfo || "默认规格" }}</div>
                <div class="goods-sub">数量：{{ scope.row.quantity || 0 }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="会员信息" min-width="220">
          <template #default="scope">
            <div class="member-cell">
              <el-avatar :src="resolveAfterSaleImage(scope.row.userAvatarUrl)" icon="el-icon-user-solid" />
              <div class="member-meta">
                <div class="member-name">{{ buildMemberName(scope.row) }}</div>
                <div class="member-sub">会员编号：{{ scope.row.userNo || "未生成" }}</div>
                <div class="member-sub">{{ maskMobile(scope.row.userMobile) }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="订单信息" min-width="220">
          <template #default="scope">
            <div class="order-cell">
              <div class="order-no">{{ scope.row.orderNo || "未记录" }}</div>
              <div class="order-sub">订单状态：{{ formatOrderStatus(scope.row) }}</div>
              <div class="order-sub">支付状态：{{ scope.row.orderIsPay === "1" ? "已支付" : "未支付" }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="售后状态" width="140" align="center">
          <template #default="scope">
            <el-tag :type="buildAfterSaleTagType(scope.row)" effect="plain" size="small">
              {{ buildAfterSaleStatusDesc(scope.row) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="退款金额" width="120" align="center">
          <template #default="scope">
            <span class="amount-text">{{ formatCurrency(scope.row.paymentPrice) }}</span>
          </template>
        </el-table-column>

        <el-table-column label="申请原因 / 审核备注" min-width="260">
          <template #default="scope">
            <div class="remark-line">申请原因：{{ scope.row.refundReason || "未填写" }}</div>
            <div class="remark-line">审核备注：{{ scope.row.refundAuditRemark || "暂无" }}</div>
          </template>
        </el-table-column>

        <el-table-column label="关键时间" width="220">
          <template #default="scope">
            <div class="time-line">申请：{{ formatDateTime(scope.row.refundApplyTime) }}</div>
            <div class="time-line">审核：{{ formatDateTime(scope.row.refundAuditTime) }}</div>
            <div class="time-line">退款：{{ formatDateTime(scope.row.refundSuccessTime) }}</div>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" link @click="goOrderPage(scope.row)">查看订单</el-button>
            <el-button
              v-if="scope.row.status === '1' && scope.row.orderIsPay === '1'"
              type="danger"
              link
              @click="openRefundAuditDialog(scope.row)"
            >
              退款审核
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="page.current"
          v-model:page-size="page.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="page.total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>

    <el-dialog v-model="refundAuditDialog.visible" title="退款审核" width="520px" @closed="resetRefundAuditDialog">
      <el-form label-width="88px">
        <el-form-item label="商品信息">
          <div class="audit-goods">
            <div class="audit-goods-name">{{ refundAuditDialog.currentItem?.spuName || "未命名商品" }}</div>
            <div class="audit-goods-sub">{{ refundAuditDialog.currentItem?.specInfo || "默认规格" }}</div>
            <div class="audit-goods-sub">退款金额：{{ formatCurrency(refundAuditDialog.currentItem?.paymentPrice) }}</div>
          </div>
        </el-form-item>
        <el-form-item label="申请原因">
          <div class="audit-reason">{{ refundAuditDialog.currentItem?.refundReason || "未填写申请原因" }}</div>
        </el-form-item>
        <el-form-item v-if="refundAuditChecklist.length" label="审核核对">
          <div class="audit-check-list">
            <div v-for="item in refundAuditChecklist" :key="item" class="audit-check-item">
              {{ item }}
            </div>
          </div>
        </el-form-item>
        <el-form-item label="审核结果">
          <el-radio-group v-model="refundAuditDialog.form.status">
            <el-radio label="3">同意退款</el-radio>
            <el-radio label="2">拒绝退款</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注">
          <el-input
            v-model="refundAuditDialog.form.refundAuditRemark"
            type="textarea"
            :rows="4"
            maxlength="200"
            show-word-limit
            :placeholder="refundAuditDialog.form.status === '3' ? '请输入审核通过说明' : '请输入拒绝退款原因'"
          />
        </el-form-item>
        <el-form-item v-if="refundAuditRemarkTemplates.length" label="快捷备注">
          <div class="audit-template-list">
            <el-button
              v-for="item in refundAuditRemarkTemplates"
              :key="item"
              plain
              size="small"
              @click="applyRefundAuditRemarkTemplate(item)"
            >
              {{ item }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span>
          <el-button @click="refundAuditDialog.visible = false">取消</el-button>
          <el-button type="primary" :loading="refundAuditDialog.submitting" @click="submitRefundAudit">提交审核</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, getCurrentInstance, onMounted, reactive, toRefs, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getAfterSalePage, getAfterSaleSummary } from "@/api/mall/aftersale";
import { doOrderRefunds } from "@/api/mall/orderinfo";
import { resolveMallImageUrl } from "@/utils/mall-image";

const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

/**
 * 统一处理售后页中的商品图和会员头像地址。
 *
 * @param {string} imageUrl 图片地址
 * @returns {string}
 */
function resolveAfterSaleImage(imageUrl) {
  return resolveMallImageUrl(imageUrl);
}

const data = reactive({
  loading: false,
  status: "ALL",
  dateRange: [],
  searchForm: {
    userId: "",
    orderNo: "",
    userNo: "",
    userNickName: "",
    userMobile: "",
    spuNameKeyword: "",
  },
  summary: {
    totalCount: 0,
    applyingCount: 0,
    rejectCount: 0,
    waitingRefundCount: 0,
    refundedCount: 0,
    refundAmount: 0,
  },
  tableData: [],
  page: {
    current: 1,
    size: 10,
    total: 0,
  },
  refundAuditDialog: {
    visible: false,
    submitting: false,
    currentItem: null,
    form: {
      id: "",
      status: "3",
      refundAuditRemark: "",
    },
  },
});

const { loading, status, dateRange, searchForm, tableData, page, refundAuditDialog } = toRefs(data);

/**
 * 快捷筛选项。
 */
const quickFilters = [
  { label: "全部售后", value: "ALL" },
  { label: "退款申请中", value: "1" },
  { label: "退款已拒绝", value: "2" },
  { label: "等待回调", value: "3" },
  { label: "已退款", value: "4" },
];

/**
 * 顶部概览卡片。
 */
const summaryCards = computed(() => {
  return [
    {
      label: "售后订单项",
      value: Number(data.summary.totalCount || 0),
      tip: "当前筛选条件下的售后商品条数",
      emphasisClass: "is-primary",
    },
    {
      label: "待审核",
      value: Number(data.summary.applyingCount || 0),
      tip: "需要客服尽快处理的退款申请",
      emphasisClass: "is-danger",
    },
    {
      label: "等待回调",
      value: Number(data.summary.waitingRefundCount || 0),
      tip: "后台已同意退款，等待微信回写",
      emphasisClass: "is-warning",
    },
    {
      label: "已退款",
      value: Number(data.summary.refundedCount || 0),
      tip: "系统已退款成功的订单项",
      emphasisClass: "is-success",
    },
    {
      label: "退款金额",
      value: formatCurrency(data.summary.refundAmount),
      tip: "当前筛选条件下的售后金额汇总",
      emphasisClass: "is-danger",
    },
  ];
});

/**
 * 当前筛选状态下的客服处理 SOP。
 * 这里不追求复杂规则引擎，只给客服最容易直接执行的重点、核对项和话术。
 */
const serviceSopCards = computed(() => buildServiceSopCards(data.status, data.summary));

/**
 * 退款审核核对项。
 * 审核弹窗里直接把关键检查项列出来，避免客服边切订单边回忆规则。
 */
const refundAuditChecklist = computed(() => {
  const currentItem = data.refundAuditDialog.currentItem;
  if (!currentItem) {
    return [];
  }
  const checklist = [
    `确认订单支付状态：${currentItem.orderIsPay === "1" ? "已支付" : "未支付"}`,
    `确认订单履约状态：${formatOrderStatus(currentItem)}`,
    `确认退款原因：${currentItem.refundReason || "未填写申请原因"}`,
  ];
  if (currentItem.refundAuditRemark) {
    checklist.push(`历史审核备注：${currentItem.refundAuditRemark}`);
  } else {
    checklist.push("如拒绝退款，请在备注里写清依据和下一步建议。");
  }
  return checklist;
});

/**
 * 退款审核快捷备注。
 * 让客服按结果一键带入标准文案，再补充当前订单的个性化信息。
 */
const refundAuditRemarkTemplates = computed(() => {
  if (!data.refundAuditDialog.currentItem) {
    return [];
  }
  if (data.refundAuditDialog.form.status === "3") {
    return [
      "已核实订单信息和申请原因，符合退款条件，已为您发起原路退款。",
      "后台已确认退款申请，资金会原路退回，请留意微信支付到账通知。",
      "已同意本次退款，系统回写完成后会自动到账，请您耐心等待。",
    ];
  }
  return [
    "已核对订单状态与售后规则，当前暂不满足退款条件，如有补充凭证可继续提交。",
    "订单已进入履约阶段，暂不支持直接退款，请先补充商品问题说明或相关证明。",
    "本次申请暂未通过，您可联系客服继续说明情况，我们会再次协助核实。",
  ];
});

watch(
  () => route.query,
  () => {
    syncRouteQuery();
    loadData();
  },
  { immediate: true }
);

onMounted(() => {
  syncRouteQuery();
});

/**
 * 同步路由筛选参数。
 */
function syncRouteQuery() {
  data.searchForm.userId = normalizeQueryValue(route.query.userId);
  data.searchForm.userNo = normalizeQueryValue(route.query.userNo);
  data.searchForm.userNickName = normalizeQueryValue(route.query.userNickName);
  data.searchForm.orderNo = normalizeQueryValue(route.query.orderNo);
  data.searchForm.userMobile = normalizeQueryValue(route.query.userMobile);
  data.searchForm.spuNameKeyword = normalizeQueryValue(route.query.spuNameKeyword);
  data.status = normalizeQueryValue(route.query.afterSaleStatus) || "ALL";
  data.page.current = 1;
}

/**
 * 加载售后工作台数据。
 */
function loadData() {
  data.loading = true;
  const query = buildQueryParams();
  Promise.all([getAfterSalePage(query), getAfterSaleSummary(query)])
    .then(([pageResponse, summaryResponse]) => {
      data.tableData = pageResponse.data?.records || [];
      data.page.total = pageResponse.data?.total || 0;
      data.summary = Object.assign(
        {
          totalCount: 0,
          applyingCount: 0,
          rejectCount: 0,
          waitingRefundCount: 0,
          refundedCount: 0,
          refundAmount: 0,
        },
        summaryResponse.data || {}
      );
      data.loading = false;
    })
    .catch(() => {
      data.loading = false;
    });
}

/**
 * 组装查询参数。
 *
 * @returns {Object}
 */
function buildQueryParams() {
  return {
    current: data.page.current,
    size: data.page.size,
    afterSaleStatus: data.status,
    userId: trimValue(data.searchForm.userId),
    orderNo: trimValue(data.searchForm.orderNo),
    userNo: trimValue(data.searchForm.userNo),
    userNickName: trimValue(data.searchForm.userNickName),
    userMobile: trimValue(data.searchForm.userMobile),
    spuNameKeyword: trimValue(data.searchForm.spuNameKeyword),
    beginTime: data.dateRange?.[0] || null,
    endTime: data.dateRange?.[1] || null,
  };
}

/**
 * 点击快捷筛选。
 *
 * @param {string} nextStatus 售后状态
 */
function handleQuickFilter(nextStatus) {
  data.status = nextStatus;
  data.page.current = 1;
  loadData();
}

/**
 * 执行搜索。
 */
function handleSearch() {
  data.page.current = 1;
  loadData();
}

/**
 * 重置搜索表单。
 */
function resetSearch() {
  data.searchForm.orderNo = "";
  data.searchForm.userNo = "";
  data.searchForm.userNickName = "";
  data.searchForm.userMobile = "";
  data.searchForm.spuNameKeyword = "";
  data.dateRange = [];
  data.page.current = 1;
  loadData();
}

/**
 * 打开退款审核弹窗。
 *
 * @param {Object} row 售后订单项
 */
function openRefundAuditDialog(row) {
  data.refundAuditDialog.currentItem = row;
  data.refundAuditDialog.form = {
    id: row?.id || "",
    status: "3",
    refundAuditRemark: "",
  };
  data.refundAuditDialog.visible = true;
}

/**
 * 应用快捷审核备注。
 *
 * @param {string} templateText 模板内容
 */
function applyRefundAuditRemarkTemplate(templateText) {
  data.refundAuditDialog.form.refundAuditRemark = templateText || "";
}

/**
 * 关闭后重置退款审核表单。
 */
function resetRefundAuditDialog() {
  data.refundAuditDialog.currentItem = null;
  data.refundAuditDialog.submitting = false;
  data.refundAuditDialog.form = {
    id: "",
    status: "3",
    refundAuditRemark: "",
  };
}

/**
 * 提交退款审核。
 */
function submitRefundAudit() {
  const currentItem = data.refundAuditDialog.currentItem;
  if (!currentItem?.id) {
    proxy.$message.error("售后订单项不存在");
    return;
  }
  const refundAuditRemark = trimValue(data.refundAuditDialog.form.refundAuditRemark);
  if (!refundAuditRemark) {
    proxy.$message.warning("请输入审核备注");
    return;
  }
  data.refundAuditDialog.submitting = true;
  doOrderRefunds({
    id: currentItem.id,
    status: data.refundAuditDialog.form.status,
    refundAuditRemark,
  })
    .then(() => {
      proxy.$message.success("审核成功");
      data.refundAuditDialog.visible = false;
      loadData();
    })
    .catch(() => {
      data.refundAuditDialog.submitting = false;
    })
    .finally(() => {
      data.refundAuditDialog.submitting = false;
    });
}

/**
 * 跳转到订单页定位当前售后单。
 *
 * @param {Object} row 售后订单项
 */
function goOrderPage(row) {
  router.push({
    path: "/mall/orderinfo",
    query: {
      orderNo: row.orderNo,
      userId: row.userId,
      userNo: row.userNo || "",
      userNickName: row.userNickName || "",
      afterSaleStatus: row.isRefund === "1" ? "4" : row.status || "ALL",
    },
  });
}

/**
 * 构建会员主展示名称。
 *
 * @param {Object} row 行数据
 * @returns {string}
 */
function buildMemberName(row) {
  return row?.userNickName || row?.userRealName || "未命名会员";
}

/**
 * 构建售后工作台 SOP 卡片。
 *
 * @param {string} currentStatus 当前筛选状态
 * @param {Object} summary 售后汇总
 * @returns {Array<Object>}
 */
function buildServiceSopCards(currentStatus, summary) {
  const currentSummary = summary || {};
  if (currentStatus === "1") {
    return [
      {
        title: "当前处理重点",
        desc: `当前有 ${Number(currentSummary.applyingCount || 0)} 条退款申请待审核，优先保证审核时效。`,
        items: [
          "先核对支付金额、申请商品和用户填写的退款原因。",
          "再确认是否已发货、是否签收，避免把履约中订单直接判成可退。",
          "审核备注里要写明通过或拒绝的依据，方便后续二次沟通。",
        ],
      },
      {
        title: "审核核对项",
        desc: "退款申请中最容易漏掉的是物流状态和证据完整性。",
        items: [
          "质量问题或描述不符，优先要求补充图片、视频或聊天凭证。",
          "未发货订单可优先走退款；已发货或已签收订单要结合规则判断。",
          "同一会员短期多次申请退款时，顺手查看历史售后和备注记录。",
        ],
      },
      {
        title: "推荐沟通话术",
        desc: "先安抚，再给结果，最后给下一步预期。",
        items: [
          "您的退款申请已经收到，我这边正在核对订单和物流状态，会尽快给您结果。",
          "如果您方便，也可以补充一下问题说明或相关凭证，我会一起帮您核实处理。",
        ],
      },
    ];
  }
  if (currentStatus === "2") {
    return [
      {
        title: "当前处理重点",
        desc: `当前有 ${Number(currentSummary.rejectCount || 0)} 条退款已拒绝，重点是做好二次沟通解释。`,
        items: [
          "优先回看拒绝时写下的审核备注，避免客服口径前后不一致。",
          "解释拒绝原因时要对应订单状态、签收状态或售后规则依据。",
          "如果用户情绪较强，先给补充凭证或人工复核的出口，不要只重复结论。",
        ],
      },
      {
        title: "沟通注意点",
        desc: "被拒绝后的投诉通常来自“听不懂为什么被拒”。",
        items: [
          "不要只说不支持退款，要说清楚是因为什么规则不满足。",
          "能继续处理的，要明确告诉用户还差什么资料或下一步怎么做。",
          "无法直接退款时，可同步其他解决路径，例如补发、换货或继续人工核实。",
        ],
      },
      {
        title: "推荐沟通话术",
        desc: "用复核口吻沟通，比直接驳回更容易接受。",
        items: [
          "我这边已经帮您复核了订单情况，本次暂未通过退款，主要原因我给您说明一下。",
          "如果您还有补充情况或凭证，可以继续发给我，我再帮您做二次核实。",
        ],
      },
    ];
  }
  if (currentStatus === "3") {
    return [
      {
        title: "当前处理重点",
        desc: `当前有 ${Number(currentSummary.waitingRefundCount || 0)} 条退款等待回调，重点是跟进到账结果。`,
        items: [
          "确认后台已经发起原路退款，不要重复提交退款。",
          "统一对外说明到账时效以微信支付回写为准。",
          "如果超出预期仍未到账，及时记录异常并人工跟进。",
        ],
      },
      {
        title: "沟通注意点",
        desc: "这一阶段用户最关心的是“钱什么时候到账”。",
        items: [
          "先明确告诉用户退款已经发起，不是仍在审核中。",
          "说明退款会退回原支付账户，减少用户找错账户的情况。",
          "若用户频繁追问，可在备注里记录已沟通的到账预期。",
        ],
      },
      {
        title: "推荐沟通话术",
        desc: "重点降低用户不确定感。",
        items: [
          "这笔退款我们已经为您提交，资金会原路退回，请留意微信支付到账通知。",
          "目前系统正在等待退款结果回写，如有延迟我会继续帮您跟进。",
        ],
      },
    ];
  }
  if (currentStatus === "4") {
    return [
      {
        title: "当前处理重点",
        desc: `当前已退款 ${Number(currentSummary.refundedCount || 0)} 条，售后已完成但仍要做好体验修复。`,
        items: [
          "确认用户是否已到账，避免已退款仍反复咨询。",
          "记录退款核心原因，反推商品、履约或客服问题点。",
          "如果用户仍有购买意愿，可在问题解决后再引导重新下单。",
        ],
      },
      {
        title: "复盘关注点",
        desc: "已退款订单是最直接的损失样本，适合沉淀问题分类。",
        items: [
          "区分是商品问题、物流问题、描述问题还是服务问题导致退款。",
          "同类退款原因集中出现时，及时反馈给商品或运营侧。",
          "高价值会员退款后，建议额外做一次回访，避免直接流失。",
        ],
      },
      {
        title: "推荐沟通话术",
        desc: "语气以确认到账和修复体验为主。",
        items: [
          "这笔退款已经处理完成，您可以留意原支付账户的到账通知。",
          "很抱歉这次没有给您带来好的体验，如果后续还有需要，我会继续帮您跟进服务。",
        ],
      },
    ];
  }
  return [
    {
      title: "当前处理重点",
      desc: `当前共有 ${Number(currentSummary.totalCount || 0)} 条售后订单项，建议按“申请中 > 待回调 > 已拒绝 > 已退款”顺序处理。`,
      items: [
        "先消化待审核申请，避免售后超时。",
        "再跟进待回调退款，减少用户催问。",
        "最后复盘已拒绝和已退款订单，总结高频问题。",
      ],
    },
    {
      title: "统一核对口径",
      desc: "客服处理售后时，至少保持三项口径一致。",
      items: [
        "订单支付状态、物流状态和商品状态要一致。",
        "审核备注、页面状态和对客口径要一致。",
        "如有补充凭证或二次处理，要在后台留下明确跟进记录。",
      ],
    },
    {
      title: "推荐沟通话术",
      desc: "适合全部售后场景的通用开场。",
      items: [
        "您的售后申请我已经收到，我先帮您核对订单和处理进度，再给您明确结果。",
        "如果您有补充说明或凭证，也可以直接发给我，我会一起帮您核实处理。",
      ],
    },
  ];
}

/**
 * 售后状态文案。
 *
 * @param {Object} row 售后订单项
 * @returns {string}
 */
function buildAfterSaleStatusDesc(row) {
  if (row?.isRefund === "1") {
    return "已退款";
  }
  if (row?.status === "1") {
    return "退款申请中";
  }
  if (row?.status === "2") {
    return "退款已拒绝";
  }
  if (row?.status === "3") {
    return "等待退款回调";
  }
  return "无售后";
}

/**
 * 售后状态标签颜色。
 *
 * @param {Object} row 售后订单项
 * @returns {string}
 */
function buildAfterSaleTagType(row) {
  if (row?.isRefund === "1") {
    return "success";
  }
  if (row?.status === "1") {
    return "warning";
  }
  if (row?.status === "2") {
    return "danger";
  }
  if (row?.status === "3") {
    return "info";
  }
  return "";
}

/**
 * 订单状态展示。
 *
 * @param {Object} row 售后订单项
 * @returns {string}
 */
function formatOrderStatus(row) {
  if (row?.orderIsPay === "0" && !row?.orderStatus) {
    return "待付款";
  }
  if (row?.orderStatus === "1") {
    return "待发货";
  }
  if (row?.orderStatus === "2") {
    return "待收货";
  }
  if (row?.orderStatus === "3") {
    return "已完成";
  }
  if (row?.orderStatus === "5") {
    return "已关闭";
  }
  return "状态未知";
}

/**
 * 手机号脱敏。
 *
 * @param {string} mobile 手机号
 * @returns {string}
 */
function maskMobile(mobile) {
  if (!mobile) {
    return "未绑定";
  }
  if (mobile.length < 7) {
    return mobile;
  }
  return `${mobile.substring(0, 3)}****${mobile.substring(mobile.length - 4)}`;
}

/**
 * 时间格式化。
 *
 * @param {string} value 时间值
 * @returns {string}
 */
function formatDateTime(value) {
  if (!value) {
    return "暂无";
  }
  return String(value).replace("T", " ").substring(0, 19);
}

/**
 * 金额格式化。
 *
 * @param {number|string} value 金额
 * @returns {string}
 */
function formatCurrency(value) {
  return `￥${Number(value || 0).toFixed(2)}`;
}

/**
 * 路由参数标准化。
 *
 * @param {string|string[]} value 路由参数
 * @returns {string}
 */
function normalizeQueryValue(value) {
  if (Array.isArray(value)) {
    return value[0] || "";
  }
  return value ? String(value).trim() : "";
}

/**
 * 字符串去空格。
 *
 * @param {string} value 文本
 * @returns {string}
 */
function trimValue(value) {
  return value ? String(value).trim() : "";
}
</script>

<style lang="scss" scoped>
.after-sale-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.summary-panel {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
}

.summary-card {
  padding: 18px 20px;
  border: 1px solid #e8eef7;
  border-radius: 16px;
  background: linear-gradient(180deg, #ffffff 0%, #f7faff 100%);
  box-shadow: 0 8px 18px rgba(41, 103, 255, 0.06);
}

.summary-label {
  color: #6b7280;
  font-size: 13px;
}

.summary-value {
  margin-top: 10px;
  color: #1f2937;
  font-size: 28px;
  font-weight: 700;
  line-height: 1.2;
}

.summary-tip {
  margin-top: 8px;
  color: #909399;
  font-size: 12px;
}

.quick-filter-panel,
.sop-panel,
.search-panel,
.table-panel {
  padding: 16px 20px;
  border: 1px solid #e8eef7;
  border-radius: 16px;
  background: #fff;
}

.quick-filter-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.quick-filter-title,
.sop-panel-title,
.table-panel-title {
  color: #1f2937;
  font-size: 14px;
  font-weight: 600;
}

.quick-filter-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: flex-end;
}

.sop-panel {
  background: linear-gradient(135deg, #f9fbff 0%, #f1f6ff 100%);
}

.sop-panel-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 12px;
}

.sop-panel-tip {
  color: #6b7280;
  font-size: 12px;
}

.sop-card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 12px;
  margin-top: 14px;
}

.sop-card {
  padding: 14px 16px;
  border: 1px solid rgba(191, 211, 255, 0.8);
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.9);
}

.sop-card-title {
  color: #1f2937;
  font-size: 14px;
  font-weight: 700;
}

.sop-card-desc {
  margin-top: 8px;
  color: #5b6473;
  font-size: 12px;
  line-height: 1.7;
}

.sop-card-item {
  margin-top: 10px;
  color: #606266;
  font-size: 13px;
  line-height: 1.7;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}

.table-panel-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 16px;
}

.table-panel-tip {
  color: #6b7280;
  font-size: 12px;
}

.goods-cell,
.member-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.goods-image {
  width: 58px;
  height: 58px;
  border-radius: 10px;
  background: #f5f7fa;
}

.goods-meta,
.member-meta,
.order-cell {
  text-align: left;
}

.goods-name,
.member-name,
.audit-goods-name {
  color: #303133;
  font-weight: 600;
}

.goods-sub,
.member-sub,
.order-sub,
.remark-line,
.time-line,
.audit-goods-sub,
.audit-reason {
  margin-top: 4px;
  color: #606266;
  font-size: 12px;
  line-height: 1.6;
}

.order-no {
  color: #1f2937;
  font-weight: 600;
}

.amount-text,
.is-danger {
  color: #d4380d;
}

.is-primary {
  color: #2967ff;
}

.is-warning {
  color: #c17f00;
}

.is-success {
  color: #1f9d55;
}

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.audit-goods,
.audit-reason {
  width: 100%;
}

.audit-reason {
  padding: 10px 12px;
  border-radius: 8px;
  background: #f6f8fb;
}

.audit-check-list,
.audit-template-list {
  width: 100%;
}

.audit-check-item {
  margin-top: 6px;
  color: #606266;
  line-height: 1.7;
}

.audit-check-item:first-child {
  margin-top: 0;
}

.audit-template-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

@media screen and (max-width: 992px) {
  .quick-filter-panel,
  .table-panel-header,
  .sop-panel-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .quick-filter-list {
    justify-content: flex-start;
  }
}
</style>

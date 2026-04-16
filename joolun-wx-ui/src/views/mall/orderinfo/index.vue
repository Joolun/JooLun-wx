<!--
  Copyright (C) 2026
  All rights reserved, Designed By www.joolun.com
  注意：
  本软件为www.joolun.com开发研制，项目使用请保留此说明
-->

<template>
  <div class="app-container order-page">
    <div class="summary-panel">
      <div v-for="item in summaryCards" :key="item.label" class="summary-card">
        <div class="summary-label">{{ item.label }}</div>
        <div class="summary-value" :class="item.emphasisClass">{{ item.value }}</div>
        <div class="summary-tip">{{ item.tip }}</div>
      </div>
    </div>

    <div v-if="hasActiveMemberFilter" class="member-filter-panel">
      <div class="member-filter-info">
        <div class="member-filter-title">会员定向筛选</div>
        <div class="member-filter-text">当前仅查看 {{ activeMemberFilterText }} 的订单数据</div>
      </div>
      <div class="member-filter-actions">
        <el-button type="primary" plain size="small" @click="openCurrentMemberDetail">会员详情</el-button>
        <el-button size="small" @click="clearMemberFilter">查看全部订单</el-button>
      </div>
    </div>

    <div class="quick-filter-panel">
      <div class="quick-filter-title">售后快捷筛选</div>
      <div class="quick-filter-list">
        <el-button
          v-for="item in afterSaleQuickFilters"
          :key="item.value"
          :type="isAfterSaleQuickFilterActive(item.value) ? 'primary' : 'default'"
          plain
          size="small"
          @click="handleAfterSaleQuickFilter(item.value)"
        >
          {{ item.label }}
        </el-button>
      </div>
    </div>

    <el-tabs v-model="status" type="border-card" @tab-click="handleClickStatus">
      <el-tab-pane name="-1">
        <template #label>
          <span>全部订单</span>
        </template>
      </el-tab-pane>
      <el-tab-pane name="0">
        <template #label>
          <span>待付款</span>
        </template>
      </el-tab-pane>
      <el-tab-pane name="1">
        <template #label>
          <span>待发货</span>
        </template>
      </el-tab-pane>
      <el-tab-pane name="2">
        <template #label>
          <span>待收货</span>
        </template>
      </el-tab-pane>
      <el-tab-pane name="3">
        <template #label>
          <span>已完成</span>
        </template>
      </el-tab-pane>
      <el-tab-pane name="5">
        <template #label>
          <span>已取消</span>
        </template>
      </el-tab-pane>

      <avue-crud
        ref="crud"
        v-model="form"
        :page="page"
        :data="tableData"
        :permission="permissionList"
        :table-loading="tableLoading"
        :option="tableOption"
        @on-load="getPageF"
        @refresh-change="refreshChange"
        @sort-change="sortChange"
        @search-change="searchChange"
        @date-change="dateChange"
      >
        <template #userNickName="scope">
          <div class="member-cell">
            <el-avatar
              :src="resolveOrderImage(scope.row.userAvatarUrl)"
              icon="el-icon-user-solid"
            ></el-avatar>
            <div class="member-meta">
              <div class="member-name">{{ buildMemberName(scope.row) }}</div>
              <div class="member-sub">会员编号：{{ scope.row.userNo || "未生成" }}</div>
              <div class="member-sub">
                {{ scope.row.userRealName || "未填写真实姓名" }}
                <span class="member-divider">|</span>
                {{ maskMobile(scope.row.userMobile) }}
              </div>
              <el-button v-if="scope.row.userId" type="primary" link class="member-link-btn" @click="openMemberDetail(scope.row)">
                查看会员
              </el-button>
            </div>
          </div>
        </template>

        <template #status="scope">
          <div class="status-cell">
            <div class="grid-content">
              订单状态：
              <el-tag
                :type="scope.row.status == '2' || scope.row.status == '3' || scope.row.status == '4' ? 'success' : 'danger'"
                effect="dark"
                size="small"
              >
                {{ scope.row.statusDesc }}
              </el-tag>
            </div>
            <div class="grid-content">
              支付状态：
              <el-tag
                :type="scope.row.isPay == '1' ? 'success' : 'danger'"
                effect="dark"
                size="small"
              >
                {{ scope.row.isPay == "1" ? "已支付" : "未支付" }}
              </el-tag>
            </div>
            <div class="grid-content" v-if="buildOrderAfterSaleStatus(scope.row)">
              售后状态：
              <el-tag :type="buildOrderAfterSaleStatus(scope.row).type" size="small">
                {{ buildOrderAfterSaleStatus(scope.row).label }}
              </el-tag>
            </div>
          </div>
        </template>

        <template #orderNo-form>
          <el-table :data="[form]" border style="width: 100%">
            <el-table-column align="center" prop="orderNo" label="订单单号">
            </el-table-column>
            <el-table-column align="left" prop="salesPrice" label="订单金额" width="220">
              <template #default="scope">
                <div>订单金额：￥{{ scope.row.salesPrice }}</div>
                <div>运费金额：+ ￥{{ scope.row.freightPrice }}</div>
                <div class="price-highlight">支付金额：￥{{ scope.row.paymentPrice }}</div>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="paymentType" label="订单状态">
              <template #default="scope">
                <div class="status-cell">
                  <div class="grid-content">
                    订单状态：
                    <el-tag
                      :type="scope.row.status == '2' || scope.row.status == '3' || scope.row.status == '4' ? 'success' : 'danger'"
                      effect="dark"
                      size="small"
                    >
                      {{ scope.row.statusDesc }}
                    </el-tag>
                  </div>
                  <div class="grid-content">
                    支付状态：
                    <el-tag
                      :type="scope.row.isPay == '1' ? 'success' : 'danger'"
                      effect="dark"
                      size="small"
                    >
                      {{ scope.row.isPay == "1" ? "已支付" : "未支付" }}
                    </el-tag>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="orderTime" label="订单时间" width="250">
              <template #default="scope">
                <div>创建时间：{{ scope.row.createTime }}</div>
                <div v-if="scope.row.paymentTime">付款时间：{{ scope.row.paymentTime }}</div>
                <div v-if="scope.row.deliveryTime">发货时间：{{ scope.row.deliveryTime }}</div>
                <div v-if="scope.row.receiverTime">收货时间：{{ scope.row.receiverTime }}</div>
                <div v-if="scope.row.closingTime">成交时间：{{ scope.row.closingTime }}</div>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="transactionId" label="支付流水号">
            </el-table-column>
            <el-table-column align="center" prop="userMessage" label="买家留言">
            </el-table-column>
          </el-table>
        </template>

        <template #listOrderItem-form>
          <el-table :data="form.listOrderItem || []" border style="width: 100%; margin-top: -10px">
            <el-table-column align="center" prop="picUrl" label="图片" width="120">
              <template #default="scope">
                <img :src="resolveOrderImage(scope.row.picUrl)" width="100" height="100" />
              </template>
            </el-table-column>
            <el-table-column align="center" prop="spuName" label="商品名">
            </el-table-column>
            <el-table-column align="center" prop="salesPrice" label="商品单价" width="100">
              <template #default="scope"> ￥{{ scope.row.salesPrice }} </template>
            </el-table-column>
            <el-table-column align="center" prop="quantity" label="数量" width="60">
            </el-table-column>
            <el-table-column align="center" prop="salesPriceTotal" label="商品总价" width="110">
              <template #default="scope">
                ￥{{ (scope.row.salesPrice * scope.row.quantity).toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column align="center" prop="freightPrice" label="运费金额" width="100">
              <template #default="scope">
                ￥{{ scope.row.freightPrice }}
              </template>
            </el-table-column>
            <el-table-column align="center" prop="paymentPrice" label="支付金额" width="90">
              <template #default="scope">
                ￥{{ scope.row.paymentPrice }}
              </template>
            </el-table-column>
            <el-table-column align="center" prop="refundAmount" label="售后金额" width="120">
              <template #default="scope">
                {{ buildRefundAmountText(scope.row) }}
              </template>
            </el-table-column>
            <el-table-column align="center" prop="afterSaleStatusDesc" label="售后状态" width="140">
              <template #default="scope">
                <el-tag
                  :type="buildAfterSaleTagType(scope.row)"
                  effect="plain"
                  size="small"
                >
                  {{ buildAfterSaleStatusDesc(scope.row) }}
                </el-tag>
                <div v-if="scope.row.refundReason" class="detail-after-sale-meta">
                  申请原因：{{ scope.row.refundReason }}
                </div>
                <div v-if="scope.row.refundAuditRemark" class="detail-after-sale-meta">
                  审核备注：{{ scope.row.refundAuditRemark }}
                </div>
                <div v-if="scope.row.status == '1' && form.isPay == '1'" class="detail-audit-action">
                  <el-button type="danger" plain size="mini" @click="openRefundAuditDialog(scope.row)">
                    退款审核
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </template>

        <template #userId-form>
          <el-table :data="[form.userInfo || {}]" border style="width: 100%">
            <el-table-column align="center" prop="nickName" label="会员信息" width="220">
              <template #default="scope">
                <el-avatar icon="el-icon-user-solid" :src="resolveOrderImage(scope.row.avatarUrl)"></el-avatar>
                <div class="detail-member-name">{{ scope.row.nickName || "未授权昵称" }}</div>
                <div class="detail-member-sub">会员编号：{{ scope.row.userNo || "未生成" }}</div>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="realName" label="真实姓名">
            </el-table-column>
            <el-table-column align="center" prop="mobile" label="手机号">
              <template #default="scope">
                {{ maskMobile(scope.row.mobile) }}
              </template>
            </el-table-column>
            <el-table-column align="center" prop="sex" label="性别">
              <template #default="scope">
                {{ formatSex(scope.row.sex) }}
              </template>
            </el-table-column>
            <el-table-column align="center" prop="status" label="会员状态">
              <template #default="scope">
                <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'" effect="plain" size="small">
                  {{ formatMemberStatus(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="consumeAmount" label="累计消费">
              <template #default="scope">
                ￥{{ Number(scope.row.consumeAmount || 0).toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="120">
              <template #default="scope">
                <el-button v-if="scope.row.id" type="primary" link @click="openMemberDetail(scope.row)">
                  查看会员
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>

        <template #orderLogistics-form>
          <el-table :data="[form.orderLogistics || {}]" border style="width: 100%">
            <el-table-column align="center" prop="userName" label="姓名">
            </el-table-column>
            <el-table-column align="center" prop="telNum" label="电话">
            </el-table-column>
            <el-table-column align="center" prop="address" label="地址">
            </el-table-column>
            <el-table-column align="center" prop="logisticsNo" label="快递单号">
            </el-table-column>
            <el-table-column align="center" prop="logisticsDesc" label="快递公司">
            </el-table-column>
            <el-table-column align="center" prop="statusDesc" label="快递状态">
            </el-table-column>
          </el-table>
        </template>

        <template #afterSaleInfo-form>
          <div class="after-sale-panel">
            <div v-if="currentOrderServiceAdvice" class="service-advice-panel">
              <div class="service-advice-header">
                <div>
                  <div class="service-advice-title">客服处理建议</div>
                  <div class="service-advice-sub-title">{{ currentOrderServiceAdvice.scene }}</div>
                </div>
                <el-tag size="small" effect="light" :type="currentOrderServiceAdvice.tagType">
                  {{ currentOrderServiceAdvice.tagText }}
                </el-tag>
              </div>
              <div class="service-advice-action">
                <span class="service-advice-label">当前建议动作</span>
                <span class="service-advice-action-text">{{ currentOrderServiceAdvice.primaryAction }}</span>
              </div>
              <div class="service-advice-grid">
                <div class="service-advice-card">
                  <div class="service-advice-card-title">沟通重点</div>
                  <div
                    v-for="item in currentOrderServiceAdvice.focusList"
                    :key="`focus-${item}`"
                    class="service-advice-item"
                  >
                    {{ item }}
                  </div>
                </div>
                <div class="service-advice-card">
                  <div class="service-advice-card-title">推荐话术</div>
                  <div
                    v-for="item in currentOrderServiceAdvice.phraseList"
                    :key="`phrase-${item}`"
                    class="service-advice-item"
                  >
                    {{ item }}
                  </div>
                </div>
              </div>
            </div>
            <div v-if="afterSaleRecordList.length" class="after-sale-record-list">
              <div v-for="item in afterSaleRecordList" :key="item.id" class="after-sale-record-card">
                <div class="after-sale-record-header">
                  <div class="after-sale-record-title">{{ item.spuName || "未命名商品" }}</div>
                  <el-tag :type="buildAfterSaleTagType(item)" effect="plain" size="small">
                    {{ buildAfterSaleStatusDesc(item) }}
                  </el-tag>
                </div>
                <div v-if="item.specInfo" class="after-sale-record-line">规格：{{ item.specInfo }}</div>
                <div class="after-sale-record-line">售后金额：{{ formatCurrency(item.paymentPrice) }}</div>
                <div class="after-sale-record-line">申请时间：{{ formatDateTime(item.refundApplyTime) }}</div>
                <div class="after-sale-record-line">申请原因：{{ buildRefundReasonText(item) }}</div>
                <div v-if="item.refundAuditTime" class="after-sale-record-line">审核时间：{{ formatDateTime(item.refundAuditTime) }}</div>
                <div v-if="item.refundAuditRemark" class="after-sale-record-line">审核备注：{{ item.refundAuditRemark }}</div>
                <div v-if="item.refundSuccessTime" class="after-sale-record-line">退款完成：{{ formatDateTime(item.refundSuccessTime) }}</div>
                <div v-if="item.status == '1' && form.isPay == '1'" class="after-sale-record-actions">
                  <el-button type="danger" plain size="small" @click="openRefundAuditDialog(item)">
                    退款审核
                  </el-button>
                </div>
              </div>
            </div>
            <el-empty
              v-if="afterSaleTimeline.length === 0"
              description="当前订单暂无售后记录"
            ></el-empty>
            <el-timeline v-else>
              <el-timeline-item
                v-for="(item, index) in afterSaleTimeline"
                :key="index"
                :timestamp="item.time"
                :type="item.type"
                placement="top"
              >
                <div class="timeline-title">{{ item.title }}</div>
                <div class="timeline-desc">{{ item.desc }}</div>
              </el-timeline-item>
            </el-timeline>
          </div>
        </template>

        <template #orderTimeline-form>
          <div class="after-sale-panel">
            <el-timeline v-if="orderTimeline.length > 0">
              <el-timeline-item
                v-for="(item, index) in orderTimeline"
                :key="index"
                :timestamp="item.time"
                :type="item.type"
                placement="top"
              >
                <div class="timeline-title">{{ item.title }}</div>
                <div class="timeline-desc">{{ item.desc }}</div>
              </el-timeline-item>
            </el-timeline>
            <el-empty v-else description="当前订单暂无可展示的订单轨迹"></el-empty>
          </div>
        </template>

        <template #name="scope">
          <el-row
            v-for="(item, index) in scope.row.listOrderItem"
            :key="index"
            :gutter="10"
            class="goods-row"
          >
            <el-col :span="4" class="goods-image-wrap">
              <el-image :src="resolveOrderImage(item.picUrl)" class="goods-image" />
            </el-col>
            <el-col :span="12" class="goods-info">
              <div class="spu-name">{{ item.spuName }}</div>
              <div v-if="item.specInfo" class="spec-info">
                {{ item.specInfo }}
              </div>
            </el-col>
            <el-col :span="8">
              <div class="grid-content price-highlight">
                ￥{{ item.paymentPrice }}
              </div>
              <div class="grid-content">×{{ item.quantity }}件</div>
              <div class="grid-content" v-if="item.status == '2'">
                <el-tag type="danger">拒绝退款</el-tag>
              </div>
              <div class="grid-content" v-if="item.status == '3'">同意退款</div>
              <div class="grid-content" v-if="item.isRefund == '1'">已退款</div>
              <div class="grid-content" v-if="item.refundReason">原因：{{ item.refundReason }}</div>
              <div class="grid-content" v-if="item.refundAuditRemark">备注：{{ item.refundAuditRemark }}</div>
              <el-button
                v-if="item.status == '1' && scope.row.isPay == '1'"
                class="grid-content"
                type="danger"
                plain
                size="small"
                @click="openRefundAuditDialog(item)"
              >
                退款审核
              </el-button>
            </el-col>
          </el-row>
        </template>

        <template #createTime="scope">
          <div class="time-cell">
            <div class="grid-content">{{ scope.row.createTime }}</div>
            <div class="grid-content order-no">{{ scope.row.orderNo }}</div>
          </div>
        </template>

        <template #salesPrice="scope">
          <div class="amount-cell">
            <div class="grid-content">订单金额：￥{{ scope.row.salesPrice }}</div>
            <div class="grid-content">运费金额：+ ￥{{ scope.row.freightPrice }}</div>
            <div class="grid-content price-highlight">支付金额：￥{{ scope.row.paymentPrice }}</div>
          </div>
        </template>

        <template #menu="scope">
          <el-button
            v-if="checkPermi(['mall:orderinfo:get'])"
            icon="el-icon-view"
            type="primary"
            link
            @click="openView(scope.row, scope.index)"
          >
            订单详情
          </el-button>
          <el-button
            v-if="checkPermi(['mall:orderinfo:edit']) && scope.row.status == '1'"
            icon="el-icon-position"
            type="primary"
            link
            @click="showDialogLogistics(scope.row)"
          >
            发货
          </el-button>
          <el-button
            v-if="checkPermi(['mall:orderinfo:edit']) && scope.row.isPay == '0' && !scope.row.status"
            icon="el-icon-delete"
            type="danger"
            link
            @click="orderCancelF(scope.row)"
          >
            取消
          </el-button>
        </template>
      </avue-crud>
    </el-tabs>

    <el-dialog title="发货" v-model="dialogLogistics" width="30%">
      <avue-form
        v-model="logisticsForm"
        :option="logisticsOption"
        @submit="delivery"
      ></avue-form>
    </el-dialog>

    <el-dialog v-model="refundAuditDialog.visible" title="退款审核" width="520px" @closed="resetRefundAuditDialog">
      <el-form label-width="88px">
        <el-form-item label="商品信息">
          <div class="refund-audit-goods">
            <div class="refund-audit-goods-name">{{ refundAuditDialog.currentItem?.spuName || "未命名商品" }}</div>
            <div v-if="refundAuditDialog.currentItem?.specInfo" class="refund-audit-goods-spec">
              {{ refundAuditDialog.currentItem.specInfo }}
            </div>
            <div class="refund-audit-goods-amount">
              退款金额：{{ formatCurrency(refundAuditDialog.currentItem?.paymentPrice) }}
            </div>
          </div>
        </el-form-item>
        <el-form-item label="申请原因">
          <div class="refund-audit-apply-reason">{{ buildRefundReasonText(refundAuditDialog.currentItem) }}</div>
        </el-form-item>
        <el-form-item v-if="refundAuditChecklist.length" label="审核核对">
          <div class="refund-audit-check-list">
            <div v-for="item in refundAuditChecklist" :key="item" class="refund-audit-check-item">
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
            :placeholder="refundAuditDialog.form.status === '3' ? '请输入审核通过说明，例如：已核实问题，发起原路退款' : '请输入拒绝原因，例如：订单已签收且无质量问题'"
          />
        </el-form-item>
        <el-form-item v-if="refundAuditRemarkTemplates.length" label="快捷备注">
          <div class="refund-audit-template-list">
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
        <span class="dialog-footer">
          <el-button @click="refundAuditDialog.visible = false">取消</el-button>
          <el-button type="primary" :loading="refundAuditDialog.submitting" @click="submitRefundAudit">提交审核</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="OrderInfo">
import { checkPermi } from "@/utils/permission";
import { doOrderRefunds, getObj, getPage, getSummary, orderCancel, putObj } from "@/api/mall/orderinfo";
import { tableOption } from "@/const/crud/mall/orderinfo";
import { resolveMallImageUrl } from "@/utils/mall-image";

const crud = ref(null);
const { proxy } = getCurrentInstance();
const route = useRoute();
const router = useRouter();

/**
 * 统一处理订单页中的商品图、会员头像等商城图片地址。
 *
 * @param {string} imageUrl 图片地址
 * @returns {string}
 */
function resolveOrderImage(imageUrl) {
  return resolveMallImageUrl(imageUrl);
}

/**
 * 订单页状态。
 * 搜索条件、当前分页、发货表单和订单详情都集中维护，方便刷新时复用。
 */
const data = reactive({
  form: {},
  date: [],
  status: "-1",
  tableData: [],
  page: {
    total: 0,
    currentPage: 1,
    pageSize: 20,
    ascs: [],
    descs: "create_time",
  },
  paramsSearch: {},
  routeFilter: {
    userId: "",
    userNo: "",
    userNickName: "",
    orderNo: "",
    status: "",
    afterSaleStatus: "",
  },
  tableLoading: false,
  summary: {
    totalCount: 0,
    paidCount: 0,
    waitDeliveryCount: 0,
    processingAfterSaleCount: 0,
    refundedCount: 0,
    paymentAmount: 0,
  },
  dialogLogistics: false,
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
  logisticsForm: {
    row: {},
    logistics: null,
    logisticsNo: null,
    userName: null,
    telNum: null,
    address: null,
  },
  logisticsOption: {
    emptyBtn: false,
    card: true,
    group: [
      {
        icon: "el-icon-user",
        label: "收货人信息",
        prop: "group1",
        column: [
          {
            label: "收货人名字",
            prop: "userName",
            span: 24,
            readonly: true,
          },
          {
            label: "电话号码",
            prop: "telNum",
            span: 24,
            readonly: true,
          },
          {
            label: "收货地址",
            prop: "address",
            type: "textarea",
            span: 24,
            readonly: true,
          },
        ],
      },
      {
        icon: "el-icon-truck",
        label: "快递信息",
        prop: "group2",
        column: [
          {
            label: "快递公司",
            prop: "logistics",
            dicUrl: "/orderlogistics/dict/LOGISTICS",
            span: 24,
            type: "select",
            rules: [
              {
                required: true,
                message: "请选择快递公司",
                trigger: "blur",
              },
            ],
          },
          {
            label: "快递单号",
            prop: "logisticsNo",
            span: 24,
            rules: [
              {
                required: true,
                message: "请输入快递单号",
                trigger: "blur",
              },
            ],
          },
        ],
      },
    ],
  },
});

const { form, page, tableData, tableLoading, status, dialogLogistics, logisticsForm, logisticsOption, refundAuditDialog } = toRefs(data);

/**
 * 当前是否处于会员定向筛选模式。
 * 当从会员详情页跳转到订单页时，会自动把 mall_user.id 带过来。
 */
const hasActiveMemberFilter = computed(() => !!data.routeFilter.userId);

/**
 * 顶部会员筛选提示文案。
 * 优先显示昵称和会员编号，避免后台只看到一串主键不易识别。
 */
const activeMemberFilterText = computed(() => {
  const routeFilter = data.routeFilter || {};
  const nameText = routeFilter.userNickName || routeFilter.userNo || routeFilter.userId || "当前会员";
  const suffixTexts = [];
  if (routeFilter.orderNo) {
    suffixTexts.push(`定位订单 ${routeFilter.orderNo}`);
  }
  if (routeFilter.status === "0") {
    suffixTexts.push("仅看待付款");
  } else if (routeFilter.status === "1") {
    suffixTexts.push("仅看待发货");
  } else if (routeFilter.status === "2") {
    suffixTexts.push("仅看待收货");
  }
  if (routeFilter.afterSaleStatus === "ALL") {
    suffixTexts.push("仅看售后订单");
  }
  return suffixTexts.length > 0 ? `${nameText}，${suffixTexts.join("，")}` : nameText;
});

/**
 * 顶部运营概览。
 * 统计口径基于当前筛选结果和当前页订单，便于后台客服、运营快速判断订单分布。
 */
const summaryCards = computed(() => {
  const currentRows = data.tableData || [];
  const summary = data.summary || {};

  return [
    {
      label: "筛选结果",
      value: `${Number(summary.totalCount || data.page.total || 0)} 单`,
      tip: "当前查询条件命中的订单总数",
      emphasisClass: "is-primary",
    },
    {
      label: "当前页订单",
      value: `${currentRows.length} 单`,
      tip: "当前分页实际加载的订单数",
      emphasisClass: "",
    },
    {
      label: "已支付订单",
      value: `${Number(summary.paidCount || 0)} 单`,
      tip: "当前筛选结果里支付成功的订单数",
      emphasisClass: "is-success",
    },
    {
      label: "待发货订单",
      value: `${Number(summary.waitDeliveryCount || 0)} 单`,
      tip: "当前筛选结果里等待发货的订单数",
      emphasisClass: "is-warning",
    },
    {
      label: "售后处理中",
      value: `${Number(summary.processingAfterSaleCount || 0)} 单`,
      tip: "当前筛选结果里退款申请中或退款待回调的订单数",
      emphasisClass: "is-danger",
    },
    {
      label: "已退款订单",
      value: `${Number(summary.refundedCount || 0)} 单`,
      tip: "当前筛选结果里已完成退款的订单数",
      emphasisClass: "is-success",
    },
    {
      label: "筛选支付额",
      value: `¥${Number(summary.paymentAmount || 0).toFixed(2)}`,
      tip: "当前筛选结果的支付金额汇总",
      emphasisClass: "is-primary",
    },
  ];
});

/**
 * 售后快捷筛选配置。
 * 用于客服快速切换到售后订单视角，不必每次展开搜索区重新选状态。
 */
const afterSaleQuickFilters = [
  {
    label: "全部订单",
    value: "",
  },
  {
    label: "仅看售后订单",
    value: "ALL",
  },
  {
    label: "退款申请中",
    value: "1",
  },
  {
    label: "拒绝退款",
    value: "2",
  },
  {
    label: "退款待回调",
    value: "3",
  },
  {
    label: "已退款",
    value: "4",
  },
];

/**
 * 当前订单的售后记录列表。
 * 只保留已经进入售后链路的订单项，用于详情页集中展示申请、审核和退款完成信息。
 */
const afterSaleRecordList = computed(() => {
  return (data.form?.listOrderItem || []).filter((item) => buildAfterSaleStatusDesc(item) !== "无售后");
});

/**
 * 当前订单客服处理建议。
 * 订单详情页除了展示数据，还要给客服一个“下一步应该怎么做”的结论，减少现场判断成本。
 */
const currentOrderServiceAdvice = computed(() => buildCurrentOrderServiceAdvice(data.form));

const AFTER_SALE_OPERATE_TYPES = ["REFUND_APPLY", "REFUND_APPROVE", "REFUND_REJECT", "REFUND_SUCCESS"];

/**
 * 订单详情里的售后轨迹。
 * 优先展示后端返回的真实售后日志；没有日志时再回退到订单项状态拼装结果。
 */
const afterSaleTimeline = computed(() => {
  const logTimeline = buildOperateLogTimeline(true);
  if (logTimeline.length > 0) {
    return logTimeline;
  }
  const orderInfo = data.form || {};
  const listOrderItem = orderInfo.listOrderItem || [];
  return listOrderItem
    .map((item) => {
      const statusDesc = buildAfterSaleStatusDesc(item);
      if (statusDesc === "无售后") {
        return null;
      }
      return {
        time: formatDateTime(item.updateTime || orderInfo.updateTime || orderInfo.createTime),
        title: `${item.spuName} · ${statusDesc}`,
        desc: buildAfterSaleTimelineDesc(item),
        type: buildAfterSaleTimelineType(item),
      };
    })
    .filter((item) => item)
    .sort((a, b) => String(b.time || "").localeCompare(String(a.time || "")));
});

/**
 * 订单主轨迹。
 * 优先展示后端返回的真实订单日志；没有日志时再回退到原有字段拼装结果。
 */
const orderTimeline = computed(() => {
  const logTimeline = buildOperateLogTimeline(false);
  if (logTimeline.length > 0) {
    return logTimeline;
  }
  const orderInfo = data.form || {};
  const timeline = [];

  if (orderInfo.createTime) {
    timeline.push({
      time: formatDateTime(orderInfo.createTime),
      title: "订单创建",
      desc: `订单已提交，订单编号 ${orderInfo.orderNo || "未知"}，买家留言：${orderInfo.userMessage || "无"}`,
      type: "primary",
    });
  }

  if (orderInfo.paymentTime) {
    timeline.push({
      time: formatDateTime(orderInfo.paymentTime),
      title: "订单支付",
      desc: `支付成功，支付金额 ${formatCurrency(orderInfo.paymentPrice)}，支付流水号：${orderInfo.transactionId || "未记录"}`,
      type: "success",
    });
  }

  if (orderInfo.deliveryTime) {
    timeline.push({
      time: formatDateTime(orderInfo.deliveryTime),
      title: "商家发货",
      desc: buildDeliveryTimelineDesc(orderInfo),
      type: "warning",
    });
  }

  if (orderInfo.receiverTime) {
    timeline.push({
      time: formatDateTime(orderInfo.receiverTime),
      title: "用户收货",
      desc: "订单已确认收货，可继续跟进评价或售后场景",
      type: "success",
    });
  }

  if (orderInfo.closingTime) {
    timeline.push({
      time: formatDateTime(orderInfo.closingTime),
      title: "订单完成",
      desc: "订单已完成成交闭环",
      type: "success",
    });
  }

  if (orderInfo.status === "5") {
    timeline.push({
      time: formatDateTime(orderInfo.updateTime || orderInfo.createTime),
      title: orderInfo.isPay === "1" ? "订单关闭" : "订单取消",
      desc: orderInfo.isPay === "1" ? "订单因退款或售后流程被关闭" : "订单未支付，后台已取消",
      type: "info",
    });
  }

  afterSaleTimeline.value.forEach((item) => {
    timeline.push({
      time: item.time,
      title: `售后处理 · ${item.title}`,
      desc: item.desc,
      type: item.type,
    });
  });

  return timeline
    .filter((item) => item.time)
    .sort((a, b) => String(b.time || "").localeCompare(String(a.time || "")));
});

const permissionList = computed(() => {
  return {
    addBtn: false,
    delBtn: false,
    editBtn: false,
    viewBtn: false,
  };
});

/**
 * 退款审核核对项。
 * 审核前固定提醒客服核对订单、支付、物流和证据，避免备注写了但关键判断没做。
 */
const refundAuditChecklist = computed(() => {
  const currentItem = data.refundAuditDialog.currentItem;
  if (!currentItem) {
    return [];
  }
  const checklist = [
    "核对订单支付金额、退款金额和申请商品是否一致。",
    "核对当前物流/签收状态，确认是否已经发货或签收。",
    `结合申请原因“${buildRefundReasonText(currentItem)}”确认是否满足售后规则。`,
  ];
  if (currentItem.refundAuditRemark) {
    checklist.push(`历史审核备注：${currentItem.refundAuditRemark}`);
  } else {
    checklist.push("如果拒绝退款，备注里要写清规则依据和下一步处理方案。");
  }
  return checklist;
});

/**
 * 退款审核快捷备注。
 * 根据审核结果动态给出常用模板，客服点一下即可带入，再按具体情况补充。
 */
const refundAuditRemarkTemplates = computed(() => {
  const currentItem = data.refundAuditDialog.currentItem;
  if (!currentItem) {
    return [];
  }
  if (data.refundAuditDialog.form.status === "3") {
    return [
      "已核实订单情况，符合退款条件，已为您发起原路退款，请留意支付账户到账通知。",
      "申请原因已确认，后台已同意退款，微信退款回写完成后会自动到账。",
      "已核对商品和支付信息，退款已提交原路退回，到账时效以微信支付通知为准。",
    ];
  }
  return [
    "已核对订单状态与售后规则，当前暂不满足退款条件，如有补充凭证可继续联系客服处理。",
    "订单已进入履约阶段，当前无法直接退款，请先补充问题说明或商品异常凭证。",
    "已结合订单签收和商品状态复核，本次暂不支持退款，如需进一步处理请联系人工客服补充情况。",
  ];
});

syncRouteFilter();

watch(
  () => route.fullPath,
  () => {
    const changed = syncRouteFilter();
    if (changed && data.tableData.length >= 0) {
      data.page.currentPage = 1;
      getPageF(data.page);
    }
  }
);

/**
 * 退款审核。
 *
 * @param {Object} orderItem 订单项
 */
function doOrderRefundsF(orderItem) {
  openRefundAuditDialog(orderItem);
}

/**
 * 给当前订单生成客服建议。
 *
 * @param {Object} order 订单详情
 * @returns {Object|null}
 */
function buildCurrentOrderServiceAdvice(order) {
  if (!order?.id) {
    return null;
  }
  const afterSaleStatus = buildOrderAfterSaleStatus(order);
  if (order.isPay !== "1") {
    return {
      scene: "当前订单尚未完成支付，不需要进入退款闭环。",
      tagType: "info",
      tagText: "未支付订单",
      primaryAction: "先确认用户是否仍需购买；如果无需购买，直接说明不会扣款即可。",
      focusList: [
        "优先向用户说明当前订单未支付，系统不会实际扣款。",
        "如果用户是支付失败或放弃购买，引导重新下单，不必走退款流程。",
        "记录未支付原因，方便后续复盘支付流失点。",
      ],
      phraseList: [
        "这笔订单当前还未支付，系统不会扣款，因此也不需要再申请退款。",
        "如果您仍想购买，我建议您重新下单；如果暂时不需要，可以直接忽略当前订单。",
      ],
    };
  }
  if (afterSaleStatus?.label === "退款申请中") {
    return {
      scene: "用户已发起退款申请，客服需要尽快给出审核结论。",
      tagType: "warning",
      tagText: "待审核",
      primaryAction: "先核对支付、物流、签收和申请原因，再明确同意或拒绝退款。",
      focusList: [
        "核对申请商品、退款金额和支付金额是否一致。",
        "判断当前是未发货、已发货还是已签收，避免误判退款条件。",
        "如果涉及质量或描述不符，先确认是否有图片、视频或聊天证据。",
      ],
      phraseList: [
        "您的退款申请已经收到，我这边正在核对订单和物流状态，会尽快给您明确结果。",
        "如果您方便，也可以补充一下问题说明或相关凭证，我会一并帮您核实处理。",
      ],
    };
  }
  if (afterSaleStatus?.label === "退款待回调") {
    return {
      scene: "后台已同意退款，当前重点是减少用户重复催问。",
      tagType: "primary",
      tagText: "待回调",
      primaryAction: "明确告知已发起原路退款，并持续关注微信退款回调结果。",
      focusList: [
        "确认后台已提交退款，不要重复发起退款操作。",
        "向用户说明退款会原路退回，到账时间以微信支付回写为准。",
        "如果长时间未回写，及时登记异常并继续跟进。",
      ],
      phraseList: [
        "这笔退款我们已经为您提交，资金会原路退回到原支付账户，请留意微信支付到账通知。",
        "目前系统正在等待退款结果回写，如有延迟我会继续帮您跟进。",
      ],
    };
  }
  if (afterSaleStatus?.label === "退款被拒绝") {
    return {
      scene: "退款申请已被拒绝，下一步重点是二次沟通和解释依据。",
      tagType: "danger",
      tagText: "已拒绝",
      primaryAction: "回看审核备注和订单履约状态，准备清晰解释拒绝原因并给替代方案。",
      focusList: [
        "先确认拒绝依据是否写清楚，避免只给结论不给原因。",
        "结合发货、签收、商品状态说明为什么暂不满足退款条件。",
        "必要时提供补充凭证、继续申诉或联系人工客服的入口。",
      ],
      phraseList: [
        "我这边帮您复核了一下，这次暂未通过退款，主要原因已经写在审核备注里，我给您再解释一下。",
        "如果您还有补充情况或凭证，可以继续发给我，我再帮您二次核实。",
      ],
    };
  }
  if (afterSaleStatus?.label === "已退款") {
    return {
      scene: "订单售后已完成，后续重点转为到账确认和体验修复。",
      tagType: "success",
      tagText: "已退款",
      primaryAction: "确认用户是否已到账，并记录不满原因，避免影响后续复购。",
      focusList: [
        "提醒用户留意原支付账户到账通知，必要时说明到账时效。",
        "记录本次退款的核心原因，用于后续商品、履约或客服改进。",
        "如果用户仍有购买意愿，可在问题解决后再引导重新下单。",
      ],
      phraseList: [
        "这笔退款已经处理完成，您可以留意原支付账户的到账通知。",
        "如果后续您还愿意继续购买，我这边也可以再帮您确认合适的商品和处理方式。",
      ],
    };
  }
  if (order.status === "1") {
    return {
      scene: "订单已支付待发货，当前重点是发货进度沟通。",
      tagType: "warning",
      tagText: "待发货",
      primaryAction: "优先同步发货节奏和库存情况，减少催单和履约投诉。",
      focusList: [
        "确认库存和预计发货时间，避免承诺不准。",
        "如果可能延迟，提前说明原因和处理方案。",
        "如用户急需且无法按时发货，及时给出取消或补偿建议。",
      ],
      phraseList: [
        "您的订单已经支付成功，当前正在安排发货，我这边同步您预计发货时间。",
        "如果发货时间有变化，我会第一时间告知您，避免您空等。",
      ],
    };
  }
  if (order.status === "2") {
    return {
      scene: "订单已发货待收货，客服应优先处理签收和物流咨询。",
      tagType: "primary",
      tagText: "待收货",
      primaryAction: "围绕物流轨迹、签收提醒和售后规则做解释，提前降低纠纷。",
      focusList: [
        "先同步物流轨迹，确认是否存在中转、派送异常或拒收风险。",
        "提醒用户签收时核对商品，如有问题及时走售后入口。",
        "如用户想中途退款，先说明发货后退款的处理规则。",
      ],
      phraseList: [
        "订单已经发出，您可以先关注物流更新；签收时如发现问题，我会继续协助您处理。",
        "如果您收到商品后有异常，请第一时间联系我，我们会按售后流程尽快处理。",
      ],
    };
  }
  return {
    scene: "当前订单没有明确售后风险，适合以服务确认和体验收集为主。",
    tagType: "success",
    tagText: "常规跟进",
    primaryAction: "确认订单体验是否正常，若已完成交易可顺带收集评价和问题反馈。",
    focusList: [
      "确认用户是否已正常收货、使用是否顺利。",
      "如果没有问题，可引导用户完成评价或收藏复购。",
      "如果用户提出新的商品问题，再及时切回售后处理链路。",
    ],
    phraseList: [
      "这笔订单目前状态正常，想确认一下您这边收货和使用是否都顺利。",
      "如果商品还有任何问题，您随时联系我，我这边会继续跟进处理。",
    ],
  };
}

/**
 * 切换订单状态页签。
 *
 * @param {Object} pane 当前页签
 */
function handleClickStatus(pane) {
  data.status = pane.paneName;
  data.page.currentPage = 1;
  getPageF(data.page);
}

/**
 * 售后快捷筛选是否命中。
 *
 * @param {string} value 快捷筛选值
 * @returns {boolean}
 */
function isAfterSaleQuickFilterActive(value) {
  const currentValue = data.paramsSearch.afterSaleStatus || "";
  if (value === "ALL") {
    return currentValue === "ALL";
  }
  return currentValue === value;
}

/**
 * 应用售后快捷筛选。
 *
 * @param {string} value 快捷筛选值
 */
function handleAfterSaleQuickFilter(value) {
  if (value === "ALL") {
    data.paramsSearch = Object.assign({}, data.paramsSearch, {
      afterSaleStatus: "ALL",
    });
  } else {
    data.paramsSearch = Object.assign({}, data.paramsSearch, {
      afterSaleStatus: value || undefined,
    });
  }
  data.page.currentPage = 1;
  getPageF(data.page);
}

/**
 * 打开会员详情页。
 *
 * @param {Object} userInfo 会员信息
 */
function openMemberDetail(userInfo) {
  const mallUserId = userInfo?.userId || userInfo?.id;
  if (!mallUserId) {
    return;
  }
  router.push({
    path: "/mall/malluser-detail/index",
    query: {
      id: mallUserId,
    },
  });
}

/**
 * 从顶部定向筛选条跳回当前会员详情页。
 */
function openCurrentMemberDetail() {
  openMemberDetail({
    id: data.routeFilter.userId,
  });
}

/**
 * 清空会员定向筛选，恢复全量订单视角。
 * 这里只清理由会员详情页带过来的路由参数，不影响其他筛选项。
 */
function clearMemberFilter() {
  const query = Object.assign({}, route.query);
  delete query.userId;
  delete query.userNo;
  delete query.userNickName;
  delete query.orderNo;
  delete query.status;
  delete query.afterSaleStatus;
  router.replace({
    path: route.path,
    query,
  });
}

/**
 * 打开订单详情。
 * 详情页单独拉取一次完整订单，保证会员、物流、订单项数据完整。
 *
 * @param {Object} row 行数据
 * @param {number} index 行索引
 */
function openView(row, index) {
  data.tableLoading = true;
  getObj(row.id)
    .then((response) => {
      data.form = response.data || {};
      crud.value.rowView(data.form, index);
      data.tableLoading = false;
    })
    .catch(() => {
      data.tableLoading = false;
    });
}

/**
 * 取消未支付订单。
 *
 * @param {Object} row 行数据
 */
function orderCancelF(row) {
  proxy
    .$confirm("是否确认取消此订单", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      return orderCancel(row.id);
    })
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "取消成功",
        type: "success",
      });
      getPageF(data.page);
    })
    .catch(() => {});
}

/**
 * 打开发货弹窗。
 * 列表页为了减轻查询负担不再附带完整物流对象，因此这里按订单主键补拉详情。
 *
 * @param {Object} row 行数据
 */
function showDialogLogistics(row) {
  data.tableLoading = true;
  getObj(row.id)
    .then((response) => {
      const detail = response.data || {};
      const orderLogistics = detail.orderLogistics || {};
      data.logisticsForm = {
        row: detail,
        logistics: orderLogistics.logistics || null,
        logisticsNo: orderLogistics.logisticsNo || null,
        userName: orderLogistics.userName || null,
        telNum: orderLogistics.telNum || null,
        address: orderLogistics.address || null,
      };
      data.dialogLogistics = true;
      data.tableLoading = false;
    })
    .catch(() => {
      data.tableLoading = false;
    });
}

/**
 * 提交发货。
 * 这里只提交订单状态和物流字段，避免把列表中的附加展示字段一并带回后台。
 *
 * @param {Object} formModel 发货表单
 * @param {Function} done Avue 回调
 */
function delivery(formModel, done) {
  const row = data.logisticsForm.row || {};
  putObj({
    id: row.id,
    logisticsId: row.logisticsId,
    status: "2",
    logistics: formModel.logistics,
    logisticsNo: formModel.logisticsNo,
  })
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "发货成功",
        type: "success",
      });
      done();
      data.dialogLogistics = false;
      refreshOrderPage(row.id);
    })
    .catch(() => {
      done();
    });
}

/**
 * 日期筛选。
 *
 * @param {Object} val 日期信息
 */
function dateChange(val) {
  data.date = val.value ? val.value : [];
  getPageF(data.page);
}

/**
 * 搜索回调。
 *
 * @param {Object} params 搜索参数
 * @param {Function} done Avue 回调
 */
function searchChange(params, done) {
  data.paramsSearch = proxy.filterForm(params);
  data.page.currentPage = 1;
  getPageF(data.page, params);
  done();
}

/**
 * 排序回调。
 *
 * @param {Object} val 排序信息
 */
function sortChange(val) {
  const prop = val.prop ? val.prop.replace(/([A-Z])/g, "_$1").toLowerCase() : "";
  if (val.order === "ascending") {
    data.page.descs = [];
    data.page.ascs = prop;
  } else if (val.order === "descending") {
    data.page.ascs = [];
    data.page.descs = prop;
  } else {
    data.page.ascs = [];
    data.page.descs = [];
  }
  getPageF(data.page);
}

/**
 * 加载订单分页。
 *
 * @param {Object} pageInfo 分页对象
 * @param {Object} params 临时参数
 */
function getPageF(pageInfo, params) {
  data.tableLoading = true;
  const afterSaleStatus = resolveAfterSaleStatus(params, data.paramsSearch);
  const query = Object.assign(
    {
      current: pageInfo.currentPage,
      size: pageInfo.pageSize,
      descs: data.page.descs,
      ascs: data.page.ascs,
      status: data.status !== "-1" ? data.status : null,
      beginTime: data.date[0],
      endTime: data.date[1],
      afterSaleStatus,
    },
    params,
    data.paramsSearch
  );
  Promise.all([getPage(query), getSummary(query)])
    .then(([pageResponse, summaryResponse]) => {
      data.tableData = pageResponse.data?.records || [];
      data.page.total = pageResponse.data?.total || 0;
      data.page.currentPage = pageInfo.currentPage;
      data.page.pageSize = pageInfo.pageSize;
      data.summary = Object.assign(
        {
          totalCount: 0,
          paidCount: 0,
          waitDeliveryCount: 0,
          processingAfterSaleCount: 0,
          refundedCount: 0,
          paymentAmount: 0,
        },
        summaryResponse.data || {}
      );
      data.tableLoading = false;
    })
    .catch(() => {
      data.tableLoading = false;
    });
}

/**
 * 刷新订单页，并在当前详情已打开时同步回填最新订单详情。
 *
 * @param {string} detailId 需要回刷的订单主键
 */
function refreshOrderPage(detailId) {
  getPageF(data.page);
  if (detailId) {
    refreshCurrentOrderDetail(detailId);
  }
}

/**
 * 单独刷新当前订单详情。
 *
 * @param {string} detailId 订单主键
 */
function refreshCurrentOrderDetail(detailId) {
  getObj(detailId).then((response) => {
    data.form = response.data || {};
  });
}

/**
 * 刷新列表。
 */
function refreshChange() {
  getPageF(data.page);
}

/**
 * 汇总订单维度的售后状态。
 * 一个订单可能包含多个订单项，这里按优先级聚合成客服更容易识别的单个状态。
 *
 * @param {Object} order 订单
 * @returns {{label: string, type: string}|null}
 */
function buildOrderAfterSaleStatus(order) {
  const listOrderItem = order?.listOrderItem || [];
  if (listOrderItem.some((item) => item.status === "1" && item.isRefund !== "1")) {
    return {
      label: "退款申请中",
      type: "warning",
    };
  }
  if (listOrderItem.some((item) => item.status === "3" && item.isRefund !== "1")) {
    return {
      label: "退款待回调",
      type: "info",
    };
  }
  if (listOrderItem.some((item) => item.status === "2" && item.isRefund !== "1")) {
    return {
      label: "退款被拒绝",
      type: "danger",
    };
  }
  if (listOrderItem.some((item) => item.isRefund === "1")) {
    return {
      label: "已退款",
      type: "success",
    };
  }
  return null;
}

/**
 * 生成会员主展示名称。
 *
 * @param {Object} row 订单行
 * @returns {string}
 */
function buildMemberName(row) {
  return row.userNickName || row.userRealName || "未命名会员";
}

/**
 * 手机号脱敏展示。
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
 * 性别格式化。
 *
 * @param {string} sex 性别
 * @returns {string}
 */
function formatSex(sex) {
  if (sex === "1") {
    return "男";
  }
  if (sex === "2") {
    return "女";
  }
  return "未知";
}

/**
 * 会员状态格式化。
 *
 * @param {string} statusValue 会员状态
 * @returns {string}
 */
function formatMemberStatus(statusValue) {
  return statusValue === "1" ? "禁用" : "正常";
}

/**
 * 构建售后状态文案。
 *
 * @param {Object} orderItem 订单项
 * @returns {string}
 */
function buildAfterSaleStatusDesc(orderItem) {
  if (orderItem?.isRefund === "1") {
    return "已退款";
  }
  if (orderItem?.status === "1") {
    return "退款申请中";
  }
  if (orderItem?.status === "2") {
    return "退款被拒绝";
  }
  if (orderItem?.status === "3") {
    return "退款待回调";
  }
  return "无售后";
}

/**
 * 退款申请原因展示。
 *
 * @param {Object} orderItem 订单项
 * @returns {string}
 */
function buildRefundReasonText(orderItem) {
  return orderItem?.refundReason || "未填写申请原因";
}

/**
 * 构建售后标签类型。
 *
 * @param {Object} orderItem 订单项
 * @returns {string}
 */
function buildAfterSaleTagType(orderItem) {
  if (orderItem?.isRefund === "1") {
    return "success";
  }
  if (orderItem?.status === "1") {
    return "warning";
  }
  if (orderItem?.status === "2") {
    return "danger";
  }
  if (orderItem?.status === "3") {
    return "info";
  }
  return "";
}

/**
 * 构建售后轨迹描述。
 *
 * @param {Object} orderItem 订单项
 * @returns {string}
 */
function buildAfterSaleTimelineDesc(orderItem) {
  const specInfo = orderItem?.specInfo ? `，规格：${orderItem.specInfo}` : "";
  if (orderItem?.isRefund === "1") {
    return `系统已完成退款回写，退款金额 ￥${orderItem.paymentPrice}${specInfo}，申请原因：${buildRefundReasonText(orderItem)}${orderItem?.refundAuditRemark ? `，审核备注：${orderItem.refundAuditRemark}` : ""}`;
  }
  if (orderItem?.status === "1") {
    return `用户已发起退款申请，待后台审核，申请金额 ￥${orderItem.paymentPrice}${specInfo}，申请原因：${buildRefundReasonText(orderItem)}`;
  }
  if (orderItem?.status === "2") {
    return `后台已拒绝退款申请，请结合订单沟通记录继续跟进${specInfo}${orderItem?.refundAuditRemark ? `，审核备注：${orderItem.refundAuditRemark}` : ""}`;
  }
  if (orderItem?.status === "3") {
    return `后台已同意退款，当前等待微信退款回调完成${specInfo}${orderItem?.refundAuditRemark ? `，审核备注：${orderItem.refundAuditRemark}` : ""}`;
  }
  return "";
}

/**
 * 售后金额展示。
 *
 * @param {Object} orderItem 订单项
 * @returns {string}
 */
function buildRefundAmountText(orderItem) {
  if (!orderItem) {
    return "￥0.00";
  }
  return formatCurrency(orderItem.paymentPrice);
}

/**
 * 构建售后时间线颜色。
 *
 * @param {Object} orderItem 订单项
 * @returns {string}
 */
function buildAfterSaleTimelineType(orderItem) {
  if (orderItem?.isRefund === "1") {
    return "success";
  }
  if (orderItem?.status === "1") {
    return "warning";
  }
  if (orderItem?.status === "2") {
    return "danger";
  }
  return "primary";
}

/**
 * 打开退款审核弹窗。
 *
 * @param {Object} orderItem 订单项
 */
function openRefundAuditDialog(orderItem) {
  data.refundAuditDialog.currentItem = orderItem || null;
  data.refundAuditDialog.form = {
    id: orderItem?.id || "",
    status: "3",
    refundAuditRemark: "",
  };
  data.refundAuditDialog.visible = true;
}

/**
 * 重置退款审核弹窗。
 */
function resetRefundAuditDialog() {
  data.refundAuditDialog.submitting = false;
  data.refundAuditDialog.currentItem = null;
  data.refundAuditDialog.form = {
    id: "",
    status: "3",
    refundAuditRemark: "",
  };
}

/**
 * 应用退款审核快捷备注。
 *
 * @param {string} templateText 模板内容
 */
function applyRefundAuditRemarkTemplate(templateText) {
  data.refundAuditDialog.form.refundAuditRemark = templateText || "";
}

/**
 * 提交退款审核。
 */
function submitRefundAudit() {
  const currentItem = data.refundAuditDialog.currentItem;
  if (!currentItem?.id) {
    proxy.$message.error("退款订单项不存在");
    return;
  }
  if (!data.refundAuditDialog.form.refundAuditRemark.trim()) {
    proxy.$message.warning("请输入审核备注");
    return;
  }
  const currentDetailId = data.form?.id === currentItem.orderId ? data.form.id : null;
  data.refundAuditDialog.submitting = true;
  doOrderRefunds({
    id: currentItem.id,
    status: data.refundAuditDialog.form.status,
    refundAuditRemark: data.refundAuditDialog.form.refundAuditRemark.trim(),
  })
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "审核成功",
        type: "success",
      });
      data.refundAuditDialog.visible = false;
      refreshOrderPage(currentDetailId);
    })
    .catch(() => {
      data.refundAuditDialog.submitting = false;
    })
    .finally(() => {
      data.refundAuditDialog.submitting = false;
    });
}

/**
 * 格式化时间。
 *
 * @param {string} value 时间值
 * @returns {string}
 */
function formatDateTime(value) {
  if (!value) {
    return "时间未知";
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
 * 构建订单/售后日志时间线。
 *
 * @param {boolean} onlyAfterSale 是否只取售后日志
 * @returns {Array}
 */
function buildOperateLogTimeline(onlyAfterSale) {
  const operateLogList = data.form?.orderOperateLogList || [];
  return operateLogList
    .filter((item) => isAfterSaleOperateType(item?.operateType) === onlyAfterSale)
    .map((item) => ({
      time: formatDateTime(item?.createTime),
      title: buildOperateLogTitle(item),
      desc: buildOperateLogDesc(item),
      type: buildOperateLogType(item?.operateType),
    }))
    .filter((item) => item.time)
    .sort((a, b) => String(b.time || "").localeCompare(String(a.time || "")));
}

/**
 * 判断是否为售后类日志。
 *
 * @param {string} operateType 操作类型
 * @returns {boolean}
 */
function isAfterSaleOperateType(operateType) {
  return AFTER_SALE_OPERATE_TYPES.includes(operateType);
}

/**
 * 构建日志标题。
 *
 * @param {Object} operateLog 操作日志
 * @returns {string}
 */
function buildOperateLogTitle(operateLog) {
  const orderItem = findOrderItemById(operateLog?.orderItemId);
  if (orderItem?.spuName) {
    return `${orderItem.spuName} · ${operateLog?.operateTitle || "订单操作"}`;
  }
  return operateLog?.operateTitle || "订单操作";
}

/**
 * 构建日志描述。
 *
 * @param {Object} operateLog 操作日志
 * @returns {string}
 */
function buildOperateLogDesc(operateLog) {
  const orderItem = findOrderItemById(operateLog?.orderItemId);
  const itemPrefix = orderItem?.spuName
    ? `商品：${orderItem.spuName}${orderItem?.specInfo ? `，规格：${orderItem.specInfo}` : ""}。`
    : "";
  const operatorPrefix = operateLog?.operatorName ? `操作人：${operateLog.operatorName}。` : "";
  return `${itemPrefix}${operatorPrefix}${operateLog?.operateContent || "暂无操作说明"}`;
}

/**
 * 按订单项主键查找订单项。
 *
 * @param {string} orderItemId 订单项ID
 * @returns {Object|null}
 */
function findOrderItemById(orderItemId) {
  return (data.form?.listOrderItem || []).find((item) => item.id === orderItemId) || null;
}

/**
 * 构建日志节点颜色。
 *
 * @param {string} operateType 操作类型
 * @returns {string}
 */
function buildOperateLogType(operateType) {
  if (operateType === "PAY_SUCCESS" || operateType === "RECEIVE" || operateType === "REFUND_SUCCESS") {
    return "success";
  }
  if (operateType === "DELIVERY" || operateType === "REFUND_APPROVE") {
    return "warning";
  }
  if (operateType === "REFUND_REJECT") {
    return "danger";
  }
  if (operateType === "ADMIN_CANCEL" || operateType === "USER_CANCEL") {
    return "info";
  }
  return "primary";
}

/**
 * 生成发货节点描述。
 *
 * @param {Object} orderInfo 订单详情
 * @returns {string}
 */
function buildDeliveryTimelineDesc(orderInfo) {
  const orderLogistics = orderInfo?.orderLogistics || {};
  const logisticsName = orderLogistics.logisticsDesc || "待补充快递公司";
  const logisticsNo = orderLogistics.logisticsNo || "待补充快递单号";
  const logisticsStatus = orderLogistics.statusDesc ? `，当前物流状态：${orderLogistics.statusDesc}` : "";
  return `${logisticsName}，单号：${logisticsNo}${logisticsStatus}`;
}

/**
 * 统一处理售后筛选参数。
 * “仅看售后订单”属于前端快捷模式，这里折算成“退款申请中”到“已退款”的并集。
 *
 * @param {Object} params 临时查询参数
 * @param {Object} paramsSearch 常驻查询参数
 * @returns {string|null}
 */
function resolveAfterSaleStatus(params, paramsSearch) {
  const tempValue = params?.afterSaleStatus;
  const persistentValue = paramsSearch?.afterSaleStatus;
  const finalValue = tempValue !== undefined ? tempValue : persistentValue;
  if (!finalValue) {
    return null;
  }
  return finalValue === "ALL" ? "ALL" : finalValue;
}

/**
 * 将路由里的会员筛选参数同步到页面状态。
 * 这样从会员详情页跳进来时，不需要用户再手工输入会员编号或订单号。
 *
 * @returns {boolean} 路由筛选是否发生变化
 */
function syncRouteFilter() {
  const nextRouteFilter = {
    userId: normalizeRouteQueryValue(route.query.userId),
    userNo: normalizeRouteQueryValue(route.query.userNo),
    userNickName: normalizeRouteQueryValue(route.query.userNickName),
    orderNo: normalizeRouteQueryValue(route.query.orderNo),
    status: normalizeRouteQueryValue(route.query.status),
    afterSaleStatus: normalizeRouteQueryValue(route.query.afterSaleStatus),
  };
  const previousRouteFilter = data.routeFilter || {};
  const changed =
    previousRouteFilter.userId !== nextRouteFilter.userId ||
    previousRouteFilter.userNo !== nextRouteFilter.userNo ||
    previousRouteFilter.userNickName !== nextRouteFilter.userNickName ||
    previousRouteFilter.orderNo !== nextRouteFilter.orderNo ||
    previousRouteFilter.status !== nextRouteFilter.status ||
    previousRouteFilter.afterSaleStatus !== nextRouteFilter.afterSaleStatus;

  if (!changed) {
    return false;
  }

  const nextParamsSearch = Object.assign({}, data.paramsSearch);
  if (nextRouteFilter.userId) {
    nextParamsSearch.userId = nextRouteFilter.userId;
  } else {
    delete nextParamsSearch.userId;
  }
  if (nextRouteFilter.orderNo) {
    nextParamsSearch.orderNo = nextRouteFilter.orderNo;
  } else {
    delete nextParamsSearch.orderNo;
  }
  if (nextRouteFilter.afterSaleStatus) {
    nextParamsSearch.afterSaleStatus = nextRouteFilter.afterSaleStatus;
  } else {
    delete nextParamsSearch.afterSaleStatus;
  }

  data.routeFilter = nextRouteFilter;
  data.paramsSearch = nextParamsSearch;
  if (nextRouteFilter.status) {
    data.status = nextRouteFilter.status;
  }
  return true;
}

/**
 * 规范化路由查询参数。
 *
 * @param {string|string[]} value 路由参数
 * @returns {string}
 */
function normalizeRouteQueryValue(value) {
  if (Array.isArray(value)) {
    return value[0] || "";
  }
  return value ? String(value).trim() : "";
}
</script>

<style lang="scss" scoped>
.order-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.member-filter-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 16px 20px;
  border: 1px solid #d9e6ff;
  border-radius: 16px;
  background: linear-gradient(135deg, #f8fbff 0%, #eef4ff 100%);
}

.member-filter-title {
  color: #1f2937;
  font-size: 14px;
  font-weight: 600;
}

.member-filter-text {
  margin-top: 6px;
  color: #5b6473;
  font-size: 13px;
}

.member-filter-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.quick-filter-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 16px 20px;
  border: 1px solid #e8eef7;
  border-radius: 16px;
  background: #fff;
}

.quick-filter-title {
  color: #303133;
  font-size: 14px;
  font-weight: 600;
  white-space: nowrap;
}

.quick-filter-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: flex-end;
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

.is-primary {
  color: #2967ff;
}

.is-success {
  color: #1f9d55;
}

.is-warning {
  color: #c17f00;
}

.is-danger {
  color: #d4380d;
}

.member-cell {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  text-align: left;
}

.member-meta {
  margin-left: 12px;
}

.member-name,
.detail-member-name {
  color: #303133;
  font-weight: 600;
}

.member-sub,
.detail-member-sub {
  margin-top: 4px;
  color: #909399;
  font-size: 12px;
}

.member-link-btn {
  margin-top: 4px;
  padding: 0;
}

.member-divider {
  margin: 0 6px;
}

.status-cell,
.amount-cell,
.time-cell {
  text-align: left;
}

.goods-row {
  border: 1px solid #eaeaea;
  padding: 5px 0;
  display: flex;
  align-items: center;
}

.goods-image-wrap {
  display: flex;
  align-items: center;
}

.goods-image {
  width: 60px;
  height: 60px;
}

.goods-info {
  text-align: left;
}

.spu-name {
  font-size: 13px;
}

.spec-info {
  margin-top: 10px;
  font-size: 12px;
  color: #7b7b7b;
}

.price-highlight {
  color: #d4380d;
}

.after-sale-panel {
  padding: 10px 0;
}

.service-advice-panel {
  margin-bottom: 16px;
  padding: 16px 18px;
  border: 1px solid #dbe7ff;
  border-radius: 14px;
  background: linear-gradient(135deg, #f8fbff 0%, #eff5ff 100%);
}

.service-advice-header,
.service-advice-action {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.service-advice-title,
.service-advice-card-title {
  color: #1f2937;
  font-size: 14px;
  font-weight: 700;
}

.service-advice-sub-title {
  margin-top: 6px;
  color: #5b6473;
  font-size: 12px;
  line-height: 1.6;
}

.service-advice-action {
  margin-top: 14px;
  padding: 12px 14px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.82);
}

.service-advice-label {
  color: #6b7280;
  font-size: 12px;
  white-space: nowrap;
}

.service-advice-action-text {
  color: #1f2937;
  font-size: 13px;
  font-weight: 600;
  line-height: 1.7;
  text-align: right;
}

.service-advice-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 12px;
  margin-top: 14px;
}

.service-advice-card {
  padding: 14px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.9);
}

.service-advice-item {
  margin-top: 10px;
  color: #606266;
  font-size: 13px;
  line-height: 1.7;
}

.after-sale-record-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 12px;
  margin-bottom: 16px;
}

.after-sale-record-card {
  padding: 14px 16px;
  border: 1px solid #e5ecf7;
  border-radius: 12px;
  background: #f8fbff;
}

.after-sale-record-header,
.after-sale-record-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.after-sale-record-title,
.refund-audit-goods-name {
  color: #303133;
  font-weight: 600;
}

.after-sale-record-line,
.refund-audit-goods-spec,
.refund-audit-goods-amount,
.refund-audit-apply-reason,
.detail-after-sale-meta {
  margin-top: 6px;
  color: #606266;
  line-height: 1.7;
  text-align: left;
}

.after-sale-record-actions {
  margin-top: 10px;
  justify-content: flex-end;
}

.refund-audit-goods,
.refund-audit-apply-reason {
  width: 100%;
}

.refund-audit-apply-reason {
  padding: 10px 12px;
  border-radius: 8px;
  background: #f6f8fb;
}

.refund-audit-check-list,
.refund-audit-template-list {
  width: 100%;
}

.refund-audit-check-item {
  margin-top: 6px;
  color: #606266;
  line-height: 1.7;
}

.refund-audit-check-item:first-child {
  margin-top: 0;
}

.refund-audit-template-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.timeline-title {
  color: #303133;
  font-weight: 600;
}

.timeline-desc {
  margin-top: 4px;
  color: #606266;
  line-height: 1.7;
}

.detail-audit-action {
  margin-top: 8px;
}

.order-no {
  color: #909399;
  font-size: 12px;
}

.avue-crud :deep(.el-card__body) {
  padding: 0 !important;
}

@media screen and (max-width: 992px) {
  .member-filter-panel,
  .quick-filter-panel {
    flex-direction: column;
    align-items: flex-start;
  }

  .service-advice-header,
  .service-advice-action {
    flex-direction: column;
    align-items: flex-start;
  }

  .service-advice-action-text {
    text-align: left;
  }

  .member-filter-actions,
  .quick-filter-list {
    justify-content: flex-start;
  }
}
</style>

<!--
  Copyright (C) 2026
  All rights reserved, Designed By www.joolun.com
  注意：
  本软件为www.joolun.com开发研制，项目使用请保留此说明
-->

<template>
  <div class="mall-user-detail-page" v-loading="loading">
    <div class="page-header">
      <div>
        <div class="page-title">会员详情</div>
        <div class="page-sub-title">查看商城会员档案、运营记录、最近订单、售后记录和收货地址</div>
      </div>
      <div class="page-actions">
        <el-button @click="goBack">返回列表</el-button>
        <el-button v-if="detail.id" type="primary" @click="goOrderList()">查看会员订单</el-button>
        <el-button v-if="detail.id" type="success" plain @click="goAfterSalePage()">售后工作台</el-button>
        <el-button type="primary" plain @click="reloadDetail">刷新详情</el-button>
      </div>
    </div>

    <div class="detail-overview">
      <div class="detail-profile-card">
        <el-avatar :src="resolveMallUserImage(detail.avatarUrl)" icon="el-icon-user-solid" :size="76"></el-avatar>
        <div class="detail-profile-main">
          <div class="detail-profile-name">{{ detail.nickName || "微信用户" }}</div>
          <div class="detail-profile-sub">会员编号：{{ detail.userNo || "未生成" }}</div>
          <div class="detail-profile-sub">商城用户ID：{{ detail.id || "未生成" }}</div>
          <div class="profile-tag-row">
            <el-tag size="small" effect="light" :type="memberSegmentTag.type">
              {{ memberSegmentTag.label }}
            </el-tag>
            <el-tag size="small" effect="light" :type="activityLevelTag.type">
              {{ activityLevelTag.label }}
            </el-tag>
            <el-tag size="small" effect="plain" :type="valueLevelTag.type">
              {{ valueLevelTag.label }}
            </el-tag>
            <el-tag v-for="item in buildUserTags(detail)" :key="item" size="small" effect="plain">
              {{ item }}
            </el-tag>
          </div>
          <div class="detail-profile-sub">
            {{ detail.realName || "未填写真实姓名" }}
            <span class="detail-divider">|</span>
            {{ formatSex(detail.sex) }}
            <span class="detail-divider">|</span>
            {{ maskMobile(detail.mobile) }}
          </div>
          <div class="detail-profile-sub">
            会员身份：{{ formatMemberIdentity(detail.memberFlag) }}
            <span class="detail-divider">|</span>
            入会时间：{{ formatDateTime(detail.memberTime) }}
            <span class="detail-divider">|</span>
            账号状态：{{ detail.status === "1" ? "禁用" : "正常" }}
          </div>
          <div class="detail-profile-sub">
            注册来源：{{ formatRegisterSource(detail.registerSource) }}
            <span class="detail-divider">|</span>
            最近下单：{{ formatDateTime(detail.lastOrderTime) }}
          </div>
        </div>
      </div>

      <div class="detail-stat-grid">
        <div class="detail-stat-card">
          <div class="detail-stat-label">累计下单</div>
          <div class="detail-stat-value">{{ Number(detail.orderCount || 0) }}</div>
        </div>
        <div class="detail-stat-card">
          <div class="detail-stat-label">累计支付</div>
          <div class="detail-stat-value">{{ Number(detail.consumeCount || 0) }}</div>
        </div>
        <div class="detail-stat-card">
          <div class="detail-stat-label">累计退款</div>
          <div class="detail-stat-value">{{ Number(detail.refundCount || 0) }}</div>
        </div>
        <div class="detail-stat-card">
          <div class="detail-stat-label">累计消费</div>
          <div class="detail-stat-value amount-text">¥{{ Number(detail.consumeAmount || 0).toFixed(2) }}</div>
        </div>
        <div class="detail-stat-card">
          <div class="detail-stat-label">待付款订单</div>
          <div class="detail-stat-value">{{ Number(detail.waitPayOrderCount || 0) }}</div>
        </div>
        <div class="detail-stat-card">
          <div class="detail-stat-label">待发货订单</div>
          <div class="detail-stat-value">{{ Number(detail.waitDeliveryOrderCount || 0) }}</div>
        </div>
        <div class="detail-stat-card">
          <div class="detail-stat-label">待收货订单</div>
          <div class="detail-stat-value">{{ Number(detail.waitReceiveOrderCount || 0) }}</div>
        </div>
        <div class="detail-stat-card">
          <div class="detail-stat-label">收货地址数</div>
          <div class="detail-stat-value">{{ Number(detail.addressCount || 0) }}</div>
        </div>
      </div>

      <div class="detail-insight-grid">
        <div class="detail-insight-card">
          <div class="detail-insight-label">最近活跃</div>
          <div class="detail-insight-value">{{ formatDateTime(detail.recentActiveTime) }}</div>
        </div>
        <div class="detail-insight-card">
          <div class="detail-insight-label">入会时长</div>
          <div class="detail-insight-value">{{ formatDayCount(detail.memberAgeDays) }}</div>
        </div>
        <div class="detail-insight-card">
          <div class="detail-insight-label">最近支付</div>
          <div class="detail-insight-value">{{ formatDateTime(detail.recentPaymentTime) }}</div>
        </div>
        <div class="detail-insight-card">
          <div class="detail-insight-label">近30天下单</div>
          <div class="detail-insight-value">{{ Number(detail.thirtyDayOrderCount || 0) }} 单</div>
        </div>
        <div class="detail-insight-card">
          <div class="detail-insight-label">近30天消费</div>
          <div class="detail-insight-value amount-text">¥{{ Number(detail.thirtyDayConsumeAmount || 0).toFixed(2) }}</div>
        </div>
        <div class="detail-insight-card">
          <div class="detail-insight-label">售后件数</div>
          <div class="detail-insight-value">{{ Number(detail.afterSaleCount || 0) }} 件</div>
        </div>
        <div class="detail-insight-card">
          <div class="detail-insight-label">已退款金额</div>
          <div class="detail-insight-value amount-text">¥{{ Number(detail.refundedAmount || 0).toFixed(2) }}</div>
        </div>
        <div class="detail-insight-card">
          <div class="detail-insight-label">购物车SKU</div>
          <div class="detail-insight-value">{{ Number(detail.cartSkuCount || 0) }} 行</div>
        </div>
        <div class="detail-insight-card">
          <div class="detail-insight-label">购物车件数</div>
          <div class="detail-insight-value">{{ Number(detail.cartQuantity || 0) }} 件</div>
        </div>
      </div>

      <div class="detail-address-banner">
        <div class="detail-address-label">默认收货地址</div>
        <div class="detail-address-text">{{ detail.defaultAddressText || "暂无收货地址" }}</div>
      </div>

      <div class="detail-service-banner">
        <div class="detail-service-banner-header">
          <div>
            <div class="detail-address-label">最近一次客服跟进</div>
            <div class="detail-service-banner-title">{{ latestServiceSummary.title }}</div>
          </div>
          <div class="detail-service-banner-actions">
            <el-button
              v-if="latestServiceActionInfo.orderNo"
              type="primary"
              size="small"
              @click="openServiceOrder(latestServiceActionInfo)"
            >
              定位订单
            </el-button>
            <el-button
              v-if="latestServiceActionInfo.orderNo && latestServiceActionInfo.afterSaleStatus"
              type="success"
              plain
              size="small"
              @click="openServiceAfterSale(latestServiceActionInfo)"
            >
              定位售后
            </el-button>
            <el-button type="primary" plain size="small" @click="activeTab = 'operate'">查看运营记录</el-button>
            <el-button
              v-if="recentAfterSaleList.length"
              type="success"
              plain
              size="small"
              @click="activeTab = 'aftersale'"
            >
              查看售后记录
            </el-button>
          </div>
        </div>
        <div class="detail-service-banner-desc">{{ latestServiceSummary.desc }}</div>
      </div>

      <div class="detail-shortcut-panel">
        <div class="detail-shortcut-title">订单快捷入口</div>
        <div class="detail-shortcut-actions">
          <el-button type="primary" plain size="small" @click="goOrderList()">全部订单</el-button>
          <el-button type="warning" plain size="small" @click="goOrderList({ status: '0' })">待付款</el-button>
          <el-button type="danger" plain size="small" @click="goOrderList({ status: '1' })">待发货</el-button>
          <el-button type="primary" plain size="small" @click="goOrderList({ status: '2' })">待收货</el-button>
          <el-button type="success" plain size="small" @click="goOrderList({ afterSaleStatus: 'ALL' })">售后中</el-button>
          <el-button type="success" size="small" @click="goAfterSalePage()">售后工作台</el-button>
        </div>
      </div>

      <div class="detail-diagnosis-grid">
        <div class="detail-diagnosis-card">
          <div class="detail-diagnosis-title">运营判断</div>
          <div class="detail-diagnosis-value">{{ operationSummary.title }}</div>
          <div class="detail-diagnosis-desc">{{ operationSummary.desc }}</div>
        </div>
        <div class="detail-diagnosis-card">
          <div class="detail-diagnosis-title">跟进建议</div>
          <div class="diagnosis-action-list">
            <div v-for="item in operationAdviceList" :key="item" class="diagnosis-action-item">
              {{ item }}
            </div>
          </div>
        </div>
        <div class="detail-diagnosis-card">
          <div class="detail-diagnosis-title">风险提醒</div>
          <div class="diagnosis-action-list">
            <div v-for="item in riskNoticeList" :key="item" class="diagnosis-action-item is-warning-text">
              {{ item }}
            </div>
          </div>
        </div>
      </div>

      <div class="detail-service-grid">
        <div class="detail-diagnosis-card">
          <div class="detail-diagnosis-title">客服沟通判断</div>
          <div class="detail-diagnosis-value">{{ customerServiceSummary.title }}</div>
          <div class="detail-diagnosis-desc">{{ customerServiceSummary.desc }}</div>
        </div>
        <div class="detail-diagnosis-card">
          <div class="detail-diagnosis-title">客服沟通建议</div>
          <div class="diagnosis-action-list">
            <div v-for="item in customerServiceAdviceList" :key="item" class="diagnosis-action-item">
              {{ item }}
            </div>
          </div>
        </div>
        <div class="detail-diagnosis-card">
          <div class="detail-diagnosis-title">最近售后关注点</div>
          <div class="diagnosis-action-list">
            <div v-for="item in recentAfterSaleFocusList" :key="item" class="diagnosis-action-item">
              {{ item }}
            </div>
          </div>
        </div>
        <div class="detail-diagnosis-card">
          <div class="detail-diagnosis-title">服务策略</div>
          <div class="diagnosis-action-list">
            <div v-for="item in serviceStrategyList" :key="item" class="diagnosis-action-item">
              {{ item }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="detail-tabs">
      <el-tab-pane label="基础资料" name="base">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="真实姓名">{{ detail.realName || "未填写" }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ maskMobile(detail.mobile) }}</el-descriptions-item>
          <el-descriptions-item label="运营标签">
            <template v-if="buildUserTags(detail).length">
              <el-tag v-for="item in buildUserTags(detail)" :key="item" size="small" effect="plain" class="inline-tag">
                {{ item }}
              </el-tag>
            </template>
            <span v-else>未打标签</span>
          </el-descriptions-item>
          <el-descriptions-item label="会员身份">{{ formatMemberIdentity(detail.memberFlag) }}</el-descriptions-item>
          <el-descriptions-item label="入会时间">{{ formatDateTime(detail.memberTime) }}</el-descriptions-item>
          <el-descriptions-item label="生日">{{ detail.birthday || "未填写" }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ formatSex(detail.sex) }}</el-descriptions-item>
          <el-descriptions-item label="OpenId">{{ detail.openId || "未同步" }}</el-descriptions-item>
          <el-descriptions-item label="UnionId">{{ detail.unionId || "未同步" }}</el-descriptions-item>
          <el-descriptions-item label="国家">{{ detail.country || "未同步" }}</el-descriptions-item>
          <el-descriptions-item label="省市">{{ buildAreaText(detail) }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ formatDateTime(detail.registerTime) }}</el-descriptions-item>
          <el-descriptions-item label="最后登录">{{ formatDateTime(detail.lastLoginTime) }}</el-descriptions-item>
          <el-descriptions-item label="最近活跃">{{ formatDateTime(detail.recentActiveTime) }}</el-descriptions-item>
          <el-descriptions-item label="入会天数">{{ formatDayCount(detail.memberAgeDays) }}</el-descriptions-item>
          <el-descriptions-item label="最近支付">{{ formatDateTime(detail.recentPaymentTime) }}</el-descriptions-item>
          <el-descriptions-item label="最近支付订单">
            <span>{{ detail.recentPaymentOrderNo || "暂无" }}</span>
            <el-button
              v-if="detail.recentPaymentOrderNo"
              type="primary"
              link
              class="inline-link-btn"
              @click="goOrderList({ orderNo: detail.recentPaymentOrderNo })"
            >
              查看订单
            </el-button>
          </el-descriptions-item>
          <el-descriptions-item label="近30天下单">{{ Number(detail.thirtyDayOrderCount || 0) }} 单</el-descriptions-item>
          <el-descriptions-item label="近30天消费">¥{{ Number(detail.thirtyDayConsumeAmount || 0).toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="售后件数">{{ Number(detail.afterSaleCount || 0) }} 件</el-descriptions-item>
          <el-descriptions-item label="已退款金额">¥{{ Number(detail.refundedAmount || 0).toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="购物车SKU">{{ Number(detail.cartSkuCount || 0) }} 行</el-descriptions-item>
          <el-descriptions-item label="购物车件数">{{ Number(detail.cartQuantity || 0) }} 件</el-descriptions-item>
          <el-descriptions-item label="注册IP">{{ detail.registerIp || "未记录" }}</el-descriptions-item>
          <el-descriptions-item label="最后登录IP">{{ detail.lastLoginIp || "未记录" }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detail.remark || "无" }}</el-descriptions-item>
        </el-descriptions>
      </el-tab-pane>

      <el-tab-pane label="关键轨迹" name="timeline">
        <div class="timeline-panel">
          <div class="timeline-title">会员生命周期轨迹</div>
          <el-timeline v-if="lifecycleTimeline.length">
            <el-timeline-item
              v-for="item in lifecycleTimeline"
              :key="`${item.title}-${item.time}`"
              :timestamp="item.time"
              :type="item.type"
            >
              <div class="timeline-item-title">{{ item.title }}</div>
              <div class="timeline-item-desc">{{ item.desc }}</div>
            </el-timeline-item>
          </el-timeline>
          <el-empty v-else description="该会员暂无可展示的关键轨迹"></el-empty>
        </div>
      </el-tab-pane>

      <el-tab-pane label="运营记录" name="operate">
        <div class="operate-panel">
          <div class="operate-panel-header">
            <div class="timeline-title">最近运营动作</div>
            <div class="operate-panel-tip">自动记录后台标签、备注、状态变更，也支持在这里手工补充跟进说明。</div>
          </div>
          <div class="follow-up-card">
            <div class="follow-up-title">新增跟进记录</div>
            <el-input
              v-model="followForm.content"
              type="textarea"
              :rows="4"
              maxlength="300"
              show-word-limit
              placeholder="请输入本次会员跟进内容，例如：已电话回访，客户反馈月底再下单。"
            />
            <div class="follow-up-actions">
              <el-button type="primary" :loading="followForm.submitting" @click="submitFollowRecord">保存跟进</el-button>
            </div>
          </div>
          <el-table v-if="recentOperateLogList.length" :data="recentOperateLogList" border style="width: 100%">
            <el-table-column prop="operateTitle" label="操作类型" width="160" align="center">
              <template #default="scope">
                <el-tag size="small" effect="plain" :type="buildOperateTypeTag(scope.row).type">
                  {{ buildOperateTypeTag(scope.row).label }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="operateContent" label="操作内容" min-width="360">
              <template #default="scope">
                <div class="operate-content">{{ scope.row.operateContent || "无" }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="operatorName" label="操作人" width="140" align="center">
              <template #default="scope">
                {{ scope.row.operatorName || "system" }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="操作时间" width="180" align="center">
              <template #default="scope">
                {{ formatDateTime(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="快捷定位" width="160" align="center">
              <template #default="scope">
                <el-button
                  v-if="parseOperateExtraInfo(scope.row).orderNo"
                  type="primary"
                  link
                  @click="openServiceOrder(parseOperateExtraInfo(scope.row))"
                >
                  订单
                </el-button>
                <el-button
                  v-if="parseOperateExtraInfo(scope.row).orderNo && parseOperateExtraInfo(scope.row).afterSaleStatus"
                  type="success"
                  link
                  @click="openServiceAfterSale(parseOperateExtraInfo(scope.row))"
                >
                  售后
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-else description="该会员暂无运营记录"></el-empty>
        </div>
      </el-tab-pane>

      <el-tab-pane label="最近订单" name="orders">
        <el-table v-if="recentOrderList.length" :data="recentOrderList" border style="width: 100%">
          <el-table-column prop="orderNo" label="订单号" min-width="180"></el-table-column>
          <el-table-column prop="name" label="订单名称" min-width="180">
            <template #default="scope">
              <div class="order-title">{{ scope.row.name || "未命名订单" }}</div>
              <div class="order-goods-summary">{{ buildOrderGoodsSummary(scope.row) }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="paymentPrice" label="支付金额" width="120" align="center">
            <template #default="scope">
              <span class="amount-text">¥{{ Number(scope.row.paymentPrice || 0).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="订单状态" width="120" align="center">
            <template #default="scope">
              <el-tag size="small" effect="plain" :type="buildOrderStatusTagType(scope.row)">
                {{ buildOrderStatusText(scope.row) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="下单时间" width="180" align="center">
            <template #default="scope">
              {{ formatDateTime(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template #default="scope">
              <el-button type="primary" link @click="goOrderList({ orderNo: scope.row.orderNo })">
                定位订单
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="该会员暂无订单记录"></el-empty>
      </el-tab-pane>

      <el-tab-pane label="售后记录" name="aftersale">
        <el-table v-if="recentAfterSaleList.length" :data="recentAfterSaleList" border style="width: 100%">
          <el-table-column prop="orderNo" label="订单号" min-width="180"></el-table-column>
          <el-table-column prop="spuName" label="商品信息" min-width="220">
            <template #default="scope">
              <div class="order-title">{{ scope.row.spuName || "未命名商品" }}</div>
              <div class="order-goods-summary">
                {{ buildAfterSaleGoodsSummary(scope.row) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="statusDesc" label="售后状态" width="130" align="center">
            <template #default="scope">
              <el-tag size="small" effect="plain" :type="scope.row.tagType">
                {{ scope.row.statusDesc }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="paymentPrice" label="售后金额" width="120" align="center">
            <template #default="scope">
              <span class="amount-text">¥{{ Number(scope.row.paymentPrice || 0).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="最近处理时间" width="180" align="center">
            <template #default="scope">
              {{ formatDateTime(scope.row.time) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160" align="center">
            <template #default="scope">
              <el-button type="primary" link @click="goOrderList({ orderNo: scope.row.orderNo })">
                查看订单
              </el-button>
              <el-button
                type="success"
                link
                @click="goAfterSalePage({ orderNo: scope.row.orderNo, afterSaleStatus: resolveAfterSaleStatusValue(scope.row) })"
              >
                定位售后
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="该会员暂无售后记录"></el-empty>
      </el-tab-pane>

      <el-tab-pane label="收货地址" name="address">
        <el-table v-if="recentAddressList.length" :data="recentAddressList" border style="width: 100%">
          <el-table-column prop="userName" label="收货人" width="100" align="center"></el-table-column>
          <el-table-column prop="telNum" label="手机号" width="140" align="center">
            <template #default="scope">
              {{ maskMobile(scope.row.telNum) }}
            </template>
          </el-table-column>
          <el-table-column prop="addressText" label="地址" min-width="280">
            <template #default="scope">
              {{ buildAddressText(scope.row) }}
            </template>
          </el-table-column>
          <el-table-column prop="isDefault" label="默认地址" width="100" align="center">
            <template #default="scope">
              <el-tag size="small" effect="plain" :type="scope.row.isDefault === '1' ? 'success' : 'info'">
                {{ scope.row.isDefault === "1" ? "默认" : "普通" }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="updateTime" label="更新时间" width="180" align="center">
            <template #default="scope">
              {{ formatDateTime(scope.row.updateTime || scope.row.createTime) }}
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="该会员暂无收货地址"></el-empty>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup name="MallUserDetail">
import { getObj, postFollowRecord } from "@/api/mall/malluser";
import { resolveMallImageUrl } from "@/utils/mall-image";

const { proxy } = getCurrentInstance();

/**
 * 统一处理商城会员详情页头像地址。
 *
 * @param {string} imageUrl 图片地址
 * @returns {string}
 */
function resolveMallUserImage(imageUrl) {
  return resolveMallImageUrl(imageUrl);
}
const route = useRoute();
const router = useRouter();

/**
 * 页面状态。
 * 独立详情页只负责展示，因此把主数据和 tab 状态收口到一个对象里管理。
 */
const data = reactive({
  loading: false,
  activeTab: "base",
  detail: {},
  followForm: {
    content: "",
    submitting: false,
  },
});

const { loading, activeTab, detail, followForm } = toRefs(data);

const recentOrderList = computed(() => data.detail?.recentOrderList || []);
const recentAddressList = computed(() => data.detail?.recentAddressList || []);
const recentOperateLogList = computed(() => data.detail?.recentOperateLogList || []);
const memberSegmentTag = computed(() => buildUserSegment(data.detail));
const activityLevelTag = computed(() => buildActivityLevel(data.detail));
const valueLevelTag = computed(() => buildValueLevel(data.detail));

/**
 * 会员运营结论。
 * 汇总当前会员所处阶段，方便客服或运营进入详情页后先看结论，再决定是否继续深挖订单明细。
 */
const operationSummary = computed(() => {
  if (data.detail?.status === "1") {
    return {
      title: "账号已禁用",
      desc: "当前会员账号处于禁用状态，适合先核对禁用原因和是否需要恢复服务。",
    };
  }
  if (data.detail?.memberFlag !== "1") {
    return {
      title: "待转化用户",
      desc: "该用户尚未绑定手机号，优先推动入会转化和手机号沉淀。",
    };
  }
  if (Number(data.detail?.consumeCount || 0) >= 3) {
    return {
      title: "稳定复购会员",
      desc: "该会员已形成持续购买行为，适合承接复购激励、会员权益和新品优先触达。",
    };
  }
  if (Number(data.detail?.consumeCount || 0) > 0) {
    return {
      title: "已成交会员",
      desc: "该会员已完成支付，但复购深度仍可继续挖掘，适合做老客召回和关联推荐。",
    };
  }
  if (Number(data.detail?.orderCount || 0) > 0) {
    return {
      title: "下单未成交用户",
      desc: "该用户已有下单行为但支付转化仍有空间，适合围绕支付完成和售后体验做触达。",
    };
  }
  return {
    title: "新入会用户",
    desc: "该会员仍处于早期观察阶段，建议重点看首单转化和首次活跃表现。",
  };
});

/**
 * 运营建议列表。
 * 这里只输出当前最有执行价值的 2 到 3 条建议，避免页面出现过多“正确但没法立刻执行”的文案。
 */
const operationAdviceList = computed(() => {
  const suggestions = [];
  if (data.detail?.memberFlag !== "1") {
    suggestions.push("优先推动手机号绑定，完成会员身份转化。");
  }
  if (Number(data.detail?.orderCount || 0) > 0 && Number(data.detail?.consumeCount || 0) <= 0) {
    suggestions.push("针对已下单未支付用户，优先安排支付提醒或限时优惠触达。");
  }
  if (activityLevelTag.value.label === "沉睡中") {
    suggestions.push("该会员已进入沉睡状态，适合做唤醒短信、服务回访或权益提醒。");
  }
  if (Number(data.detail?.consumeCount || 0) >= 3) {
    suggestions.push("可进入高价值老客维护池，优先承接新品预售和会员专享活动。");
  } else if (Number(data.detail?.consumeCount || 0) > 0) {
    suggestions.push("建议结合最近支付订单做复购推荐，缩短二次购买周期。");
  }
  if (suggestions.length <= 0) {
    suggestions.push("当前以持续观察活跃和首单转化为主，暂不建议高频打扰。");
  }
  return suggestions.slice(0, 3);
});

/**
 * 风险提醒列表。
 * 这里单独展示可能影响转化或服务体验的异常信号，避免和正向建议混在一起。
 */
const riskNoticeList = computed(() => {
  const notices = [];
  if (!data.detail?.mobile) {
    notices.push("未绑定手机号，后续会员识别、召回和人工服务效率都会受影响。");
  }
  if (activityLevelTag.value.label === "沉睡中") {
    notices.push("最近 30 天未见活跃，流失风险较高。");
  }
  if (Number(data.detail?.afterSaleCount || 0) > 0) {
    notices.push("存在售后记录，建议关注售后处理体验是否影响复购。");
  }
  if (Number(data.detail?.waitPayOrderCount || 0) > 0) {
    notices.push("仍有待付款订单未完成，支付流失风险仍在。");
  }
  if (notices.length <= 0) {
    notices.push("当前未发现明显异常信号，可按常规会员运营策略持续维护。");
  }
  return notices.slice(0, 3);
});

/**
 * 客服沟通结论。
 * 相比运营判断，这里更强调“当前客服开口第一句该怎么说、先处理什么”。
 */
const customerServiceSummary = computed(() => {
  if (data.detail?.status === "1") {
    return {
      title: "先核对账号状态",
      desc: "当前会员账号已禁用，客服沟通前先确认禁用原因和是否需要恢复服务，避免直接承诺订单或售后处理。",
    };
  }
  if (!data.detail?.mobile) {
    return {
      title: "先推动绑定手机号",
      desc: "该用户还未完成会员转化，客服优先引导绑定手机号，后续会员识别、售后回访和复购服务会更顺畅。",
    };
  }
  if (recentAfterSaleList.value.length > 0) {
    return {
      title: "先收口最近售后",
      desc: "该会员近期存在售后记录，建议客服先确认售后结果和用户满意度，再继续做复购或运营沟通。",
    };
  }
  if (Number(data.detail?.waitDeliveryOrderCount || 0) > 0 || Number(data.detail?.waitReceiveOrderCount || 0) > 0) {
    return {
      title: "先同步履约进度",
      desc: "当前会员仍有履约中的订单，客服优先围绕发货、物流、签收和售后预期展开沟通，减少咨询反复。",
    };
  }
  if (activityLevelTag.value.label === "沉睡中") {
    return {
      title: "低打扰回访",
      desc: "该会员近期活跃偏低，沟通上应更偏服务回访和问题关怀，不适合上来就强推活动。",
    };
  }
  return {
    title: "常规服务维护",
    desc: "当前会员状态整体稳定，客服可按订单服务、使用反馈和会员权益提醒的常规节奏维护。",
  };
});

/**
 * 客服沟通建议。
 * 只保留前台客服最需要立刻执行的动作，避免和运营建议重复堆满页面。
 */
const customerServiceAdviceList = computed(() => {
  const suggestions = [];
  if (!data.detail?.mobile) {
    suggestions.push("先解释绑定手机号后的权益和服务价值，再引导完成手机号绑定。");
  }
  if (recentAfterSaleList.value.length > 0) {
    suggestions.push("先确认最近售后是否已闭环，避免在问题未解决时直接做复购沟通。");
  }
  if (Number(data.detail?.waitDeliveryOrderCount || 0) > 0) {
    suggestions.push("存在待发货订单，优先同步发货进度和预计履约时间。");
  }
  if (Number(data.detail?.waitReceiveOrderCount || 0) > 0) {
    suggestions.push("存在待收货订单，提醒签收验货，并提前说明有问题可直接走售后。");
  }
  if (Number(data.detail?.waitPayOrderCount || 0) > 0) {
    suggestions.push("存在待付款订单，沟通时可以先确认是否遇到支付或商品决策障碍。");
  }
  if (activityLevelTag.value.label === "沉睡中") {
    suggestions.push("对沉睡会员建议先做关怀式回访，语气以服务确认为主，避免高频营销。");
  }
  if (suggestions.length <= 0) {
    suggestions.push("当前以订单服务确认、商品使用反馈和会员关系维护为主。");
  }
  return suggestions.slice(0, 4);
});

/**
 * 会员售后记录。
 * 当前没有独立售后主表，这里直接从最近订单里的订单项售后状态中展开出一份可读列表。
 */
const recentAfterSaleList = computed(() => {
  return recentOrderList.value
    .flatMap((order) => {
      return (order?.listOrderItem || [])
        .map((item) => {
          const statusDesc = buildAfterSaleStatusDesc(item);
          if (statusDesc === "无售后") {
            return null;
          }
          return {
            id: item.id,
            orderId: order.id,
            orderNo: order.orderNo,
            spuName: item.spuName,
            specInfo: item.specInfo,
            quantity: item.quantity,
            paymentPrice: item.paymentPrice,
            statusDesc,
            tagType: buildAfterSaleTagType(item),
            time: item.updateTime || order.updateTime || order.createTime,
          };
        })
        .filter((item) => item);
    })
    .sort((a, b) => String(b.time || "").localeCompare(String(a.time || "")));
});

/**
 * 最近售后关注点。
 * 直接把最近几条售后整理成客服视角的提醒语句，进入会员详情时不必再切到售后 tab 才能判断。
 */
const recentAfterSaleFocusList = computed(() => {
  if (recentAfterSaleList.value.length <= 0) {
    return ["该会员最近没有售后记录，当前可以按常规服务和会员维护节奏跟进。"];
  }
  return recentAfterSaleList.value.slice(0, 3).map((item) => {
    return `${item.spuName || "商品"}当前为“${item.statusDesc}”，最近处理时间 ${formatDateTime(item.time)}，订单号 ${item.orderNo || "未记录"}。`;
  });
});

/**
 * 与客服处理直接相关的日志。
 * 会员详情里需要把人工跟进、退款审核、退款到账这些动作提炼出来，避免运营日志太杂看不出服务脉络。
 */
const serviceOperateLogList = computed(() => {
  const serviceOperateTypes = ["FOLLOW_UP", "REFUND_APPROVE", "REFUND_REJECT", "REFUND_SUCCESS"];
  return recentOperateLogList.value.filter((item) => serviceOperateTypes.includes(item?.operateType));
});

/**
 * 最近一次客服跟进摘要。
 * 进入会员详情首页时直接给出最近的服务动作，减少客服切 tab 查日志的成本。
 */
const latestServiceSummary = computed(() => {
  const latestOperateLog = serviceOperateLogList.value[0];
  if (latestOperateLog) {
    return {
      title: `${buildOperateTypeTag(latestOperateLog).label} · ${formatDateTime(latestOperateLog.createTime)}`,
      desc: `${latestOperateLog.operatorName || "system"}：${latestOperateLog.operateContent || "暂无服务说明"}`,
    };
  }
  if (recentAfterSaleList.value.length > 0) {
    return {
      title: "待补客服跟进记录",
      desc: "该会员存在售后记录，但当前还没有沉淀客服跟进日志，建议本次处理后补一条跟进说明。",
    };
  }
  if (Number(data.detail?.waitDeliveryOrderCount || 0) > 0 || Number(data.detail?.waitReceiveOrderCount || 0) > 0) {
    return {
      title: "当前以履约服务为主",
      desc: "该会员当前有履约中的订单，建议优先围绕发货、物流、签收和售后预期提供服务说明。",
    };
  }
  return {
    title: "暂无客服跟进记录",
    desc: "当前还没有人工服务日志，可在本次回访、售后处理或会员维护后补充一条跟进记录。",
  };
});

/**
 * 最近一次客服跟进的快捷定位信息。
 * 优先读取运营日志里的扩展字段，方便从会员页直接跳到对应订单或售后工作台。
 */
const latestServiceActionInfo = computed(() => parseOperateExtraInfo(serviceOperateLogList.value[0]));

/**
 * 服务策略。
 * 这里输出更偏长期关系维护的动作，和“客服沟通建议”的当下动作形成区分。
 */
const serviceStrategyList = computed(() => {
  const strategies = [];
  if (data.detail?.memberFlag !== "1") {
    strategies.push("仍按商城用户而非成熟会员维护，重点先完成手机号绑定和基础资料沉淀。");
  } else if (Number(data.detail?.consumeCount || 0) >= 3) {
    strategies.push("属于复购会员，后续服务策略可偏向老客维护、优先响应和重点关怀。");
  } else if (Number(data.detail?.consumeCount || 0) > 0) {
    strategies.push("已完成首次成交，适合在售后稳定后承接二次购买和关联商品服务。");
  } else {
    strategies.push("仍处于首单观察阶段，服务重点放在首单转化和履约体验。");
  }
  if (Number(data.detail?.afterSaleCount || 0) > 0) {
    strategies.push("历史存在售后，沟通时要优先确认问题是否彻底解决，避免重复踩雷。");
  }
  if (activityLevelTag.value.label === "沉睡中") {
    strategies.push("活跃度偏低，建议降低打扰频次，优先做问题回访和服务唤醒。");
  }
  if (strategies.length <= 0) {
    strategies.push("当前会员状态稳定，可继续按常规会员服务策略维护。");
  }
  return strategies.slice(0, 4);
});

/**
 * 生命周期时间线。
 * 不追求全量事件，只把运营最关心的关键节点串出来，便于快速复盘这个会员的发展轨迹。
 */
const lifecycleTimeline = computed(() => {
  const timeline = [];
  if (data.detail?.registerTime) {
    timeline.push({
      title: "注册进入商城",
      time: formatDateTime(data.detail.registerTime),
      desc: `注册来源：${formatRegisterSource(data.detail.registerSource)}`,
      type: "primary",
    });
  }
  if (data.detail?.memberTime && data.detail?.memberFlag === "1") {
    timeline.push({
      title: "完成会员转化",
      time: formatDateTime(data.detail.memberTime),
      desc: "用户完成手机号绑定，正式进入会员体系。",
      type: "success",
    });
  }
  if (data.detail?.lastOrderTime) {
    timeline.push({
      title: "最近一次下单",
      time: formatDateTime(data.detail.lastOrderTime),
      desc: `累计下单 ${Number(data.detail.orderCount || 0)} 次。`,
      type: "warning",
    });
  }
  if (data.detail?.recentPaymentTime) {
    timeline.push({
      title: "最近一次支付",
      time: formatDateTime(data.detail.recentPaymentTime),
      desc: `最近支付订单：${data.detail.recentPaymentOrderNo || "未记录"}`,
      type: "success",
    });
  }
  if (data.detail?.lastLoginTime) {
    timeline.push({
      title: "最近一次登录",
      time: formatDateTime(data.detail.lastLoginTime),
      desc: `当前活跃度：${activityLevelTag.value.label}`,
      type: "info",
    });
  }
  recentOperateLogList.value.slice(0, 5).forEach((item) => {
    timeline.push({
      title: item.operateTitle || buildOperateTypeTag(item).label,
      time: formatDateTime(item.createTime),
      desc: item.operateContent || "无操作内容",
      type: buildOperateTypeTag(item).type,
    });
  });
  return timeline.sort((a, b) => String(b.time || "").localeCompare(String(a.time || "")));
});

/**
 * 当前会员主键。
 */
const userId = computed(() => route.query.id || "");

watch(
  () => userId.value,
  () => {
    loadDetail();
  },
  { immediate: true }
);

/**
 * 加载会员详情。
 */
function loadDetail() {
  if (!userId.value) {
    data.detail = {};
    data.followForm.content = "";
    return;
  }
  data.loading = true;
  getObj(userId.value)
    .then((response) => {
      data.detail = response.data || {};
      data.followForm.content = "";
      data.followForm.submitting = false;
      data.loading = false;
    })
    .catch(() => {
      data.followForm.submitting = false;
      data.loading = false;
    });
}

/**
 * 手工刷新当前详情。
 */
function reloadDetail() {
  loadDetail();
}

/**
 * 返回会员列表。
 */
function goBack() {
  router.push("/mall/malluser");
}

/**
 * 跳转到订单列表。
 * 默认按当前会员过滤，也可以额外带上订单号做进一步定位。
 *
 * @param {Object} extraQuery 额外查询参数
 */
function goOrderList(extraQuery = {}) {
  if (!detail.value?.id) {
    return;
  }
  const query = Object.assign(
    {
      userId: detail.value.id,
      userNo: detail.value.userNo || "",
      userNickName: detail.value.nickName || "",
    },
    extraQuery
  );
  router.push({
    path: "/mall/orderinfo",
    query,
  });
}

/**
 * 跳转到售后工作台。
 * 直接带上当前会员筛选参数，客服进入后即可看到该会员的全部售后记录。
 *
 * @param {Object} extraQuery 额外查询参数
 */
function goAfterSalePage(extraQuery = {}) {
  if (!detail.value?.id) {
    return;
  }
  router.push({
    path: "/mall/aftersale",
    query: Object.assign(
      {
        userId: detail.value.id,
        userNo: detail.value.userNo || "",
        userNickName: detail.value.nickName || "",
        afterSaleStatus: "ALL",
      },
      extraQuery
    ),
  });
}

/**
 * 从服务摘要或运营记录直接定位订单。
 *
 * @param {Object} actionInfo 日志扩展信息
 */
function openServiceOrder(actionInfo) {
  if (!actionInfo?.orderNo) {
    return;
  }
  goOrderList({
    orderNo: actionInfo.orderNo,
  });
}

/**
 * 从服务摘要或运营记录直接定位售后工作台。
 *
 * @param {Object} actionInfo 日志扩展信息
 */
function openServiceAfterSale(actionInfo) {
  if (!actionInfo?.orderNo) {
    return;
  }
  goAfterSalePage({
    orderNo: actionInfo.orderNo,
    afterSaleStatus: actionInfo.afterSaleStatus || "ALL",
  });
}

function formatRegisterSource(registerSource) {
  if (registerSource === "1") {
    return "小程序";
  }
  if (registerSource === "2") {
    return "公众号";
  }
  if (registerSource === "3") {
    return "H5";
  }
  if (registerSource === "4") {
    return "APP";
  }
  return "其他";
}

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
 * 会员身份格式化。
 *
 * @param {string} memberFlag 会员标记
 * @returns {string}
 */
function formatMemberIdentity(memberFlag) {
  return memberFlag === "1" ? "已入会" : "未入会";
}

/**
 * 解析运营标签列表。
 *
 * @param {Object} row 会员数据
 * @returns {string[]}
 */
function buildUserTags(row) {
  const tagText = String(row?.userTag || "").trim();
  if (!tagText) {
    return [];
  }
  return tagText
    .split(/[，,、]/)
    .map((item) => item.trim())
    .filter(Boolean);
}

/**
 * 运营记录类型标签。
 *
 * @param {Object} row 运营记录
 * @returns {{ label: string, type: string }}
 */
function buildOperateTypeTag(row) {
  switch (row?.operateType) {
    case "FOLLOW_UP":
      return { label: "跟进记录", type: "primary" };
    case "REFUND_APPROVE":
      return { label: "退款已通过", type: "success" };
    case "REFUND_REJECT":
      return { label: "退款已拒绝", type: "danger" };
    case "REFUND_SUCCESS":
      return { label: "退款已到账", type: "warning" };
    case "TAG":
      return { label: "标签变更", type: "success" };
    case "REMARK":
      return { label: "备注变更", type: "warning" };
    case "STATUS":
      return { label: "状态变更", type: "danger" };
    case "PROFILE":
      return { label: "资料修改", type: "info" };
    default:
      return { label: row?.operateTitle || "运营记录", type: "" };
  }
}

/**
 * 解析运营日志扩展信息。
 * 新日志会写入 JSON 结构，老日志没有这个字段时直接回退为空对象。
 *
 * @param {Object} row 运营日志
 * @returns {{scene: string, orderId: string, orderNo: string, orderItemId: string, afterSaleStatus: string}}
 */
function parseOperateExtraInfo(row) {
  const defaultValue = {
    scene: "",
    orderId: "",
    orderNo: "",
    orderItemId: "",
    afterSaleStatus: "",
  };
  if (!row?.extraInfo) {
    return defaultValue;
  }
  try {
    return Object.assign({}, defaultValue, JSON.parse(row.extraInfo) || {});
  } catch (error) {
    return defaultValue;
  }
}

function maskMobile(mobile) {
  if (!mobile) {
    return "未绑定";
  }
  if (mobile.length < 7) {
    return mobile;
  }
  return `${mobile.substring(0, 3)}****${mobile.substring(mobile.length - 4)}`;
}

function formatDateTime(value) {
  if (!value) {
    return "暂无";
  }
  return String(value).replace("T", " ").substring(0, 19);
}

function formatDayCount(dayCount) {
  if (dayCount === null || dayCount === undefined || dayCount === "") {
    return "未入会";
  }
  const count = Number(dayCount || 0);
  return `${Number.isNaN(count) ? 0 : count} 天`;
}

function buildAreaText(userInfo) {
  const areaText = `${userInfo?.province || ""}${userInfo?.city || ""}`;
  return areaText || "未同步";
}

function buildAddressText(address) {
  if (!address) {
    return "暂无收货地址";
  }
  return `${address.provinceName || ""}${address.cityName || ""}${address.countyName || ""}${address.detailInfo || ""}`;
}

/**
 * 统一计算距今天数。
 * 后端时间字段有的带 T，有的是普通字符串，这里先做一次兼容处理，避免浏览器解析差异。
 *
 * @param {string} value 时间值
 * @returns {number|null}
 */
function getDaysFromNow(value) {
  if (!value) {
    return null;
  }
  const normalizedValue = String(value).replace("T", " ").replace(/-/g, "/");
  const targetTime = new Date(normalizedValue).getTime();
  if (Number.isNaN(targetTime)) {
    return null;
  }
  const diff = Date.now() - targetTime;
  return Math.max(0, Math.floor(diff / (24 * 60 * 60 * 1000)));
}

/**
 * 构建会员分层标签。
 *
 * @param {Object} row 会员数据
 * @returns {{ label: string, type: string }}
 */
function buildUserSegment(row) {
  if (row?.status === "1") {
    return { label: "已禁用", type: "danger" };
  }
  if (row?.memberFlag !== "1") {
    return { label: "待转化", type: "info" };
  }
  if (Number(row?.consumeCount || 0) > 0 && Number(row?.orderCount || 0) >= 3) {
    return { label: "复购会员", type: "success" };
  }
  if (Number(row?.consumeCount || 0) > 0) {
    return { label: "成交会员", type: "warning" };
  }
  if (Number(row?.orderCount || 0) > 0) {
    return { label: "下单待支付", type: "primary" };
  }
  return { label: "新入会用户", type: "" };
}

/**
 * 构建活跃度标签。
 *
 * @param {Object} row 会员数据
 * @returns {{ label: string, type: string }}
 */
function buildActivityLevel(row) {
  const activeDays = getDaysFromNow(row?.lastLoginTime || row?.registerTime || row?.createTime);
  if (activeDays === null) {
    return { label: "暂无记录", type: "info" };
  }
  if (activeDays <= 7) {
    return { label: "高活跃", type: "success" };
  }
  if (activeDays <= 30) {
    return { label: "可唤醒", type: "warning" };
  }
  return { label: "沉睡中", type: "info" };
}

/**
 * 构建价值分层标签。
 *
 * @param {Object} row 会员数据
 * @returns {{ label: string, type: string }}
 */
function buildValueLevel(row) {
  const consumeAmount = Number(row?.consumeAmount || 0);
  if (consumeAmount >= 3000 || Number(row?.consumeCount || 0) >= 5) {
    return { label: "高价值", type: "danger" };
  }
  if (consumeAmount >= 1000 || Number(row?.consumeCount || 0) >= 2) {
    return { label: "成长中", type: "warning" };
  }
  if (Number(row?.consumeCount || 0) > 0) {
    return { label: "已成交", type: "success" };
  }
  return { label: "待培养", type: "info" };
}

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

function buildAfterSaleGoodsSummary(record) {
  const specInfo = record?.specInfo ? `规格：${record.specInfo}` : "默认规格";
  return `${specInfo}；数量：${record?.quantity || 0}`;
}

/**
 * 将售后记录状态转换成售后工作台筛选值。
 *
 * @param {Object} record 售后记录
 * @returns {string}
 */
function resolveAfterSaleStatusValue(record) {
  if (record?.statusDesc === "已退款") {
    return "4";
  }
  if (record?.statusDesc === "退款申请中") {
    return "1";
  }
  if (record?.statusDesc === "退款被拒绝") {
    return "2";
  }
  if (record?.statusDesc === "退款待回调") {
    return "3";
  }
  return "ALL";
}

function buildOrderStatusText(order) {
  if (order?.isPay === "0" && !order?.status) {
    return "待付款";
  }
  if (order?.status === "1") {
    return "待发货";
  }
  if (order?.status === "2") {
    return "待收货";
  }
  if (order?.status === "3") {
    return "已完成";
  }
  if (order?.status === "5") {
    return "已取消";
  }
  return "状态未知";
}

function buildOrderStatusTagType(order) {
  if (order?.isPay === "0" && !order?.status) {
    return "warning";
  }
  if (order?.status === "1") {
    return "danger";
  }
  if (order?.status === "2") {
    return "primary";
  }
  if (order?.status === "3") {
    return "success";
  }
  if (order?.status === "5") {
    return "info";
  }
  return "";
}

function buildOrderGoodsSummary(order) {
  const orderItems = order?.listOrderItem || [];
  if (!orderItems.length) {
    return "暂无商品快照";
  }
  return orderItems
    .map((item) => {
      const specInfo = item.specInfo ? `（${item.specInfo}）` : "";
      return `${item.spuName || "未命名商品"} ×${item.quantity || 0}${specInfo}`;
    })
    .join("；");
}

/**
 * 提交会员跟进记录。
 */
function submitFollowRecord() {
  if (!detail.value?.id) {
    return;
  }
  const content = String(data.followForm.content || "").trim();
  if (!content) {
    proxy.$message.warning("请先输入跟进内容");
    return;
  }
  data.followForm.submitting = true;
  postFollowRecord(detail.value.id, {
    operateContent: content,
  })
    .then(() => {
      proxy.$message.success("跟进记录已保存");
      data.followForm.content = "";
      loadDetail();
    })
    .catch(() => {
      data.followForm.submitting = false;
    });
}
</script>

<style lang="scss" scoped>
.mall-user-detail-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 22px 24px;
  border-radius: 18px;
  background: linear-gradient(135deg, #ffffff 0%, #f5f8ff 100%);
  border: 1px solid #e5ecfb;
}

.page-title {
  color: #111827;
  font-size: 24px;
  font-weight: 700;
}

.page-sub-title {
  margin-top: 8px;
  color: #6b7280;
  font-size: 13px;
}

.page-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.detail-overview {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-profile-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border: 1px solid #e8eef7;
  border-radius: 16px;
  background: linear-gradient(180deg, #ffffff 0%, #f6f9ff 100%);
}

.detail-profile-main {
  flex: 1;
}

.detail-profile-name {
  color: #1f2937;
  font-size: 20px;
  font-weight: 700;
}

.detail-profile-sub {
  margin-top: 6px;
  color: #6b7280;
  font-size: 13px;
}

.profile-tag-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.detail-divider {
  margin: 0 6px;
}

.detail-stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 12px;
}

.detail-stat-card {
  padding: 16px;
  border: 1px solid #edf2f7;
  border-radius: 14px;
  background: #fff;
}

.detail-stat-label {
  color: #6b7280;
  font-size: 12px;
}

.detail-stat-value {
  margin-top: 8px;
  color: #1f2937;
  font-size: 24px;
  font-weight: 700;
}

.detail-address-banner {
  padding: 16px 18px;
  border-radius: 14px;
  background: #f8fbff;
  border: 1px dashed #bfd3ff;
}

.detail-insight-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

.detail-insight-card {
  padding: 16px;
  border: 1px solid #edf2f7;
  border-radius: 14px;
  background: linear-gradient(180deg, #ffffff 0%, #fbfcff 100%);
}

.detail-insight-label {
  color: #6b7280;
  font-size: 12px;
}

.detail-insight-value {
  margin-top: 8px;
  color: #1f2937;
  font-size: 18px;
  font-weight: 600;
  line-height: 1.5;
}

.detail-address-label {
  color: #4b5563;
  font-size: 12px;
}

.detail-address-text {
  margin-top: 8px;
  color: #1f2937;
  line-height: 1.8;
}

.detail-service-banner {
  padding: 18px;
  border: 1px solid #dbe7ff;
  border-radius: 14px;
  background: linear-gradient(135deg, #f8fbff 0%, #eef4ff 100%);
}

.detail-service-banner-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.detail-service-banner-title {
  margin-top: 8px;
  color: #1f2937;
  font-size: 18px;
  font-weight: 700;
}

.detail-service-banner-desc {
  margin-top: 10px;
  color: #4b5563;
  font-size: 13px;
  line-height: 1.8;
}

.detail-service-banner-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.detail-shortcut-panel {
  padding: 16px 18px;
  border: 1px solid #e8eef7;
  border-radius: 14px;
  background: #fff;
}

.detail-shortcut-title {
  color: #1f2937;
  font-size: 14px;
  font-weight: 600;
}

.detail-shortcut-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 12px;
}

.detail-diagnosis-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 12px;
}

.detail-service-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 12px;
}

.detail-diagnosis-card {
  padding: 18px;
  border: 1px solid #edf2f7;
  border-radius: 14px;
  background: linear-gradient(180deg, #ffffff 0%, #fbfdff 100%);
}

.detail-diagnosis-title {
  color: #6b7280;
  font-size: 12px;
}

.detail-diagnosis-value {
  margin-top: 8px;
  color: #1f2937;
  font-size: 18px;
  font-weight: 700;
}

.detail-diagnosis-desc {
  margin-top: 8px;
  color: #4b5563;
  font-size: 13px;
  line-height: 1.7;
}

.diagnosis-action-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 10px;
}

.diagnosis-action-item {
  color: #4b5563;
  font-size: 13px;
  line-height: 1.7;
}

.is-warning-text {
  color: #b45309;
}

.detail-tabs {
  padding: 18px 20px;
  border-radius: 18px;
  background: #fff;
  border: 1px solid #e8eef7;
}

.amount-text {
  color: #d4380d;
  font-weight: 600;
}

.order-title {
  color: #303133;
  font-weight: 600;
}

.order-goods-summary {
  margin-top: 4px;
  color: #909399;
  font-size: 12px;
  line-height: 1.6;
}

.inline-link-btn {
  margin-left: 8px;
  padding: 0;
}

.inline-tag {
  margin-right: 6px;
}

.timeline-panel {
  padding: 8px 4px 4px;
}

.timeline-title {
  margin-bottom: 16px;
  color: #1f2937;
  font-size: 15px;
  font-weight: 600;
}

.timeline-item-title {
  color: #1f2937;
  font-weight: 600;
}

.timeline-item-desc {
  margin-top: 6px;
  color: #6b7280;
  line-height: 1.7;
}

.operate-panel {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.operate-panel-header {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.operate-panel-tip {
  color: #6b7280;
  font-size: 13px;
}

.follow-up-card {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 16px 18px;
  border: 1px solid #e8eef7;
  border-radius: 14px;
  background: linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
}

.follow-up-title {
  color: #1f2937;
  font-size: 14px;
  font-weight: 600;
}

.follow-up-actions {
  display: flex;
  justify-content: flex-end;
}

.operate-content {
  color: #4b5563;
  line-height: 1.7;
}

@media screen and (max-width: 768px) {
  .page-header,
  .detail-service-banner-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .detail-profile-card {
    flex-direction: column;
    align-items: flex-start;
  }

  .page-actions {
    width: 100%;
  }
}
</style>

<!--
  Copyright (C) 2026
  All rights reserved, Designed By www.joolun.com
  注意：
  本软件为www.joolun.com开发研制，项目使用请保留此说明
-->

<template>
  <div class="mall-user-page">
    <div class="summary-panel">
      <div v-for="item in summaryCardsView" :key="item.label" class="summary-card">
        <div class="summary-label">{{ item.label }}</div>
        <div class="summary-value" :class="item.emphasisClass">{{ item.value }}</div>
        <div class="summary-tip">{{ item.tip }}</div>
      </div>
    </div>
    <div class="operate-filter-panel">
      <div class="filter-header">
        <div>
          <div class="filter-title">会员运营筛选</div>
          <div class="filter-sub-title">按会员转化、下单、支付和时间区间快速圈选目标用户。</div>
        </div>
        <div class="filter-actions">
          <el-button type="primary" @click="applyAdvancedFilter">应用筛选</el-button>
          <el-button @click="resetAdvancedFilter">重置条件</el-button>
        </div>
      </div>
      <div class="quick-filter-group">
        <div class="group-label">快捷分层</div>
        <el-radio-group v-model="manualFilters.quickScene" size="small" @change="handleQuickSceneChange">
          <el-radio-button v-for="item in quickSceneOptions" :key="item.value" :label="item.value">
            {{ item.label }}
          </el-radio-button>
        </el-radio-group>
      </div>
      <div class="preset-filter-group">
        <div class="group-label">运营预设</div>
        <div class="preset-filter-list">
          <button v-for="item in presetSceneOptions" :key="item.value" type="button" class="preset-filter-card" @click="applyPresetFilter(item.value)">
            <span class="preset-filter-title">{{ item.label }}</span>
            <span class="preset-filter-tip">{{ item.tip }}</span>
          </button>
        </div>
      </div>
      <div class="filter-grid">
        <div class="filter-item">
          <div class="filter-item-label">注册时间</div>
          <el-date-picker
            v-model="manualFilters.registerTimeRange"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            range-separator="至"
            unlink-panels
            clearable
          />
        </div>
        <div class="filter-item">
          <div class="filter-item-label">最近登录</div>
          <el-date-picker
            v-model="manualFilters.lastLoginTimeRange"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            range-separator="至"
            unlink-panels
            clearable
          />
        </div>
        <div class="filter-item">
          <div class="filter-item-label">入会时间</div>
          <el-date-picker
            v-model="manualFilters.memberTimeRange"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            range-separator="至"
            unlink-panels
            clearable
          />
        </div>
        <div class="filter-item">
          <div class="filter-item-label">活跃状态</div>
          <el-select v-model="manualFilters.activityScene" placeholder="请选择活跃状态" clearable>
            <el-option v-for="item in activitySceneOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </div>
        <div class="filter-item">
          <div class="filter-item-label">累计消费区间</div>
          <div class="number-range">
            <el-input-number v-model="manualFilters.minConsumeAmount" :min="0" :precision="2" :step="100" controls-position="right" />
            <span class="range-separator">至</span>
            <el-input-number v-model="manualFilters.maxConsumeAmount" :min="0" :precision="2" :step="100" controls-position="right" />
          </div>
        </div>
        <div class="filter-item">
          <div class="filter-item-label">累计下单区间</div>
          <div class="number-range">
            <el-input-number v-model="manualFilters.minOrderCount" :min="0" :precision="0" :step="1" controls-position="right" />
            <span class="range-separator">至</span>
            <el-input-number v-model="manualFilters.maxOrderCount" :min="0" :precision="0" :step="1" controls-position="right" />
          </div>
        </div>
      </div>
      <el-alert v-if="activeFilterText" :title="activeFilterText" type="info" :closable="false" show-icon />
    </div>
    <div class="insight-panel">
      <div class="insight-header">
        <div class="filter-title">当前页洞察</div>
        <div class="insight-sub-title">基于当前分页数据快速识别待转化、可唤醒和高价值用户。</div>
      </div>
      <div class="insight-grid">
        <div v-for="item in pageInsightCards" :key="item.label" class="insight-card">
          <div class="insight-label">{{ item.label }}</div>
          <div class="insight-value" :class="item.emphasisClass">{{ item.value }}</div>
          <div class="insight-tip">{{ item.tip }}</div>
        </div>
      </div>
    </div>
    <div class="tag-panel">
      <div class="tag-panel-header">
        <div>
          <div class="filter-title">运营标签分布</div>
          <div class="insight-sub-title">按当前筛选口径统计常用标签，支持一键筛选和给已选会员快速打标。</div>
        </div>
        <div class="tag-panel-actions">
          <el-button v-if="checkPermi(['mall:malluser:edit'])" plain @click="openTagLibraryDialog">标签库管理</el-button>
          <el-button link type="primary" :disabled="!manualFilters.quickTag" @click="clearQuickTagFilter">清空标签筛选</el-button>
          <el-button v-if="checkPermi(['mall:malluser:edit'])" type="primary" plain :disabled="selectionData.length <= 0" @click="batchUpdateTag">
            自定义批量打标签
          </el-button>
        </div>
      </div>
      <div class="tag-summary-grid" v-if="tagSummaryCards.length">
        <button
          v-for="item in tagSummaryCards"
          :key="item.tag"
          type="button"
          class="tag-summary-card"
          :class="{ 'is-active': manualFilters.quickTag === item.tag }"
          @click="applyQuickTagFilter(item.tag)"
        >
          <div class="tag-summary-name">{{ item.tag }}</div>
          <div class="tag-summary-count">{{ item.count }} 人</div>
          <div class="tag-summary-tip">覆盖当前用户池 {{ item.coverageText }}</div>
        </button>
      </div>
      <el-empty v-else description="当前筛选结果暂无运营标签"></el-empty>
      <div class="tag-toolbar-group">
        <div class="group-label">常用标签快捷按钮</div>
        <div class="tag-chip-list">
          <button
            v-for="tag in commonTagOptions"
            :key="tag"
            type="button"
            class="tag-chip-btn"
            :class="{ 'is-active': manualFilters.quickTag === tag }"
            @click="applyQuickTagFilter(tag)"
          >
            {{ tag }}
          </button>
        </div>
      </div>
      <div v-if="selectionData.length > 0 && checkPermi(['mall:malluser:edit'])" class="tag-toolbar-group">
        <div class="group-label">给已选 {{ selectionData.length }} 人快速打标签</div>
        <div class="tag-chip-list">
          <button v-for="tag in commonTagOptions" :key="`selection-${tag}`" type="button" class="tag-chip-btn is-batch" @click="quickBatchTag(tag)">
            {{ tag }}
          </button>
        </div>
      </div>
    </div>
    <avue-crud
      ref="crud"
      v-model="form"
      :page="page"
      :data="tableData"
      :table-loading="tableLoading"
      :option="tableOption"
      :permission="permissionList"
      :before-open="beforeOpen"
      @on-load="getPageF"
      @refresh-change="refreshChange"
      @row-update="handleUpdate"
      @sort-change="sortChange"
      @search-change="searchChange"
      @selection-change="selectionChange"
    >
      <template #menu-left>
        <div class="batch-action-bar">
          <div class="batch-action-text">已选 {{ selectionData.length }} 人</div>
          <el-button v-if="checkPermi(['mall:malluser:index'])" type="primary" plain :loading="tableLoading" @click="handleExportCurrentFilter">
            导出当前筛选
          </el-button>
          <el-button v-if="checkPermi(['mall:malluser:edit'])" type="success" plain :disabled="selectionData.length <= 0" @click="batchChangeStatus('0')">
            批量启用
          </el-button>
          <el-button v-if="checkPermi(['mall:malluser:edit'])" type="warning" plain :disabled="selectionData.length <= 0" @click="batchChangeStatus('1')">
            批量禁用
          </el-button>
          <el-button v-if="checkPermi(['mall:malluser:edit'])" type="info" plain :disabled="selectionData.length <= 0" @click="batchUpdateTag">
            批量打标签
          </el-button>
          <el-button v-if="checkPermi(['mall:malluser:edit'])" type="primary" plain :disabled="selectionData.length <= 0" @click="batchUpdateRemark">
            批量备注
          </el-button>
          <el-button plain :disabled="selectionData.length <= 0" @click="clearSelection">
            清空选择
          </el-button>
        </div>
      </template>
      <template #avatarUrl="scope">
        <el-avatar :src="resolveMallUserImage(scope.row.avatarUrl)" icon="el-icon-user-solid"></el-avatar>
      </template>
      <template #profileSummary-form>
        <div class="detail-overview">
          <div class="detail-profile-card">
            <el-avatar :src="resolveMallUserImage(form.avatarUrl)" icon="el-icon-user-solid" :size="72"></el-avatar>
            <div class="detail-profile-main">
              <div class="detail-profile-name">{{ form.nickName || "微信用户" }}</div>
              <div class="detail-profile-sub">会员编号：{{ form.userNo || "未生成" }}</div>
              <div class="detail-profile-sub">商城用户ID：{{ form.id || "未生成" }}</div>
              <div class="detail-profile-sub">
                运营标签：
                <template v-if="buildUserTags(form).length">
                  <el-tag v-for="item in buildUserTags(form)" :key="item" size="small" effect="light" class="inline-tag">
                    {{ item }}
                  </el-tag>
                </template>
                <span v-else>未打标签</span>
              </div>
              <div class="detail-profile-sub">
                {{ form.realName || "未填写真实姓名" }}
                <span class="detail-divider">|</span>
                {{ formatSex(form.sex) }}
                <span class="detail-divider">|</span>
                {{ maskMobile(form.mobile) }}
              </div>
              <div class="detail-profile-sub">
                会员身份：{{ formatMemberIdentity(form.memberFlag) }}
                <span class="detail-divider">|</span>
                入会时间：{{ formatDateTime(form.memberTime) }}
              </div>
              <div class="detail-profile-sub">
                注册来源：{{ formatRegisterSource(form.registerSource) }}
                <span class="detail-divider">|</span>
                最近下单：{{ formatDateTime(form.lastOrderTime) }}
              </div>
              <div class="detail-profile-sub">
                最近活跃：{{ formatDateTime(form.recentActiveTime) }}
                <span class="detail-divider">|</span>
                入会天数：{{ formatMemberAgeDays(form.memberAgeDays) }}
              </div>
              <div class="detail-profile-sub">
                最近支付：{{ formatDateTime(form.recentPaymentTime) }}
                <span class="detail-divider">|</span>
                支付单号：{{ form.recentPaymentOrderNo || "暂无" }}
              </div>
            </div>
          </div>
          <div class="detail-stat-grid">
            <div class="detail-stat-card">
              <div class="detail-stat-label">累计下单</div>
              <div class="detail-stat-value">{{ Number(form.orderCount || 0) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">累计支付</div>
              <div class="detail-stat-value">{{ Number(form.consumeCount || 0) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">累计退款</div>
              <div class="detail-stat-value">{{ Number(form.refundCount || 0) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">累计消费</div>
              <div class="detail-stat-value amount-text">¥{{ Number(form.consumeAmount || 0).toFixed(2) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">待付款订单</div>
              <div class="detail-stat-value">{{ Number(form.waitPayOrderCount || 0) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">待发货订单</div>
              <div class="detail-stat-value">{{ Number(form.waitDeliveryOrderCount || 0) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">待收货订单</div>
              <div class="detail-stat-value">{{ Number(form.waitReceiveOrderCount || 0) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">收货地址数</div>
              <div class="detail-stat-value">{{ Number(form.addressCount || 0) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">近30天下单</div>
              <div class="detail-stat-value">{{ Number(form.thirtyDayOrderCount || 0) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">近30天消费</div>
              <div class="detail-stat-value amount-text">￥{{ Number(form.thirtyDayConsumeAmount || 0).toFixed(2) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">售后件数</div>
              <div class="detail-stat-value">{{ Number(form.afterSaleCount || 0) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">已退款金额</div>
              <div class="detail-stat-value amount-text">￥{{ Number(form.refundedAmount || 0).toFixed(2) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">购物车SKU</div>
              <div class="detail-stat-value">{{ Number(form.cartSkuCount || 0) }}</div>
            </div>
            <div class="detail-stat-card">
              <div class="detail-stat-label">购物车件数</div>
              <div class="detail-stat-value">{{ Number(form.cartQuantity || 0) }}</div>
            </div>
          </div>
          <div class="detail-address-banner">
            <div class="detail-address-label">默认收货地址</div>
            <div class="detail-address-text">{{ form.defaultAddressText || "暂无收货地址" }}</div>
          </div>
        </div>
      </template>
      <template #nickName="scope">
        <div class="user-cell">
          <el-avatar :src="resolveMallUserImage(scope.row.avatarUrl)" icon="el-icon-user-solid"></el-avatar>
          <div class="user-meta">
            <div class="user-name">{{ scope.row.nickName || "微信用户" }}</div>
            <div class="user-id">{{ scope.row.id }}</div>
            <div class="user-segment-row">
              <el-tag size="small" effect="light" :type="buildUserSegment(scope.row).type">
                {{ buildUserSegment(scope.row).label }}
              </el-tag>
            </div>
            <el-button type="primary" link class="detail-link-btn" @click="openDetail(scope.row)">
              查看会员详情
            </el-button>
            <el-button type="success" link class="detail-link-btn" @click="openAfterSalePage(scope.row)">
              查看售后
            </el-button>
          </div>
        </div>
      </template>
      <template #sex="scope">
        <el-tag size="small" effect="light" :type="scope.row.sex === '1' ? 'success' : scope.row.sex === '2' ? 'danger' : 'info'">
          {{ scope.row.sex === "1" ? "男" : scope.row.sex === "2" ? "女" : "未知" }}
        </el-tag>
      </template>
      <template #registerSource="scope">
        <el-tag size="small" effect="plain">
          {{ formatRegisterSource(scope.row.registerSource) }}
        </el-tag>
      </template>
      <template #memberFlag="scope">
        <el-tag size="small" effect="light" :type="scope.row.memberFlag === '1' ? 'success' : 'info'">
          {{ formatMemberIdentity(scope.row.memberFlag) }}
        </el-tag>
      </template>
      <template #userTag="scope">
        <div v-if="buildUserTags(scope.row).length" class="user-tag-list">
          <el-tag v-for="item in buildUserTags(scope.row)" :key="item" size="small" effect="plain">
            {{ item }}
          </el-tag>
        </div>
        <span v-else class="empty-tag-text">未打标签</span>
      </template>
      <template #userSegment="scope">
        <el-tag size="small" effect="light" :type="buildUserSegment(scope.row).type">
          {{ buildUserSegment(scope.row).label }}
        </el-tag>
      </template>
      <template #memberAgeDays="scope">
        <span>{{ buildMemberAgeText(scope.row) }}</span>
      </template>
      <template #activityLevel="scope">
        <el-tag size="small" effect="light" :type="buildActivityLevel(scope.row).type">
          {{ buildActivityLevel(scope.row).label }}
        </el-tag>
      </template>
      <template #status="scope">
        <el-switch
          v-model="scope.row.status"
          active-value="0"
          inactive-value="1"
          active-text="正常"
          inactive-text="禁用"
          @change="changeStatus(scope.row)"
        />
      </template>
      <template #consumeAmount="scope">
        <span class="amount-text">¥{{ Number(scope.row.consumeAmount || 0).toFixed(2) }}</span>
      </template>
      <template #recentOrderList-form>
        <div class="detail-section">
          <div class="detail-section-title">最近订单</div>
          <el-table v-if="(form.recentOrderList || []).length" :data="form.recentOrderList || []" border style="width: 100%">
            <el-table-column prop="orderNo" label="订单号" min-width="180"></el-table-column>
            <el-table-column prop="name" label="订单名称" min-width="160">
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
          </el-table>
          <el-empty v-else description="该会员暂无订单记录"></el-empty>
        </div>
      </template>
      <template #recentAddressList-form>
        <div class="detail-section">
          <div class="detail-section-title">收货地址</div>
          <el-table v-if="(form.recentAddressList || []).length" :data="form.recentAddressList || []" border style="width: 100%">
            <el-table-column prop="userName" label="收货人" width="100" align="center"></el-table-column>
            <el-table-column prop="telNum" label="手机号" width="140" align="center">
              <template #default="scope">
                {{ maskMobile(scope.row.telNum) }}
              </template>
            </el-table-column>
            <el-table-column prop="addressText" label="地址" min-width="260">
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
        </div>
      </template>
    </avue-crud>
    <el-dialog v-model="tagDialog.visible" title="批量打标签" width="680px" destroy-on-close>
      <div class="tag-dialog-body">
        <div class="tag-dialog-tip">已选 {{ selectionData.length }} 人。可直接点选常用标签，也可自定义补充多个标签，系统会自动去重并用中文逗号保存。</div>
        <div class="tag-toolbar-group">
          <div class="group-label">常用标签</div>
          <div class="tag-chip-list">
            <button
              v-for="tag in commonTagOptions"
              :key="`dialog-${tag}`"
              type="button"
              class="tag-chip-btn"
              :class="{ 'is-active': tagDialog.selectedTags.includes(tag) }"
              @click="toggleDialogTag(tag)"
            >
              {{ tag }}
            </button>
          </div>
        </div>
        <div class="tag-toolbar-group">
          <div class="group-label">自定义标签</div>
          <div class="tag-custom-input-row">
            <el-input
              v-model="tagDialog.customTag"
              maxlength="20"
              show-word-limit
              clearable
              placeholder="输入后回车或点击添加，例如：4月首购、短信唤醒"
              @keyup.enter="appendCustomTag"
            />
            <el-button type="primary" plain @click="appendCustomTag">添加标签</el-button>
          </div>
        </div>
        <div class="tag-toolbar-group">
          <div class="group-label">本次将写入</div>
          <div v-if="tagDialog.selectedTags.length" class="tag-selected-list">
            <el-tag v-for="tag in tagDialog.selectedTags" :key="`selected-${tag}`" closable effect="plain" @close="removeDialogTag(tag)">
              {{ tag }}
            </el-tag>
          </div>
          <el-empty v-else description="请先选择或输入标签"></el-empty>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeTagDialog">取消</el-button>
          <el-button type="primary" :loading="tagDialog.submitting" @click="submitBatchTag">确认打标签</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="tagLibraryDialog.visible" title="标签库管理" width="920px" destroy-on-close @closed="resetTagLibraryForm">
      <div class="tag-library-layout">
        <div class="tag-library-form-card">
          <div class="tag-library-form-title">{{ tagLibraryDialog.form.id ? "编辑标签" : "新增标签" }}</div>
          <el-form label-width="88px">
            <el-form-item label="标签名称">
              <el-input v-model="tagLibraryDialog.form.tagName" maxlength="20" show-word-limit clearable placeholder="例如：高价值会员" />
            </el-form-item>
            <el-form-item label="状态">
              <el-radio-group v-model="tagLibraryDialog.form.status">
                <el-radio-button label="0">启用</el-radio-button>
                <el-radio-button label="1">停用</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="排序">
              <el-input-number v-model="tagLibraryDialog.form.sort" :min="0" :step="10" controls-position="right" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="tagLibraryDialog.form.remark" type="textarea" :rows="3" maxlength="100" show-word-limit placeholder="可选，记录该标签适用场景" />
            </el-form-item>
          </el-form>
          <div class="tag-library-form-actions">
            <el-button @click="resetTagLibraryForm">重置</el-button>
            <el-button type="primary" :loading="tagLibraryDialog.submitting" @click="submitTagLibraryForm">保存标签</el-button>
          </div>
        </div>
        <div class="tag-library-table-card">
          <div class="tag-library-table-header">
            <div class="tag-library-form-title">标签库列表</div>
            <el-button link type="primary" :loading="tagLibraryDialog.loading" @click="loadTagLibrary">刷新列表</el-button>
          </div>
          <div class="tag-library-table-wrap">
            <el-table v-loading="tagLibraryDialog.loading" :data="tagLibraryDialog.list" border style="width: 100%">
              <el-table-column prop="tagName" label="标签名称" min-width="150"></el-table-column>
              <el-table-column prop="useCount" label="使用人数" width="100" align="center"></el-table-column>
              <el-table-column prop="sort" label="排序" width="90" align="center"></el-table-column>
              <el-table-column prop="status" label="状态" width="110" align="center">
                <template #default="scope">
                  <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" active-text="启用" inactive-text="停用" @change="changeTagStatus(scope.row)" />
                </template>
              </el-table-column>
              <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip></el-table-column>
              <el-table-column prop="updateTime" label="更新时间" width="170" align="center">
                <template #default="scope">
                  {{ formatDateTime(scope.row.updateTime || scope.row.createTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100" align="center">
                <template #default="scope">
                  <el-button type="primary" link @click="editTagLibraryItem(scope.row)">编辑</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="MallUser">
import { checkPermi } from "@/utils/permission";
import { addTag, exportMallUser, getPage, getObj, getSummary, getTagLibrary, getTagOptions, getTagSummary, putBatchRemark, putBatchStatus, putBatchTag, putObj, putTag } from "@/api/mall/malluser";
import { resolveMallImageUrl } from "@/utils/mall-image";
import { tableOption } from "@/const/crud/mall/malluser";

const { proxy } = getCurrentInstance();

/**
 * 统一处理商城用户列表与弹窗中的头像地址。
 *
 * @param {string} imageUrl 图片地址
 * @returns {string}
 */
function resolveMallUserImage(imageUrl) {
  return resolveMallImageUrl(imageUrl);
}
const router = useRouter();
const crud = ref(null);

const quickSceneOptions = [
  { label: "全部用户", value: "all" },
  { label: "已入会", value: "member" },
  { label: "未入会", value: "guest" },
  { label: "正常会员", value: "normalMember" },
  { label: "已下单", value: "ordered" },
  { label: "已支付", value: "paid" },
  { label: "禁用账号", value: "disabled" },
];

/**
 * 活跃状态筛选项。
 * 这里的编码需要和后端 MallUserController / MallUserMapper.xml 保持一致。
 */
const activitySceneOptions = [
  { label: "近7天活跃", value: "active7" },
  { label: "近30天可唤醒", value: "wake30" },
  { label: "30天沉睡", value: "silent30" },
  { label: "从未登录", value: "neverLogin" },
];

/**
 * 运营预设按钮。
 * 每个预设本质上都是一组人工筛选条件，方便运营同学一键圈选目标人群。
 */
const presetSceneOptions = [
  { label: "近7天待转化", value: "newGuest7", tip: "最近7天注册且未绑定手机号" },
  { label: "近7天新会员", value: "newMember7", tip: "最近7天入会的新增会员" },
  { label: "30天沉睡会员", value: "silentMember30", tip: "已入会且 30 天未登录或从未登录" },
  { label: "高价值会员", value: "highValueMember", tip: "已入会且累计消费满 1000 元" },
  { label: "复购会员", value: "repeatMember", tip: "累计下单 3 次及以上的已支付会员" },
];

/**
 * 后台默认推荐标签。
 * 当标签库尚未配置任何启用标签时，先用这组兜底选项保障页面可操作。
 */
const fallbackTagOptions = ["高价值会员", "复购会员", "新会员", "待转化", "沉睡召回", "短信唤醒", "活动邀约", "4月首购"];

/**
 * 页面状态集中放到一个响应式对象内，方便列表、查询和弹窗共用。
 */
const data = reactive({
  form: {},
  tableData: [],
  page: {
    total: 0,
    currentPage: 1,
    pageSize: 20,
    ascs: [],
    descs: "create_time",
  },
  summary: {
    totalCount: 0,
    memberCount: 0,
    nonMemberCount: 0,
    normalMemberCount: 0,
    normalCount: 0,
    disabledCount: 0,
    orderUserCount: 0,
    consumeUserCount: 0,
    consumeAmount: 0,
  },
  paramsSearch: {},
  manualFilters: createDefaultManualFilters(),
  tagOptionList: [],
  tagSummaryList: [],
  tableLoading: false,
  selectionData: [],
  tagDialog: {
    visible: false,
    customTag: "",
    selectedTags: [],
    submitting: false,
  },
  tagLibraryDialog: {
    visible: false,
    loading: false,
    submitting: false,
    list: [],
    form: createDefaultTagLibraryForm(),
  },
});

const { form, tableData, tableLoading, page, manualFilters, selectionData, tagDialog, tagLibraryDialog } = toRefs(data);

const summaryCardsView = computed(() => {
  const summary = data.summary || {};
  const totalCount = Number(summary.totalCount || data.page.total || 0);
  const memberCount = Number(summary.memberCount || 0);
  const normalMemberCount = Number(summary.normalMemberCount || 0);
  const orderUserCount = Number(summary.orderUserCount || 0);
  const consumeUserCount = Number(summary.consumeUserCount || 0);
  const conversionRate = totalCount > 0 ? `${((memberCount / totalCount) * 100).toFixed(1)}%` : "0.0%";
  return [
    { label: "用户池总数", value: `${totalCount} 人`, tip: "当前筛选口径下的商城用户总数", emphasisClass: "is-primary" },
    { label: "会员转化率", value: conversionRate, tip: "已绑定手机号的会员占当前用户池比例", emphasisClass: "is-success" },
    { label: "正常会员", value: `${normalMemberCount} 人`, tip: "状态正常且已入会的会员数量", emphasisClass: "is-success" },
    { label: "已下单用户", value: `${orderUserCount} 人`, tip: "至少提交过一笔订单的用户数", emphasisClass: "is-warning" },
    { label: "已支付用户", value: `${consumeUserCount} 人`, tip: "至少完成过一笔支付订单的用户数", emphasisClass: "is-primary" },
    { label: "累计消费额", value: `￥${Number(summary.consumeAmount || 0).toFixed(2)}`, tip: "当前筛选用户池历史累计支付金额汇总", emphasisClass: "is-danger" },
  ];
});

const activeFilterText = computed(() => {
  const texts = [];
  if (data.manualFilters.quickScene !== "all") {
    texts.push(`快捷分层：${getQuickSceneLabel(data.manualFilters.quickScene)}`);
  }
  if ((data.manualFilters.registerTimeRange || []).length === 2) {
    texts.push(`注册时间：${data.manualFilters.registerTimeRange[0]} 至 ${data.manualFilters.registerTimeRange[1]}`);
  }
  if ((data.manualFilters.lastLoginTimeRange || []).length === 2) {
    texts.push(`最近登录：${data.manualFilters.lastLoginTimeRange[0]} 至 ${data.manualFilters.lastLoginTimeRange[1]}`);
  }
  if ((data.manualFilters.memberTimeRange || []).length === 2) {
    texts.push(`入会时间：${data.manualFilters.memberTimeRange[0]} 至 ${data.manualFilters.memberTimeRange[1]}`);
  }
  if (data.manualFilters.activityScene) {
    texts.push(`活跃状态：${getActivitySceneLabel(data.manualFilters.activityScene)}`);
  }
  if (data.manualFilters.quickTag) {
    texts.push(`运营标签：${data.manualFilters.quickTag}`);
  }
  if (data.manualFilters.minConsumeAmount !== null || data.manualFilters.maxConsumeAmount !== null) {
    texts.push(`累计消费：${data.manualFilters.minConsumeAmount ?? 0} ~ ${data.manualFilters.maxConsumeAmount ?? "不限"}`);
  }
  if (data.manualFilters.minOrderCount !== null || data.manualFilters.maxOrderCount !== null) {
    texts.push(`累计下单：${data.manualFilters.minOrderCount ?? 0} ~ ${data.manualFilters.maxOrderCount ?? "不限"}`);
  }
  return texts.join("；");
});

/**
 * 标签分布卡片。
 * count 由后端按当前筛选口径返回，这里补一个覆盖率文案，方便运营判断标签密度。
 */
const tagSummaryCards = computed(() => {
  const totalCount = Number(data.summary?.totalCount || data.page.total || 0);
  return (data.tagSummaryList || []).map((item) => {
    const count = Number(item?.count || 0);
    return {
      tag: item?.tag || "",
      count,
      coverageText: totalCount > 0 ? `${((count / totalCount) * 100).toFixed(1)}%` : "0.0%",
    };
  });
});

/**
 * 常用标签按钮。
 * 优先展示真实数据里已经出现过的标签，再补默认推荐标签，避免页面空白。
 */
const commonTagOptions = computed(() => {
  const tagSet = new Set();
  (data.tagOptionList || []).forEach((item) => {
    const tag = normalizeTagText(item?.tagName);
    if (tag) {
      tagSet.add(tag);
    }
  });
  (data.tagSummaryList || []).forEach((item) => {
    const tag = normalizeTagText(item?.tag);
    if (tag) {
      tagSet.add(tag);
    }
  });
  fallbackTagOptions.forEach((tag) => {
    const normalizedTag = normalizeTagText(tag);
    if (normalizedTag) {
      tagSet.add(normalizedTag);
    }
  });
  return Array.from(tagSet).slice(0, 10);
});

/**
 * 当前页洞察卡片。
 * 这里故意只基于当前页数据计算，避免误导为全量统计，同时能让运营快速看到当前页的人群结构。
 */
const pageInsightCards = computed(() => {
  const rows = data.tableData || [];
  const pendingCount = rows.filter((item) => item?.memberFlag !== "1").length;
  const repeatCount = rows.filter((item) => Number(item?.consumeCount || 0) > 0 && Number(item?.orderCount || 0) >= 3).length;
  const activeCount = rows.filter((item) => buildActivityLevel(item).label === "高活跃").length;
  const silentCount = rows.filter((item) => buildActivityLevel(item).label === "沉睡中").length;
  return [
    { label: "待转化", value: `${pendingCount} 人`, tip: "未绑定手机号，仍有转会员空间", emphasisClass: "is-warning" },
    { label: "复购会员", value: `${repeatCount} 人`, tip: "已形成稳定复购习惯，可承接会员权益", emphasisClass: "is-success" },
    { label: "高活跃", value: `${activeCount} 人`, tip: "近 7 天有登录记录，适合做新品触达", emphasisClass: "is-primary" },
    { label: "沉睡中", value: `${silentCount} 人`, tip: "超过 30 天未登录，适合做唤醒任务", emphasisClass: "is-danger" },
  ];
});

const permissionList = computed(() => {
  return {
    addBtn: false,
    delBtn: false,
    editBtn: checkPermi(["mall:malluser:edit"]),
    viewBtn: checkPermi(["mall:malluser:get"]),
  };
});

/**
 * 统一格式化注册来源，保证表格插槽和弹窗文案一致。
 *
 * @param {string} registerSource 注册来源编码
 * @returns {string}
 */
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
 * 会员身份格式化。
 * 当前口径：绑定手机号后即视为已入会。
 *
 * @param {string} memberFlag 会员标记
 * @returns {string}
 */
function formatMemberIdentity(memberFlag) {
  return memberFlag === "1" ? "已入会" : "未入会";
}

/**
 * 规范化单个运营标签。
 *
 * @param {string} tag 标签文本
 * @returns {string}
 */
function normalizeTagText(tag) {
  return String(tag || "").trim().replace(/[，,、]+/g, "");
}

/**
 * 解析运营标签列表。
 * 当前后端存的是单字段文本，这里兼容中文逗号、英文逗号和顿号，方便后续扩展多标签展示。
 *
 * @param {Object} row 用户数据
 * @returns {string[]}
 */
function buildUserTags(row) {
  const tagText = String(row?.userTag || "").trim();
  if (!tagText) {
    return [];
  }
  return tagText
    .split(/[，,、]/)
    .map((item) => normalizeTagText(item))
    .filter(Boolean);
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
 * 订单状态展示文案。
 *
 * @param {Object} order 订单
 * @returns {string}
 */
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

/**
 * 订单状态标签颜色。
 *
 * @param {Object} order 订单
 * @returns {string}
 */
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

/**
 * 订单商品摘要。
 *
 * @param {Object} order 订单
 * @returns {string}
 */
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
 * 地址摘要拼装。
 *
 * @param {Object} address 地址对象
 * @returns {string}
 */
function buildAddressText(address) {
  if (!address) {
    return "暂无收货地址";
  }
  return `${address.provinceName || ""}${address.cityName || ""}${address.countyName || ""}${address.detailInfo || ""}`;
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
 * 打开独立会员详情页。
 *
 * @param {Object} row 会员行数据
 */
function openDetail(row) {
  if (!row?.id) {
    return;
  }
  router.push({
    path: "/mall/malluser-detail/index",
    query: {
      id: row.id,
    },
  });
}

/**
 * 直接跳转到会员售后工作台。
 * 客服从会员列表进入时，默认按当前会员过滤，减少重复输入搜索条件。
 *
 * @param {Object} row 会员行数据
 */
function openAfterSalePage(row) {
  if (!row?.id) {
    return;
  }
  router.push({
    path: "/mall/aftersale",
    query: {
      userId: row.id,
      userNo: row.userNo || "",
      userNickName: row.nickName || "",
      afterSaleStatus: "ALL",
    },
  });
}

/**
 * 打开查看/编辑抽屉前，先根据主键拉取最新会员信息。
 *
 * @param {Function} done 抽屉继续回调
 * @param {string} type 弹窗类型
 */
function beforeOpen(done, type) {
  if (type === "add") {
    done();
    return;
  }
  if (!data.form?.id) {
    done();
    return;
  }
  data.tableLoading = true;
  getObj(data.form.id)
    .then((response) => {
      data.form = response.data || {};
      data.tableLoading = false;
      done();
    })
    .catch(() => {
      data.tableLoading = false;
      done();
    });
}

/**
 * 快速切换会员状态。
 * 这里只提交后台允许维护的资料字段，避免把统计字段误覆盖回数据库。
 *
 * @param {Object} row 当前行数据
 */
function changeStatus(row) {
  if (!row?.id) {
    return;
  }
  const originStatus = row.status === "0" ? "1" : "0";
  putObj({
    id: row.id,
    realName: row.realName,
    sex: row.sex,
    birthday: row.birthday,
    userTag: row.userTag,
    status: row.status,
    remark: row.remark,
  })
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "状态更新成功",
        type: "success",
      });
    })
    .catch(() => {
      row.status = originStatus;
    });
}

/**
 * 搜索回调。
 * 每次搜索都重置到第一页，避免分页停留导致结果错位。
 *
 * @param {Object} params 搜索参数
 * @param {Function} done Avue 完成回调
 */
function searchChange(params, done) {
  params = proxy.filterForm(params);
  data.paramsSearch = params;
  data.page.currentPage = 1;
  getPageF(data.page, params);
  done();
}

/**
 * 排序回调。
 * Avue 返回的是驼峰字段，这里统一转成后端分页接口需要的下划线字段。
 *
 * @param {Object} val 排序信息
 */
function sortChange(val) {
  let prop = val.prop ? val.prop.replace(/([A-Z])/g, "_$1").toLowerCase() : "";
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
 * 加载分页数据。
 *
 * @param {Object} pageInfo 分页对象
 * @param {Object} params 临时查询参数
 */
function buildQuickSceneQuery(quickScene) {
  switch (quickScene) {
    case "member":
      return { memberFlag: "1" };
    case "guest":
      return { memberFlag: "0" };
    case "normalMember":
      return { memberFlag: "1", status: "0" };
    case "ordered":
      return { hasOrder: 1 };
    case "paid":
      return { hasConsume: 1 };
    case "disabled":
      return { status: "1" };
    default:
      return {};
  }
}

/**
 * 生成默认筛选对象。
 * 单独抽成函数，方便重置条件和应用运营预设时复用，避免不同入口漏字段。
 *
 * @returns {Object}
 */
function createDefaultManualFilters() {
  return {
    quickScene: "all",
    quickTag: "",
    registerTimeRange: [],
    lastLoginTimeRange: [],
    memberTimeRange: [],
    activityScene: "",
    minConsumeAmount: null,
    maxConsumeAmount: null,
    minOrderCount: null,
    maxOrderCount: null,
  };
}

/**
 * 生成标签库表单默认值。
 *
 * @returns {Object}
 */
function createDefaultTagLibraryForm() {
  return {
    id: "",
    tagName: "",
    status: "0",
    sort: 0,
    remark: "",
  };
}

function buildManualQuery() {
  const query = {
    ...buildQuickSceneQuery(data.manualFilters.quickScene),
  };
  if (data.manualFilters.quickTag) {
    query.userTag = data.manualFilters.quickTag;
  }
  if ((data.manualFilters.registerTimeRange || []).length === 2) {
    query.registerTimeBegin = `${data.manualFilters.registerTimeRange[0]} 00:00:00`;
    query.registerTimeEnd = `${data.manualFilters.registerTimeRange[1]} 23:59:59`;
  }
  if ((data.manualFilters.lastLoginTimeRange || []).length === 2) {
    query.lastLoginTimeBegin = `${data.manualFilters.lastLoginTimeRange[0]} 00:00:00`;
    query.lastLoginTimeEnd = `${data.manualFilters.lastLoginTimeRange[1]} 23:59:59`;
  }
  if ((data.manualFilters.memberTimeRange || []).length === 2) {
    query.memberTimeBegin = `${data.manualFilters.memberTimeRange[0]} 00:00:00`;
    query.memberTimeEnd = `${data.manualFilters.memberTimeRange[1]} 23:59:59`;
  }
  if (data.manualFilters.activityScene) {
    query.activityScene = data.manualFilters.activityScene;
  }
  if (data.manualFilters.minConsumeAmount !== null && data.manualFilters.minConsumeAmount !== undefined) {
    query.minConsumeAmount = data.manualFilters.minConsumeAmount;
  }
  if (data.manualFilters.maxConsumeAmount !== null && data.manualFilters.maxConsumeAmount !== undefined) {
    query.maxConsumeAmount = data.manualFilters.maxConsumeAmount;
  }
  if (data.manualFilters.minOrderCount !== null && data.manualFilters.minOrderCount !== undefined) {
    query.minOrderCount = data.manualFilters.minOrderCount;
  }
  if (data.manualFilters.maxOrderCount !== null && data.manualFilters.maxOrderCount !== undefined) {
    query.maxOrderCount = data.manualFilters.maxOrderCount;
  }
  return query;
}

function buildQueryParams(pageInfo, params = {}) {
  return Object.assign(
    {
      current: pageInfo.currentPage,
      size: pageInfo.pageSize,
      descs: data.page.descs,
      ascs: data.page.ascs,
    },
    data.paramsSearch,
    buildManualQuery(),
    params
  );
}

/**
 * 查询启用中的标签选项。
 * 用于快捷筛选、快捷打标签和批量打标签弹窗。
 */
function loadTagOptions() {
  return getTagOptions().then((response) => {
    data.tagOptionList = Array.isArray(response.data) ? response.data : [];
  });
}

/**
 * 查询标签库。
 * 标签库管理弹窗和页面按钮都复用这份数据。
 */
function loadTagLibrary() {
  data.tagLibraryDialog.loading = true;
  return getTagLibrary()
    .then((response) => {
      data.tagLibraryDialog.list = Array.isArray(response.data) ? response.data : [];
      data.tagLibraryDialog.loading = false;
    })
    .catch(() => {
      data.tagLibraryDialog.loading = false;
    });
}

/**
 * 同步刷新标签库相关数据。
 */
function refreshTagLibraryState() {
  return Promise.all([loadTagOptions(), loadTagLibrary()]);
}

/**
 * 生成导出查询参数。
 * 导出不需要分页字段，但需要保留当前搜索、筛选和排序条件，确保导出结果与列表口径一致。
 *
 * @returns {Object}
 */
function buildExportQuery() {
  const query = buildQueryParams(data.page);
  const { current, size, ...exportQuery } = query;
  return exportQuery;
}

function getPageF(pageInfo, params) {
  data.tableLoading = true;
  const query = buildQueryParams(pageInfo, params);
  Promise.all([getPage(query), getSummary(query), getTagSummary(query)])
    .then(([pageResponse, summaryResponse, tagSummaryResponse]) => {
      const records = pageResponse.data?.records || [];
      data.tableData = records;
      data.selectionData = [];
      data.page.total = pageResponse.data?.total || 0;
      data.page.currentPage = pageInfo.currentPage;
      data.page.pageSize = pageInfo.pageSize;
      data.summary = Object.assign(
        {
          totalCount: 0,
          memberCount: 0,
          nonMemberCount: 0,
          normalMemberCount: 0,
          normalCount: 0,
          disabledCount: 0,
          orderUserCount: 0,
          consumeUserCount: 0,
          consumeAmount: 0,
        },
        summaryResponse.data || {}
      );
      data.tagSummaryList = Array.isArray(tagSummaryResponse.data) ? tagSummaryResponse.data : [];
      data.tableLoading = false;
    })
    .catch(() => {
      data.tagSummaryList = [];
      data.tableLoading = false;
    });
}

/**
 * 提交会员资料修改。
 * 后台只允许维护商城自有字段，成功后刷新当前分页保持页面状态不丢失。
 *
 * @param {Object} row 编辑后的行数据
 * @param {number} index 行索引
 * @param {Function} done 关闭弹窗
 * @param {Function} loading 关闭加载状态
 */
function handleUpdate(row, index, done, loading) {
  putObj(row)
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "修改成功",
        type: "success",
      });
      done();
      getPageF(data.page);
    })
    .catch(() => {
      loading();
    });
}

/**
 * 记录表格当前选中项。
 * 后续批量启用、禁用、备注都依赖这里维护的选中结果。
 *
 * @param {Object[]} list 当前选中行
 */
function selectionChange(list) {
  data.selectionData = Array.isArray(list) ? list : [];
}

/**
 * 清空当前表格选中项。
 * 调用 Avue 内部的表格清空能力，避免只清本地状态导致 UI 不一致。
 */
function clearSelection() {
  if (crud.value?.selectClear) {
    crud.value.selectClear();
  }
  data.selectionData = [];
}

/**
 * 获取批量操作的用户主键集合。
 *
 * @returns {string[]}
 */
function getSelectionIds() {
  return (data.selectionData || [])
    .map((item) => item?.id)
    .filter(Boolean);
}

/**
 * 导出当前筛选结果。
 * 这里导出的是整个筛选结果集，而不是当前分页，方便运营直接拿去做外部分析或人工跟进。
 */
function handleExportCurrentFilter() {
  const totalCount = Number(data.summary?.totalCount || data.page.total || 0);
  if (totalCount <= 0) {
    proxy.$message.warning("当前没有可导出的商城用户数据");
    return;
  }
  exportMallUser(buildExportQuery());
}

/**
 * 批量更新会员状态。
 *
 * @param {string} status 状态值
 */
function batchChangeStatus(status) {
  const ids = getSelectionIds();
  if (ids.length <= 0) {
    proxy.$message.error("请先选择商城用户");
    return;
  }
  const actionText = status === "1" ? "禁用" : "启用";
  proxy
    .$confirm(`确认批量${actionText}已选中的 ${ids.length} 个商城用户吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      data.tableLoading = true;
      return putBatchStatus({ ids, status });
    })
    .then(() => {
      proxy.$message({
        showClose: true,
        message: `批量${actionText}成功`,
        type: "success",
      });
      clearSelection();
      getPageF(data.page);
    })
    .catch(() => {
      data.tableLoading = false;
    });
}

/**
 * 批量更新会员备注。
 * 统一通过弹窗采集本次运营说明，便于后续客服或运营追踪。
 */
function batchUpdateRemark() {
  const ids = getSelectionIds();
  if (ids.length <= 0) {
    proxy.$message.error("请先选择商城用户");
    return;
  }
  proxy
    .$prompt("请输入要批量写入的备注", "批量备注", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      inputType: "textarea",
      inputPlaceholder: "例如：4月回访名单、短信唤醒第一批",
      inputValidator: (value) => {
        if (!String(value || "").trim()) {
          return "备注不能为空";
        }
        return true;
      },
    })
    .then(({ value }) => {
      data.tableLoading = true;
      return putBatchRemark({
        ids,
        remark: value,
      });
    })
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "批量备注成功",
        type: "success",
      });
      clearSelection();
      getPageF(data.page);
    })
    .catch(() => {
      data.tableLoading = false;
    });
}

/**
 * 批量更新会员运营标签。
 * 轻量运营场景下，标签是比备注更适合拿来筛选和导出的结构化字段。
 */
function batchUpdateTag() {
  if (getSelectionIds().length <= 0) {
    proxy.$message.error("请先选择商城用户");
    return;
  }
  data.tagDialog.visible = true;
  data.tagDialog.customTag = "";
  data.tagDialog.selectedTags = [];
  loadTagOptions();
}

/**
 * 关闭标签弹窗并清理状态。
 */
function closeTagDialog() {
  data.tagDialog.visible = false;
  data.tagDialog.customTag = "";
  data.tagDialog.selectedTags = [];
  data.tagDialog.submitting = false;
}

/**
 * 勾选或取消一个待写入标签。
 *
 * @param {string} tag 标签
 */
function toggleDialogTag(tag) {
  const normalizedTag = normalizeTagText(tag);
  if (!normalizedTag) {
    return;
  }
  const index = data.tagDialog.selectedTags.indexOf(normalizedTag);
  if (index >= 0) {
    data.tagDialog.selectedTags.splice(index, 1);
    return;
  }
  data.tagDialog.selectedTags.push(normalizedTag);
}

/**
 * 移除待写入标签。
 *
 * @param {string} tag 标签
 */
function removeDialogTag(tag) {
  data.tagDialog.selectedTags = data.tagDialog.selectedTags.filter((item) => item !== tag);
}

/**
 * 追加自定义标签。
 * 输入框支持手工输入多个标签，自动做空格清理和去重。
 */
function appendCustomTag() {
  const tagList = String(data.tagDialog.customTag || "")
    .split(/[，,、]/)
    .map((item) => normalizeTagText(item))
    .filter(Boolean);
  if (!tagList.length) {
    proxy.$message.warning("请先输入要添加的标签");
    return;
  }
  const nextTagSet = new Set(data.tagDialog.selectedTags);
  tagList.forEach((item) => nextTagSet.add(item));
  data.tagDialog.selectedTags = Array.from(nextTagSet);
  data.tagDialog.customTag = "";
}

/**
 * 提交批量标签写入。
 * 最终统一用中文逗号保存，保证后端和页面展示口径一致。
 */
function submitBatchTag() {
  const ids = getSelectionIds();
  if (ids.length <= 0) {
    proxy.$message.error("请先选择商城用户");
    return;
  }
  if (!data.tagDialog.selectedTags.length) {
    proxy.$message.warning("请先选择或输入至少一个标签");
    return;
  }
  data.tableLoading = true;
  data.tagDialog.submitting = true;
  putBatchTag({
    ids,
    userTag: data.tagDialog.selectedTags.join("，"),
  })
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "批量打标签成功",
        type: "success",
      });
      closeTagDialog();
      clearSelection();
      refreshTagLibraryState();
      getPageF(data.page);
    })
    .catch(() => {
      data.tagDialog.submitting = false;
      data.tableLoading = false;
    });
}

/**
 * 快速按标签筛选列表。
 *
 * @param {string} tag 标签
 */
function applyQuickTagFilter(tag) {
  data.manualFilters.quickTag = normalizeTagText(tag);
  data.page.currentPage = 1;
  getPageF(data.page);
}

/**
 * 清空快捷标签筛选。
 */
function clearQuickTagFilter() {
  data.manualFilters.quickTag = "";
  data.page.currentPage = 1;
  getPageF(data.page);
}

/**
 * 打开标签库管理弹窗。
 */
function openTagLibraryDialog() {
  data.tagLibraryDialog.visible = true;
  resetTagLibraryForm();
  loadTagLibrary();
}

/**
 * 重置标签库表单。
 */
function resetTagLibraryForm() {
  data.tagLibraryDialog.form = createDefaultTagLibraryForm();
}

/**
 * 回填标签库编辑表单。
 *
 * @param {Object} row 标签行数据
 */
function editTagLibraryItem(row) {
  data.tagLibraryDialog.form = {
    id: row?.id || "",
    tagName: row?.tagName || "",
    status: row?.status || "0",
    sort: Number(row?.sort || 0),
    remark: row?.remark || "",
  };
}

/**
 * 保存标签库表单。
 */
function submitTagLibraryForm() {
  const form = {
    ...data.tagLibraryDialog.form,
    tagName: normalizeTagText(data.tagLibraryDialog.form.tagName),
  };
  if (!form.tagName) {
    proxy.$message.warning("标签名称不能为空");
    return;
  }
  data.tagLibraryDialog.submitting = true;
  const requestFn = form.id ? putTag : addTag;
  requestFn(form)
    .then(() => {
      proxy.$message.success(form.id ? "标签更新成功" : "标签新增成功");
      resetTagLibraryForm();
      return refreshTagLibraryState();
    })
    .catch(() => {
      data.tagLibraryDialog.submitting = false;
    })
    .finally(() => {
      data.tagLibraryDialog.submitting = false;
    });
}

/**
 * 快速切换标签库状态。
 *
 * @param {Object} row 标签行数据
 */
function changeTagStatus(row) {
  const originStatus = row.status === "0" ? "1" : "0";
  putTag({
    id: row.id,
    tagName: row.tagName,
    status: row.status,
    sort: row.sort,
    remark: row.remark,
  })
    .then(() => {
      proxy.$message.success("标签状态更新成功");
      refreshTagLibraryState();
    })
    .catch(() => {
      row.status = originStatus;
    });
}

/**
 * 对当前选中会员直接写入一个常用标签。
 * 适合运营批量圈人后快速打一个标签，不需要再打开弹窗。
 *
 * @param {string} tag 标签
 */
function quickBatchTag(tag) {
  const normalizedTag = normalizeTagText(tag);
  if (!normalizedTag) {
    return;
  }
  const ids = getSelectionIds();
  if (ids.length <= 0) {
    proxy.$message.error("请先选择商城用户");
    return;
  }
  proxy
    .$confirm(`确认给已选 ${ids.length} 位会员打上“${normalizedTag}”标签吗？`, "快捷打标签", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      data.tableLoading = true;
      return putBatchTag({
        ids,
        userTag: normalizedTag,
      });
    })
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "快捷打标签成功",
        type: "success",
      });
      clearSelection();
      refreshTagLibraryState();
      getPageF(data.page);
    })
    .catch(() => {
      data.tableLoading = false;
    });
}

/**
 * 刷新列表。
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
    return { label: "待支付用户", type: "primary" };
  }
  return { label: "新入会用户", type: "" };
}

/**
 * 统一格式化日期。
 * 页面运营预设需要构造时间区间，这里固定输出 YYYY-MM-DD，和筛选控件保持一致。
 *
 * @param {Date} date 日期对象
 * @returns {string}
 */
function formatDate(date) {
  const year = date.getFullYear();
  const month = `${date.getMonth() + 1}`.padStart(2, "0");
  const day = `${date.getDate()}`.padStart(2, "0");
  return `${year}-${month}-${day}`;
}

/**
 * 生成最近 N 天日期区间。
 * 包含今天，用于一键圈选新客、新会员等运营场景。
 *
 * @param {number} days 天数
 * @returns {string[]}
 */
function buildRecentDateRange(days) {
  const endDate = new Date();
  const startDate = new Date();
  startDate.setDate(endDate.getDate() - Math.max(days - 1, 0));
  return [formatDate(startDate), formatDate(endDate)];
}

/**
 * 统一计算日期距离今天的天数。
 * 列表时间字段来自后端字符串，先做一次兼容转换，避免浏览器解析失败。
 *
 * @param {string} value 时间字符串
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
 * 计算列表活跃度标签。
 * 优先使用最后登录时间，没有登录记录时回退到注册时间，方便运营快速识别沉睡用户。
 *
 * @param {Object} row 用户行数据
 * @returns {{label: string, type: string}}
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
 * 计算列表会员龄文案。
 * 当前口径下绑定手机号才算会员，因此未入会用户直接返回固定文案。
 *
 * @param {Object} row 用户行数据
 * @returns {string}
 */
function buildMemberAgeText(row) {
  if (row?.memberFlag !== "1" || !row?.memberTime) {
    return "未入会";
  }
  return formatMemberAgeDays(getDaysFromNow(row.memberTime));
}

function applyAdvancedFilter() {
  data.page.currentPage = 1;
  getPageF(data.page);
}

/**
 * 应用后台预设运营场景。
 * 预设本质上是人工筛选条件的组合，因此仍然复用统一的查询参数拼装逻辑。
 *
 * @param {string} presetValue 预设编码
 */
function applyPresetFilter(presetValue) {
  const nextFilters = createDefaultManualFilters();
  switch (presetValue) {
    case "newGuest7":
      nextFilters.quickScene = "guest";
      nextFilters.registerTimeRange = buildRecentDateRange(7);
      break;
    case "newMember7":
      nextFilters.quickScene = "member";
      nextFilters.memberTimeRange = buildRecentDateRange(7);
      break;
    case "silentMember30":
      nextFilters.quickScene = "member";
      nextFilters.activityScene = "silent30";
      break;
    case "highValueMember":
      nextFilters.quickScene = "member";
      nextFilters.minConsumeAmount = 1000;
      break;
    case "repeatMember":
      nextFilters.quickScene = "paid";
      nextFilters.minOrderCount = 3;
      break;
    default:
      break;
  }
  data.manualFilters = nextFilters;
  data.page.currentPage = 1;
  getPageF(data.page);
}

function resetAdvancedFilter() {
  data.manualFilters = createDefaultManualFilters();
  data.page.currentPage = 1;
  getPageF(data.page);
}

function handleQuickSceneChange() {
  data.page.currentPage = 1;
  getPageF(data.page);
}

function getQuickSceneLabel(value) {
  return quickSceneOptions.find((item) => item.value === value)?.label || "";
}

function getActivitySceneLabel(value) {
  return activitySceneOptions.find((item) => item.value === value)?.label || "";
}

function formatMemberAgeDays(memberAgeDays) {
  if (memberAgeDays === null || memberAgeDays === undefined) {
    return "未入会";
  }
  if (Number(memberAgeDays) <= 0) {
    return "当日入会";
  }
  return `${memberAgeDays} 天`;
}

onMounted(() => {
  refreshTagLibraryState();
});

function refreshChange() {
  getPageF(data.page);
}
</script>

<style lang="scss" scoped>
.mall-user-page {
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
  background: linear-gradient(180deg, #ffffff 0%, #f6f9ff 100%);
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

.operate-filter-panel {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 18px 20px;
  border: 1px solid #e8eef7;
  border-radius: 16px;
  background: #ffffff;
  box-shadow: 0 6px 16px rgba(15, 23, 42, 0.04);
}

.filter-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.filter-title {
  color: #1f2937;
  font-size: 16px;
  font-weight: 700;
}

.filter-sub-title {
  margin-top: 6px;
  color: #6b7280;
  font-size: 13px;
}

.filter-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.quick-filter-group {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.preset-filter-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.preset-filter-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

.preset-filter-card {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 6px;
  padding: 14px 16px;
  border: 1px solid #d8e4ff;
  border-radius: 14px;
  background: linear-gradient(180deg, #ffffff 0%, #f7faff 100%);
  cursor: pointer;
  transition: all 0.2s ease;
}

.preset-filter-card:hover {
  border-color: #2967ff;
  box-shadow: 0 10px 24px rgba(41, 103, 255, 0.08);
  transform: translateY(-1px);
}

.preset-filter-title {
  color: #1f2937;
  font-size: 14px;
  font-weight: 600;
}

.preset-filter-tip {
  color: #6b7280;
  font-size: 12px;
  line-height: 1.5;
  text-align: left;
}

.group-label,
.filter-item-label {
  color: #4b5563;
  font-size: 13px;
  font-weight: 600;
}

.filter-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 16px;
}

.filter-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.number-range {
  display: flex;
  align-items: center;
  gap: 10px;
}

.range-separator {
  color: #6b7280;
  font-size: 13px;
}

.insight-panel {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 18px 20px;
  border: 1px solid #e8eef7;
  border-radius: 16px;
  background: linear-gradient(180deg, #ffffff 0%, #fbfdff 100%);
  box-shadow: 0 6px 16px rgba(15, 23, 42, 0.04);
}

.tag-panel {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 18px 20px;
  border: 1px solid #e8eef7;
  border-radius: 16px;
  background: linear-gradient(180deg, #ffffff 0%, #fbfdff 100%);
  box-shadow: 0 6px 16px rgba(15, 23, 42, 0.04);
}

.tag-panel-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
}

.tag-panel-actions {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.tag-summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

.tag-summary-card {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 8px;
  padding: 16px 18px;
  border: 1px solid #dce7ff;
  border-radius: 14px;
  background: #ffffff;
  cursor: pointer;
  transition: all 0.2s ease;
}

.tag-summary-card:hover,
.tag-summary-card.is-active {
  border-color: #2967ff;
  box-shadow: 0 10px 24px rgba(41, 103, 255, 0.1);
  transform: translateY(-1px);
}

.tag-summary-name {
  color: #1f2937;
  font-size: 15px;
  font-weight: 700;
}

.tag-summary-count {
  color: #2967ff;
  font-size: 24px;
  font-weight: 700;
}

.tag-summary-tip {
  color: #6b7280;
  font-size: 12px;
}

.tag-toolbar-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.tag-chip-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag-chip-btn {
  padding: 8px 14px;
  border: 1px solid #d8e4ff;
  border-radius: 999px;
  background: #f8fbff;
  color: #35507a;
  cursor: pointer;
  transition: all 0.2s ease;
}

.tag-chip-btn:hover,
.tag-chip-btn.is-active {
  border-color: #2967ff;
  background: #edf3ff;
  color: #1f4ecc;
}

.tag-chip-btn.is-batch {
  border-style: dashed;
}

.batch-action-bar {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.batch-action-text {
  color: #4b5563;
  font-size: 13px;
  font-weight: 600;
}

.insight-header {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.insight-sub-title {
  color: #6b7280;
  font-size: 13px;
}

.insight-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

.insight-card {
  padding: 16px 18px;
  border: 1px solid #edf2f7;
  border-radius: 14px;
  background: #ffffff;
}

.insight-label {
  color: #6b7280;
  font-size: 13px;
}

.insight-value {
  margin-top: 8px;
  font-size: 24px;
  font-weight: 700;
  line-height: 1.2;
}

.insight-tip {
  margin-top: 8px;
  color: #909399;
  font-size: 12px;
  line-height: 1.5;
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

.user-cell {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.user-meta {
  margin-left: 12px;
  text-align: left;
}

.user-name {
  color: #303133;
  font-weight: 600;
}

.user-id {
  margin-top: 4px;
  color: #909399;
  font-size: 12px;
}

.user-segment-row {
  margin-top: 6px;
}

.user-tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  justify-content: center;
}

.empty-tag-text {
  color: #909399;
}

.detail-link-btn {
  margin-top: 4px;
  padding: 0;
}

.amount-text {
  color: #d4380d;
  font-weight: 600;
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

.inline-tag {
  margin-left: 6px;
}

.detail-divider {
  margin: 0 6px;
}

.tag-dialog-body {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.tag-dialog-tip {
  color: #6b7280;
  font-size: 13px;
  line-height: 1.7;
}

.tag-custom-input-row {
  display: flex;
  gap: 12px;
}

.tag-selected-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag-library-layout {
  display: grid;
  grid-template-columns: minmax(280px, 320px) minmax(0, 1fr);
  gap: 16px;
  min-width: 0;
}

.tag-library-form-card,
.tag-library-table-card {
  padding: 16px 18px;
  border: 1px solid #e8eef7;
  border-radius: 14px;
  background: #ffffff;
}

.tag-library-form-card,
.tag-library-table-card,
.tag-library-table-wrap {
  min-width: 0;
}

.tag-library-table-card {
  overflow: hidden;
}

.tag-library-table-wrap {
  width: 100%;
  overflow-x: auto;
}

.tag-library-form-title {
  color: #1f2937;
  font-size: 14px;
  font-weight: 600;
}

.tag-library-form-actions,
.tag-library-table-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.tag-library-form-actions {
  justify-content: flex-end;
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

.detail-address-label {
  color: #4b5563;
  font-size: 12px;
}

.detail-address-text {
  margin-top: 8px;
  color: #1f2937;
  line-height: 1.8;
}

.detail-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-section-title {
  color: #1f2937;
  font-size: 15px;
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

@media screen and (max-width: 768px) {
  .filter-header,
  .quick-filter-group,
  .tag-panel-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .number-range {
    width: 100%;
    flex-wrap: wrap;
  }

  .tag-custom-input-row {
    flex-direction: column;
  }

  .tag-library-layout {
    grid-template-columns: 1fr;
  }

  .detail-profile-card {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>

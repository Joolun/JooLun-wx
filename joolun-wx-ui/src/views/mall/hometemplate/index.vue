<!--
  Copyright (C) 2026
  All rights reserved, Designed By www.joolun.com
  注意：
  本软件为www.joolun.com开发研制，项目使用请保留此说明
-->

<template>
  <div v-loading="loading" class="app-container home-template-page">
    <el-row :gutter="16" class="layout-row">
      <el-col :xl="16" :lg="15" :md="24" :sm="24" :xs="24">
        <el-card shadow="never" class="page-card">
          <template #header>
            <div class="page-header">
              <div>
                <div class="page-title">配置式首页装修</div>
                <div class="page-desc">
                  当前版本支持轮播、导航、公告、活动图和商品分组。保存后会同步刷新首页装修缓存，小程序重新进入首页即可看到最新结果。
                </div>
              </div>
              <div class="page-actions">
                <el-button @click="loadCurrentTemplate">重新加载</el-button>
                <el-button plain :loading="refreshingCache" :disabled="!canEdit" @click="handleRefreshCache">
                  刷新缓存
                </el-button>
                <el-button plain @click="previewDialogVisible = true">放大预览</el-button>
                <el-button type="primary" :loading="saving" :disabled="!canEdit" @click="handleSave">
                  保存模板
                </el-button>
              </div>
            </div>
          </template>

          <el-form label-width="96px">
            <el-row :gutter="16">
              <el-col :xl="8" :lg="8" :md="12" :sm="24" :xs="24">
                <el-form-item label="模板名称">
                  <el-input v-model="templateForm.templateName" maxlength="100" />
                </el-form-item>
              </el-col>
              <el-col :xl="8" :lg="8" :md="12" :sm="24" :xs="24">
                <el-form-item label="模板编码">
                  <el-input v-model="templateForm.templateCode" maxlength="100" />
                </el-form-item>
              </el-col>
              <el-col :xl="8" :lg="8" :md="12" :sm="24" :xs="24">
                <el-form-item label="模板状态">
                  <el-switch
                    v-model="templateForm.status"
                    active-value="1"
                    inactive-value="0"
                    active-text="启用"
                    inactive-text="停用"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="备注">
              <el-input
                v-model="templateForm.remark"
                type="textarea"
                :rows="2"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" class="page-card">
          <template #header>
            <div class="page-header">
              <div>
                <div class="page-title">首页组件</div>
                <div class="page-desc">
                  这里维护首页主内容区，当前已支持首页咨询入口参与装修编排；顶部搜索、客服悬浮和底部猜你喜欢仍为固定区域。组件数量不设上限，可继续新增、删除，也可重复添加同类型组件。
                </div>
                <div class="page-desc page-desc-light">
                  当前已添加 {{ templateForm.itemList.length }} 个组件，下面 6 个按钮表示组件类型，不是组件数量上限。
                </div>
              </div>
              <div class="page-actions add-actions">
                <el-button plain @click="addSection('banner')">轮播</el-button>
                <el-button plain @click="addSection('nav')">导航</el-button>
                <el-button plain @click="addSection('notice')">公告</el-button>
                <el-button plain @click="addSection('service')">咨询入口</el-button>
                <el-button plain @click="addSection('image')">活动图</el-button>
                <el-button plain @click="addSection('goods')">商品分组</el-button>
              </div>
            </div>
          </template>

          <div class="section-toolbar">
            <div class="section-toolbar-head">
              <div class="section-toolbar-title">新增组件</div>
              <div class="section-toolbar-desc">
                点击下面按钮即可继续追加首页组件，同一种组件类型也可以重复添加。
              </div>
            </div>
            <div class="section-toolbar-actions">
              <el-button type="primary" plain @click="addSection('banner')">+ 轮播图</el-button>
              <el-button type="primary" plain @click="addSection('nav')">+ 快捷导航</el-button>
              <el-button type="primary" plain @click="addSection('notice')">+ 首页公告</el-button>
              <el-button type="primary" plain @click="addSection('service')">+ 首页咨询入口</el-button>
              <el-button type="primary" plain @click="addSection('image')">+ 活动图片</el-button>
              <el-button type="primary" plain @click="addSection('goods')">+ 商品分组</el-button>
            </div>
          </div>

          <el-empty v-if="templateForm.itemList.length <= 0" description="请先添加首页组件" />

          <div
            v-for="(section, index) in templateForm.itemList"
            :key="section.localKey"
            class="section-box"
          >
            <div class="section-head">
              <div class="section-meta">
                <span class="section-index">#{{ index + 1 }}</span>
                <el-tag size="small" effect="plain">{{ getDefaultItemName(section.itemType) }}</el-tag>
                <span class="section-name">{{ section.itemName || getDefaultItemName(section.itemType) }}</span>
              </div>
              <div class="section-actions">
                <el-switch
                  v-model="section.itemStatus"
                  active-value="1"
                  inactive-value="0"
                  active-text="启用"
                  inactive-text="停用"
                />
                <el-button text :disabled="index <= 0" @click="moveSection(index, -1)">上移</el-button>
                <el-button
                  text
                  :disabled="index >= templateForm.itemList.length - 1"
                  @click="moveSection(index, 1)"
                >
                  下移
                </el-button>
                <el-button text type="danger" @click="removeSection(index)">删除</el-button>
              </div>
            </div>

            <el-row :gutter="16">
              <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                <div class="field-label">组件类型</div>
                <el-select v-model="section.itemType" @change="handleItemTypeChange(section)">
                  <el-option
                    v-for="item in itemTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-col>
              <el-col :xl="10" :lg="10" :md="12" :sm="24" :xs="24">
                <div class="field-label">组件名称</div>
                <el-input v-model="section.itemName" maxlength="100" />
              </el-col>
            </el-row>

            <div
              v-if="section.itemType === 'banner' || section.itemType === 'image' || section.itemType === 'nav'"
              class="config-block"
            >
              <div class="block-title-row">
                <span class="block-title">{{ section.itemType === 'nav' ? '导航项' : '图片项' }}</span>
                <el-button plain @click="addMediaItem(section)">新增一项</el-button>
              </div>

              <div
                v-for="(item, itemIndex) in section.content.items"
                :key="`${section.localKey}-${itemIndex}`"
                class="item-row"
              >
                <div class="upload-wrap">
                  <ImageUpload v-model="item.imageUrl" :limit="1" />
                </div>

                <div class="item-form">
                  <div v-if="section.itemType === 'nav'">
                    <div class="field-label">标题</div>
                    <el-input v-model="item.title" maxlength="20" placeholder="请输入导航标题" />
                  </div>

                  <div class="field-label">链接类型</div>
                  <el-select v-model="item.linkType" @change="handleLinkTypeChange(item)">
                    <el-option
                      v-for="option in linkTypeOptions"
                      :key="option.value"
                      :label="option.label"
                      :value="option.value"
                    />
                  </el-select>

                  <div class="field-label">链接值</div>
                  <template v-if="item.linkType === 'goods'">
                    <el-select
                      v-model="item.linkValue"
                      filterable
                      remote
                      reserve-keyword
                      :remote-method="searchGoodsOptions"
                      :loading="goodsSearching"
                      placeholder="搜索并选择商品"
                    >
                      <el-option
                        v-for="option in getGoodsLinkOptions(item.linkValue)"
                        :key="option.id"
                        :label="option.name"
                        :value="option.id"
                      />
                    </el-select>
                  </template>
                  <template v-else-if="item.linkType === 'page' || item.linkType === 'tab'">
                    <el-select v-model="item.linkValue" filterable placeholder="请选择页面">
                      <el-option
                        v-for="option in getPageLinkOptions(item.linkType, item.linkValue)"
                        :key="option.value"
                        :label="option.label"
                        :value="option.value"
                      />
                    </el-select>
                  </template>
                  <template v-else>
                    <el-input
                      v-model="item.linkValue"
                      disabled
                      placeholder="当前类型无需配置链接值"
                    />
                  </template>
                </div>

                <el-button
                  text
                  type="danger"
                  :disabled="section.content.items.length <= 1"
                  @click="removeMediaItem(section, itemIndex)"
                >
                  删除
                </el-button>
              </div>
            </div>

            <div v-else-if="section.itemType === 'notice'" class="config-block">
              <div class="block-title-row">
                <span class="block-title">公告项</span>
                <el-button plain @click="addNoticeItem(section)">新增公告</el-button>
              </div>

              <div
                v-for="(item, itemIndex) in section.content.items"
                :key="`${section.localKey}-notice-${itemIndex}`"
                class="notice-row"
              >
                <el-input
                  v-model="item.tag"
                  maxlength="10"
                  placeholder="标签，如：公告"
                  class="notice-tag"
                />
                <el-input v-model="item.text" maxlength="100" placeholder="请输入公告内容" />
                <el-button
                  text
                  type="danger"
                  :disabled="section.content.items.length <= 1"
                  @click="removeNoticeItem(section, itemIndex)"
                >
                  删除
                </el-button>
              </div>
            </div>

            <div v-else-if="section.itemType === 'service'" class="config-block">
              <el-row :gutter="16">
                <el-col :xl="8" :lg="8" :md="12" :sm="24" :xs="24">
                  <div class="field-label">主标题</div>
                  <el-input v-model="section.content.title" maxlength="20" placeholder="如：首页咨询入口" />
                </el-col>
                <el-col :xl="16" :lg="16" :md="12" :sm="24" :xs="24">
                  <div class="field-label">说明文案</div>
                  <el-input
                    v-model="section.content.desc"
                    maxlength="80"
                    placeholder="如：买前可咨询选品，买后可追踪订单和售后"
                  />
                </el-col>
              </el-row>

              <el-row :gutter="16">
                <el-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
                  <div class="field-label">服务标签</div>
                  <el-input
                    :model-value="ensureArray(section.content.tags).join('，')"
                    maxlength="80"
                    placeholder="多个标签用中文或英文逗号隔开"
                    @update:model-value="section.content.tags = normalizeTagInput($event)"
                  />
                </el-col>
              </el-row>

              <el-row :gutter="16">
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">主按钮文案</div>
                  <el-input v-model="section.content.contactButtonText" maxlength="10" />
                </el-col>
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">选品按钮文案</div>
                  <el-input v-model="section.content.goodsButtonText" maxlength="10" />
                </el-col>
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">订单按钮文案</div>
                  <el-input v-model="section.content.orderButtonText" maxlength="10" />
                </el-col>
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">售后按钮文案</div>
                  <el-input v-model="section.content.afterSaleButtonText" maxlength="10" />
                </el-col>
              </el-row>
            </div>

            <div v-else-if="section.itemType === 'goods'" class="config-block">
              <el-row :gutter="16">
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">分组标题</div>
                  <el-input v-model="section.content.title" maxlength="20" placeholder="如：热销推荐" />
                </el-col>
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">副标题</div>
                  <el-input v-model="section.content.subTitle" maxlength="50" placeholder="选填" />
                </el-col>
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">商品来源</div>
                  <el-select v-model="section.content.sourceType">
                    <el-option
                      v-for="item in goodsSourceOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-col>
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">展示数量</div>
                  <el-input-number
                    v-model="section.content.size"
                    :min="1"
                    :max="20"
                    controls-position="right"
                  />
                </el-col>
              </el-row>

              <el-row :gutter="16">
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">排序字段</div>
                  <el-select
                    v-model="section.content.sortField"
                    :disabled="section.content.sourceType === 'manual'"
                  >
                    <el-option label="销量" value="saleNum" />
                    <el-option label="创建时间" value="createTime" />
                    <el-option label="后台排序" value="sort" />
                    <el-option label="更新时间" value="updateTime" />
                  </el-select>
                </el-col>
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">排序方式</div>
                  <el-select
                    v-model="section.content.sortOrder"
                    :disabled="section.content.sourceType === 'manual'"
                  >
                    <el-option label="降序" value="desc" />
                    <el-option label="升序" value="asc" />
                  </el-select>
                </el-col>
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">更多文案</div>
                  <el-input v-model="section.content.moreText" maxlength="20" placeholder="如：更多" />
                </el-col>
                <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
                  <div class="field-label">更多链接类型</div>
                  <el-select
                    v-model="section.content.moreLinkType"
                    @change="handleLinkTypeChange(section.content, 'moreLinkType', 'moreLinkValue')"
                  >
                    <el-option
                      v-for="option in linkTypeOptions"
                      :key="option.value"
                      :label="option.label"
                      :value="option.value"
                    />
                  </el-select>
                </el-col>
              </el-row>

              <div class="field-label">更多链接值</div>
              <template v-if="section.content.moreLinkType === 'goods'">
                <el-select
                  v-model="section.content.moreLinkValue"
                  filterable
                  remote
                  reserve-keyword
                  :remote-method="searchGoodsOptions"
                  :loading="goodsSearching"
                  placeholder="搜索并选择商品"
                >
                  <el-option
                    v-for="option in getGoodsLinkOptions(section.content.moreLinkValue)"
                    :key="option.id"
                    :label="option.name"
                    :value="option.id"
                  />
                </el-select>
              </template>
              <template
                v-else-if="section.content.moreLinkType === 'page' || section.content.moreLinkType === 'tab'"
              >
                <el-select v-model="section.content.moreLinkValue" filterable placeholder="请选择页面">
                  <el-option
                    v-for="option in getPageLinkOptions(section.content.moreLinkType, section.content.moreLinkValue)"
                    :key="option.value"
                    :label="option.label"
                    :value="option.value"
                  />
                </el-select>
              </template>
              <template v-else>
                <el-input
                  v-model="section.content.moreLinkValue"
                  disabled
                  placeholder="当前类型无需配置链接值"
                />
              </template>

              <div v-if="section.content.sourceType === 'manual'" class="manual-box">
                <div class="field-label">手动商品</div>
                <el-select
                  v-model="section.content.goodsIds"
                  multiple
                  filterable
                  remote
                  reserve-keyword
                  collapse-tags
                  collapse-tags-tooltip
                  :remote-method="searchGoodsOptions"
                  :loading="goodsSearching"
                  class="manual-select"
                  placeholder="搜索并选择要展示的商品"
                >
                  <el-option
                    v-for="item in getGoodsLinkOptionsList(section.content.goodsIds)"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xl="8" :lg="9" :md="24" :sm="24" :xs="24">
        <el-card shadow="never" class="page-card preview-card">
          <template #header>
            <div class="page-header">
              <div>
                <div class="page-title">实时预览</div>
                <div class="page-desc">右侧预览会随着表单即时变化，便于保存前检查样式和内容。</div>
              </div>
              <div class="page-actions">
                <el-button plain @click="previewDialogVisible = true">放大预览</el-button>
              </div>
            </div>
          </template>

          <div class="preview-tip">
            这里只模拟小程序首页主内容区，未保存的数据也会实时展示。保存模板后会同时刷新 Redis 缓存；如果运营侧怀疑页面未更新，可以再手动点一次“刷新缓存”。
          </div>

          <div class="phone-shell">
            <div class="phone-status">
              <span>09:41</span>
              <span>首页</span>
              <span>100%</span>
            </div>
            <div class="phone-content">
              <div class="preview-search-bar">搜索商品、分类、活动</div>
              <div v-if="previewSections.length <= 0" class="preview-empty">请先添加首页组件</div>
              <div
                v-for="section in previewSections"
                :key="`${section.localKey}-side`"
                class="preview-section"
                :class="{ 'is-disabled': section.disabled }"
              >
                <span v-if="section.disabled" class="preview-section-status preview-section-status--float">已停用</span>

                <el-carousel
                  v-if="section.itemType === 'banner'"
                  :interval="5000"
                  arrow="never"
                  height="166px"
                  class="preview-banner-carousel"
                  indicator-position="outside"
                >
                  <el-carousel-item
                    v-for="(item, itemIndex) in section.items"
                    :key="`${section.localKey}-banner-${itemIndex}`"
                  >
                    <div class="preview-banner-item">
                      <img v-if="item.imageUrl" :src="item.imageUrl" class="preview-banner-image" />
                      <div v-else class="preview-media-empty preview-banner-empty">请上传轮播图</div>
                    </div>
                  </el-carousel-item>
                </el-carousel>

                <div
                  v-else-if="section.itemType === 'nav'"
                  class="preview-nav-grid"
                  :style="getPreviewNavStyle(section.items)"
                >
                  <div
                    v-for="(item, itemIndex) in section.items"
                    :key="`${section.localKey}-nav-${itemIndex}`"
                    class="preview-nav-item"
                  >
                    <div class="preview-nav-icon">
                      <img v-if="item.imageUrl" :src="item.imageUrl" class="preview-nav-image" />
                      <span v-else>{{ itemIndex + 1 }}</span>
                    </div>
                    <div class="preview-nav-title">{{ item.title || "导航标题" }}</div>
                  </div>
                </div>

                <el-carousel
                  v-else-if="section.itemType === 'notice'"
                  :interval="6000"
                  arrow="never"
                  direction="vertical"
                  height="42px"
                  class="preview-notice-carousel"
                  autoplay
                >
                  <el-carousel-item
                    v-for="(item, itemIndex) in section.items"
                    :key="`${section.localKey}-notice-${itemIndex}`"
                  >
                    <div class="preview-notice-item">
                      <span class="preview-notice-tag">{{ item.tag || "公告" }}</span>
                      <span class="preview-notice-text">{{ item.text || "请填写公告内容" }}</span>
                    </div>
                  </el-carousel-item>
                </el-carousel>

                <div v-else-if="section.itemType === 'image'" class="preview-image-list">
                  <div
                    v-for="(item, itemIndex) in section.items"
                    :key="`${section.localKey}-image-${itemIndex}`"
                    class="preview-image-item"
                  >
                    <img v-if="item.imageUrl" :src="item.imageUrl" class="preview-media-image" />
                    <div v-else class="preview-media-empty">请上传活动图</div>
                  </div>
                </div>

                <div v-else-if="section.itemType === 'service'" class="preview-service-card">
                  <div class="preview-service-header">
                    <div>
                      <div class="preview-service-title">{{ section.title || "首页咨询入口" }}</div>
                      <div class="preview-service-desc">
                        {{ section.desc || "买前可咨询选品，买后可追踪订单和售后，统一走小程序原生客服更直接。" }}
                      </div>
                    </div>
                    <span class="preview-service-button">{{ section.contactButtonText || "立即咨询" }}</span>
                  </div>
                  <div class="preview-service-tags">
                    <span
                      v-for="(tag, tagIndex) in section.tags"
                      :key="`${section.localKey}-service-tag-${tagIndex}`"
                      class="preview-service-tag"
                    >
                      {{ tag }}
                    </span>
                  </div>
                  <div class="preview-service-shortcuts">
                    <span class="preview-service-shortcut is-primary">{{ section.goodsButtonText || "选品咨询" }}</span>
                    <span class="preview-service-shortcut">{{ section.orderButtonText || "订单进度" }}</span>
                    <span class="preview-service-shortcut">{{ section.afterSaleButtonText || "售后帮助" }}</span>
                  </div>
                </div>

                <div v-else-if="section.itemType === 'goods'" class="preview-goods-section">
                  <div class="preview-goods-head">
                    <div>
                      <div class="preview-goods-title">{{ section.title || "商品分组" }}</div>
                      <div v-if="section.subTitle" class="preview-goods-sub-title">{{ section.subTitle }}</div>
                    </div>
                    <span class="preview-goods-more">{{ section.moreText || "更多" }}</span>
                  </div>
                  <div class="preview-goods-grid">
                    <div
                      v-for="(goods, goodsIndex) in section.goodsList"
                      :key="`${section.localKey}-goods-${goodsIndex}`"
                      class="preview-goods-card"
                    >
                      <div class="preview-goods-image-wrap">
                        <img
                          v-if="goods.imageUrl"
                          :src="goods.imageUrl"
                          class="preview-goods-image"
                        />
                        <div v-else class="preview-goods-image-empty">商品图</div>
                      </div>
                      <div class="preview-goods-name">{{ goods.name }}</div>
                      <div class="preview-goods-price">{{ goods.priceText }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog
      v-model="previewDialogVisible"
      title="首页预览"
      width="430px"
      append-to-body
      destroy-on-close
    >
      <div class="dialog-preview-wrap">
        <div class="phone-shell is-dialog">
          <div class="phone-status">
            <span>09:41</span>
            <span>首页</span>
            <span>100%</span>
          </div>
          <div class="phone-content">
            <div class="preview-search-bar">搜索商品、分类、活动</div>
            <div v-if="previewSections.length <= 0" class="preview-empty">请先添加首页组件</div>
            <div
              v-for="section in previewSections"
              :key="`${section.localKey}-dialog`"
              class="preview-section"
              :class="{ 'is-disabled': section.disabled }"
            >
              <span v-if="section.disabled" class="preview-section-status preview-section-status--float">已停用</span>

              <el-carousel
                v-if="section.itemType === 'banner'"
                :interval="5000"
                arrow="never"
                height="166px"
                class="preview-banner-carousel"
                indicator-position="outside"
              >
                <el-carousel-item
                  v-for="(item, itemIndex) in section.items"
                  :key="`${section.localKey}-dialog-banner-${itemIndex}`"
                >
                  <div class="preview-banner-item">
                    <img v-if="item.imageUrl" :src="item.imageUrl" class="preview-banner-image" />
                    <div v-else class="preview-media-empty preview-banner-empty">请上传轮播图</div>
                  </div>
                </el-carousel-item>
              </el-carousel>

              <div
                v-else-if="section.itemType === 'nav'"
                class="preview-nav-grid"
                :style="getPreviewNavStyle(section.items)"
              >
                <div
                  v-for="(item, itemIndex) in section.items"
                  :key="`${section.localKey}-dialog-nav-${itemIndex}`"
                  class="preview-nav-item"
                >
                  <div class="preview-nav-icon">
                    <img v-if="item.imageUrl" :src="item.imageUrl" class="preview-nav-image" />
                    <span v-else>{{ itemIndex + 1 }}</span>
                  </div>
                  <div class="preview-nav-title">{{ item.title || "导航标题" }}</div>
                </div>
              </div>

              <el-carousel
                v-else-if="section.itemType === 'notice'"
                :interval="6000"
                arrow="never"
                direction="vertical"
                height="42px"
                class="preview-notice-carousel"
                autoplay
              >
                <el-carousel-item
                  v-for="(item, itemIndex) in section.items"
                  :key="`${section.localKey}-dialog-notice-${itemIndex}`"
                >
                  <div class="preview-notice-item">
                    <span class="preview-notice-tag">{{ item.tag || "公告" }}</span>
                    <span class="preview-notice-text">{{ item.text || "请填写公告内容" }}</span>
                  </div>
                </el-carousel-item>
              </el-carousel>

              <div v-else-if="section.itemType === 'image'" class="preview-image-list">
                <div
                  v-for="(item, itemIndex) in section.items"
                  :key="`${section.localKey}-dialog-image-${itemIndex}`"
                  class="preview-image-item"
                >
                  <img v-if="item.imageUrl" :src="item.imageUrl" class="preview-media-image" />
                  <div v-else class="preview-media-empty">请上传活动图</div>
                </div>
              </div>

              <div v-else-if="section.itemType === 'service'" class="preview-service-card">
                <div class="preview-service-header">
                  <div>
                    <div class="preview-service-title">{{ section.title || "首页咨询入口" }}</div>
                    <div class="preview-service-desc">
                      {{ section.desc || "买前可咨询选品，买后可追踪订单和售后，统一走小程序原生客服更直接。" }}
                    </div>
                  </div>
                  <span class="preview-service-button">{{ section.contactButtonText || "立即咨询" }}</span>
                </div>
                <div class="preview-service-tags">
                  <span
                    v-for="(tag, tagIndex) in section.tags"
                    :key="`${section.localKey}-dialog-service-tag-${tagIndex}`"
                    class="preview-service-tag"
                  >
                    {{ tag }}
                  </span>
                </div>
                <div class="preview-service-shortcuts">
                  <span class="preview-service-shortcut is-primary">{{ section.goodsButtonText || "选品咨询" }}</span>
                  <span class="preview-service-shortcut">{{ section.orderButtonText || "订单进度" }}</span>
                  <span class="preview-service-shortcut">{{ section.afterSaleButtonText || "售后帮助" }}</span>
                </div>
              </div>

              <div v-else-if="section.itemType === 'goods'" class="preview-goods-section">
                <div class="preview-goods-head">
                  <div>
                    <div class="preview-goods-title">{{ section.title || "商品分组" }}</div>
                    <div v-if="section.subTitle" class="preview-goods-sub-title">{{ section.subTitle }}</div>
                  </div>
                  <span class="preview-goods-more">{{ section.moreText || "更多" }}</span>
                </div>
                <div class="preview-goods-grid">
                  <div
                    v-for="(goods, goodsIndex) in section.goodsList"
                    :key="`${section.localKey}-dialog-goods-${goodsIndex}`"
                    class="preview-goods-card"
                  >
                    <div class="preview-goods-image-wrap">
                      <img
                        v-if="goods.imageUrl"
                        :src="goods.imageUrl"
                        class="preview-goods-image"
                      />
                      <div v-else class="preview-goods-image-empty">商品图</div>
                    </div>
                    <div class="preview-goods-name">{{ goods.name }}</div>
                    <div class="preview-goods-price">{{ goods.priceText }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="MallHomeTemplate">
import { computed, getCurrentInstance, onMounted, reactive, ref } from "vue";
import { checkPermi } from "@/utils/permission";
import {
  getCurrentTemplate,
  refreshCurrentTemplateCache,
  saveCurrentTemplate,
} from "@/api/mall/hometemplate";
import { getObj as getGoodsObj, getPage as getGoodsPage } from "@/api/mall/goodsspu";
import { getMallFirstImageUrl, resolveMallImageUrl } from "@/utils/mall-image";

const { proxy } = getCurrentInstance();

// 首页装修支持的组件类型。
const itemTypeOptions = [
  { label: "轮播图", value: "banner" },
  { label: "导航宫格", value: "nav" },
  { label: "公告栏", value: "notice" },
  { label: "首页咨询入口", value: "service" },
  { label: "活动图片", value: "image" },
  { label: "商品分组", value: "goods" },
];

// 链接类型统一收口，避免页面里出现自由输入路径。
const linkTypeOptions = [
  { label: "不跳转", value: "none" },
  { label: "商品详情", value: "goods" },
  { label: "普通页面", value: "page" },
  { label: "Tab 页面", value: "tab" },
];

const goodsSourceOptions = [
  { label: "热销商品", value: "hot" },
  { label: "新品商品", value: "new" },
  { label: "手动选择", value: "manual" },
];

const normalPageOptions = [
  { label: "搜索页", value: "/pages/base/search/index" },
  { label: "商品列表-新品首发", value: "/pages/goods/goods-list/index?type=1" },
  { label: "商品列表-热销单品", value: "/pages/goods/goods-list/index?type=2" },
  { label: "订单列表", value: "/pages/order/order-list/index" },
  { label: "售后列表", value: "/pages/order/after-sale-list/index" },
  { label: "个人资料", value: "/pages/user/user-profile/index" },
  { label: "收货地址", value: "/pages/user/user-address/list/index" },
];

const tabPageOptions = [
  { label: "首页", value: "/pages/home/index" },
  { label: "分类", value: "/pages/goods/goods-category/index" },
  { label: "购物车", value: "/pages/shopping-cart/index" },
  { label: "我的", value: "/pages/user/user-center/index" },
];

const loading = ref(false);
const saving = ref(false);
const refreshingCache = ref(false);
const goodsSearching = ref(false);
const previewDialogVisible = ref(false);
const goodsOptions = ref([]);
const templateForm = reactive({
  id: "",
  templateName: "",
  templateCode: "",
  status: "1",
  remark: "",
  itemList: [],
});
const canEdit = computed(() => checkPermi(["mall:hometemplate:edit"]));
let sectionKeySeed = 0;

// 预览区完全基于当前表单数据生成，不依赖保存后的结果。
const previewSections = computed(() =>
  templateForm.itemList.map((section) => buildPreviewSection(section))
);

onMounted(() => {
  loadCurrentTemplate();
});

async function loadCurrentTemplate() {
  loading.value = true;
  try {
    const response = await getCurrentTemplate();
    const data = response.data || {};
    templateForm.id = data.id || "";
    templateForm.templateName = data.templateName || "默认首页模板";
    templateForm.templateCode = data.templateCode || "ma_home_default";
    templateForm.status = data.status || "1";
    templateForm.remark = data.remark || "";
    templateForm.itemList = ensureArray(data.itemList).map((item) => normalizeSection(item));
    syncItemSort();
    await preloadGoodsOptions();
  } finally {
    loading.value = false;
  }
}

async function handleRefreshCache() {
  refreshingCache.value = true;
  try {
    await refreshCurrentTemplateCache();
    proxy.$message.success("首页装修缓存刷新成功");
    await loadCurrentTemplate();
  } finally {
    refreshingCache.value = false;
  }
}

function normalizeSection(item = {}) {
  const itemType = item.itemType || "banner";
  return {
    id: item.id || "",
    localKey: nextSectionKey(),
    itemType,
    itemName: item.itemName || getDefaultItemName(itemType),
    itemStatus: item.itemStatus || "1",
    itemSort: item.itemSort || 0,
    content: normalizeContent(itemType, item.content || {}),
  };
}

function normalizeContent(type, content = {}) {
  if (type === "banner" || type === "image") {
    return {
      items: ensureArray(content.items).length
        ? ensureArray(content.items).map((item) => ({
            imageUrl: item.imageUrl || "",
            linkType: normalizeLinkType(item.linkType),
            linkValue: item.linkValue || "",
          }))
        : [createMediaItem()],
    };
  }
  if (type === "nav") {
    return {
      items: ensureArray(content.items).length
        ? ensureArray(content.items).map((item) => ({
            title: item.title || "",
            imageUrl: item.imageUrl || "",
            linkType: normalizeLinkType(item.linkType),
            linkValue: item.linkValue || "",
          }))
        : [createNavItem()],
    };
  }
  if (type === "notice") {
    return {
      items: ensureArray(content.items).length
        ? ensureArray(content.items).map((item) => ({
            tag: item.tag || "公告",
            text: item.text || "",
          }))
        : [createNoticeItem()],
    };
  }
  if (type === "service") {
    return {
      title: content.title || "首页咨询入口",
      desc: content.desc || "买前可咨询选品，买后可追踪订单和售后，统一走小程序原生客服更直接。",
      tags: ensureArray(content.tags).length
        ? ensureArray(content.tags)
        : ["选品推荐", "活动优惠", "订单进度", "售后帮助"],
      contactButtonText: content.contactButtonText || "立即咨询",
      goodsButtonText: content.goodsButtonText || "选品咨询",
      orderButtonText: content.orderButtonText || "订单进度",
      afterSaleButtonText: content.afterSaleButtonText || "售后帮助",
    };
  }
  return {
    title: content.title || "商品分组",
    subTitle: content.subTitle || "",
    sourceType: content.sourceType || "hot",
    size: Number(content.size || 6),
    sortField: content.sortField || "saleNum",
    sortOrder: content.sortOrder === "asc" ? "asc" : "desc",
    moreText: content.moreText || "更多",
    moreLinkType: normalizeLinkType(content.moreLinkType),
    moreLinkValue: content.moreLinkValue || "",
    goodsIds: ensureArray(content.goodsIds),
  };
}

function addSection(type) {
  templateForm.itemList.push(normalizeSection({ itemType: type, content: {} }));
  syncItemSort();
}

function removeSection(index) {
  templateForm.itemList.splice(index, 1);
  syncItemSort();
}

function moveSection(index, offset) {
  const targetIndex = index + offset;
  if (targetIndex < 0 || targetIndex >= templateForm.itemList.length) {
    return;
  }
  const currentItem = templateForm.itemList[index];
  templateForm.itemList.splice(index, 1);
  templateForm.itemList.splice(targetIndex, 0, currentItem);
  syncItemSort();
}

function handleItemTypeChange(section) {
  section.itemName = getDefaultItemName(section.itemType);
  section.content = normalizeContent(section.itemType, {});
}

function handleLinkTypeChange(target, typeField = "linkType", valueField = "linkValue") {
  if (!target) {
    return;
  }
  const linkType = target[typeField];
  if (linkType === "none") {
    target[valueField] = "";
    return;
  }
  if (linkType === "page") {
    target[valueField] = getDefaultPageValue(normalPageOptions, target[valueField]);
    return;
  }
  if (linkType === "tab") {
    target[valueField] = getDefaultPageValue(tabPageOptions, target[valueField]);
    return;
  }
  if (linkType !== "goods") {
    target[valueField] = "";
  }
}

function addMediaItem(section) {
  section.content.items.push(section.itemType === "nav" ? createNavItem() : createMediaItem());
}

function removeMediaItem(section, index) {
  section.content.items.splice(index, 1);
  if (section.content.items.length <= 0) {
    addMediaItem(section);
  }
}

function addNoticeItem(section) {
  section.content.items.push(createNoticeItem());
}

function removeNoticeItem(section, index) {
  section.content.items.splice(index, 1);
  if (section.content.items.length <= 0) {
    addNoticeItem(section);
  }
}

async function preloadGoodsOptions() {
  const goodsIds = [];

  templateForm.itemList.forEach((section) => {
    if (section.itemType === "goods" && section.content.sourceType === "manual") {
      ensureArray(section.content.goodsIds).forEach((goodsId) => pushGoodsId(goodsIds, goodsId));
    }
    if (section.itemType === "goods" && section.content.moreLinkType === "goods") {
      pushGoodsId(goodsIds, section.content.moreLinkValue);
    }
    if (section.itemType === "banner" || section.itemType === "image" || section.itemType === "nav") {
      ensureArray(section.content.items).forEach((item) => {
        if (item.linkType === "goods") {
          pushGoodsId(goodsIds, item.linkValue);
        }
      });
    }
  });

  if (goodsIds.length <= 0) {
    return;
  }

  const responses = await Promise.all(
    goodsIds.map((goodsId) => getGoodsObj(goodsId).then((res) => res.data).catch(() => null))
  );
  mergeGoodsOptions(responses.filter(Boolean));
}

function pushGoodsId(goodsIds, goodsId) {
  if (goodsId && !goodsIds.includes(goodsId)) {
    goodsIds.push(goodsId);
  }
}

async function searchGoodsOptions(keyword) {
  goodsSearching.value = true;
  try {
    const response = await getGoodsPage({
      current: 1,
      size: 20,
      shelf: "1",
      name: keyword || undefined,
    });
    mergeGoodsOptions((((response || {}).data || {}).records || []).filter(Boolean));
  } finally {
    goodsSearching.value = false;
  }
}

function mergeGoodsOptions(list) {
  const optionMap = new Map(goodsOptions.value.map((item) => [item.id, item]));
  ensureArray(list).forEach((item) => {
    const normalizedItem = normalizeGoodsOption(item);
    if (normalizedItem.id) {
      optionMap.set(normalizedItem.id, normalizedItem);
    }
  });
  goodsOptions.value = Array.from(optionMap.values());
}

function normalizeGoodsOption(item = {}) {
  return {
    id: item.id || "",
    name: item.name || item.goodsName || "未命名商品",
    imageUrl: getMallFirstImageUrl(item.picUrls || item.imageUrl || item.picUrl || ""),
    price: getFirstValidNumber([
      item.price,
      item.salePrice,
      item.minPrice,
      item.minSalePrice,
      item.minPriceAmount,
    ]),
  };
}

function getGoodsLinkOptions(currentValue) {
  const options = [...goodsOptions.value];
  if (currentValue && !options.find((item) => item.id === currentValue)) {
    options.unshift({
      id: currentValue,
      name: `当前商品 ${currentValue}`,
      imageUrl: "",
      price: 0,
    });
  }
  return options;
}

function getGoodsLinkOptionsList(currentValues) {
  const options = [...goodsOptions.value];
  ensureArray(currentValues).forEach((goodsId) => {
    if (goodsId && !options.find((item) => item.id === goodsId)) {
      options.unshift({
        id: goodsId,
        name: `当前商品 ${goodsId}`,
        imageUrl: "",
        price: 0,
      });
    }
  });
  return options;
}

function getPageLinkOptions(linkType, currentValue) {
  const baseOptions = linkType === "tab" ? tabPageOptions : normalPageOptions;
  const options = [...baseOptions];
  if (currentValue && !options.find((item) => item.value === currentValue)) {
    options.unshift({
      label: `当前路径 ${currentValue}`,
      value: currentValue,
    });
  }
  return options;
}

function getDefaultPageValue(options, currentValue) {
  if (currentValue && options.find((item) => item.value === currentValue)) {
    return currentValue;
  }
  return options.length > 0 ? options[0].value : "";
}

async function handleSave() {
  if (!templateForm.templateName || !templateForm.templateName.trim()) {
    proxy.$message.error("模板名称不能为空");
    return;
  }
  const validateMessage = validateTemplateForm();
  if (validateMessage) {
    proxy.$message.error(validateMessage);
    return;
  }

  saving.value = true;
  try {
    await saveCurrentTemplate({
      id: templateForm.id,
      templateName: templateForm.templateName.trim(),
      templateCode: templateForm.templateCode ? templateForm.templateCode.trim() : "ma_home_default",
      status: templateForm.status,
      remark: templateForm.remark ? templateForm.remark.trim() : "",
      itemList: templateForm.itemList.map((section, index) => ({
        id: section.id || undefined,
        itemType: section.itemType,
        itemName: (section.itemName || getDefaultItemName(section.itemType)).trim(),
        itemStatus: section.itemStatus,
        itemSort: index + 1,
        content: buildSectionContent(section),
      })),
    });
    proxy.$message.success("首页模板保存成功");
    await loadCurrentTemplate();
  } finally {
    saving.value = false;
  }
}

function validateTemplateForm() {
  for (let index = 0; index < templateForm.itemList.length; index += 1) {
    const message = validateSection(templateForm.itemList[index], index);
    if (message) {
      return message;
    }
  }
  return "";
}

function validateSection(section, index) {
  const sectionName = section.itemName || getDefaultItemName(section.itemType);
  const sectionLabel = `第 ${index + 1} 个组件【${sectionName}】`;

  if (section.itemType === "banner" || section.itemType === "image") {
    const items = ensureArray(section.content.items);
    if (items.length <= 0) {
      return `${sectionLabel}至少需要保留 1 张图片`;
    }
    for (let itemIndex = 0; itemIndex < items.length; itemIndex += 1) {
      const item = items[itemIndex];
      if (!item.imageUrl) {
        return `${sectionLabel}第 ${itemIndex + 1} 项图片未上传`;
      }
      const linkMessage = validateLinkSetting(
        item.linkType,
        item.linkValue,
        `${sectionLabel}第 ${itemIndex + 1} 项`
      );
      if (linkMessage) {
        return linkMessage;
      }
    }
    return "";
  }

  if (section.itemType === "nav") {
    const items = ensureArray(section.content.items);
    if (items.length <= 0) {
      return `${sectionLabel}至少需要保留 1 个导航项`;
    }
    for (let itemIndex = 0; itemIndex < items.length; itemIndex += 1) {
      const item = items[itemIndex];
      if (!item.title || !item.title.trim()) {
        return `${sectionLabel}第 ${itemIndex + 1} 项导航标题不能为空`;
      }
      if (!item.imageUrl) {
        return `${sectionLabel}第 ${itemIndex + 1} 项导航图片未上传`;
      }
      const linkMessage = validateLinkSetting(
        item.linkType,
        item.linkValue,
        `${sectionLabel}第 ${itemIndex + 1} 项`
      );
      if (linkMessage) {
        return linkMessage;
      }
    }
    return "";
  }

  if (section.itemType === "notice") {
    const items = ensureArray(section.content.items);
    if (items.length <= 0) {
      return `${sectionLabel}至少需要保留 1 条公告`;
    }
    for (let itemIndex = 0; itemIndex < items.length; itemIndex += 1) {
      const item = items[itemIndex];
      if (!item.text || !item.text.trim()) {
        return `${sectionLabel}第 ${itemIndex + 1} 条公告内容不能为空`;
      }
    }
    return "";
  }
  if (section.itemType === "service") {
    if (!section.content.title || !section.content.title.trim()) {
      return `${sectionLabel}标题不能为空`;
    }
    if (!section.content.contactButtonText || !section.content.contactButtonText.trim()) {
      return `${sectionLabel}主咨询按钮文案不能为空`;
    }
    if (ensureArray(section.content.tags).filter((item) => String(item || "").trim()).length <= 0) {
      return `${sectionLabel}至少需要配置 1 个服务标签`;
    }
    return "";
  }

  if (!section.content.title || !section.content.title.trim()) {
    return `${sectionLabel}分组标题不能为空`;
  }
  if (section.content.sourceType === "manual" && ensureArray(section.content.goodsIds).length <= 0) {
    return `${sectionLabel}选择了手动商品后，至少要配置 1 个商品`;
  }
  return validateLinkSetting(
    section.content.moreLinkType,
    section.content.moreLinkValue,
    `${sectionLabel}更多链接`
  );
}

function validateLinkSetting(linkType, linkValue, label) {
  if (!["goods", "page", "tab"].includes(linkType)) {
    return "";
  }
  if (!linkValue || !String(linkValue).trim()) {
    return `${label}未配置跳转值`;
  }
  return "";
}

function buildSectionContent(section) {
  if (section.itemType === "banner" || section.itemType === "image") {
    return {
      items: ensureArray(section.content.items).map((item) => ({
        imageUrl: item.imageUrl || "",
        linkType: normalizeLinkType(item.linkType),
        linkValue: normalizeLinkValue(item.linkType, item.linkValue),
      })),
    };
  }
  if (section.itemType === "nav") {
    return {
      items: ensureArray(section.content.items).map((item) => ({
        title: item.title || "",
        imageUrl: item.imageUrl || "",
        linkType: normalizeLinkType(item.linkType),
        linkValue: normalizeLinkValue(item.linkType, item.linkValue),
      })),
    };
  }
  if (section.itemType === "notice") {
    return {
      items: ensureArray(section.content.items).map((item) => ({
        tag: item.tag || "公告",
        text: item.text || "",
      })),
    };
  }
  if (section.itemType === "service") {
    return {
      title: section.content.title || "首页咨询入口",
      desc: section.content.desc || "",
      tags: ensureArray(section.content.tags)
        .map((item) => String(item || "").trim())
        .filter(Boolean),
      contactButtonText: section.content.contactButtonText || "立即咨询",
      goodsButtonText: section.content.goodsButtonText || "选品咨询",
      orderButtonText: section.content.orderButtonText || "订单进度",
      afterSaleButtonText: section.content.afterSaleButtonText || "售后帮助",
    };
  }
  return {
    title: section.content.title || "商品分组",
    subTitle: section.content.subTitle || "",
    sourceType: section.content.sourceType || "hot",
    size: Math.min(Math.max(Number(section.content.size || 6), 1), 20),
    sortField: section.content.sortField || "saleNum",
    sortOrder: section.content.sortOrder === "asc" ? "asc" : "desc",
    moreText: section.content.moreText || "更多",
    moreLinkType: normalizeLinkType(section.content.moreLinkType),
    moreLinkValue: normalizeLinkValue(section.content.moreLinkType, section.content.moreLinkValue),
    goodsIds: ensureArray(section.content.goodsIds),
  };
}

function buildPreviewSection(section) {
  const baseSection = {
    localKey: section.localKey,
    itemType: section.itemType,
    itemName: section.itemName || getDefaultItemName(section.itemType),
    disabled: section.itemStatus !== "1",
  };

  if (section.itemType === "goods") {
    return {
      ...baseSection,
      title: section.content.title || "商品分组",
      subTitle: section.content.subTitle || "",
      moreText: section.content.moreText || "更多",
      goodsList: buildPreviewGoodsList(section),
    };
  }
  if (section.itemType === "service") {
    return {
      ...baseSection,
      title: section.content.title || "首页咨询入口",
      desc: section.content.desc || "",
      tags: ensureArray(section.content.tags).filter((item) => String(item || "").trim()),
      contactButtonText: section.content.contactButtonText || "立即咨询",
      goodsButtonText: section.content.goodsButtonText || "选品咨询",
      orderButtonText: section.content.orderButtonText || "订单进度",
      afterSaleButtonText: section.content.afterSaleButtonText || "售后帮助",
    };
  }

  return {
    ...baseSection,
    items: ensureArray(section.content.items).map((item) => ({
      title: item.title || "",
      tag: item.tag || "",
      text: item.text || "",
      imageUrl: item.imageUrl ? resolveMallImageUrl(item.imageUrl) : "",
    })),
  };
}

function buildPreviewGoodsList(section) {
  const size = Math.min(Math.max(Number(section.content.size || 4), 1), 6);
  if (section.content.sourceType === "manual") {
    const goodsList = ensureArray(section.content.goodsIds)
      .slice(0, size)
      .map((goodsId) => {
        const goodsInfo = goodsOptions.value.find((item) => item.id === goodsId);
        return buildPreviewGoodsCard(goodsInfo, goodsId);
      });
    return goodsList.length > 0 ? goodsList : buildPlaceholderGoods(size, "manual");
  }
  return buildPlaceholderGoods(size, section.content.sourceType);
}

function buildPreviewGoodsCard(goodsInfo, goodsId) {
  if (goodsInfo) {
    return {
      name: goodsInfo.name || `商品 ${goodsInfo.id}`,
      imageUrl: goodsInfo.imageUrl || "",
      priceText: formatPrice(goodsInfo.price),
    };
  }
  return {
    name: goodsId ? `商品 ${goodsId}` : "商品示意",
    imageUrl: "",
    priceText: formatPrice(0),
  };
}

function buildPlaceholderGoods(size, sourceType) {
  const count = Math.min(size, 6);
  const prefixMap = {
    hot: "热销商品",
    new: "新品推荐",
    manual: "商品示意",
  };
  const prefix = prefixMap[sourceType] || "商品示意";
  return Array.from({ length: count }).map((_, index) => ({
    name: `${prefix} ${index + 1}`,
    imageUrl: "",
    priceText: formatPrice(99 + index),
  }));
}

function normalizeLinkValue(linkType, linkValue) {
  if (linkType === "none") {
    return "";
  }
  return linkValue || "";
}

function createMediaItem() {
  return {
    imageUrl: "",
    linkType: "none",
    linkValue: "",
  };
}

function createNavItem() {
  return {
    title: "",
    imageUrl: "",
    linkType: "none",
    linkValue: "",
  };
}

function createNoticeItem() {
  return {
    tag: "公告",
    text: "",
  };
}

function normalizeTagInput(tagText) {
  return String(tagText || "")
    .split(/[,，\n]/)
    .map((item) => item.trim())
    .filter(Boolean)
    .slice(0, 6);
}

function ensureArray(list) {
  return Array.isArray(list) ? list : [];
}

function normalizeLinkType(linkType) {
  return ["goods", "page", "tab"].includes(linkType) ? linkType : "none";
}

function getDefaultItemName(itemType) {
  const option = itemTypeOptions.find((item) => item.value === itemType);
  return option ? option.label : "首页组件";
}

function getPreviewNavStyle(items) {
  const itemCount = ensureArray(items).length;
  const columnCount = Math.min(Math.max(itemCount || 4, 4), 5);
  return {
    gridTemplateColumns: `repeat(${columnCount}, minmax(0, 1fr))`,
  };
}

function resolvePreviewUrl(imageUrl) {
  return resolveMallImageUrl(imageUrl);
}

function formatPrice(value) {
  const numberValue = Number(value);
  const finalValue = Number.isFinite(numberValue) ? numberValue : 0;
  return `¥${finalValue.toFixed(2)}`;
}

function getFirstValidNumber(valueList) {
  const matchedValue = valueList.find((item) => item !== null && item !== undefined && item !== "");
  return matchedValue === undefined ? 0 : Number(matchedValue);
}

function nextSectionKey() {
  sectionKeySeed += 1;
  return `section_${Date.now()}_${sectionKeySeed}`;
}

function syncItemSort() {
  templateForm.itemList.forEach((item, index) => {
    item.itemSort = index + 1;
  });
}
</script>

<style lang="scss" scoped>
.home-template-page {
  display: flex;
  flex-direction: column;
}

.layout-row {
  align-items: flex-start;
}

.page-card {
  margin-bottom: 16px;
  border-radius: 18px;
}

.preview-card {
  position: sticky;
  top: 16px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.page-title {
  color: #17253d;
  font-size: 18px;
  font-weight: 700;
}

.page-desc {
  margin-top: 6px;
  color: #66758c;
  font-size: 13px;
  line-height: 1.6;
}

.page-desc-light {
  color: #8a97aa;
  font-size: 12px;
}

.page-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.add-actions {
  justify-content: flex-end;
}

.section-toolbar {
  margin-bottom: 16px;
  padding: 16px 18px;
  border: 1px solid #dbe7ff;
  border-radius: 16px;
  background: linear-gradient(180deg, #f8fbff 0%, #f3f7ff 100%);
}

.section-toolbar-head {
  margin-bottom: 12px;
}

.section-toolbar-title {
  color: #17253d;
  font-size: 15px;
  font-weight: 700;
}

.section-toolbar-desc {
  margin-top: 6px;
  color: #5f6f86;
  font-size: 12px;
  line-height: 1.6;
}

.section-toolbar-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.section-box {
  padding: 18px;
  border: 1px solid #e8edf5;
  border-radius: 16px;
  background: #fff;
}

.section-box + .section-box {
  margin-top: 16px;
}

.section-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px dashed #e7edf5;
}

.section-meta,
.section-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.section-index {
  color: #7b8799;
  font-size: 13px;
  font-weight: 600;
}

.section-name {
  color: #17253d;
  font-size: 15px;
  font-weight: 600;
}

.field-label {
  margin: 12px 0 8px;
  color: #475569;
  font-size: 13px;
  font-weight: 600;
}

.config-block {
  margin-top: 12px;
}

.block-title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.block-title {
  color: #17253d;
  font-size: 14px;
  font-weight: 700;
}

.item-row {
  display: grid;
  grid-template-columns: 240px minmax(0, 1fr) auto;
  gap: 16px;
  padding: 14px;
  background: #f8fafc;
  border: 1px solid #edf1f5;
  border-radius: 14px;
}

.item-row + .item-row,
.notice-row + .notice-row {
  margin-top: 12px;
}

.upload-wrap {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.item-form {
  display: flex;
  flex-direction: column;
}

.notice-row {
  display: grid;
  grid-template-columns: 140px minmax(0, 1fr) auto;
  gap: 12px;
  padding: 14px;
  background: #f8fafc;
  border: 1px solid #edf1f5;
  border-radius: 14px;
  align-items: center;
}

.notice-tag,
.manual-select {
  width: 100%;
}

.manual-box {
  margin-top: 12px;
  padding: 14px;
  background: #f8fafc;
  border: 1px solid #edf1f5;
  border-radius: 14px;
}

.preview-tip {
  margin-bottom: 14px;
  padding: 12px 14px;
  border-radius: 14px;
  background: #f5f8ff;
  color: #516173;
  font-size: 13px;
  line-height: 1.6;
}

.dialog-preview-wrap {
  display: flex;
  justify-content: center;
}

.phone-shell {
  width: 100%;
  max-width: 360px;
  margin: 0 auto;
  padding: 12px;
  border-radius: 28px;
  background: linear-gradient(180deg, #263247 0%, #111927 100%);
  box-shadow: 0 16px 40px rgba(17, 25, 39, 0.2);
}

.phone-shell.is-dialog {
  max-width: 380px;
}

.phone-status {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding: 0 8px;
  color: #fff;
  font-size: 12px;
  letter-spacing: 0.5px;
}

.phone-content {
  min-height: 520px;
  padding: 12px;
  border-radius: 22px;
  background: linear-gradient(180deg, #f7fbff 0%, #eef4fb 100%);
}

.preview-search-bar {
  margin-bottom: 12px;
  padding: 10px 14px;
  border-radius: 999px;
  background: #fff;
  color: #94a3b8;
  font-size: 13px;
  box-shadow: inset 0 0 0 1px #e2e8f0;
}

.preview-empty {
  padding: 40px 0;
  color: #94a3b8;
  font-size: 13px;
  text-align: center;
}

.preview-section {
  margin-bottom: 12px;
  padding: 12px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 8px 24px rgba(148, 163, 184, 0.12);
  transition: opacity 0.2s ease;
  position: relative;
}

.preview-section.is-disabled {
  opacity: 0.45;
}

.preview-section-status {
  color: #b45309;
  font-size: 12px;
  font-weight: 600;
}

.preview-section-status--float {
  position: absolute;
  top: 10px;
  right: 12px;
  z-index: 2;
  padding: 2px 8px;
  border-radius: 999px;
  background: rgba(255, 247, 237, 0.92);
}

.preview-image-list {
  display: grid;
  gap: 10px;
}

.preview-image-item {
  overflow: hidden;
  border-radius: 14px;
  background: #e2e8f0;
}

.preview-banner-carousel {
  margin: 10px 0 0;
}

:deep(.preview-banner-carousel .el-carousel__container) {
  overflow: visible;
}

:deep(.preview-banner-carousel .el-carousel__indicators--outside) {
  margin-top: -2px;
}

:deep(.preview-banner-carousel .el-carousel__button) {
  width: 8px;
  height: 8px;
  border-radius: 999px;
}

.preview-banner-item {
  width: calc(100% - 18px);
  height: 146px;
  margin: 10px auto;
  overflow: hidden;
  border-radius: 16px;
  background: #e2e8f0;
  box-shadow: 0 10px 30px rgba(148, 163, 184, 0.24);
}

.preview-banner-image,
.preview-media-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-banner-empty {
  height: 100%;
}

.preview-media-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  color: #94a3b8;
  font-size: 13px;
  background: linear-gradient(135deg, #e2e8f0 0%, #f8fafc 100%);
}

.preview-nav-grid {
  display: grid;
  margin-top: 4px;
  padding: 6px 2px 2px;
  gap: 2px 0;
  text-align: center;
}

.preview-nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 10px 2px 12px;
}

.preview-nav-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 52px;
  height: 52px;
  overflow: hidden;
  border-radius: 18px;
  background: linear-gradient(135deg, #eef4ff 0%, #f8fbff 100%);
  color: #2563eb;
  font-size: 13px;
  font-weight: 700;
}

.preview-nav-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-nav-title {
  width: 100%;
  color: #111827;
  font-size: 12px;
  text-align: center;
  line-height: 1.4;
  word-break: break-all;
}

.preview-notice-carousel {
  margin-top: 2px;
  padding: 4px 0;
}

:deep(.preview-notice-carousel .el-carousel__container) {
  border-radius: 12px;
  background: #ffffff;
}

:deep(.preview-notice-carousel .el-carousel__indicators) {
  display: none;
}

.preview-notice-item {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
  height: 42px;
  padding: 0 10px;
}

.preview-notice-tag {
  flex-shrink: 0;
  padding: 4px 8px;
  border-radius: 999px;
  background: #fee2e2;
  color: #dc2626;
  font-size: 11px;
  font-weight: 700;
}

.preview-notice-text {
  color: #475569;
  font-size: 12px;
  line-height: 1.5;
  word-break: break-all;
}

.preview-service-card {
  margin-top: 10px;
  padding: 16px;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 12px 32px rgba(37, 99, 235, 0.08);
}

.preview-service-header {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
}

.preview-service-title {
  color: #111827;
  font-size: 15px;
  font-weight: 700;
}

.preview-service-desc {
  margin-top: 8px;
  color: #64748b;
  font-size: 12px;
  line-height: 1.7;
}

.preview-service-button {
  flex-shrink: 0;
  padding: 8px 14px;
  border-radius: 999px;
  background: linear-gradient(135deg, #2967ff 0%, #5b8dff 100%);
  color: #fff;
  font-size: 12px;
  font-weight: 600;
}

.preview-service-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 14px;
}

.preview-service-tag {
  padding: 5px 10px;
  border-radius: 999px;
  background: #eef4ff;
  color: #2967ff;
  font-size: 11px;
}

.preview-service-shortcuts {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 10px;
  margin-top: 14px;
}

.preview-service-shortcut {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 40px;
  padding: 0 8px;
  border-radius: 12px;
  background: #f8fafc;
  color: #1f2937;
  font-size: 12px;
  text-align: center;
}

.preview-service-shortcut.is-primary {
  background: #edf4ff;
  color: #2967ff;
  font-weight: 600;
}

.preview-goods-head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 10px;
}

.preview-goods-title {
  color: #0f172a;
  font-size: 15px;
  font-weight: 700;
}

.preview-goods-sub-title {
  margin-top: 2px;
  color: #94a3b8;
  font-size: 12px;
}

.preview-goods-more {
  color: #2563eb;
  font-size: 12px;
  font-weight: 600;
}

.preview-goods-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}

.preview-goods-card {
  overflow: hidden;
  border-radius: 14px;
  background: #fff;
  box-shadow: inset 0 0 0 1px #e2e8f0;
}

.preview-goods-image-wrap {
  background: #f8fafc;
}

.preview-goods-image,
.preview-goods-image-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 118px;
}

.preview-goods-image {
  object-fit: cover;
}

.preview-goods-image-empty {
  color: #94a3b8;
  font-size: 13px;
  background: linear-gradient(135deg, #e2e8f0 0%, #f8fafc 100%);
}

.preview-goods-name {
  min-height: 34px;
  padding: 8px 10px 0;
  color: #1e293b;
  font-size: 12px;
  line-height: 1.4;
  word-break: break-all;
}

.preview-goods-price {
  padding: 8px 10px 10px;
  color: #dc2626;
  font-size: 14px;
  font-weight: 700;
}

@media (max-width: 1200px) {
  .item-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 992px) {
  .preview-card {
    position: static;
  }

  .page-header,
  .section-head {
    flex-direction: column;
    align-items: flex-start;
  }

  .notice-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .preview-nav-grid {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }
}
</style>

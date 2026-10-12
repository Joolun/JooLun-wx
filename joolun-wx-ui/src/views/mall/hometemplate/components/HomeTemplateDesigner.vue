<template>
  <div v-loading="loading" class="home-designer-page">
    <el-card shadow="never" class="designer-top-card">
      <div class="designer-topbar">
        <div>
          <div class="designer-title-row">
            <h2>可视化首页装修</h2>
            <el-tag v-if="isDirty" type="warning" effect="light" round>有未保存修改</el-tag>
            <el-tag v-else type="success" effect="plain" round>已保存</el-tag>
          </div>
          <p>从左侧拖入组件，在中间画布调整顺序，选中后在右侧配置内容。</p>
        </div>
        <div class="designer-actions">
          <el-button @click="templateSettingsOpen = !templateSettingsOpen">
            {{ templateSettingsOpen ? "收起模板设置" : "模板设置" }}
          </el-button>
          <el-button @click="handleReload">重新加载</el-button>
          <el-tooltip :disabled="!isDirty" content="请先保存当前修改，再刷新缓存" placement="bottom">
            <span>
              <el-button plain :loading="refreshingCache" :disabled="!canEdit || isDirty" @click="handleRefreshCache">
                刷新缓存
              </el-button>
            </span>
          </el-tooltip>
          <el-button plain @click="previewDialogVisible = true">放大预览</el-button>
          <el-button type="primary" :loading="saving" :disabled="!canEdit" @click="handleSave">
            保存模板
          </el-button>
        </div>
      </div>

      <el-collapse-transition>
        <el-form v-show="templateSettingsOpen" class="template-settings" label-position="top">
          <el-row :gutter="16">
            <el-col :xl="7" :lg="7" :md="12" :sm="24" :xs="24">
              <el-form-item label="模板名称">
                <el-input v-model="templateForm.templateName" maxlength="100" />
              </el-form-item>
            </el-col>
            <el-col :xl="7" :lg="7" :md="12" :sm="24" :xs="24">
              <el-form-item label="模板编码">
                <el-input v-model="templateForm.templateCode" maxlength="100" />
              </el-form-item>
            </el-col>
            <el-col :xl="4" :lg="4" :md="12" :sm="24" :xs="24">
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
            <el-col :xl="6" :lg="6" :md="12" :sm="24" :xs="24">
              <el-form-item label="备注">
                <el-input v-model="templateForm.remark" maxlength="500" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-collapse-transition>
    </el-card>

    <div class="designer-workspace">
      <aside class="designer-panel component-library">
        <div class="panel-heading">
          <div>
            <div class="panel-title">组件库</div>
            <div class="panel-desc">拖到画布，或点击快速添加</div>
          </div>
          <el-tag size="small" effect="plain">{{ templateForm.itemList.length }} 个</el-tag>
        </div>

        <draggable
          :list="itemTypeOptions"
          :group="paletteDragGroup"
          :sort="false"
          :clone="clonePaletteSection"
          :disabled="!canEdit"
          item-key="value"
          handle=".palette-drag-handle"
          class="palette-list"
        >
          <template #item="{ element: item }">
            <button
              type="button"
              class="palette-item palette-drag-handle"
              :disabled="!canEdit"
              @click="addSection(item.value)"
            >
              <span class="palette-icon" :style="{ '--palette-color': item.color }">{{ item.icon }}</span>
              <span class="palette-copy">
                <strong>{{ item.label }}</strong>
                <small>{{ item.desc }}</small>
              </span>
              <span class="palette-add">＋</span>
            </button>
          </template>
        </draggable>

        <div class="palette-help">
          <strong>操作提示</strong>
          <span>组件可重复添加；画布中的组件可拖拽、复制、停用或删除。</span>
        </div>
      </aside>

      <main class="designer-canvas-panel">
        <div class="canvas-heading">
          <div>
            <div class="panel-title">手机画布</div>
            <div class="panel-desc">所见即所得，点击组件后在右侧编辑</div>
          </div>
          <div class="canvas-heading-actions">
            <el-button text @click="previewDialogVisible = true">放大预览</el-button>
          </div>
        </div>

        <div class="phone-shell canvas-phone">
          <div class="phone-status">
            <span>09:41</span>
            <span>首页</span>
            <span>100%</span>
          </div>
          <div class="phone-content canvas-phone-content">
            <div class="preview-search-bar">搜索商品、分类、活动</div>
            <draggable
              v-model="templateForm.itemList"
              item-key="localKey"
              :group="canvasDragGroup"
              handle=".canvas-drag-handle"
              ghost-class="canvas-drag-ghost"
              chosen-class="canvas-drag-chosen"
              drag-class="canvas-drag-active"
              :animation="220"
              :disabled="!canEdit"
              :scroll="true"
              :scroll-sensitivity="90"
              :scroll-speed="14"
              class="canvas-section-list"
              @change="handleSectionDragChange"
            >
              <template #item="{ element: section, index }">
                <div
                  class="canvas-section-frame"
                  :class="{
                    'is-selected': selectedSectionKey === section.localKey,
                    'is-disabled': section.itemStatus !== '1',
                  }"
                  :data-section-key="section.localKey"
                  tabindex="0"
                  @click="selectSection(section)"
                  @keydown.enter.prevent="selectSection(section)"
                >
                  <div class="canvas-section-label">
                    <span>{{ index + 1 }}</span>
                    {{ section.itemName || getDefaultItemName(section.itemType) }}
                  </div>
                  <div class="canvas-section-tools" @click.stop>
                    <button
                      type="button"
                      class="canvas-tool canvas-drag-handle"
                      title="拖拽排序"
                      :disabled="!canEdit"
                    >
                      ⠿
                    </button>
                    <button
                      type="button"
                      class="canvas-tool"
                      title="复制组件"
                      :disabled="!canEdit"
                      @click="copySection(section, index)"
                    >
                      ⧉
                    </button>
                    <button
                      type="button"
                      class="canvas-tool"
                      :title="section.itemStatus === '1' ? '停用组件' : '启用组件'"
                      :disabled="!canEdit"
                      @click="toggleSectionStatus(section)"
                    >
                      {{ section.itemStatus === "1" ? "◉" : "○" }}
                    </button>
                    <button
                      type="button"
                      class="canvas-tool is-danger"
                      title="删除组件"
                      :disabled="!canEdit"
                      @click="removeSection(index)"
                    >
                      ×
                    </button>
                  </div>
                  <HomeComponentRenderer :section="previewSectionMap.get(section.localKey)" />
                </div>
              </template>
            </draggable>
            <div v-if="templateForm.itemList.length <= 0" class="canvas-empty">
              <div class="canvas-empty-icon">＋</div>
              <strong>把组件拖到这里</strong>
              <span>也可以点击左侧组件快速添加</span>
            </div>
          </div>
        </div>
      </main>

      <aside ref="propertyPanelRef" class="designer-panel property-panel">
        <template v-if="selectedSection">
          <div class="panel-heading property-heading">
            <div>
              <div class="panel-title">属性设置</div>
              <div class="panel-desc">第 {{ selectedSectionIndex + 1 }} 个 · {{ getDefaultItemName(selectedSection.itemType) }}</div>
            </div>
            <el-tag :type="selectedSection.itemStatus === '1' ? 'success' : 'info'" size="small" effect="light">
              {{ selectedSection.itemStatus === "1" ? "展示中" : "已停用" }}
            </el-tag>
          </div>

          <div class="property-quick-actions">
            <el-button
              size="small"
              :disabled="!canEdit || selectedSectionIndex <= 0"
              @click="moveSection(selectedSectionIndex, -1)"
            >
              上移
            </el-button>
            <el-button
              size="small"
              :disabled="!canEdit || selectedSectionIndex >= templateForm.itemList.length - 1"
              @click="moveSection(selectedSectionIndex, 1)"
            >
              下移
            </el-button>
            <el-button size="small" :disabled="!canEdit" @click="copySection(selectedSection, selectedSectionIndex)">
              复制
            </el-button>
            <el-button size="small" :disabled="!canEdit" @click="toggleSectionStatus(selectedSection)">
              {{ selectedSection.itemStatus === "1" ? "停用" : "启用" }}
            </el-button>
            <el-button size="small" type="danger" plain :disabled="!canEdit" @click="removeSection(selectedSectionIndex)">
              删除
            </el-button>
          </div>

          <div class="property-body">
            <el-form label-position="top" class="property-form">
              <div class="property-section-title">基础设置</div>
              <el-form-item label="组件类型">
                <el-select v-model="selectedSection.itemType" @change="handleItemTypeChange(selectedSection)">
                  <el-option
                    v-for="item in itemTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="组件名称">
                <el-input v-model="selectedSection.itemName" maxlength="100" />
              </el-form-item>
              <el-form-item label="显示状态">
                <el-switch
                  v-model="selectedSection.itemStatus"
                  active-value="1"
                  inactive-value="0"
                  active-text="启用"
                  inactive-text="停用"
                />
              </el-form-item>

              <template v-if="['banner', 'image', 'nav'].includes(selectedSection.itemType)">
                <div class="property-section-title with-action">
                  <span>{{ selectedSection.itemType === "nav" ? "导航项" : "图片项" }}</span>
                  <el-button size="small" type="primary" plain @click="addMediaItem(selectedSection)">新增一项</el-button>
                </div>
                <draggable
                  v-model="selectedSection.content.items"
                  item-key="localKey"
                  handle=".item-drag-handle"
                  ghost-class="item-drag-ghost"
                  chosen-class="item-drag-chosen"
                  :animation="180"
                  :disabled="!canEdit"
                  class="property-item-list"
                >
                  <template #item="{ element: item, index: itemIndex }">
                    <div class="property-item-card">
                      <div class="property-item-head">
                        <button type="button" class="item-drag-handle" title="拖拽排序">⠿</button>
                        <strong>第 {{ itemIndex + 1 }} 项</strong>
                        <el-button
                          text
                          type="danger"
                          :disabled="selectedSection.content.items.length <= 1"
                          @click="removeMediaItem(selectedSection, itemIndex)"
                        >
                          删除
                        </el-button>
                      </div>
                      <ImageUpload v-model="item.imageUrl" :limit="1" />
                      <el-form-item v-if="selectedSection.itemType === 'nav'" label="标题">
                        <el-input v-model="item.title" maxlength="20" placeholder="请输入导航标题" />
                      </el-form-item>
                      <el-form-item label="链接类型">
                        <el-select v-model="item.linkType" @change="handleLinkTypeChange(item)">
                          <el-option
                            v-for="option in linkTypeOptions"
                            :key="option.value"
                            :label="option.label"
                            :value="option.value"
                          />
                        </el-select>
                      </el-form-item>
                      <el-form-item label="链接值">
                        <el-select
                          v-if="item.linkType === 'goods'"
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
                        <el-select
                          v-else-if="item.linkType === 'page' || item.linkType === 'tab'"
                          v-model="item.linkValue"
                          filterable
                          placeholder="请选择页面"
                        >
                          <el-option
                            v-for="option in getPageLinkOptions(item.linkType, item.linkValue)"
                            :key="option.value"
                            :label="option.label"
                            :value="option.value"
                          />
                        </el-select>
                        <el-input v-else v-model="item.linkValue" disabled placeholder="当前类型无需配置链接值" />
                      </el-form-item>
                    </div>
                  </template>
                </draggable>
              </template>

              <template v-else-if="selectedSection.itemType === 'notice'">
                <div class="property-section-title with-action">
                  <span>公告项</span>
                  <el-button size="small" type="primary" plain @click="addNoticeItem(selectedSection)">新增公告</el-button>
                </div>
                <draggable
                  v-model="selectedSection.content.items"
                  item-key="localKey"
                  handle=".item-drag-handle"
                  ghost-class="item-drag-ghost"
                  chosen-class="item-drag-chosen"
                  :animation="180"
                  :disabled="!canEdit"
                  class="property-item-list"
                >
                  <template #item="{ element: item, index: itemIndex }">
                    <div class="property-item-card">
                      <div class="property-item-head">
                        <button type="button" class="item-drag-handle" title="拖拽排序">⠿</button>
                        <strong>第 {{ itemIndex + 1 }} 条</strong>
                        <el-button
                          text
                          type="danger"
                          :disabled="selectedSection.content.items.length <= 1"
                          @click="removeNoticeItem(selectedSection, itemIndex)"
                        >
                          删除
                        </el-button>
                      </div>
                      <el-form-item label="标签">
                        <el-input v-model="item.tag" maxlength="10" placeholder="如：公告" />
                      </el-form-item>
                      <el-form-item label="公告内容">
                        <el-input v-model="item.text" type="textarea" :rows="2" maxlength="100" show-word-limit />
                      </el-form-item>
                    </div>
                  </template>
                </draggable>
              </template>

              <template v-else-if="selectedSection.itemType === 'service'">
                <div class="property-section-title">咨询入口设置</div>
                <el-form-item label="主标题">
                  <el-input v-model="selectedSection.content.title" maxlength="20" />
                </el-form-item>
                <el-form-item label="说明文案">
                  <el-input v-model="selectedSection.content.desc" type="textarea" :rows="3" maxlength="80" show-word-limit />
                </el-form-item>
                <el-form-item label="服务标签">
                  <el-input
                    :model-value="ensureArray(selectedSection.content.tags).join('，')"
                    maxlength="80"
                    placeholder="多个标签用逗号隔开"
                    @update:model-value="selectedSection.content.tags = normalizeTagInput($event)"
                  />
                </el-form-item>
                <el-form-item label="主按钮文案">
                  <el-input v-model="selectedSection.content.contactButtonText" maxlength="10" />
                </el-form-item>
                <el-row :gutter="10">
                  <el-col :span="8">
                    <el-form-item label="选品按钮">
                      <el-input v-model="selectedSection.content.goodsButtonText" maxlength="10" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="订单按钮">
                      <el-input v-model="selectedSection.content.orderButtonText" maxlength="10" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="售后按钮">
                      <el-input v-model="selectedSection.content.afterSaleButtonText" maxlength="10" />
                    </el-form-item>
                  </el-col>
                </el-row>
              </template>

              <template v-else-if="selectedSection.itemType === 'goods'">
                <div class="property-section-title">商品分组设置</div>
                <el-form-item label="分组标题">
                  <el-input v-model="selectedSection.content.title" maxlength="20" />
                </el-form-item>
                <el-form-item label="副标题">
                  <el-input v-model="selectedSection.content.subTitle" maxlength="50" />
                </el-form-item>
                <el-row :gutter="10">
                  <el-col :span="12">
                    <el-form-item label="商品来源">
                      <el-select v-model="selectedSection.content.sourceType">
                        <el-option
                          v-for="item in goodsSourceOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="展示数量">
                      <el-input-number v-model="selectedSection.content.size" :min="1" :max="20" controls-position="right" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="10">
                  <el-col :span="12">
                    <el-form-item label="排序字段">
                      <el-select
                        v-model="selectedSection.content.sortField"
                        :disabled="selectedSection.content.sourceType === 'manual'"
                      >
                        <el-option label="销量" value="saleNum" />
                        <el-option label="创建时间" value="createTime" />
                        <el-option label="后台排序" value="sort" />
                        <el-option label="更新时间" value="updateTime" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="排序方式">
                      <el-select
                        v-model="selectedSection.content.sortOrder"
                        :disabled="selectedSection.content.sourceType === 'manual'"
                      >
                        <el-option label="降序" value="desc" />
                        <el-option label="升序" value="asc" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="更多文案">
                  <el-input v-model="selectedSection.content.moreText" maxlength="20" />
                </el-form-item>
                <el-form-item label="更多链接类型">
                  <el-select
                    v-model="selectedSection.content.moreLinkType"
                    @change="handleLinkTypeChange(selectedSection.content, 'moreLinkType', 'moreLinkValue')"
                  >
                    <el-option
                      v-for="option in linkTypeOptions"
                      :key="option.value"
                      :label="option.label"
                      :value="option.value"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="更多链接值">
                  <el-select
                    v-if="selectedSection.content.moreLinkType === 'goods'"
                    v-model="selectedSection.content.moreLinkValue"
                    filterable
                    remote
                    reserve-keyword
                    :remote-method="searchGoodsOptions"
                    :loading="goodsSearching"
                    placeholder="搜索并选择商品"
                  >
                    <el-option
                      v-for="option in getGoodsLinkOptions(selectedSection.content.moreLinkValue)"
                      :key="option.id"
                      :label="option.name"
                      :value="option.id"
                    />
                  </el-select>
                  <el-select
                    v-else-if="['page', 'tab'].includes(selectedSection.content.moreLinkType)"
                    v-model="selectedSection.content.moreLinkValue"
                    filterable
                    placeholder="请选择页面"
                  >
                    <el-option
                      v-for="option in getPageLinkOptions(
                        selectedSection.content.moreLinkType,
                        selectedSection.content.moreLinkValue
                      )"
                      :key="option.value"
                      :label="option.label"
                      :value="option.value"
                    />
                  </el-select>
                  <el-input v-else v-model="selectedSection.content.moreLinkValue" disabled />
                </el-form-item>

                <template v-if="selectedSection.content.sourceType === 'manual'">
                  <el-form-item label="手动选择商品">
                    <el-select
                      v-model="selectedSection.content.goodsIds"
                      multiple
                      filterable
                      remote
                      reserve-keyword
                      collapse-tags
                      collapse-tags-tooltip
                      :remote-method="searchGoodsOptions"
                      :loading="goodsSearching"
                      placeholder="搜索并选择商品"
                    >
                      <el-option
                        v-for="item in getGoodsLinkOptionsList(selectedSection.content.goodsIds)"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                      />
                    </el-select>
                  </el-form-item>
                  <div v-if="selectedSection.content.goodsIds.length" class="manual-sort-title">
                    已选商品展示顺序
                    <span>拖拽调整</span>
                  </div>
                  <draggable
                    v-if="selectedSection.content.goodsIds.length"
                    v-model="selectedSection.content.goodsIds"
                    :item-key="getGoodsItemKey"
                    handle=".goods-drag-handle"
                    ghost-class="item-drag-ghost"
                    :animation="180"
                    class="manual-goods-list"
                  >
                    <template #item="{ element: goodsId, index: goodsIndex }">
                      <div class="manual-goods-item">
                        <button type="button" class="goods-drag-handle" title="拖拽排序">⠿</button>
                        <span class="manual-goods-index">{{ goodsIndex + 1 }}</span>
                        <div class="manual-goods-thumb">
                          <img
                            v-if="getSelectedGoods(goodsId).imageUrl"
                            :src="resolvePreviewUrl(getSelectedGoods(goodsId).imageUrl)"
                            alt=""
                          />
                          <span v-else>商品</span>
                        </div>
                        <div class="manual-goods-info">
                          <strong>{{ getSelectedGoods(goodsId).name }}</strong>
                          <small>ID：{{ goodsId }}</small>
                        </div>
                        <el-button text type="danger" @click="removeManualGoods(selectedSection, goodsIndex)">移除</el-button>
                      </div>
                    </template>
                  </draggable>
                </template>
              </template>
            </el-form>
          </div>
        </template>

        <div v-else class="property-empty">
          <div class="property-empty-icon">⌁</div>
          <strong>请选择一个组件</strong>
          <span>点击中间画布中的组件，即可在这里编辑属性。</span>
        </div>
      </aside>
    </div>

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
            <HomeComponentRenderer
              v-for="section in previewSections"
              :key="`${section.localKey}-dialog`"
              :section="section"
            />
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, getCurrentInstance, nextTick, onBeforeUnmount, onMounted, reactive, ref } from "vue";
import { onBeforeRouteLeave } from "vue-router";
import { ElMessageBox } from "element-plus";
import draggable from "vuedraggable";
import { checkPermi } from "@/utils/permission";
import {
  getCurrentTemplate,
  refreshCurrentTemplateCache,
  saveCurrentTemplate,
} from "@/api/mall/hometemplate";
import { getObj as getGoodsObj, getPage as getGoodsPage } from "@/api/mall/goodsspu";
import { getMallFirstImageUrl, resolveMallImageUrl } from "@/utils/mall-image";
import HomeComponentRenderer from "./HomeComponentRenderer.vue";

const { proxy } = getCurrentInstance();

const itemTypeOptions = [
  { label: "轮播图", value: "banner", icon: "▣", color: "#2563eb", desc: "多图轮播展示" },
  { label: "导航宫格", value: "nav", icon: "⌘", color: "#7c3aed", desc: "快捷入口导航" },
  { label: "公告栏", value: "notice", icon: "☷", color: "#ea580c", desc: "滚动公告信息" },
  { label: "咨询入口", value: "service", icon: "✦", color: "#0891b2", desc: "客服咨询卡片" },
  { label: "活动图片", value: "image", icon: "▧", color: "#db2777", desc: "活动与专题图片" },
  { label: "商品分组", value: "goods", icon: "◆", color: "#16a34a", desc: "热销或自选商品" },
];

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

const paletteDragGroup = { name: "home-sections", pull: "clone", put: false };
const canvasDragGroup = { name: "home-sections", pull: true, put: true };
const loading = ref(false);
const saving = ref(false);
const refreshingCache = ref(false);
const goodsSearching = ref(false);
const previewDialogVisible = ref(false);
const templateSettingsOpen = ref(false);
const selectedSectionKey = ref("");
const savedSnapshot = ref("");
const goodsOptions = ref([]);
const propertyPanelRef = ref();
const templateForm = reactive({
  id: "",
  templateName: "",
  templateCode: "",
  status: "1",
  remark: "",
  itemList: [],
});
const canEdit = computed(() => checkPermi(["mall:hometemplate:edit"]));
const selectedSection = computed(
  () => templateForm.itemList.find((section) => section.localKey === selectedSectionKey.value) || null
);
const selectedSectionIndex = computed(() =>
  selectedSection.value ? templateForm.itemList.indexOf(selectedSection.value) : -1
);
const previewSections = computed(() => templateForm.itemList.map((section) => buildPreviewSection(section)));
const previewSectionMap = computed(
  () => new Map(previewSections.value.map((section) => [section.localKey, section]))
);
const currentSnapshot = computed(() => serializeCurrentForm());
const isDirty = computed(
  () => Boolean(savedSnapshot.value) && currentSnapshot.value !== savedSnapshot.value
);

let sectionKeySeed = 0;
let contentItemKeySeed = 0;

onMounted(() => {
  window.addEventListener("beforeunload", handleBeforeUnload);
  loadCurrentTemplate();
});

onBeforeUnmount(() => {
  window.removeEventListener("beforeunload", handleBeforeUnload);
});

onBeforeRouteLeave(async () => {
  if (!isDirty.value) {
    return true;
  }
  try {
    await ElMessageBox.confirm("当前装修内容尚未保存，确定离开吗？", "未保存修改", {
      confirmButtonText: "离开",
      cancelButtonText: "继续编辑",
      type: "warning",
    });
    return true;
  } catch {
    return false;
  }
});

function handleBeforeUnload(event) {
  if (!isDirty.value) {
    return;
  }
  event.preventDefault();
  event.returnValue = "";
}

async function loadCurrentTemplate(preferredIndex = 0) {
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
    goodsOptions.value = [];
    await preloadGoodsOptions();
    const nextIndex = Math.min(Math.max(preferredIndex, 0), Math.max(templateForm.itemList.length - 1, 0));
    selectedSectionKey.value = templateForm.itemList[nextIndex]?.localKey || "";
    savedSnapshot.value = serializeCurrentForm();
  } finally {
    loading.value = false;
  }
}

async function handleReload() {
  if (isDirty.value) {
    try {
      await ElMessageBox.confirm("重新加载会丢弃当前未保存修改，确定继续吗？", "重新加载", {
        confirmButtonText: "重新加载",
        cancelButtonText: "取消",
        type: "warning",
      });
    } catch {
      return;
    }
  }
  await loadCurrentTemplate(Math.max(selectedSectionIndex.value, 0));
}

async function handleRefreshCache() {
  refreshingCache.value = true;
  try {
    await refreshCurrentTemplateCache();
    proxy.$message.success("首页装修缓存刷新成功");
    await loadCurrentTemplate(Math.max(selectedSectionIndex.value, 0));
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
            localKey: nextContentItemKey(),
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
            localKey: nextContentItemKey(),
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
            localKey: nextContentItemKey(),
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

function clonePaletteSection(item) {
  return normalizeSection({ itemType: item.value, content: {} });
}

function addSection(type) {
  const section = normalizeSection({ itemType: type, content: {} });
  templateForm.itemList.push(section);
  syncItemSort();
  selectAndReveal(section);
}

async function removeSection(index) {
  if (index < 0 || index >= templateForm.itemList.length) {
    return;
  }
  const section = templateForm.itemList[index];
  try {
    await ElMessageBox.confirm(
      `确定删除组件“${section.itemName || getDefaultItemName(section.itemType)}”吗？`,
      "删除组件",
      {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "warning",
      }
    );
  } catch {
    return;
  }
  templateForm.itemList.splice(index, 1);
  syncItemSort();
  const nextSection = templateForm.itemList[Math.min(index, templateForm.itemList.length - 1)];
  selectedSectionKey.value = nextSection?.localKey || "";
}

function copySection(section, index) {
  const copy = normalizeSection({
    itemType: section.itemType,
    itemName: `${section.itemName || getDefaultItemName(section.itemType)} 副本`.slice(0, 100),
    itemStatus: section.itemStatus,
    content: buildSectionContent(section),
  });
  templateForm.itemList.splice(index + 1, 0, copy);
  syncItemSort();
  selectAndReveal(copy);
}

function toggleSectionStatus(section) {
  section.itemStatus = section.itemStatus === "1" ? "0" : "1";
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
  selectAndReveal(currentItem, false);
}

function handleSectionDragChange(event) {
  syncItemSort();
  const changedSection = event?.added?.element || event?.moved?.element;
  if (changedSection) {
    selectAndReveal(changedSection, false);
  }
}

function selectSection(section) {
  selectedSectionKey.value = section.localKey;
}

function selectAndReveal(section, shouldScroll = true) {
  selectedSectionKey.value = section.localKey;
  if (!shouldScroll) {
    return;
  }
  nextTick(() => {
    const target = document.querySelector(`[data-section-key="${section.localKey}"]`);
    target?.scrollIntoView({ behavior: "smooth", block: "center" });
  });
}

function focusInvalidSection(section) {
  selectedSectionKey.value = section.localKey;
  nextTick(() => {
    propertyPanelRef.value?.scrollIntoView({ behavior: "smooth", block: "start" });
    const target = document.querySelector(`[data-section-key="${section.localKey}"]`);
    target?.scrollIntoView({ behavior: "smooth", block: "center" });
  });
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
    if (["banner", "image", "nav"].includes(section.itemType)) {
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
  if (goodsId && !goodsIds.some((item) => String(item) === String(goodsId))) {
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
  const optionMap = new Map(goodsOptions.value.map((item) => [String(item.id), item]));
  ensureArray(list).forEach((item) => {
    const normalizedItem = normalizeGoodsOption(item);
    if (normalizedItem.id) {
      optionMap.set(String(normalizedItem.id), normalizedItem);
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
  if (currentValue && !options.find((item) => String(item.id) === String(currentValue))) {
    options.unshift({ id: currentValue, name: `当前商品 ${currentValue}`, imageUrl: "", price: 0 });
  }
  return options;
}

function getGoodsLinkOptionsList(currentValues) {
  const options = [...goodsOptions.value];
  ensureArray(currentValues).forEach((goodsId) => {
    if (goodsId && !options.find((item) => String(item.id) === String(goodsId))) {
      options.unshift({ id: goodsId, name: `当前商品 ${goodsId}`, imageUrl: "", price: 0 });
    }
  });
  return options;
}

function getSelectedGoods(goodsId) {
  return (
    goodsOptions.value.find((item) => String(item.id) === String(goodsId)) || {
      id: goodsId,
      name: `当前商品 ${goodsId}`,
      imageUrl: "",
      price: 0,
    }
  );
}

function getGoodsItemKey(goodsId) {
  return String(goodsId);
}

function removeManualGoods(section, index) {
  section.content.goodsIds.splice(index, 1);
}

function getPageLinkOptions(linkType, currentValue) {
  const baseOptions = linkType === "tab" ? tabPageOptions : normalPageOptions;
  const options = [...baseOptions];
  if (currentValue && !options.find((item) => item.value === currentValue)) {
    options.unshift({ label: `当前路径 ${currentValue}`, value: currentValue });
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
    templateSettingsOpen.value = true;
    proxy.$message.error("模板名称不能为空");
    return;
  }
  const validation = validateTemplateForm();
  if (validation) {
    focusInvalidSection(validation.section);
    proxy.$message.error(validation.message);
    return;
  }

  const preferredIndex = Math.max(selectedSectionIndex.value, 0);
  saving.value = true;
  try {
    await saveCurrentTemplate(buildSavePayload());
    proxy.$message.success("首页模板保存成功");
    await loadCurrentTemplate(preferredIndex);
  } finally {
    saving.value = false;
  }
}

function buildSavePayload() {
  return {
    id: templateForm.id,
    templateName: templateForm.templateName ? templateForm.templateName.trim() : "",
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
  };
}

function serializeCurrentForm() {
  return JSON.stringify(buildSavePayload());
}

function validateTemplateForm() {
  for (let index = 0; index < templateForm.itemList.length; index += 1) {
    const section = templateForm.itemList[index];
    const message = validateSection(section, index);
    if (message) {
      return { message, section };
    }
  }
  return null;
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
      const linkMessage = validateLinkSetting(item.linkType, item.linkValue, `${sectionLabel}第 ${itemIndex + 1} 项`);
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
      const linkMessage = validateLinkSetting(item.linkType, item.linkValue, `${sectionLabel}第 ${itemIndex + 1} 项`);
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
      if (!items[itemIndex].text || !items[itemIndex].text.trim()) {
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
  return validateLinkSetting(section.content.moreLinkType, section.content.moreLinkValue, `${sectionLabel}更多链接`);
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
      items: ensureArray(section.content.items).map((item) => ({ tag: item.tag || "公告", text: item.text || "" })),
    };
  }
  if (section.itemType === "service") {
    return {
      title: section.content.title || "首页咨询入口",
      desc: section.content.desc || "",
      tags: ensureArray(section.content.tags).map((item) => String(item || "").trim()).filter(Boolean),
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
        const goodsInfo = goodsOptions.value.find((item) => String(item.id) === String(goodsId));
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
      imageUrl: goodsInfo.imageUrl ? resolveMallImageUrl(goodsInfo.imageUrl) : "",
      priceText: formatPrice(goodsInfo.price),
    };
  }
  return { name: goodsId ? `商品 ${goodsId}` : "商品示意", imageUrl: "", priceText: formatPrice(0) };
}

function buildPlaceholderGoods(size, sourceType) {
  const count = Math.min(size, 6);
  const prefixMap = { hot: "热销商品", new: "新品推荐", manual: "商品示意" };
  const prefix = prefixMap[sourceType] || "商品示意";
  return Array.from({ length: count }).map((_, index) => ({
    name: `${prefix} ${index + 1}`,
    imageUrl: "",
    priceText: formatPrice(99 + index),
  }));
}

function normalizeLinkValue(linkType, linkValue) {
  return linkType === "none" ? "" : linkValue || "";
}

function createMediaItem() {
  return { localKey: nextContentItemKey(), imageUrl: "", linkType: "none", linkValue: "" };
}

function createNavItem() {
  return { localKey: nextContentItemKey(), title: "", imageUrl: "", linkType: "none", linkValue: "" };
}

function createNoticeItem() {
  return { localKey: nextContentItemKey(), tag: "公告", text: "" };
}

function normalizeTagInput(tagText) {
  return String(tagText || "").split(/[,，\n]/).map((item) => item.trim()).filter(Boolean).slice(0, 6);
}

function ensureArray(list) {
  return Array.isArray(list) ? list : [];
}

function normalizeLinkType(linkType) {
  return ["goods", "page", "tab"].includes(linkType) ? linkType : "none";
}

function getDefaultItemName(itemType) {
  return itemTypeOptions.find((item) => item.value === itemType)?.label || "首页组件";
}

function resolvePreviewUrl(imageUrl) {
  return resolveMallImageUrl(imageUrl);
}

function formatPrice(value) {
  const numberValue = Number(value);
  return `¥${(Number.isFinite(numberValue) ? numberValue : 0).toFixed(2)}`;
}

function getFirstValidNumber(valueList) {
  const matchedValue = valueList.find((item) => item !== null && item !== undefined && item !== "");
  return matchedValue === undefined ? 0 : Number(matchedValue);
}

function nextSectionKey() {
  sectionKeySeed += 1;
  return `section_${Date.now()}_${sectionKeySeed}`;
}

function nextContentItemKey() {
  contentItemKeySeed += 1;
  return `content_${Date.now()}_${contentItemKeySeed}`;
}

function syncItemSort() {
  templateForm.itemList.forEach((item, index) => {
    item.itemSort = index + 1;
  });
}
</script>

<style lang="scss" scoped>
.home-designer-page {
  --designer-primary: #2967ff;
  --designer-border: #e4eaf2;
  --designer-text: #17253d;
  min-width: 0;
}

.designer-top-card {
  margin-bottom: 16px;
  border: 0;
  border-radius: 18px;
  box-shadow: 0 8px 28px rgba(15, 23, 42, 0.06);
}

.designer-topbar,
.designer-title-row,
.designer-actions,
.panel-heading,
.canvas-heading,
.property-quick-actions,
.property-item-head,
.manual-sort-title {
  display: flex;
  align-items: center;
}

.designer-topbar,
.panel-heading,
.canvas-heading,
.manual-sort-title {
  justify-content: space-between;
}

.designer-topbar {
  gap: 20px;
}

.designer-title-row {
  gap: 10px;
}

.designer-title-row h2 {
  margin: 0;
  color: var(--designer-text);
  font-size: 21px;
  line-height: 1.35;
}

.designer-topbar p {
  margin: 7px 0 0;
  color: #64748b;
  font-size: 13px;
}

.designer-actions {
  flex-shrink: 0;
  flex-wrap: wrap;
  justify-content: flex-end;
  gap: 8px;
}

.designer-actions :deep(.el-button + .el-button) {
  margin-left: 0;
}

.template-settings {
  margin-top: 18px;
  padding-top: 16px;
  border-top: 1px solid #edf1f5;
}

.template-settings :deep(.el-form-item) {
  margin-bottom: 0;
}

.designer-workspace {
  display: grid;
  grid-template-columns: minmax(190px, 220px) minmax(390px, 1fr) minmax(360px, 430px);
  align-items: start;
  gap: 16px;
}

.designer-panel,
.designer-canvas-panel {
  min-width: 0;
  border: 1px solid var(--designer-border);
  border-radius: 18px;
  background: #fff;
  box-shadow: 0 8px 28px rgba(15, 23, 42, 0.05);
}

.component-library,
.property-panel {
  position: sticky;
  top: 16px;
}

.component-library {
  padding: 16px;
}

.property-panel {
  max-height: calc(100vh - 32px);
  overflow: hidden;
}

.panel-heading,
.canvas-heading {
  gap: 12px;
  padding-bottom: 14px;
  border-bottom: 1px solid #edf1f5;
}

.canvas-heading {
  padding: 16px 18px;
}

.property-heading {
  padding: 16px 18px 14px;
}

.panel-title {
  color: var(--designer-text);
  font-size: 16px;
  font-weight: 700;
}

.panel-desc {
  margin-top: 5px;
  color: #8491a5;
  font-size: 12px;
  line-height: 1.5;
}

.palette-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 14px;
}

.palette-item {
  display: grid;
  grid-template-columns: 42px minmax(0, 1fr) 24px;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 11px 10px;
  border: 1px solid #e5ebf3;
  border-radius: 13px;
  background: #fff;
  color: inherit;
  text-align: left;
  cursor: grab;
  touch-action: none;
  transition:
    transform 0.18s ease,
    border-color 0.18s ease,
    box-shadow 0.18s ease;
}

.palette-item:hover:not(:disabled) {
  transform: translateY(-1px);
  border-color: #9ec5ff;
  box-shadow: 0 9px 20px rgba(41, 103, 255, 0.09);
}

.palette-item:active:not(:disabled) {
  cursor: grabbing;
}

.palette-item:disabled {
  cursor: not-allowed;
  opacity: 0.55;
}

.palette-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 42px;
  height: 42px;
  border-radius: 12px;
  background: #f4f7ff;
  color: var(--palette-color);
  font-size: 21px;
  font-weight: 800;
}

.palette-copy {
  display: flex;
  min-width: 0;
  flex-direction: column;
  gap: 4px;
}

.palette-copy strong {
  color: #26364d;
  font-size: 13px;
}

.palette-copy small {
  overflow: hidden;
  color: #8b98aa;
  font-size: 11px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.palette-add {
  color: #9aa7b8;
  font-size: 18px;
  text-align: center;
}

.palette-help {
  display: flex;
  flex-direction: column;
  gap: 5px;
  margin-top: 16px;
  padding: 12px;
  border-radius: 12px;
  background: #f6f9ff;
  color: #78879a;
  font-size: 11px;
  line-height: 1.55;
}

.palette-help strong {
  color: #49627f;
  font-size: 12px;
}

.designer-canvas-panel {
  padding-bottom: 22px;
  background: linear-gradient(180deg, #fbfdff 0%, #f5f8fd 100%);
}

.canvas-heading-actions {
  flex-shrink: 0;
}

.phone-shell {
  width: 100%;
  max-width: 390px;
  margin: 0 auto;
  padding: 12px;
  border-radius: 30px;
  background: linear-gradient(180deg, #263247 0%, #111927 100%);
  box-shadow: 0 18px 46px rgba(17, 25, 39, 0.22);
}

.canvas-phone {
  margin-top: 20px;
}

.phone-shell.is-dialog {
  max-width: 380px;
}

.phone-status {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
  padding: 0 8px;
  color: #fff;
  font-size: 12px;
  letter-spacing: 0.5px;
}

.phone-content {
  min-height: 570px;
  padding: 12px;
  border-radius: 23px;
  background: linear-gradient(180deg, #f7fbff 0%, #eef4fb 100%);
}

.canvas-phone-content {
  min-height: 650px;
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

.canvas-section-list {
  min-height: 520px;
}

.canvas-section-frame {
  position: relative;
  margin-bottom: 12px;
  padding: 3px;
  border: 2px solid transparent;
  border-radius: 21px;
  outline: none;
  cursor: pointer;
  transition:
    border-color 0.18s ease,
    box-shadow 0.18s ease,
    transform 0.18s ease;
}

.canvas-section-frame:hover,
.canvas-section-frame:focus-visible {
  border-color: #a8caff;
}

.canvas-section-frame.is-selected {
  border-color: var(--designer-primary);
  box-shadow: 0 0 0 4px rgba(41, 103, 255, 0.1);
}

.canvas-section-frame.is-disabled {
  background: repeating-linear-gradient(-45deg, transparent, transparent 6px, rgba(148, 163, 184, 0.08) 6px, rgba(148, 163, 184, 0.08) 12px);
}

.canvas-section-frame :deep(.preview-section) {
  margin-bottom: 0;
}

.canvas-section-label {
  position: absolute;
  top: -11px;
  left: 12px;
  z-index: 5;
  max-width: calc(100% - 145px);
  overflow: hidden;
  padding: 3px 9px;
  border-radius: 999px;
  background: #fff;
  color: #49627f;
  font-size: 10px;
  font-weight: 700;
  text-overflow: ellipsis;
  white-space: nowrap;
  box-shadow: 0 4px 12px rgba(15, 23, 42, 0.1);
}

.canvas-section-label span {
  margin-right: 4px;
  color: var(--designer-primary);
}

.canvas-section-tools {
  position: absolute;
  top: -14px;
  right: 9px;
  z-index: 6;
  display: flex;
  overflow: hidden;
  border: 1px solid #dce6f4;
  border-radius: 9px;
  background: #fff;
  box-shadow: 0 6px 16px rgba(15, 23, 42, 0.12);
  opacity: 0;
  transform: translateY(3px);
  transition:
    opacity 0.18s ease,
    transform 0.18s ease;
}

.canvas-section-frame:hover .canvas-section-tools,
.canvas-section-frame.is-selected .canvas-section-tools,
.canvas-section-frame:focus-within .canvas-section-tools {
  opacity: 1;
  transform: translateY(0);
}

.canvas-tool {
  width: 30px;
  height: 28px;
  padding: 0;
  border: 0;
  border-right: 1px solid #edf1f5;
  background: #fff;
  color: #52647a;
  font-size: 14px;
  cursor: pointer;
}

.canvas-tool:last-child {
  border-right: 0;
}

.canvas-tool:hover:not(:disabled) {
  background: #edf4ff;
  color: var(--designer-primary);
}

.canvas-tool.is-danger:hover:not(:disabled) {
  background: #fff1f2;
  color: #dc2626;
}

.canvas-tool:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.canvas-drag-handle {
  cursor: grab;
  touch-action: none;
}

.canvas-drag-handle:active {
  cursor: grabbing;
}

.canvas-drag-ghost {
  min-height: 92px;
  border: 2px dashed var(--designer-primary);
  background: #eaf2ff;
  opacity: 0.42;
}

.canvas-drag-chosen {
  box-shadow: 0 16px 38px rgba(41, 103, 255, 0.18);
}

.canvas-drag-active {
  transform: rotate(0.7deg);
}

.canvas-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 490px;
  flex-direction: column;
  gap: 8px;
  border: 2px dashed #b9cae3;
  border-radius: 18px;
  color: #7b8ba1;
  text-align: center;
}

.canvas-empty-icon,
.property-empty-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 16px;
  background: #eaf2ff;
  color: var(--designer-primary);
  font-size: 24px;
}

.canvas-empty span,
.property-empty span {
  font-size: 12px;
}

.property-quick-actions {
  gap: 8px;
  padding: 12px 18px;
  border-bottom: 1px solid #edf1f5;
  background: #fbfdff;
}

.property-quick-actions :deep(.el-button + .el-button) {
  margin-left: 0;
}

.property-body {
  max-height: calc(100vh - 174px);
  overflow-y: auto;
  padding: 16px 18px 24px;
  scrollbar-color: #cbd5e1 transparent;
  scrollbar-width: thin;
}

.property-form :deep(.el-form-item) {
  margin-bottom: 14px;
}

.property-form :deep(.el-form-item__label) {
  margin-bottom: 5px;
  color: #4b5e76;
  font-size: 12px;
  font-weight: 650;
  line-height: 1.4;
}

.property-form :deep(.el-select),
.property-form :deep(.el-input-number) {
  width: 100%;
}

.property-section-title {
  margin: 6px 0 14px;
  padding-left: 9px;
  border-left: 3px solid var(--designer-primary);
  color: #26364d;
  font-size: 14px;
  font-weight: 750;
}

.property-section-title:not(:first-child) {
  margin-top: 22px;
}

.property-section-title.with-action {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.property-item-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.property-item-card {
  padding: 12px;
  border: 1px solid #e5ebf3;
  border-radius: 14px;
  background: #f8fafc;
}

.property-item-card :deep(.el-upload--picture-card),
.property-item-card :deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 92px;
  height: 92px;
}

.property-item-head {
  justify-content: flex-start;
  gap: 8px;
  margin-bottom: 10px;
}

.property-item-head strong {
  flex: 1;
  color: #4b5e76;
  font-size: 12px;
}

.item-drag-handle,
.goods-drag-handle {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  padding: 0;
  border: 1px solid #d9e3f0;
  border-radius: 8px;
  background: #fff;
  color: #6b7f99;
  cursor: grab;
  touch-action: none;
}

.item-drag-ghost {
  border-color: var(--designer-primary);
  background: #eaf2ff;
  opacity: 0.45;
}

.item-drag-chosen {
  box-shadow: 0 10px 24px rgba(41, 103, 255, 0.13);
}

.manual-sort-title {
  gap: 8px;
  margin: 5px 0 10px;
  color: #43566f;
  font-size: 12px;
  font-weight: 700;
}

.manual-sort-title span {
  color: #8b98aa;
  font-weight: 400;
}

.manual-goods-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.manual-goods-item {
  display: grid;
  grid-template-columns: 30px 20px 42px minmax(0, 1fr) auto;
  align-items: center;
  gap: 8px;
  padding: 8px;
  border: 1px solid #e5ebf3;
  border-radius: 12px;
  background: #fff;
}

.manual-goods-index {
  color: #94a3b8;
  font-size: 11px;
  font-weight: 700;
  text-align: center;
}

.manual-goods-thumb {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 42px;
  height: 42px;
  overflow: hidden;
  border-radius: 9px;
  background: #edf2f7;
  color: #94a3b8;
  font-size: 10px;
}

.manual-goods-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.manual-goods-info {
  display: flex;
  min-width: 0;
  flex-direction: column;
  gap: 3px;
}

.manual-goods-info strong {
  overflow: hidden;
  color: #334155;
  font-size: 12px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.manual-goods-info small {
  color: #94a3b8;
  font-size: 10px;
}

.property-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 440px;
  padding: 30px;
  flex-direction: column;
  gap: 10px;
  color: #8190a4;
  text-align: center;
}

.property-empty strong {
  color: #43566f;
}

.dialog-preview-wrap {
  display: flex;
  justify-content: center;
}

.preview-empty {
  padding: 40px 0;
  color: #94a3b8;
  font-size: 13px;
  text-align: center;
}

@media (max-width: 1500px) {
  .designer-workspace {
    grid-template-columns: 190px minmax(370px, 1fr) 370px;
  }

  .component-library {
    padding: 13px;
  }

  .palette-item {
    grid-template-columns: 38px minmax(0, 1fr) 20px;
    padding: 9px 8px;
  }

  .palette-icon {
    width: 38px;
    height: 38px;
  }
}

@media (max-width: 1200px) {
  .designer-workspace {
    grid-template-columns: 200px minmax(390px, 1fr);
  }

  .property-panel {
    position: static;
    grid-column: 1 / -1;
    max-height: none;
  }

  .property-body {
    max-height: none;
  }
}

@media (max-width: 860px) {
  .designer-topbar {
    align-items: flex-start;
    flex-direction: column;
  }

  .designer-actions {
    justify-content: flex-start;
  }

  .designer-workspace {
    grid-template-columns: 1fr;
  }

  .component-library,
  .property-panel {
    position: static;
  }

  .palette-list {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .phone-shell {
    max-width: 410px;
  }
}

@media (max-width: 560px) {
  .palette-list {
    grid-template-columns: 1fr;
  }

  .designer-actions {
    width: 100%;
  }

  .designer-actions :deep(.el-button) {
    flex: 1;
  }

  .manual-goods-item {
    grid-template-columns: 30px 20px 38px minmax(0, 1fr);
  }

  .manual-goods-item > :deep(.el-button) {
    grid-column: 4;
    justify-self: start;
  }
}
</style>

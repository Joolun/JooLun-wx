<template>
  <div class="preview-section" :class="{ 'is-disabled': section.disabled }">
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
          <img v-if="item.imageUrl" :src="item.imageUrl" class="preview-banner-image" alt="" />
          <div v-else class="preview-media-empty preview-banner-empty">请上传轮播图</div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <div
      v-else-if="section.itemType === 'nav'"
      class="preview-nav-grid"
      :style="previewNavStyle"
    >
      <div
        v-for="(item, itemIndex) in section.items"
        :key="`${section.localKey}-nav-${itemIndex}`"
        class="preview-nav-item"
      >
        <div class="preview-nav-icon">
          <img v-if="item.imageUrl" :src="item.imageUrl" class="preview-nav-image" alt="" />
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
        <img v-if="item.imageUrl" :src="item.imageUrl" class="preview-media-image" alt="" />
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
            <img v-if="goods.imageUrl" :src="goods.imageUrl" class="preview-goods-image" alt="" />
            <div v-else class="preview-goods-image-empty">商品图</div>
          </div>
          <div class="preview-goods-name">{{ goods.name }}</div>
          <div class="preview-goods-price">{{ goods.priceText }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  section: {
    type: Object,
    required: true,
  },
});

const previewNavStyle = computed(() => {
  const itemCount = Array.isArray(props.section.items) ? props.section.items.length : 0;
  const columnCount = Math.min(Math.max(itemCount || 4, 4), 5);
  return {
    gridTemplateColumns: `repeat(${columnCount}, minmax(0, 1fr))`,
  };
});
</script>

<style lang="scss" scoped>
.preview-section {
  position: relative;
  margin-bottom: 12px;
  padding: 12px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 8px 24px rgba(148, 163, 184, 0.12);
  transition: opacity 0.2s ease;
}

.preview-section.is-disabled {
  opacity: 0.45;
}

.preview-section-status--float {
  position: absolute;
  top: 10px;
  right: 12px;
  z-index: 2;
  padding: 2px 8px;
  border-radius: 999px;
  background: rgba(255, 247, 237, 0.92);
  color: #b45309;
  font-size: 12px;
  font-weight: 600;
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

.preview-media-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  color: #94a3b8;
  font-size: 13px;
  background: linear-gradient(135deg, #e2e8f0 0%, #f8fafc 100%);
}

.preview-banner-empty {
  height: 100%;
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
  line-height: 1.4;
  text-align: center;
  word-break: break-all;
}

.preview-notice-carousel {
  margin-top: 2px;
  padding: 4px 0;
}

:deep(.preview-notice-carousel .el-carousel__container) {
  border-radius: 12px;
  background: #fff;
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
  background: #fff;
  box-shadow: 0 12px 32px rgba(37, 99, 235, 0.08);
}

.preview-service-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
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
  align-items: flex-start;
  justify-content: space-between;
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
</style>

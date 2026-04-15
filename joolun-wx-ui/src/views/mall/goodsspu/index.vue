<!--
  Copyright (C) 2026
  All rights reserved, Designed By www.joolun.com
  注意：
  本软件为www.joolun.com开发研制，项目使用请保留此说明
-->

<template>
  <div>
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
      @row-save="handleSave"
      @row-del="handleDel"
      @sort-change="sortChange"
      @search-change="searchChange"
      @selection-change="selectionChange"
    >
      <template #menu-left>
        <el-button
          v-if="checkPermi(['mall:goodsspu:edit'])"
          type="success"
          @click="batchShelf('1')"
          icon="el-icon-document"
        >
          批量上架
        </el-button>
        <el-button
          v-if="checkPermi(['mall:goodsspu:edit'])"
          type="warning"
          @click="batchShelf('0')"
          icon="el-icon-document"
        >
          批量下架
        </el-button>
      </template>

      <template #salesPrice="scope">
        <div style="color: red">￥{{ scope.row.salesPrice }}</div>
      </template>

      <template #shelf="scope">
        <el-switch
          active-value="1"
          inactive-value="0"
          v-model="scope.row.shelf"
          style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
          inline-prompt
          active-icon="Check"
          inactive-icon="Close"
          @change="changeShelf(scope.row)"
        />
      </template>

      <template #description-form>
        <BaseEditor v-model="form.description" :minHeight="300" />
      </template>

      <template #picUrls-form="scope">
        <ImageUpload
          :limit="5"
          :disabled="scope.type == 'view'"
          returnType="array"
          v-model="form.picUrls"
        />
      </template>

      <template #picUrls="scope">
        <el-image
          style="width: 100px; height: 100px"
          :src="firstPic(scope.row.picUrls)"
        />
      </template>

      <template #skuEditor-form="scope">
        <div class="sku-editor">
          <div class="sku-editor__header">
            <div class="sku-editor__title">规格模式</div>
            <el-radio-group
              v-model="form.specType"
              :disabled="isViewMode(scope.type)"
              @change="handleSpecTypeChange"
            >
              <el-radio-button label="0">统一规格</el-radio-button>
              <el-radio-button label="1">多规格</el-radio-button>
            </el-radio-group>
          </div>

          <div v-if="form.specType === '1'" class="sku-editor__section">
            <div class="sku-editor__section-title">规格项</div>
            <div
              v-for="(spec, specIndex) in form.spuSpec"
              :key="spec.localKey"
              class="sku-editor__spec-row"
            >
              <div class="sku-editor__spec-name">
                <el-input
                  v-model="spec.name"
                  placeholder="规格名，例如 颜色"
                  :disabled="isViewMode(scope.type)"
                  @blur="refreshSkuRows"
                />
                <div
                  v-if="isDuplicateSpecName(spec.name, specIndex)"
                  class="sku-editor__warning"
                >
                  规格名重复，请调整后再保存
                </div>
              </div>
              <div class="sku-editor__spec-values">
                <div class="sku-editor__tag-list">
                  <el-tag
                    v-for="valueName in getSpecValueList(spec)"
                    :key="valueName"
                    closable
                    class="sku-editor__tag"
                    :disable-transitions="true"
                    :closable="!isViewMode(scope.type)"
                    @close="removeSpecValue(specIndex, valueName)"
                  >
                    {{ valueName }}
                  </el-tag>
                  <span
                    v-if="getSpecValueList(spec).length <= 0"
                    class="sku-editor__tag-placeholder"
                  >
                    还没有规格值，请在下方输入后回车添加
                  </span>
                </div>
                <div class="sku-editor__tag-toolbar">
                  <span class="sku-editor__tag-count">
                    已添加 {{ getSpecValueList(spec).length }} 项
                  </span>
                  <el-button
                    v-if="getSpecValueList(spec).length > 0"
                    type="danger"
                    link
                    :disabled="isViewMode(scope.type)"
                    @click="clearSpecValues(specIndex)"
                  >
                    清空
                  </el-button>
                </div>
                <div class="sku-editor__value-input-wrap">
                  <el-input
                    v-model="spec.draftValue"
                    placeholder="输入规格值后回车，可批量粘贴逗号或换行分隔内容"
                    :disabled="isViewMode(scope.type)"
                    @keyup.enter="appendSpecValues(specIndex)"
                    @keydown="handleSpecDraftKeydown($event, specIndex)"
                    @blur="appendSpecValues(specIndex)"
                  />
                  <el-button
                    type="primary"
                    plain
                    :disabled="isViewMode(scope.type)"
                    @click="appendSpecValues(specIndex)"
                  >
                    添加
                  </el-button>
                </div>
              </div>
              <el-button
                type="danger"
                plain
                :disabled="isViewMode(scope.type) || form.spuSpec.length <= 1"
                @click="removeSpecRow(specIndex)"
              >
                删除
              </el-button>
            </div>

            <div class="sku-editor__actions">
              <el-button
                type="primary"
                plain
                :disabled="isViewMode(scope.type)"
                @click="addSpecRow"
              >
                新增规格
              </el-button>
              <el-button
                type="success"
                plain
                :disabled="isViewMode(scope.type)"
                @click="refreshSkuRows"
              >
                生成 SKU 组合
              </el-button>
            </div>
          </div>

          <div class="sku-editor__section">
            <div class="sku-editor__section-title">SKU 列表</div>
            <div class="sku-editor__tip">
              {{ form.specType === '1' ? '规格项变化后请重新生成 SKU 组合。' : '统一规格只保留一条 SKU。' }}
            </div>
            <el-table :data="form.skus" border class="sku-editor__table">
              <el-table-column
                v-for="spec in validSpecList"
                :key="spec.localKey"
                :label="spec.name || '规格'"
                min-width="120"
              >
                <template #default="{ row }">
                  <span>{{ row.specDisplay[spec.name] || '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="SKU编码" min-width="150">
                <template #default="{ row }">
                  <el-input
                    v-model="row.skuCode"
                    :disabled="isViewMode(scope.type)"
                    placeholder="SKU编码"
                  />
                </template>
              </el-table-column>
              <el-table-column label="图片" min-width="240">
                <template #default="{ row }">
                  <div class="sku-editor__pic-cell">
                    <ImageUpload
                      class="sku-editor__pic-upload"
                      :limit="1"
                      returnType="string"
                      v-model="row.picUrl"
                      :disabled="isViewMode(scope.type)"
                    />
                    <el-button
                      v-if="!isViewMode(scope.type)"
                      type="primary"
                      link
                      @click="useMainPic(row)"
                    >
                      使用主图
                    </el-button>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="销售价" min-width="120">
                <template #default="{ row }">
                  <el-input-number
                    v-model="row.salesPrice"
                    :disabled="isViewMode(scope.type)"
                    :min="0"
                    :precision="2"
                    controls-position="right"
                  />
                </template>
              </el-table-column>
              <el-table-column label="市场价" min-width="120">
                <template #default="{ row }">
                  <el-input-number
                    v-model="row.marketPrice"
                    :disabled="isViewMode(scope.type)"
                    :min="0"
                    :precision="2"
                    controls-position="right"
                  />
                </template>
              </el-table-column>
              <el-table-column label="成本价" min-width="120">
                <template #default="{ row }">
                  <el-input-number
                    v-model="row.costPrice"
                    :disabled="isViewMode(scope.type)"
                    :min="0"
                    :precision="2"
                    controls-position="right"
                  />
                </template>
              </el-table-column>
              <el-table-column label="库存" min-width="110">
                <template #default="{ row }">
                  <el-input-number
                    v-model="row.stock"
                    :disabled="isViewMode(scope.type)"
                    :min="0"
                    :precision="0"
                    controls-position="right"
                  />
                </template>
              </el-table-column>
              <el-table-column label="启用" width="90">
                <template #default="{ row }">
                  <el-switch
                    v-model="row.enable"
                    active-value="1"
                    inactive-value="0"
                    :disabled="isViewMode(scope.type)"
                  />
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </template>
    </avue-crud>
  </div>
</template>

<script setup name="GoodsSpu">
/**
 * 商品 SPU 管理页。
 *
 * 多规格说明：
 * 1. 表单内维护规格项、规格值和 SKU 组合。
 * 2. 保存时统一整理成 spuSpec 和 skus 提交给后端。
 * 3. 编辑时再把接口数据回填成可编辑结构。
 */
import { computed, getCurrentInstance, reactive, toRefs } from "vue";
import { checkPermi } from "@/utils/permission";
import {
  getPage,
  getObj,
  addObj,
  putObj,
  delObj,
  putObjShelf,
} from "@/api/mall/goodsspu";
import { tableOption } from "@/const/crud/mall/goodsspu";
import BaseEditor from "@/components/Editor/index.vue";
import {
  getMallFirstImageValue,
  getMallFirstImageUrl,
} from "@/utils/mall-image";

const { proxy } = getCurrentInstance();

const data = reactive({
  form: createDefaultForm(),
  tableData: [],
  page: {
    total: 0,
    currentPage: 1,
    pageSize: 20,
    ascs: [],
    descs: "create_time",
  },
  paramsSearch: {},
  tableLoading: false,
  selectionData: [],
});

const { form, page, tableData, tableLoading } = toRefs(data);

const permissionList = computed(() => ({
  addBtn: checkPermi(["mall:goodsspu:add"]),
  delBtn: checkPermi(["mall:goodsspu:del"]),
  editBtn: checkPermi(["mall:goodsspu:edit"]),
  viewBtn: checkPermi(["mall:goodsspu:get"]),
}));

// 仅返回有效规格项，供 SKU 表格列和组合生成复用。
const validSpecList = computed(() => getValidSpecs(data.form.spuSpec));

function createDefaultForm() {
  // 新增商品的默认表单。
  // 即使是单规格，也保留一条 SKU 记录，避免表单出现空数组。
  return {
    shelf: "0",
    specType: "0",
    picUrls: [],
    categoryId: [],
    saleNum: 0,
    spuSpec: [createSpecRow()],
    skus: [createSingleSku()],
  };
}

function createSpecRow(spec = {}) {
  // localKey 只用于前端渲染，避免新增未落库数据时 key 冲突。
  return {
    localKey: `${Date.now()}-${Math.random()}`,
    id: spec.id || "",
    name: spec.name || "",
    value: spec.value || "",
    draftValue: spec.draftValue || "",
    sort: spec.sort ?? 0,
    values: Array.isArray(spec.values) ? spec.values : [],
  };
}

function createSingleSku(payload = {}) {
  // SKU 工厂方法，确保新增、回显、重算后的字段结构一致。
  return {
    id: payload.id || "",
    skuCode: payload.skuCode || "",
    picUrl: payload.picUrl || "",
    salesPrice: payload.salesPrice ?? 0,
    marketPrice: payload.marketPrice ?? 0,
    costPrice: payload.costPrice ?? 0,
    stock: payload.stock ?? 0,
    enable: payload.enable || "1",
    specs: Array.isArray(payload.specs) ? payload.specs : [],
    specDisplay: payload.specDisplay || {},
  };
}

function selectionChange(list) {
  data.selectionData = list;
}

function cloneGoodsForm(formData) {
  return JSON.parse(JSON.stringify(formData || {}));
}

function batchShelf(shelf) {
  // 上下架仍然以 SPU 为单位，不区分到 SKU。
  if (data.selectionData.length <= 0) {
    proxy.$message.error("请选择商品");
    return;
  }
  const selectionIds = data.selectionData.map((item) => item.id).join(",");
  putObjShelfF(selectionIds, shelf);
}

function changeShelf(row) {
  if (row && row.id) putObjShelfF(row.id, row.shelf);
}

function putObjShelfF(ids, shelf) {
  putObjShelf({ ids, shelf })
    .then(() => getPageF(data.page))
    .catch(() => getPageF(data.page));
}

function beforeOpen(done, type) {
  // 新增直接初始化默认表单；编辑和查看则先转成前端编辑结构。
  if (type === "add") {
    data.form = createDefaultForm();
    done();
    return;
  }
  data.tableLoading = true;
  getObj(data.form.id)
    .then((response) => {
      data.form = formatGoodsForm(response.data);
      data.tableLoading = false;
      done();
    })
    .catch(() => {
      data.tableLoading = false;
      done();
    });
}

function searchChange(params, done) {
  params = proxy.filterForm(params);
  data.paramsSearch = params;
  data.page.currentPage = 1;
  getPageF(data.page, params);
  done();
}

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

function getPageF(pageInfo, params) {
  // 级联分类组件使用 categoryId 数组，接口仍然要求拆成一级、二级分类。
  data.tableLoading = true;
  if (data.paramsSearch.categoryId) {
    data.paramsSearch.categoryFirst = data.paramsSearch.categoryId[0];
    data.paramsSearch.categorySecond = data.paramsSearch.categoryId[1];
  }
  getPage(
    Object.assign(
      {
        current: pageInfo.currentPage,
        size: pageInfo.pageSize,
        descs: data.page.descs,
        ascs: data.page.ascs,
      },
      params,
      data.paramsSearch
    )
  )
    .then((response) => {
      const pageData = response.data.records ? response.data.records : [];
      pageData.forEach((item) => {
        const categoryId = [];
        if (item.categoryFirst) categoryId.push(item.categoryFirst);
        if (item.categorySecond) categoryId.push(item.categorySecond);
        item.categoryId = categoryId;
      });
      data.tableData = pageData;
      data.page.total = response.data.total;
      data.page.currentPage = pageInfo.currentPage;
      data.page.pageSize = pageInfo.pageSize;
      data.tableLoading = false;
    })
    .catch(() => {
      data.tableLoading = false;
    });
}

function handleDel(row) {
  proxy
    .$confirm("是否确认删除此数据", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => delObj(row.id))
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "删除成功",
        type: "success",
      });
      getPageF(data.page);
    })
    .catch(() => {});
}

function handleUpdate(row, index, done, loading) {
  const payload = buildPayload(cloneGoodsForm(data.form));
  if (!payload) {
    loading();
    return;
  }
  putObj(payload)
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "修改成功",
        type: "success",
      });
      done();
      getPageF(data.page);
    })
    .catch(() => loading());
}

function handleSave(row, done, loading) {
  const payload = buildPayload(cloneGoodsForm(data.form));
  if (!payload) {
    loading();
    return;
  }
  addObj(payload)
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "添加成功",
        type: "success",
      });
      done();
      getPageF(data.page);
    })
    .catch(() => loading());
}

function refreshChange() {
  getPageF(data.page);
}

function formatGoodsForm(goods) {
  // 把接口返回结构转换成前端编辑结构。
  // 例如：categoryFirst/categorySecond -> categoryId；sku.specs -> specDisplay。
  const result = Object.assign(createDefaultForm(), goods || {});
  result.categoryId = [];
  if (result.categoryFirst) result.categoryId.push(result.categoryFirst);
  if (result.categorySecond) result.categoryId.push(result.categorySecond);
  result.specType = result.specType || "0";
  result.spuSpec =
    Array.isArray(result.spuSpec) && result.spuSpec.length > 0
      ? result.spuSpec.map((item, index) =>
          createSpecRow({
            id: item.id,
            name: item.name,
            sort: item.sort ?? index,
            value: Array.isArray(item.values)
              ? item.values.map((valueItem) => valueItem.name).join(",")
              : item.value || "",
            values: item.values || [],
          })
        )
      : [createSpecRow()];
  result.skus =
    Array.isArray(result.skus) && result.skus.length > 0
      ? result.skus.map((sku) => normalizeSkuRow(sku))
      : [createSingleSku()];
  // 详情回显时需要尽量保留后端已保存的 SKU 行数据，
  // 否则像 SKU 图片这类独立字段会在重新按规格组装时被主图或空值覆盖。
  syncSkuRows(result, true);
  return result;
}

function normalizeSkuRow(sku = {}) {
  // 统一规格明细结构，避免表格展示和提交字段不一致。
  const specs = Array.isArray(sku.specs) ? sku.specs : [];
  const specDisplay = {};
  specs.forEach((item) => {
    specDisplay[item.specName] = item.specValueName;
  });
  return createSingleSku({
    ...sku,
    specs: specs.map((item) => ({
      id: item.id || "",
      specId: item.specId || "",
      specName: item.specName || "",
      specValueId: item.specValueId || "",
      specValueName: item.specValueName || "",
      sort: item.sort ?? 0,
    })),
    specDisplay,
  });
}

function isViewMode(type) {
  return type === "view";
}

function handleSpecTypeChange() {
  // 切换规格模式后立即重算 SKU，并尽量保留已录入字段。
  syncSkuRows(data.form, true);
}

function addSpecRow() {
  data.form.spuSpec.push(createSpecRow());
}

function removeSpecRow(index) {
  data.form.spuSpec.splice(index, 1);
  if (data.form.spuSpec.length === 0) {
    data.form.spuSpec.push(createSpecRow());
  }
  refreshSkuRows();
}

function refreshSkuRows() {
  syncSkuRows(data.form, true);
}

function getSpecValueList(spec) {
  // UI 层统一按标签数组渲染，底层仍旧兼容 value 字符串存储。
  return parseSpecValues(spec?.value);
}

function isDuplicateSpecName(specName, currentIndex) {
  const name = String(specName || "").trim();
  if (!name) {
    return false;
  }
  return data.form.spuSpec.some((item, index) => index !== currentIndex && String(item?.name || "").trim() === name);
}

function handleSpecDraftKeydown(event, specIndex) {
  // 录入时遇到逗号、中文逗号直接拆词，减少必须手动点“添加”的负担。
  if (event.isComposing) {
    return;
  }
  if (event.key === "," || event.key === "，") {
    event.preventDefault();
    appendSpecValues(specIndex);
  }
}

function appendSpecValues(specIndex) {
  // 支持一次输入一个值，也支持粘贴“红色,黑色”或按行粘贴后批量拆分。
  const spec = data.form.spuSpec[specIndex];
  if (!spec || !spec.draftValue) {
    return;
  }
  const currentValues = getSpecValueList(spec);
  const inputValues = parseSpecValues(spec.draftValue);
  if (inputValues.length <= 0) {
    spec.draftValue = "";
    return;
  }
  const duplicatedValues = inputValues.filter((item) => currentValues.includes(item));
  const nextValues = Array.from(new Set(currentValues.concat(inputValues)));
  spec.value = nextValues.join(",");
  spec.draftValue = "";
  if (duplicatedValues.length > 0) {
    proxy.$message.warning(`已忽略重复规格值：${duplicatedValues.join("、")}`);
  }
  refreshSkuRows();
}

function removeSpecValue(specIndex, valueName) {
  // 删除标签后立刻重算 SKU，避免无效组合残留。
  const spec = data.form.spuSpec[specIndex];
  if (!spec) {
    return;
  }
  spec.value = getSpecValueList(spec)
    .filter((item) => item !== valueName)
    .join(",");
  refreshSkuRows();
}

function clearSpecValues(specIndex) {
  const spec = data.form.spuSpec[specIndex];
  if (!spec) {
    return;
  }
  spec.value = "";
  spec.draftValue = "";
  refreshSkuRows();
}

function useMainPic(row) {
  // SKU 未单独上传图片时，允许直接复用商品主图。
  row.picUrl = firstPicValue(data.form.picUrls);
}

function syncSkuRows(targetForm, preserveFields) {
  // 规格与 SKU 的核心同步入口。
  // 单规格只保留一条 SKU；多规格则生成笛卡尔积并尽量复用历史填写值。
  if (targetForm.specType !== "1") {
    const firstSku = Array.isArray(targetForm.skus) && targetForm.skus.length > 0 ? normalizeSkuRow(targetForm.skus[0]) : createSingleSku();
    firstSku.specs = [];
    firstSku.specDisplay = {};
    if (!firstSku.picUrl) {
      firstSku.picUrl = firstPicValue(targetForm.picUrls);
    }
    targetForm.skus = [firstSku];
    return;
  }

  const specDefs = getValidSpecs(targetForm.spuSpec).map((spec, index) => ({
    ...spec,
    sort: index,
    valuesParsed: parseSpecValues(spec.value).map((valueName) => {
      const existingValue = Array.isArray(spec.values)
        ? spec.values.find((item) => item.name === valueName)
        : null;
      return {
        id: existingValue ? existingValue.id : "",
        name: valueName,
      };
    }),
  }));

  if (specDefs.length === 0 || specDefs.some((item) => item.valuesParsed.length === 0)) {
    targetForm.skus = [];
    return;
  }

  const existingMap = new Map();
  (targetForm.skus || []).forEach((sku) => {
    const normalized = normalizeSkuRow(sku);
    existingMap.set(buildSkuKey(normalized.specs), normalized);
  });

  const combinations = buildSpecCombinations(specDefs);
  targetForm.skus = combinations.map((combination) => {
    const specs = combination.map((item, index) => ({
      specId: item.specId,
      specName: item.specName,
      specValueId: item.specValueId,
      specValueName: item.specValueName,
      sort: index,
    }));
    const key = buildSkuKey(specs);
    const previous = existingMap.get(key);
    const nextRow = preserveFields && previous ? previous : createSingleSku();
    return createSingleSku({
      ...nextRow,
      picUrl: nextRow.picUrl || firstPicValue(targetForm.picUrls),
      specs,
      specDisplay: specs.reduce((result, item) => {
        result[item.specName] = item.specValueName;
        return result;
      }, {}),
    });
  });
}

function getValidSpecs(specs) {
  return (Array.isArray(specs) ? specs : []).filter(
    (item) => item && item.name && item.name.trim()
  );
}

function parseSpecValues(value) {
  // 兼容英文逗号、中文逗号和换行，并去重，避免生成重复 SKU。
  return Array.from(
    new Set(
      String(value || "")
        .split(/[,\n\uFF0C]/)
        .map((item) => item.trim())
        .filter(Boolean)
    )
  );
}

function buildSpecCombinations(specDefs) {
  // 根据规格项和值生成笛卡尔积，例如颜色 * 尺码。
  let combinations = [[]];
  specDefs.forEach((spec) => {
    const next = [];
    combinations.forEach((base) => {
      spec.valuesParsed.forEach((valueItem) => {
        next.push(
          base.concat({
            specId: spec.id || "",
            specName: spec.name,
            specValueId: valueItem.id || "",
            specValueName: valueItem.name,
          })
        );
      });
    });
    combinations = next;
  });
  return combinations;
}

function buildSkuKey(specs) {
  // 用“规格名:规格值”生成稳定键，便于复用已有 SKU 行。
  return (specs || [])
    .map((item) => `${item.specName}:${item.specValueName}`)
    .join("|");
}

function buildPayload(row) {
  // 保存前统一整理最终入参。
  row.categoryFirst = row.categoryId[0];
  row.categorySecond = row.categoryId[1] ? row.categoryId[1] : "";
  const validSpecRows = getValidSpecs(row.spuSpec);
  const specNameSet = new Set();
  for (const spec of validSpecRows) {
    const specName = spec.name.trim();
    if (specNameSet.has(specName)) {
      proxy.$message.error(`规格名“${specName}”重复，请调整后再保存`);
      return null;
    }
    specNameSet.add(specName);
  }
  syncSkuRows(row, true);

  if (!Array.isArray(row.skus) || row.skus.length === 0) {
    proxy.$message.error("请至少配置一条 SKU");
    return null;
  }

  for (const sku of row.skus) {
    if (sku.salesPrice === null || sku.salesPrice === undefined) {
      proxy.$message.error("SKU 销售价不能为空");
      return null;
    }
    if (sku.stock === null || sku.stock === undefined) {
      proxy.$message.error("SKU 库存不能为空");
      return null;
    }
  }

  const payload = {
    ...row,
    spuSpec: [],
    skus: [],
  };

  if (row.specType === "1") {
    const validSpecs = getValidSpecs(row.spuSpec).map((spec, index) => {
      const values = parseSpecValues(spec.value).map((valueName) => {
        const previous = Array.isArray(spec.values)
          ? spec.values.find((item) => item.name === valueName)
          : null;
        return {
          id: previous ? previous.id : "",
          name: valueName,
        };
      });
      return {
        id: spec.id || "",
        name: spec.name.trim(),
        value: values.map((item) => item.name).join(","),
        sort: index,
        values,
      };
    });
    if (validSpecs.length === 0) {
      proxy.$message.error("多规格模式至少需要配置一项规格");
      return null;
    }
    payload.spuSpec = validSpecs;
  }

  payload.skus = row.skus.map((sku) => ({
    id: sku.id || "",
    skuCode: sku.skuCode || "",
    picUrl: sku.picUrl || firstPicValue(row.picUrls),
    salesPrice: sku.salesPrice ?? 0,
    marketPrice: sku.marketPrice ?? 0,
    costPrice: sku.costPrice ?? 0,
    stock: sku.stock ?? 0,
    enable: sku.enable || "1",
    specs:
      row.specType === "1"
        ? (sku.specs || []).map((item, index) => ({
            specId: item.specId || "",
            specName: item.specName || "",
            specValueId: item.specValueId || "",
            specValueName: item.specValueName || "",
            sort: item.sort ?? index,
          }))
        : [],
  }));

  const firstSku = payload.skus[0];
  payload.salesPrice = firstSku.salesPrice;
  payload.marketPrice = firstSku.marketPrice;
  payload.costPrice = firstSku.costPrice;
  payload.stock = payload.skus.reduce((total, item) => total + Number(item.stock || 0), 0);

  return payload;
}

function firstPicValue(picUrls) {
  return getMallFirstImageValue(picUrls);
}

function firstPic(picUrls) {
  return getMallFirstImageUrl(picUrls);
}
</script>

<style scoped>
.sku-editor {
  border: 1px solid var(--el-border-color);
  border-radius: 8px;
  padding: 16px;
  background: #fafafa;
}

.sku-editor__header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.sku-editor__title,
.sku-editor__section-title {
  font-weight: 600;
  color: #303133;
}

.sku-editor__section {
  margin-top: 16px;
}

.sku-editor__spec-row {
  display: grid;
  grid-template-columns: 220px minmax(280px, 1fr) 100px;
  gap: 12px;
  margin-bottom: 12px;
}

.sku-editor__spec-values {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.sku-editor__tag-list {
  min-height: 72px;
  padding: 10px 12px;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  background: #fff;
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  gap: 8px;
}

.sku-editor__tag {
  margin: 0;
}

.sku-editor__tag-placeholder {
  color: #909399;
  font-size: 13px;
  line-height: 24px;
}

.sku-editor__tag-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 24px;
}

.sku-editor__tag-count {
  color: #909399;
  font-size: 12px;
}

.sku-editor__value-input-wrap {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 72px;
  gap: 8px;
}

.sku-editor__warning {
  color: var(--el-color-danger);
  font-size: 12px;
  line-height: 1.2;
}

.sku-editor__actions {
  display: flex;
  gap: 12px;
}

.sku-editor__tip {
  margin: 8px 0 12px;
  color: #909399;
  font-size: 13px;
}

.sku-editor__table {
  width: 100%;
}

.sku-editor__pic-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.sku-editor__pic-upload :deep(.el-upload--picture-card),
.sku-editor__pic-upload :deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 88px;
  height: 88px;
}

@media (max-width: 1200px) {
  .sku-editor__spec-row {
    grid-template-columns: 1fr;
  }
}
</style>

<template>
  <div v-if="objData.repType == 'image'">
    <div
      class="waterfall"
      v-loading="tableLoading">
      <div
        class="waterfall-item"
        v-for="item in tableData"
        :key="item.mediaId">
        <img
          class="material-img"
          :src="item.url" />
        <p class="item-name">{{ item.name }}</p>
        <el-row class="ope-row">
          <el-button
            size="small"
            type="success"
            @click="selectMaterial(item)"
            >选择<el-icon class="el-icon--right"><CircleCheck /></el-icon
          ></el-button>
        </el-row>
      </div>
    </div>
    <div
      v-if="tableData.length <= 0 && !tableLoading"
      class="el-table__empty-block">
      <span class="el-table__empty-text">暂无数据</span>
    </div>
    <span class="dialog-footer">
      <el-pagination
        @size-change="sizeChange"
        @current-change="currentChange"
        v-model:current-page="page.currentPage"
        :page-sizes="[10, 20]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total"
        class="pagination">
      </el-pagination>
    </span>
  </div>
  <div v-else-if="objData.repType == 'voice'">
    <avue-crud
      ref="crud"
      :page="page"
      :data="tableData"
      :table-loading="tableLoading"
      :option="tableOptionVoice"
      @on-load="getPageF"
      @size-change="sizeChange"
      @refresh-change="refreshChange">
      <template #menu="scope">
        <el-button
          link
          icon="CircleCheck"
          type="success"
          @click="selectMaterial(scope.row)"
          >选择</el-button
        >
      </template>
    </avue-crud>
  </div>
  <div v-else-if="objData.repType == 'video'">
    <avue-crud
      ref="crud"
      :page="page"
      :data="tableData"
      :table-loading="tableLoading"
      :option="tableOptionVideo"
      @on-load="getPageF"
      @size-change="sizeChange"
      @refresh-change="refreshChange">
      <template #menu="scope">
        <el-button
          link
          icon="CircleCheck"
          type="success"
          @click="selectMaterial(scope.row)"
          >选择</el-button
        >
      </template>
    </avue-crud>
  </div>
  <div v-else-if="objData.repType == 'news'">
    <div
      class="waterfall"
      v-loading="tableLoading">
      <template
        v-for="(item, index) in tableData"
        :key="index">
        <div
          v-if="item.content && item.content.articles"
          class="waterfall-item">
          <WxNews :objData="item.content.articles"></WxNews>

          <el-row class="ope-row">
            <el-button
              size="small"
              type="success"
              @click="selectMaterial(item)"
              >选择<el-icon class="el-icon--right"><CircleCheck /></el-icon
            ></el-button>
          </el-row>
        </div>
      </template>
    </div>
    <div
      v-if="tableData.length <= 0 && !tableLoading"
      class="el-table__empty-block">
      <span class="el-table__empty-text">暂无数据</span>
    </div>
    <span class="dialog-footer">
      <el-pagination
        style="margin-top: 20px"
        @size-change="sizeChange"
        v-model:current-page="page.currentPage"
        :page-sizes="[10, 20]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total"
        class="pagination">
      </el-pagination>
    </span>
  </div>
</template>

<script setup name="WxMaterialSelect">
import { getPage, getMaterialVideo } from "@/api/wxmp/wxmaterial";
import { tableOptionVoice } from "@/const/crud/wxmp/wxmaterial_voice";
import { tableOptionVideo } from "@/const/crud/wxmp/wxmaterial_video";
import WxNews from "@/components/wx-news/main.vue";
import { getPage as getPageNews } from "@/api/wxmp/wxfreepublish";
import { getPage as getPageNewsDraft } from "@/api/wxmp/wxdraft";

const { proxy } = getCurrentInstance();

const props = defineProps({
  objData: {
    type: Object,
  },
  newsType: {
    type: String,
    default: "1",
  },
});

const data = reactive({
  tableLoading: false,
  tableData: [],
  page: {
    total: 0, // 总页数
    currentPage: 1, // 当前页数
    pageSize: 20, // 每页显示多少条
    ascs: [], //升序字段
    descs: [], //降序字段
  },
});
const { tableData, tableLoading, page } = toRefs(data);

function onCreated() {
  getPageF(data.page);
}
onCreated();

onMounted(() => {});

function selectMaterial(item) {
  proxy.$emit("selectMaterial", item);
}

function getPageF(page, params) {
  data.tableLoading = true;
  if (props.objData.repType == "news") {
    if (props.newsType == "1") {
      getPageNews(
        Object.assign(
          {
            current: page.currentPage,
            size: page.pageSize,
            appId: /* Warn: Unknown source: appId */ proxy.appId,
          },
          params
        )
      ).then((response) => {
        let tableData = response.data.items;
        tableData.forEach((item) => {
          item.mediaId = item.articleId;
          item.content.articles = item.content.newsItem;
        });
        data.tableData = tableData;
        data.page.total = response.data.totalCount;
        data.page.currentPage = page.currentPage;
        data.page.pageSize = page.pageSize;
        data.tableLoading = false;
      });
    } else if (props.newsType == "2") {
      getPageNewsDraft(
        Object.assign(
          {
            current: page.currentPage,
            size: page.pageSize,
            appId: /* Warn: Unknown source: appId */ proxy.appId,
          },
          params
        )
      ).then((response) => {
        let tableData = response.data.items;
        tableData.forEach((item) => {
          item.mediaId = item.mediaId;
          item.content.articles = item.content.newsItem;
        });
        data.tableData = tableData;
        data.page.total = response.data.totalCount;
        data.page.currentPage = page.currentPage;
        data.page.pageSize = page.pageSize;
        data.tableLoading = false;
      });
    }
  } else {
    getPage(
      Object.assign(
        {
          current: page.currentPage,
          size: page.pageSize,
          appId: /* Warn: Unknown source: appId */ proxy.appId,
          type: props.objData.repType,
        },
        params
      )
    ).then((response) => {
      data.tableData = response.data.items;
      data.page.total = response.data.totalCount;
      data.page.currentPage = page.currentPage;
      data.page.pageSize = page.pageSize;
      data.tableLoading = false;
    });
  }
}

function sizeChange(val) {
  data.page.currentPage = 1;
  data.page.pageSize = val;
  getPageF(data.page);
}

function currentChange(val) {
  data.page.currentPage = val;
  getPageF(data.page);
}

function refreshChange(page) {
  getPageF(data.page);
}
</script>

<style lang="scss" scoped>
.waterfall {
  width: 100%;
  column-gap: 10px;
  column-count: 5;
  margin: 0 auto;
}
.waterfall-item {
  padding: 10px;
  margin-bottom: 10px;
  break-inside: avoid;
  border: 1px solid #eaeaea;
}
.material-img {
  width: 100%;
}
.ope-row {
  margin-top: 5px;
}
p {
  line-height: 30px;
}
@media (min-width: 992px) and (max-width: 1300px) {
  .waterfall {
    column-count: 3;
  }
  p {
    color: red;
  }
}
@media (min-width: 768px) and (max-width: 991px) {
  .waterfall {
    column-count: 2;
  }
  p {
    color: orange;
  }
}
@media (max-width: 767px) {
  .waterfall {
    column-count: 1;
  }
}
</style>

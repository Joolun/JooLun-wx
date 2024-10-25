<template>
  <div class="app-container">
    <div class="waterfall" v-loading="tableLoading">
      <template v-for="item in tableData" :key="item.id">
        <div
          v-if="item.content && item.content.newsItem"
          class="waterfall-item"
        >
          <WxNews :objData="item.content.newsItem"></WxNews>
          <el-row class="ope-row flex-c">
            <el-button
              type="danger"
              icon="el-icon-delete"
              circle
              @click="delMaterial(item)"
            ></el-button>
          </el-row>
        </div>
      </template>
    </div>
    <div
      v-if="tableData.length <= 0 && !tableLoading"
      class="el-table__empty-block"
    >
      <el-empty />
    </div>
    <el-pagination
      @size-change="sizeChange"
      @current-change="currentChange"
      v-model:current-page="page.currentPage"
      :page-sizes="[10, 20]"
      :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.total"
      class="pagination"
    >
    </el-pagination>
  </div>
</template>

<script setup name="WxMpFreePublish">
import { getPage, delObj } from "@/api/wxmp/wxfreepublish";
import WxNews from "@/components/wx-news/main.vue";
import { getToken } from "@/utils/auth";
import { getCurrentInstance, onMounted, reactive, ref, watch } from "vue";

const { proxy } = getCurrentInstance();

const tree = ref(null);

const data = reactive({
  tableData: [],
  page: {
    total: 0, // 总页数
    currentPage: 1, // 当前页数
    pageSize: 10, // 每页显示多少条
  },
  page1: {
    total: 0, // 总页数
    currentPage: 1, // 当前页数
    pageSize: 10, // 每页显示多少条
  },
  tableLoading: false,
  headers: {
    Authorization: "Bearer " + getToken(),
  },
});
const { tableData, tableLoading, page, page1, headers } = toRefs(data);

watch(
  () => proxy.filterText,
  (val) => {
    tree.value.filter(val);
  }
);
onMounted(() => {
  getPageF(data.page);
});

function getPageF(page, params) {
  data.tableData = [];
  data.tableLoading = true;
  getPage(
    Object.assign(
      {
        current: page.currentPage,
        size: page.pageSize,
      },
      params
    )
  )
    .then((response) => {
      data.tableData = response.data.items;
      data.page.total = response.data.totalCount;
      data.page.currentPage = page.currentPage;
      data.page.pageSize = page.pageSize;
      data.tableLoading = false;
    })
    .catch(() => {
      data.tableLoading = false;
    });
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

function delMaterial(item) {
  proxy
    .$confirm("删除后用户将无法访问此页面，确定删除？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      data.tableLoading = true;
      delObj({
        id: item.articleId,
      })
        .then((response) => {
          data.tableLoading = false;
          if (response.code == 200) {
            getPageF(data.page);
          } else {
            data.tableLoading = false;
            proxy.$message.error("删除出错：" + response.msg);
          }
        })
        .catch(() => {
          data.tableLoading = false;
        });
    });
}
</script>

<style lang="scss" scoped>
.pagination {
  float: right;
  margin-right: 25px;
}
.add_but {
  padding: 10px;
}
.ope-row {
  margin-top: 5px;
  text-align: center;
  border-top: 1px solid #eaeaea;
  padding-top: 5px;
}
.item-name {
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
}
.el-upload__tip {
  margin-left: 5px;
}
.left {
  display: inline-block;
  width: 35%;
  vertical-align: top;
  margin-top: 200px;
}
.right {
  display: inline-block;
  width: 60%;
  margin-top: -40px;
}
.avatar-uploader {
  width: 20%;
  display: inline-block;
}
.avatar-uploader .el-upload {
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  text-align: unset !important;
}
.avatar-uploader .el-upload:hover {
  border-color: #165dff;
}
.avatar-uploader-icon {
  border: 1px solid #d9d9d9;
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 230px;
  height: 120px;
}
.avatar1 {
  width: 120px;
  height: 120px;
}
.digest {
  width: 60%;
  display: inline-block;
  vertical-align: top;
}
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
.news-main {
  background-color: #ffffff;
  width: 100%;
  margin: auto;
  height: 120px;
}
.news-content {
  background-color: #acadae;
  width: 100%;
  height: 120px;
  position: relative;
}
.news-content-title {
  display: inline-block;
  font-size: 15px;
  color: #ffffff;
  position: absolute;
  left: 0px;
  bottom: 0px;
  background-color: black;
  width: 98%;
  padding: 1%;
  opacity: 0.65;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  height: 25px;
}
.news-main-item {
  background-color: #ffffff;
  padding: 5px 0px;
  border-top: 1px solid #eaeaea;
  width: 100%;
  margin: auto;
}
.news-content-item {
  position: relative;
  margin-left: -3px;
}
.news-content-item-title {
  display: inline-block;
  font-size: 12px;
  width: 70%;
}
.news-content-item-img {
  display: inline-block;
  width: 25%;
  background-color: #acadae;
}
.input-tt {
  padding: 5px;
}
.activeAddNews {
  border: 5px solid #2bb673;
}
.news-main-plus {
  width: 280px;
  text-align: center;
  margin: auto;
  height: 50px;
}
.icon-plus {
  margin: 10px;
  font-size: 25px;
}
.select-item {
  width: 60%;
  padding: 10px;
  margin: 0 auto 10px auto;
  border: 1px solid #eaeaea;
}
.father .child {
  display: none;
  text-align: center;
  position: relative;
  bottom: 25px;
}
.father:hover .child {
  display: block;
}
.thumb-div {
  display: inline-block;
  width: 30%;
  text-align: center;
}
.thumb-but {
  margin: 5px;
}
.material-img {
  width: 100%;
  height: 100%;
}
</style>

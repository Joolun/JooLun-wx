<template>
  <div class="app-container">
    <div class="add_but">
      <el-button type="primary" @click="handleAddNews">新 增 </el-button>
    </div>
    <el-dialog
      :title="operateMaterial == 'add' ? '新建图文' : '修改图文'"
      append-to-body
      :before-close="dialogNewsClose"
      :close-on-click-modal="false"
      v-model="dialogNewsVisible"
      width="80%"
      top="20px"
    >
      <div class="left">
        <div class="select-item">
          <div v-for="(news, index) in articlesAdd" :key="news.id">
            <div
              class="news-main father"
              v-if="index == 0"
              :class="{ activeAddNews: isActiveAddNews == index }"
              @click="activeNews(index)"
            >
              <div class="news-content">
                <el-image class="material-img" :src="news.thumbUrl">
                  <template #error
                    ><div class="flex-c material-img">未设置</div>
                  </template>
                </el-image>
                <div class="news-content-title">{{ news.title }}</div>
              </div>
              <div class="child" v-if="articlesAdd.length > 1">
                <el-button size="small" icon="Bottom" @click="downNews(index)"
                  >下移</el-button
                >
                <el-button
                  v-if="operateMaterial == 'add'"
                  size="small"
                  type="danger"
                  icon="Delete"
                  @click="minusNews(index)"
                  >删除
                </el-button>
              </div>
            </div>
            <div
              class="news-main-item father"
              v-if="index > 0"
              :class="{ activeAddNews: isActiveAddNews == index }"
              @click="activeNews(index)"
            >
              <div class="news-content-item">
                <div class="news-content-item-title">{{ news.title }}</div>
                <div class="news-content-item-img">
                  <el-image
                    class="material-img"
                    style="height: 55px"
                    :src="news.thumbUrl"
                  >
                    <template #error
                      ><div
                        class="flex-c material-img"
                        style="background: #eeeeee"
                      >
                        未设置
                      </div>
                    </template>
                  </el-image>
                </div>
              </div>
              <div class="child">
                <el-button
                  v-if="articlesAdd.length > index + 1"
                  size="small"
                  icon="Bottom"
                  @click="downNews(index)"
                  >下移
                </el-button>
                <el-button size="small" icon="Top" @click="upNews(index)"
                  >上移</el-button
                >
                <el-button
                  v-if="operateMaterial == 'add'"
                  size="small"
                  type="danger"
                  icon="Delete"
                  @click="minusNews(index)"
                  >删除
                </el-button>
              </div>
            </div>
          </div>

          <div
            v-if="articlesAdd.length < 8 && operateMaterial == 'add'"
            class="news-main-plus"
          >
            <el-button @click="plusNews" type="primary" icon="Plus" circle />
          </div>
        </div>
      </div>
      <div class="right" v-loading="addMaterialLoading">
        <!--富文本编辑器组件-->
        <el-tabs v-model="activeName">
          <el-tab-pane label="封面标题" name="title">
            <div>
              <div class="thumb-div">
                <div class="input-tt" style="display: flex">封面：</div>
                <div>
                  <img
                    class="material-img"
                    v-if="articlesAdd[isActiveAddNews].thumbUrl"
                    :src="articlesAdd[isActiveAddNews].thumbUrl"
                    :class="isActiveAddNews == 0 ? 'avatar' : 'avatar1'"
                  />
                  <el-icon
                    v-else
                    class="avatar-uploader-icon"
                    :class="isActiveAddNews == 0 ? 'avatar' : 'avatar1'"
                    ><Plus
                  /></el-icon>
                  <div class="thumb-but">
                    <el-upload
                      :action="actionUrl"
                      :headers="headers"
                      multiple
                      :limit="1"
                      :on-success="handleUploadSuccess"
                      :file-list="fileList"
                      :before-upload="beforeThumbImageUpload"
                      :data="uploadData"
                    >
                      <template #trigger>
                        <el-button size="small" type="primary">
                          本地上传
                        </el-button>
                      </template>
                      <el-button
                        size="small"
                        type="primary"
                        @click="openMaterial"
                        style="margin-left: 5px"
                        >素材库选择
                      </el-button>
                      <template #tip>
                        <div class="el-upload__tip">
                          支持bmp/png/jpeg/jpg/gif格式，大小不超过2M
                        </div>
                      </template>
                    </el-upload>
                  </div>
                </div>
              </div>
              <el-dialog
                title="选择图片"
                v-model="dialogImageVisible"
                width="80%"
                append-to-body
              >
                <WxMaterialSelect
                  :objData="{ repType: 'image' }"
                  @selectMaterial="selectMaterial"
                ></WxMaterialSelect>
              </el-dialog>
              <div class="digest">
                <div class="input-tt">标题：</div>
                <el-input
                  v-model="articlesAdd[isActiveAddNews].title"
                  placeholder="请输入标题"
                ></el-input>
                <div class="input-tt">作者：</div>
                <el-input
                  v-model="articlesAdd[isActiveAddNews].author"
                  placeholder="请输入作者"
                ></el-input>
                <div class="input-tt">原文地址：</div>
                <el-input
                  v-model="articlesAdd[isActiveAddNews].contentSourceUrl"
                  placeholder="请输入原文地址"
                ></el-input>
              </div>
            </div>
            <div class="input-tt">摘要：</div>
            <el-input
              :rows="5"
              type="textarea"
              v-model="articlesAdd[isActiveAddNews].digest"
              placeholder="请输入摘要"
              maxlength="120"
            ></el-input>
          </el-tab-pane>
          <el-tab-pane label="内容详情" name="content">
            <div style="padding-bottom: 20px">
              <WxEditor
                v-model="articlesAdd[isActiveAddNews].content"
                :minHeight="350"
                :uploadData="uploadData"
                v-if="hackResetEditor"
              />
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogNewsVisible = false">取 消</el-button>
          <el-button type="primary" @click="subNews">提 交</el-button>
        </div>
      </template>
    </el-dialog>
    <div class="waterfall" v-loading="tableLoading">
      <template v-for="item in tableData" :key="item.id">
        <div
          v-if="item.content && item.content.newsItem"
          class="waterfall-item"
        >
          <WxNews :objData="item.content.newsItem"></WxNews>
          <el-row class="ope-row">
            <el-button
              type="success"
              icon="Promotion"
              size="small"
              @click="handlePublishNews(item)"
              >发布</el-button
            >
            <el-button
              type="primary"
              size="small"
              plain
              icon="el-icon-edit"
              @click="handleEditNews(item)"
              >编辑</el-button
            >
            <el-button
              type="danger"
              size="small"
              plain
              icon="el-icon-delete"
              @click="delMaterial(item)"
              >删除</el-button
            >
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

<script setup name="WxMpDraft">
import { getPage, addObj, delObj, putObj, publish } from "@/api/wxmp/wxdraft";
import { getPage as getPage1 } from "@/api/wxmp/wxmaterial";
import WxEditor from "@/components/Editor/WxEditor.vue";
import WxNews from "@/components/wx-news/main.vue";
import WxMaterialSelect from "@/components/wx-material-select/main.vue";
import { getToken } from "@/utils/auth";
import { getCurrentInstance, nextTick, onMounted, reactive } from "vue";

const { proxy } = getCurrentInstance();

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
  fileList: [],
  dialogVideoVisible: false,
  dialogNewsVisible: false,
  addMaterialLoading: false,
  uploadData: {
    mediaType: "image",
    title: "",
    introduction: "",
  },
  articlesAdd: [
    {
      title: "",
      thumbMediaId: "",
      author: "",
      digest: "",
      showCoverPic: "",
      content: "",
      contentSourceUrl: "",
      needOpenComment: "",
      onlyFansCanComment: "",
      thumbUrl: "",
    },
  ],
  isActiveAddNews: 0,
  dialogImageVisible: false,
  imageListData: [],
  tableLoading1: false,
  operateMaterial: "add",
  articlesMediaId: "",
  hackResetEditor: false,
  actionUrl: ref(
    import.meta.env.VITE_APP_BASE_API + "/wxmaterial/materialFileUpload"
  ), // 上传的图片服务器地址
  headers: {
    Authorization: "Bearer " + getToken(),
  },
  activeName: "title",
});
const {
  activeName,
  materialType,
  tableData,
  tableLoading,
  page,
  page1,
  fileList,
  dialogVideoVisible,
  dialogNewsVisible,
  addMaterialLoading,
  uploadData,
  uploadRules,
  articlesAdd,
  isActiveAddNews,
  dialogImageVisible,
  imageListData,
  tableLoading1,
  operateMaterial,
  articlesMediaId,
  hackResetEditor,
  actionUrl,
  headers,
} = toRefs(data);

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
    .$confirm("此操作将永久删除该草稿, 是否继续?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      data.tableLoading = true;
      delObj({
        id: item.mediaId,
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

function getPage1F() {
  data.tableLoading1 = true;
  getPage1({
    current: data.page1.currentPage,
    size: data.page1.pageSize,
    type: "image",
  })
    .then((response) => {
      data.tableLoading1 = false;
      data.imageListData = response.data.items;
      data.page1.total = response.data.totalCount;
    })
    .catch(() => {
      data.tableLoading1 = false;
    });
}

function openMaterial() {
  data.imageListData = null;
  data.page1.currentPage = 1;
  getPage1F();
  data.dialogImageVisible = true;
}

function dialogNewsClose(done) {
  /* Warn: Unknown source: $confirm */
  proxy
    .$confirm("修改内容可能还未保存，确定关闭吗?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      data.hackResetEditor = false; //销毁组件
      nextTick(() => {
        data.hackResetEditor = true; //重建组件
      });
      data.isActiveAddNews = 0;
      done();
    })
    .catch(() => {});
}

function downNews(index) {
  let temp = data.articlesAdd[index];
  data.articlesAdd[index] = data.articlesAdd[index + 1];
  data.articlesAdd[index + 1] = temp;
  data.isActiveAddNews = index + 1;
}

function upNews(index) {
  let temp = data.articlesAdd[index];
  data.articlesAdd[index] = data.articlesAdd[index - 1];
  data.articlesAdd[index - 1] = temp;
  data.isActiveAddNews = index - 1;
}

function activeNews(index) {
  data.hackResetEditor = false; //销毁组件
  nextTick(() => {
    data.hackResetEditor = true; //重建组件
  });
  data.isActiveAddNews = index;
}

function minusNews(index) {
  proxy
    .$confirm("确定删除该图文吗?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      data.articlesAdd.splice(index, 1);
      if (data.isActiveAddNews == index) {
        data.isActiveAddNews = 0;
      }
    })
    .catch(() => {});
}

function plusNews() {
  data.articlesAdd.push({
    title: "",
    thumbMediaId: "",
    author: "",
    digest: "",
    showCoverPic: "",
    content: "",
    contentSourceUrl: "",
    needOpenComment: "",
    onlyFansCanComment: "",
    thumbUrl: "",
  });
  data.isActiveAddNews = data.articlesAdd.length - 1;
}

function subNews() {
  data.addMaterialLoading = true;
  if (data.operateMaterial == "add") {
    addObj({
      articles: data.articlesAdd,
    })
      .then((response) => {
        data.addMaterialLoading = false;
        data.dialogNewsVisible = false;
        if (response.code == 200) {
          data.isActiveAddNews = 0;
          data.articlesAdd = [
            {
              title: "",
              thumbMediaId: "",
              author: "",
              digest: "",
              showCoverPic: "",
              content: "",
              contentSourceUrl: "",
              needOpenComment: "",
              onlyFansCanComment: "",
              thumbUrl: "",
            },
          ];
          data.page.currentPage = 1;
          getPageF(data.page);
        } else {
          proxy.$message.error("新增图文出错：" + response.msg);
        }
      })
      .catch(() => {
        data.addMaterialLoading = false;
      });
  }
  if (data.operateMaterial == "edit") {
    putObj({
      articles: data.articlesAdd,
      mediaId: data.articlesMediaId,
    })
      .then((response) => {
        data.addMaterialLoading = false;
        data.dialogNewsVisible = false;
        if (response.code == 200) {
          data.isActiveAddNews = 0;
          data.articlesAdd = [
            {
              title: "",
              thumbMediaId: "",
              author: "",
              digest: "",
              showCoverPic: "",
              content: "",
              contentSourceUrl: "",
              needOpenComment: "",
              onlyFansCanComment: "",
              thumbUrl: "",
            },
          ];
          getPageF(data.page);
        } else {
          proxy.$message.error("修改图文出错：" + response.msg);
        }
      })
      .catch(() => {
        data.addMaterialLoading = false;
      });
  }
}

function handleEditNews(item) {
  data.hackResetEditor = false; //销毁组件
  nextTick(() => {
    data.hackResetEditor = true; //重建组件
  });
  if (data.operateMaterial == "add") {
    data.isActiveAddNews = 0;
  }
  data.operateMaterial = "edit";
  data.articlesAdd = JSON.parse(JSON.stringify(item.content.newsItem));
  data.articlesMediaId = item.mediaId;
  data.dialogNewsVisible = true;
}

function handleAddNews() {
  data.hackResetEditor = false; //销毁组件
  nextTick(() => {
    data.hackResetEditor = true; //重建组件
  });
  if (data.operateMaterial == "edit") {
    data.isActiveAddNews = 0;
    data.articlesAdd = [
      {
        title: "",
        thumbMediaId: "",
        author: "",
        digest: "",
        showCoverPic: "",
        content: "",
        contentSourceUrl: "",
        needOpenComment: "",
        onlyFansCanComment: "",
        thumbUrl: "",
      },
    ];
  }
  data.operateMaterial = "add";
  data.dialogNewsVisible = true;
}

function beforeThumbImageUpload(file) {
  data.addMaterialLoading = true;
  const isType =
    file.type === "image/jpeg" ||
    file.type === "image/png" ||
    file.type === "image/gif" ||
    file.type === "image/bmp" ||
    file.type === "image/jpg";
  const isLt = file.size / 1024 / 1024 < 2;
  if (!isType) {
    proxy.$message.error("上传图片格式不对!");
  }
  if (!isLt) {
    proxy.$message.error("上传图片大小不能超过2M!");
  }
  data.addMaterialLoading = false;
  return isType && isLt;
}

function handleUploadSuccess(response, file, fileList) {
  data.tableLoading = false;
  data.addMaterialLoading = false;
  if (response.code == 200) {
    data.dialogVideoVisible = false;
    data.fileList = [];
    data.uploadData.title = "";
    data.uploadData.introduction = "";
    data.articlesAdd[data.isActiveAddNews].thumbMediaId = response.data.mediaId;
    data.articlesAdd[data.isActiveAddNews].thumbUrl = response.data.url;
  } else {
    proxy.$message.error("上传出错：" + response.msg);
  }
}

function selectMaterial(item) {
  data.dialogImageVisible = false;
  data.articlesAdd[data.isActiveAddNews].thumbMediaId = item.mediaId;
  data.articlesAdd[data.isActiveAddNews].thumbUrl = item.url;
}

function handlePublishNews(item) {
  proxy
    .$confirm(
      "你正在通过发布的方式发表内容。 发布不占用群发次数，一天可多次发布。已发布内容不会推送给用户，也不会展示在公众号主页中。 发布后，你可以前往发表记录获取链接，也可以将发布内容添加到自定义菜单、自动回复、话题和页面模板中。",
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    )
    .then(() => {
      data.tableLoading = true;
      publish(item.mediaId)
        .then((response) => {
          data.tableLoading = false;

          proxy.$message.success("发布任务提交成功");
          getPageF(data.page);
        })
        .catch(() => {
          data.tableLoading = false;
        });
    })
    .catch(() => {});
}
</script>

<style lang="scss" scoped>
.pagination {
  float: right;
  margin-right: 25px;
}
.add_but {
  padding-bottom: 10px;
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
  margin-top: 10px;
}
.right {
  display: inline-block;
  width: 60%;
  min-height: 500px;
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
  width: 220px;
  height: 120px;
}
.avatar1 {
  width: 120px;
  height: 120px;
}
.digest {
  width: 70%;
  display: inline-block;
  vertical-align: top;
  padding-left: 20px;
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
  min-height: 25px;
  left: 0px;
  bottom: 0px;
  padding: 5px;
  background-color: rgba(0, 0, 0, 0.5);
  width: 100%;
  opacity: 0.65;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.news-main-item {
  background-color: #ffffff;
  padding: 5px 0px;
  border-bottom: 1px solid #eaeaea;
  width: 100%;
  margin: auto;
}
.news-content-item {
  position: relative;
  padding: 5px;
  display: flex;
  align-items: center;
}
.news-content-item-title {
  display: inline-block;
  font-size: 12px;
  align-items: center;
  width: 70%;
}
.news-content-item-img {
  display: inline-block;
  width: 25%;
  // background-color: #acadae;
}
.input-tt {
  padding: 5px;
}
.activeAddNews {
  position: relative;
  overflow: visible;
  cursor: pointer;
}

.activeAddNews::before {
  position: absolute;
  inset: 0;
  z-index: 1;
  content: "";
  border: 5px solid #2bb673;
}
.news-main-plus {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
}
.icon-plus {
  margin: 10px;
  font-size: 25px;
}
.select-item {
  width: 60%;
  margin: 0 auto 10px auto;
  border: 1px solid #eaeaea;
}
.father .child {
  display: none;
  text-align: center;
  position: relative;
  bottom: 25px;
  height: 0;
}
.father:hover .child {
  display: block;
  z-index: 3;
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
  min-width: 50px;
  min-height: 50px;
}

.flex-c {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>

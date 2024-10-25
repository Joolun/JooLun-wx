<template>
  <div class="app-container">
    <el-tabs v-model="materialType" @tab-click="handleClick">
      <el-tab-pane name="image">
        <template #label>
          <span>图片</span>
        </template>
        <div class="add_but">
          <el-upload
            :action="actionUrl"
            :headers="headers"
            multiple
            :limit="1"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :file-list="fileList"
            :before-upload="beforeImageUpload"
            :data="uploadData"
          >
            <el-button type="primary" icon="Upload">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">
                支持bmp/png/jpeg/jpg/gif格式，大小不超过2M
              </div>
            </template>
          </el-upload>
        </div>
        <div class="waterfall" v-loading="tableLoading">
          <div class="waterfall-item" v-for="item in tableData" :key="item.id">
            <a target="_blank" :href="item.url">
              <img class="material-img" :src="item.url" />
              <div class="item-name">{{ item.name }}</div>
            </a>
            <el-row class="ope-row flex-c">
              <el-button
                type="danger"
                icon="el-icon-delete"
                circle
                @click="delMaterial(item)"
              ></el-button>
            </el-row>
          </div>
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
      </el-tab-pane>
      <el-tab-pane name="voice">
        <template #label>
          <span>语音</span>
        </template>
        <div class="add_but">
          <el-upload
            :action="actionUrl"
            :headers="headers"
            multiple
            :limit="1"
            :on-success="handleUploadSuccess"
            :file-list="fileList"
            :before-upload="beforeVoiceUpload"
            :data="uploadData"
          >
            <el-button type="primary" icon="Upload">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">
                格式支持mp3/wma/wav/amr，文件大小不超过2M，播放长度不超过60s
              </div>
            </template>
          </el-upload>
        </div>
        <el-table :data="tableData" stripe border v-loading="tableLoading">
          <el-table-column prop="mediaId" label="media_id"> </el-table-column>
          <el-table-column prop="name" label="名称"> </el-table-column>
          <el-table-column prop="updateTime" label="更新时间">
          </el-table-column>
          <el-table-column fixed="right" label="操作">
            <template v-slot="scope">
              <el-button
                link
                icon="el-icon-download"
                type="primary"
                @click="handleDown(scope.row)"
                >下载</el-button
              >
              <el-button
                link
                icon="el-icon-delete"
                type="danger"
                @click="delMaterial(scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
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
      </el-tab-pane>
      <el-tab-pane name="video">
        <template #label>
          <span>视频</span>
        </template>
        <div class="add_but">
          <el-button type="primary" icon="Plus" @click="handleAddVideo"
            >新建</el-button
          >
        </div>
        <el-dialog title="新建视频" v-model="dialogVideoVisible">
          <el-upload
            ref="uploadVideo"
            :action="actionUrl"
            :headers="headers"
            multiple
            :limit="1"
            :on-success="handleUploadSuccess"
            :file-list="fileList"
            :before-upload="beforeVideoUpload"
            :auto-upload="false"
            :data="uploadData"
          >
            <template #trigger>
              <el-button type="primary">选择视频</el-button>
            </template>
            <div class="el-upload__tip">格式支持MP4，文件大小不超过10MB</div>
          </el-upload>
          <el-form :model="uploadData" :rules="uploadRules" ref="uploadForm">
            <el-form-item label="标题" prop="title">
              <el-input
                v-model="uploadData.title"
                placeholder="标题将展示在相关播放页面，建议填写清晰、准确、生动的标题"
              ></el-input>
            </el-form-item>
            <el-form-item label="描述" prop="introduction">
              <el-input
                :rows="3"
                type="textarea"
                v-model="uploadData.introduction"
                placeholder="介绍语将展示在相关播放页面，建议填写简洁明确、有信息量的内容"
              ></el-input>
            </el-form-item>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="dialogVideoVisible = false">取 消</el-button>
              <el-button type="primary" @click="subVideo">提 交</el-button>
            </div>
          </template>
        </el-dialog>
        <el-table :data="tableData" stripe border v-loading="tableLoading">
          <el-table-column prop="mediaId" label="media_id"> </el-table-column>
          <el-table-column prop="name" label="名称"> </el-table-column>
          <el-table-column prop="updateTime" label="更新时间">
          </el-table-column>
          <el-table-column fixed="right" label="操作">
            <template v-slot="scope">
              <el-button
                link
                icon="el-icon-view"
                type="primary"
                @click="handleInfo(scope.row)"
                >查看</el-button
              >
              <el-button
                link
                icon="el-icon-delete"
                type="danger"
                @click="delMaterial(scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
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
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup name="WxMpMaterial">
import {
  getPage,
  addObj,
  delObj,
  getMaterialOther,
  getMaterialVideo,
} from "@/api/wxmp/wxmaterial";
import { checkPermi, checkRole } from "@/utils/permission";
import WxEditor from "@/components/Editor/WxEditor.vue";
import WxNews from "@/components/wx-news/main.vue";
import WxMaterialSelect from "@/components/wx-material-select/main.vue";
import { getToken } from "@/utils/auth";

const { proxy } = getCurrentInstance();

const uploadVideo = ref(null);

const data = reactive({
  materialType: "image",
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
  uploadRules: {
    title: [{ required: true, message: "请输入标题", trigger: "blur" }],
    introduction: [{ required: true, message: "请输入描述", trigger: "blur" }],
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
});

const {
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

function dialogNewsClose(done) {
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

function getPage1() {
  data.tableLoading1 = true;
  getPageF({
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

function selectMaterial(item) {
  data.dialogImageVisible = false;
  data.articlesAdd[data.isActiveAddNews].thumbMediaId = item.mediaId;
  data.articlesAdd[data.isActiveAddNews].thumbUrl = item.url;
}

function openMaterial() {
  data.imageListData = null;
  data.page1.currentPage = 1;
  getPage1();
  data.dialogImageVisible = true;
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

function handleInfo(row) {
  data.tableLoading = true;
  getMaterialVideo({
    mediaId: row.mediaId,
  })
    .then((response) => {
      data.tableLoading = false;
      if (response.code == 200) {
        let downUrl = response.data.downUrl;
        window.open(downUrl, "_blank");
      } else {
        proxy.$message.error("获取微信视频素材出错：" + response.data.msg);
      }
    })
    .catch(() => {
      data.tableLoading = false;
    });
}

function handleDown(row) {
  data.tableLoading = true;
  getMaterialOther({
    mediaId: row.mediaId,
    fileName: row.name,
  })
    .then((response) => {
      data.tableLoading = false;
      let url = window.URL.createObjectURL(new Blob([response.data]));
      let link = document.createElement("a");
      link.style.display = "none";
      link.href = url;
      link.setAttribute("download", row.name);
      document.body.appendChild(link);
      link.click();
    })
    .catch(() => {
      data.tableLoading = false;
    });
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
          proxy.$message.error("新增图文出错：" + response.data.msg);
        }
      })
      .catch(() => {
        data.addMaterialLoading = false;
      });
  }
  if (data.operateMaterial == "edit") {
    // materialNewsUpdate({
    //   articles: data.articlesAdd,
    //   mediaId: data.articlesMediaId,
    // })
    //   .then((response) => {
    //     data.addMaterialLoading = false;
    //     data.dialogNewsVisible = false;
    //     if (response.code == 200) {
    //       data.isActiveAddNews = 0;
    //       data.articlesAdd = [
    //         {
    //           title: "",
    //           thumbMediaId: "",
    //           author: "",
    //           digest: "",
    //           showCoverPic: "",
    //           content: "",
    //           contentSourceUrl: "",
    //           needOpenComment: "",
    //           onlyFansCanComment: "",
    //           thumbUrl: "",
    //         },
    //       ];
    //       getPageF(data.page);
    //     } else {
    //       proxy.$message.error("修改图文出错：" + response.data.msg);
    //     }
    //   })
    //   .catch(() => {
    //     data.addMaterialLoading = false;
    //   });
  }
}

function subVideo() {
  proxy.$refs["uploadForm"].validate((valid) => {
    if (valid) {
      uploadVideo.value.submit();
    } else {
      return false;
    }
  });
}

function handleAddVideo() {
  data.dialogVideoVisible = true;
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
  data.articlesAdd = JSON.parse(JSON.stringify(item.content.articles));
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

function beforeImageUpload(file) {
  data.tableLoading = true;
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
  data.tableLoading = false;
  return isType && isLt;
}

function beforeVoiceUpload(file) {
  data.tableLoading = true;
  const isType =
    file.type === "audio/mp3" ||
    file.type === "audio/wma" ||
    file.type === "audio/wav" ||
    file.type === "audio/amr";
  const isLt = file.size / 1024 / 1024 < 2;
  if (!isType) {
    proxy.$message.error("上传语音格式不对!");
  }
  if (!isLt) {
    proxy.$message.error("上传语音大小不能超过2M!");
  }
  data.tableLoading = false;
  return isType && isLt;
}

function beforeVideoUpload(file) {
  data.addMaterialLoading = true;
  const isType = file.type === "video/mp4";
  const isLt = file.size / 1024 / 1024 < 10;
  if (!isType) {
    proxy.$message.error("上传视频格式不对!");
  }
  if (!isLt) {
    proxy.$message.error("上传视频大小不能超过10M!");
  }
  data.addMaterialLoading = false;
  return isType && isLt;
}

function delMaterial(item) {
  proxy
    .$confirm("此操作将永久删除该文件, 是否继续?", "提示", {
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

            proxy.$message.error("删除出错：" + response.data.msg);
          }
        })
        .catch(() => {
          data.tableLoading = false;
        });
    });
}

function handleUploadError(response) {
  proxy.$message.error("上传出错：" + response);
}
function handleUploadSuccess(response, file, fileList) {
  data.tableLoading = false;
  data.addMaterialLoading = false;
  if (response.code == 200) {
    data.dialogVideoVisible = false;
    data.fileList = [];
    data.uploadData.title = "";
    data.uploadData.introduction = "";
    if (data.materialType != "news") {
      data.page.currentPage = 1;
      getPageF(data.page);
    } else {
      data.articlesAdd[data.isActiveAddNews].thumbMediaId =
        response.data.mediaId;
      data.articlesAdd[data.isActiveAddNews].thumbUrl = response.data.url;
    }
  } else {
    data.fileList = [];
    proxy.$message.error("上传出错：" + response.msg);
  }
}

function handleClick(tab, event) {
  let materialType = tab.paneName;
  data.page.currentPage = 1;
  data.materialType = materialType;
  getPageF(data.page);
  data.uploadData.mediaType = materialType;
  if (materialType == "news") {
    data.uploadData.mediaType = "image";
  }
}

function getPageF(page, params) {
  data.tableData = [];
  data.tableLoading = true;
  getPage(
    Object.assign(
      {
        current: page.currentPage,
        size: page.pageSize,
        type: data.materialType,
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

function refreshChange(page) {
  getPageF(data.page);
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
  border-color: #409eff;
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

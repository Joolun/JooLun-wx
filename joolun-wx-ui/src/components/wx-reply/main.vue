<!--
  - Copyright (C) 2024
  - All rights reserved, Designed By www.joolun.com
-->
<template>
  <el-tabs
    type="border-card"
    v-model="objDataCopy.repType"
    @tab-click="handleClick">
    <el-tab-pane name="text">
      <template #label>
        <div class="flex-c">
          <el-icon><el-icon-document /></el-icon>文本
        </div>
      </template>
      <el-input
        v-model="objDataCopy.repContent"
        type="textarea"
        :rows="5"
        placeholder="请输入内容" />
    </el-tab-pane>
    <el-tab-pane name="image">
      <template #label>
        <span class="flex-c"
          ><el-icon><el-icon-picture /></el-icon>图片</span
        >
      </template>
      <el-row>
        <div
          v-if="objDataCopy.repUrl"
          class="select-item">
          <img
            class="material-img"
            :src="objDataCopy.repUrl" />
          <p
            v-if="objDataCopy.repName"
            class="item-name">
            {{ objDataCopy.repName }}
          </p>
          <el-row class="ope-row">
            <el-button
              type="danger"
              icon="delete"
              circle
              @click="deleteObj" />
          </el-row>
        </div>
        <div
          v-if="!objDataCopy.repUrl"
          class="w-full">
          <el-row style="text-align: center">
            <el-col
              :span="12"
              class="col-select">
              <el-button
                type="success"
                @click="openMaterial"
                >素材库选择<el-icon class="el-icon--right">
                  <el-icon-circle-check />
                </el-icon>
              </el-button>
            </el-col>
            <el-col
              :span="12"
              class="col-add">
              <el-upload
                :action="actionUrl"
                :headers="headers"
                multiple
                :limit="1"
                :on-success="handleUploadSuccess"
                :on-error="handleUploadError"
                :file-list="fileList"
                :before-upload="beforeImageUpload"
                :data="uploadData">
                <el-button type="primary">上传图片</el-button>
                <template #tip>
                  <div class="el-upload__tip">支持bmp/png/jpeg/jpg/gif格式，大小不超过2M</div>
                </template>
              </el-upload>
            </el-col>
          </el-row>
        </div>
        <el-dialog
          v-model="dialogImageVisible"
          title="选择图片"
          width="90%"
          append-to-body>
          <WxMaterialSelect
            :objData="objDataCopy"
            @selectMaterial="selectMaterial" />
        </el-dialog>
      </el-row>
    </el-tab-pane>
    <el-tab-pane name="voice">
      <template #label>
        <span class="flex-c"
          ><el-icon><el-icon-phone /></el-icon>语音</span
        >
      </template>
      <el-row v-if="objDataCopy.repType == 'voice'">
        <div
          v-if="objDataCopy.repName"
          class="select-item2">
          <p class="item-name">{{ objDataCopy.repName }}</p>
          <div class="item-infos">
            <WxVoicePlayer
              :objData="
                Object.assign(tempPlayerObj, {
                  repMediaId: objDataCopy.media_id,
                  repName: objDataCopy.repName,
                })
              " />
          </div>
          <el-row class="ope-row">
            <el-button
              type="danger"
              icon="delete"
              circle
              @click="deleteObj" />
          </el-row>
        </div>
        <div
          v-if="!objDataCopy.repName"
          class="w-full">
          <el-row style="text-align: center">
            <el-col
              :span="12"
              class="col-select">
              <el-button
                type="success"
                @click="openMaterial"
                >素材库选择<el-icon class="el-icon--right">
                  <el-icon-circle-check />
                </el-icon>
              </el-button>
            </el-col>
            <el-col
              :span="12"
              class="col-add">
              <el-upload
                :action="actionUrl"
                :headers="headers"
                multiple
                :limit="1"
                :on-success="handleUploadSuccess"
                :on-error="handleUploadError"
                :file-list="fileList"
                :before-upload="beforeVoiceUpload"
                :data="uploadData">
                <el-button type="primary">点击上传</el-button>
                <template #tip>
                  <div class="el-upload__tip">格式支持mp3/wma/wav/amr，文件大小不超过2M，播放长度不超过60s</div>
                </template>
              </el-upload>
            </el-col>
          </el-row>
        </div>
        <el-dialog
          v-model="dialogVoiceVisible"
          title="选择语音"
          width="90%"
          append-to-body>
          <WxMaterialSelect
            :objData="objDataCopy"
            @selectMaterial="selectMaterial" />
        </el-dialog>
      </el-row>
    </el-tab-pane>
    <el-tab-pane name="video">
      <template #label>
        <span class="flex-c"
          ><el-icon><el-icon-share /></el-icon>视频</span
        >
      </template>
      <el-row class="w-full">
        <div class="w-full">
          <el-input
            v-model="objDataCopy.repName"
            placeholder="请输入标题">
            <template #prepend>标题</template>
          </el-input>
        </div>
        <div style="margin: 20px 0" />
        <el-input
          style="margin-top: 10px"
          v-model="objDataCopy.repDesc"
          placeholder="请输入描述">
          <template #prepend>描述</template>
        </el-input>
        <div
          v-if="objDataCopy.repUrl"
          style="color: #888; font-size: 12px; background: #ececec; padding: 5px; word-wrap: break-word; word-break: break-all">
          <a
            target="_blank"
            class="flex-c"
            :href="objDataCopy.repUrl"
            ><el-icon size="18"><VideoPlay /></el-icon>
            <span style="margin-left: 5px"> {{ objDataCopy.repUrl }}</span>
          </a>
        </div>
        <div class="flex-c w-full"></div>
        <div
          style="text-align: center; margin-top: 20px"
          class="flex-c w-full">
          <el-button
            type="success"
            @click="openMaterial"
            >素材库选择<el-icon class="el-icon--right">
              <el-icon-circle-check />
            </el-icon>
          </el-button>
        </div>
        <el-dialog
          v-model="dialogVideoVisible"
          title="选择视频"
          width="90%"
          append-to-body>
          <WxMaterialSelect
            :objData="objDataCopy"
            @selectMaterial="selectMaterial" />
        </el-dialog>
      </el-row>
    </el-tab-pane>
    <el-tab-pane name="news">
      <template #label>
        <span class="flex-c"
          ><el-icon> <Message /> </el-icon>图文</span
        >
      </template>
      <el-row>
        <div
          v-if="objDataCopy.content"
          class="select-item">
          <WxNews :objData="objDataCopy.content.articles" />
          <el-row class="ope-row">
            <el-button
              type="danger"
              icon="delete"
              circle
              @click="deleteObj" />
          </el-row>
        </div>
        <div
          v-if="!objDataCopy.content"
          class="w-full">
          <el-row style="text-align: center">
            <el-col
              :span="24"
              class="col-select2">
              <el-button
                type="success"
                icon="edit"
                @click="openMaterial"
                >{{ newsType == "1" ? "选择已发布图文" : "选择草稿箱图文" }}<i class="el-icon-circle-check el-icon--right"></i
              ></el-button>
            </el-col>
          </el-row>
        </div>
        <el-dialog
          v-model="dialogNewsVisible"
          title="选择图文"
          width="90%"
          append-to-body>
          <WxMaterialSelect
            :objData="objDataCopy"
            :newsType="newsType"
            @selectMaterial="selectMaterial" />
        </el-dialog>
      </el-row>
    </el-tab-pane>
    <el-tab-pane name="music">
      <template #label>
        <span class="flex-c"
          ><el-icon><el-icon-service /></el-icon>音乐</span
        >
      </template>
      <div class="w-full">
        <el-row class="w-full">
          <el-col :span="6">
            <div class="thumb-div">
              <img
                v-if="objDataCopy.repThumbUrl"
                style="width: 80px"
                :src="objDataCopy.repThumbUrl" />
              <el-icon
                v-else
                class="avatar-uploader-icon">
                <el-icon-plus />
              </el-icon>
              <div class="flex-c">
                <el-upload
                  :action="actionUrl"
                  :headers="headers"
                  multiple
                  :limit="1"
                  :on-success="handleUploadSuccess"
                  :on-error="handleUploadError"
                  :file-list="fileList"
                  :before-upload="beforeThumbImageUpload"
                  :data="uploadData">
                  <template #trigger>
                    <el-button
                      size="small"
                      link
                      >本地上传</el-button
                    >
                  </template>
                  <el-button
                    size="small"
                    link
                    type="primary"
                    @click.stop="openMaterial"
                    >素材库选择</el-button
                  >
                </el-upload>
              </div>
            </div>
            <el-dialog
              v-model="dialogThumbVisible"
              title="选择图片"
              width="80%"
              append-to-body>
              <WxMaterialSelect
                :objData="{ repType: 'image' }"
                @selectMaterial="selectMaterial" />
            </el-dialog>
          </el-col>
          <el-col :span="18">
            <el-input
              v-model="objDataCopy.repName"
              placeholder="请输入标题" />
            <div style="margin: 10px 0" />
            <el-input
              v-model="objDataCopy.repDesc"
              placeholder="请输入描述" />
          </el-col>
        </el-row>
        <div style="margin: 20px 0" />
        <div class="w-full">
          <el-input
            v-model="objDataCopy.repUrl"
            placeholder="请输入音乐链接" />
        </div>
        <div style="margin: 10px 0" />
        <div>
          <el-input
            v-model="objDataCopy.repHqUrl"
            placeholder="请输入高质量音乐链接" />
        </div>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>
<script setup name="WxReply">
import { getPage, getMaterialVideo } from "@/api/wxmp/wxmaterial";
import WxNews from "@/components/wx-news/main.vue";
import WxMaterialSelect from "@/components/wx-material-select/main.vue";
import WxVoicePlayer from "@/components/wx-voice-play/main.vue";
import { getToken } from "@/utils/auth";

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
const objDataCopy = ref(props.objData);
if (!objDataCopy.value.repType) {
  objDataCopy.value.repType = "text";
}
const data = reactive({
  tempPlayerObj: {
    type: "2",
  },
  tableData: [],
  page: {
    total: 0,
    currentPage: 1,
    pageSize: 20,
    ascs: "",
    descs: "",
  },
  tableLoading: false,
  dialogNewsVisible: false,
  dialogImageVisible: false,
  dialogVoiceVisible: false,
  dialogVideoVisible: false,
  dialogThumbVisible: false,
  tempObj: new Map().set(objDataCopy.value.repType, Object.assign({}, objDataCopy.value)),
  fileList: [],
  uploadData: {
    mediaType: objDataCopy.value.repType,
    title: "",
    introduction: "",
  },
  actionUrl: "/weixin/wxmaterial/materialFileUpload",
  headers: {
    Authorization: "Bearer " + getToken().access_token,
  },
});

const { tableData, tableLoading, page, tempPlayerObj, dialogNewsVisible, dialogImageVisible, dialogVoiceVisible, dialogVideoVisible, dialogThumbVisible, fileList, actionUrl, headers, uploadData } = toRefs(data);

function beforeThumbImageUpload(file) {
  const isType = file.type === "image/jpeg" || file.type === "image/png" || file.type === "image/gif" || file.type === "image/bmp" || file.type === "image/jpg";
  const isLt = file.size / 1024 / 1024 < 2;

  if (!isType) {
    proxy.$message.error("上传图片格式不对!");
  }

  if (!isLt) {
    proxy.$message.error("上传图片大小不能超过2M!");
  }

  return isType && isLt;
}

function deleteObj() {
  objDataCopy.value.repUrl = "";
  objDataCopy.value.repName = "";
  objDataCopy.value.content = "";
  data.tempObj.set(objDataCopy.value.repType, objDataCopy.value);
  proxy.$emit("update:objData", objDataCopy.value);
}

function beforeVoiceUpload(file) {
  data.tableLoading = true;
  const isType = file.type === "audio/mp3" || file.type === "audio/wma" || file.type === "audio/wav" || file.type === "audio/amr";
  const isLt = file.size / 1024 / 1024 < 2;

  if (!isType) {
    proxy.$message.error("上传图片格式不对!");
  }

  if (!isLt) {
    proxy.$message.error("上传图片大小不能超过2M!");
  }

  return isType && isLt;
}

function beforeImageUpload(file) {
  const isType = file.type === "image/jpeg" || file.type === "image/png" || file.type === "image/gif" || file.type === "image/bmp" || file.type === "image/jpg";
  const isLt = file.size / 1024 / 1024 < 2;

  if (!isType) {
    proxy.$message.error("上传图片格式不对!");
  }

  if (!isLt) {
    proxy.$message.error("上传图片大小不能超过2M!");
  }

  return isType && isLt;
}

function handleUploadSuccess(response) {
  if (response.code == "0") {
    data.fileList = [];
    data.uploadData.title = "";
    data.uploadData.introduction = "";
    const item = response.data;
    selectMaterial(item);
  } else {
    proxy.$message.error("上传出错：" + response.msg);
  }
}
function handleUploadError(response) {
  proxy.$message.error("上传出错：" + response);
}

function handleClick(tab) {
  objDataCopy.value.repType = tab.paneName;
  data.uploadData.mediaType = objDataCopy.value.repType;
  const tempObjItem = data.tempObj.get(objDataCopy.value.repType);

  if (tempObjItem) {
    objDataCopy.value.repName = tempObjItem.repName ? tempObjItem.repName : null;
    objDataCopy.value.repMediaId = tempObjItem.repMediaId ? tempObjItem.repMediaId : null;
    objDataCopy.value.media_id = tempObjItem.media_id ? tempObjItem.media_id : null;
    objDataCopy.value.repUrl = tempObjItem.repUrl ? tempObjItem.repUrl : null;
    objDataCopy.value.content = tempObjItem.content ? tempObjItem.content : null;
    objDataCopy.value.repDesc = tempObjItem.repDesc ? tempObjItem.repDesc : null;
    proxy.$emit("update:objData", objDataCopy.value);
  } else {
    objDataCopy.value.repUrl = "";
    objDataCopy.value.repName = "";
    objDataCopy.value.repMediaId = "";
    objDataCopy.value.media_id = "";
    objDataCopy.value.content = "";
    objDataCopy.value.repDesc = "";
    proxy.$emit("update:objData", objDataCopy.value);
  }
}

function selectMaterial(item) {
  const tempObjItem = {};
  tempObjItem.repType = objDataCopy.value.repType;
  tempObjItem.repMediaId = item.mediaId;
  tempObjItem.media_id = item.mediaId;
  tempObjItem.content = item.content;
  data.dialogNewsVisible = false;
  data.dialogImageVisible = false;
  data.dialogVoiceVisible = false;
  data.dialogVideoVisible = false;
  objDataCopy.value.repMediaId = item.mediaId;
  objDataCopy.value.media_id = item.mediaId;
  objDataCopy.value.content = item.content;

  if (objDataCopy.value.repType == "music") {
    tempObjItem.repThumbMediaId = item.mediaId;
    tempObjItem.repThumbUrl = item.url;
    objDataCopy.value.repThumbMediaId = item.mediaId;
    objDataCopy.value.repThumbUrl = item.url;
    data.dialogThumbVisible = false;
  } else {
    tempObjItem.repName = item.name;
    tempObjItem.repUrl = item.url;
    objDataCopy.value.repName = item.name;
    objDataCopy.value.repUrl = item.url;
  }

  if (objDataCopy.value.repType == "video") {
    getMaterialVideo({
      mediaId: item.mediaId,
    }).then((response) => {
      if (response.code == 200) {
        objDataCopy.value.repName = response.data.title;
        objDataCopy.value.repDesc = response.data.description;
        objDataCopy.value.repUrl = response.data.downUrl;
        tempObjItem.repDesc = response.data.description;
        tempObjItem.repUrl = response.data.downUrl;
        data.tempObj.set(objDataCopy.value.repType, tempObjItem);
      }
    });
  }

  if (props.oneNews && objDataCopy.value.repType == "news" && item.content.articles.length > 1) {
    proxy.$alert("您选择的是多图文，微信限制只能回复1条图文消息，将默认选择第一篇	", "提示", {
      confirmButtonText: "确认",
    });
    item.content.articles = item.content.articles.slice(0, 1);
  }

  data.tempObj.set(objDataCopy.value.repType, tempObjItem);
}

function openMaterial() {
  if (objDataCopy.value.repType == "news") {
    data.dialogNewsVisible = true;
  } else if (objDataCopy.value.repType == "image") {
    data.dialogImageVisible = true;
  } else if (objDataCopy.value.repType == "voice") {
    data.dialogVoiceVisible = true;
  } else if (objDataCopy.value.repType == "video") {
    data.dialogVideoVisible = true;
  } else if (objDataCopy.value.repType == "music") {
    data.dialogThumbVisible = true;
  }
}

function getPageF(page, params) {
  data.tableLoading = true;
  getPage(
    Object.assign(
      {
        current: page.currentPage,
        size: page.pageSize,
        type: objDataCopy.value.repType,
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

function handleError(err) {
  try {
    let errorMsg = err.message ? JSON.parse(err.message).msg : err.message;
    proxy.$message.error(errorMsg);
  } catch (e) {}
}

function handleSuccess(response) {
  objDataCopy.value.repUrl = response.link;
}

function handleSuccess2(response) {
  objDataCopy.value.repHqUrl = response.link;
}
</script>
<style lang="scss" scoped>
// .public-account-management {
// .el-input {
// width: 70%;
// margin-right: 2%;
// }
// }

.pagination {
  margin-right: 25px;
  text-align: right;
}

.select-item {
  width: 280px;
  padding: 10px;
  margin: 0 auto 10px;
  border: 1px solid #eaeaea;
}

.select-item2 {
  padding: 10px;
  margin: 0 auto 10px;
  border: 1px solid #eaeaea;
}

.ope-row {
  padding-top: 10px;
  text-align: center;
  display: flex;
  justify-content: center;
}

.item-name {
  overflow: hidden;
  font-size: 12px;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.el-form-item__content {
  line-height: unset !important;
}

.col-select {
  width: 49.5%;
  height: 160px;
  padding: 50px 0;
  border: 1px solid rgb(234 234 234);
}

.col-select2 {
  height: 160px;
  padding: 50px 0;
  border: 1px solid rgb(234 234 234);
}

.col-add {
  float: right;
  width: 49.5%;
  height: 160px;
  padding: 50px 0;
  border: 1px solid rgb(234 234 234);
}

.avatar-uploader-icon {
  width: 60px !important;
  height: 60px !important;
  font-size: 28px;
  line-height: 100px !important;
  color: #8c939d;
  text-align: center;
  border: 1px solid #d9d9d9;
}

.material-img {
  width: 100%;
}

.thumb-div {
  display: inline-block;
  text-align: center;
}

.item-infos {
  width: 30%;
  margin: auto;
}

.flex-c {
  display: flex;
  justify-content: center;
  align-items: center;
}
.w-full {
  width: 100%;
}
</style>

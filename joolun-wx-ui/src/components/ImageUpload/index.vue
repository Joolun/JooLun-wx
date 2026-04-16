<template>
  <div class="component-upload-image">
    <el-upload
      ref="imageUploadRef"
      :multiple="limit > 1"
      :disabled="disabled"
      :action="uploadImgUrl"
      list-type="picture-card"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :before-remove="handleDelete"
      :show-file-list="true"
      :headers="headers"
      :file-list="fileList"
      :on-preview="handlePictureCardPreview"
      :class="{ hide: fileList.length >= limit }"
    >
      <el-icon class="avatar-uploader-icon"><plus /></el-icon>
    </el-upload>

    <!-- 上传说明 -->
    <div v-if="showTip" class="el-upload__tip">
      请上传
      <template v-if="fileSize">
        大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b>
      </template>
      <template v-if="fileType">
        格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b>
      </template>
      的图片文件
    </div>

    <el-dialog v-model="dialogVisible" title="预览" width="800px" append-to-body>
      <img
        :src="dialogImageUrl"
        style="display: block; max-width: 100%; margin: 0 auto"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import { getToken } from "@/utils/auth";

const props = defineProps({
  modelValue: [String, Object, Array],
  // 图片数量限制
  limit: {
    type: Number,
    default: 5,
  },
  // 图片大小限制，单位 MB
  fileSize: {
    type: Number,
    default: 5,
  },
  // 允许上传的图片类型
  fileType: {
    type: Array,
    default: () => ["png", "jpg", "jpeg"],
  },
  // 是否显示上传提示
  isShowTip: {
    type: Boolean,
    default: true,
  },
  // 是否禁用上传，仅允许预览
  disabled: {
    type: Boolean,
    default: false,
  },
  /**
   * 返回值类型
   * string: "url,url"
   * array: ["url", "url"]
   */
  returnType: {
    type: String,
    default: "string",
  },
});

const { proxy } = getCurrentInstance();
const emit = defineEmits();
const imageUploadRef = ref();
const number = ref(0);
const uploadList = ref([]);
const dialogImageUrl = ref("");
const dialogVisible = ref(false);
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const uploadImgUrl = ref(`${import.meta.env.VITE_APP_BASE_API}/common/upload`);
const headers = ref({ Authorization: `Bearer ${getToken()}` });
const fileList = ref([]);
const showTip = computed(
  () => props.isShowTip && (props.fileType || props.fileSize)
);

watch(
  () => props.modelValue,
  (val) => {
    fileList.value = normalizeFileList(val);
  },
  { deep: true, immediate: true }
);

// 统一整理外部传入值，只保留已经上传成功的正式图片地址。
function normalizeFileList(val) {
  if (!val) {
    return [];
  }
  const list = Array.isArray(val)
    ? val
    : typeof val === "object"
      ? [val]
      : String(val).split(",");
  let temp = 1;
  return list
    .filter((item) => !!item)
    .map((item) => {
      if (typeof item === "string") {
        if (item.indexOf(baseUrl) === -1 && !item.startsWith("http")) {
          item = { name: `${baseUrl}${item}`, url: `${baseUrl}${item}` };
        } else {
          item = { name: item, url: item };
        }
      } else {
        item = { ...item };
      }
      item.uid = item.uid || `${Date.now()}_${temp++}`;
      return item;
    })
    .filter((item) => isPersistedFile(item));
}

// 只有真正的服务端图片地址才允许进入最终列表，临时 blob 地址和失败占位项都要过滤掉。
function isPersistedFile(file) {
  return !!file?.url && file.url.indexOf("blob:") !== 0;
}

// 从当前文件列表中移除指定文件，优先按 uid 匹配，兜底再按名称和地址匹配。
function removeFileItem(file) {
  if (!file) {
    return;
  }
  fileList.value = fileList.value.filter((item) => {
    if (file.uid && item.uid) {
      return item.uid !== file.uid;
    }
    return item.name !== file.name && item.url !== file.url;
  });
}

// 上传前校验图片格式和大小。
function handleBeforeUpload(file) {
  let isImg = false;
  if (props.fileType.length) {
    let fileExtension = "";
    if (file.name.lastIndexOf(".") > -1) {
      fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
    }
    isImg = props.fileType.some((type) => {
      if (file.type.indexOf(type) > -1) {
        return true;
      }
      if (fileExtension && fileExtension.indexOf(type) > -1) {
        return true;
      }
      return false;
    });
  } else {
    isImg = file.type.indexOf("image") > -1;
  }
  if (!isImg) {
    proxy.$modal.msgError(
      `文件格式不正确，请上传 ${props.fileType.join("/")} 格式的图片文件`
    );
    return false;
  }
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize;
    if (!isLt) {
      proxy.$modal.msgError(`上传图片大小不能超过 ${props.fileSize} MB`);
      return false;
    }
  }
  proxy.$modal.loading("正在上传图片，请稍候...");
  number.value++;
  return true;
}

// 上传数量超出限制时给出提示。
function handleExceed() {
  proxy.$modal.msgError(`上传图片数量不能超过 ${props.limit} 张`);
}

// 上传成功时先暂存本次返回的正式图片地址，等当前批次处理完成后再统一回写。
function handleUploadSuccess(res, file) {
  if (res.code === 200) {
    uploadList.value.push({
      uid: file.uid,
      name: res.fileName,
      url: res.fileName,
    });
    uploadedSuccessfully();
    return;
  }
  handleUploadFailure(file, res.msg || "上传图片失败");
}

// 上传失败时回收上传计数，并清理失败文件卡片，避免单图组件出现失败后再次上传变成多图的问题。
function handleUploadError(err, file) {
  handleUploadFailure(file, "上传图片失败");
}

function handleUploadFailure(file, message) {
  if (number.value > 0) {
    number.value--;
  }
  removeFileItem(file);
  if (typeof imageUploadRef.value?.handleRemove === "function" && file) {
    const removeResult = imageUploadRef.value.handleRemove(file);
    if (typeof removeResult?.catch === "function") {
      removeResult.catch(() => {});
    }
  }
  proxy.$modal.msgError(message);
  uploadedSuccessfully();
}

// 删除图片时由组件自身维护 fileList，并主动把结果回写到外层表单。
function handleDelete(file) {
  removeFileItem(file);
  emitModelValue();
  return false;
}

// 当前批次全部处理完成后，再合并正式图片地址，确保最终值里没有失败项和临时项。
function uploadedSuccessfully() {
  if (number.value === 0) {
    uploadList.value = [];
    proxy.$modal.closeLoading();
    return;
  }
  if (uploadList.value.length !== number.value) {
    return;
  }
  const mergedList = fileList.value
    .filter((item) => isPersistedFile(item))
    .concat(uploadList.value);
  fileList.value = props.limit === 1 ? mergedList.slice(-1) : mergedList;
  uploadList.value = [];
  number.value = 0;
  emitModelValue();
  proxy.$modal.closeLoading();
}

// 图片预览。
function handlePictureCardPreview(file) {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
}

// 统一按组件配置向外同步 string 或 array，避免不同分支重复处理。
function emitModelValue() {
  if (props.returnType === "array") {
    emit("update:modelValue", listToArray(fileList.value));
  } else {
    emit("update:modelValue", listToString(fileList.value));
  }
}

// 图片对象数组转逗号分隔字符串。
function listToString(list, separator) {
  let strs = "";
  separator = separator || ",";
  for (let i in list) {
    if (isPersistedFile(list[i])) {
      strs += list[i].url.replace(baseUrl, "") + separator;
    }
  }
  return strs !== "" ? strs.substr(0, strs.length - 1) : "";
}

// 图片对象数组转图片地址数组。
function listToArray(list) {
  const urls = [];
  for (let i in list) {
    if (isPersistedFile(list[i])) {
      urls.push(list[i].url.replace(baseUrl, ""));
    }
  }
  return urls;
}
</script>

<style scoped lang="scss">
/* 达到上限后隐藏上传按钮 */
:deep(.hide .el-upload--picture-card) {
  display: none;
}
</style>

<template>
  <div>
    <avue-crud
      ref="crud"
      :page="page"
      :data="tableData"
      :table-loading="tableLoading"
      :option="tableOption"
      :permission="permissionList"
      @on-load="getPageF"
      @refresh-change="refreshChange"
      @row-update="handleUpdate"
      @row-save="handleSave"
      @row-del="handleDel"
      @sort-change="sortChange"
      @search-change="searchChange"
      @selection-change="selectionChange">
      <template #subscribe="scope">
        <el-tag
          size="small"
          effect="dark"
          :type="scope.row.subscribe == '1' ? 'success' : scope.row.subscribe == '0' ? 'danger' : 'warning'">
          {{ scope.row.$subscribe }}
        </el-tag>
      </template>
      <template #sex="scope">
        <el-tag
          size="small"
          effect="light"
          :type="scope.row.sex == '1' ? 'success' : scope.row.sex == '2' ? 'danger' : 'warning'">
          {{ scope.row.$sex || "未知" }}
        </el-tag>
      </template>
      <template #latitude="scope">
        <el-link
          v-if="scope.row.longitude"
          type="primary"
          target="_blank"
          :href="'https://map.qq.com/?type=marker&isopeninfowin=1&markertype=1&pointx=' + scope.row.longitude + '&pointy=' + scope.row.latitude + '&name=' + scope.row.nickName + '&ref=joolun'">
          <i class="el-icon-map-location"></i>
        </el-link>
      </template>
      <template #menu-left>
        <el-button
          type="success"
          @click="
            dialogTagging = true;
            taggingType = 'tagging';
          "
          icon="el-icon-document"
          v-hasPermi="['wxmp:wxuser:tagging']"
          >打标签
        </el-button>
        <el-button
          type="warning"
          @click="
            dialogTagging = true;
            taggingType = 'unTagging';
          "
          icon="el-icon-document"
          v-hasPermi="['wxmp:wxuser:tagging']"
          >去除标签
        </el-button>
        <el-button
          type="danger"
          @click="synchroWxUserF"
          icon="el-icon-refresh"
          v-hasPermi="['wxmp:wxuser:synchro']"
          >同步用户
        </el-button>
        <el-dialog
          :title="taggingType == 'tagging' ? '请选择要打的标签' : '请选择要去除的标签'"
          v-model="dialogTagging"
          width="30%">
          <el-checkbox-group v-model="checkedTags">
            <el-checkbox
              v-for="tag in userTagsData"
              :label="tag.id"
              :key="tag.id"
              >{{ tag.name }}
            </el-checkbox>
          </el-checkbox-group>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogTagging = false">取 消</el-button>
              <el-button
                type="primary"
                @click="toTagging"
                >确 定</el-button
              >
            </span>
          </template>
        </el-dialog>
      </template>
      <template #menu="scope">
        <el-button
          link
          v-hasPermi="['wxmp:wxuser:edit:remark']"
          icon="el-icon-edit"
          type="primary"
          plain
          @click="updateRemarkF(scope.row, scope.index)"
          >修改备注
        </el-button>
        <el-button
          link
          v-hasPermi="['wxmp:wxuser:index']"
          icon="el-icon-chat-line-round"
          type="primary"
          plain
          @click="wxMsgDo(scope.row, scope.index)"
          >消息
        </el-button>
      </template>
      <template #tagidListSearch="scope">
        <el-select
          v-model="scope.row.tagidList"
          placeholder="请选择">
          <el-option
            v-for="item in userTagsData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </template>
    </avue-crud>
    <el-dialog
      title="用户消息"
      v-model="dialogMsgVisible"
      width="700px">
      <WxMsg
        :wxUserId="wxUserId"
        v-if="dialogMsgVisible"></WxMsg>
    </el-dialog>
  </div>
</template>

<script setup name="WxMpUser">
import { checkPermi, checkRole } from "@/utils/permission";
import { getPage, getObj, addObj, putObj, delObj, synchroWxUser, updateRemark, tagging } from "@/api/wxmp/wxuser";
import { getList as listUserTags } from "@/api/wxmp/wxusertags";
import { tableOption } from "@/const/crud/wxmp/wxuser";
import WxMsg from "@/components/wx-msg/main.vue";
const { proxy } = getCurrentInstance();

const crud = ref(null);

const data = reactive({
  tableData: [],
  page: {
    total: 0, // 总页数
    currentPage: 1, // 当前页数
    pageSize: 20, // 每页显示多少条
    ascs: [],
    descs: "subscribe_time",
  },
  paramsSearch: {},
  tableLoading: false,
  selectionData: [],
  dialogTagging: false,
  checkedTags: [],
  userTagsData: [],
  taggingType: "",
  dialogMsgVisible: false,
  wxUserId: "",
});
const { tableData, tableLoading, page, dialogTagging, checkedTags, userTagsData, taggingType, dialogMsgVisible, wxUserId } = toRefs(data);

const permissionList = computed(() => {
  return {
    addBtn: checkPermi(["wxmp:wxuser:add"]),
    delBtn: checkPermi(["wxmp:wxuser:del"]),
    editBtn: checkPermi(["wxmp:wxuser:edit"]),
    viewBtn: checkPermi(["wxmp:wxuser:get"]),
  };
});

onMounted(() => {
  listUserTagsF();
});

function listUserTagsF() {
  data.tableLoading = true;
  listUserTags()
    .then((response) => {
      if (response.code == 200) {
        let userTagsData = response.data;
        data.userTagsData = userTagsData;
        crud.value.DIC.tagidList = userTagsData;
      } else {
        proxy.$message.error("获取用户标签出错：" + response.msg);
      }
      data.tableLoading = false;
      getPageF(data.page);
    })
    .catch(() => {
      data.tableLoading = false;
      getPageF(data.page);
    });
}

function wxMsgDo(row) {
  data.wxUserId = row.id;
  data.dialogMsgVisible = true;
}

function toTagging() {
  let openIdList = [];
  for (let i = 0; i < data.selectionData.length; i++) {
    openIdList.push(data.selectionData[i].openId);
  }
  if (data.checkedTags.length <= 0) {
    proxy.$message.error("请选择标签");
    return;
  }
  if (openIdList.length <= 0) {
    proxy.$message.error("请选择用户");
    return;
  }
  data.tableLoading = true;
  tagging({
    taggingType: data.taggingType,
    tagIds: data.checkedTags,
    openIds: openIdList,
  })
    .then((response) => {
      data.tableLoading = false;
      data.checkedTags = [];
      crud.value.selectClear();
      if (response.code == 200) {
        getPageF(data.page);
      } else {
        proxy.$message.error("打标签出错：" + response.msg);
      }
    })
    .catch(() => {
      data.tableLoading = false;
    });
  data.dialogTagging = false;
}

function selectionChange(list) {
  data.selectionData = list;
}

function searchChange(params, done) {
  params = proxy.filterForm(params);
  data.paramsSearch = params;
  data.page.currentPage = 1;
  getPageF(data.page, params);
  done();
}

function synchroWxUserF() {
  proxy
    .$confirm("同步用户需要一定时间，用户量越大、用时越久，请耐心等待，勿重复提交；确认此操作吗?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      data.tableLoading = true;
      synchroWxUser()
        .then((response) => {
          data.tableLoading = false;
          if (response.code == 200) {
            getPageF(data.page);
          } else {
            proxy.$message.error("同步微信用户出错：" + response.msg);
          }
        })
        .catch(() => {
          data.tableLoading = false;
        });
      setTimeout(() => {
        if (data.tableLoading) {
          data.tableLoading = false;
          proxy.$alert("同步请求发送成功，系统正在处理中，请稍后刷新查看同步结果，用户量越大用时越久，请耐心等待，勿重复提交", "提示", {
            confirmButtonText: "确定",
          });
        }
      }, 3000);
    })
    .catch(() => {});
}

function sortChange(val) {
  let prop = val.prop ? val.prop.replace(/([A-Z])/g, "_$1").toLowerCase() : "";
  if (val.order == "ascending") {
    data.page.descs = [];
    data.page.ascs = prop;
  } else if (val.order == "descending") {
    data.page.ascs = [];
    data.page.descs = prop;
  } else {
    data.page.ascs = [];
    data.page.descs = [];
  }
  getPageF(data.page);
}

function getPageF(page, params) {
  data.tableLoading = true;
  getPage(
    Object.assign(
      {
        current: page.currentPage,
        size: page.pageSize,
        descs: data.page.descs,
        ascs: data.page.ascs,
        appType: "2",
      },
      params,
      data.paramsSearch
    )
  )
    .then((response) => {
      data.tableData = response.data.records;
      data.page.total = response.data.total;
      data.page.currentPage = page.currentPage;
      data.page.pageSize = page.pageSize;
      data.tableLoading = false;
    })
    .catch(() => {
      data.tableLoading = false;
    });
}

function updateRemarkF(row, index) {
  proxy
    .$prompt("请输入备注", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      inputPattern: /\S/,
      inputErrorMessage: "输入不能为空",
    })
    .then(({ value }) => {
      data.tableLoading = true;
      row.remark = value;
      updateRemark(row)
        .then((response) => {
          data.tableLoading = false;
          if (response.code == 200) {
            data.tableData.splice(index, 1, Object.assign({}, row));
            proxy.$message({
              showClose: true,
              message: "修改成功",
              type: "success",
            });
            getPageF(data.page);
          } else {
            proxy.$message.error(response.msg);
          }
        })
        .catch(() => {
          data.tableLoading = false;
        });
    });
}

function handleDel(row, index) {
  proxy
    .$confirm("是否确认删除", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(function () {
      return delObj(row.id);
    })
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "删除成功",
        type: "success",
      });
      getPageF(data.page);
    })
    .catch(function (err) {});
}

function handleUpdate(row, index, done, loading) {
  putObj(row)
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "修改成功",
        type: "success",
      });
      done();
      getPageF(data.page);
    })
    .catch(() => {
      loading();
    });
}

function handleSave(row, done, loading) {
  addObj(row)
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "添加成功",
        type: "success",
      });
      done();
      getPageF(data.page);
    })
    .catch(() => {
      loading();
    });
}

function refreshChange(page) {
  getPageF(data.page);
}
</script>

<style lang="scss" scoped></style>

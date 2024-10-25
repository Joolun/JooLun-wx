<template>
  <div>
    <avue-crud
      ref="crud"
      :page="page"
      :data="tableData"
      :table-loading="tableLoading"
      :option="tableOption"
      @on-load="getPage"
      @refresh-change="refreshChange"
      @row-update="handleUpdate"
      @row-save="handleSave"
      @row-del="handleDel"
      @sort-change="sortChange"
      @search-change="searchChange"
    >
    </avue-crud>
    <el-dialog title="用户消息" v-model="dialogMsgVisible" width="40%">
      <WxMsg :wxUserId="wxUserId" v-if="dialogMsgVisible"></WxMsg>
    </el-dialog>
  </div>
</template>

<script setup name="WxMpUserTags">
import WxMsg from "@/components/wx-msg/main.vue";
import { getList, addObj, putObj, delObj } from "@/api/wxmp/wxusertags";
import { tableOption } from "@/const/crud/wxmp/wxusertags";
import { checkPermi, checkRole } from "@/utils/permission";
const { proxy } = getCurrentInstance();

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
  dialogMsgVisible: false,
  wxUserId: "",
});

const { page, tableData, tableLoading, dialogMsgVisible, wxUserId } =
  toRefs(data);
const permissionList = computed(() => {
  return {
    addBtn: checkPermi(["wxmp:wxusertags:add"]),
    delBtn: checkPermi(["wxmp:wxusertags:del"]),
    editBtn: checkPermi(["wxmp:wxusertags:edit"]),
    viewBtn: checkPermi(["wxmp:wxusertags:get"]),
  };
});

function searchChange(params, done) {
  params = proxy.filterForm(params);
  data.paramsSearch = params;
  data.page.currentPage = 1;
  getPage(data.page, params);
  done();
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
  getPage(data.page);
}

function getPage(page, params) {
  data.tableLoading = true;
  getList(Object.assign({}, params, data.paramsSearch))
    .then((response) => {
      data.tableData = response.data;
      data.tableLoading = false;
    })
    .catch(() => {
      data.tableLoading = false;
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
      return delObj({
        id: row.id,
      });
    })
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "删除成功",
        type: "success",
      });
      getPage(data.page);
    })
    .catch(function (err) {});
}

function handleUpdate(row, index, done, loading) {
  putObj(Object.assign({}, row))
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "修改成功",
        type: "success",
      });
      done();
      getPage(data.page);
    })
    .catch(() => {
      loading();
    });
}

function handleSave(row, done, loading) {
  addObj(Object.assign({}, row))
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "添加成功",
        type: "success",
      });
      done();
      getPage(data.page);
    })
    .catch(() => {
      loading();
    });
}

function refreshChange(page) {
  getPage(data.page);
}
</script>

<style lang="scss" scoped></style>

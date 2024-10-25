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
      <template #sex="scope">
        <el-tag
          size="small"
          effect="light"
          :type="scope.row.sex == '1' ? 'success' : scope.row.sex == '2' ? 'danger' : 'warning'">
          {{ scope.row.$sex || "未知" }}
        </el-tag>
      </template>
    </avue-crud>
  </div>
</template>

<script setup name="WxMaUser">
import { checkPermi, checkRole } from "@/utils/permission";
import { getPage, addObj, putObj, delObj } from "@/api/wxma/wxuser";
import { tableOption } from "@/const/crud/wxma/wxuser";

const { proxy } = getCurrentInstance();

const data = reactive({
  tableData: [],
  page: {
    total: 0, // 总页数
    currentPage: 1, // 当前页数
    pageSize: 20, // 每页显示多少条
    ascs: [],
    descs: "create_time",
  },
  paramsSearch: {},
  tableLoading: false,
  selectionData: [],
  dialogTagging: false,
});
const { page, tableData, tableLoading } = toRefs(data);

const permissionList = computed(() => {
  return {
    addBtn: checkPermi(["wxma:wxuser:add"]),
    delBtn: checkPermi(["wxma:wxuser:del"]),
    editBtn: checkPermi(["wxma:wxuser:edit"]),
    viewBtn: checkPermi(["wxma:wxuser:get"]),
  };
});

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
        appType: "1",
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

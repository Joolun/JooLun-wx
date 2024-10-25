<!--
  - Copyright (C) 2024
  - All rights reserved, Designed By www.joolun.com
-->
<template>
  <div>
    <avue-crud
      ref="crud"
      v-model="form"
      :data="tableData"
      :table-loading="tableLoading"
      :permission="permissionList"
      :option="tableOption"
      @on-load="getPageF"
      @refresh-change="refreshChange"
      @row-update="handleUpdate"
      @row-save="handleSave"
      @row-del="handleDel"
    >
      <template #enable="scope">
        <el-switch
          active-value="1"
          inactive-value="0"
          v-model="scope.row.enable"
          style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
          inline-prompt
          active-icon="Check"
          inactive-icon="Close"
          @change="changeEnable(scope.row)"
        >
        </el-switch>
      </template>
      <template #picUrl-form="scope">
        <ImageUpload
          :limit="1"
          :disabled="scope.type == 'view'"
          v-model="form.picUrl"
        />
      </template>
    </avue-crud>
  </div>
</template>
<script setup name="GoodsCategory">
import { checkPermi, checkRole } from "@/utils/permission";
import {
  getPage,
  fetchTree,
  getObj,
  addObj,
  putObj,
  delObj,
} from "@/api/mall/goodscategory";
import { tableOption } from "@/const/crud/mall/goodscategory";

const { proxy } = getCurrentInstance();
const crud = ref(null);
const data = reactive({
  form: {},
  tableData: [],
  tableLoading: false,
  page: {
    total: 0, // 总页数
    currentPage: 1, // 当前页数
    pageSize: 20, // 每页显示多少条
    ascs: [], //升序字段
    descs: "create_time", //降序字段
  },
});
const permissionList = computed(() => {
  return {
    addBtn: checkPermi(["mall:goodscategory:add"]),
    delBtn: checkPermi(["mall:goodscategory:del"]),
    editBtn: checkPermi(["mall:goodscategory:edit"]),
    viewBtn: checkPermi(["mall:goodscategory:get"]),
  };
});
const { form, tableData, tableLoading } = toRefs(data);

// 启用禁用
function changeEnable(row) {
  if (row && row.id) {
    putObj({
      id: row.id,
      enable: row.enable,
    }).then(() => {});
  }
}

// 查询所有树形结构分类数据
function getPageF() {
  data.tableLoading = true;
  fetchTree()
    .then((response) => {
      let tableData = response.data;
      data.tableData = tableData;
      let parentIdDIC = [
        {
          id: "0",
          name: "顶级分类",
          parentId: "0",
        },
      ];
      tableData.forEach((item) => {
        parentIdDIC.push({
          id: item.id,
          name: item.name,
          parentId: item.parentId,
        });
      });
      crud.value.DIC.parentId = parentIdDIC;
      data.tableLoading = false;
    })
    .catch(() => {
      data.tableLoading = false;
    });
}

function handleDel(row, index) {
  proxy
    .$confirm("是否确认删除此数据", "提示", {
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

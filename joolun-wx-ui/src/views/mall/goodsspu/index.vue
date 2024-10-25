<!--
  - Copyright (C) 2024
  - All rights reserved, Designed By www.joolun.com
-->
<template>
  <div>
    <avue-crud
      ref="crud"
      v-model="form"
      :page="page"
      :data="tableData"
      :table-loading="tableLoading"
      :option="tableOption"
      :permission="permissionList"
      :before-open="beforeOpen"
      @on-load="getPageF"
      @refresh-change="refreshChange"
      @row-update="handleUpdate"
      @row-save="handleSave"
      @row-del="handleDel"
      @sort-change="sortChange"
      @search-change="searchChange"
      @selection-change="selectionChange"
    >
      <template #menu-left>
        <el-button
          v-if="checkPermi(['mall:goodsspu:edit'])"
          type="success"
          @click="batchShelf('1')"
          icon="el-icon-document"
          >批量上架</el-button
        >
        <el-button
          v-if="checkPermi(['mall:goodsspu:edit'])"
          type="warning"
          @click="batchShelf('0')"
          icon="el-icon-document"
          >批量下架</el-button
        >
      </template>
      <template #salesPrice="scope">
        <div style="color: red">￥{{ scope.row.salesPrice }}</div>
      </template>
      <template #shelf="scope">
        <el-switch
          active-value="1"
          inactive-value="0"
          v-model="scope.row.shelf"
          style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
          inline-prompt
          active-icon="Check"
          inactive-icon="Close"
          @change="changeShelf(scope.row)"
        >
        </el-switch>
      </template>
      <template #description-form>
        <BaseEditor v-model="form.description" :minHeight="300" />
      </template>
      <template #picUrls-form="scope">
        <ImageUpload
          :limit="5"
          :disabled="scope.type == 'view'"
          returnType="array"
          v-model="form.picUrls"
        />
      </template>
      <template #picUrls="scope">
        <el-image
          style="width: 100px; height: 100px"
          :src="scope.row.picUrls ? scope.row.picUrls[0] : ''"
        >
        </el-image>
      </template>
    </avue-crud>
  </div>
</template>

<script setup name="GoodsSpu">
import { checkPermi, checkRole } from "@/utils/permission";
import {
  getPage,
  getObj,
  addObj,
  putObj,
  delObj,
  putObjShelf,
} from "@/api/mall/goodsspu";
import { tableOption } from "@/const/crud/mall/goodsspu";
import BaseEditor from "@/components/Editor/index.vue";

const { proxy } = getCurrentInstance();

const data = reactive({
  form: {},
  tableData: [],
  page: {
    total: 0, // 总页数
    currentPage: 1, // 当前页数
    pageSize: 20, // 每页显示多少条
    ascs: [], //升序字段
    descs: "create_time", //降序字段
  },
  paramsSearch: {},
  tableLoading: false,
  dialogAppraises: false,
  optionAppraises: {
    props: {
      avatar: "nickName",
      author: "headimgUrl",
      body: "content",
    },
  },
  selectionData: [],
  pointsConfig: null,
});
const { form, page, tableData, tableLoading } = toRefs(data);

const permissionList = computed(() => {
  return {
    addBtn: checkPermi(["mall:goodsspu:add"]),
    delBtn: checkPermi(["mall:goodsspu:del"]),
    editBtn: checkPermi(["mall:goodsspu:edit"]),
    viewBtn: checkPermi(["mall:goodsspu:get"]),
  };
});

function selectionChange(list) {
  data.selectionData = list;
}

function batchShelf(shelf) {
  if (data.selectionData.length <= 0) {
    proxy.$message.error("请选择商品");
    return;
  }
  let selectionIds = "";
  data.selectionData.forEach((item) => {
    selectionIds += item.id + ",";
  });
  putObjShelfF(selectionIds, shelf);
}

function changeShelf(row) {
  if (row && row.id) putObjShelfF(row.id, row.shelf);
}

function putObjShelfF(ids, shelf) {
  putObjShelf({
    ids: ids,
    shelf: shelf,
  })
    .then((response) => {
      getPageF(data.page);
    })
    .catch(() => {
      getPageF(data.page);
    });
}

function beforeOpen(done, type) {
  if (type == "add") {
    done();
  } else if (type == "edit") {
    data.tableLoading = true;
    getObj(data.form.id)
      .then((response) => {
        data.form["description"] = response.data.description;
        data.tableLoading = false;
        done();
      })
      .catch(() => {
        data.tableLoading = false;
        done();
      });
  }
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
  if (data.paramsSearch.categoryId) {
    data.paramsSearch.categoryFirst = data.paramsSearch.categoryId[0];
    data.paramsSearch.categorySecond = data.paramsSearch.categoryId[1];
  }
  getPage(
    Object.assign(
      {
        current: page.currentPage,
        size: page.pageSize,
        descs: data.page.descs,
        ascs: data.page.ascs,
      },
      params,
      data.paramsSearch
    )
  )
    .then((response) => {
      let tableData = response.data.records ? response.data.records : [];
      tableData.forEach(function (item, index) {
        let categoryId = [];
        if (item.categoryFirst) {
          categoryId.push(item.categoryFirst);
        }
        if (item.categorySecond) {
          categoryId.push(item.categorySecond);
        }
        item.categoryId = categoryId;
      });
      data.tableData = tableData;
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
  row.categoryFirst = row.categoryId[0];
  row.categorySecond = row.categoryId[1] ? row.categoryId[1] : "";
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
  row.categoryFirst = row.categoryId[0];
  row.categorySecond = row.categoryId[1] ? row.categoryId[1] : "";
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

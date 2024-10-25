<template>
  <div class="app-container">
    <el-tabs
      v-model="type"
      @tab-click="handleClick">
      <el-tab-pane name="1">
        <template #label>
          <span>关注时回复</span>
        </template>
        <avue-crud
          style="padding: 0"
          ref="crud"
          :page="page"
          :data="tableData"
          :permission="permissionList"
          :table-loading="tableLoading"
          :option="tableOption1"
          @refresh-change="refreshChange">
          <template #menu-left="">
            <div
              class="add_but"
              v-if="tableData.length <= 0">
              <el-button
                type="primary"
                icon="Plus"
                @click="handleAdd"
                >新 增</el-button
              >
            </div>
          </template>
          <template #menu="scope">
            <el-button
              link
              icon="el-icon-edit"
              plain
              type="primary"
              @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-button
              link
              icon="el-icon-delete"
              type="danger"
              plain
              @click="handleDel(scope.row)"
              >删除</el-button
            >
          </template>
        </avue-crud>
      </el-tab-pane>
      <el-tab-pane name="2">
        <template #label>
          <span><i class="el-icon-chat-line-round"></i> 消息回复</span>
        </template>
        <avue-crud
          ref="crud"
          :page="page"
          :data="tableData"
          :permission="permissionList"
          :table-loading="tableLoading"
          :option="tableOption2"
          @refresh-change="refreshChange"
          @sort-change="sortChange">
          <template #menu-left>
            <div class="add_but">
              <el-button
                type="primary"
                icon="Plus"
                @click="handleAdd"
                >新 增</el-button
              >
            </div>
          </template>
          <template #menu="scope">
            <el-button
              link
              icon="el-icon-edit"
              type="primary"
              plain
              @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-button
              link
              icon="el-icon-delete"
              type="danger"
              plain
              @click="handleDel(scope.row)"
              >删除</el-button
            >
          </template>
        </avue-crud>
      </el-tab-pane>
      <el-tab-pane name="3">
        <template #label>
          <span><i class="el-icon-news"></i> 关键词回复</span>
        </template>
        <avue-crud
          ref="crud"
          :page="page"
          :data="tableData"
          :permission="permissionList"
          :table-loading="tableLoading"
          :option="tableOption3"
          @on-load="getPageF"
          @refresh-change="refreshChange"
          @sort-change="sortChange"
          @search-change="searchChange">
          <template #menu-left>
            <div class="add_but">
              <el-button
                type="primary"
                icon="Plus"
                @click="handleAdd"
                >新 增</el-button
              >
            </div>
          </template>
          <template #menu="scope">
            <el-button
              link
              icon="el-icon-edit"
              type="primary"
              plain
              @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-button
              link
              icon="el-icon-delete"
              type="danger"
              plain
              @click="handleDel(scope.row)"
              >删除</el-button
            >
          </template>
        </avue-crud>
      </el-tab-pane>
    </el-tabs>
    <el-dialog
      :title="handleType == 'add' ? '新增回复消息' : '修改回复消息'"
      v-model="dialog1Visible"
      width="50%">
      <el-form label-width="100px">
        <el-form-item
          label="请求消息类型"
          v-if="type == '2'">
          <el-select
            v-model="objData.reqType"
            placeholder="请选择">
            <template
              v-for="item in dictData.get('wx_req_type')"
              :key="item.value">
              <el-option
                v-if="item.value !== 'event'"
                :label="item.label"
                :value="item.value"
                :disabled="item.disabled" />
            </template>
          </el-select>
        </el-form-item>
        <el-form-item
          label="匹配类型"
          v-if="type == '3'">
          <el-select
            v-model="objData.repMate"
            placeholder="请选择"
            style="width: 100px">
            <el-option
              v-for="item in dictData.get('wx_rep_mate')"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="关键词"
          v-if="type == '3'">
          <el-input
            placeholder="请输入内容"
            v-model="objData.reqKey"
            clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="回复消息">
          <div
            v-if="hackResetWxReplySelect"
            style="width: 100%">
            <WxReplySelect :objData="objData"></WxReplySelect>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialog1Visible = false">取 消</el-button>
          <el-button
            type="primary"
            @click="handleSubmit"
            >确 定</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="WxMpAutoreply">
/**
 * 消息自动回复
 */
import { getPage, getObj, addObj, putObj, delObj } from "@/api/wxmp/wxautoreply";
import { tableOption1, tableOption2, tableOption3 } from "@/const/crud/wxmp/wxautoreply";
import WxReplySelect from "@/components/wx-reply/main.vue";
import { checkPermi, checkRole } from "@/utils/permission";
import { computed, getCurrentInstance, nextTick, onMounted, reactive } from "vue";

const { proxy } = getCurrentInstance();

const data = reactive({
  tableData: [],
  page: {
    total: 0, // 总页数
    currentPage: 1, // 当前页数
    pageSize: 20, // 每页显示多少条
    ascs: [], //升序字段
    descs: [], //降序字段
  },
  paramsSearch: {},
  tableLoading: false,
  dialog1Visible: false,
  type: "1", //类型（1、关注时回复；2、消息回复；3、关键词回复）
  objData: {
    repType: "text",
  },
  handleType: null,
  dictData: new Map(),
  hackResetWxReplySelect: false,
});

const { tableData, tableLoading, page, dialog1Visible, objData, type, handleType, dictData, hackResetWxReplySelect } = toRefs(data);
const permissionList = computed(() => {
  return {
    addBtn: checkPermi(["wxmp:wxautoreply:add"]),
    delBtn: checkPermi(["wxmp:wxautoreply:del"]),
    editBtn: checkPermi(["wxmp:wxautoreply:edit"]),
    viewBtn: checkPermi(["wxmp:wxautoreply:get"]),
  };
});

function onCreated() {
  getPageF(data.page);
  data.dictData.set("wx_rep_mate", [
    {
      value: "1",
      label: "全匹配",
    },
    {
      value: "2",
      label: "半匹配",
    },
  ]);
  data.dictData.set("wx_req_type", [
    {
      value: "text",
      label: "文本",
    },
    {
      value: "image",
      label: "图片",
    },
    {
      value: "voice",
      label: "语音",
    },
    {
      value: "video",
      label: "视频",
    },
    {
      value: "shortvideo",
      label: "小视频",
    },
    {
      value: "location",
      label: "地理位置",
    },
    {
      value: "link",
      label: "链接消息",
    },
    {
      value: "event",
      label: "事件推送",
    },
  ]);
}
onCreated();

onMounted(() => {});

function handleAdd() {
  data.hackResetWxReplySelect = false; //销毁组件
  nextTick(() => {
    data.hackResetWxReplySelect = true; //重建组件
  });
  data.handleType = "add";
  data.dialog1Visible = true;
  data.objData = {
    repType: "text",
  };
}

function handleEdit(row) {
  data.hackResetWxReplySelect = false; //销毁组件
  nextTick(() => {
    data.hackResetWxReplySelect = true; //重建组件
  });
  data.handleType = "edit";
  data.dialog1Visible = true;
  data.objData = Object.assign({}, row);
}

function handleClick(tab, event) {
  data.tableData = [];
  data.page.currentPage = 1;
  data.type = tab.paneName;
  getPageF(data.page);
}

function searchChange(params, done) {
  params = /* Warn: Unknown source: filterForm */ proxy.filterForm(params);
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
        type: data.type,
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
      if (data.type == "2") {
        let wxReqType = data.dictData.get("wx_req_type");
        for (let i = 0; i < wxReqType.length; i++) {
          wxReqType[i].disabled = false;
          for (let j = 0; j < data.tableData.length; j++) {
            if (wxReqType[i].value == data.tableData[j].reqType) {
              wxReqType[i].disabled = true;
            }
          }
        }
      }
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

function handleSubmit(row) {
  if (data.handleType == "add") {
    addObj(
      Object.assign(
        {
          type: data.type,
        },
        data.objData
      )
    ).then(() => {
      proxy.$message({
        showClose: true,
        message: "添加成功",
        type: "success",
      });
      getPageF(data.page);
      data.dialog1Visible = false;
    });
  }
  if (data.handleType == "edit") {
    putObj(data.objData).then(() => {
      proxy.$message({
        showClose: true,
        message: "修改成功",
        type: "success",
      });
      getPageF(data.page);
      data.dialog1Visible = false;
    });
  }
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
  addObj(
    Object.assign(
      {
        type: data.type,
      },
      data.objData
    )
  )
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "添加成功",
        type: "success",
      });
      getPageF(data.page);
      data.dialog1Visible = false;
    })
    .catch(() => {
      done();
    });
}

function refreshChange(page) {
  data.objData = {
    repType: "text",
  };
  getPageF(data.page);
}
</script>

<style lang="scss" scoped>
// 去掉 ruoyi 的样式
.avue-crud :deep(.el-card__body) {
  padding: 0 !important;
}
</style>

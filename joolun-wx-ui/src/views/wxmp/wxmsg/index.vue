<template>
  <div>
    <avue-crud
      ref="crud"
      :page="page"
      :data="tableData"
      :permission="permissionList"
      :table-loading="tableLoading"
      :option="tableOption"
      @on-load="getPageF"
      @refresh-change="refreshChange"
      @row-update="handleUpdate"
      @row-save="handleSave"
      @row-del="handleDel"
      @sort-change="sortChange"
      @search-change="searchChange">
      <template #appName="scope">
        <el-tag
          type="success"
          size="small"
          >{{ scope.row.appName }}</el-tag
        >
      </template>
      <template #nickName="scope">
        <el-badge
          :value="scope.row.countMsg"
          class="count-msg">
          <img
            class="head-img"
            :src="scope.row.headimgUrl" />
          <div class="nick-name">{{ scope.row.nickName }}</div>
        </el-badge>
      </template>
      <template #readFlag="scope">
        <el-tag
          :type="scope.row.readFlag == '1' ? 'success' : 'danger'"
          size="small"
          >{{ scope.row.$readFlag }}</el-tag
        >
      </template>
      <template #repContent="scope">
        <div v-if="scope.row.repType == 'event' && scope.row.repEvent == 'subscribe'">
          <el-tag
            type="success"
            size="small"
            >关注</el-tag
          >
        </div>
        <div v-if="scope.row.repType == 'event' && scope.row.repEvent == 'unsubscribe'">
          <el-tag
            type="danger"
            size="small"
            >取消关注</el-tag
          >
        </div>
        <div v-if="scope.row.repType == 'event' && scope.row.repEvent == 'CLICK'"><el-tag size="small">点击菜单</el-tag>：【{{ scope.row.repName }}】</div>
        <div v-if="scope.row.repType == 'event' && scope.row.repEvent == 'VIEW'"><el-tag size="small">点击菜单链接</el-tag>：【{{ scope.row.repUrl }}】</div>
        <div v-if="scope.row.repType == 'event' && scope.row.repEvent == 'scancode_waitmsg'"><el-tag size="small">扫码结果：</el-tag>：【{{ scope.row.repContent }}】</div>
        <div v-if="scope.row.repType == 'text'">{{ scope.row.repContent }}</div>
        <div v-if="scope.row.repType == 'image'">
          <a
            target="_blank"
            :href="scope.row.repUrl"
            ><img
              :src="scope.row.repUrl"
              style="width: 100px"
          /></a>
        </div>
        <div v-if="scope.row.repType == 'voice'">
          <WxVoicePlayer :objData="scope.row"></WxVoicePlayer>
        </div>
        <div v-if="scope.row.repType == 'video'">
          <WxVideoPlayer
            :objData="scope.row"
            style="margin-top: 40px"></WxVideoPlayer>
        </div>
        <div v-if="scope.row.repType == 'shortvideo'">
          <WxVideoPlayer
            :objData="scope.row"
            style="margin-top: 40px"></WxVideoPlayer>
        </div>
        <div v-if="scope.row.repType == 'link'">
          <el-tag size="small">链接</el-tag>：<a
            :href="scope.row.repUrl"
            target="_blank"
            >{{ scope.row.repName }}</a
          >
        </div>
      </template>
      <template #menu="scope">
        <el-button
          link
          icon="el-icon-chat-line-round"
          type="primary"
          plain
          @click="wxMsgDo(scope.row, scope.index)"
          >消息</el-button
        >
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

<script setup name="WxMpMsg">
import { getPage, getObj, addObj, putObj, delObj } from "@/api/wxmp/wxmsg";
import { tableOption } from "@/const/crud/wxmp/wxmsg";
import { checkPermi, checkRole } from "@/utils/permission";
import WxMsg from "@/components/wx-msg/main.vue";
import WxVideoPlayer from "@/components/wx-video-play/main.vue";
import WxVoicePlayer from "@/components/wx-voice-play/main.vue";

const { proxy } = getCurrentInstance();

const data = reactive({
  dialogMsgVisible: false,
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
  wxUserId: "",
});
const { tableData, tableLoading, page, dialogMsgVisible, wxUserId } = toRefs(data);

const permissionList = computed(() => {
  return {
    addBtn: checkPermi(["wxmp:wxmsg:add"]),
    delBtn: checkPermi(["wxmp:wxmsg:del"]),
    editBtn: checkPermi(["wxmp:wxmsg:edit"]),
    viewBtn: checkPermi(["wxmp:wxmsg:get"]),
  };
});

function wxMsgDo(row) {
  data.wxUserId = row.wxUserId;
  data.dialogMsgVisible = true;
  row["readFlag"] = "1";
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
        type: "1",
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

<style lang="scss" scoped>
.head-img {
  width: 50px;
}
</style>

<!--
  Copyright (C) 2026
  All rights reserved, Designed By www.joolun.com
-->
<template>
  <div class="app-container">
    <el-alert title="按客服账号、会话、聊天记录、发送消息四步管理；可从列表直接带入客服和用户信息。" type="info" show-icon :closable="false" />
    <el-tabs v-model="activeTab" type="border-card" class="workspace-tabs">
      <el-tab-pane label="客服账号" name="accounts">
        <div class="toolbar">
          <el-segmented v-model="accountScope" :options="accountScopeOptions" @change="changeAccountScope" />
          <el-button type="primary" v-hasPermi="['wxmp:operations:edit']" @click="openAccountDialog()">新增客服</el-button>
        </div>
        <el-table v-loading="accountLoading" :data="accounts" border stripe>
          <el-table-column label="头像" width="72" align="center"><template #default="scope"><el-avatar :size="36" :src="scope.row.headImgUrl">{{ accountNick(scope.row).slice(0, 1) }}</el-avatar></template></el-table-column>
          <el-table-column label="客服账号" min-width="180"><template #default="scope">{{ accountName(scope.row) }}</template></el-table-column>
          <el-table-column label="客服昵称" min-width="140"><template #default="scope">{{ accountNick(scope.row) }}</template></el-table-column>
          <el-table-column label="绑定微信号" min-width="140"><template #default="scope">{{ scope.row.wxAccount || scope.row.kfWx || '-' }}</template></el-table-column>
          <el-table-column prop="acceptedCase" label="当前接待数" width="110" align="center" />
          <el-table-column label="状态" width="100" align="center"><template #default="scope"><el-tag :type="scope.row.status > 0 ? 'success' : 'info'">{{ scope.row.status > 0 ? '在线' : '未在线' }}</el-tag></template></el-table-column>
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button link type="primary" @click="manageSessions(scope.row)">管理会话</el-button>
              <el-button link type="primary" v-hasPermi="['wxmp:operations:edit']" @click="openAccountDialog(scope.row)">编辑</el-button>
              <el-button link type="danger" v-hasPermi="['wxmp:operations:edit']" @click="removeCustomer(accountName(scope.row))">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="会话管理" name="sessions">
        <el-form :inline="true" :model="sessionForm" class="query-form">
          <el-form-item label="客服账号" required>
            <el-select v-model="sessionForm.account" filterable allow-create default-first-option placeholder="选择或输入客服账号" style="width: 230px">
              <el-option v-for="item in accounts" :key="accountName(item)" :label="`${accountNick(item)}（${accountName(item)}）`" :value="accountName(item)" />
            </el-select>
          </el-form-item>
          <el-form-item label="用户 OpenID"><el-input v-model="sessionForm.openId" placeholder="接入或关闭会话时填写" style="width: 300px" /></el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="sessionLoading" v-hasPermi="['wxmp:operations:query']" @click="loadSessions">查询该客服会话</el-button>
            <el-button :loading="sessionLoading" v-hasPermi="['wxmp:operations:query']" @click="loadWaitingSessions">查看等待队列</el-button>
            <el-button type="success" v-hasPermi="['wxmp:operations:edit']" @click="createSession">接入会话</el-button>
          </el-form-item>
        </el-form>
        <el-table v-loading="sessionLoading" :data="sessions" border stripe>
          <el-table-column prop="kfAccount" label="客服账号" min-width="180" />
          <el-table-column prop="openid" label="用户 OpenID" min-width="280" />
          <el-table-column label="创建时间" min-width="170"><template #default="scope">{{ formatTime(scope.row.createTime) }}</template></el-table-column>
          <el-table-column label="最近活动" min-width="170"><template #default="scope">{{ formatTime(scope.row.latestTime) }}</template></el-table-column>
          <el-table-column label="操作" width="160" fixed="right"><template #default="scope"><el-button link type="primary" @click="sendToUser(scope.row)">发送消息</el-button><el-button link type="danger" v-hasPermi="['wxmp:operations:edit']" @click="closeSession(scope.row)">关闭</el-button></template></el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="聊天记录" name="records">
        <el-form :inline="true" class="query-form">
          <el-form-item label="时间范围"><el-date-picker v-model="recordDates" type="datetimerange" value-format="YYYY-MM-DD HH:mm:ss" start-placeholder="开始时间" end-placeholder="结束时间" /></el-form-item>
          <el-form-item><el-button type="primary" :loading="recordLoading" v-hasPermi="['wxmp:operations:query']" @click="loadRecords">查询记录</el-button></el-form-item>
        </el-form>
        <el-table v-loading="recordLoading" :data="records" border stripe>
          <el-table-column prop="worker" label="客服账号" min-width="160" />
          <el-table-column prop="openid" label="用户 OpenID" min-width="260" />
          <el-table-column prop="text" label="消息内容" min-width="300" show-overflow-tooltip />
          <el-table-column label="时间" width="180"><template #default="scope">{{ formatTime(scope.row.time) }}</template></el-table-column>
          <el-table-column label="操作" width="100" fixed="right"><template #default="scope"><el-button link type="primary" @click="sendToUser({ openid: scope.row.openid, kfAccount: scope.row.worker })">回复用户</el-button></template></el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="发送消息" name="message">
        <el-alert class="send-tip" title="客服消息仅能发送给最近 48 小时内与公众号互动过的用户。" type="warning" show-icon :closable="false" />
        <el-form :model="messageForm" label-width="120px" class="message-form">
          <el-row :gutter="16">
            <el-col :xs="24" :md="12"><el-form-item label="接收人 OpenID" required><wx-user-select v-model="messageForm.toUser" app-type="2" /></el-form-item></el-col>
            <el-col :xs="24" :md="12"><el-form-item label="指定客服账号"><el-select v-model="messageForm.kfAccount" filterable clearable placeholder="不指定则由系统发送"><el-option v-for="item in accounts" :key="accountName(item)" :label="`${accountNick(item)}（${accountName(item)}）`" :value="accountName(item)" /></el-select></el-form-item></el-col>
            <el-col :xs="24" :md="12"><el-form-item label="消息类型"><el-select v-model="messageForm.msgType" @change="resetMessageContent"><el-option label="文本" value="text" /><el-option label="图片" value="image" /><el-option label="语音" value="voice" /><el-option label="视频" value="video" /><el-option label="图文素材" value="mpnews" /><el-option label="小程序卡片" value="miniprogrampage" /></el-select></el-form-item></el-col>
          </el-row>
          <el-form-item v-if="messageForm.msgType === 'text'" label="文本内容" required><el-input v-model="messageForm.content" type="textarea" :rows="6" maxlength="600" show-word-limit /></el-form-item>
          <el-form-item v-else-if="['image', 'voice', 'video'].includes(messageForm.msgType)" label="素材编号" required><el-input v-model="messageForm.mediaId" placeholder="从公众号素材管理中复制 Media ID" /></el-form-item>
          <el-form-item v-else-if="messageForm.msgType === 'mpnews'" label="图文素材编号" required><el-input v-model="messageForm.mpNewsMediaId" placeholder="从公众号素材管理中复制 Media ID" /></el-form-item>
          <template v-else>
            <el-row :gutter="16">
              <el-col :xs="24" :md="12"><el-form-item label="卡片标题" required><el-input v-model="messageForm.title" /></el-form-item></el-col>
              <el-col :xs="24" :md="12"><el-form-item label="小程序 AppID" required><el-input v-model="messageForm.miniProgramAppId" /></el-form-item></el-col>
              <el-col :xs="24" :md="12"><el-form-item label="页面路径" required><el-input v-model="messageForm.miniProgramPagePath" placeholder="pages/index/index" /></el-form-item></el-col>
              <el-col :xs="24" :md="12"><el-form-item label="封面素材编号" required><el-input v-model="messageForm.thumbMediaId" /></el-form-item></el-col>
            </el-row>
          </template>
          <el-form-item><el-button type="primary" :loading="sending" v-hasPermi="['wxmp:operations:send']" @click="sendCustomer">发送消息</el-button><el-button @click="clearMessage">清空内容</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="accountDialog" :title="accountEditing ? '编辑客服账号' : '新增客服账号'" width="520px">
      <el-form :model="accountForm" label-width="110px">
        <el-form-item label="客服账号" required>
          <el-input v-model.trim="accountForm.kfAccount" :disabled="accountEditing" placeholder="例如：service@公众号微信号" />
          <div class="form-tip">完整格式为“账号前缀@公众号微信号”。前缀限 1-10 位英文、数字或下划线；@ 后填写公众平台设置的微信号，不是 AppID、原始 ID 或公众号名称。</div>
        </el-form-item>
        <el-form-item label="客服昵称" required><el-input v-model="accountForm.nickName" /></el-form-item>
        <el-form-item label="邀请微信号"><el-input v-model="accountForm.inviteWx" placeholder="用于接收绑定邀请" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="accountDialog = false">取消</el-button><el-button type="primary" :loading="accountSaving" @click="saveCustomer">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup name="WxMpCustomerService">
import { getCurrentInstance, reactive, ref } from "vue";
import WxUserSelect from "@/components/WxUserSelect/index.vue";
import {
  getCustomerAccounts, getOnlineAccounts, addCustomerAccount, updateCustomerAccount,
  deleteCustomerAccount, getCustomerSessions, getWaitingSessions, createCustomerSession,
  closeCustomerSession, getCustomerRecords, sendCustomerMessage,
} from "@/api/wxmp/wxoperations";

const { proxy } = getCurrentInstance();
const activeTab = ref("accounts");
const accountScope = ref("全部客服");
const accountScopeOptions = ["全部客服", "在线客服"];
const accounts = ref([]);
const sessions = ref([]);
const records = ref([]);
const recordDates = ref(defaultRecordRange());
const accountLoading = ref(false);
const sessionLoading = ref(false);
const recordLoading = ref(false);
const sending = ref(false);
const accountSaving = ref(false);
const accountDialog = ref(false);
const accountEditing = ref(false);
const accountForm = reactive({ kfAccount: "", nickName: "", inviteWx: "" });
const customerAccountPattern = /^[A-Za-z0-9_]{1,10}@[A-Za-z0-9_-]{1,30}$/;
const sessionForm = reactive({ account: "", openId: "" });
const messageForm = reactive({
  toUser: "", kfAccount: "", msgType: "text", content: "", mediaId: "", mpNewsMediaId: "",
  title: "", miniProgramAppId: "", miniProgramPagePath: "", thumbMediaId: "",
});

function accountName(row) { return row?.account || row?.kfAccount || ""; }
function accountNick(row) { return row?.nick || row?.kfNick || accountName(row); }
function requireValue(value, label) {
  if (!String(value || "").trim()) { proxy.$modal.msgWarning(`请填写${label}`); return false; }
  return true;
}
function pad(value) { return String(value).padStart(2, "0"); }
function dateTime(value) { return `${value.getFullYear()}-${pad(value.getMonth() + 1)}-${pad(value.getDate())} ${pad(value.getHours())}:${pad(value.getMinutes())}:${pad(value.getSeconds())}`; }
function defaultRecordRange() { const end = new Date(); return [dateTime(new Date(end.getTime() - 24 * 3600000)), dateTime(end)]; }
function formatTime(value) {
  if (!value) return "-";
  const timestamp = Number(value) < 1000000000000 ? Number(value) * 1000 : Number(value);
  return new Date(timestamp).toLocaleString("zh-CN", { hour12: false });
}
async function changeAccountScope() { await (accountScope.value === "在线客服" ? loadOnlineAccounts() : loadCustomerAccounts()); }
async function loadCustomerAccounts() {
  accountLoading.value = true;
  try { accounts.value = (await getCustomerAccounts()).data?.kfList || []; }
  finally { accountLoading.value = false; }
}
async function loadOnlineAccounts() {
  accountLoading.value = true;
  try { const response = await getOnlineAccounts(); accounts.value = response.data?.kfOnlineList || response.data?.kfList || []; }
  finally { accountLoading.value = false; }
}
function openAccountDialog(row) {
  accountEditing.value = Boolean(row);
  Object.assign(accountForm, row ? { kfAccount: accountName(row), nickName: accountNick(row), inviteWx: row.inviteWx || "" } : { kfAccount: "", nickName: "", inviteWx: "" });
  accountDialog.value = true;
}
async function saveCustomer() {
  if (!requireValue(accountForm.kfAccount, "客服账号") || !requireValue(accountForm.nickName, "客服昵称")) return;
  if (!customerAccountPattern.test(accountForm.kfAccount)) {
    proxy.$modal.msgWarning("客服账号格式不正确，请填写“账号前缀@公众号微信号”；@ 后不是 AppID、原始 ID 或公众号名称");
    return;
  }
  accountSaving.value = true;
  try {
    const body = {
      kfAccount: accountForm.kfAccount.trim(),
      nickName: accountForm.nickName.trim(),
      inviteWx: accountForm.inviteWx.trim(),
    };
    await (accountEditing.value ? updateCustomerAccount(body) : addCustomerAccount(body));
    proxy.$modal.msgSuccess(accountEditing.value ? "客服账号已修改" : "客服账号已新增");
    accountDialog.value = false;
    loadCustomerAccounts();
  } finally { accountSaving.value = false; }
}
async function removeCustomer(account) {
  await proxy.$modal.confirm(`确认删除客服账号 ${account} 吗？`);
  await deleteCustomerAccount(account);
  proxy.$modal.msgSuccess("客服账号已删除");
  loadCustomerAccounts();
}
function manageSessions(row) { activeTab.value = "sessions"; sessionForm.account = accountName(row); loadSessions(); }
async function loadSessions() {
  if (!requireValue(sessionForm.account, "客服账号")) return;
  sessionLoading.value = true;
  try { sessions.value = (await getCustomerSessions(sessionForm.account)).data?.kfSessionList || []; }
  finally { sessionLoading.value = false; }
}
async function loadWaitingSessions() {
  sessionLoading.value = true;
  try { sessions.value = (await getWaitingSessions()).data?.kfSessionWaitCaseList || []; }
  finally { sessionLoading.value = false; }
}
async function createSession() {
  if (!requireValue(sessionForm.account, "客服账号") || !requireValue(sessionForm.openId, "用户 OpenID")) return;
  await createCustomerSession(sessionForm);
  proxy.$modal.msgSuccess("会话已接入");
  loadSessions();
}
async function closeSession(row) {
  const body = row ? { account: row.kfAccount || sessionForm.account, openId: row.openid } : sessionForm;
  if (!requireValue(body.account, "客服账号") || !requireValue(body.openId, "用户 OpenID")) return;
  await proxy.$modal.confirm(`确认关闭与用户 ${body.openId} 的会话吗？`);
  await closeCustomerSession(body);
  proxy.$modal.msgSuccess("会话已关闭");
  loadSessions();
}
async function loadRecords() {
  if (recordDates.value.length !== 2) return proxy.$modal.msgWarning("请选择聊天记录时间范围");
  recordLoading.value = true;
  try { records.value = (await getCustomerRecords({ startTime: recordDates.value[0], endTime: recordDates.value[1], number: 100 })).data?.records || []; }
  finally { recordLoading.value = false; }
}
function sendToUser(row) {
  activeTab.value = "message";
  messageForm.toUser = row.openid || "";
  messageForm.kfAccount = row.kfAccount || "";
}
function resetMessageContent() {
  Object.assign(messageForm, { content: "", mediaId: "", mpNewsMediaId: "", title: "", miniProgramAppId: "", miniProgramPagePath: "", thumbMediaId: "" });
}
function clearMessage() { resetMessageContent(); }
async function sendCustomer() {
  if (!requireValue(messageForm.toUser, "接收人 OpenID")) return;
  if (messageForm.msgType === "text" && !requireValue(messageForm.content, "文本内容")) return;
  if (["image", "voice", "video"].includes(messageForm.msgType) && !requireValue(messageForm.mediaId, "素材编号")) return;
  if (messageForm.msgType === "mpnews" && !requireValue(messageForm.mpNewsMediaId, "图文素材编号")) return;
  if (messageForm.msgType === "miniprogrampage" && (!requireValue(messageForm.title, "卡片标题") || !requireValue(messageForm.miniProgramAppId, "小程序 AppID") || !requireValue(messageForm.miniProgramPagePath, "页面路径") || !requireValue(messageForm.thumbMediaId, "封面素材编号"))) return;
  sending.value = true;
  try {
    await sendCustomerMessage({ ...messageForm });
    proxy.$modal.msgSuccess("客服消息发送成功");
    resetMessageContent();
  } finally { sending.value = false; }
}

loadCustomerAccounts();
</script>

<style scoped>
.workspace-tabs { margin-top: 16px; }
.toolbar { display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; }
.query-form { margin-top: 6px; }
.send-tip { margin-bottom: 18px; }
.message-form { max-width: 1050px; }
.form-tip { margin-top: 6px; color: var(--el-text-color-secondary); font-size: 12px; line-height: 1.6; }
</style>

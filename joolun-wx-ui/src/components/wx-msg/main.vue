<template>
  <div
    class="msg-main"
    v-loading="mainLoading">
    <div
      class="msg-div"
      :id="'msg-div' + nowStr">
      <div v-loading="tableLoading"></div>
      <div v-if="!tableLoading">
        <div
          class="el-table__empty-block"
          v-if="loadMore"
          @click="loadingMore">
          <span class="el-table__empty-text">点击加载更多</span>
        </div>
        <div
          class="el-table__empty-block"
          v-if="!loadMore">
          <span class="el-table__empty-text">没有更多了</span>
        </div>
      </div>
      <div
        class="execution"
        v-for="item in tableData"
        :key="item.id">
        <div
          class="avue-comment"
          :class="item.type == '2' ? 'avue-comment--reverse' : ''">
          <div class="avatar-div">
            <el-avatar
              :src="item.type == '1' ? item.headimgUrl : item.appLogo"
              class="avue-comment__avatar"
              ><el-icon><UserFilled /></el-icon
            ></el-avatar>
            <div
              class="avue-comment__author"
              style="margin-left: 8px">
              {{ item.type == "1" ? item.nickName : item.appName }}
            </div>
          </div>
          <div
            class="avue-comment__main"
            :style="item.type == '1' ? 'margin-right:105px;' : 'margin-left:105px;'">
            <div class="avue-comment__header">
              <div class="avue-comment__create_time">{{ item.createTime }}</div>
            </div>
            <div
              class="avue-comment__body"
              :style="item.type == '2' ? 'background: #6BED72;' : ''">
              <div v-if="item.repType == 'event' && item.repEvent == 'subscribe'">
                <el-tag
                  type="success"
                  size="small"
                  >关注</el-tag
                >
              </div>
              <div v-if="item.repType == 'event' && item.repEvent == 'unsubscribe'">
                <el-tag
                  type="danger"
                  size="small"
                  >取消关注</el-tag
                >
              </div>
              <div v-if="item.repType == 'event' && item.repEvent == 'CLICK'"><el-tag size="small">点击菜单</el-tag>：【{{ item.repName }}】</div>
              <div v-if="item.repType == 'event' && item.repEvent == 'VIEW'"><el-tag size="small">点击菜单链接</el-tag>：【{{ item.repUrl }}】</div>
              <div v-if="item.repType == 'event' && item.repEvent == 'scancode_waitmsg'"><el-tag size="small">扫码结果：</el-tag>：【{{ item.repContent }}】</div>
              <div v-if="item.repType == 'text'">{{ item.repContent }}</div>
              <div v-if="item.repType == 'image'">
                <a
                  target="_blank"
                  :href="item.repUrl"
                  ><img
                    :src="item.repUrl"
                    style="width: 100px"
                /></a>
              </div>
              <div v-if="item.repType == 'voice'">
                <WxVoicePlayer :objData="item"></WxVoicePlayer>
              </div>
              <div
                v-if="item.repType == 'video'"
                style="text-align: center">
                <WxVideoPlayer :objData="item"></WxVideoPlayer>
              </div>
              <div
                v-if="item.repType == 'shortvideo'"
                style="text-align: center">
                <WxVideoPlayer :objData="item"></WxVideoPlayer>
              </div>
              <div v-if="item.repType == 'location'">
                <el-link
                  type="primary"
                  target="_blank"
                  :href="'https://map.qq.com/?type=marker&isopeninfowin=1&markertype=1&pointx=' + item.repLocationY + '&pointy=' + item.repLocationX + '&name=' + item.repContent + '&ref=joolun'">
                  <img :src="'https://apis.map.qq.com/ws/staticmap/v2/?zoom=10&markers=color:blue|label:A|' + item.repLocationX + ',' + item.repLocationY + '&key=' + qqMapKey + '&size=250*180'" />
                  <p />
                  <i class="el-icon-map-location"></i>{{ item.repContent }}
                </el-link>
              </div>
              <div
                v-if="item.repType == 'link'"
                class="avue-card__detail">
                <el-link
                  type="success"
                  :underline="false"
                  target="_blank"
                  :href="item.repUrl">
                  <div class="avue-card__title"><i class="el-icon-link"></i>{{ item.repName }}</div>
                </el-link>
                <div
                  class="avue-card__info"
                  style="height: unset">
                  {{ item.repDesc }}
                </div>
              </div>
              <div
                v-if="item.repType == 'news'"
                style="width: 300px">
                <WxNews :objData="item.content.articles"></WxNews>
              </div>
              <div v-if="item.repType == 'music'">
                <el-link
                  type="success"
                  :underline="false"
                  target="_blank"
                  :href="item.repUrl">
                  <div
                    class="avue-card__body"
                    style="padding: 10px; background-color: #fff; border-radius: 5px">
                    <div class="avue-card__avatar">
                      <img
                        :src="item.repThumbUrl"
                        alt="" />
                    </div>
                    <div class="avue-card__detail">
                      <div
                        class="avue-card__title"
                        style="margin-bottom: unset">
                        {{ item.repName }}
                      </div>
                      <div
                        class="avue-card__info"
                        style="height: unset">
                        {{ item.repDesc }}
                      </div>
                    </div>
                  </div>
                </el-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div
      class="msg-send"
      v-loading="sendLoading">
      <WxReplySelect :objData="objData"></WxReplySelect>
      <el-button
        type="success"
        class="send-but"
        @click="sendMsg"
        >发送</el-button
      >
    </div>
  </div>
</template>

<script setup name="WxMsg">
import { getPage, addObj, putObj } from "@/api/wxmp/wxmsg";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import { getToken } from "@/utils/auth";
import WxReplySelect from "@/components/wx-reply/main.vue";
import WxNews from "@/components/wx-news/main.vue";
import WxVideoPlayer from "@/components/wx-video-play/main.vue";
import WxVoicePlayer from "@/components/wx-voice-play/main.vue";
import { getCurrentInstance, nextTick, onMounted, onUnmounted, reactive } from "vue";

const { proxy } = getCurrentInstance();

const props = defineProps({
  wxUserId: {
    type: String,
  },
});

const data = reactive({
  nowStr: new Date().getTime(),
  objData: {
    repType: "text",
  },
  mainLoading: false,
  sendLoading: false,
  tableLoading: false,
  loadMore: true,
  tableData: [],
  page: {
    total: 0, // 总页数
    currentPage: 1, // 当前页数
    pageSize: 14, // 每页显示多少条
    ascs: [], //升序字段
    descs: "create_time", //降序字段
  },
  option: {
    props: {
      avatar: "avatar",
      author: "author",
      body: "body",
    },
  },
  timer: undefined,
});
const { tableData, page, mainLoading, sendLoading, tableLoading, dialogMsgVisible, objData, nowStr, loadMore, option } = toRefs(data);

// 链接socket
const stompClient = ref(null);
onMounted(() => {
  refreshChange();
  // 开源版接口没有实现websocket，固隐藏
  // initWebSocket()
});
onUnmounted(() => {
  disconnect();
  if (data.timer) clearInterval(data.timer);
});

function initWebSocket() {
  connection();
  //断开重连机制,尝试发送消息,捕获异常发生时重连
  data.timer = setInterval(() => {
    try {
      stompClient.value.send("test");
    } catch (err) {
      console.log("断线了: " + err);
      connection();
    }
  }, 5000);
}

function connection() {
  const token = getToken().access_token;
  const headers = {
    Authorization: "Bearer " + token,
  };
  const socket = new SockJS("/weixin/ws");
  stompClient.value = Stomp.over(socket);
  stompClient.value.connect(
    headers,
    () => {
      stompClient.value.subscribe("/weixin/wx_msg" + props.wxUserId, (msg) => {
        const msgBody = JSON.parse(msg.body);
        data.tableData = [...data.tableData, ...[msgBody]];
        scrollToBottom();

        if (msgBody.type == "1") {
          putObj({
            id: msgBody.id,
            readFlag: "0",
          }).then(() => {});
        }
      });
    },
    () => {}
  );
}

function disconnect() {
  if (stompClient.value != null) {
    stompClient.value.disconnect();
  }
}

function sendMsg() {
  if (data.objData) {
    if (data.objData.repType == "news") {
      data.objData.content.articles = [data.objData.content.articles[0]];
      proxy.$message({
        showClose: true,
        message: "图文消息条数限制在1条以内，已默认发送第一条",
        type: "success",
      });
    }
    data.sendLoading = true;
    addObj(
      Object.assign(
        {
          wxUserId: props.wxUserId,
        },
        data.objData
      )
    )
      .then((response) => {
        data.sendLoading = false;
        data.tableData = [...data.tableData, ...[response.data]];
        scrollToBottom();
        data.objData = {
          repType: "text",
        };
      })
      .catch(() => {
        data.sendLoading = false;
      });
  }
}

function scrollToBottom() {
  nextTick(() => {
    let div = document.getElementById("msg-div" + data.nowStr);
    div.scrollTop = div.scrollHeight;
  });
}

function loadingMore() {
  data.page.currentPage++;
  getPageF(data.page);
}

function getPageF(page, params) {
  data.tableLoading = true;
  getPage(
    Object.assign(
      {
        current: page.currentPage,
        size: page.pageSize,
        descs: page.descs,
        ascs: page.ascs,
        wxUserId: props.wxUserId,
      },
      params
    )
  ).then((response) => {
    let msgDiv = document.getElementById("msg-div" + data.nowStr);
    let scrollHeight = 0;
    if (msgDiv) {
      scrollHeight = msgDiv.scrollHeight;
    }
    const dataResult = response.data.records.reverse();
    data.tableData = [...dataResult, ...data.tableData];
    data.page.total = response.data.total;
    data.tableLoading = false;
    if (data.length < data.page.pageSize || data.length == 0) {
      data.loadMore = false;
    }
    if (data.page.currentPage == 1) {
      //定位到消息底部
      scrollToBottom();
    } else {
      if (data.length != 0) {
        nextTick(() => {
          //定位滚动条
          if (scrollHeight != 0) {
            msgDiv.scrollTop = document.getElementById("msg-div" + data.nowStr).scrollHeight - scrollHeight - 100;
          }
        });
      }
    }
    data.page.currentPage = page.currentPage;
    data.page.pageSize = page.pageSize;
  });
}

function refreshChange() {
  getPageF(data.page);
}
</script>

<style lang="scss" scoped>
.msg-main {
  margin-top: -5px;
}
.msg-div {
  height: 58vh;
  overflow: auto;
  background-color: #eaeaea;
}
.msg-send {
  padding-bottom: 40px;
}
.avue-comment__main {
  flex: unset !important;
  border-radius: 5px !important;
  margin: 0 5px;
}
.avue-comment__header {
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
}
.avue-comment__body {
  border-bottom-right-radius: 5px;
  border-bottom-left-radius: 5px;
}
.avatar-div {
  text-align: center;
  padding-left: 8px;
  padding-right: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.send-but {
  float: right;
  margin-top: 8px !important;
}
</style>

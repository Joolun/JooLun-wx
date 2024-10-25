<template>
  <div>
    <div v-if="!outTime">
      <el-button
        icon="VideoPlay"
        link
        @click="playVideo()"
        >点击播放视频</el-button
      >
    </div>
    <div v-if="outTime">
      <i class="el-icon-video-play shipin-i"></i>
      <p />
      <i class="el-icon-time">&nbsp;该视频已过期</i>
    </div>
    <el-dialog
      title="视频播放"
      v-model="dialogVideo"
      width="40%"
      v-loading="mainLoading"
      append-to-body>
      <video-player
        v-if="playerOptions.sources[0].src"
        class="video-player vjs-custom-skin"
        ref="videoPlayer"
        :playsinline="true"
        :options="playerOptions"
        @play="onPlayerPlay($event)"
        @pause="onPlayerPause($event)">
      </video-player>
    </el-dialog>
  </div>
</template>

<script setup named="WxVideoPlay">
import { VideoPlayer } from "vue-video-player";
import { getTempMaterialOther, getMaterialOther } from "@/api/wxmp/wxmaterial";
import "video.js/dist/video-js.css";
const props = defineProps({
  objData: {
    type: Object,
  },
});

const data = reactive({
  dialogVideo: false,
  outTime: false,
  mainLoading: false,
  playerOptions: {
    //        playbackRates: [0.7, 1.0, 1.5, 2.0], //播放速度
    autoplay: false, //如果true,浏览器准备好时开始回放。
    muted: false, // 默认情况下将会消除任何音频。
    loop: false, // 导致视频一结束就重新开始。
    preload: "auto", // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
    language: "zh-CN",
    aspectRatio: "4:3", // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
    fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
    sources: [
      {
        type: "video/mp4",
        src: "", //你的视频地址（必填）
      },
    ],
    poster: "", //你的封面地址
    width: document.documentElement.clientWidth,
    notSupportedMessage: "此视频暂无法播放，请稍后再试", //允许覆盖Video.js无法播放媒体源时显示的默认信息。
    //        controlBar: {
    //          timeDivider: true,
    //          durationDisplay: true,
    //          remainingTimeDisplay: false,
    //          fullscreenToggle: true  //全屏按钮
    //        }
  },
});
const { dialogVideo, outTime, mainLoading, playerOptions } = toRefs(data);

function onCreated() {
  data.outTime = props.objData.type === "1" && parseInt(new Date().getTime() - new Date(props.objData.createTime).getTime()) >= 259200000;
}
onCreated();

onMounted(() => {});

function playVideo() {
  data.dialogVideo = true;
  getVideo();
}

async function getTempMaterialOtherF(repMediaId, fileName) {
  let url;
  data.mainLoading = true;
  await getTempMaterialOther({
    mediaId: repMediaId,
    fileName: fileName,
  }).then((response) => {
    data.mainLoading = false;
    url = window.URL.createObjectURL(new Blob([response.data]));
  });
  return url;
}

async function getMaterialOtherF(repMediaId, fileName) {
  let url;
  data.mainLoading = true;
  await getMaterialOther({
    mediaId: repMediaId,
    fileName: fileName,
  }).then((response) => {
    data.mainLoading = false;
    url = window.URL.createObjectURL(new Blob([response.data]));
  });
  return url;
}

function getVideo() {
  if (props.objData.type == "2") {
    data.playerOptions.sources[0]["src"] = props.objData.repUrl;
  } else if (props.objData.type == "1") {
    getTempMaterialOtherF(props.objData.repMediaId, props.objData.repMediaId + ".mp4").then((val) => {
      data.playerOptions.sources[0]["src"] = val;
    });
  }
}

function onPlayerPlay(player) {}

function onPlayerPause(player) {}
</script>

<style lang="scss" scoped></style>

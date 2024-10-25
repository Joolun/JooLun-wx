<template>
  <div
    v-loading="mainLoading"
    class="wx-voice-div"
    @click="outTime ? '' : playVoice(objData)">
    <el-button
      v-if="!outTime"
      link
      :icon="objData.amrPlaying != true ? 'Microphone' : 'Headset'">
      <span
        class="amr-duration"
        v-if="objData.amrDuration"
        >{{ objData.amrDuration }}"</span
      >语音
    </el-button>
    <i
      v-if="outTime"
      class="el-icon-time">
      <span class="amr-duration">该语音已过期</span>
    </i>
    <div v-if="objData.repContent">
      <el-tag
        type="success"
        size="small"
        >语音识别</el-tag
      >{{ objData.repContent }}
    </div>
  </div>
</template>

<script setup name="WxVoicePlay">
import { getTempMaterialOther, getMaterialOther } from "@/api/wxmp/wxmaterial";
import BenzAMRRecorder from "benz-amr-recorder";

const props = defineProps({
  objData: {
    type: Object,
  },
});

const data = reactive({
  outTime: false,
  mainLoading: false,
});

const { outTime, mainLoading } = toRefs(data);

function onCreated() {
  data.outTime = props.objData.type === "1" && parseInt(new Date().getTime() - new Date(props.objData.createTime).getTime()) >= 259200000;
}
onCreated();

onMounted(() => {});

function amrPlay(amr, val) {
  val["amrPlaying"] = true;
  amr.play();
}

function amrStop(amr, val) {
  val["amrPlaying"] = false;
  amr.stop();
}

function playVoice(val) {
  let amr = val.amr;
  if (amr == undefined) {
    if (val.type == "2") {
      getMaterialOtherF(val.repMediaId, val.repName).then((url) => {
        val["repUrl"] = url;
        handleAudio(val);
      });
    } else if (val.type == "1") {
      getTempMaterialOtherF(val.repMediaId, val.repName).then((url) => {
        val["repUrl"] = url;
        handleAudio(val);
      });
    }
  } else {
    if (amr.isPlaying()) {
      amrStop(amr, val);
    } else {
      amrPlay(amr, val);
    }
  }
}

async function getTempMaterialOtherF(repMediaId, fileName) {
  let url;
  data.mainLoading = true;
  await getTempMaterialOther({
    mediaId: repMediaId,
    fileName: fileName,
  })
    .then((response) => {
      data.mainLoading = false;
      url = window.URL.createObjectURL(new Blob([response.data]));
    })
    .catch(() => {
      data.mainLoading = false;
    });
  return url;
}

async function getMaterialOtherF(repMediaId, fileName) {
  let url;
  data.mainLoading = true;
  await getMaterialOther({
    mediaId: repMediaId,
    fileName: fileName,
  })
    .then((response) => {
      data.mainLoading = false;
      url = window.URL.createObjectURL(new Blob([response.data]));
    })
    .catch(() => {
      data.mainLoading = false;
    });
  return url;
}

function handleAudio(val) {
  val["amr"] = new BenzAMRRecorder();
  let amr = val.amr;
  let that = this;
  amr.initWithUrl(val.repUrl).then(function () {
    that.amrPlay(amr, val);
    that.$set(val, "amrDuration", amr.getDuration());
  });
  amr.onEnded(function () {
    that.$set(val, "amrPlaying", false); //播放完了
  });
}
</script>

<style lang="scss" scoped>
.wx-voice-div {
  padding: 5px;
  background-color: #eaeaea;
  border-radius: 10px;
}
.amr-duration {
  font-size: 11px;
  margin-left: 5px;
}
</style>

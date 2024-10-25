<template>
  <div>
    <el-card class="box-card">
      <template #header>
        <div class="clearfix">
          <span class="demonstration">时间范围</span>
          <el-date-picker
            format="YYYY-MM-DD"
            v-model="dateValues"
            type="daterange"
            :picker-options="datePickerOptions"
            @change="changeDate"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </div>
      </template>
      <el-row justify="center">
        <el-col :span="12">
          <div
            id="userSummaryChart"
            :style="{ width: '80%', height: '378px' }"></div>
        </el-col>
        <el-col :span="12">
          <div
            id="userCumulateChart"
            :style="{ width: '80%', height: '378px' }"></div>
        </el-col>
        <el-col :span="12">
          <div
            id="interfaceSummaryChart"
            :style="{ width: '80%', height: '378px' }"></div>
        </el-col>
        <el-col :span="12">
          <div
            id="interfaceSummaryChart2"
            :style="{ width: '80%', height: '378px' }"></div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup name="WxMpSummary">
import { dayjs } from "element-plus";
// 引入基本模板
import * as echarts from "echarts";
import { getInterfaceSummary, getUserSummary, getUserCumulate } from "@/api/wxmp/wxsummary";

import { getCurrentInstance, onMounted, reactive } from "vue";

const { proxy } = getCurrentInstance();

const data = reactive({
  datePickerOptions: {
    disabledDate(date) {
      return date >= new Date(new Date() - 3600 * 1000 * 24);
    },
  },
  startDate: dayjs().add(-7, "days").format("YYYY-MM-DD"),
  endDate: dayjs().add(-1, "days").format("YYYY-MM-DD"),
  dateValues: [dayjs().add(-7, "days").format("YYYY-MM-DD"), dayjs().add(-1, "days").format("YYYY-MM-DD")],

  xAxisData: [],
  seriesData1: [],
  seriesData2: [],
  seriesData3: [],
  seriesData4: [],
  seriesData5: [],
  seriesData6: [],
  seriesData7: [],
});
const { datePickerOptions, startDate, endDate, dateValues, xAxisData, seriesData1, seriesData2, seriesData3, seriesData4, seriesData5, seriesData6, seriesData7 } = toRefs(data);

onMounted(() => {
  getSummary();
});

function changeDate() {
  let startDate = new Date(data.dateValues[0]);
  let endDate = new Date(data.dateValues[1]);
  if (dayjs(endDate).diff(dayjs(startDate), "day") > 6) {
    proxy.$message.error("时间间隔7天以内，请重新选择");
    return false;
  } else {
    data.startDate = dayjs(startDate).format("YYYY-MM-DD");
    data.endDate = dayjs(endDate).format("YYYY-MM-DD");
    data.xAxisData = [];
    data.seriesData1 = [];
    data.seriesData2 = [];
    data.seriesData5 = [];
    data.seriesData6 = [];
    getSummary();
  }
}

function getSummary() {
  let days = dayjs(data.endDate).diff(dayjs(data.startDate), "day"); //相差天数
  for (let i = 0; i <= days; i++) {
    data.xAxisData.push(dayjs(data.startDate).add(i, "days").format("YYYY-MM-DD"));
    data.seriesData1.push(0);
    data.seriesData2.push(0);
    data.seriesData5.push(0);
    data.seriesData6.push(0);
  }
  getUserSummary({
    startDate: data.startDate,
    endDate: data.endDate,
  })
    .then((response) => {
      data.xAxisData.forEach((item, index, arr) => {
        response.data.forEach((item2, index2, arr2) => {
          if (item2.refDate.indexOf(item) >= 0) {
            data.seriesData5[index] = data.seriesData5[index] + item2.newUser;
            data.seriesData6[index] = data.seriesData6[index] + item2.cancelUser;
          }
        });
      });
      // 基于准备好的dom，初始化echarts实例
      let userSummaryChart = echarts.init(document.getElementById("userSummaryChart"));
      // 绘制图表
      userSummaryChart.setOption({
        title: { text: "用户增减数据" },
        color: ["#67C23A", "#F56C6C"],
        legend: {
          data: ["新增用户", "取消关注的用户"],
        },
        tooltip: {},
        xAxis: {
          data: data.xAxisData,
        },
        yAxis: {},
        series: [
          {
            name: "新增用户",
            type: "bar",
            label: {
              normal: {
                show: true,
              },
            },
            barGap: 0,
            data: data.seriesData5,
          },
          {
            name: "取消关注的用户",
            type: "bar",
            label: {
              normal: {
                show: true,
              },
            },
            data: data.seriesData6,
          },
        ],
      });
    })
    .catch(() => {});

  getUserCumulate({
    startDate: data.startDate,
    endDate: data.endDate,
  })
    .then((response) => {
      response.data.forEach((item, index, arr) => {
        data.seriesData7[index] = item.cumulateUser;
      });
      // 基于准备好的dom，初始化echarts实例
      let userCumulateChart = echarts.init(document.getElementById("userCumulateChart"));
      // 绘制图表
      userCumulateChart.setOption({
        title: { text: "累计用户数据" },
        legend: {
          data: ["累计用户量"],
        },
        xAxis: {
          type: "category",
          data: data.xAxisData,
        },
        yAxis: {
          type: "value",
        },
        tooltip: {
          trigger: "axis",
        },
        series: [
          {
            name: "累计用户量",
            data: data.seriesData7,
            type: "line",
            smooth: true,
          },
        ],
      });
    })
    .catch(() => {});

  //获取接口数据
  getInterfaceSummary({
    startDate: data.startDate,
    endDate: data.endDate,
  })
    .then((response) => {
      response.data.forEach((item, index, arr) => {
        data.seriesData1[index] = item.callbackCount;
        data.seriesData2[index] = item.maxTimeCost;
        data.seriesData3[index] = item.totalTimeCost;
        data.seriesData4[index] = item.failCount;
      });
      // 基于准备好的dom，初始化echarts实例
      let interfaceSummaryChart = echarts.init(document.getElementById("interfaceSummaryChart"));
      // 绘制图表
      interfaceSummaryChart.setOption({
        title: { text: "接口分析数据" },
        color: ["#67C23A", "#F56C6C"],
        legend: {
          data: ["被动回复用户消息的次数", "失败次数"],
        },
        tooltip: {},
        xAxis: {
          data: data.xAxisData,
        },
        yAxis: {},
        series: [
          {
            name: "被动回复用户消息的次数",
            type: "bar",
            label: {
              normal: {
                show: true,
              },
            },
            barGap: 0,
            data: data.seriesData1,
          },
          {
            name: "失败次数",
            type: "bar",
            label: {
              normal: {
                show: true,
              },
            },
            data: data.seriesData4,
          },
        ],
      });

      // 基于准备好的dom，初始化echarts实例
      let interfaceSummaryChart2 = echarts.init(document.getElementById("interfaceSummaryChart2"));
      // 绘制图表
      interfaceSummaryChart2.setOption({
        title: { text: "接口分析数据" },
        color: ["#E6A23C", "#409EFF"],
        legend: {
          data: ["最大耗时", "总耗时"],
        },
        tooltip: {},
        xAxis: {
          data: data.xAxisData,
        },
        yAxis: {},
        series: [
          {
            name: "最大耗时",
            type: "bar",
            label: {
              normal: {
                show: true,
              },
            },
            data: data.seriesData2,
          },
          {
            name: "总耗时",
            type: "bar",
            label: {
              normal: {
                show: true,
              },
            },
            data: data.seriesData3,
          },
        ],
      });
    })
    .catch(() => {});
}
</script>

<style lang="scss" scoped>
.demonstration {
  font-size: 15px;
  margin-right: 10px;
}
</style>

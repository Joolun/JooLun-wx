<template>
  <div class="app-container home">
    <el-row
      :gutter="20"
      style="text-align: center; margin: auto; padding-bottom: 20px"
    >
      <el-col :sm="24" :lg="6">
        <el-card class="update-log">
          <div class="clearfix">
            <span class="clearfix-title">JooLun开源版</span>
            <p class="clearfix-text">
              JooLun(乔伦)微信管理系统快速开发开源框架,
              使用RuoYi框架+Avue框架二次开发,新增商城模块、小程序管理模块、微信公众号管理模块
              <br />
              <br />
              <el-link
                href="https://gitee.com/joolun/JooLun-wx/tree/master/"
                target="_blank"
                >SpringBoot2+Vue2+微信小程序开源代码
                <el-icon>
                  <TopRight />
                </el-icon>
              </el-link>
              <br />
              <el-link
                href="https://gitee.com/joolun/JooLun-wx/tree/boot3/"
                target="_blank"
                >SpringBoot3+Vue3+微信小程序开源代码
                <el-icon>
                  <TopRight />
                </el-icon>
              </el-link>
              <br />
              <el-link href="https://doc.ruoyi.vip/ruoyi/" target="_blank"
                >RuoYi框架
                <el-icon>
                  <TopRight />
                </el-icon>
              </el-link>
              <el-link
                href="https://avuejs.com/"
                target="_blank"
                style="margin-left: 5px"
                >Avue框架
                <el-icon>
                  <TopRight />
                </el-icon>
              </el-link>
            </p>
            <br />
            <p>此公众号对接了该平台，和该后台数据同步</p>
            <img class="code" src="/wxmpjoolun.jpeg" />
            <p>
              <el-button @click="goTarget('https://gitee.com/joolun/JooLun-wx')"
                >访问码云
              </el-button>
              <el-button @click="goTarget('https://www.joolun.com/')"
                >访问官网
              </el-button>
            </p>
          </div>
          <div>
            <br />
            <div>
              <el-text size="large">客服微信</el-text>
            </div>
            <img class="left-image" src="/joolunkefu.jpeg" />
          </div>
        </el-card>
      </el-col>
      <el-col :sm="24" :lg="18">
        <el-card class="update-log">
          <div class="clearfix">
            <span class="clearfix-title">JooLun商业版</span>
            <p>
              <el-text size="large">全源码交付</el-text>
            </p>
            <p class="clearfix-text">
              微服务Sping Cloud + uniapp
              强强联合打造的B2B2C多商户（多店铺）商城系统,
              一套代码多端复用，小程序、H5、APP、PC端,轻松实现多终端覆盖！无论是移动端商城还是pc端商城，商城首页、店铺
              首页均可拖拽式自定义装修！拥有商户APP，IM客服系统，砍价、拼团、秒杀、分销、优惠券、直播等丰富营销组件
            </p>
            <p>
              <el-button @click="goTarget('https://www.joolun.com/yshj/')"
                >后台演示
              </el-button>
              <el-button @click="goTarget('https://www.joolun.com/gndb.html')"
                >功能对比
              </el-button>
            </p>
            <a href="https://www.joolun.com" target="_blank">
              <img width="100%" src="/function_menu2.png" />
            </a>
            <el-row style="padding: 10px">
              <el-col :sm="24" :lg="6" style="text-align: center">
                公众号基础版没有商城功能
              </el-col>
              <el-col :sm="24" :lg="6">
                <img width="200px" height="200px" src="/wxma.jpg" />
                <p>小程序商城版</p>
              </el-col>
              <el-col :sm="24" :lg="6">
                <img width="200px" height="200px" src="/wxmaplus.png" />
                <p>JooLun Plus 多店版</p>
              </el-col>
              <el-col :sm="24" :lg="6">
                <img width="200px" height="200px" src="/wxmapro.jpg" />
                <p>JooLun Pro 旗舰版</p>
              </el-col>
            </el-row>
            <img
              style="margin-top: 15px"
              width="100%"
              src="/function_menu.png"
            />
          </div>
          <div class="clearfix" style="">
            <span class="clearfix-title">Joolun uniapp 多店版-多租户管理</span>
            <p class="clearfix-text-2">
              Joolun
              uniapp多店版商城系统采用多租户机制，一个租户即为一个独立的商城
              多租户就能同时承建和管理多个商城，这是其他同类型产品无法比拟的！
              简单的说，一个租户即为一个某宝或者某东，多租户就能同时承建和管理多个某宝或者某东！
              商城系统前端采用uniapp开发，可发布到小程序、H5、ios以及Android等多个平台，一套代码多端发行，轻松实现多终端覆盖！
            </p>
          </div>
          <div class="clearfix">
            <span class="clearfix-title">Joolun uniapp 多店版-后台管理</span>
            <p class="clearfix-text-2">
              仅需部署一套后台系统即可管理所有租户和整个系统平台，系统用户分为三种类别
              系统管理员——管理整个系统和所有商城 商城管理员——管理自己所属商城
              店铺管理员——管理自己所属店铺
            </p>
          </div>
          <div class="clearfix">
            <span class="clearfix-title">Joolun uniapp 多店版-商城端</span>
            <p class="clearfix-text-2">
              秒杀、拼团、砍价、电子券、积分、分销、直播等等,丰富新颖的营销功能，满足不同客户需求！支持微信和支付宝服务商收款,满足APP、H5、PC、小程序等主流支付场景！
            </p>
          </div>
          <div class="clearfix">
            <span class="clearfix-title">Joolun uniapp 多店版-商户端</span>
            <p class="clearfix-text-2">
              JooLun Uniapp多店版商城系统拥有独立的IM客服管理系统
              客服聊天，上架商品，管理订单，实时发货，商城动态尽在掌控！
            </p>
          </div>
          <div class="clearfix">
            <span class="clearfix-title">拖拽式自定义商城装修</span>
            <p class="clearfix-text-2">
              系统商城和店铺，无论是移动端还是pc端，均支持拖拽式的自定义装修
              丰富的基础组件，随意拖拽，装修适合自己风格的主题，让商城更多样化！
            </p>
          </div>
          <div class="clearfix">
            <span class="clearfix-title">正版授权 无域名限制</span>
            <p class="clearfix-text-2">
              正版授权，无域名限制，多维度保障用户的权益，使用起来无后顾之忧
              购买后可获得全部源码+文档教程，前期部署提供技术协助,无需授权,全部代码“0”加密
            </p>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 活动图片展示区 -->
    <!-- <el-row
      :gutter="20"
      class="advertising">
      <el-col
        :sm="24"
        :lg="24">
        <a
          href="https://www.joolun.com/uniappddb.html"
          target="_blank">
          <img
            style="width: 240px"
            src="https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun_Plus_13.png" />
        </a>
      </el-col>
    </el-row> -->
  </div>
</template>

<script setup name="ViewIndex">
import { onMounted } from "vue";

const { proxy } = getCurrentInstance();

const data = reactive({
  // 版本号
  version: "3.3.0",
});
const { version } = toRefs(data);
onMounted(() => {
  onCreated();
});

function onCreated() {
  proxy.$notify({
    title: "云服务器抢购",
    dangerouslyUseHTMLString: true,
    duration: 0,
    position: "bottom-right",
    message:
      '<el-row :gutter="20">' +
      '<el-col :sm="24" :lg="24">' +
      '<a href="https://www.aliyun.com/minisite/goods?userCode=wnw8gle1" target="_blank">' +
      '<img style="width: 280px" src="https://images.gitee.com/uploads/images/2021/0622/110750_59b42ced_5079715.jpeg">' +
      "</a>" +
      '<p><a style="color: red" href="https://www.aliyun.com/minisite/goods?userCode=wnw8gle1" type="danger" target="_blank">阿里云领取￥2000红包，服务器低至￥91.80/年</a></p>' +
      "</el-col>" +
      "</el-row>",
  });
}

function goTarget(href) {
  window.open(href, "_blank");
}
</script>

<style lang="scss" scoped>
.el-card.is-always-shadow {
  box-shadow: none !important;
}

.el-card {
  border-radius: 0px;
}

.clearfix {
  margin-top: 30px;
}

.clearfix-title {
  font-size: 24px;
  font-weight: 1000;
}

.clearfix-text {
  min-height: 60px;
  font-size: 14px;
  color: gray;
}

.clearfix-text-2 {
  font-size: 14px;
  color: gray;
}

.left-image {
  width: 220px;
  height: 220px;
  margin-bottom: 120px;
}

.code {
  width: 156px;
  height: 156px;
  margin-bottom: 40px;
  display: flex;
  margin: auto;
}

.right-image {
  width: 720px;
  height: 360px;
  margin-top: 20px;
  margin-bottom: 20px;
}

.right-image-2 {
  width: 680px;
}

.advertising {
  position: fixed;
  top: 130px;
  right: 30px;
}

.app-container {
  padding: 0;
}

.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    margin-top: 20px;

    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

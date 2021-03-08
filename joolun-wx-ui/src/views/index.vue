<template>
  <div class="app-container home">
    <el-row :gutter="20" style="text-align: center">
      <el-col :sm="24" :lg="8" style="padding-left: 20px">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>JooLun微信管理框架-开源版</span>
          </div>
          <div class="body">
            <img src="https://images.gitee.com/uploads/images/2020/0305/171054_0a84eb49_5079715.jpeg" alt="donate"/>
            <p>此公众号对接了该平台，和该后台数据同步</p>
          </div>
          <el-row>
            <el-col :span="12">
              <h4>后端技术</h4>
              <ul>
                <li>SpringBoot</li>
                <li>Spring Security</li>
                <li>JWT</li>
                <li>MyBatis-Plus</li>
                <li>Druid</li>
                <li>Fastjson</li>
                <li>WXJava</li>
                <li>...</li>
              </ul>
            </el-col>
            <el-col :span="12">
              <h4>前端技术</h4>
              <ul>
                <li>Vue</li>
                <li>Vuex</li>
                <li>AVue</li>
                <li>Element-ui</li>
                <li>Axios</li>
                <li>小程序原生</li>
                <li>...</li>
              </ul>
            </el-col>
          </el-row>
          <p>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-cloudy"
              plain
              @click="goTarget('https://gitee.com/joolun/JooLun-wx')">访问码云</el-button>
            <el-button
              size="mini"
              icon="el-icon-s-home"
              plain
              @click="goTarget('https://www.joolun.com/')">访问官网</el-button>
          </p>
        </el-card>
      </el-col>
      <el-col :sm="24" :lg="16" style="padding-left: 20px">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span style="color: red; font-weight: bold">微服务商业版本（功能更完善，代码更严谨）</span>
          </div>
          <div class="body">
            <el-row :gutter="20">
              <el-col :sm="24" :lg="12">
                <img src="https://joolun-blog.oss-cn-zhangjiakou.aliyuncs.com/qrcode/qrcode_for_gh_6999ad5cfb2f_258.jpg" alt="donate"/>
                <p><span style="font-size: 20px">小程序商城单店版</span></p>
                <el-row>
                  <el-col :span="12">
                    <h4>后端技术（微服务）</h4>
                    <ul>
                      <li>Spring Boot2</li>
                      <li>Spring Security OAuth2</li>
                      <li>Spring Cloud Gateway</li>
                      <li>Nacos</li>
                      <li>OpenFeign</li>
                      <li>MyBatis-Plus</li>
                      <li>Druid</li>
                      <li>WXJava</li>
                      <li>阿里oss、七牛云、腾讯cos、minio</li>
                      <li>...</li>
                    </ul>
                  </el-col>
                  <el-col :span="12">
                    <h4>前端技术</h4>
                    <ul>
                      <li>Vue2</li>
                      <li>Vuex</li>
                      <li>Axios</li>
                      <li>Element-UI</li>
                      <li>Avue</li>
                      <li>color-ui</li>
                      <li>小程序原生</li>
                      <li>...</li>
                    </ul>
                  </el-col>
                </el-row>
                <p>
                  <el-button
                    type="primary"
                    size="mini"
                    icon="el-icon-cloudy"
                    plain
                    @click="goTarget('https://demo.joolun.com/')">后台演示</el-button>
                  <el-button
                    size="mini"
                    icon="el-icon-s-home"
                    plain
                    @click="goTarget('https://www.joolun.com/xcxscb.html')">获取源码</el-button>
                </p>
              </el-col>
              <el-col :sm="24" :lg="12">
                <img src="https://joolun-blog.oss-cn-zhangjiakou.aliyuncs.com/qrcode/qrcode_for_gh_ae23ad0d32af_258.jpg" alt="donate"/>
                <p><span style="font-size: 20px">uniapp商城多店版</span></p>
                <el-row>
                  <el-col :span="12">
                    <h4>后端技术（微服务）</h4>
                    <ul>
                      <li>Spring Boot2</li>
                      <li>Spring Security OAuth2</li>
                      <li>Spring Cloud Gateway</li>
                      <li>Nacos</li>
                      <li>Sentinel</li>
                      <li>Seata</li>
                      <li>OpenFeign</li>
                      <li>MyBatis-Plus</li>
                      <li>Druid</li>
                      <li>WXJava</li>
                      <li>阿里oss、七牛云、腾讯cos、minio</li>
                      <li>...</li>
                    </ul>
                  </el-col>
                  <el-col :span="12">
                    <h4>前端技术</h4>
                    <ul>
                      <li>Vue2</li>
                      <li>Vuex</li>
                      <li>Axios</li>
                      <li>Element-UI</li>
                      <li>Avue</li>
                      <li>color-ui</li>
                      <li>UniApp多端编译</li>
                      <li>小程序、H5、ios App、安卓App</li>
                      <li>极光IM</li>
                      <li>...</li>
                    </ul>
                  </el-col>
                </el-row>
                <p>
                  <el-button
                    type="primary"
                    size="mini"
                    icon="el-icon-cloudy"
                    plain
                    @click="goTarget('https://demo1.joolun.com/')">后台演示</el-button>
                  <el-button
                    size="mini"
                    icon="el-icon-s-home"
                    plain
                    @click="goTarget('https://www.joolun.com/uniappddb.html')">获取源码</el-button>
                </p>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
        <a href="https://www.aliyun.com/minisite/goods?userCode=ktp7i3ac" target="_blank">
          <img src="https://23592599.s21i.faiusr.com/2/ABUIABACGAAgyPjr9gUop57p5gIwyAE4fg.jpg.webp">
        </a>
        <p><el-link href="https://www.aliyun.com/minisite/goods?userCode=ktp7i3ac" type="danger" target="_blank">阿里云领取￥2000红包，服务器低至￥91.80/年</el-link></p>
        <hr />
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      // 版本号
      version: "3.3.0",
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
  },
};
</script>

<style scoped lang="scss">
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


<template>
  <div class="app-container">
    <div
      class="public-account-management clearfix"
      v-loading="saveLoading">
      <!--左边配置菜单-->
      <div class="left">
        <div class="weixin-hd">
          <div class="weixin-title">
            {{ menuName }}
          </div>
        </div>
        <div class="weixin-menu menu_main clearfix">
          <div
            class="menu_bottom"
            v-for="(item, i) of menu.button"
            :key="i">
            <!-- 一级菜单 -->
            <div
              @click="menuFun(i, item)"
              class="menu_item el-icon-s-fold"
              :class="{ active: isActive == i }">
              {{ item.name }}
            </div>
            <!--以下为二级菜单-->
            <div
              class="submenu"
              v-if="isSubMenuFlag == i">
              <template
                v-for="(subItem, k) in item.sub_button"
                :key="k">
                <div
                  v-if="item.sub_button"
                  class="subtitle menu_bottom">
                  <div
                    class="menu_subItem"
                    :class="{ active: isSubMenuActive == i + '' + k }"
                    @click="subMenuFun(subItem, i, k)">
                    {{ subItem.name }}
                  </div>
                </div>
              </template>
              <!--  二级菜单加号， 当长度 小于  5 才显示二级菜单的加号  -->
              <div
                class="menu_bottom menu_addicon"
                v-if="!item.sub_button || item.sub_button.length < 5"
                @click="addSubMenu(i, item)">
                <el-icon><Plus /></el-icon>
              </div>
            </div>
          </div>
          <!-- 一级菜单加号 -->
          <div
            class="menu_bottom menu_addicon"
            v-if="menuKeyLength < 3"
            @click="addMenu">
            <el-icon><Plus /></el-icon>
          </div>
        </div>
        <div class="save_div">
          <!--<el-button class="save_btn" type="warning" size="small" @click="saveFun">保存菜单</el-button>-->
          <el-button
            class="save_btn"
            type="success"
            size="small"
            @click="saveAndReleaseFun"
            >保存并发布菜单</el-button
          >
        </div>
      </div>
      <!--右边配置-->
      <div
        v-if="showRightFlag"
        class="right">
        <div class="configure_page">
          <div class="delete_btn">
            <el-button
              size="small"
              type="danger"
              icon="el-icon-delete"
              @click="deleteMenu(tempObj)"
              >删除当前菜单</el-button
            >
          </div>
          <div>
            <span>菜单名称：</span>
            <el-input
              class="input_width"
              v-model="tempObj.name"
              placeholder="请输入菜单名称"
              :maxlength="nameMaxlength"
              clearable></el-input>
          </div>
          <div v-if="showConfigurContent">
            <div class="menu_content">
              <span>菜单内容：</span>
              <el-select
                v-model="tempObj.type"
                clearable
                placeholder="请选择"
                class="menu_option">
                <el-option
                  v-for="item in menuOptions"
                  :label="item.label"
                  :value="item.value"
                  :key="item.value"></el-option>
              </el-select>
            </div>
            <div
              class="configur_content"
              v-if="tempObj.type == 'view'">
              <span>跳转链接：</span>
              <el-input
                class="input_width"
                v-model="tempObj.url"
                placeholder="请输入链接"
                clearable></el-input>
            </div>
            <div
              class="configur_content"
              v-if="tempObj.type == 'miniprogram'">
              <div class="applet">
                <span>小程序的appid：</span>
                <el-input
                  class="input_width"
                  v-model="tempObj.appid"
                  placeholder="请输入小程序的appid"
                  clearable></el-input>
              </div>
              <div class="applet">
                <span>小程序的页面路径：</span>
                <el-input
                  class="input_width"
                  v-model="tempObj.pagepath"
                  placeholder="请输入小程序的页面路径，如：pages/index"
                  clearable></el-input>
              </div>
              <div class="applet">
                <span>备用网页：</span>
                <el-input
                  class="input_width"
                  v-model="tempObj.url"
                  placeholder="不支持小程序的老版本客户端将打开本网页"
                  clearable></el-input>
              </div>
              <p class="blue">tips:需要和公众号进行关联才可以把小程序绑定带微信菜单上哟！</p>
            </div>
            <div
              class="configur_content"
              v-if="tempObj.type == 'article_view_limited'">
              <el-row>
                <div
                  class="select-item"
                  v-if="tempObj && tempObj.content && tempObj.content.articles">
                  <WxNews :objData="tempObj.content.articles"></WxNews>
                  <el-row class="ope-row">
                    <el-button
                      type="danger"
                      icon="el-icon-delete"
                      circle
                      @click="deleteTempObj"></el-button>
                  </el-row>
                </div>
                <div v-if="!tempObj.content || !tempObj.content.articles">
                  <el-row>
                    <el-col
                      :span="24"
                      style="text-align: center">
                      <el-button
                        type="success"
                        @click="openMaterial"
                        >素材库选择<el-icon class="el-icon--right"><CircleCheck /></el-icon
                      ></el-button>
                    </el-col>
                  </el-row>
                </div>
                <el-dialog
                  title="选择图文"
                  v-model="dialogNewsVisible"
                  width="90%">
                  <WxMaterialSelect
                    :objData="{ repType: 'news' }"
                    @selectMaterial="selectMaterial"></WxMaterialSelect>
                </el-dialog>
              </el-row>
            </div>
            <div
              class="configur_content"
              v-if="tempObj.type == 'click' || tempObj.type == 'scancode_waitmsg'">
              <WxReplySelect
                v-model:objData="tempObj"
                v-if="hackResetWxReplySelect"></WxReplySelect>
            </div>
          </div>
        </div>
      </div>
      <!--一进页面就显示的默认页面，当点击左边按钮的时候，就不显示了-->
      <div
        v-if="!showRightFlag"
        class="right">
        <p>请选择菜单配置</p>
      </div>
    </div>
  </div>
</template>

<script setup name="WxMpMenuDetail">
import { save, saveAndRelease, getList } from "@/api/wxmp/wxmenu";
import WxReplySelect from "@/components/wx-reply/main.vue";
import WxNews from "@/components/wx-news/main.vue";
import WxMaterialSelect from "@/components/wx-material-select/main.vue";
import { computed, getCurrentInstance, nextTick, onMounted, reactive } from "vue";

const { proxy } = getCurrentInstance();

const data = reactive({
  showRightFlag: false, //右边配置显示默认详情还是配置详情
  menu: {
    button: [],
  }, //横向菜单
  isActive: -1, // 一级菜单点中样式
  isSubMenuActive: -1, // 一级菜单点中样式
  isSubMenuFlag: -1, // 二级菜单显示标志
  tempObj: {}, //右边临时变量，作为中间值牵引关系
  tempSelfObj: {
    //一些临时值放在这里进行判断，如果放在tempObj，由于引用关系，menu也会多了多余的参数
  },
  visible2: false, //素材内容  "选择素材"按钮弹框显示隐藏
  tableData: [], //素材内容弹框数据,
  menuName: "",
  showConfigurContent: true,
  nameMaxlength: 0, //名称最大长度
  menuOptions: [
    {
      value: "view",
      label: "跳转网页",
    },
    {
      value: "miniprogram",
      label: "跳转小程序",
    },
    {
      value: "click",
      label: "点击回复",
    },
    {
      value: "article_view_limited",
      label: "跳转图文消息",
    },
    {
      value: "scancode_push",
      label: "扫码直接返回结果",
    },
    {
      value: "scancode_waitmsg",
      label: "扫码回复",
    },
    {
      value: "pic_sysphoto",
      label: "系统拍照发图",
    },
    {
      value: "pic_photo_or_album",
      label: "拍照或者相册",
    },
    {
      value: "pic_weixin",
      label: "微信相册",
    },
    {
      value: "location_select",
      label: "选择地理位置",
    },
  ],
  dialogNewsVisible: false,
  saveLoading: false,
  hackResetWxReplySelect: false,
});
const { showRightFlag, menu, isActive, isSubMenuActive, isSubMenuFlag, tempObj, tempSelfObj, menuName, showConfigurContent, nameMaxlength, menuOptions, dialogNewsVisible, saveLoading, hackResetWxReplySelect } = toRefs(data);

const menuKeyLength = computed(() => {
  return data.menu.button.length;
});

function onCreated() {
  getMenuFun();
}
onCreated();

onMounted(() => {});

function deleteTempObj() {}

function openMaterial() {
  data.dialogNewsVisible = true;
}

function selectMaterial(item) {
  if (item.content.articles.length > 1) {
    proxy.$alert("您选择的是多图文，将默认跳转第一篇", "提示", {
      confirmButtonText: "确定",
    });
  }
  data.dialogNewsVisible = false;
  data.tempObj.article_id = item.articleId;
  data.tempObj.mediaName = item.name;
  data.tempObj.url = item.url;
  item.mediaType = data.tempObj.mediaType;
  item.content.articles = item.content.articles.slice(0, 1);
  data.tempObj.content = item.content;
}

function getMenuFun() {
  getList().then((res) => {
    data.menu.button = JSON.parse(res.msg).button;
  });
}

function handleClick(tab, event) {
  data.tempObj.mediaType = tab.paneName;
}

function saveAndReleaseFun() {
  proxy
    .$confirm("确定要保证并发布该菜单吗?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      data.saveLoading = true;
      saveAndRelease({
        strWxMenu: data.menu,
      })
        .then((response) => {
          data.saveLoading = false;
          if (response.code == 200) {
            proxy.$message({
              showClose: true,
              message: "发布成功",
              type: "success",
            });
          } else {
            proxy.$message.error(response.data.msg);
          }
        })
        .catch(() => {
          data.saveLoading = false;
        });
    })
    .catch(() => {});
}

function menuFun(i, item) {
  data.hackResetWxReplySelect = false; //销毁组件
  nextTick(() => {
    data.hackResetWxReplySelect = true; //重建组件
  });
  data.showRightFlag = true; //右边菜单
  data.tempObj = item; //这个如果放在顶部，flag会没有。因为重新赋值了。
  data.tempSelfObj.grand = "1"; //表示一级菜单
  data.tempSelfObj.index = i; //表示一级菜单索引
  data.isActive = i; //一级菜单选中样式
  data.isSubMenuFlag = i; //二级菜单显示标志
  data.isSubMenuActive = -1; //二级菜单去除选中样式
  data.nameMaxlength = 4;
  if (item.sub_button && item.sub_button.length > 0) {
    data.showConfigurContent = false;
  } else {
    data.showConfigurContent = true;
  }
}

function subMenuFun(subItem, index, k) {
  data.hackResetWxReplySelect = false; //销毁组件
  nextTick(() => {
    data.hackResetWxReplySelect = true; //重建组件
  });
  data.showRightFlag = true; //右边菜单
  data.tempObj = subItem; //将点击的数据放到临时变量，对象有引用作用
  data.tempSelfObj.grand = "2"; //表示二级菜单
  data.tempSelfObj.index = index; //表示一级菜单索引
  data.tempSelfObj.secondIndex = k; //表示二级菜单索引
  data.isSubMenuActive = index + "" + k; //二级菜单选中样式
  data.isActive = -1; //一级菜单去除样式
  data.showConfigurContent = true;
  data.nameMaxlength = 7;
}

function addMenu() {
  let menuKeyLength = menuKeyLength.value;
  let addButton = {
    name: "菜单名称",
    sub_button: [],
  };
  data.menu.button[menuKeyLength] = addButton;
  menuFun(menuKeyLength.value - 1, addButton);
}

function addSubMenu(i, item) {
  if (!item.sub_button || item.sub_button.length <= 0) {
    item["sub_button"] = [];
    data.showConfigurContent = false;
  }
  let subMenuKeyLength = item.sub_button.length; //获取二级菜单key长度
  let addButton = {
    name: "子菜单名称",
  };
  item.sub_button[subMenuKeyLength] = addButton;
  subMenuFun(item.sub_button[subMenuKeyLength], i, subMenuKeyLength);
}

function deleteMenu(obj) {
  proxy
    .$confirm("确定要删除吗?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(() => {
      deleteData(); // 删除菜单数据
      data.tempObj = {};
      data.showRightFlag = false;
      data.isActive = -1;
      data.isSubMenuActive = -1;
    })
    .catch(() => {});
}

function deleteData() {
  // 一级菜单的删除方法
  if (data.tempSelfObj.grand == "1") {
    data.menu.button.splice(data.tempSelfObj.index, 1);
  }
  // 二级菜单的删除方法
  if (data.tempSelfObj.grand == "2") {
    data.menu.button[data.tempSelfObj.index].sub_button.splice(data.tempSelfObj.secondIndex, 1);
  }
  proxy.$message({
    type: "success",
    message: "删除成功!",
  });
}
</script>

<style lang="scss" scoped="scoped">
.clearfix {
  *zoom: 1;
}
.clearfix::after {
  content: "";
  display: table;
  clear: both;
}
div {
  text-align: left;
}
.weixin-hd {
  color: #fff;
  text-align: center;
  position: relative;
  bottom: 426px;
  left: 0px;
  width: 300px;
  height: 64px;
  background: transparent url(assets/menu_head.png) no-repeat 0 0;
  background-position: 0 0;
  background-size: 100%;
}
.weixin-title {
  color: #fff;
  font-size: 14px;
  width: 100%;
  text-align: center;
  position: absolute;
  top: 33px;
  left: 0px;
}
.weixin-menu {
  background: transparent url(assets/menu_foot.png) no-repeat 0 0;
  padding-left: 43px;
  font-size: 12px;
}
.menu_option {
  width: 40% !important;
}
.public-account-management {
  min-width: 1200px;
  width: 1200px;
  margin: 0 auto;
  .left {
    float: left;
    display: inline-block;
    width: 350px;
    height: 715px;
    background: url("assets/iphone_backImg.png") no-repeat;
    background-size: 100% auto;
    padding: 518px 25px 88px;
    position: relative;
    box-sizing: border-box;
    /*第一级菜单*/
    .menu_main {
      .menu_bottom {
        position: relative;
        float: left;
        display: inline-block;
        box-sizing: border-box;
        width: 85.5px;
        text-align: center;
        border: 1px solid #ebedee;
        background-color: #fff;
        cursor: pointer;
        &.menu_addicon {
          height: 46px;
          line-height: 46px;
        }
        .menu_item {
          height: 44px;
          line-height: 44px;
          text-align: center;
          box-sizing: border-box;
          width: 100%;
          &.active {
            border: 1px solid #2bb673;
          }
        }
        .menu_subItem {
          height: 44px;
          line-height: 44px;
          text-align: center;
          box-sizing: border-box;
          &.active {
            border: 1px solid #2bb673;
          }
        }
      }
      i {
        color: #2bb673;
      }
      /*第二级菜单*/
      .submenu {
        position: absolute;
        width: 85.5px;
        bottom: 45px;
        .subtitle {
          background-color: #fff;
          box-sizing: border-box;
        }
      }
    }
    .save_div {
      margin-top: 15px;
      text-align: center;
      .save_btn {
        bottom: 20px;
        left: 100px;
      }
    }
  }
  /*右边菜单内容*/
  .right {
    float: left;
    width: 63%;
    background-color: #e8e7e7;
    padding: 20px;
    margin-left: 20px;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    .configure_page {
      .delete_btn {
        text-align: right;
        margin-bottom: 15px;
      }
      .menu_content {
        margin-top: 20px;
      }
      .configur_content {
        margin-top: 20px;
        background-color: #fff;
        padding: 20px 10px;
        border-radius: 5px;
      }
      .blue {
        color: #29b6f6;
        margin-top: 10px;
      }
      .applet {
        margin-bottom: 20px;
        span {
          width: 20%;
        }
      }
      .material {
        .input_width {
          width: 30%;
        }
        .el-textarea {
          width: 80%;
        }
      }
    }
  }
}
</style>

<style lang="scss" scoped>
.public-account-management {
  .el-input {
    width: 70%;
    margin-right: 2%;
  }
}
</style>

<style lang="scss" scoped>
.pagination {
  text-align: right;
  margin-right: 25px;
}
.select-item {
  width: 280px;
  padding: 10px;
  margin: 0 auto 10px auto;
  border: 1px solid #eaeaea;
}
.select-item2 {
  padding: 10px;
  margin: 0 auto 10px auto;
  border: 1px solid #eaeaea;
}
.ope-row {
  padding-top: 10px;
  text-align: center;
}
.item-name {
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
}
</style>

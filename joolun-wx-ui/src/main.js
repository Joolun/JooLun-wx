import { createApp } from "vue";

import Cookies from "js-cookie";

import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import locale from "element-plus/es/locale/lang/zh-cn";

import "@/assets/styles/index.scss"; // global css

import App from "./App";
import store from "./store";
import router from "./router";
import directive from "./directive"; // directive

// 注册指令
import plugins from "./plugins"; // plugins
import { download } from "@/utils/request";

// svg图标
import "virtual:svg-icons-register";
import SvgIcon from "@/components/SvgIcon";
import elementIcons from "@/components/SvgIcon/svgicon";

import "./permission"; // permission control

import { useDict } from "@/utils/dict";
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels } from "@/utils/ruoyi";

// 分页组件
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar";
// 富文本组件
import Editor from "@/components/Editor";
// 文件上传组件
import FileUpload from "@/components/FileUpload";
// 图片上传组件
import ImageUpload from "@/components/ImageUpload";
// 图片预览组件
import ImagePreview from "@/components/ImagePreview";
// 自定义树选择组件
import TreeSelect from "@/components/TreeSelect";
// 字典标签组件
import DictTag from "@/components/DictTag";

const app = createApp(App);

// 全局方法挂载
app.config.globalProperties.useDict = useDict;
app.config.globalProperties.download = download;
app.config.globalProperties.parseTime = parseTime;
app.config.globalProperties.resetForm = resetForm;
app.config.globalProperties.handleTree = handleTree;
app.config.globalProperties.addDateRange = addDateRange;
app.config.globalProperties.selectDictLabel = selectDictLabel;
app.config.globalProperties.selectDictLabels = selectDictLabels;

// 全局组件挂载
app.component("DictTag", DictTag);
app.component("Pagination", Pagination);
app.component("TreeSelect", TreeSelect);
app.component("FileUpload", FileUpload);
app.component("ImageUpload", ImageUpload);
app.component("ImagePreview", ImagePreview);
app.component("RightToolbar", RightToolbar);
app.component("Editor", Editor);

app.use(router);
app.use(store);
app.use(plugins);
app.use(elementIcons);
app.component("svg-icon", SvgIcon);

directive(app);

// 使用element-plus 并且设置全局的大小
app.use(ElementPlus, {
  locale: locale,
  // 支持 large、default、small
  size: Cookies.get("size") || "default",
});
/*********  JooLun 新增   *************/
import { filterForm } from "@/utils/ruoyi";
//空值过滤器
app.config.globalProperties.filterForm = filterForm;
// 增加avue的引入 https://avuejs.com/docs/installation.html
import Avue from "@smallwei/avue";
import "@smallwei/avue/lib/index.css";
// avue的网络字典请求
import request from "@/utils/request";
window.axios = request;
// avue全局配置 https://avuejs.com/docs/global.html
app.use(Avue, {
  axios: request,
  // avue文件上传配置(也可以用ruoyi自带的文件图片上传组件)具体使用可查看goodscategory.vue文件
  ali: {
    // 阿里云配置,  配置文档: https://avuejs.com/docs/global.html
    region: "",
    endpoint: "",
    accessKeyId: "",
    accessKeySecret: "",
    bucket: "",
  },
});

app.mount("#app");

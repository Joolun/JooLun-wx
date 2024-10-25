### [阿里云领取￥ 2000 红包，服务器低至￥ 91.80/年](https://promotion.aliyun.com/ntms/yunparter/invite.html?userCode=wnw8gle1)

### 注意

1. 本软件基于 RuoYi v3.8.8 开发研制 https://github.com/yangzongzhuan/RuoYi-Vue3

## 平台简介

- 本仓库为前端技术栈 [Vue3](https://v3.cn.vuejs.org) + [Element Plus](https://element-plus.org/zh-CN) + [Vite](https://cn.vitejs.dev) 版本。
- 配套后端代码仓库地址[RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 或 [RuoYi-Vue-fast](https://github.com/yangzongzhuan/RuoYi-Vue-fast) 版本。
- 前端技术栈（[Vue2](https://cn.vuejs.org) + [Element](https://github.com/ElemeFE/element) + [Vue CLI](https://cli.vuejs.org/zh)），请移步[RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue/tree/master/ruoyi-ui)。

## 前端运行

### 版本说明

```bash
Node = "^18.0.0 || >=20.0.0"
```

### 运行

```bash
# 进入项目目录
cd joolun-wx-ui
```

### 使用 npm 安装依赖

```bash
# ruoyi推荐yarn,但是可能有些js三方库安装不到,这里建议使用npm
npm install --registry=https://registry.npmmirror.com
```

### 本地启动服务

```bash
npm run dev
```

### 发布

```bash
# 构建测试环境
npm run build:stage

# 运行发布命令报错解决: Cannot find package 'fast-glob'
# 解决方法: npm add fast-glob -D 或者 yarn add fast-glob -D

# 构建生产环境
npm run build:prod
```

## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持 CRUD 下载 。
14. 系统接口：根据业务代码自动生成相关的 api 接口文档。
15. 服务监控：监视当前系统 CPU、内存、磁盘、堆栈等相关信息。
16. 缓存监控：对系统的缓存信息查询，命令统计等。
17. 在线构建器：拖动表单元素生成相应的 HTML 代码。
18. 连接池监视：监视当前系统数据库连接池状态，可进行分析 SQL 找出系统性能瓶颈。
19. 公众号管理模块
20. 小程序管理模块
21. 商城管理模块

### 重要信息

- 商业版功能更完善，代码更严谨，详情请访问（https://www.joolun.com/）

![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-1.jpg)
![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-2.jpg)
![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-3.jpg)
![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-4.jpg)
![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-5.jpg)
![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-6.jpg)
![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-7.jpg)
![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-8.jpg)
![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-9.jpg)
![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-10.jpg)
![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-11.jpg)
![输入图片说明](https://joolun-plus-1313274050.cos.ap-nanjing.myqcloud.com/joolun-uniapp-wx-ui/Joolun-PPT-14.gif)

<table>
    <tr>
        <td><img src="https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg2s7hmAYo_sam-wMw6Ac42AQ.png.webp"/></td>
        <td><img src="https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg2s7hmAYoisCngQYw6Ac42AQ.png.webp"/></td>
    </tr>
    <tr>
        <td><img src="https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg2c7hmAYo6PLorQcw6Ac42AQ.png.webp"/></td>
        <td><img src="https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg2s7hmAYo-duw0gQw6Ac42AQ.png.webp"/></td>
    </tr>
    <tr>
        <td><img src="https://23592599.s21i.faiusr.com/4/ABUIABAEGAAguc-hmAYopdHjxQUw6Ac42AQ.png.webp"/></td>
        <td><img src="https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg2s7hmAYowPiB2AIw6Ac42AQ.png.webp"/></td>
    </tr>
</table>

# JooLun微信商城

#### 介绍
1. Spring Cloud微服务化开发，采用Nacos注册和配置中心，具有统一授权、认证后台管理系统， 其中包含具备用户管理、资源权限管理、数据导出、打印、Swagger API 管理等多个模块，支持多业务系统并行开发，可以作为后端服务的开发脚手架。 代码简洁，架构清晰，restful接口规范，是学习、毕设、接私活、实际项目的不二选择
1. 采用最新前后端完全分离框架（Spring Cloud+Spring Boot+Vue2+Element ui）
1. 前端封装微信专用vue组件，开发中实现灵活调用，杜绝重复造轮子，让前端开发更容易
1. 微信接口采用WxJava（微信开发 Java SDK），开发中无需一个个对接微信接口，直接方法调用。极大的提高了微信开发效率
1. 第三方平台全网发布、支持多公众号，redis全局缓存access_token
1. 小程序官方原生框架开发，采用纯css框架ColorUI，不冗余，一套代码多端覆盖；商城数据库严格按照通用商城业务设计，绝不含糊，方便二次开发拓展

#### 商城演示
![小程序商城扫码演示](https://images.gitee.com/uploads/images/2019/1009/094717_f5a9a16d_5079715.jpeg "c119f0d1694aeabc99344814d9b3fe4a.jpg")
[了解更多](http://www.joolun.com)
[后台演示](http://demo.joolun.com)

#### 软件架构
1. 核心框架：Spring Boot2 + Spring Cloud Alibaba + Spring Cloud Gateway 。
1. 安全框架：Spring Security OAuth2。
1. 前端框架：Vue + element-ui + avue。
1. 持久层框架：MyBatis-plus。
1. 微信开发 Java SDK：WxJava 。
1. 文件管理：阿里OSS、minio。
1. JDK：java8+mysql5.7+
1. 图表插件：Echarts
1. 小程序框架：原生框架+ColorUI

#### 功能模块
一、小程序商城模块
1. 小程序官方原生框架开发，采用纯css框架ColorUI，不冗余，一套代码多端覆盖；商城数据库严格按照通用商城业务设计，绝不含糊，方便二次开发拓展
1. 多规格sku商品管理
1. 商品分类管理
1. 商品评价管理
1. 订单管理
1. 实时物流
1. 用户购物车
1. 用户收藏
1. 用户收货地址管理
1. 小程序用户信息管理
1. 更多小程序商城功能在持续开发中

二、微信公众号模块
1. 灵活封装微信SDK，直接调用，专会微信开发量身打造
1. 公众号多账号管理，不同用户管理不同公众号
1. 第三方平台全网发布
1. 微信自定义菜单管理
1. 关注欢迎语，消息自动回复
1. 关键字回复管理
1. 微信消息群发管理
1. 微信素材管理
1. 微信用户粉丝管理
1. 微信用户消息管理
1. 微信用户实时聊天功能
1. 微信AccessToken管理
1. 微信用户标签管理
1. 微信数据统计

三、后台基础模块
1. 该有的权限管理后台功能，我们都有，代码一键生成，告别烦人的增、删、改、查，开发效率直线提高
1. 系统菜单管理
1. 系统用户管理
1. 系统角色管理
1. 系统部门管理
1. 基于mybatis plus实现多租户管理
1. 数据权限
1. 日志管理
1. 字典管理
1. 令牌管理
1. 代码生成器
1. 数据导出、打印


#### 截图
![输入图片说明](https://images.gitee.com/uploads/images/2019/1009/094925_c1a30b27_5079715.jpeg "d3.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1009/094911_f6c62715_5079715.jpeg "d2.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1009/094902_e9474d39_5079715.jpeg "d4.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1009/094852_4fd2d160_5079715.jpeg "d5.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1009/094842_a9fdc49c_5079715.jpeg "d6.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1009/094824_642cd2d3_5079715.jpeg "d8.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1009/094812_fddf1bc7_5079715.jpeg "d1.jpg")
![自定义菜单](https://images.gitee.com/uploads/images/2019/0615/235522_4a27ee4a_5079715.gif "wx-menu.gif")
![微信粉丝实时聊天](https://images.gitee.com/uploads/images/2019/0615/235540_d512fa59_5079715.gif "liaotian.gif")
![输入图片说明](https://images.gitee.com/uploads/images/2019/0615/235616_dc33cdea_5079715.png "QQ截图20190612232849.png")
![多账号管理](https://images.gitee.com/uploads/images/2019/0626/094510_96c2c21a_5079715.png "屏幕截图.png")
[演示地址](http://demo.joolun.com)
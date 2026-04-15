[![JooLun广告位.jpg](zyplayer-doc-wiki/common/file?uuid=69336cd0f6bc4a11a4c806efb8b0daae)](https://www.joolun.com/)
### 小程序登录
* 先看下微信官方的文档和流程图

[官方文档](https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html)

![登录流程时序](https://res.wx.qq.com/wxdoc/dist/assets/img/api-login.2fcc9f35.jpg)
* 登录接口实现，具体逻辑请读登录接口源码WxUserApi：ma/wxuser/login，登录接口会生成一个自定义session，保存在redis中，之后的每个小程序接口都要带上这个session才能访问
### 小程序登录成功
* 通过登录接口返回后会获得一个自定义session，小程序会将自定义session放入全局变量（joolun-ma/app.js）

```
api.login({
  jsCode: res.code
})
  .then(res => {
    let wxUser = res.data
    that.globalData.thirdSession = wxUser.sessionKey
    that.globalData.wxUser = wxUser
    resolve("success")
  })
```
### 小程序接口header自动赋参
* 读joolun-wx-ma/utils/api.js源码，可知在每次接口请求前，我们会自动给接口header中带上app-id和thirdSession（登录接口返回的自定义session），所以我们自己加接口的时间无需维护app-id和thirdSession

```
wx.request({
      url: _url,
      method: method,
      data: data,
      header: {
        'app-id': wx.getAccountInfoSync().miniProgram.appId,
        'third-session': getApp().globalData.thirdSession
      }
 })
```
### 小程序接口新增
* 假如要加一个商品查询接口goodsGet，在joolun-wx-ma/utils/api.js中添加如下代码

```
goodsGet: (id) => {
    return request('/weixin/api/ma/goodsspu/' + id, 'get', null, false)
  },
```
* 其他页面就可以直接通过app.api.xx调用了

```
app.api.goodsGet(id)
      .then(res => {
        let goodsDetail = res.data
        this.setData({
          goodsDetail: goodsDetail
        })
      })
```
### 后台接收到接口调用后处理
* 我们统一将非后台的相关接口放在各项目的com.joolun.web.api目录下
* 我们以购物车接口为例（ShoppingCartApi.java）
```
/**
 * 分页查询
 * @param page 分页对象
 * @param shoppingCart 购物车
 * @return
 */
@ApiOperation(value = "分页查询")
   @GetMapping("/page")
   public AjaxResult getShoppingCartPage(Page page, ShoppingCart shoppingCart) {
   shoppingCart.setUserId(ThirdSessionHolder.getWxUserId());
   return AjaxResult.success(shoppingCartService.page2(page, shoppingCart));
   }
```
* 增加ThirdSession拦截器（ThirdSessionInterceptor.java）
  当小程序访问接口时我们需要校验当前小程序用户ThirdSession的真实性，所以我们需要加一个拦截器（ThirdSessionInterceptor），统一拦截校验ThirdSession
```
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
   //获取header中的thirdSession
   String thirdSessionHeader = request.getHeader(ConfigConstant.HEADER_THIRDSESSION);
   if(StrUtil.isNotBlank(thirdSessionHeader)){
      //获取缓存中的ThirdSession
      String key = WxMaConstants.THIRD_SESSION_BEGIN  + ":" + thirdSessionHeader;
      Object thirdSessionObj = redisTemplate.opsForValue().get(key);
      if(thirdSessionObj == null) {//session过期
         AjaxResult r = AjaxResult.error(MyReturnCode.ERR_60001.getCode(), MyReturnCode.ERR_60001.getMsg());
         this.writerPrint(response, r);
         return Boolean.FALSE;
      }else {
         String thirdSessionStr = String.valueOf(thirdSessionObj);
         ThirdSession thirdSession = JSONUtil.toBean(thirdSessionStr, ThirdSession.class);
         ThirdSessionHolder.setThirdSession(thirdSession);//设置thirdSession
      }
   }else{
      AjaxResult r = AjaxResult.error(MyReturnCode.ERR_60002.getCode(), MyReturnCode.ERR_60002.getMsg());
      this.writerPrint(response, r);
      return Boolean.FALSE;
   }
   return Boolean.TRUE;
}
```
* 启用拦截器（WebConfig.java）
```
/**
 * 拦截器
 * @param registry
 */
@Override
public void addInterceptors(InterceptorRegistry registry) {
   /**
    * 进入ThirdSession拦截器
    */
   registry.addInterceptor(new ThirdSessionInterceptor(redisTemplate))
         .addPathPatterns("/weixin/api/**")//拦截/api/**接口
         .excludePathPatterns("/weixin/api/ma/wxuser/login",
               "/weixin/api/ma/orderinfo/notify-order",
               "/weixin/api/ma/orderinfo/notify-logisticsr",
               "/weixin/api/ma/orderrefunds/notify-refunds");//放行接口
}
```
当我们有接口在/api/下，但不需要进入ThirdSession拦截器，比如订单支付回调接口，我们只需将接口加入到excludePathPatterns中即可，如上面代码所示，这样就不会报“session不能为空”的错了

* 当接口返回session过期60001错误时，小程序端会重新加载当前页，重新触发登录获取新的session，用户无感知
```
joolun-wx-ma\utils\api.js
if(res.data.code == 60001){
   //session过期，则清除过期session，并重新加载当前页
   getApp().globalData.thirdSession = null
      wx.reLaunch({
         url: getApp().getCurrentPageUrlWithArgs()
      })
  }
```
### postman调试
* 通过上面原理介绍得知，我们只要知道thirdSession就能直接用postman调试
* thirdSession我们可以通过微信开发者工具获取，编译一下，小程序会调用登录接口，从登录接口我们可以获取到thirdSession
  ![39a4e4b1160b4814b78743a967108818.png](zyplayer-doc-wiki/common/file?uuid=0dfca294ea7c4feaac275ea79b7cf0ff)

* postman调试
  ![QQ截图20200418143355.png](zyplayer-doc-wiki/common/file?uuid=2205205a773345ae9e3e07f9373d3853)
### 获取当前小程序用户的信息
* 以购物车查询接口（/api/ma/shoppingcart/page）为例：我们需要获取当前用户的购物车数据，那么当前用户的ID我们怎么获取到呢？看以下代码便知
```
/**
 * 分页查询
 * @param page 分页对象
 * @param shoppingCart 购物车
 * @return
 */
@ApiOperation(value = "分页查询")
   @GetMapping("/page")
   public AjaxResult getShoppingCartPage(Page page, ShoppingCart shoppingCart) {
   shoppingCart.setUserId(ThirdSessionHolder.getWxUserId());
   return AjaxResult.success(shoppingCartService.page2(page, shoppingCart));
   }
```
```
//获取当前用户的商城用户ID
ThirdSessionHolder.getMallUserId()
//获取当前用户的ThirdSession对象
ThirdSessionHolder.getThirdSession()
```

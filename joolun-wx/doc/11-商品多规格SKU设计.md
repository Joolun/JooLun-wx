# 商品多规格 SKU 设计

## 1. 设计目标

当前商品模型采用标准的多规格商品设计，目标如下：

- 商品主表与销售单元职责分离
- 购物车和订单项围绕具体 `sku_id` 工作
- 后台可维护规格维度、规格值和 SKU 组合
- 小程序可准确表达“颜色 + 尺码”这类规格组合

当前设计约束如下：

- 不考虑旧数据兼容
- 表名统一采用商城前缀 `mall_`
- 命名风格参考 `joolun` 项目
- 不引入多租户及当前项目暂不需要的冗余字段

## 2. 目标模型

### 2.1 SPU 与 SKU 分工

#### SPU

`SPU` 用于表达商品主档，主要承载：

- 商品名称
- 卖点
- 图文详情
- 分类归属
- 主图列表
- 上下架状态
- 排序
- 价格区间

#### SKU

`SKU` 用于表达可独立销售的最小单元，主要承载：

- SKU 编码
- SKU 图片
- 销售价
- 市场价
- 成本价
- 库存
- 启用状态

一句话概括：

- `SPU` 负责“商品展示”
- `SKU` 负责“真实售卖”

## 3. 数据库设计

## 3.1 核心表

### `mall_goods_spu`

商品主表。

当前重点字段：

- `id`
- `spu_code`
- `name`
- `sell_point`
- `description`
- `category_first`
- `category_second`
- `pic_urls`
- `shelf`
- `sort`
- `price_down`
- `price_up`
- `sale_num`
- `spec_type`

说明：

- 原单规格时期的 `sales_price`、`market_price`、`cost_price`、`stock` 已从主表移出
- `price_down`、`price_up` 用于展示当前商品启用 SKU 的价格区间
- `spec_type` 用于标识当前商品是单规格还是多规格

### `mall_goods_sku`

SKU 主表。

当前重点字段：

- `id`
- `sku_code`
- `spu_id`
- `pic_url`
- `sales_price`
- `market_price`
- `cost_price`
- `stock`
- `enable`

说明：

- 一个 `SPU` 对应多个 `SKU`
- 实际下单、购物车、库存校验均以 `SKU` 为准

### `mall_goods_spu_spec`

SPU 规格定义表。

用于保存当前商品有哪些规格维度，例如：

- 颜色
- 尺码
- 容量

当前重点字段：

- `id`
- `spu_id`
- `spec_id`
- `spec_name`
- `sort`

### `mall_goods_sku_spec_value`

SKU 规格值关系表。

用于保存某个 SKU 对应的规格值组合，例如：

- 红色 + M
- 黑色 + L

当前重点字段：

- `id`
- `spu_id`
- `sku_id`
- `spec_value_id`
- `sort`

## 3.2 关联交易表

多规格商品不只影响商品表，还影响交易快照表。

### `mall_shopping_cart`

新增字段：

- `sku_id`
- `spec_info`

说明：

- 购物车记录现在绑定的是具体 SKU
- `spec_info` 用于直接展示规格摘要，避免页面每次重新拼装

### `mall_order_item`

新增字段：

- `sku_id`
- `spec_info`

说明：

- 订单项在下单时会固化当前 SKU 快照
- 后续即使商品改价、改规格，订单展示仍保持一致

## 4. 后端实体模型

当前后端关键实体：

- `GoodsSpu`
- `GoodsSku`
- `GoodsSpuSpec`
- `GoodsSkuSpecValue`
- `ShoppingCart`
- `OrderItem`

### `GoodsSpu`

当前代码中，`GoodsSpu` 除了主表字段，还包含以下非持久化扩展字段：

- `skus`
  当前商品完整 SKU 列表
- `spuSpec`
  当前商品绑定的规格定义
- `salesPrice`
- `marketPrice`
- `costPrice`
- `stock`

说明：

- 这些兼容字段主要用于前端回显或兼容旧展示逻辑
- 真实销售数据仍应以 `GoodsSku` 为准

### `GoodsSku`

当前代码中，`GoodsSku` 额外包含：

- `specs`
  当前 SKU 对应的规格值明细
- `name`
  规格展示文案

### `ShoppingCart`

当前购物车记录包含：

- `spuId`
- `skuId`
- `addPrice`
- `picUrl`
- `specInfo`

### `OrderItem`

当前订单项快照包含：

- `spuId`
- `skuId`
- `salesPrice`
- `paymentPrice`
- `picUrl`
- `specInfo`

## 5. 后台管理端设计

## 5.1 管理目标

后台商品页需要解决 4 个问题：

- 维护商品基础信息
- 维护规格维度与规格值
- 自动生成 SKU 组合
- 维护每个 SKU 的价格、库存、图片、启用状态

## 5.2 当前交互原则

### 规格维度维护

例如：

- 颜色
- 尺码
- 版本

### 规格值维护

当前已对录入体验做过优化：

- 不再单纯依赖英文逗号输入
- 更适合运营人员逐项维护规格值

### SKU 组合生成

当前后台页会根据规格维度和值生成组合表。

例如：

- 颜色：红、黑
- 尺码：M、L

会生成：

- 红 / M
- 红 / L
- 黑 / M
- 黑 / L

### SKU 明细维护

每一行 SKU 需要维护：

- 销售价
- 市场价
- 成本价
- 库存
- SKU 图片
- 启用状态

## 6. 小程序端设计

## 6.1 商品详情页

当前小程序商品详情页已改为类似淘宝的底部规格弹窗。

目的：

- 提升移动端选择效率
- 让规格切换、数量调整、加购、立即购买走统一流程

## 6.2 规格选择逻辑

当前前端处理逻辑包括：

- 根据 SPU 数据还原规格组
- 根据当前选中规格组合匹配 SKU
- 自动禁用无效规格值
- 规格切换后同步价格、库存、图片、摘要

用户侧感知表现：

- 已选规格会高亮
- 不存在的组合会置灰禁用
- 加购、立即购买、购物车改规格共用同一套弹窗流程

## 6.3 交易链路变化

### 加入购物车

提交内容以当前选中 SKU 为准，重点包括：

- `spuId`
- `skuId`
- `quantity`
- `salesPrice`
- `picUrl`
- `specInfo`

### 立即购买

下单确认页读取的是商品详情页或购物车页预写入的 SKU 快照，而不是重新按 SPU 推导。

### 购物车改规格

当前购物车“修改规格”会回到商品详情页，并带回：

- `shoppingCartId`
- `skuId`

修改完成后覆盖原购物车记录。

## 7. 价格与库存口径

多规格商品场景下，统一以下业务口径：

- 商品展示价格区间来自 `mall_goods_spu.price_down` 和 `price_up`
- 实际销售价格来自 `mall_goods_sku.sales_price`
- 实际库存来自 `mall_goods_sku.stock`
- 购物车价格快照来自加入购物车时的 SKU 价格
- 订单项价格快照来自提交订单时的 SKU 价格

不再允许直接用 SPU 主表字段作为真实交易价格和库存。

## 8. 当前模型优势

当前商品模型具备以下优势：

- 能支持标准多规格商品
- 购物车、订单、售后链路和商品规格保持一致
- 订单可保留 SKU 快照，避免后续展示失真
- 小程序交互更接近成熟电商产品
- 后续继续扩展批量改价、批量启停、批量库存调整会更容易

## 9. 扩展方向

可继续扩展的方向：

- SKU 批量填充价格和库存
- SKU 图片批量继承主图
- 规格模板复用
- SKU 编码自动生成规则
- 商品导入导出适配多规格
- 售后和客服场景中展示更明确的规格摘要

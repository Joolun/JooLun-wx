/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

/**
 * 商品详情页规格弹窗说明：
 * 1. 规格选择统一改成淘宝式底部弹窗，详情页主体只展示已选摘要。
 * 2. 规格值会根据当前已选组合动态置灰，避免选出不存在或无库存的 SKU。
 * 3. 加购、立即购买、购物车改规格三条链路共用同一套弹窗和 SKU 提交流程。
 */
const WxParse = require('../../../public/wxParse/wxParse.js')
const util = require('../../../utils/util.js')
const app = getApp()

Page({
  data: {
    config: app.globalData.config,
    goodsSpu: null,
    serviceGuarantees: [
      '正品保障，商品信息以实际发货为准',
      '下单后可在订单详情查看售后进度',
      '规格、数量和库存以下单时确认为准'
    ],
    purchaseNotes: [
      '已发货订单请先签收并核对商品，如有问题再发起售后',
      '退款申请需按订单项提交，不同商品会分别进入售后流程',
      '商家审核后，退款结果会回写到订单详情和售后中心'
    ],
    selectedSku: null,
    selectedSkuId: '',
    selectedSpecMap: {},
    skuSpecGroups: [],
    selectedSpecSummary: '',
    selectedSkuPicUrl: '',
    currents: 1,
    cartNum: 1,
    shoppingCartCount: 0,
    shareShow: '',
    modalService: '',
    skuPopupVisible: false,
    skuActionType: '',
    shoppingCartId: '',
    posterShow: false,
    posterUrl: '',
    posterConfig: null
  },
  onLoad(options) {
    // 支持普通跳转、扫码进入、购物车改规格回跳三种入口。
    let id
    if (options.scene) {
      id = decodeURIComponent(options.scene)
    } else {
      id = options.id
    }
    this.setData({
      id: id,
      initSkuId: options.skuId || '',
      shoppingCartId: options.shoppingCartId || ''
    })
    app.initPage()
      .then(() => {
        this.goodsGet(id, options.skuId)
        this.shoppingCartCount()
      })
  },
  onShareAppMessage() {
    const goodsSpu = this.data.goodsSpu
    const title = goodsSpu.name
    const imageUrl = util.getFirstPicUrl(goodsSpu.picUrls)
    const path = 'pages/goods/goods-detail/index?id=' + goodsSpu.id + (this.data.selectedSkuId ? '&skuId=' + this.data.selectedSkuId : '')
    return {
      title: title,
      path: path,
      imageUrl: imageUrl,
      success(res) {
        if (res.errMsg == 'shareAppMessage:ok') {
          console.log(res.errMsg)
        }
      },
      fail() {
      }
    }
  },
  goodsGet(id, skuId) {
    // 商品详情接口返回的是 SPU 主体，这里补齐前端展示所需的 SKU 和规格字段。
    app.api.goodsGet(id)
      .then(res => {
        const goodsSpu = this.normalizeGoodsSpu(res.data)
        const selectedSku = this.pickDefaultSku(goodsSpu, skuId)
        this.syncSelectedSku(goodsSpu, selectedSku, () => {
          if (this.data.shoppingCartId) {
            this.showSkuPopup({ currentTarget: { dataset: { type: 'cart' } } })
          }
        })
        WxParse.wxParse('description', 'html', goodsSpu.description || '', this, 0)
      })
  },
  normalizeGoodsSpu(goodsSpu) {
    // 统一规格和 SKU 结构，避免模板层到处判空。
    goodsSpu.picUrls = util.normalizePicUrlList(goodsSpu.picUrls)
    goodsSpu.skus = Array.isArray(goodsSpu.skus) ? goodsSpu.skus : []
    goodsSpu.spuSpec = Array.isArray(goodsSpu.spuSpec) ? goodsSpu.spuSpec : []
    goodsSpu.spuSpec = goodsSpu.spuSpec.map(spec => {
      let values = Array.isArray(spec.values) ? spec.values : []
      if ((!values || values.length <= 0) && spec.value) {
        values = spec.value.split(/[,\n\uFF0C]/).map(item => ({ name: item.trim() })).filter(item => item.name)
      }
      return Object.assign({}, spec, {
        values: values.map(value => Object.assign({}, value, { name: value.name || value.value || '' })).filter(value => value.name)
      })
    })
    goodsSpu.skus = goodsSpu.skus.map(sku => {
      const specs = Array.isArray(sku.specs) ? sku.specs : []
      return Object.assign({}, sku, {
        specs: specs,
        picUrl: util.resolveResourceUrl(sku.picUrl),
        specInfo: this.buildSpecInfoBySpecs(specs)
      })
    })
    return goodsSpu
  },
  pickDefaultSku(goodsSpu, preferredSkuId) {
    // 默认选中优先级：传入 skuId > 有库存且启用的 SKU > 启用的 SKU > 第一条 SKU。
    const skuList = goodsSpu.skus || []
    if (skuList.length <= 0) {
      return null
    }
    let selectedSku = null
    if (preferredSkuId) {
      selectedSku = skuList.find(item => String(item.id) === String(preferredSkuId))
    }
    if (!selectedSku) {
      selectedSku = skuList.find(item => item.enable === '1' && Number(item.stock || 0) > 0)
    }
    if (!selectedSku) {
      selectedSku = skuList.find(item => item.enable === '1')
    }
    return selectedSku || skuList[0]
  },
  syncSelectedSku(goodsSpu, selectedSku, callback) {
    // 规格切换后统一刷新摘要、禁用态和当前购买数量，避免页面状态不同步。
    const selectedSpecMap = this.buildSelectedSpecMap(selectedSku)
    const skuSpecGroups = this.buildSkuSpecGroups(goodsSpu, selectedSpecMap)
    const selectedSpecSummary = this.buildSelectedSpecSummary(goodsSpu, selectedSpecMap, selectedSku)
    this.setData({
      goodsSpu: goodsSpu,
      selectedSku: selectedSku,
      selectedSkuId: selectedSku ? selectedSku.id : '',
      selectedSpecMap: selectedSpecMap,
      skuSpecGroups: skuSpecGroups,
      selectedSpecSummary: selectedSpecSummary,
      selectedSkuPicUrl: selectedSku && selectedSku.picUrl ? selectedSku.picUrl : util.getFirstPicUrl(goodsSpu.picUrls),
      cartNum: this.normalizeCartNum(selectedSku, this.data.cartNum)
    }, callback)
  },
  buildSelectedSpecMap(selectedSku) {
    // 把当前 SKU 的规格明细转成 map，方便模板判断高亮态。
    const selectedSpecMap = {}
    if (!selectedSku || !Array.isArray(selectedSku.specs)) {
      return selectedSpecMap
    }
    selectedSku.specs.forEach(item => {
      selectedSpecMap[item.specName] = item.specValueName
    })
    return selectedSpecMap
  },
  buildSkuSpecMap(specs) {
    const skuSpecMap = {}
    if (!Array.isArray(specs)) {
      return skuSpecMap
    }
    specs.forEach(item => {
      skuSpecMap[item.specName] = item.specValueName
    })
    return skuSpecMap
  },
  buildSpecInfoBySpecs(specs) {
    // 统一规格展示文案，购物车、下单页和详情页共用。
    if (!Array.isArray(specs)) {
      return ''
    }
    return specs
      .filter(item => item && item.specValueName)
      .map(item => item.specName ? item.specName + ':' + item.specValueName : item.specValueName)
      .join('; ')
  },
  buildSelectedSpecSummary(goodsSpu, selectedSpecMap, selectedSku) {
    // 页面摘要统一走这里，未选满时也能给用户清晰提示。
    if (selectedSku && selectedSku.specInfo) {
      return selectedSku.specInfo
    }
    const specList = goodsSpu && Array.isArray(goodsSpu.spuSpec) ? goodsSpu.spuSpec : []
    if (specList.length <= 0) {
      return '默认规格'
    }
    const selectedNames = specList
      .map(spec => selectedSpecMap[spec.name])
      .filter(Boolean)
    if (selectedNames.length === specList.length) {
      return selectedNames.join(' / ')
    }
    const pendingNames = specList
      .filter(spec => !selectedSpecMap[spec.name])
      .map(spec => spec.name)
    return pendingNames.length > 0 ? '请选择' + pendingNames.join('、') : '请选择规格'
  },
  buildSkuSpecGroups(goodsSpu, selectedSpecMap) {
    // 每次切换规格都重算可选态，效果和淘宝规格层一致。
    const specList = goodsSpu && Array.isArray(goodsSpu.spuSpec) ? goodsSpu.spuSpec : []
    return specList.map(spec => {
      const currentValue = selectedSpecMap[spec.name]
      return Object.assign({}, spec, {
        values: (spec.values || []).map(value => {
          const selected = currentValue === value.name
          const disabled = !selected && !this.hasAvailableSku(goodsSpu, spec.name, value.name, selectedSpecMap)
          return Object.assign({}, value, {
            selected: selected,
            disabled: disabled
          })
        })
      })
    })
  },
  hasAvailableSku(goodsSpu, targetSpecName, targetValueName, selectedSpecMap) {
    // 只要存在启用且有库存的 SKU 命中当前组合，这个规格值就允许点击。
    return (goodsSpu.skus || []).some(sku => {
      if (sku.enable !== '1' || Number(sku.stock || 0) <= 0) {
        return false
      }
      const skuSpecMap = this.buildSkuSpecMap(sku.specs)
      if (skuSpecMap[targetSpecName] !== targetValueName) {
        return false
      }
      return Object.keys(selectedSpecMap).every(specName => {
        if (specName === targetSpecName) {
          return true
        }
        return !selectedSpecMap[specName] || skuSpecMap[specName] === selectedSpecMap[specName]
      })
    })
  },
  findMatchedSku(goodsSpu, selectedSpecMap) {
    // 规格切换后优先命中启用 SKU，没有的话再退到原始记录，方便展示失效状态。
    const exactMatcher = sku => {
      const skuSpecMap = this.buildSkuSpecMap(sku.specs)
      return Object.keys(selectedSpecMap).every(specName => skuSpecMap[specName] === selectedSpecMap[specName])
    }
    const skuList = goodsSpu.skus || []
    return skuList.find(sku => sku.enable === '1' && exactMatcher(sku)) || skuList.find(exactMatcher) || null
  },
  normalizeCartNum(selectedSku, currentCartNum) {
    const nextCartNum = Number(currentCartNum || 1)
    const stock = Number(selectedSku && selectedSku.stock || 0)
    if (stock <= 0) {
      return 1
    }
    return Math.min(Math.max(nextCartNum, 1), stock)
  },
  change(e) {
    this.setData({
      currents: e.detail.current + 1
    })
  },
  showSkuPopup(e) {
    // data-type 用来区分加入购物车、立即购买和购物车改规格三种打开方式。
    const selectedSku = this.data.selectedSku
    if (!selectedSku) {
      wx.showToast({
        title: '未找到可用规格',
        icon: 'none',
        duration: 2000
      })
      return
    }
    this.setData({
      skuPopupVisible: true,
      skuActionType: e && e.currentTarget ? (e.currentTarget.dataset.type || '') : ''
    })
  },
  hideSkuPopup() {
    this.setData({
      skuPopupVisible: false,
      skuActionType: ''
    })
  },
  selectSpecValue(e) {
    // 每次只切换一个规格值，再根据完整规格集合反查目标 SKU。
    if (e.currentTarget.dataset.disabled === '1') {
      return
    }
    const specName = e.currentTarget.dataset.specname
    const valueName = e.currentTarget.dataset.valuename
    if (this.data.selectedSpecMap[specName] === valueName) {
      return
    }
    const nextSpecMap = Object.assign({}, this.data.selectedSpecMap, {
      [specName]: valueName
    })
    const selectedSku = this.findMatchedSku(this.data.goodsSpu, nextSpecMap)
    if (!selectedSku) {
      wx.showToast({
        title: '当前规格组合不存在',
        icon: 'none',
        duration: 2000
      })
      return
    }
    this.syncSelectedSku(this.data.goodsSpu, selectedSku)
  },
  changeCartNum(e) {
    // 规格弹窗里的数量也走统一库存上限，防止超卖。
    this.setData({
      cartNum: this.normalizeCartNum(this.data.selectedSku, e.detail)
    })
  },
  buildSubmitPayload() {
    const goodsSpu = this.data.goodsSpu
    const selectedSku = this.data.selectedSku
    return {
      spuId: goodsSpu.id,
      skuId: selectedSku.id,
      quantity: this.data.cartNum,
      addPrice: selectedSku.salesPrice,
      salesPrice: selectedSku.salesPrice,
      spuName: goodsSpu.name,
      picUrl: selectedSku.picUrl || util.getFirstPicUrl(goodsSpu.picUrls, ''),
      specInfo: selectedSku.specInfo || this.buildSpecInfoBySpecs(selectedSku.specs)
    }
  },
  confirmSkuAction(e) {
    // 提交前统一校验库存，避免分享、回跳、购物车改规格出现脏数据。
    const type = e.currentTarget.dataset.type || this.data.skuActionType
    const selectedSku = this.data.selectedSku
    if (!selectedSku) {
      wx.showToast({
        title: '未找到可用规格',
        icon: 'none',
        duration: 2000
      })
      return
    }
    if (Number(selectedSku.stock || 0) <= 0) {
      wx.showToast({
        title: '抱歉，当前规格库存不足',
        icon: 'none',
        duration: 2000
      })
      return
    }
    const payload = this.buildSubmitPayload()
    if (type === 'cart') {
      app.api.shoppingCartEdit(Object.assign({ id: this.data.shoppingCartId }, payload))
        .then(() => {
          this.shoppingCartCount()
          wx.showToast({
            title: '修改成功',
            duration: 1500
          })
          this.hideSkuPopup()
          setTimeout(() => {
            wx.navigateBack({
              delta: 1,
              fail: () => {}
            })
          }, 400)
        })
      return
    }
    if (type === '1') {
      app.api.shoppingCartAdd(payload)
        .then(() => {
          this.shoppingCartCount()
          wx.showToast({
            title: '添加成功',
            duration: 1500
          })
          this.hideSkuPopup()
        })
      return
    }
    wx.setStorage({
      key: 'param-orderConfirm',
      data: [payload]
    })
    this.hideSkuPopup()
    wx.navigateTo({
      url: '/pages/order/order-confirm/index'
    })
  },
  shoppingCartCount() {
    // 刷新页面和底部 TabBar 的购物车角标。
    app.shoppingCartCount()
      .then(shoppingCartCount => {
        this.setData({
          shoppingCartCount: shoppingCartCount
        })
      })
  },
  operateCartEvent() {
    this.shoppingCartCount()
  },
  shareShow() {
    this.setData({
      shareShow: 'show'
    })
  },
  shareHide() {
    this.setData({
      shareShow: ''
    })
  },
  showModalService() {
    // 服务说明弹层只做展示，不参与业务逻辑。
    this.setData({
      modalService: 'show'
    })
  },
  hideModalService() {
    this.setData({
      modalService: ''
    })
  },
  onPosterSuccess(e) {
    const { detail } = e
    this.setData({
      posterUrl: detail
    })
  },
  onPosterFail(err) {
    console.error(err)
  },
  hidePosterShow() {
    this.setData({
      posterShow: false,
      shareShow: ''
    })
  },
  onCreatePoster() {
  },
  savePoster() {
  },
  handleContact(e) {
    console.log(e)
  },
  noop() {
  }
})

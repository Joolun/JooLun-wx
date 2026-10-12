const app = getApp()

Page({
  data: { tab: 'center', coupons: [], mine: [], loading: false },
  onShow() { app.initPage().then(() => this.load()) },
  onPullDownRefresh() { this.load().finally(() => wx.stopPullDownRefresh()) },
  changeTab(e) { this.setData({ tab: e.currentTarget.dataset.tab }) },
  load() {
    this.setData({ loading: true })
    return Promise.all([app.api.couponCenter(), app.api.couponMine({})]).then(([center, mine]) => {
      this.setData({ coupons: center.data || [], mine: mine.data || [] })
    }).finally(() => this.setData({ loading: false }))
  },
  claim(e) {
    app.api.couponClaim(e.currentTarget.dataset.id).then(() => {
      wx.showToast({ title: '领取成功' })
      this.load()
    })
  },
  statusText(item) {
    return item.status === '2' ? '已使用' : item.status === '1' ? '使用中' : item.available ? '可使用' : '暂不可用'
  }
})

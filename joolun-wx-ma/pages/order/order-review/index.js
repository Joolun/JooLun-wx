const app = getApp()

Page({
  data: { orderItemId: '', spuName: '', specInfo: '', rating: 5, content: '', anonymousFlag: '0', submitting: false },
  onLoad(options) { this.setData({ orderItemId: options.orderItemId, spuName: decodeURIComponent(options.spuName || ''), specInfo: decodeURIComponent(options.specInfo || '') }) },
  chooseRating(e) { this.setData({ rating: Number(e.currentTarget.dataset.value) }) },
  inputContent(e) { this.setData({ content: e.detail.value }) },
  changeAnonymous(e) { this.setData({ anonymousFlag: e.detail.value ? '1' : '0' }) },
  submit() {
    if (this.data.submitting) return
    this.setData({ submitting: true })
    app.api.goodsReviewSubmit({ orderItemId: this.data.orderItemId, rating: this.data.rating, content: this.data.content, anonymousFlag: this.data.anonymousFlag }).then(() => {
      wx.showToast({ title: '评价成功' })
      setTimeout(() => wx.navigateBack(), 600)
    }).catch(() => this.setData({ submitting: false }))
  }
})

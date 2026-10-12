const app = getApp()

Page({
  data: { profile: {}, logs: [], progress: 0 },
  onShow() { app.initPage().then(() => this.load()) },
  onPullDownRefresh() { this.load().finally(() => wx.stopPullDownRefresh()) },
  load() {
    return Promise.all([app.api.memberProfile(), app.api.memberPoints({ current: 1, size: 20 })]).then(([profile, logs]) => {
      const data = profile.data || {}
      const growth = Number(data.user && data.user.growthValue || 0)
      const currentMin = Number(data.currentLevel && data.currentLevel.minGrowth || 0)
      const nextMin = Number(data.nextLevel && data.nextLevel.minGrowth || currentMin)
      const progress = nextMin > currentMin ? Math.min(100, Math.round((growth - currentMin) * 100 / (nextMin - currentMin))) : 100
      this.setData({ profile: data, logs: logs.data && logs.data.records || [], progress })
    })
  }
})

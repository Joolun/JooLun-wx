Component({
  properties: {
    // 这里定义了innerText属性，属性值可以在组件使用时指定
    outTime: {
      type: Number,
      value: 0
    }
  },
  data: {
    day: null,
    hour: null,
    minute: null,
    second: null,
    timer: null
  },
  ready: function () {
    this.CaculateDate()
  },
  detached: function () {
    if (this.data.timer) {
      clearInterval(this.data.timer)
    }
  },
  methods: {
    CaculateDate: function () {
      var that = this
      var timerID = setInterval(function () {
        var leftTime = that.data.outTime - 1000
        var days = parseInt(leftTime / 1000 / 60 / 60 / 24, 10)
        var hours = parseInt(leftTime / 1000 / 60 / 60 % 24, 10)
        var minutes = parseInt(leftTime / 1000 / 60 % 60, 10)
        var seconds = parseInt(leftTime / 1000 % 60, 10)
        if (leftTime > 0) {
          that.setData({ 
            outTime: leftTime 
          })
          that.setData({ 
            day: days > 0 ? that.timeFormat(days) : null, 
            hour: hours > 0 ? that.timeFormat(hours) : null, 
            minute: minutes > 0 ? that.timeFormat(minutes) : null, 
            second: seconds > 0 ? that.timeFormat(seconds) : null
          })
        } else { //结束
          clearInterval(that.data.timer)
          setTimeout(function () {
            that.triggerEvent('countDownDone', null)
          }, 2000)
        }
      }, 1000)
      this.setData({ timer: timerID })
    },
    timeFormat(param) { //小于10的格式化函数
      return param < 10 ? '0' + param : param
    }
  }
})
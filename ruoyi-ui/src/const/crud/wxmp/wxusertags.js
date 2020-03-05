/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
export const tableOption = {
  dialogDrag:true,
  border: true,
  index: false,
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  viewBtn: true,
  searchShow: false,
  menuWidth: 150,
  menuType:'text',
  searchMenuSpan: 6,
  column: [
    {
      label: '名称',
      prop: 'name',
      rules: [{
        required: true,
        message: "请输入名称",
        trigger: "blur"
      }]
    }
  ]
}
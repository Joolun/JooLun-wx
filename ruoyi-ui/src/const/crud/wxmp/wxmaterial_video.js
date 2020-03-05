/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
export const tableOptionVideo = {
  dialogDrag:true,
  border: true,
  index: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  editBtn: false,
  delBtn: false,
  addBtn: false,
  viewBtn: true,
  searchMenuSpan: 6,
  column: [
	  {
      label: 'media_id',
      prop: 'mediaId'
    },
	  {
      label: '名称',
      prop: 'name'
    },
	  {
      label: '更新时间',
      prop: 'updateTime'
    }
  ]
}

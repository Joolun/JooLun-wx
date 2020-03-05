let dicDataRepType = [{
  label: '文本',
  value: 'text'
},{
  label: '图片',
  value: 'image'
},{
  label: '语音',
  value: 'voice'
},{
  label: '视频',
  value: 'video'
},{
  label: '音乐',
  value: 'music'
},{
  label: '图文',
  value: 'news'
}]
export const tableOption1 = {
  dialogDrag:true,
  border: true,
  index: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  menuType:'text',
  addBtn:false,
  delBtn:false,
  editBtn:false,
  searchShow:false,
  searchMenuSpan: 6,
  // defaultSort:{
  //    prop: 'id',
  //    order: 'descending'
  // },
  column: [
    {
      label: '回复消息类型',
      prop: 'repType',
      type: 'select',
      dicData: dicDataRepType
    }
  ]
}
export const tableOption2 = {
  dialogDrag:true,
  border: true,
  index: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  menuType:'text',
  searchShow:false,
  addBtn:false,
  delBtn:false,
  editBtn:false,
  searchMenuSpan: 6,
  // defaultSort:{
  //    prop: 'id',
  //    order: 'descending'
  // },
  column: [
    {
      label: '请求消息类型',
      prop: 'reqType',
      type: 'select',
      dicData: [{
        value: 'text',
        label: '文本'
      },{
        value: 'image',
        label: '图片'
      },{
        value: 'voice',
        label: '语音'
      },{
        value: 'video',
        label: '视频'
      },{
        value: 'shortvideo',
        label: '小视频'
      },{
        value: 'location',
        label: '地理位置'
      },{
        value: 'link',
        label: '链接消息'
      },{
        value: 'event',
        label: '事件推送'
      }],
      sortable:true
    },
    {
      label: '回复消息类型',
      prop: 'repType',
      type: 'select',
      dicData: dicDataRepType,
      sortable:true
    }
  ]
}
export const tableOption3 = {
  dialogDrag:true,
  border: true,
  index: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  menuType:'text',
  searchShow:false,
  addBtn:false,
  delBtn:false,
  editBtn:false,
  searchMenuSpan: 6,
  // defaultSort:{
  //    prop: 'id',
  //    order: 'descending'
  // },
  column: [
	  {
      label: '关键词',
      prop: 'reqKey',
      search:true,
      sortable:true
    },
    {
      label: '匹配类型',
      prop: 'repMate',
      type: 'select',
      dicData: [{
        value: '1',
        label: '全匹配'
      },{
        value: '2',
        label: '半匹配'
      }],
      sortable:true
    },
	  {
      label: '回复消息类型',
      prop: 'repType',
      type: 'select',
        dicData: dicDataRepType,
      sortable:true
    }
  ]
}

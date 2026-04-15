/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

/**
 * 性别字典。
 * 商城用户和小程序资料编辑页保持同一套枚举定义，避免展示口径不一致。
 */
const sexDic = [
  {
    label: "未知",
    value: "0",
  },
  {
    label: "男",
    value: "1",
  },
  {
    label: "女",
    value: "2",
  },
];

/**
 * 状态字典。
 * 0 表示正常会员，1 表示禁用会员。
 */
const statusDic = [
  {
    label: "正常",
    value: "0",
  },
  {
    label: "禁用",
    value: "1",
  },
];

/**
 * 会员身份字典。
 * 当前约定绑定手机号后即成为会员。
 */
const memberFlagDic = [
  {
    label: "未入会",
    value: "0",
  },
  {
    label: "已入会",
    value: "1",
  },
];

/**
 * 注册来源字典。
 * 只保留当前商城项目实际会用到的来源编码。
 */
const registerSourceDic = [
  {
    label: "小程序",
    value: "1",
  },
  {
    label: "公众号",
    value: "2",
  },
  {
    label: "H5",
    value: "3",
  },
  {
    label: "APP",
    value: "4",
  },
  {
    label: "其他",
    value: "9",
  },
];

/**
 * 商城用户后台表格配置。
 * 这里主要服务于会员管理，因此禁用手工新增、删除，只保留查询、查看、编辑。
 */
export const tableOption = {
  dialogType: "drawer",
  dialogWidth: "45%",
  dialogDrag: true,
  border: true,
  stripe: true,
  selection: true,
  menuAlign: "center",
  align: "center",
  menuType: "text",
  searchShow: true,
  addBtn: false,
  delBtn: false,
  viewBtn: true,
  editBtn: true,
  excelBtn: true,
  printBtn: true,
  menuWidth: 160,
  searchMenuSpan: 6,
  defaultSort: {
    prop: "createTime",
    order: "descending",
  },
  column: [
    {
      label: "商城用户ID",
      prop: "id",
      hide: true,
      editDisplay: false,
      addDisplay: false,
    },
    {
      label: "会员总览",
      prop: "profileSummary",
      hide: true,
      display: false,
      addDisplay: false,
      formslot: true,
      span: 24,
    },
    {
      label: "会员编号",
      prop: "userNo",
      minWidth: 150,
      sortable: true,
      search: true,
      editDisabled: true,
    },
    {
      label: "昵称",
      prop: "nickName",
      minWidth: 120,
      sortable: true,
      search: true,
      editDisabled: true,
    },
    {
      label: "头像",
      prop: "avatarUrl",
      type: "upload",
      listType: "picture-img",
      minWidth: 100,
      editDisplay: false,
      viewDisplay: false,
    },
    {
      label: "真实姓名",
      prop: "realName",
      minWidth: 120,
      search: true,
    },
    {
      label: "手机号",
      prop: "mobile",
      minWidth: 140,
      sortable: true,
      search: true,
      editDisabled: true,
    },
    {
      label: "运营标签",
      prop: "userTag",
      minWidth: 140,
      search: true,
      slot: true,
    },
    {
      label: "运营分层",
      prop: "userSegment",
      minWidth: 120,
      slot: true,
      display: false,
      addDisplay: false,
      editDisplay: false,
    },
    {
      label: "会员身份",
      prop: "memberFlag",
      type: "select",
      dicData: memberFlagDic,
      sortable: true,
      search: true,
      slot: true,
      width: 110,
      editDisplay: false,
      addDisplay: false,
    },
    {
      label: "入会时间",
      prop: "memberTime",
      type: "datetime",
      minWidth: 160,
      sortable: true,
      editDisplay: false,
      addDisplay: false,
    },
    {
      label: "会员龄",
      prop: "memberAgeDays",
      minWidth: 100,
      slot: true,
      display: false,
      addDisplay: false,
      editDisplay: false,
    },
    {
      label: "性别",
      prop: "sex",
      type: "select",
      dicData: sexDic,
      sortable: true,
      slot: true,
      width: 90,
    },
    {
      label: "生日",
      prop: "birthday",
      type: "date",
      valueFormat: "yyyy-MM-dd",
      format: "yyyy-MM-dd",
      minWidth: 120,
      sortable: true,
    },
    {
      label: "注册来源",
      prop: "registerSource",
      type: "select",
      dicData: registerSourceDic,
      sortable: true,
      search: true,
      slot: true,
      width: 110,
      editDisabled: true,
    },
    {
      label: "状态",
      prop: "status",
      type: "radio",
      dicData: statusDic,
      sortable: true,
      search: true,
      slot: true,
      width: 110,
      rules: [
        {
          required: true,
          message: "请选择状态",
          trigger: "blur",
        },
      ],
    },
    {
      label: "累计下单",
      prop: "orderCount",
      sortable: true,
      width: 100,
      editDisabled: true,
      addDisplay: false,
    },
    {
      label: "累计支付",
      prop: "consumeCount",
      sortable: true,
      width: 100,
      editDisabled: true,
      addDisplay: false,
    },
    {
      label: "累计退款",
      prop: "refundCount",
      sortable: true,
      width: 100,
      editDisabled: true,
      addDisplay: false,
    },
    {
      label: "累计消费",
      prop: "consumeAmount",
      sortable: true,
      width: 120,
      slot: true,
      editDisabled: true,
      addDisplay: false,
    },
    {
      label: "注册时间",
      prop: "registerTime",
      type: "datetime",
      sortable: true,
      minWidth: 160,
      editDisplay: false,
      addDisplay: false,
    },
    {
      label: "最后登录时间",
      prop: "lastLoginTime",
      type: "datetime",
      sortable: true,
      minWidth: 160,
      editDisplay: false,
      addDisplay: false,
    },
    {
      label: "活跃度",
      prop: "activityLevel",
      width: 110,
      slot: true,
      display: false,
      addDisplay: false,
      editDisplay: false,
    },
    {
      label: "微信用户ID",
      prop: "wxUserId",
      hide: true,
      editDisabled: true,
    },
    {
      label: "OpenId",
      prop: "openId",
      hide: true,
      editDisabled: true,
    },
    {
      label: "UnionId",
      prop: "unionId",
      hide: true,
      editDisabled: true,
    },
    {
      label: "国家",
      prop: "country",
      hide: true,
      editDisabled: true,
    },
    {
      label: "省份",
      prop: "province",
      hide: true,
      editDisabled: true,
    },
    {
      label: "城市",
      prop: "city",
      hide: true,
      editDisabled: true,
    },
    {
      label: "备注",
      prop: "remark",
      type: "textarea",
      minRows: 3,
      span: 24,
      overHidden: true,
    },
    {
      label: "最近订单",
      prop: "recentOrderList",
      hide: true,
      display: false,
      addDisplay: false,
      formslot: true,
      span: 24,
    },
    {
      label: "收货地址",
      prop: "recentAddressList",
      hide: true,
      display: false,
      addDisplay: false,
      formslot: true,
      span: 24,
    },
    {
      label: "创建时间",
      prop: "createTime",
      type: "datetime",
      hide: true,
      editDisplay: false,
      addDisplay: false,
    },
    {
      label: "更新时间",
      prop: "updateTime",
      type: "datetime",
      hide: true,
      editDisplay: false,
      addDisplay: false,
    },
  ],
};

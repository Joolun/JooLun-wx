/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

/**
 * 商城订单后台表格配置。
 * 订单流转由业务动作驱动，因此列表页仅提供查询、查看和发货等动作，不开放手工新增删除。
 */
export const tableOption = {
  dialogType: "drawer",
  dialogWidth: "80%",
  border: true,
  index: false,
  indexLabel: "序号",
  stripe: true,
  menuAlign: "center",
  align: "center",
  menuWidth: 150,
  menuType: "text",
  searchShow: true,
  excelBtn: true,
  printBtn: true,
  editBtn: false,
  delBtn: false,
  addBtn: false,
  viewBtn: false,
  defaultExpandAll: true,
  searchMenuSpan: 6,
  dateBtn: true,
  column: [
    {
      label: "订单ID",
      prop: "id",
      hide: true,
      display: false,
    },
    {
      label: "会员ID",
      prop: "userId",
      sortable: true,
      hide: true,
      display: false,
    },
    {
      label: "会员编号",
      prop: "userNo",
      search: true,
      hide: true,
      display: false,
    },
    {
      label: "会员昵称",
      prop: "userNickName",
      search: true,
      slot: true,
      minWidth: 220,
      display: false,
    },
    {
      label: "真实姓名",
      prop: "userRealName",
      search: true,
      hide: true,
      display: false,
    },
    {
      label: "手机号",
      prop: "userMobile",
      search: true,
      hide: true,
      display: false,
    },
    {
      label: "是否支付",
      prop: "isPay",
      type: "radio",
      hide: true,
      search: true,
      display: false,
      dicData: [
        {
          label: "已支付",
          value: "1",
        },
        {
          label: "未支付",
          value: "0",
        },
      ],
    },
    {
      label: "订单编号",
      prop: "orderNo",
      search: true,
      hide: true,
      display: false,
    },
    {
      label: "售后状态",
      prop: "afterSaleStatus",
      type: "radio",
      search: true,
      hide: true,
      display: false,
      dicData: [
        {
          label: "退款申请中",
          value: "1",
        },
        {
          label: "拒绝退款",
          value: "2",
        },
        {
          label: "退款待回调",
          value: "3",
        },
        {
          label: "已退款",
          value: "4",
        },
      ],
    },
    {
      label: "商品信息",
      prop: "name",
      width: 500,
      slot: true,
      display: false,
    },
    {
      label: "时间/订单编号",
      prop: "createTime",
      sortable: true,
      width: 250,
      slot: true,
      display: false,
    },
    {
      label: "状态",
      prop: "status",
      type: "radio",
      sortable: true,
      slot: true,
      width: 170,
      display: false,
    },
    {
      label: "订单金额(￥)",
      prop: "salesPrice",
      sortable: true,
      slot: true,
      width: 180,
      display: false,
    },
    {
      label: "付款时间",
      prop: "paymentTime",
      sortable: "custom",
      display: false,
      width: 160,
    },
    {
      label: "发货时间",
      prop: "deliveryTime",
      sortable: true,
      hide: true,
      display: false,
    },
    {
      label: "收货时间",
      prop: "receiverTime",
      sortable: true,
      hide: true,
      display: false,
    },
    {
      label: "成交时间",
      prop: "closingTime",
      sortable: true,
      hide: true,
      display: false,
    },
    {
      label: "最后更新时间",
      prop: "updateTime",
      sortable: true,
      hide: true,
      display: false,
    },
    {
      label: "备注",
      prop: "remark",
      hide: true,
      display: false,
    },
  ],
  group: [
    {
      icon: "el-icon-s-order",
      label: "基本信息",
      prop: "group1",
      column: [
        {
          prop: "orderNo",
          formslot: true,
          span: 24,
        },
      ],
    },
    {
      icon: "el-icon-user",
      label: "用户详情",
      prop: "group2",
      column: [
        {
          prop: "userId",
          formslot: true,
          span: 24,
        },
      ],
    },
    {
      icon: "el-icon-goods",
      label: "商品信息",
      prop: "group3",
      column: [
        {
          prop: "listOrderItem",
          formslot: true,
          span: 24,
        },
      ],
    },
    {
      icon: "el-icon-location-outline",
      label: "收货信息",
      prop: "group4",
      column: [
        {
          prop: "orderLogistics",
          formslot: true,
          span: 24,
        },
      ],
    },
    {
      icon: "el-icon-refresh-left",
      label: "售后轨迹",
      prop: "group5",
      column: [
        {
          prop: "afterSaleInfo",
          formslot: true,
          span: 24,
        },
      ],
    },
    {
      icon: "el-icon-time",
      label: "订单轨迹",
      prop: "group6",
      column: [
        {
          prop: "orderTimeline",
          formslot: true,
          span: 24,
        },
      ],
    },
  ],
};

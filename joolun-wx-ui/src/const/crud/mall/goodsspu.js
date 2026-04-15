// 商品管理页 Avue 配置。
// 多规格改造后，规格编辑区不再拆成普通字段，而是通过 skuEditor 自定义表单插槽统一维护。
export const tableOption = {
  dialogType: "drawer",
  border: true,
  stripe: true,
  menuAlign: "center",
  align: "center",
  menuType: "text",
  searchShow: false,
  excelBtn: true,
  printBtn: true,
  selection: true,
  dialogWidth: "88%",
  searchMenuSpan: 6,
  // 列表区展示的是商品级摘要字段。
  // 其中价格、库存由后端按启用 SKU 聚合后返回，便于列表直接展示。
  column: [
    {
      label: "商品名称",
      prop: "name",
      search: true,
      display: false,
    },
    {
      label: "商品图片",
      prop: "picUrls",
      width: 125,
      slot: true,
      display: false,
    },
    {
      label: "类目",
      prop: "categoryId",
      type: "cascader",
      search: true,
      props: {
        label: "name",
        value: "id",
      },
      dicUrl: "/goodscategory/tree",
      checkStrictly: true,
      display: false,
    },
    {
      label: "卖点",
      prop: "sellPoint",
      display: false,
      overHidden: true,
      width: 120,
    },
    {
      label: "销售价",
      prop: "salesPrice",
      slot: true,
      display: false,
    },
    {
      label: "市场价",
      prop: "marketPrice",
      display: false,
    },
    {
      label: "成本价",
      prop: "costPrice",
      display: false,
    },
    {
      label: "库存",
      prop: "stock",
      display: false,
    },
    {
      label: "商品编码",
      prop: "spuCode",
      width: 110,
      search: true,
      sortable: true,
      display: false,
    },
    {
      label: "虚拟销量",
      prop: "saleNum",
      display: false,
    },
    {
      label: "创建时间",
      prop: "createTime",
      sortable: "custom",
      width: 110,
      display: false,
    },
    {
      label: "是否上架",
      prop: "shelf",
      type: "radio",
      search: true,
      width: 110,
      sortable: "custom",
      slot: true,
      display: false,
      dicData: [
        { label: "下架", value: "0" },
        { label: "上架", value: "1" },
      ],
    },
    {
      label: "更新时间",
      prop: "updateTime",
      width: 110,
      display: false,
    },
  ],
  // 表单区按“基础信息 + 辅助信息”分组。
  // 规格与 SKU 编辑器放在基础信息里，保证新增、编辑、查看都走同一套结构。
  group: [
    {
      icon: "el-icon-goods",
      label: "基本信息",
      prop: "group1",
      column: [
        {
          label: "商品名称",
          prop: "name",
          span: 24,
          rules: [
            {
              required: true,
              message: "商品名称不能为空",
              trigger: "blur",
            },
            {
              max: 100,
              message: "长度不能超过100个字符",
            },
          ],
        },
        {
          label: "商品图片",
          prop: "picUrls",
          listType: "picture-card",
          dataType: "array",
          type: "upload",
          width: 250,
          rules: [
            {
              required: true,
              message: "图片不能为空",
              trigger: "change",
            },
          ],
          canvasOption: {
            text: "joolun",
            ratio: 0.1,
          },
          oss: "ali",
          loadText: "附件上传中，请稍等",
          span: 24,
          tip: "只能上传jpg/png文件，且不超过50kb",
        },
        {
          label: "类目",
          prop: "categoryId",
          type: "cascader",
          props: {
            label: "name",
            value: "id",
          },
          dicUrl: "/goodscategory/tree",
          checkStrictly: true,
          rules: [
            {
              required: true,
              message: "请选择类目",
              trigger: "blur",
            },
          ],
        },
        {
          label: "是否上架",
          prop: "shelf",
          type: "radio",
          rules: [
            {
              required: true,
              message: "请选择是否上架",
              trigger: "blur",
            },
          ],
          dicData: [
            { label: "下架", value: "0" },
            { label: "上架", value: "1" },
          ],
        },
        {
          label: "商品编码",
          prop: "spuCode",
          rules: [
            {
              max: 32,
              message: "长度不能超过32个字符",
            },
          ],
        },
        {
          label: "虚拟销量",
          prop: "saleNum",
          type: "number",
          precision: 0,
          min: 0,
          tip: "系统会按销售情况自动累加",
        },
        {
          label: "卖点",
          prop: "sellPoint",
          span: 24,
          rules: [
            {
              max: 500,
              message: "长度不能超过500个字符",
            },
          ],
        },
        {
          label: "规格与SKU",
          prop: "skuEditor",
          // 通过 formslot 渲染完整 SKU 编辑器，避免 Avue 普通字段无法承载多规格联动。
          formslot: true,
          span: 24,
        },
      ],
    },
    {
      icon: "el-icon-grape",
      label: "辅助信息",
      prop: "group5",
      column: [
        {
          label: "描述",
          prop: "description",
          formslot: true,
          span: 24,
        },
      ],
    },
  ],
};

// 只读弹窗配置，沿用商品主表字段展示，便于批量选择商品等场景复用。
export const tableOption2 = {
  dialogType: "drawer",
  border: true,
  stripe: true,
  menuAlign: "center",
  align: "center",
  searchShow: false,
  dialogWidth: "88%",
  editBtn: false,
  delBtn: false,
  addBtn: false,
  selection: true,
  menu: false,
  maxHeight: 450,
  column: [
    {
      label: "商品名称",
      prop: "name",
      search: true,
    },
    {
      label: "商品图片",
      prop: "picUrls",
      width: 120,
      slot: true,
    },
    {
      label: "类目",
      prop: "categoryId",
      type: "cascader",
      search: true,
      props: {
        label: "name",
        value: "id",
      },
      dicUrl: "/goodscategory/tree",
    },
    {
      label: "卖点",
      prop: "sellPoint",
      sortable: true,
    },
    {
      label: "销售价",
      prop: "salesPrice",
      display: false,
    },
    {
      label: "市场价",
      prop: "marketPrice",
      display: false,
    },
    {
      label: "成本价",
      prop: "costPrice",
      display: false,
    },
    {
      label: "库存",
      prop: "stock",
      display: false,
    },
    {
      label: "商品编码",
      prop: "spuCode",
      search: true,
      sortable: true,
    },
    {
      label: "虚拟销量",
      prop: "saleNum",
      sortable: true,
    },
    {
      label: "创建时间",
      prop: "createTime",
      sortable: true,
    },
    {
      label: "更新时间",
      prop: "updateTime",
      sortable: true,
    },
    {
      label: "是否上架",
      prop: "shelf",
      type: "radio",
      search: true,
      sortable: true,
      slot: true,
    },
  ],
};

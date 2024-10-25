/*
MIT License
Copyright (c) 2024 www.joolun.com
*/
export const tableOption = {
  dialogDrag: true,
  border: true,
  index: false,
  stripe: true,
  menuAlign: "center",
  align: "center",
  viewBtn: true,
  searchShow: false,
  menuWidth: 350,
  menuType: "text",
  searchMenuSpan: 6,
  column: [
    {
      label: "名称",
      prop: "name",
      rules: [
        {
          required: true,
          message: "请输入名称",
          trigger: "blur",
        },
      ],
    },
  ],
};

<!--
  - Copyright (C) 2024
  - All rights reserved, Designed By www.joolun.com
-->
<template>
  <div class="app-container">
    <el-tabs v-model="status" type="border-card" @tab-click="handleClickStatus">
      <el-tab-pane name="-1">
        <template #label>
          <span>全部订单</span>
        </template>
      </el-tab-pane>
      <el-tab-pane name="0">
        <template #label>
          <span>待付款</span>
        </template>
      </el-tab-pane>
      <el-tab-pane name="1">
        <template #label>
          <span>待发货</span>
        </template>
      </el-tab-pane>
      <el-tab-pane name="2">
        <template #label>
          <span>待收货</span>
        </template>
      </el-tab-pane>
      <el-tab-pane name="3">
        <template #label>
          <span>已完成</span>
        </template>
      </el-tab-pane>
      <el-tab-pane name="5">
        <template #label>
          <span>已取消</span>
        </template>
      </el-tab-pane>
      <avue-crud
        ref="crud"
        v-model="form"
        :page="page"
        :data="tableData"
        :permission="permissionList"
        :table-loading="tableLoading"
        :option="tableOption"
        @on-load="getPageF"
        @refresh-change="refreshChange"
        @row-update="handleUpdate"
        @row-save="handleSave"
        @row-del="handleDel"
        @sort-change="sortChange"
        @search-change="searchChange"
        @date-change="dateChange"
      >
        <template #status="scope">
          <div style="text-align: left">
            <div class="grid-content">
              订单状态：
              <el-tag
                :type="
                  scope.row.status == '2' ||
                  scope.row.status == '3' ||
                  scope.row.status == '4'
                    ? 'success'
                    : 'danger'
                "
                effect="dark"
                size="small"
              >
                {{ scope.row.statusDesc }}
              </el-tag>
            </div>
            <div class="grid-content">
              支付状态：
              <el-tag
                :type="scope.row.isPay == '1' ? 'success' : 'danger'"
                effect="dark"
                size="small"
                >{{ scope.row.isPay == "1" ? "已支付" : "未支付" }}
              </el-tag>
            </div>
          </div>
        </template>
        <template #orderNo-form>
          <el-table :data="[form]" border style="width: 100%">
            <el-table-column align="center" prop="orderNo" label="订单单号">
            </el-table-column>
            <el-table-column
              align="left"
              prop="salesPrice"
              label="订单金额"
              width="200"
            >
              <template v-slot="scope">
                <div>订单金额：￥{{ scope.row.salesPrice }}</div>
                <div>运费金额：+ ￥{{ scope.row.freightPrice }}</div>
                <div style="color: red">
                  支付金额：￥{{ scope.row.paymentPrice }}
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="paymentType" label="订单状态">
              <template v-slot="scope">
                <div style="text-align: left">
                  <div class="grid-content">
                    订单状态：
                    <el-tag
                      :type="
                        scope.row.status == '2' ||
                        scope.row.status == '3' ||
                        scope.row.status == '4'
                          ? 'success'
                          : 'danger'
                      "
                      effect="dark"
                      size="small"
                    >
                      {{ scope.row.statusDesc }}
                    </el-tag>
                  </div>
                  <div class="grid-content">
                    支付状态：
                    <el-tag
                      :type="scope.row.isPay == '1' ? 'success' : 'danger'"
                      effect="dark"
                      size="small"
                      >{{ scope.row.isPay == "1" ? "已支付" : "未支付" }}
                    </el-tag>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="orderTime"
              label="订单时间"
              width="250"
            >
              <template v-slot="scope">
                <div>创建时间：{{ scope.row.createTime }}</div>
                <div v-if="scope.row.paymentTime">
                  付款时间：{{ scope.row.paymentTime }}
                </div>
                <div v-if="scope.row.deliveryTime">
                  发货时间：{{ scope.row.deliveryTime }}
                </div>
                <div v-if="scope.row.receiverTime">
                  收货时间：{{ scope.row.receiverTime }}
                </div>
                <div v-if="scope.row.closingTime">
                  成交时间：{{ scope.row.closingTime }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="transactionId"
              label="支付流水号"
            >
            </el-table-column>
            <el-table-column align="center" prop="userMessage" label="买家留言">
            </el-table-column>
          </el-table>
        </template>
        <template #listOrderItem-form>
          <el-table
            :data="form.listOrderItem"
            border
            style="width: 100%; margin-top: -10px"
          >
            <el-table-column
              align="center"
              prop="picUrl"
              label="图片"
              width="120"
            >
              <template v-slot="scope">
                <img :src="scope.row.picUrl" width="100" height="100" />
              </template>
            </el-table-column>
            <el-table-column align="center" prop="spuName" label="商品名">
            </el-table-column>
            <el-table-column
              align="center"
              prop="salesPrice"
              label="商品单价"
              width="100"
            >
              <template v-slot="scope"> ￥{{ scope.row.salesPrice }} </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="quantity"
              label="数量"
              width="50"
            >
            </el-table-column>
            <el-table-column
              align="center"
              prop="salesPriceTotal"
              label="商品总价"
              width="100"
            >
              <template v-slot="scope">
                ￥{{ (scope.row.salesPrice * scope.row.quantity).toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="freightPrice"
              label="运费金额"
              width="100"
            >
              <template v-slot="scope">
                ￥{{ scope.row.freightPrice }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="salesPriceTotal"
              label="支付金额"
              width="80"
            >
              <template v-slot="scope">
                ￥{{ scope.row.paymentPrice }}
              </template>
            </el-table-column>
          </el-table>
        </template>
        <template #userId-form>
          <el-table :data="[form.userInfo]" border style="width: 100%">
            <el-table-column align="center" prop="nickName" label="用户名">
              <template v-slot="scope">
                <el-avatar
                  icon="el-icon-user-solid"
                  :src="scope.row.headimgUrl"
                ></el-avatar>
                <div>{{ scope.row.nickName }}</div>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="sex" label="性别">
              <template v-slot="scope">
                <div>
                  {{
                    scope.row.sex == "1"
                      ? "男"
                      : scope.row.sex == "2"
                      ? "女"
                      : "未知"
                  }}
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="id" label="用户编号">
            </el-table-column>
          </el-table>
        </template>
        <template #orderLogistics-form>
          <div>
            <el-table :data="[form.orderLogistics]" border style="width: 100%">
              <el-table-column align="center" prop="userName" label="姓名">
              </el-table-column>
              <el-table-column align="center" prop="telNum" label="电话">
              </el-table-column>
              <el-table-column align="center" prop="address" label="地址">
              </el-table-column>
              <el-table-column
                align="center"
                prop="logisticsNo"
                label="快递单号"
              >
              </el-table-column>
              <el-table-column
                align="center"
                prop="logisticsDesc"
                label="快递公司"
              >
              </el-table-column>
              <el-table-column
                align="center"
                prop="statusDesc"
                label="快递状态"
              >
              </el-table-column>
            </el-table>
          </div>
        </template>
        <template #name="scope">
          <el-row
            :gutter="10"
            v-for="(item, index) in scope.row.listOrderItem"
            :key="index"
            style="
              border: 1px solid #eaeaea;
              padding: 5px 0;
              display: flex;
              align-items: center;
            "
          >
            <el-col :span="4" style="display: flex; align-items: center">
              <el-image :src="item.picUrl" style="width: 60px; height: 60px" />
            </el-col>
            <el-col :span="12" style="text-align: left">
              <div class="spu-name">{{ item.spuName }}</div>
              <div v-if="item.specInfo" class="spec-info">
                {{ item.specInfo }}
              </div>
            </el-col>
            <el-col :span="8">
              <div class="grid-content" style="color: red">
                ￥{{ item.paymentPrice }}
              </div>
              <div class="grid-content">×{{ item.quantity }}件</div>
              <div class="grid-content" v-if="item.status == '2'">
                <el-tag type="danger">拒绝退款</el-tag>
              </div>
              <div class="grid-content" v-if="item.status == '3'">同意退款</div>
              <div class="grid-content" v-if="item.isRefund == '1'">已退款</div>
              <el-button
                v-if="item.status == '1' && scope.row.isPay == '1'"
                class="grid-content"
                type="danger"
                @click="doOrderRefundsF(item)"
                plain
                size="small"
              >
                申请退款
              </el-button>
            </el-col>
          </el-row>
        </template>
        <template #createTime="scope">
          <div>
            <div class="grid-content">{{ scope.row.createTime }}</div>
            <div class="grid-content">{{ scope.row.orderNo }}</div>
          </div>
        </template>
        <template #salesPrice="scope">
          <div style="text-align: left">
            <div class="grid-content">
              订单金额：￥{{ scope.row.salesPrice }}
            </div>
            <div class="grid-content">
              运费金额：+ ￥{{ scope.row.freightPrice }}
            </div>
            <div class="grid-content" style="color: red">
              支付金额：￥{{ scope.row.paymentPrice }}
            </div>
          </div>
        </template>
        <template #menu="scope">
          <el-button
            v-if="checkPermi(['mall:orderinfo:get'])"
            icon="el-icon-edit"
            type="primary"
            link
            @click="openView(scope.row, scope.index)"
            >订单详情
          </el-button>
          <el-button
            v-if="
              checkPermi(['mall:orderinfo:edit']) && scope.row.status == '1'
            "
            icon="el-icon-position"
            type="primary"
            link
            @click="showDialogLogistics(scope.row, scope.index)"
            >发货
          </el-button>
          <el-button
            v-if="
              checkPermi(['mall:orderinfo:edit']) &&
              scope.row.isPay == '0' &&
              !scope.row.status
            "
            icon="el-icon-delete"
            type="danger"
            link
            @click="orderCancelF(scope.row, scope.index)"
            >取消
          </el-button>
        </template>
      </avue-crud>
    </el-tabs>
    <el-dialog title="发货" v-model="dialogLogistics" width="30%">
      <avue-form
        :option="logisticsOption"
        v-model="logisticsForm"
        @submit="delivery"
      ></avue-form>
    </el-dialog>
  </div>
</template>

<script setup name="OrderInfo">
import { checkPermi, checkRole } from "@/utils/permission";
import {
  getPage,
  getObj,
  addObj,
  putObj,
  delObj,
  editPrice,
  orderCancel,
  takeGoods,
  doOrderRefunds,
} from "@/api/mall/orderinfo";
import { tableOption } from "@/const/crud/mall/orderinfo";
const crud = ref(null);
const { proxy } = getCurrentInstance();

const data = reactive({
  form: {},
  date: [],
  status: "-1",
  tableData: [],
  page: {
    total: 0, // 总页数
    currentPage: 1, // 当前页数
    pageSize: 20, // 每页显示多少条
    ascs: [], //升序字段
    descs: "create_time", //降序字段
  },
  paramsSearch: {},
  tableLoading: false,
  dialogLogistics: false,
  logisticsForm: {
    row: {},
    logistics: null,
    logisticsNo: null,
    address: null,
  },
  logisticsOption: {
    emptyBtn: false,
    card: true,
    group: [
      {
        icon: "el-icon-user",
        label: "收货人信息",
        prop: "group1",
        column: [
          {
            label: "收货人名字",
            prop: "userName",
            span: 24,
            readonly: true,
          },
          {
            label: "电话号码",
            prop: "telNum",
            span: 24,
            readonly: true,
          },
          {
            label: "收货地址",
            prop: "address",
            type: "textarea",
            span: 24,
            readonly: true,
          },
        ],
      },
      {
        icon: "el-icon-truck",
        label: "快递信息",
        prop: "group2",
        column: [
          {
            label: "快递公司",
            prop: "logistics",
            dicUrl: "/orderlogistics/dict/LOGISTICS",
            span: 24,
            type: "select",
            rules: [
              {
                required: true,
                message: "请选择快递公司",
                trigger: "blur",
              },
            ],
          },
          {
            label: "快递单号",
            prop: "logisticsNo",
            span: 24,
            rules: [
              {
                required: true,
                message: "请输入快递单号",
                trigger: "blur",
              },
            ],
          },
        ],
      },
    ],
  },
  dialogRefunds: false,
  orderItemObj: {},
});

const {
  form,
  page,
  tableData,
  tableLoading,
  status,
  dialogLogistics,
  logisticsForm,
  logisticsOption,
  paramsSearch,
} = toRefs(data);
const permissionList = computed(() => {
  return {
    addBtn: checkPermi(["mall:orderinfo:add"]),
    delBtn: checkPermi(["mall:orderinfo:del"]),
    editBtn: checkPermi(["mall:orderinfo:edit"]),
    viewBtn: checkPermi(["mall:orderinfo:get"]),
  };
});

function doOrderRefundsF(obj) {
  proxy
    .$confirm("是否同意退款申请", "提示", {
      distinguishCancelAndClose: true,
      confirmButtonText: "同意",
      cancelButtonText: "拒绝",
      type: "warning",
    })
    .then(function () {
      return doOrderRefunds({
        id: obj.id,
        status: "3",
      })
        .then(() => {
          proxy.$message({
            showClose: true,
            message: "审核成功",
            type: "success",
          });
          getPageF(data.page);
        })
        .catch(function (err) {});
    })
    .catch((action) => {
      if (action === "cancel") {
        return doOrderRefunds({
          id: obj.id,
          status: "2",
        })
          .then(() => {
            proxy.$message({
              showClose: true,
              message: "审核成功",
              type: "success",
            });
            getPageF(data.page);
          })
          .catch(function (err) {});
      }
    });
}

function handleEditPrice(obj) {
  proxy
    .$prompt("请输入价格", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      inputType: "number",
      inputPattern: /\S/,
      inputErrorMessage: "请输入价格",
    })
    .then(({ value }) => {
      editPrice({
        id: obj.id,
        paymentPrice: value,
      })
        .then(() => {
          getPageF(data.page);
        })
        .catch(() => {});
    })
    .catch(() => {});
}

function handleClickStatus(pane) {
  data.status = pane.paneName;
  data.page.currentPage = 1;
  getPageF(data.page);
}

function openView(row, index) {
  data.tableLoading = true;
  getObj(row.id).then((response) => {
    row.userInfo = response.data.userInfo;
    row.orderLogistics = response.data.orderLogistics;
    crud.value.rowView(row, index);
    data.tableLoading = false;
  });
}
// 取消订单
function orderCancelF(row, index) {
  proxy
    .$confirm("是否确认取消此订单", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(function () {
      return orderCancel(row.id)
        .then(() => {
          proxy.$message({
            showClose: true,
            message: "取消成功",
            type: "success",
          });
          getPageF(data.page);
        })
        .catch(function (err) {});
    });
}

function showDialogLogistics(row, index, done) {
  data.logisticsForm.row = row;
  data.logisticsForm.address = row.orderLogistics.address;
  data.logisticsForm.userName = row.orderLogistics.userName;
  data.logisticsForm.telNum = row.orderLogistics.telNum;
  data.dialogLogistics = true;
}

function delivery(form, done) {
  let row = data.logisticsForm.row;
  (row.status = "2"),
    (row.logistics = form.logistics),
    (row.logisticsNo = form.logisticsNo);
  putObj(row)
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "发货成功",
        type: "success",
      });
      done();
      getPageF(data.page);
      data.dialogLogistics = false;
    })
    .catch(() => {
      done();
    });
}

function dateChange(val) {
  if (val.value) {
    data.date = val.value;
  } else {
    data.date = [];
  }
  getPageF(data.page);
}

function searchChange(params, done) {
  params = proxy.filterForm(params);
  data.paramsSearch = params;
  data.page.currentPage = 1;
  getPageF(data.page, params);
  done();
}

function sortChange(val) {
  let prop = val.prop ? val.prop.replace(/([A-Z])/g, "_$1").toLowerCase() : "";
  if (val.order == "ascending") {
    data.page.descs = [];
    data.page.ascs = prop;
  } else if (val.order == "descending") {
    data.page.ascs = [];
    data.page.descs = prop;
  } else {
    data.page.ascs = [];
    data.page.descs = [];
  }
  getPageF(data.page);
}

function getPageF(page, params) {
  data.tableLoading = true;
  getPage(
    Object.assign(
      {
        current: page.currentPage,
        size: page.pageSize,
        descs: data.page.descs,
        ascs: data.page.ascs,
        status: data.status != "-1" ? data.status : null,
        beginTime: data.date[0],
        endTime: data.date[1],
      },
      params,
      data.paramsSearch
    )
  )
    .then((response) => {
      data.tableData = response.data.records;
      data.page.total = response.data.total;
      data.page.currentPage = page.currentPage;
      data.page.pageSize = page.pageSize;
      data.tableLoading = false;
    })
    .catch(() => {
      data.tableLoading = false;
    });
}

function handleDel(row, index) {
  proxy
    .$confirm("是否确认删除此数据", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
    .then(function () {
      return delObj(row.id);
    })
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "删除成功",
        type: "success",
      });
      getPageF(data.page);
    })
    .catch(function (err) {});
}

function handleUpdate(row, index, done, loading) {
  putObj(row)
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "修改成功",
        type: "success",
      });
      done();
      getPageF(data.page);
    })
    .catch(() => {
      loading();
    });
}

function handleSave(row, done, loading) {
  addObj(row)
    .then(() => {
      proxy.$message({
        showClose: true,
        message: "添加成功",
        type: "success",
      });
      done();
      getPageF(data.page);
    })
    .catch(() => {
      loading();
    });
}

function refreshChange(page) {
  getPageF(data.page);
}
</script>

<style lang="scss" scoped>
.spu-name {
  font-size: 13px;
}
.spec-info {
  margin-top: 10px;
  font-size: 12px;
  color: #7b7b7b;
}
// 去掉 ruoyi 的样式
.avue-crud :deep(.el-card__body) {
  padding: 0 !important;
}
</style>

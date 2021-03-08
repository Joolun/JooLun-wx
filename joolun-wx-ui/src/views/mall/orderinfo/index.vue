<!--
  - Copyright (C) 2018-2019
  - All rights reserved, Designed By www.joolun.com
  - 注意：
  - 本软件为www.joolun.com开发研制，项目使用请保留此说明
-->
<template>
  <div class="app-container">
      <el-tabs v-model="status" type="card" @tab-click="handleClickStatus">
          <el-tab-pane name="-1">
              <span slot="label"><i class="el-icon-s-order"></i> 全部订单</span>
          </el-tab-pane>
          <el-tab-pane name="0">
              <span slot="label"><i class="el-icon-bank-card"></i> 待付款</span>
          </el-tab-pane>
          <el-tab-pane name="1">
              <span slot="label"><i class="el-icon-refrigerator"></i> 待发货</span>
          </el-tab-pane>
          <el-tab-pane name="2">
              <span slot="label"><i class="el-icon-truck"></i> 待收货</span>
          </el-tab-pane>
          <el-tab-pane name="3">
              <span slot="label"><i class="el-icon-document"></i> 已完成</span>
          </el-tab-pane>
          <el-tab-pane name="5">
              <span slot="label"><i class="el-icon-circle-close"></i> 已取消</span>
          </el-tab-pane>
      </el-tabs>
      <avue-crud ref="crud"
                 :page="page"
                 :data="tableData"
                 :permission="permissionList"
                 :table-loading="tableLoading"
                 :option="tableOption"
                 @on-load="getPage"
                 @refresh-change="refreshChange"
                 @row-update="handleUpdate"
                 @row-save="handleSave"
                 @row-del="handleDel"
                 @sort-change="sortChange"
                 @search-change="searchChange"
                 @date-change="dateChange">
          <template slot-scope="scope" slot="status">
              <div style="text-align: left">
                  <div class="grid-content">订单状态：
                      <el-tag :type="scope.row.status=='2' ||scope.row.status=='3' || scope.row.status=='4' ? 'success' : 'danger'"
                              effect="dark" size="mini"> {{ scope.row.statusDesc}}
                      </el-tag>
                  </div>
                  <div class="grid-content">支付状态：
                      <el-tag :type="scope.row.isPay=='1' ? 'success' : 'danger'"
                              effect="dark" size="mini">{{scope.row.isPay == '1' ? '已支付' : '未支付'}}
                      </el-tag>
                  </div>
              </div>
          </template>
          <template slot-scope="scope" slot="orderNoForm">
              <el-table
                      :data="[scope.row]"
                      border
                      style="width: 100%; margin-top: 20px; margin-top: -10px">
                  <el-table-column
                          align="center"
                          prop="orderNo"
                          label="订单单号">
                  </el-table-column>
                  <el-table-column
                          align="left"
                          prop="salesPrice"
                          label="订单金额"
                          width="200">
                      <template slot-scope="scope">
                          <div>订单金额：￥{{scope.row.salesPrice}}</div>
                          <div>运费金额：+ ￥{{scope.row.freightPrice}}</div>
                          <div style="color: red">支付金额：￥{{scope.row.paymentPrice}}</div>
                      </template>
                  </el-table-column>
                  <el-table-column
                          align="center"
                          prop="paymentType"
                          label="订单状态">
                      <template slot-scope="scope">
                          <div style="text-align: left">
                              <div class="grid-content">订单状态：
                                  <el-tag :type="scope.row.status=='2' ||scope.row.status=='3' || scope.row.status=='4' ? 'success' : 'danger'"
                                          effect="dark" size="mini"> {{ scope.row.statusDesc}}
                                  </el-tag>
                              </div>
                              <div class="grid-content">支付状态：
                                  <el-tag :type="scope.row.isPay=='1' ? 'success' : 'danger'"
                                          effect="dark" size="mini">{{scope.row.isPay == '1' ? '已支付' : '未支付'}}
                                  </el-tag>
                              </div>
                          </div>
                      </template>
                  </el-table-column>
                  <el-table-column
                          align="center"
                          prop="orderTime"
                          label="订单时间"
                          width="250">
                      <template slot-scope="scope">
                          <div>创建时间：{{scope.row.createTime}}</div>
                          <div v-if="scope.row.paymentTime">付款时间：{{scope.row.paymentTime}}</div>
                          <div v-if="scope.row.deliveryTime">发货时间：{{scope.row.deliveryTime}}</div>
                          <div v-if="scope.row.receiverTime">收货时间：{{scope.row.receiverTime}}</div>
                          <div v-if="scope.row.closingTime">成交时间：{{scope.row.closingTime}}</div>
                      </template>
                  </el-table-column>
                  <el-table-column
                          align="center"
                          prop="transactionId"
                          label="支付流水号">
                  </el-table-column>
                  <el-table-column
                          align="center"
                          prop="userMessage"
                          label="买家留言">
                  </el-table-column>
              </el-table>
          </template>
          <template slot-scope="scope" slot="listOrderItemForm">
              <el-table
                      :data="scope.row.listOrderItem"
                      border
                      style="width: 100%; margin-top: 20px; margin-top: -10px">
                  <el-table-column
                          align="center"
                          prop="picUrl"
                          label="图片"
                          width="120">
                      <template slot-scope="scope">
                          <img :src="scope.row.picUrl" width="100" height="100"/>
                      </template>
                  </el-table-column>
                  <el-table-column
                          align="center"
                          prop="spuName"
                          label="商品名">
                  </el-table-column>
                  <el-table-column
                          align="center"
                          prop="salesPrice"
                          label="商品单价"
                          width="100">
                      <template slot-scope="scope">
                          ￥{{scope.row.salesPrice}}
                      </template>
                  </el-table-column>
                  <el-table-column
                          align="center"
                          prop="quantity"
                          label="数量"
                          width="50">
                  </el-table-column>
                  <el-table-column
                          align="center"
                          prop="salesPriceTotal"
                          label="商品总价"
                          width="100">
                      <template slot-scope="scope">
                          ￥{{(scope.row.salesPrice * scope.row.quantity).toFixed(2)}}
                      </template>
                  </el-table-column>
                  <el-table-column
                          align="center"
                          prop="freightPrice"
                          label="运费金额"
                          width="100">
                      <template slot-scope="scope">
                          ￥{{scope.row.freightPrice}}
                      </template>
                  </el-table-column>
                  <el-table-column
                          align="center"
                          prop="salesPriceTotal"
                          label="支付金额"
                          width="80">
                      <template slot-scope="scope">
                          ￥{{scope.row.paymentPrice}}
                      </template>
                  </el-table-column>
              </el-table>
          </template>
          <template slot-scope="scope" slot="userIdForm">
              <el-table
                      :data="[scope.row.userInfo]"
                      border
                      style="width: 100%">
                  <el-table-column
                          align="center"
                          prop="nickName"
                          label="用户名">
                      <template slot-scope="scope">
                          <el-avatar icon="el-icon-user-solid" :src="scope.row.headimgUrl"></el-avatar>
                          <div>{{scope.row.nickName}}</div>
                      </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    prop="sex"
                    label="性别">
                      <template slot-scope="scope">
                          <div>{{scope.row.sex == '1' ? '男' : scope.row.sex == '2' ? '女' : '未知'}}</div>
                      </template>
                  </el-table-column>
                  <el-table-column
                          align="center"
                          prop="id"
                          label="用户编号">
                  </el-table-column>
              </el-table>
          </template>
          <template slot-scope="scope" slot="orderLogisticsForm">
              <div>
                  <el-table
                          :data="[scope.row.orderLogistics]"
                          border
                          style="width: 100%">
                      <el-table-column
                              align="center"
                              prop="userName"
                              label="姓名">
                      </el-table-column>
                      <el-table-column
                              align="center"
                              prop="telNum"
                              label="电话">
                      </el-table-column>
                      <el-table-column
                              align="center"
                              prop="address"
                              label="地址">
                      </el-table-column>
                  </el-table>
              </div>
          </template>
          <template slot-scope="scope" slot="name">
              <el-row :gutter="10" v-for="(item, index) in scope.row.listOrderItem" :key="index"
                      style="border:1px solid #eaeaea;padding: 5px">
                  <el-col :span="3">
                      <img :src="item.picUrl" width="100%"/>
                  </el-col>
                  <el-col :span="13" style="text-align: left">
                      <div class="spu-name">{{item.spuName}}</div>
                      <div class="spec-info">{{item.specInfo}}</div>
                  </el-col>
                  <el-col :span="8">
                      <div class="grid-content" style="color:red;">￥{{item.paymentPrice}}</div>
                      <div class="grid-content">×{{item.quantity}}件</div>
                      <div class="grid-content" v-if="item.status == '0' && scope.row.isPay == '0' && !scope.row.status">
                          <el-tag type="danger" @click="handleEditPrice(item)">改价</el-tag>
                      </div>
                  </el-col>
              </el-row>
          </template>
          <template slot-scope="scope" slot="createTime">
              <div>
                  <div class="grid-content">{{scope.row.createTime}}</div>
                  <div class="grid-content">{{scope.row.orderNo}}</div>
              </div>
          </template>
          <template slot-scope="scope" slot="salesPrice">
              <div style="text-align: left">
                  <div class="grid-content">订单金额：￥{{scope.row.salesPrice}}</div>
                  <div class="grid-content">运费金额：+ ￥{{scope.row.freightPrice}}</div>
                  <div class="grid-content" style="color: red">支付金额：￥{{scope.row.paymentPrice}}</div>
              </div>
          </template>
          <template slot-scope="scope" slot="menu">
              <el-button icon="el-icon-edit"
                         size="small"
                         type="text"
                         v-if="checkPermi(['mall:orderinfo:get'])"
                         @click="openView(scope.row,scope.index)">订单详情
              </el-button>
              <el-button icon="el-icon-position"
                         size="small"
                         type="text"
                         v-if="checkPermi(['mall:orderinfo:edit']) && scope.row.status == '1'"
                         @click="showDialogLogistics(scope.row,scope.index)">发货
              </el-button>
              <el-button icon="el-icon-delete"
                         size="small"
                         type="text"
                         v-if="checkPermi(['mall:orderinfo:edit']) && scope.row.isPay == '0' && !scope.row.status"
                         @click="orderCancel(scope.row,scope.index)">取消
              </el-button>
          </template>
      </avue-crud>
      <el-dialog
              title="发货"
              :visible.sync="dialogLogistics"
              width="30%">
          <avue-form :option="logisticsOption" v-model="logisticsForm" @submit="delivery"></avue-form>
      </el-dialog>
    </div>
</template>

<script>

    import {checkPermi, checkRole} from "@/utils/permission"
    import {getPage, getObj, addObj, putObj, delObj, editPrice, orderCancel, takeGoods} from '@/api/mall/orderinfo'
    import {tableOption} from '@/const/crud/mall/orderinfo'

    export default {
        name: 'orderinfo',
        data() {
            return {
                checkPermi: checkPermi,
                date: [],
                status: '-1',
                tableData: [],
                page: {
                    total: 0, // 总页数
                    currentPage: 1, // 当前页数
                    pageSize: 20, // 每页显示多少条
                    ascs: [],//升序字段
                    descs: 'create_time'//降序字段
                },
                paramsSearch: {},
                tableLoading: false,
                tableOption: tableOption,
                dialogLogistics: false,
                logisticsForm: {
                    row: {},
                    logistics: null,
                    logisticsNo: null,
                    address: null
                },
                logisticsOption: {
                    emptyBtn: false,
                    card: true,
                    group: [
                        {
                            icon: 'el-icon-user',
                            label: '收货人信息',
                            prop: 'group1',
                            column: [{
                                label: '收货人名字',
                                prop: 'userName',
                                span: 24,
                                readonly: true
                            }, {
                                label: '电话号码',
                                prop: 'telNum',
                                span: 24,
                                readonly: true
                            }, {
                                label: '收货地址',
                                prop: 'address',
                                type: 'textarea',
                                span: 24,
                                readonly: true
                            }]
                        }, {
                            icon: 'el-icon-truck',
                            label: '快递信息',
                            prop: 'group2',
                            column: [{
                                label: '快递公司',
                                prop: 'logistics',
                                dicUrl: '/orderlogistics/dict/LOGISTICS',
                                span: 24,
                                type: 'select',
                                rules: [{
                                    required: true,
                                    message: "请选择快递公司",
                                    trigger: "blur"
                                }]
                            },
                                {
                                    label: '快递单号',
                                    prop: 'logisticsNo',
                                    span: 24,
                                    rules: [{
                                        required: true,
                                        message: "请输入快递单号",
                                        trigger: "blur"
                                    }]
                                }]
                        }
                    ]
                },
                dialogRefunds: false,
                orderItemObj: {}
            }
        },
        created() {
        },
        mounted: function () {
        },
        computed: {
            permissionList() {
                return {
                    addBtn: checkPermi(['mall:orderinfo:add']),
                    delBtn: checkPermi(['mall:orderinfo:del']),
                    editBtn: checkPermi(['mall:orderinfo:edit']),
                    viewBtn: checkPermi(['mall:orderinfo:get'])
                };
            }
        },
        methods: {
            //改价
            handleEditPrice(obj){
                this.$prompt('请输入价格', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputType: 'number',
                    inputPattern: /\S/,
                    inputErrorMessage: '请输入价格'
                }).then(({ value }) => {
                    editPrice({
                        id: obj.id,
                        paymentPrice: value
                    }).then(data => {
                        this.getPage(this.page)
                    }).catch(() => {

                    })
                }).catch(() => {

                })
            },
            handleClickStatus(tab, event) {
                this.status = tab.name
                this.page.currentPage = 1
                this.getPage(this.page)
            },
            openView(row, index) {
                this.tableLoading = true
                getObj(row.id).then(response => {
                    row.userInfo = response.data.userInfo
                    row.orderLogistics = response.data.orderLogistics
                    this.$refs.crud.rowView(row, index)
                    this.tableLoading = false
                })
            },
            //订单取消
            orderCancel(row, index){
                var _this = this
                this.$confirm('是否确认取消此订单', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    return orderCancel(row.id).then(data => {
                        _this.$message({
                            showClose: true,
                            message: '取消成功',
                            type: 'success'
                        })
                        _this.getPage(_this.page)
                    }).catch(function (err) {
                    })
                })
            },
            showDialogLogistics(row, index, done) {
                this.logisticsForm.row = row
                this.logisticsForm.address = row.orderLogistics.address
                this.logisticsForm.userName = row.orderLogistics.userName
                this.logisticsForm.telNum = row.orderLogistics.telNum
                this.dialogLogistics = true
            },
            delivery(form, done) {
                let row = this.logisticsForm.row
                row.status = '2',
                row.logistics = form.logistics,
                row.logisticsNo = form.logisticsNo
                putObj(row).then(data => {
                    this.$message({
                        showClose: true,
                        message: '发货成功',
                        type: 'success'
                    })
                    done()
                    this.getPage(this.page)
                    this.dialogLogistics = false
                }).catch(() => {
                    done()
                })
            },
            dateChange(date){
                if(date){
                    this.date = date
                }else{
                    this.date = []
                }
                this.getPage(this.page)
            },
            searchChange(params,done) {
                params = this.filterForm(params)
                this.paramsSearch = params
                this.page.currentPage = 1
                this.getPage(this.page, params)
                done()
            },
            sortChange(val) {
                let prop = val.prop ? val.prop.replace(/([A-Z])/g, "_$1").toLowerCase() : '';
                if (val.order == 'ascending') {
                    this.page.descs = []
                    this.page.ascs = prop
                } else if (val.order == 'descending') {
                    this.page.ascs = []
                    this.page.descs = prop
                } else {
                    this.page.ascs = []
                    this.page.descs = []
                }
                this.getPage(this.page)
            },
            getPage(page, params) {
                this.tableLoading = true
                getPage(Object.assign({
                    current: page.currentPage,
                    size: page.pageSize,
                    descs: this.page.descs,
                    ascs: this.page.ascs,
                    status: this.status != '-1' ? this.status : null,
                    beginTime: this.date[0],
                    endTime: this.date[1]
                }, params, this.paramsSearch)).then(response => {
                    this.tableData = response.data.records
                    this.page.total = response.data.total
                    this.page.currentPage = page.currentPage
                    this.page.pageSize = page.pageSize
                    this.tableLoading = false
                }).catch(() => {
                    this.tableLoading = false
                })
            },
            /**
             * @title 数据删除
             * @param row 为当前的数据
             * @param index 为当前删除数据的行数
             *
             **/
            handleDel: function (row, index) {
                var _this = this
                this.$confirm('是否确认删除此数据', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    return delObj(row.id)
                }).then(data => {
                    _this.$message({
                        showClose: true,
                        message: '删除成功',
                        type: 'success'
                    })
                    this.getPage(this.page)
                }).catch(function (err) {
                })
            },
            /**
             * @title 数据更新
             * @param row 为当前的数据
             * @param index 为当前更新数据的行数
             * @param done 为表单关闭函数
             *
             **/
            handleUpdate: function (row, index, done, loading) {
                putObj(row).then(data => {
                    this.$message({
                        showClose: true,
                        message: '修改成功',
                        type: 'success'
                    })
                    done()
                    this.getPage(this.page)
                }).catch(() => {
                    loading()
                })
            },
            /**
             * @title 数据添加
             * @param row 为当前的数据
             * @param done 为表单关闭函数
             *
             **/
            handleSave: function (row, done, loading) {
                addObj(row).then(data => {
                    this.$message({
                        showClose: true,
                        message: '添加成功',
                        type: 'success'
                    })
                    done()
                    this.getPage(this.page)
                }).catch(() => {
                    loading()
                })
            },
            /**
             * 刷新回调
             */
            refreshChange(page) {
                this.getPage(this.page)
            }
        }
    }
</script>
<style>
    .spu-name {
        font-size: 13px;
    }

    .spec-info {
        margin-top: 10px;
        font-size: 12px;
        color: #7b7b7b;
    }
</style>

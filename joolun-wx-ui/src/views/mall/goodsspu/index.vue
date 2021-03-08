<!--
  - Copyright (C) 2018-2019
  - All rights reserved, Designed By www.joolun.com
  - 注意：
  - 本软件为www.joolun.com开发研制，项目使用请保留此说明
-->
<template>
  <div class="app-container">
      <avue-crud ref="crud"
                 :page="page"
                 :data="tableData"
                 :permission="permissionList"
                 :table-loading="tableLoading"
                 :option="tableOption"
                 :before-open="beforeOpen"
                 v-model="form"
                 @on-load="getPage"
                 @refresh-change="refreshChange"
                 @row-update="handleUpdate"
                 @row-save="handleSave"
                 @row-del="handleDel"
                 @sort-change="sortChange"
                 @search-change="searchChange"
                 @selection-change="selectionChange">
        <template slot="menuLeft">
          <el-button type="success"
                     @click="batchShelf('1')"
                     size="small"
                     icon="el-icon-document"
                     v-if="checkPermi(['mall:goodsspu:edit'])">批量上架</el-button>
          <el-button type="warning"
                     @click="batchShelf('0')"
                     size="small"
                     icon="el-icon-document"
                     v-if="checkPermi(['mall:goodsspu:edit'])">批量下架</el-button>
        </template>
        <template slot="picUrls" slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.picUrls[0]"
            :preview-src-list="scope.row.picUrls">
          </el-image>
        </template>
        <template slot="salesPrice" slot-scope="scope">
          <div style="color: red">￥{{scope.row.salesPrice}}</div>
        </template>
        <template slot="shelf" slot-scope="scope">
          <el-switch
                  active-value="1"
                  inactive-value="0"
                  v-model="scope.row.shelf"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  @change="changeShelf(scope.row)">
          </el-switch>
        </template>
        <template slot="descriptionForm" slot-scope="scope">
          <BaseEditor v-model="scope.row.description"/>
        </template>
        <template slot="picUrls" slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.picUrls?scope.row.picUrls[0]:''"
            :preview-src-list="scope.row.picUrls">
          </el-image>
        </template>

      </avue-crud>
  </div>
</template>

<script>
    import {checkPermi, checkRole} from "@/utils/permission"
    import {getPage, getObj, addObj, putObj, delObj, putObjShelf} from '@/api/mall/goodsspu'
    import {tableOption} from '@/const/crud/mall/goodsspu'
    import {mapGetters} from 'vuex'
    import BaseEditor from '@/components/Editor/index.vue'

    export default {
        name: 'goodsspu',
        components: {
            BaseEditor,
        },
        data() {
            return {
                checkPermi: checkPermi,
                form: {},
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
                dialogAppraises: false,
                optionAppraises: {
                    props: {
                        avatar: 'nickName',
                        author: 'headimgUrl',
                        body: 'content'
                    }
                },
              selectionData: '',
              pointsConfig: null
            }
        },
        watch:{
        },
        created() {
        },
        mounted: function () {
        },
        computed: {
            permissionList() {
                return {
                    addBtn: checkPermi(['mall:goodsspu:add']),
                    delBtn: checkPermi(['mall:goodsspu:del']),
                    editBtn: checkPermi(['mall:goodsspu:edit']),
                    viewBtn: checkPermi(['mall:goodsspu:get'])
                };
            },
        },
        methods: {

            selectionChange(list){
              this.selectionData = list
            },
            batchShelf(shelf){
              if(this.selectionData.length<=0){
                this.$message.error('请选择商品')
                return
              }
              let selectionIds = ''
              this.selectionData.forEach(item => {
                selectionIds += item.id+ ','
              })
              this.putObjShelf(selectionIds, shelf)
            },
            changeShelf(row){
              this.putObjShelf(row.id, row.shelf)
            },
            putObjShelf(ids, shelf){
                putObjShelf({
                  ids: ids,
                  shelf: shelf
                }).then(data => {
                  this.getPage(this.page)
                })
            },
            beforeOpen(done,type){
                if(type == 'add'){
                    done()
                }else if(type == 'edit'){
                    this.tableLoading = true
                    getObj(this.form.id).then(response => {
                      this.$set(this.form,'description', response.data.description)
                      this.tableLoading = false
                      done()
                    })
                }
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
                if (this.paramsSearch.categoryId) {
                    this.paramsSearch.categoryFirst = this.paramsSearch.categoryId[0]
                    this.paramsSearch.categorySecond = this.paramsSearch.categoryId[1]
                }
                getPage(Object.assign({
                    current: page.currentPage,
                    size: page.pageSize,
                    descs: this.page.descs,
                    ascs: this.page.ascs,
                }, params, this.paramsSearch)).then(response => {
                    let tableData = response.data.records
                    tableData.forEach(function (item, index) {
                        let categoryId = []
                        if(item.categoryFirst){
                          categoryId.push(item.categoryFirst)
                        }
                        if(item.categorySecond){
                          categoryId.push(item.categorySecond)
                        }
                        item.categoryId = categoryId
                    })
                    this.tableData = tableData
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
                row.categoryFirst = row.categoryId[0]
                row.categorySecond = row.categoryId[1]
                row.picUrls = row.picUrls?row.picUrls.split(','):''
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
                row.categoryFirst = row.categoryId[0]
                row.categorySecond = row.categoryId[1]
                row.picUrls = row.picUrls?row.picUrls.split(','):''
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

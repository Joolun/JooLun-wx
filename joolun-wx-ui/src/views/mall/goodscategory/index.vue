<!--
  - Copyright (C) 2018-2019
  - All rights reserved, Designed By www.joolun.com
  - 注意：
  - 本软件为www.joolun.com开发研制，项目使用请保留此说明
-->
<template>
  <div class="app-container">
    <avue-crud ref="crud"
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
               @row-del="handleDel">
      <template slot="enable" slot-scope="scope">
        <el-switch
          active-value="1"
          inactive-value="0"
          v-model="scope.row.enable"
          active-color="#13ce66"
          inactive-color="#ff4949"
          @change="changeEnable(scope.row)">
        </el-switch>
      </template>
      <template slot="picUrl" slot-scope="scope">
        <img
          style="height: 100px"
          :src="scope.row.picUrl">
      </template>
    </avue-crud>
  </div>
</template>

<script>
  import {checkPermi, checkRole} from "@/utils/permission"
  import {getPage, fetchTree, getObj, addObj, putObj, delObj} from '@/api/mall/goodscategory'
  import {tableOption} from '@/const/crud/mall/goodscategory'

  export default {
    components: {},
    name: 'goodscategory',
    data() {
      return {
        form: {},
        tableData: [],
        tableLoading: false,
        tableOption: tableOption
      }
    },
    created() {
    },
    mounted: function () {
    },
    computed: {
      permissionList() {
        return {
          addBtn: checkPermi(['mall:goodscategory:add']),
          delBtn: checkPermi(['mall:goodscategory:del']),
          editBtn: checkPermi(['mall:goodscategory:edit']),
          viewBtn: checkPermi(['mall:goodscategory:get']),
        };
      }
    },
    methods: {
      changeEnable(row) {
        putObj({
          id: row.id,
          enable: row.enable
        }).then(data => {

        })
      },
      beforeOpen(done, type) {
        done()
      },
      getPage() {
        this.tableLoading = true
        fetchTree().then(response => {
          let tableData = response.data
          this.tableData = tableData
          let parentIdDIC = [{
            id: "0",
            name: "顶级分类",
            parentId: "0"
          }]
          tableData.forEach(item => {
            parentIdDIC.push({
              id: item.id,
              name: item.name,
              parentId: item.parentId
            })
          })
          this.$refs.crud.DIC.parentId = parentIdDIC
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

<style lang="scss" scoped>
</style>

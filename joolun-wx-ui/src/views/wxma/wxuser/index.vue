<!--
MIT License

Copyright (c) 2020 www.joolun.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
-->
<template>
    <div class="app-container">
        <avue-crud ref="crud"
                   :page="page"
                   :data="tableData"
                   :table-loading="tableLoading"
                   :option="tableOption"
                   :permission="permissionList"
                   @on-load="getPage"
                   @refresh-change="refreshChange"
                   @row-update="handleUpdate"
                   @row-save="handleSave"
                   @row-del="handleDel"
                   @sort-change="sortChange"
                   @search-change="searchChange"
                   @selection-change="selectionChange">
            <template slot="sex" slot-scope="scope">
                <el-tag size="mini" effect="light"
                        :type="scope.row.sex == '1' ? '' : scope.row.sex == '2' ? 'danger' : 'warning'">
                    {{scope.row.$sex}}
                </el-tag>
            </template>
        </avue-crud>
    </div>
</template>

<script>
    import {checkPermi, checkRole} from "@/utils/permission"
    import {getPage, getObj, addObj, putObj, delObj, synchroWxUser, updateRemark, tagging} from '@/api/wxma/wxuser'
    import {tableOption} from '@/const/crud/wxma/wxuser'

    export default {
        name: 'wxuser',
        data() {
            return {
                tableData: [],
                page: {
                    total: 0, // 总页数
                    currentPage: 1, // 当前页数
                    pageSize: 20, // 每页显示多少条
                    ascs: [],
                    descs: 'create_time'
                },
                paramsSearch: {},
                tableLoading: false,
                tableOption: tableOption,
                selectionData: [],
                dialogTagging: false,
            }
        },
        created() {

        },
        mounted: function () {
        },
        computed: {
            permissionList() {
                return {
                    addBtn: checkPermi(['wxma:wxuser:add']),
                    delBtn: checkPermi(['wxma:wxuser:del']),
                    editBtn: checkPermi(['wxma:wxuser:edit']),
                    viewBtn: checkPermi(['wxma:wxuser:get'])
                }
            }
        },
        methods: {
            selectionChange(list) {
                this.selectionData = list
            },
            searchChange(params, done) {
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
                    appType: '1'
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
            updateRemark(row, index) {
                this.$prompt('请输入备注', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /\S/,
                    inputErrorMessage: '输入不能为空'
                }).then(({value}) => {
                    this.tableLoading = true
                    row.remark = value
                    updateRemark(row).then(response => {
                        this.tableLoading = false
                        if (response.code == 200) {
                            this.tableData.splice(index, 1, Object.assign({}, row))
                            this.$message({
                                showClose: true,
                                message: '修改成功',
                                type: 'success'
                            })
                            this.getPage(this.page)
                        } else {
                            this.$message.error(response.msg)
                        }
                    }).catch(() => {
                        this.tableLoading = false
                    })
                })
            },
            handleDel: function (row, index) {
                var _this = this
                this.$confirm('是否确认删除', '提示', {
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

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
  <div class="execution">
    <basic-container>
      <div class="add_but">
        <el-button type="primary"
                   @click="handleAddNews"
                   size="mini">新 增
        </el-button>
      </div>
      <el-dialog :title="operateMaterial=='add'?'新建图文':'修改图文'"
                 append-to-body
                 :before-close="dialogNewsClose"
                 :close-on-click-modal="false"
                 :visible.sync="dialogNewsVisible"
                 width="80%"
                 top="20px">
        <div class="left">
          <div class="select-item">
            <div v-for="(news, index) in articlesAdd" :key='news.id'>
              <div class="news-main father" v-if="index==0" :class="{'activeAddNews': isActiveAddNews == index}"
                   @click="activeNews(index)">
                <div class="news-content">
                  <img class="material-img" v-if="news.thumbUrl" :src="news.thumbUrl"/>
                  <div class="news-content-title">{{news.title}}</div>
                </div>
                <div class="child" v-if="articlesAdd.length>1">
                  <el-button type="mini" icon="el-icon-sort-down" @click="downNews(index)">下移</el-button>
                  <el-button v-if="operateMaterial=='add'" type="mini" icon="el-icon-delete"
                             @click="minusNews(index)">删除
                  </el-button>
                </div>
              </div>
              <div class="news-main-item father" v-if="index>0" :class="{'activeAddNews': isActiveAddNews == index}"
                   @click="activeNews(index)">
                <div class="news-content-item">
                  <div class="news-content-item-title ">{{news.title}}</div>
                  <div class="news-content-item-img">
                    <img class="material-img" v-if="news.thumbUrl" :src="news.thumbUrl" height="100%"/>
                  </div>
                </div>
                <div class="child">
                  <el-button v-if="articlesAdd.length > index+1" type="mini" icon="el-icon-sort-down"
                             @click="downNews(index)">下移
                  </el-button>
                  <el-button type="mini" icon="el-icon-sort-up" @click="upNews(index)">上移</el-button>
                  <el-button v-if="operateMaterial=='add'" type="mini" icon="el-icon-delete"
                             @click="minusNews(index)">删除
                  </el-button>
                </div>
              </div>
            </div>
            <div class="news-main-plus" @click="plusNews" v-if="articlesAdd.length<8 && operateMaterial=='add'">
              <i class="el-icon-circle-plus icon-plus"></i>
            </div>
          </div>
        </div>
        <div class="right" v-loading="addMaterialLoading">
          <!--富文本编辑器组件-->
          <el-row>
            <WxEditor v-model="articlesAdd[isActiveAddNews].content" :uploadData="uploadData"
                      v-if="hackResetEditor"/>
          </el-row>
          <br><br><br><br>
          <div class="input-tt">封面和摘要：</div>
          <div>
            <div class="thumb-div">
              <img class="material-img" v-if="articlesAdd[isActiveAddNews].thumbUrl"
                   :src="articlesAdd[isActiveAddNews].thumbUrl" :class="isActiveAddNews == 0 ? 'avatar':'avatar1'">
              <i v-else class="el-icon-plus avatar-uploader-icon"
                 :class="isActiveAddNews == 0 ? 'avatar':'avatar1'"></i>
              <div class="thumb-but">
                <el-upload
                  :action="actionUrl"
                  :headers="headers"
                  multiple
                  :limit="1"
                  :on-success="handleUploadSuccess"
                  :file-list="fileList"
                  :before-upload="beforeThumbImageUpload"
                  :data="uploadData">
                  <el-button slot="trigger" size="mini" type="primary">
                    本地上传
                  </el-button>
                  <el-button size="mini" type="primary"
                             @click="openMaterial" style="margin-left: 5px">素材库选择
                  </el-button>
                  <div slot="tip" class="el-upload__tip">支持bmp/png/jpeg/jpg/gif格式，大小不超过2M</div>
                </el-upload>
              </div>
            </div>
            <el-dialog title="选择图片" :visible.sync="dialogImageVisible" width="80%" append-to-body>
              <WxMaterialSelect :objData="{repType:'image'}"
                                @selectMaterial="selectMaterial"></WxMaterialSelect>
            </el-dialog>
            <el-input :rows="8" type="textarea" v-model="articlesAdd[isActiveAddNews].digest" placeholder="请输入摘要"
                      class="digest" maxlength="120"></el-input>
          </div>
          <div class="input-tt">标题：</div>
          <el-input v-model="articlesAdd[isActiveAddNews].title" placeholder="请输入标题"></el-input>
          <div class="input-tt">作者：</div>
          <el-input v-model="articlesAdd[isActiveAddNews].author" placeholder="请输入作者"></el-input>
          <div class="input-tt">原文地址：</div>
          <el-input v-model="articlesAdd[isActiveAddNews].contentSourceUrl" placeholder="请输入原文地址"></el-input>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogNewsVisible = false">取 消</el-button>
          <el-button type="primary" @click="subNews">提 交</el-button>
        </div>
      </el-dialog>
      <div class="waterfall" v-loading="tableLoading">
        <div v-if="item.content && item.content.newsItem" class="waterfall-item" v-for="item in tableData"
             :key='item.id'>
          <WxNews :objData="item.content.newsItem"></WxNews>
          <el-row class="ope-row">
            <el-button type="success" circle @click="handlePublishNews(item)">发布</el-button>
            <el-button type="primary" icon="el-icon-edit" circle @click="handleEditNews(item)"></el-button>
            <el-button type="danger" icon="el-icon-delete" circle @click="delMaterial(item)"></el-button>
          </el-row>
        </div>
      </div>
      <div v-if="tableData.length <=0 && !tableLoading" class="el-table__empty-block">
        <span class="el-table__empty-text">暂无数据</span>
      </div>
      <el-pagination
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page.sync="page.currentPage"
        :page-sizes="[10, 20]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total"
        class="pagination"
      >
      </el-pagination>
    </basic-container>
  </div>
</template>

<script>
  import {getPage, addObj, delObj, putObj, publish} from '@/api/wxmp/wxdraft'
  import { getPage as getPage1 } from '@/api/wxmp/wxmaterial'
  import WxEditor from '@/components/Editor/WxEditor.vue'
  import WxNews from '@/components/wx-news/main.vue'
  import WxMaterialSelect from '@/components/wx-material-select/main.vue'
  import { getToken } from '@/utils/auth'

  export default {
    name: 'wxdraft',
    components: {
      WxEditor,
      WxNews,
      WxMaterialSelect
    },
    data() {
      return {
        tableData: [],
        page: {
          total: 0, // 总页数
          currentPage: 1, // 当前页数
          pageSize: 10 // 每页显示多少条
        },
        page1: {
          total: 0, // 总页数
          currentPage: 1, // 当前页数
          pageSize: 10 // 每页显示多少条
        },
        tableLoading: false,
        actionUrl: process.env.VUE_APP_BASE_API +'/wxmaterial/materialFileUpload',
        headers: {
          Authorization: 'Bearer ' + getToken()
        },
        fileList: [],
        dialogVideoVisible: false,
        dialogNewsVisible: false,
        addMaterialLoading: false,
        uploadData: {
          "mediaType": 'image',
          "title": '',
          "introduction": ''
        },
        articlesAdd: [
          {
            "title": '',
            "thumbMediaId": '',
            "author": '',
            "digest": '',
            "showCoverPic": '',
            "content": '',
            "contentSourceUrl": '',
            "needOpenComment": '',
            "onlyFansCanComment": '',
            "thumbUrl": ''
          }
        ],
        isActiveAddNews: 0,
        dialogImageVisible: false,
        imageListData: [],
        tableLoading1: false,
        operateMaterial: 'add',
        articlesMediaId: '',
        hackResetEditor: false
      }
    },
    watch: {

    },
    created() {

    },
    mounted: function () {
      this.getPage(this.page)
    },
    computed: {

    },
    methods: {
      getPage(page, params) {
        this.tableData = []
        this.tableLoading = true
        getPage(Object.assign({
          current: page.currentPage,
          size: page.pageSize
        }, params)).then(response => {
          this.tableData = response.data.items
          this.page.total = response.data.totalCount
          this.page.currentPage = page.currentPage
          this.page.pageSize = page.pageSize
          this.tableLoading = false
        }).catch(() => {
          this.tableLoading = false
        })
      },
      sizeChange(val) {
        this.page.currentPage = 1
        this.page.pageSize = val
        this.getPage(this.page)
      },
      currentChange(val) {
        this.page.currentPage = val
        this.getPage(this.page)
      },
      delMaterial(item){
        this.$confirm('此操作将永久删除该草稿, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.tableLoading = true
          delObj({
            id:item.mediaId
          }).then(response => {
            this.tableLoading = false
            if(response.code == 200){
              this.getPage(this.page)
            }else{
              this.tableLoading = false
              this.$message.error('删除出错：' + response.msg)
            }
          }).catch(() => {
            this.tableLoading = false
          })
        })
      },
      getPage1(){
        this.tableLoading1 = true
        getPage1({
          current: this.page1.currentPage,
          size: this.page1.pageSize,
          type:'image'
        }).then(response => {
          this.tableLoading1 = false
          this.imageListData = response.data.items
          this.page1.total = response.data.totalCount
        }).catch(() => {
          this.tableLoading1 = false
        })
      },
      openMaterial(){
        this.imageListData = null
        this.page1.currentPage = 1
        this.getPage1()
        this.dialogImageVisible = true
      },
      dialogNewsClose(done){
        this.$confirm('修改内容可能还未保存，确定关闭吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.hackResetEditor = false//销毁组件
          this.$nextTick(() => {
            this.hackResetEditor = true//重建组件
          })
          this.isActiveAddNews = 0
          done()
        }).catch(() => {
        })

      },
      downNews(index){
        let temp = this.articlesAdd[index]
        this.articlesAdd[index] = this.articlesAdd[index+1]
        this.articlesAdd[index+1] = temp
        this.isActiveAddNews = index+1
      },
      upNews(index){
        let temp = this.articlesAdd[index]
        this.articlesAdd[index] = this.articlesAdd[index-1]
        this.articlesAdd[index-1] = temp
        this.isActiveAddNews = index-1
      },
      activeNews(index){
        this.hackResetEditor = false//销毁组件
        this.$nextTick(() => {
          this.hackResetEditor = true//重建组件
        })
        this.isActiveAddNews = index
      },
      minusNews(index){
        this.$confirm('确定删除该图文吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.articlesAdd.splice(index,1);
          if(this.isActiveAddNews ==  index){
            this.isActiveAddNews = 0
          }
        }).catch(() => {
        })
      },
      plusNews(){
        this.articlesAdd.push(
          {
            "title": '',
            "thumbMediaId": '',
            "author": '',
            "digest": '',
            "showCoverPic": '',
            "content": '',
            "contentSourceUrl": '',
            "needOpenComment":'',
            "onlyFansCanComment":'',
            "thumbUrl":''
          }
        )
        this.isActiveAddNews = this.articlesAdd.length - 1
      },
      subNews(){
        this.addMaterialLoading = true
        if(this.operateMaterial == 'add'){
          addObj({
            articles:this.articlesAdd
          }).then(response => {
            this.addMaterialLoading = false
            this.dialogNewsVisible = false
            if(response.code == 200){
              this.isActiveAddNews = 0
              this.articlesAdd = [
                {
                  "title": '',
                  "thumbMediaId": '',
                  "author": '',
                  "digest": '',
                  "showCoverPic": '',
                  "content": '',
                  "contentSourceUrl": '',
                  "needOpenComment":'',
                  "onlyFansCanComment":'',
                  "thumbUrl":''
                }
              ]
              this.page.currentPage = 1
              this.getPage(this.page)
            }else{
              this.$message.error('新增图文出错：' + response.msg)
            }
          }).catch(() => {
            this.addMaterialLoading = false
          })
        }
        if(this.operateMaterial == 'edit'){
          putObj({
            articles:this.articlesAdd,
            mediaId:this.articlesMediaId
          }).then(response => {
            this.addMaterialLoading = false
            this.dialogNewsVisible = false
            if(response.code == 200){
              this.isActiveAddNews = 0
              this.articlesAdd = [
                {
                  "title": '',
                  "thumbMediaId": '',
                  "author": '',
                  "digest": '',
                  "showCoverPic": '',
                  "content": '',
                  "contentSourceUrl": '',
                  "needOpenComment":'',
                  "onlyFansCanComment":'',
                  "thumbUrl":''
                }
              ]
              this.getPage(this.page)
            }else{
              this.$message.error('修改图文出错：' + response.msg)
            }
          }).catch(() => {
            this.addMaterialLoading = false
          })
        }
      },
      handleEditNews(item){
        this.hackResetEditor = false//销毁组件
        this.$nextTick(() => {
          this.hackResetEditor = true//重建组件
        })
        if(this.operateMaterial == 'add'){
          this.isActiveAddNews = 0
        }
        this.operateMaterial = 'edit'
        this.articlesAdd = JSON.parse(JSON.stringify(item.content.newsItem))
        this.articlesMediaId = item.mediaId
        this.dialogNewsVisible = true
      },
      handleAddNews(){
        this.hackResetEditor = false//销毁组件
        this.$nextTick(() => {
          this.hackResetEditor = true//重建组件
        })
        if(this.operateMaterial == 'edit'){
          this.isActiveAddNews = 0
          this.articlesAdd = [
            {
              "title": '',
              "thumbMediaId": '',
              "author": '',
              "digest": '',
              "showCoverPic": '',
              "content": '',
              "contentSourceUrl": '',
              "needOpenComment":'',
              "onlyFansCanComment":'',
              "thumbUrl":''
            }
          ]
        }
        this.operateMaterial = 'add'
        this.dialogNewsVisible = true
      },
      beforeThumbImageUpload(file){
        this.addMaterialLoading = true
        const isType = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif' || file.type === 'image/bmp' || file.type === 'image/jpg';
        const isLt = file.size / 1024 / 1024 < 2
        if (!isType) {
          this.$message.error('上传图片格式不对!')
        }
        if (!isLt) {
          this.$message.error('上传图片大小不能超过2M!')
        }
        this.addMaterialLoading = false
        return isType && isLt;
      },
      handleUploadSuccess(response, file, fileList){
        this.tableLoading = false
        this.addMaterialLoading = false
        if(response.code == 200){
          this.dialogVideoVisible = false
          this.fileList = []
          this.uploadData.title = ''
          this.uploadData.introduction = ''
          this.articlesAdd[this.isActiveAddNews].thumbMediaId = response.data.mediaId
          this.articlesAdd[this.isActiveAddNews].thumbUrl = response.data.url
        }else{
          this.$message.error('上传出错：' + response.msg)
        }
      },
      selectMaterial(item){
        this.dialogImageVisible = false
        this.articlesAdd[this.isActiveAddNews].thumbMediaId = item.mediaId
        this.articlesAdd[this.isActiveAddNews].thumbUrl = item.url
      },
      handlePublishNews(item){
        this.$confirm('你正在通过发布的方式发表内容。 发布不占用群发次数，一天可多次发布。已发布内容不会推送给用户，也不会展示在公众号主页中。 发布后，你可以前往发表记录获取链接，也可以将发布内容添加到自定义菜单、自动回复、话题和页面模板中。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.tableLoading = true
          publish(item.mediaId).then(response => {
            this.tableLoading = false
            this.$message.success('发布任务提交成功')
            this.getPage(this.page)
          }).catch(() => {
            this.tableLoading = false
          })
        }).catch(() => {
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .pagination {
    float: right;
    margin-right: 25px;
  }

  .add_but {
    padding: 10px;
  }

  .ope-row {
    margin-top: 5px;
    text-align: center;
    border-top: 1px solid #eaeaea;
    padding-top: 5px;
  }

  .item-name {
    font-size: 12px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    text-align: center;
  }

  .el-upload__tip {
    margin-left: 5px;
  }

  /*新增图文*/
  .left {
    display: inline-block;
    width: 35%;
    vertical-align: top;
    margin-top: 200px;
  }

  .right {
    display: inline-block;
    width: 60%;
    margin-top: -40px;
  }

  .avatar-uploader {
    width: 20%;
    display: inline-block;
  }

  .avatar-uploader .el-upload {
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    text-align: unset !important;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #165dff;
  }

  .avatar-uploader-icon {
    border: 1px solid #d9d9d9;
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }

  .avatar {
    width: 230px;
    height: 120px;
  }

  .avatar1 {
    width: 120px;
    height: 120px;
  }

  .digest {
    width: 60%;
    display: inline-block;
    vertical-align: top;
  }

  /*新增图文*/
  /*瀑布流样式*/
  .waterfall {
    width: 100%;
    column-gap: 10px;
    column-count: 5;
    margin: 0 auto;
  }

  .waterfall-item {
    padding: 10px;
    margin-bottom: 10px;
    break-inside: avoid;
    border: 1px solid #eaeaea;
  }

  p {
    line-height: 30px;
  }

  @media (min-width: 992px) and (max-width: 1300px) {
    .waterfall {
      column-count: 3;
    }
    p {
      color: red;
    }
  }

  @media (min-width: 768px) and (max-width: 991px) {
    .waterfall {
      column-count: 2;
    }
    p {
      color: orange;
    }
  }

  @media (max-width: 767px) {
    .waterfall {
      column-count: 1;
    }
  }

  /*瀑布流样式*/
  .news-main {
    background-color: #FFFFFF;
    width: 100%;
    margin: auto;
    height: 120px;
  }

  .news-content {
    background-color: #acadae;
    width: 100%;
    height: 120px;
    position: relative;
  }

  .news-content-title {
    display: inline-block;
    font-size: 15px;
    color: #FFFFFF;
    position: absolute;
    left: 0px;
    bottom: 0px;
    background-color: black;
    width: 98%;
    padding: 1%;
    opacity: 0.65;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    height: 25px;
  }

  .news-main-item {
    background-color: #FFFFFF;
    padding: 5px 0px;
    border-top: 1px solid #eaeaea;
    width: 100%;
    margin: auto;
  }

  .news-content-item {
    position: relative;
    margin-left: -3px
  }

  .news-content-item-title {
    display: inline-block;
    font-size: 12px;
    width: 70%;
  }

  .news-content-item-img {
    display: inline-block;
    width: 25%;
    background-color: #acadae
  }

  .input-tt {
    padding: 5px;
  }

  .activeAddNews {
    border: 5px solid #2bb673;
  }

  .news-main-plus {
    width: 280px;
    text-align: center;
    margin: auto;
    height: 50px;
  }

  .icon-plus {
    margin: 10px;
    font-size: 25px;
  }

  .select-item {
    width: 60%;
    padding: 10px;
    margin: 0 auto 10px auto;
    border: 1px solid #eaeaea;
  }

  .father .child {
    display: none;
    text-align: center;
    position: relative;
    bottom: 25px;
  }

  .father:hover .child {
    display: block;
  }

  .thumb-div {
    display: inline-block;
    width: 30%;
    text-align: center;
  }

  .thumb-but {
    margin: 5px;
  }

  .material-img {
    width: 100%;
    height: 100%;
  }
</style>

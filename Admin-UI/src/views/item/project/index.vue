<template>
  <div class="app-container calendar-list-container">
    <!--查询条件-->
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="项目名称"
                v-model="listQuery.name"></el-input>
      <el-select class="filter-item" style="width: 200px;" v-model="listQuery.dictType" placeholder="请选择状态">
        <el-option v-for="a in typeOptions" :key="a.id" :label="a.name" :value="a.id"></el-option>
      </el-select>
      <el-select class="filter-item" style="width: 200px;" v-model="listQuery.dictStatus" placeholder="请选择类型">
        <el-option v-for="a in statusOptions" :key="a.id" :label="a.name" :value="a.id"></el-option>
      </el-select>
      <el-button class="filter-item" type="primary" icon="search" @click="handleFilter" title="搜索">搜索</el-button>
      <el-button class="filter-item" type="primary" icon="plus" v-if="userManager_btn_add" title="新增"
                 style="margin-left:10px;" @click="handleCreate">新增
      </el-button>
    </div>
    <!--列表-->
    <el-row>
      <el-col :span="4" v-for="(row,index) in list" :key="index" style="margin-bottom:10px;margin-left: 20px"
              @click.native="getNodeData(row)">
        <el-card :body-style="{ padding: '0px' }">
          <img :src="row.logo" class="image_project">
          <div style="padding: 14px;">
            <span>{{row.name}}</span>
            <div class="bottom clearfix">
              <time class="time">{{row.createTime }}</time>
            </div>
          </div>
          <el-button class="button1" v-if="userManager_btn_del" size="small" type="danger" @click="handleDelete(row)">删除
          </el-button>
          <el-button class="button1" v-if="userManager_btn_edit" size="small" type="success" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button class="button1" size="small" type="success" @click="toStage(row.id)">任务
          </el-button>

        </el-card>
      </el-col>
    </el-row>
    <!--分页-->
    <div v-show="!listLoading" class="pagination-container" style="margin-bottom:10px">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                     :current-page.sync="listQuery.page"
                     :page-sizes="[5.,10,20,30,50]" :page-size="listQuery.limit"
                     layout="total,sizes,prev,pager,next,jumper" :total="total">
      </el-pagination>
    </div>

    <!--项目成员-->
    <el-menu style="margin-bottom: 10px;">
      <el-menu-item index="1"><i class="el-icon-menu">&nbsp;&nbsp;{{projectName}}</i></el-menu-item>
    </el-menu>
    <el-card class="box-card">
      <user-project :projectId="currentId" :projectName="projectName"  ref="projectUser"></user-project>
    </el-card>

    <!--编辑框-->
    <el-dialog :visible.sync="dialogFormVisible" :before-close="handleClose" width="35%" style="height: 680px;">
      <el-row style="height: 30px;">
        <el-col :span="24" >
          <el-breadcrumb separator-class="el-icon-arrow-right"  style="bottom:0px; left:0px; position:absolute;">
            <el-breadcrumb-item :to="{ path: '/item/project'}"><i class="el-icon-notebook-2" />项目管理</el-breadcrumb-item>
            <el-breadcrumb-item>{{this.textMap[dialogStatus]}}项目</el-breadcrumb-item>
          </el-breadcrumb>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
      </el-row>
      <el-form :model="form" :inline="true" :rules="rules" ref="form" label-width="90px">
        <el-row>
          <el-col :span="12">
            <!--label="名称"-->
            <el-form-item  prop="name">
              <el-col :span="7">
                <i class="el-icon-collection"></i>&nbsp;<span class="fontclass">名&nbsp;&nbsp;称</span>
              </el-col>
              <el-col :span="17">
                <el-input v-model="form.name" placeholder="请输入名称" class="input-selects-width"></el-input>
              </el-col>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="sort">
              <el-col :span="7">
                <i class="el-icon-sort"></i>&nbsp;<span class="fontclass">排&nbsp;&nbsp;序</span>
              </el-col>
              <el-col :span="17">
                <el-input v-if="dialogStatus=='create'" v-model="form.sort" placeholder="请输入排序"
                        class="input-selects-width"></el-input>
                <el-input v-else v-model="form.sort" placeholder="请输入排序" class="input-selects-width"></el-input>
              </el-col>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item>
              <el-col :span="7">
                <i class="el-icon-coordinate"></i>&nbsp;<span class="fontclass">状&nbsp;&nbsp;态</span>
              </el-col>
              <el-col :span="17">
                <el-select class="filter-item input-selects-width" v-model="form.dictType" placeholder="请选择">
                  <el-option v-for="a in typeOptions" :key="a.id" :label="a.name" :value="a.id"></el-option>
                </el-select>
              </el-col>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item>
              <el-col :span="7">
                <i class="el-icon-wallet"></i>&nbsp;<span class="fontclass">类&nbsp;&nbsp;型</span>
              </el-col>
              <el-col :span="17">
                <el-select class="filter-item input-selects-width" v-model="form.dictStatus" placeholder="请选择">
                  <el-option v-for="a in statusOptions" :key="a.id" :label="a.name" :value="a.id"></el-option>
                </el-select>
              </el-col>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="dialogStatus=='update'">
          <el-col :span="12">
            <el-form-item prop="createUserName">
              <el-col :span="7">
                <i class="el-icon-s-custom"></i>&nbsp;<span class="fontclass">创建人</span>
              </el-col>
              <el-col :span="17">
                <el-input v-model="form.createUserName" class="input-selects-width" readonly></el-input>
              </el-col>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="updateUserName">
              <el-col :span="7">
                <i class="el-icon-user"></i>&nbsp;<span class="fontclass">修改人</span>
              </el-col>
              <el-col :span="17">
                <el-input v-model="form.updateUserName" class="input-selects-width" readonly></el-input>
              </el-col>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="description">
              <el-col :span="7">
                <i class="el-icon-edit-outline"></i>&nbsp;<span class="fontclass">备&nbsp;&nbsp;注</span>
              </el-col>
              <el-col :span="17">
              <el-input type="textarea" :autosize="{ minRows: 5, maxRows: 10}" class="input-selects-width"
                        placeholder="请输入备注"
                        v-model="form.description"></el-input>
              </el-col>
            </el-form-item>
          </el-col>
          <el-col :span="12" >
            <el-form-item>
              <el-col :span="9">
                <i class="el-icon-upload"></i>&nbsp;<span class="fontclass">Logo</span>
              </el-col>
              <el-col :span="15">&nbsp;
                <a href="javascript:;" class="file">选择文件
                  <input @change="uploadPhoto($event)" type="file" id="fileImage" ref="fileImage">
                </a>
                <img v-if="dialogStatus=='create'" :src="fileImage" alt="" style="width: 50px ;height: 50px">
                <img v-if="dialogStatus=='update'" :src="fileLogo" alt="" style="width: 50px ;height: 50px">
              </el-col>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script scoped>
  import {
    page, addObj, delObj, putObj, getDict, getUserBoolean
  }
  from 'api/admin/project/index'
  import {mapGetters} from 'vuex'

  export default {
    name: 'project',
    components: {
      'user-project': () => import('./user')
    },
    data() {
      return {
        form: this.initObj(),
        rules: {
          name: [
            {
              required: true,
              message: '请输入用户名',
              trigger: 'blur'
            }
          ]
        },
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          userId: '',
          dictType: '',
          dictStatus: '',
          page: 1,
          limit: 5,
          name: undefined
        },
        userQuery: {
          page: 1,
          limit: 10,
          projectId: undefined
        },
        currentId: '',
        projectName: '项目成员',
        statusOptions: [],
        typeOptions: [],
        dialogFormVisible: false,
        userStatus: '',
        dialogStatus: '',
        userManager_btn_edit: false,
        userManager_btn_add: false,
        userManager_btn_del: false,
        textMap: {
          update: '更新',
          create: '创建'
        },
        tableKey: 0,
        treeNodes: '',
        userList: [],
        fileImage: '',
        fileLogo: '',
        projectUsers: []
      }
    },
    created() {
      console.log(this.client);
      this.mqttMSG();
      this.getList();
      this.getStatus('xm-status');
      this.getType('xm-type');
      this.userManager_btn_edit = true;
      this.userManager_btn_add = true;
      this.userManager_btn_del = true
    },
    computed: {
      ...mapGetters([
        'user',
        'client',
        'name'
      ])
    },
    methods: {
      mqttMSG() {
        this.client.on('message', (topic, message) => {
          const msg = message.toString();
          if (topic === 'project/del') {
            for (var i = 0; i < this.list.length; i++) {
              if (msg.split('/')[1] === this.list[i].id) {
                // if (this.projectName === this.list[i].name) {
                //   this.projectName = '【】关联用户';
                // }
                console.log('project1收到来自', topic, '的消息', message.toString());
                this.getList();
                if (this.currentId === message.toString().split('/')[1]) {
                  this.projectName = '项目成员'
                }
                // this.$router.push({
                //   path: '/item/project'
                // })
              }
            }
          }
          getUserBoolean(msg.split('/')[1], this.user.id).then(response => {
            if (response.data && topic.split('/')[0] === 'project') {
              if (this.currentId === msg.split('/')[1]) {
                this.projectName = '【' + msg.split('/')[2] + '】关联用户';
              }
              this.getList();
              console.log('project收到来自', topic, '的消息', message.toString());
            }
          });
          if (topic === 'user/add') {
            var listId = msg.split('/')[2];
            for (var j = 0 ; j < listId.split(',').length ; j++) {
              if (this.user.id === listId[j]) {
                this.getList();
              }
            }
          }
          if (topic === 'user/del' && msg.split('/')[2] === this.user.id) {
            this.getList();
          }
        });
      },
      mqttSend(topic, message) {
        // 发布消息
        if (!this.client.connected) {
          console.log('客户端未连接')
          return
        }
        this.client.publish(topic, message, (error) => {
          console.log(error || message + '消息发布成功')
        })
      },
      toStage(id) {
        console.log('to-task')
        this.$router.push({
          path: '/project/' + id
        })
      },
      uploadPhoto(e) {
        // 利用fileReader对象获取file
        var file = e.target.files[0];
        var filesize = file.size;
        // 2,621,440   2M
        if (filesize > 2101440) {
          this.$refs.fileImage.value = ''
          alert('文件大于2M');
          return false;
        }
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = (e) => {
          this.fileImage = e.target.result;
          this.fileLogo = e.target.result;
          // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
          if (this.dialogStatus === 'create') {
            this.form.logo = e.target.result;
          }
        }
      },
      getNodeData(row) {
        this.currentId = row.id;
        this.projectName = '【' + row.name + '】关联用户';
        if (this.$refs.projectUser) {
          this.$refs.projectUser.projectId = row.id;
          this.$refs.projectUser.projectManager_btn_user_add = true;
          this.$refs.projectUser.getList();
        }
      },
      getStatus(code) {
        getDict(code).then(response => {
          this.statusOptions = response.data;
        })
      },
      getType(code) {
        getDict(code).then(response => {
          this.typeOptions = response.data
        })
      },
      initObj() {
        return {
          id: undefined,
          name: undefined,
          description: undefined,
          logo: undefined,
          dictStatus: undefined,
          sort: undefined,
          dictType: undefined,
          createTime: undefined,
          createUser: undefined,
          createUserName: undefined,
          updateTime: undefined,
          updateUser: undefined,
          updateUserName: undefined,
          status: undefined,
          type: undefined
        }
      },
      handleClose(done) {
        this.cancel('form');
        done();
      },
      getList() {
        this.listLoading = true;
        this.listQuery.userId = this.user.id;
        page(this.listQuery).then(response => {
          this.list = response.data.rows;
          this.total = response.data.total;
          this.listLoading = false
        })
      },
      handleFilter() {
        this.getList()
      },
      handleSizeChange(val) {
        this.listQuery.limit = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.listQuery.page = val
        this.getList()
      },
      handleCreate() {
        this.resetTemp();
        this.dialogStatus = 'create';
        this.dialogFormVisible = true
      },
      handleUpdate(row) {
        this.form = row;
        this.fileLogo = row.logo;
        this.dialogFormVisible = true;
        this.dialogStatus = 'update'
      },
      handleDelete(row) {
        this.$confirm('是否刪除该记录？', '记录', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delObj(row.id).then(() => {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            });
            this.mqttSend('project/del', '删除项目名称为' + row.name + '的项目/' + row.id);
            // const index = this.list.indexOf(row);
            // this.list.splice(index, 1)
            this.getList();
          })
        })
      },
      create(formName) {
        this.fileImage = '';
        this.$refs.fileImage.value = ''
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            // console.info(this.user.id);
            this.form.createUser = this.user.id;
            let addProjectId = '';
            addObj(this.form).then(response => {
              addProjectId = response.data;
              this.dialogFormVisible = false;
              // this.getList();
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              });
              this.getList();
              this.mqttSend('project/add', '新增项目名称为' + this.form.name + '的项目/' + addProjectId + '/' + this.user.id);
            })
          } else {
            return false
          }
        });
      },
      cancel(formName) {
        this.fileImage = '';
        this.$refs.fileImage.value = ''
        this.dialogFormVisible = false;
        // this.$refs[formName].resetFields()
      },
      update(formName) {
        this.fileImage = '';
        this.$refs.fileImage.value = ''
        const set = this.$refs
        set[formName].validate(valid => {
          if (valid) {
            this.dialogFormVisible = false;
            this.form.password = undefined;
            this.form.logo = this.fileLogo;
            putObj(this.form.id, this.form).then(response => {
              this.dialogFormVisible = false
              // this.getList()
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              });
              this.getList();
              this.mqttSend('project/update', '更新项目名称为' + this.form.name + '的项目/' + this.form.id + '/' + response.data.name);
            })
          } else {
            return false
          }
        })
        this.getList();
      },
      resetTemp() {
        this.form = this.initObj();
      },
      getTreeNodes(msg) {
        var cid = '';
        var corg = '';
        cid = msg.map(item => item.id).join();
        corg = msg.map(item => item.label).join();
        this.treeNodes = msg
        this.form.corg = corg
        this.form.corgId = cid
      }
    }
  }
</script>
<style>
  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .image_project {
    width: 100%;
    height: 150px;
    display: block;
  }

  .button1 {
    float: right;
    width: 25%;
    margin-right: 10px;
    margin-bottom: 5px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .file {
    position: relative;
    display: inline-block;
    background: #D0EEFF;
    border: 1px solid #99D3F5;
    border-radius: 4px;
    padding: 4px 12px;
    overflow: hidden;
    color: #1E88C7;
    text-decoration: none;
    text-indent: 0;
    line-height: 20px;
  }
  .file input {
    position: absolute;
    font-size: 20px;
    right: 0;
    top: 0;
    opacity: 0;
  }
  .file:hover {
    background: #AADFFD;
    border-color: #78C3F3;
    color: #004974;
    text-decoration: none;
  }
  .el-dialog__headerbtn .el-dialog__close {
    color: #ffffff;
  }
  .el-dialog__body {
    padding: 10px 20px;
    color: #606266;
    font-size: 25px;
    word-break: break-all;
  }
  .el-dialog__header {
    padding: 0px 0px 0px;
  }
</style>

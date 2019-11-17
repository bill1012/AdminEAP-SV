<template>
  <div class="calendar-list-container">
    <!--查询条件-->
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="姓名或账户"
                v-model="listQuery.name"></el-input>
      <el-button class="filter-item" type="primary" icon="search" @click="handleFilter" title="搜索">搜索</el-button>
      <el-button class="filter-item" type="primary" icon="plus" v-if="projectManager_btn_user_add" title="新增"
                 style="margin-left:10px;" @click="handleAdd">添加
      </el-button>
    </div>
    <!--列表-->
    <el-table :key="tableKey" :data="userList" stripe v-loading.body="listLoading" fit highlight-current-row
              style="width: 100%" @selection-change="handleSelectionChange">
      <!--<el-table-column align="center" label="序号" type="index" width="80">
      </el-table-column>-->
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="姓名" sortable prop="name" width="140">
        <template slot-scope="scope">
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="账户" width="140">
        <template slot-scope="scope">
          <span>{{scope.row.username}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="性别" width="80">
        <template slot-scope="scope">
          <span>{{scope.row.sex}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="生日" width="140" prop="birthday">
        <template slot-scope="scope">
          <span>{{scope.row.birthday | formatDate('yyyy-MM-dd')}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="邮箱" width="200">
        <template slot-scope="scope">
          <span>{{scope.row.email}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="手机" width="200">
        <template slot-scope="scope">
          <span>{{scope.row.mobilePhone}}</span>
        </template>
      </el-table-column>

    </el-table>
    <!--分页-->
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                     :current-page.sync="listQuery.page"
                     :page-sizes="[10.,20,30,50]" :page-size="listQuery.limit"
                     layout="total,sizes,prev,pager,next,jumper" :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  import {getUserNO, addProUser, getUserBoolean} from 'api/admin/project/index'
  import {mapGetters} from 'vuex'

  export default {
    name: 'user',
    props: {
      projectId: {
        default: '-1'
      },
      projectName: {
        default: ''
      }
    },
    data() {
      return {
        userList: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 10,
          name: '',
          projectId: this.projectId
        },
        projectManager_btn_user_add: false,
        tableKey: 0,
        list: []
      }
    },
    created() {
      this.getList();
      this.mqttSelectUser();
      this.projectManager_btn_user_add = true;
    },
    computed: {
      ...mapGetters([
        'user',
        'elements',
        'client'
      ])
    },
    methods: {
      mqttSelectUser() {
        this.client.on('message', (topic, message) => {
          if (topic.split('/')[0] === 'user') {
            getUserBoolean(message.toString().split('/')[1], this.user.id).then(response => {
              if (response.data) {
                this.getList();
                console.log('userSelect收到来自', topic, '的消息', message.toString());
                this.msg = message.toString()
              }
            })
          }
        });
      },
      mqttSendUser(topic, message) {
        // 发布消息
        if (!this.client.connected) {
          console.log('客户端未连接')
          return
        }
        this.client.publish(topic, message, (error) => {
          console.log(error || message + '消息发布成功')
        })
      },
      getList() {
        this.listLoading = true;
        this.listQuery.projectId = this.projectId;
        getUserNO(this.listQuery).then(response => {
          this.userList = response.data.rows;
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
      handleAdd() {
        if (this.list) {
          console.log(this.list)
          addProUser(this.projectId, this.list, this.user.id).then(() => {
            this.$emit('closeDialog');
          })
          var listId = [];
          for (var i = 0; i < this.list.length; i++) {
            listId.push(this.list[i].id);
          }
          this.mqttSendUser('user/add', this.projectName + '项目添加关联成员/' + this.projectId + '/' + listId);
        } else {
          this.$notify({
            title: '提示',
            message: '请选择要关联的用户',
            type: 'info',
            duration: 2000
          });
        }
      },
      handleSelectionChange(val) {
        const userIdArr = [];
        for (var i = 0; i < val.length; i++) {
          userIdArr.push(val[i]);
        }
        this.list = userIdArr;
      }
    }
  }
</script>

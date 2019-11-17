<template>
  <div class="calendar-list-container">
    <!--查询条件-->
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="姓名或账户"
                v-model="listQuery.name"></el-input>
      <el-button class="filter-item" type="primary" icon="search" @click="handleFilter" title="搜索">搜索</el-button>
      <el-button class="filter-item" type="primary" icon="plus" v-if="roleManager_btn_user_add"
                 style="margin-left:10px;" @click="handleAdd">添加
      </el-button>
    </div>
    <!--列表-->
    <el-table :key="tableKey" :data="list" stripe v-loading.body="listLoading" fit highlight-current-row
              style="width: 100%">
      <el-table-column align="center" label="序号" type="index" width="80">
      </el-table-column>
      <el-table-column align="center" label="姓名" sortable prop="name" width="160">
        <template slot-scope="scope">
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="账户" width="120">
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
      <el-table-column align="center" label="邮箱" width="180">
        <template slot-scope="scope">
          <span>{{scope.row.email}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="手机" width="180">
        <template slot-scope="scope">
          <span>{{scope.row.mobilePhone}}</span>
        </template>
      </el-table-column>

      <el-table-column fixed="right" align="center" label="操作" width="150">
        <template slot-scope="scope">
          <el-button v-if="roleManager_btn_user_del" size="small" icon="delete" type="danger"
                     @click="handleDelete(scope.row)">移除
          </el-button>
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
    <el-dialog :visible.sync="dialogUserSelector" :title="dialogUserSelectorName">
      <user-selector ref="userSelector" :roleId="roleId" @closeDialog="closeDialogForUserSelector"></user-selector>
    </el-dialog>
  </div>
</template>

<script>
  import {page, delObj} from 'api/admin/role/user'
  import {mapGetters} from 'vuex'

  export default {
    name: 'user',
    props: {
      roleName: {
        default: ''
      }
    },
    components: {
      'user-selector': () => import('./userSelector')
    },
    data() {
      return {
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 10,
          name: undefined,
          roleId: this.roleId
        },
        roleId: '-1',
        roleManager_btn_user_add: false,
        roleManager_btn_user_del: false,
        tableKey: 0,
        dialogUserSelector: false,
        dialogUserSelectorName: '添加用户'
      }
    },
    created() {
      this.getList()
      this.roleManager_btn_user_add = false
      this.roleManager_btn_user_del = true
    },
    computed: {
      ...mapGetters([
        'elements'
      ])
    },
    methods: {
      closeDialogForUserSelector() {
        this.dialogUserSelector = false;
        this.getList();
        this.$notify({
          title: '提示',
          message: '角色关联人员成功',
          type: 'success',
          duration: 2000
        })
      },
      getList() {
        this.listLoading = true
        this.listQuery.roleId = this.roleId;
        page(this.listQuery).then(response => {
          this.list = response.data.rows
          this.total = response.data.total
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
        this.dialogUserSelector = true;
        this.dialogUserSelectorName = '【' + this.roleName + '】添加用户';
        if (this.$refs.userSelector) {
          this.$refs.userSelector.getList();
        }
      },
      handleDelete(row) {
        this.$confirm('是否刪除该记录？', '记录', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delObj(this.roleId, row.id).then(() => {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            const index = this.list.indexOf(row)
            this.list.splice(index, 1)
          })
        })
      }

    }
  }
</script>

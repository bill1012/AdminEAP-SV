<template>
  <div id="application" class="app-container calendar-list-container">
    <div class="app-order-manager">应用管理</div>
    <div class="app-order-div">
      <label class="app-order-label">排序 ： 创建时间  应用名称  </label>
      <el-checkbox> 只看异常</el-checkbox>
    </div>
    <div>
      <el-card class="box-card">
        <div class="app-size">
          <div style="height:30%">
          </div>
          <div style="height:40%;text-align:center;" @click="toCreate();">
            <i class="iconfont esp-admin-add-a"></i>
            <div style="margin-top:15px;">
              创建应用
            </div>
          </div>
          <div style="height:30%">
          </div>
        </div>
        <div v-for="app in appList" :key="app" class="app-size">
          <el-row style="height:85%;">
            <div style="height:25%;">
              <div class="app-div-left-location-danger" v-if="app.status!='正常'">{{app.status}}</div>
              <div class="app-div-left-location" v-else>{{app.status}}</div>
              <div class="app-div-right-location">
                {{app.createTime}}
              </div>
            </div>
            <div style="height:35%;float:left;width:100%">
              <div class="app-div-manager">{{app.name}}</div>
              <div style="margin: 5% 4%;">
                <i class="iconfont esp-admin-diannao2"></i>
                1
                <i class="iconfont esp-admin-yingpan"></i> 0.5G
              </div>
            </div>
            <div style="height:40%;">
              <div class="app-server-contriner">
                <span class="app-server-contriner-server">服务</span>
                <span class="app-server-contriner-number">{{app.service}}</span>
              </div>
              <div class="app-server-contriner">
                <span class="app-server-contriner-server">容器</span>
                <span class="app-server-contriner-number">{{app.container}}</span>
              </div>
            </div>
          </el-row>
          <el-row class="app-bottom-div">
            <div class="app-bottom-div-edit">
              <i class="iconfont esp-admin-gongneng"></i>
              <i class="iconfont esp-admin-edit"></i>
            </div>
            <div class="app-bottom-div-del" @click="deleted();">
              <i class="iconfont esp-admin-shanchu"></i>
            </div>
          </el-row>
        </div>
      </el-card>
    </div>
        <!--分页-->
    <div class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                     :current-page.sync="listQuery.page"
                     :page-sizes="[10.,20,30,50]" :page-size="listQuery.limit"
                     layout="total,sizes,prev,pager,next,jumper" :total="total">
      </el-pagination>
    </div>
    <el-dialog title="新增应用" :visible.sync="dialogFormVisible">
      <el-form :model="appForm">
        <el-row>
          <el-col :span="12">
            <el-form-item label="应用名称" :label-width="formLabelWidth">
              <el-input v-model="appForm.name" auto-complete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用IP" :label-width="formLabelWidth">
              <el-input v-model="appForm.ip" auto-complete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="应用名称" :label-width="formLabelWidth">
              <el-input v-model="appForm.name" auto-complete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用状态" :label-width="formLabelWidth">
              <el-select v-model="appForm.states" placeholder="请选择">
                <el-option
                  v-for="item in states"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="应用名称" :label-width="formLabelWidth">
              <el-input v-model="appForm.name" auto-complete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用IP" :label-width="formLabelWidth">
              <el-input v-model="appForm.ip" auto-complete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        dialogFormVisible: false,
        appForm: {
          name: '',
          ip: '',
          states: ''
        },
        formLabelWidth: '80px',
        states: [{
          value: '选项1',
          label: '状态一'
        }, {
          value: '选项2',
          label: '状态二'
        }, {
          value: '选项3',
          label: '状态三'
        }, {
          value: '选项4',
          label: '状态四'
        }, {
          value: '选项5',
          label: '状态五'
        }],
        appList: [
          {name: '人事信息辅助系统', service: 10, container: 25, status: '正常', createTime: '2017-10-01'},
          {name: '会议室预定系统', service: 4, container: 10, status: '故障', createTime: '2017-10-02'},
          {name: '员工用餐管理系统', service: 3, container: 25, status: '正常', createTime: '2017-10-03'},
          {name: '实验室运行管理系统', service: 10, container: 22, status: '正常', createTime: '2017-10-04'},
          {name: '物资管理系统', service: 3, container: 9, status: '正常', createTime: '2017-10-05'},
          {name: '招投标评分系统', service: 4, container: 11, status: '正常', createTime: '2017-10-10'},
          {name: '党务管理系统', service: 3, container: 10, status: '正常', createTime: '2017-10-23'},
          {name: '物业App后台系统', service: 5, container: 13, status: '正常', createTime: '2017-10-23'},
          {name: '科研管理公共信息平台', service: 12, container: 30, status: '故障', createTime: '2017-10-27'}
        ],
        listQuery: {
          page: 1,
          limit: 20,
          name: undefined
        }
      }
    },
    methods: {
      initObj() {
        return {
          name: '',
          code: '',
          levelCode: '',
          orgType: '', // 机构类别
          deleted: '',
          note: '',
          parentid: ''
        }
      },
      toCreate() {
        this.dialogFormVisible = true;
      },
      deleted() {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
        });
      }
    }
  }
</script>

<style scoped>
  .app-order-manager {
    margin-bottom: 17px;
    font-size: 30px;
  }

  .app-order-div {
    margin-bottom: 17px;
    font-size: 14px;
    margin-right: 19px;
  }

  .app-order-label {
    margin-right: 19px;
  }

  .app-size {
    height: 280px;
    width: 280px;
    float: left;
    margin: 18px;
    padding: 16px;
    background: white;
    border: 1px solid #d1dbe5;
    box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .12), 0 0 6px 0 rgba(0, 0, 0, .04);
  }

  .app-div-left-location {
    margin: 15px 10px;
    padding: 3px;
    float: left;
    font-size: 16px;
    color: white;
    width: 60px;
    text-align: center;
    background: #2672ac;
  }

  .app-div-left-location-danger {
    margin: 15px 10px;
    padding: 3px;
    float: left;
    font-size: 16px;
    color: white;
    width: 60px;
    text-align: center;
    background: #eb9e05;;
  }




  .app-div-right-location {
    margin: 15px 10px;
    padding: 3px;
    float: right;
    color: gray;
    font-size: 16px;
  }

  .app-div-manager {
    padding: 3px;
    font-size: 20px;
  }

  .app-server-contriner {
    margin: 15px 5px;
    float: left;
    width: 45%;
    height: 70%;
    background: #f4f3f3;
  }

  .app-server-contriner-server {
    font-size: 14px;
    float: left;
    margin: 18% 10%;
  }

  .app-server-contriner-number {
    font-size: 30px;
    float: right;
    margin: 10% 20% 10% 0%;
    color: #2672ac;
  }

  .app-bottom-div {
    height: 15%;
    margin: 0 3%;
    background-color: white;
    border-top: 2px solid #cdcdcd;
  }

  .app-bottom-div-edit {
    float: left;
  }

  .app-bottom-div-del {
    float: right;
  }
</style>

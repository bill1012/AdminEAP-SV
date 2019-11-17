<template>
  <div class="errPage-container">
    <!--<el-row>-->
      <!--<el-button icon="el-icon-search" circle></el-button>-->
      <!--<el-button type="primary" icon="el-icon-edit" circle></el-button>-->
      <!--<el-button type="success" icon="el-icon-check" circle></el-button>-->
      <!--<el-button type="info" icon="el-icon-message" circle></el-button>-->
      <!--<el-button type="warning" icon="el-icon-star-off" circle></el-button>-->
      <!--<el-button type="danger" icon="el-icon-delete" circle></el-button>-->
    <!--</el-row>-->
    <!--<el-button type="danger" icon="el-icon-delete-solid" circle></el-button>-->
    <!--<h3>项目视图：{{sendhea}}</h3>-->
    <div>
      <el-table
        :data="taskList"
        style="width: 100%" @row-click="openDetails">
        <el-table-column
          prop="name"
          label="任务名称"
          width="420">
        </el-table-column>
        <el-table-column
          label="时间"
          width="200">
          <template slot-scope="scope" v-if="scope.row.endTimeStr!=null">{{scope.row.endTimeStr.slice(0,10)}}</template>
        </el-table-column>
        <el-table-column
          prop="stageName"
          label="阶段"
          width="120">
        </el-table-column>
        <el-table-column
          prop="userName"
          label="处理人"
          width="120">
        </el-table-column>
        <el-table-column
          label="头像">
          <template slot-scope="scope">
            <img :src="scope.row.header" v-if="scope.row.header != null " class="task-img" style="width: 30px;height: 30px"/>
            <img src="/static/images/null.png" v-if="scope.row.header == null " class="task-img" style="width: 30px;height: 30px"/>
          </template>
        </el-table-column>
        <el-table-column
          prop="id"
          label="任务ID"
          width="1" v-if="false">
        </el-table-column>
      </el-table>
    </div>
    <edit :flag="flag" :taskId="taskId" :usersMap="usersMap" :userId="createUser" v-on:listenToChildEvent="showMsgFromChild"/>
    <!--<edit :flag="flag" :taskId="taskId" :usersMap="usersMap" :userId="data.createUser" v-on:listenToChildEvent="showMsgFromChild"/>-->
  </div>
</template>

<script>
  import { getTaskListVoByExample } from 'api/project/task/index'
  import { mapGetters } from 'vuex'
  import edit from '../mytask/module/edit'
  import { getUsersByProjectId, getUsersMapByProjectId } from 'api/admin/user/index'

  export default {
    created() {
      this.projectId = this.$route.params.id;
      this.getTaskList();
      this.getUsersByProject(this.projectId)
    },
    components: {
      edit
    },
    computed: {
      ...mapGetters([
        'user',
        'elements'
      ])
    },
    data() {
      return {
        flag: false,
        createUser: '2',
        taskId: '',
        usersMap: {},
        projectId: '',
        taskFrom: {
          projectId: '',
          userId: '',
          endTimeStr: '',
          finished: ''
        },
        taskList: []
      }
    },
    props: {
      sendhea: {
        type: String
      }
    },
    watch: {
      'sendhea': function() {
        this.getTaskList();
      }
    },
    methods: {
      getUsersByProject(id) {
        getUsersByProjectId(id).then(response => {
          this.users = response.data
          this.users.push({id: 'dairenling', name: '待认领'})
          this.users.reverse()
        })
        getUsersMapByProjectId(id, 1).then(response => {
          this.usersMap = response.data
          this.usersMap['dairenling'] = '待认领'
        })
        getUsersMapByProjectId(id, 2).then(response => {
          this.usersIdHeaderMap = response.data
          this.usersIdHeaderMap['dairenling'] = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAABGlBMVEWmpqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqampqapqamtra2zs7O6urrAwMDCwsLDw8PExMTFxcXGxsbKysrMzMzS0tLY2Nje3t7f39/i4uLk5OTm5ubp6ent7e3v7+/x8fH09PT5+fn6+vr9/f3+/v7///9sumsYAAAAQHRSTlMABAUGExQWFxkaGxwlJikqTk9QUVdYW1xmaXBxcnV2enuNjo+TlJW2t7jLzM7P0NjZ2+zt7vHy8/T19vj5+vz+SWi1rwAAAhZJREFUGBmFwQ1b0mAUBuATirYyJZMCib6WikIqqUwxt6e5aeEGCoQ4Pf//byS6C8/7buB9U8LcilnZ2Ts+3tupmCtz9IzZ0pYDwdkqzdJkL78eIuHou0HpXpQtpLLKGUqxUMNEP5Yo4e0BprCWSVOwMZVdIEURzyqSkLOh8IMwDH0Xkp2jsUULgtuJ+FHXg2AtUiyzDcGL+EkAYTtDj8oQ3CFLHoRP9MCwILRZMYBgGTSyBsGNWOVDWKd72SYEnzUhhGaWiFYh+awJIa0SURWSz5oQ0hbRnAPJZY0PyTEoD1XEKg+K92RC4Q5Z5UFhUgWKkDVdKDaoDkXImj4UdWpAEbCmA0WDoHIHrIg8qAi6PguRC9UvakATstCF5ifVoTllIYCmThXo2jw2gG6DTCQMOBadQmdSHgktjrWR8IHmHeh8joXQOQZRFbqQY13oqkS0Cl2fY5ELzUciyjahCnisA1UzS/fWoQhZ6LiQ1mnEsPDE77NiGOCJZdCDMmJu0OeEYXiK2Cd6lKlhxOtEnK7bwkgtQ7GFAyAY8BTD8Dcab2gsZ+OSp7qEnSOhCFzwFBdAkRQFG61bnuCuBbtAmuUDeD1O1fNgvaOEhRrw5x8nDP4C20uUYuZzEzi7umPh7uoMaH6ZoXSvvh0COG/3rm9ub2+ue+1zAEdrr2mybKnqQHCqpSw9Yz5vbu7un5zs726a+XnS/QfUNwZ8HxlD9AAAAABJRU5ErkJggg=='
        })
      },
      // showMsgFromChild(data) {
      //   this.flag = data
      // },
      showMsgFromChild(data) {
        if (data.flag !== undefined) {
          this.flag = data.flag
        }
        if (data.name !== undefined) {
          for (let i = 0;i < this.tasks[data.stageId].length;i++) {
            if (this.tasks[data.stageId][i].id === data.id) {
              this.tasks[data.stageId][i].name = data.name
              break
            }
          }
        }
        if (data.executorId !== undefined) {
          this.taskIdAndExecutorId[data.id] = data.executorId
        }
      },
      openDetails(row) {
        this.flag = true
        this.taskId = row.id
        // console.info(row.taskId.toString())
        // console.info(row.name.toString())
        // console.info(row.name.toString())
      },
      getTaskList() {
        this.getSwitch();
        getTaskListVoByExample(this.taskFrom).then(response => {
          // console.log(this.taskFrom)
          this.taskList = JSON.parse(response.data)
          // console.log(this.taskList)
        })
      },
      getSwitch() {
        this.taskFrom.projectId = this.projectId
        // console.info('projectview-this.projectId')
        // console.info(this.projectId)
        this.taskFrom.endTimeStr = ''
        this.taskFrom.userId = ''
        this.taskFrom.finished = ''
        if (this.sendhea === '所有任务') {
          // console.info(3);
        } else if (this.sendhea === '待认领任务') {
          // console.info(2);
          this.taskFrom.userId = 'dairenling'
        } else if (this.sendhea === '今天的任务') {
          this.taskFrom.endTimeStr = this.getNowFormatDate()
          // console.info(this.getNowFormatDate())
        } else if (this.sendhea === '我执行的任务') {
          // console.info(this.user.id);
          this.taskFrom.userId = this.user.id
        } else if (this.sendhea === '已完成任务') {
          this.taskFrom.finished = 'Y'
        } else if (this.sendhea === '未完成任务') {
          this.taskFrom.finished = 'N'
        } else {
          this.taskFrom.endTimeStr = this.getNowFormatDate()
          // console.info(this.getNowFormatDate())
        }
      },
      getNowFormatDate() {
        var date = new Date();
        var seperator1 = '-'
        var year = date.getFullYear()
        var month = date.getMonth() + 1
        var strDate = date.getDate()
        if (month >= 1 && month <= 9) {
          month = '0' + month
        }
        if (strDate >= 0 && strDate <= 9) {
          strDate = '0' + strDate
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate
        return currentdate
      }
    }
  }
</script>

<style scoped>
  .errPage-container {
    padding: 30px;
  }
  .task-img {
    border-radius: 30%;
  }
</style>

<template>
  <div class="app-container calendar-list-container" style="padding: 5px;">
    <el-row style="padding: 5px;" :gutter="10">
      <el-col :span="24">
        <el-card class="box-card">
          <template>
            <el-button type="primary" icon="el-icon-s-home" @click="toProject()" size="mini">返回</el-button>
          <el-select v-model="projectId" placeholder="请选择"  @change="toProjectList(projectId)" size="mini">
            <el-option v-for="item in projectlist" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
          </template>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="10" style="padding: 5px;">
      <el-col :span="4">
        <el-card class="box-card">
        <el-input
          placeholder="输入关键字进行过滤"
          v-model="filterText">
        </el-input>
        <el-tree
          class="filter-tree"
          style="margin-top:10px;"
          :data="treeData"
          node-key="id"
          highlight-current
          :props="defaultProps"
          :filter-node-method="filterNode"
          ref="menuTree"
          @node-click="getNodeData"
          default-expand-all>
        </el-tree>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <my-task style="padding: 0px;" v-if="taskgroupshow"></my-task>
          <project-view style="padding: 0px;" v-if="projectviewshow" v-bind:sendhea="sendmessage"></project-view>
          <project-overview style="padding: 0px;" v-if="projectoverviewshow" v-bind:sendhea="sendmessage"></project-overview>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script scoped>
  import {mapGetters} from 'vuex';
  import MyTask from './mytask/index'; // 任务分组
  import ProjectView from './projectview/index'; // 项目视图
  import ProjectOverview from './projectoverview/index'; // 项目总览
  import {getProjectsByUser} from 'api/project/index';

  export default {
    name: 'myproject',
    components: {
      MyTask, // 任务分组
      ProjectView, // 项目视图
      ProjectOverview // 项目总览
    },
    created() {
      this.getProject();
      this.mqttReveice();
      this.projectId = this.$route.params.id
    },
    data() {
      return {
        projectId: '',
        projectlist: [],
        projectviewshow: false,  // 项目视图
        taskgroupshow: true,   // 任务分组
        projectoverviewshow: false, // 项目总览
        sendmessage: '',
        filterText: '',
        treeData: [{
          id: 1,
          label: '任务分组',
          children: [{
            id: 1,
            label: '当前任务'
          }]
        }, {
          id: 2,
          label: '项目视图',
          children: [{
            id: 1,
            label: '所有任务',
            url: '&111'
          }, {
            id: 2,
            label: '待认领任务'
          }, {
            id: 3,
            label: '我执行的任务'
          }, {
            id: 4,
            label: '已完成任务'
          }, {
            id: 5,
            label: '未完成任务'
          }, {
            id: 6,
            label: '今天的任务'
          }]
        }, {
          id: 3,
          label: '项目总览',
          children: [{
            id: 1,
            label: '统计'
          }]
        }],
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      }
    },
    watch: {
      filterText(val) {
        this.$refs.menuTree.filter(val);
      }
    },
    computed: {
      ...mapGetters([
        'user',
        'client',
        'elements'
      ])
    },
    methods: {
      mqttReveice() {
        this.client.on('message', (topic, message) => {
          const msg = message.toString();
          // 删除此用户项目当前
          if (this.projectId === msg.split('/')[1] && (topic === 'project/del' || (topic === 'user/del' && msg.split('/')[2] === this.user.id))) {
            // console.log(1);
            this.$router.push({
              path: '/item/project'
            });
            this.$notify({
              title: '用户',
              message: '此项目已被移除',
              type: 'success',
              duration: 2000
            });
          } else {
            // 非当前用户
            for (var i1 = 0 ; i1 < this.projectlist.length; i1++) {
              if (msg.split('/')[1] === this.projectlist[i1].id && (topic === 'project/del' || (topic === 'user/del' && msg.split('/')[2] === this.user.id))) {
                this.getProject();
              }
            }
          }
          // 新增用户的项目
          if (topic === 'project/add' || topic === 'user/add') {
            const msg = message.toString().split('/')[2];
            for (var i = 0; i < msg.split(',').length; i++) {
              if (this.user.id === msg.split(',')[i]) {
                this.getProject();
              }
            }
          }
        });
      },
      mqttSend(topic, message) {
        // 发布消息
        if (!this.client.connected) {
          // console.log('客户端未连接')
          return
        }
        this.client.publish(topic, message, (error) => {
          console.log(error || message + '消息发布成功')
        })
      },
      toProjectList(id) {
        // console.log('toprojectlist')
        this.$router.push({
          path: '/project/' + id
        })
      },
      toProject() {
        // console.log('toproject');
        this.$router.push({
          path: '/item/project'
        })
      },
      getProject() {
        getProjectsByUser(this.user.id).then(response => {
          this.projectlist = response.data;
        })
      },
      init() {
        return {
          code: undefined,
          name: undefined,
          parentId: this.currentId,
          href: undefined,
          icon: undefined,
          component: undefined,
          levelcode: undefined,
          description: undefined,
          enabled: '1',
          type: undefined,
          parentName: this.currentName
        }
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      getNodeData(data) {
        const link_url = data.label
        if (link_url === '当前任务') {
          this.projectviewshow = false;
          this.taskgroupshow = true;
          this.projectoverviewshow = false;
        } else if (link_url === '所有任务') {
          this.projectviewshow = true;
          this.taskgroupshow = false;
          this.projectoverviewshow = false;
          this.sendmessage = link_url;
        } else if (link_url === '待认领任务') {
          this.projectviewshow = true;
          this.taskgroupshow = false;
          this.projectoverviewshow = false;
          this.sendmessage = link_url;
        } else if (link_url === '我执行的任务') {
          this.projectviewshow = true;
          this.taskgroupshow = false;
          this.projectoverviewshow = false;
          this.sendmessage = link_url;
        } else if (link_url === '已完成任务') {
          this.projectviewshow = true;
          this.taskgroupshow = false;
          this.projectoverviewshow = false;
          this.sendmessage = link_url;
        } else if (link_url === '未完成任务') {
          this.projectviewshow = true;
          this.taskgroupshow = false;
          this.projectoverviewshow = false;
          this.sendmessage = link_url;
        } else if (link_url === '今天的任务') {
          this.projectviewshow = true;
          this.taskgroupshow = false;
          this.projectoverviewshow = false;
          this.sendmessage = link_url;
        } else if (link_url === '统计') {
          this.projectviewshow = false;
          this.taskgroupshow = false;
          this.projectoverviewshow = true;
          this.sendmessage = link_url;
        }
      }
    }
  }
</script>


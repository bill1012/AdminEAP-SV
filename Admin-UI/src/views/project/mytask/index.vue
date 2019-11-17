<template>
  <div class="rootBar">
    <div :style="divwidth">
      <div v-for="row in stages" class="div-inline">
          <div class="panel panel-default" style="width: 300px">
            <div class="panel-heading">
              <h3 class="panel-title" style="color: #a6a6a6;font-size: 16px;font-weight: bold;">
                <span>{{row.name}}</span>
                <el-button-group style="float: right;" getter="0">
                <el-popover placement="top" width="160" :ref="row.id">
                  <p>确定删除该状态及以下任务吗？</p>
                  <div style="text-align: right; margin: 0">
                    <el-button size="mini" type="text" @click="deleteCancel(row.id)">取消</el-button>
                    <el-button type="primary" size="mini" @click="deleteConfirm(row.id)">确定</el-button>
                  </div>
                  <!--<el-button style="float: right; padding: 3px 0" type="text" slot="reference">删除</el-button>-->
                  <el-button type="primary" icon="el-icon-delete" slot="reference" size="mini" style="padding: 0px ;margin: 3px"></el-button>
                </el-popover>
                <!--<el-button style="float: right; padding: 3px 0" type="text">修改</el-button>-->
                <el-popover placement="top" width="280" trigger="click" :ref="'popover' + row.id">
                  <el-form>
                    <el-form-item align="center">
                      <span>编辑名称</span>
                    </el-form-item>
                    <el-form-item>
                      <el-input placeholder="状态名称" v-model.trim="row.name"> </el-input>
                    </el-form-item>
                    <el-form-item align="center">
                      <el-button @click="updateStageState(row.id,row.name)" type="primary">保存</el-button>
                    </el-form-item>
                  </el-form>
                  <el-button type="primary" icon="el-icon-edit" slot="reference" size="mini" style="padding: 0px ;margin: 3px"></el-button>
                  <!--<el-button style="float: right; padding: 3px 0" type="text" slot="reference">修改</el-button>-->
                </el-popover>
                </el-button-group>
              </h3>
            </div>
            <div class="panel-body">
              <draggable class="list-group" :id="row.id" :list="tasks[row.id]" group="people"  tag="ul" v-bind="dragOptions" @end="onEnd">
                <div class="list-group-item" v-for="(element,index) in tasks[row.id]" :id="element.id" :key="index">
                  <span>
                    <el-checkbox disabled v-model="element.finished === 'Y'"/>
                  </span>
                  <span style="float: right">
                    <el-popover placement="top" width="160" :ref="'task' + element.id">
                      <p>确定删除该任务吗？</p>
                      <div style="text-align: right; margin: 0">
                        <el-button size="mini" type="text" @click="deleteTaskCancel(element.id)">取消</el-button>
                        <el-button type="primary" size="mini" @click="deleteTaskConfirm(row.id,element.id)">确定</el-button>
                      </div>
                      <!--<i class="el-icon-delete" slot="reference"></i>-->
                      <el-button type="primary" icon="el-icon-delete" slot="reference" size="mini" style="padding: 0px ;margin: 3px"></el-button>
                    </el-popover>
                  </span>
                  <span @click="handleUpdate(element.id)">
                    <span :title="element.name">
                      {{ formatTaskName(element.name) }}
                    </span>
                    <span style="float: right">
                      <img :src="usersIdHeaderMap[taskIdAndExecutorId[element.id]]" style="width: 30px ;height: 30px ;display: block ;border-radius: 50%;">
                    </span>
                  </span>
                </div>

              </draggable>
            </div>
            <div class="panel-footer">
              <div style="text-align:center">
                <el-button class="filter-item" icon="el-icon-plus" title="新建任务"
                           style="margin-left:10px;"  @click="handleCreate(row.id)" size="mini">
                </el-button>
              </div>

            </div>
          </div>
      </div>

      <div class="div-inline">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h3 class="panel-title">
                <el-popover placement="top" width="280" trigger="click" ref="popover">
                  <el-form>
                    <el-form-item align="center">
                      <span>添加状态</span>
                    </el-form-item>
                    <el-form-item>
                      <el-input @input="stageStateBlur" placeholder="状态名称" v-model.trim="stageStateName"> </el-input>
                    </el-form-item>
                    <el-form-item align="center">
                      <el-button @click="createStageState" type="primary" :disabled="creatStageStatuButtonFlag">添加</el-button>
                    </el-form-item>
                  </el-form>
                  <span slot="reference">+添加状态</span>
<!--                  <el-button slot="reference">添加状态</el-button>-->
                </el-popover>
              </h3>
            </div>
          </div>
      </div>
    </div>

    <!-- 新建任务框 -->
    <el-dialog  :visible.sync="dialogFormVisible" :before-close="handleClose">
      <el-row style="height: 30px;">
        <el-col :span="22" >
          <el-breadcrumb separator-class="el-icon-arrow-right"  style="bottom:0px; left:0px;position:absolute;">
            <el-breadcrumb-item :to="{ path: '/' }"><i class="el-icon-notebook-2" />任务</el-breadcrumb-item>
            <el-breadcrumb-item>E8助手</el-breadcrumb-item>
            <el-breadcrumb-item>任务</el-breadcrumb-item>
            <el-breadcrumb-item>创建任务</el-breadcrumb-item>
          </el-breadcrumb>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <el-form :model="data" ref="form" label-width="80px">
        <el-form-item prop="name">
          <el-input type="textarea" placeholder="任务标题" v-model.trim="data.name" @input="changeCreatButton"></el-input>
        </el-form-item>
        <el-form-item v-show="finishedStatus">
          <el-col :span="5">
            <span class="fontclass">状态</span>
          </el-col>
          <el-col :span="5">
            <el-form-item>
              <el-select v-model="data.finished" placeholder="请选择">
                <el-option v-for="(item,index) in taskStatus" :key="index" :label="item.name" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="4">
            <i class="el-icon-s-custom"></i>&nbsp;&nbsp;<span class="fontclass">执行人</span>
          </el-col>
          <el-col :span="4">
            <!--<span @mouseover="changeFontColor1" @mouseout="changeFontColor2" @click="getTask" style="cursor:pointer;color: #a6a6a6" ref="fontChange">待领取</span>-->
            <el-select v-model="data.executorId" filterable placeholder="请选择">
              <el-option v-for="(item,index) in users" :key="index" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>

            <el-form-item>
              <el-col :span="4">
                <i class="el-icon-alarm-clock"></i>&nbsp;&nbsp;<span class="fontclass">时&nbsp;&nbsp;间</span>
              </el-col>
              <el-col :span="5">
            <el-form-item prop="startTime">
              <el-date-picker @change="formatStartTime" v-model="data.startTime" type="datetime" placeholder="设置开始时间" format="yyyy-MM-dd HH:mm" value-format="yyyy-MM-dd HH:mm" style="width: 100%;">
              </el-date-picker>
            </el-form-item>
              </el-col>

          <el-col class="line" :span="1" align="center">-</el-col>
              <el-col :span="5">
            <el-form-item prop="endTime">
              <el-date-picker @change="formatEndTime" v-model="data.endTime" type="datetime" placeholder="设置结束时间" format="yyyy-MM-dd HH:mm" value-format="yyyy-MM-dd HH:mm" style="width: 100%;">
              </el-date-picker>
            </el-form-item>
              </el-col>
            </el-form-item>

        <el-form-item>
          <el-col :span="4">
            <i class="el-icon-finished"></i>&nbsp;&nbsp; <span class="fontclass">优先级</span>
          </el-col>
          <el-col :span="5">
            <el-form-item prop="remark">
              <el-select v-model="data.dictPriority" placeholder="请选择">
                <el-option v-for="(item,index) in options" :key="index" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="4">
            <i class="el-icon-user"></i>&nbsp;&nbsp; <span class="fontclass">参与者</span>
          </el-col>
          <el-col :span="18">
            <el-select v-model="data.participatorIds" multiple filterable default-first-option placeholder="请选择">
              <el-option v-for="(item,index) in partUser" :key="index" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
            <el-form-item>
              <el-col :span="4">
                <i class="el-icon-edit-outline"></i>&nbsp;&nbsp;<span class="fontclass">备&nbsp;&nbsp;注</span>
              </el-col>
              <el-col :span="18">
                <el-form-item prop="remark">
                  <el-input type="textarea" placeholder="添加备注" v-model="data.remark"></el-input>
                </el-form-item>
              </el-col>
            </el-form-item>
      </el-form>
        </el-col>
      </el-row>
      <!--<el-row>-->
        <!--<el-col>-->
          <div slot="footer" class="dialog-footer">
            <el-button @click="handleClose">取 消</el-button>
            <el-button v-if="dialogStatus=='create'" type="primary" @click="create" :disabled="creatTaskButtonFlag">创 建</el-button>
            <el-button v-else type="primary" @click="update">更 新</el-button>
          </div>
        <!--</el-col>-->
      <!--</el-row>-->
    </el-dialog>
    <edit :flag="flag" :taskId="taskId" :usersMap="usersMap" :userId="data.createUser" v-on:listenToChildEvent="showMsgFromChild"/>
  </div>
</template>

<script>
  import { getObjsByCode } from 'api/admin/dict/index'
  import { getUsersByProjectId, getUsersMapByProjectId, getTaskIdExecutorId } from 'api/admin/user/index'
  import { addObj, getTasksByProjectId, updateTask, deleteTask, updateDragTasks } from 'api/project/task/index'
  import { parseTime } from 'utils/index'
  import { getStagesByProjectId, addStage, deleteStage, updateStage } from 'api/project/stage/index'
  // import { add } from 'api/project/record/index'
  import draggable from 'vuedraggable'
  import 'bootstrap/dist/css/bootstrap.css'
  import { mapGetters } from 'vuex'
  import edit from './module/edit'

  export default {
    created() {
      this.mqttReveice();
      this.data.projectId = this.$route.params.id
      this.data.createUser = this.user.id
      this.getStagesByProjectId(this.data.projectId)
      this.getTasksByProjectId(this.data.projectId)
      this.getDictsByCode('task_priority')
      this.getUsersByProject(this.data.projectId)
      this.getPartUserByProject(this.data.projectId)
      // 当前项目下所有的任务id对应的执行人id
      this.getTaskIdExecutorId(this.data.projectId)
      // 参与人默认当前创建人
      this.data.participatorIds.push(this.user.id)
    },
    name: 'mytask',
    components: {
      draggable,
      edit
    },
    data() {
      return {
        flag: false,
        taskId: '',
        dialogStatus: '',
        textMap: {
          update: '更新任务',
          create: '新建任务'
        },
        finishedStatus: false,
        // 任务状态
        taskStatus: [
          {
            name: '未完成',
            value: 'N'
          },
          {
            name: '已完成',
            value: 'Y'
          }
        ],
        stages: [],
        tasks: '',
        taskIdAndExecutorId: [],
        title: '创建任务',
        dialogFormVisible: false,
        // data: this.initObj(),
        data: {
          id: null,
          projectId: '',
          name: '',
          executorId: 'dairenling',
          participatorIds: [],
          stageId: '',
          parentId: '',
          deleted: 'N',
          finished: 'N',
          finishedUserId: '',
          startTime: '',
          endTime: '',
          remark: '',
          dictPriority: '',
          createUser: ''
        },
        usersMap: {},
        usersIdHeaderMap: {},
        // 待认领选项
        users: [],
        // 优先级选项
        options: [],
        // 参与人选项
        partUser: [],
        editable: true,
        delayedDragging: false,
        // stage相关
        stageStateName: '',
        divwidth: '',
        divwidthvulue: '',
        creatTaskButtonFlag: true,
        creatStageStatuButtonFlag: true
      }
    },
    methods: {
      formatTaskName(name) {
        if (name.length < 11) {
          return name
        } else {
          return name.substring(0, 10) + '...'
        }
      },
      mqttReveice() {
        this.client.on('message', (topic, message) => {
          const msg = message.toString();
          // console.log('topic:', topic, 'message: ', msg)
          if ((topic === 'user/del' || topic === 'user/add') && msg.split('/')[1] === this.data.projectId) {
            // 添加或删除
            for (var w = 0; w < msg.split('/')[2].split(','); w++) {
              if (msg.split('/')[2].split(',')[w] !== this.user.id) {
                this.getUsersByProject(this.data.projectId);
                this.getPartUserByProject(this.data.projectId);
              }
            }
          }
          // console.log('topic:', topic, 'message: ', msg)
          if (msg.split('/')[0] === this.data.projectId) {
            if (topic.split('/')[0] === 'stage') {
              this.getStagesByProjectId(this.data.projectId);
            }
            if (topic.split('/')[0] === 'task') {
              this.getTasksByProjectId(this.data.projectId);
            }
          }
        });
      },
      orderList() {
        this.list = this.list.sort((one, two) => {
          return one.order - two.order;
        });
      },
      onEnd(evt) {
        // 拖拽到列表之前为null
        if (this.tasks[evt.to.id].length === 1) {
          updateDragTasks({stageId: evt.to.id, taskId: this.tasks[evt.to.id][evt.newIndex].id, flag: '0'})
          // 当前元素是否拖拽到列表的最后一个
        } else if (this.tasks[evt.to.id].length === (evt.newIndex + 1)) {
          // console.log(2)
          updateDragTasks({stageId: evt.to.id, taskId: this.tasks[evt.to.id][evt.newIndex].id, lastSort: this.tasks[evt.to.id][evt.newIndex - 1].sort, flag: '1'})
        } else {
          // console.log(3)
          updateDragTasks({stageId: evt.to.id, taskId: this.tasks[evt.to.id][evt.newIndex].id, nextSort: this.tasks[evt.to.id][evt.newIndex + 1].sort, flag: '2'})
        }
        this.mqttSend('task/update', this.data.projectId);
      },
      handleCreate(stageId) {
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.data.stageId = stageId
      },
      handleUpdate(taskId) {
        /* getTaskById(taskId).then(response => {
          this.data = response.data
          // console.log(response.data)
        })
        this.finishedStatus = true
        this.dialogStatus = 'update'
        // this.dialogFormVisible = true
        this.flag = true
        this.taskId = taskId*/
        this.flag = true
        this.taskId = taskId
      },
      handleClose() {
        this.cancel('form')
        this.creatTaskButtonFlag = true
      },
      cancel(formName) {
        this.dialogFormVisible = false
        this.data.id = null
        this.finishedStatus = false
        this.$refs[formName].resetFields()
        this.data.executorId = 'dairenling'
        // 优先级默认普通
        this.data.dictPriority = this.options[0].id
        // 参与者默认为当前登录人
        this.data.participatorIds = [this.user.id]
      },
      changeFontColor1() {
        this.$refs.fontChange.style.color = '#6495ED'
      },
      changeFontColor2() {
        this.$refs.fontChange.style.color = '#a6a6a6'
      },
      getDictsByCode(code) {
        getObjsByCode(code).then(response => {
          this.options = response.data
          // 优先级默认普通
          this.data.dictPriority = this.options[0].id
        })
      },
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
      getPartUserByProject(id) {
        getUsersByProjectId(id).then(response => {
          this.partUser = response.data
        })
      },
      getStagesByProjectId(id) {
        getStagesByProjectId(id).then(response => {
          this.stages = response.data
          this.divwidthvulue = (response.data.length + 1) * 320
          this.divwidth = 'width: ' + this.divwidthvulue + 'px'
        })
      },
      getTasksByProjectId(id) {
        getTasksByProjectId(id).then(response => {
          this.tasks = response.data
        })
      },
      // 任务对应的执行人id---projectId
      getTaskIdExecutorId(id) {
        getTaskIdExecutorId(id).then(response => {
          this.taskIdAndExecutorId = response.data
        })
      },
      create() {
        addObj(this.data).then(response => {
          this.tasks[this.data.stageId].push({
            id: response.data.id,
            name: this.data.name
          })
          this.taskIdAndExecutorId[response.data.id] = this.data.executorId
          this.cancel('form')
          this.dialogFormVisible = false
          this.$notify({
            title: '成功',
            message: '创建成功',
            type: 'success',
            duration: 2000
          })
          this.mqttSend('task/add', this.data.projectId)
        })
      },
      // 已经不用了
      update() {
        updateTask(this.data).then(() => {
          this.dialogFormVisible = false
          this.$notify({
            title: '成功',
            message: '更新成功',
            type: 'success',
            duration: 2000
          })
        })
      },
      formatStartTime(time) {
        this.data.startTime = parseTime(time)
      },
      formatEndTime(time) {
        this.data.endTime = parseTime(time)
      },
      createStageState() {
        addStage({
          projectId: this.data.projectId,
          name: this.stageStateName
        }).then(response => {
          this.stages.push({id: response.data.id, name: this.stageStateName})
          this.tasks[response.data.id] = []
        })
        this.$refs.popover.doClose()
        this.divwidth = 'width: ' + this.divwidthvulue + 320 + 'px'
        this.mqttSend('stage/add', this.data.projectId)
      },
      deleteCancel(id) {
        this.$refs[id][0].doClose()
      },
      deleteConfirm(id) {
        deleteStage(id).then(() => {
          this.$refs[id][0].doClose()
          for (let i = 0; i < this.stages.length; i++) {
            if (this.stages[i].id === id) {
              this.stages.splice(i, 1)
              break
            }
          }
        })
        this.mqttSend('stage/del', this.data.projectId)
      },
      updateStageState(id, name) {
        updateStage({id: id, name: name}).then(() => {
          this.$refs[`popover` + id][0].doClose()
        })
        this.mqttSend('stage/update', this.data.projectId)
      },
      showMsgFromChild(data) {
        if (data.flag !== undefined) {
          this.flag = data.flag
        }
        if (data.finished !== undefined) {
          for (let i = 0;i < this.tasks[data.stageId].length;i++) {
            if (this.tasks[data.stageId][i].id === data.id) {
              this.tasks[data.stageId][i].finished = data.finished
              break
            }
          }
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
      changeCreatButton() {
        if (this.data.name.length !== 0) {
          this.creatTaskButtonFlag = false
        } else {
          this.creatTaskButtonFlag = true
        }
      },
      stageStateBlur() {
        if (this.stageStateName.length !== 0) {
          this.creatStageStatuButtonFlag = false
        } else {
          this.creatStageStatuButtonFlag = true
        }
      },
      deleteTaskCancel(taskId) {
        this.$refs[`task` + taskId][0].doClose()
      },
      deleteTaskConfirm(stageId, taskId) {
        deleteTask(taskId).then(() => {
          this.$refs[`task` + taskId][0].doClose()
          for (let i = 0; i < this.tasks[stageId].length; i++) {
            if (this.tasks[stageId][i].id === taskId) {
              this.tasks[stageId].splice(i, 1)
              break
            }
          }
        })
        this.mqttSend('task/del', this.data.projectId + '/' + taskId);
      },
      mqttSend(topic, message) {
        // 发布消息
        if (!this.client.connected) {
          // console.log('客户端未连接')
          return
        }
        this.client.publish(topic, message, (error) => {
          console.log(error || message + '消息发布成功' + topic)
        })
      }
    },
    computed: {
      ...mapGetters([
        'user',
        'client'
      ]),
      dragOptions() {
        return {
          animation: 0,
          group: 'description',
          disabled: !this.editable,
          ghostClass: 'ghost'
        };
      }
    }
  };
</script>

<style>
  .flip-list-move {
    transition: transform 0.5s;
  }

  .no-move {
    transition: transform 0s;
  }

  .ghost {
    opacity: 0.5;
    background: #c8ebfb;
  }

  .list-group {
    min-height: 20px;
  }

  .list-group-item {
    cursor: move;
  }

  .list-group-item i {
    cursor: pointer;
  }

  .div-inline{
    width: 320px;
    float: left;
    display: inline-block;
  }
  .rootBar{
    overflow-y: hidden;
    overflow-x: auto;
    white-space: nowrap;
    height: 75vh;
  }
  .header{
    color: #a6a6a6;
    font-size: 16px;
    font-weight: bold;
  }
</style>

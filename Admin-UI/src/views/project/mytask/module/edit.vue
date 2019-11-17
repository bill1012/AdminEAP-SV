<template>
  <div>
    <el-dialog width="75%" :visible.sync="flag" @open="open" :before-close="cancel">
      <el-row style="height: 30px;">
        <el-col :span="8" >
          <el-breadcrumb separator-class="el-icon-arrow-right"  style="bottom:0px; left:0px; position:absolute;">
            <el-breadcrumb-item :to="{ path: '/project/' + this.$route.params.id}"><i class="el-icon-notebook-2" />任务</el-breadcrumb-item>
            <el-breadcrumb-item>{{this.projectName}}</el-breadcrumb-item>
            <el-breadcrumb-item>任务</el-breadcrumb-item>
            <el-breadcrumb-item>{{this.stageName}}</el-breadcrumb-item>
          </el-breadcrumb>
        </el-col>
        <el-col :span="24">
          <div><el-button class="el-icon-close" type="text" style="bottom:0px; right:0px; position:absolute;" @click="deleteTaskCancel()"></el-button></div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="13">
          <el-form :model="data" label-width="20px">
            <el-form-item prop="name">
              <!--<effect-input @focus="titleFocus" @blur="titleBlur" v-model="data.name" type="ichiro" label="任务标题"></effect-input>-->
              <span v-show='flagInput'><span @click='edit()'><div style="color: #a6a6a6;font-size: 20px; font-weight: bold;">{{data.name}}</div></span></span>
              <el-input v-show='!flagInput' @change='input()' ref="inputVal" @focus="titleFocus" @blur="titleBlur" type="text" placeholder="任务标题" v-model.trim="data.name" style="background-color:transparent;border:0;"></el-input>
            </el-form-item>
            <el-form-item>
              <el-col :span="4">
                <i class="el-icon-circle-plus-outline"></i>&nbsp;&nbsp;<span class="fontclass">状&nbsp; &nbsp; 态</span>
              </el-col>
              <el-col :span="4">
                <el-form-item>
                  <el-select @change="finishChange" v-model="data.finished" placeholder="请选择">
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
                <el-select @change="executorChange" v-model="data.executorId" filterable placeholder="请选择">
                  <el-option v-for="(item,index) in users" :key="index" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-col :span="4">
                <i class="el-icon-alarm-clock"></i>&nbsp;&nbsp;<span class="fontclass">时&nbsp;&nbsp;间</span>
              </el-col>
              <el-col :span="6">
                <el-form-item>
                  <el-date-picker @change="startTimeChange" v-model="data.startTime" type="datetime" :clearable="clearableFlag" placeholder="设置开始时间" format="yyyy-MM-dd HH:mm" value-format="yyyy-MM-dd HH:mm" style="width: 100%;">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col class="line" :span="1" align="center">-</el-col>
              <el-col :span="6">
                <el-form-item>
                  <el-date-picker @change="endTimeChange" v-model="data.endTime" type="datetime" :clearable="clearableFlag" placeholder="设置结束时间" format="yyyy-MM-dd HH:mm" value-format="yyyy-MM-dd HH:mm" style="width: 100%;">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-form-item>

            <el-form-item>
              <el-col :span="4">
                <i class="el-icon-finished"></i>&nbsp;&nbsp; <span class="fontclass">优先级</span>
              </el-col>
              <el-col :span="4">
                <el-form-item>
                  <el-select @change="priorityChange" v-model="data.dictPriority" placeholder="请选择">
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
                <el-select @change="participatorsChange" v-model="data.participatorIds" multiple filterable default-first-option placeholder="请选择">
                  <el-option v-for="(item,index) in partUser" :key="index" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-col :span="4">
                <i class="el-icon-edit-outline"></i>&nbsp;&nbsp;<span class="fontclass">备&nbsp;&nbsp;注</span>
              </el-col>
              <el-col :span="16">
                <el-form-item prop="remark">
                  <el-input @focus="remarkFocus" @blur="remarkBlur" placeholder="添加备注" v-model="data.remark" type="textarea" :rows="2"></el-input>
                </el-form-item>
              </el-col>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="1">
          <!--<div style="width: 30px; height: 600px; position:relative;">-->
            <!--<div style="width: 2px; height: 600px; border: #787a80;  top: 0; left: 0; bottom: 0; right: 0;" ></div>-->
          <!--</div>-->
          <div style="width:30px;height:600px; position:relative;">
            <div style="width:1px;height:550px;margin: auto;  position: absolute;  top: 0; left: 0; bottom: 0; right: 0; background: #DCDFE6;"></div>
          </div>
        </el-col>
        <el-col :span="10">
          <el-form>
            <el-form-item>
              <h4>
                所有动态
              </h4>
            </el-form-item>
            <div style="height: 90%">
              <el-scrollbar wrap-class="list">
                <el-form-item v-for="(row,index) in records" :key="index">
                  <span style="float: left;color: #a6a6a6">{{usersMap[row.userId]}}</span>
                  &nbsp;&nbsp;&nbsp;
                  <span style="color: #a6a6a6">{{row.content}}</span>
                  <span style="float: right;color: #a6a6a6">{{row.createTime}}</span>
                </el-form-item>
              </el-scrollbar>
            </div>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex';
  import { getTaskById, updateTask } from 'api/project/task/index'
  import { getProjectById } from 'api/project/index'
  import { getStageById } from 'api/project/stage/index'
  import { getUsersByProjectId } from 'api/admin/user/index'
  import { getRecordsByTaskId, add } from 'api/project/record/index'
  import { getObjsByCode, getObj } from 'api/admin/dict/index'
  import { parseTime } from 'utils/index'

  export default {
    components: {
      // EffectInput
    },
    computed: {
      ...mapGetters([
        'client',
        'user'
      ])
    },
    created() {
      this.mqttReveice();
    },
    props: {
      flag: {
        type: Boolean,
        required: true
      },
      taskId: {
        type: String,
        required: true
      },
      usersMap: {
        type: Object,
        required: true
      },
      userId: {
        type: String,
        required: true
      }
    },
    data() {
      return {
        projectName: '',
        stageName: '',
        flagInput: true,
        content: '',
        dialog: false,
        options: [],
        partUser: [],
        users: [],
        data: {
          id: '',
          projectId: '',
          name: '',
          executorId: '',
          participatorIds: [],
          stageId: '',
          parentId: '',
          deleted: '',
          finished: '',
          finishedUserId: '',
          startTime: '',
          endTime: '',
          remark: '',
          dictPriority: ''
        },
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
        records: [],
        record: {
          taskId: '',
          content: '',
          userId: this.userId,
          createTime: parseTime(new Date())
        },
        endTimeFlag: false,
        clearableFlag: false
      }
    },
    methods: {
      mqttReveice() {
        this.client.on('message', (topic, message) => {
          const msg = message.toString();
          // console.log('topic:', topic, 'message: ', msg)
          if (msg.split('/')[1] === this.$route.params.id && (topic === 'user/del' || topic === 'user/add')) {
            this.open();
          }
          if (topic === 'task/del' && msg.split('/')[1] === this.taskId) {
            this.cancel();
          }
        });
      },
      edit() {
        this.flagInput = false;
        this.$nextTick(function() {
          // DOM 更新了
          this.$refs.inputVal.focus()
        })
      },
      input() {
        this.flagInput = true;
      },
      async open() {
        await getTaskById(this.taskId).then(response => {
         /* this.data.id = response.data.id
          this.data.projectId = response.data.projectId
          this.data.name = response.data.name
          this.data.executorId = response.data.executorId
          this.data.participatorIds = response.data.participatorIds
          this.data.stageId = response.data.stageId
          this.data.parentId = response.data.parentId
          this.data.deleted = response.data.deleted
          this.data.finished = response.data.finished
          this.data.finishedUserId = response.data.finishedUserId
          if (response.data.startTime !== undefined) {
            this.data.startTime = response.data.startTime
          } else {
            this.startTimeFlag = true
          }
          if (response.data.endTime !== undefined) {
            this.data.endTime = response.data.endTime
          } else {
            this.endTimeFlag = true
          }
          this.data.remark = response.data.remark
          this.data.dictPriority = response.data.dictPriority*/
          this.data = response.data
          // console.info(this.data)
        })
        getProjectById(this.$route.params.id).then(response => {
          this.projectName = response.data.name
        })
        getUsersByProjectId(this.$route.params.id).then(response => {
          this.users = response.data
          this.users.push({id: 'dairenling', name: '待认领'})
          this.users.reverse()
        })
        getObjsByCode('task_priority').then(response => {
          this.options = response.data
        })
        getUsersByProjectId(this.$route.params.id).then(response => {
          this.partUser = response.data
        })
        getRecordsByTaskId(this.taskId).then(response => {
          this.records = response.data
        })
        getStageById(this.data.stageId).then(response => {
          this.stageName = response.data.name
        })
        this.record.taskId = this.taskId
      },
      cancel() {
        this.initObj()
        this.$emit('listenToChildEvent', { 'flag': false})
        this.endTimeFlag = false
      },
      titleFocus() {
        // this.flagInput = true
        // console.info(2222)
        this.content = this.data.name
      },
      titleBlur() {
        // console.info(1111)
        if (this.data.name.length === 0) {
          this.data.name = this.content
        } else {
          if (this.content !== this.data.name) {
            updateTask(this.data).then(() => {
              this.$emit('listenToChildEvent', { 'id': this.data.id, 'stageId': this.data.stageId, 'name': this.data.name})
              this.record.content = '更新了任务内容:' + this.data.name
              add(this.record).then(() => {
                this.records.push(JSON.parse(JSON.stringify(this.record)))
              })
            })
          }
        }
        // console.info(1111)
        this.flagInput = true
      },
      finishChange() {
        if (this.data.finished === 'Y') {
          this.record.content = '完成了任务'
          this.data.finishedUserId = this.userId
        } else {
          this.record.content = '重做了任务'
          this.data.finishedUserId = null
        }
        updateTask(this.data).then(() => {
          this.$emit('listenToChildEvent', { 'id': this.data.id, 'stageId': this.data.stageId, 'finished': this.data.finished})
          add(this.record).then(() => {
            this.records.push(JSON.parse(JSON.stringify(this.record)))
          })
        })
      },
      executorChange() {
        updateTask(this.data).then(() => {
          this.$emit('listenToChildEvent', { 'id': this.data.id, 'executorId': this.data.executorId})
          this.record.content = '将任务指派给了' + this.usersMap[this.data.executorId]
          add(this.record).then(() => {
            this.records.push(JSON.parse(JSON.stringify(this.record)))
          })
        })
      },
      startTimeChange() {
        this.data.startTime = parseTime(this.data.startTime)
        updateTask(this.data).then(() => {
          this.record.content = '更新开始时间为' + (this.data.startTime).substring(0, 10)
          add(this.record).then(() => {
            this.records.push(JSON.parse(JSON.stringify(this.record)))
          })
        })
      },
      endTimeChange() {
        this.data.endTime = parseTime(this.data.endTime)
        updateTask(this.data).then(() => {
          this.record.content = '更新结束时间为' + (this.data.endTime).substr(0, 10)
          add(this.record).then(() => {
            this.records.push(JSON.parse(JSON.stringify(this.record)))
          })
        })
      },
      remarkFocus() {
        this.content = this.data.remark
      },
      remarkBlur() {
        if (this.content !== this.data.remark) {
          updateTask(this.data).then(() => {
            this.record.content = '更新了任务备注:' + this.data.remark
            add(this.record).then(() => {
              this.records.push(JSON.parse(JSON.stringify(this.record)))
            })
          })
        }
      },
      priorityChange() {
        updateTask(this.data).then(() => {
          getObj(this.data.dictPriority).then(response => {
            this.record.content = '更新任务优先级为' + response.data.name
            add(this.record).then(() => {
              this.records.push(JSON.parse(JSON.stringify(this.record)))
            })
          })
        })
      },
      deleteTaskCancel() {
        this.cancel('form')
      },
      participatorsChange() {
        if (this.data.participatorIds.length === 0) {
          this.record.content = '移除了所有参与人'
        } else {
          let names = ''
          for (let i = 0; i < this.data.participatorIds.length; i++) {
            names = names + ',' + this.usersMap[this.data.participatorIds[i]]
          }
          names = names.substring(1, names.length)
          this.record.content = '将参与者修改为' + names + this.data.participatorIds.length + '人'
        }
        updateTask(this.data).then(() => {
          add(this.record).then(() => {
            this.records.push(JSON.parse(JSON.stringify(this.record)))
          })
        })
      },
      initObj() {
        this.data.id = ''
        this.data.projectId = ''
        this.data.name = ''
        this.data.executorId = ''
        this.data.participatorIds = []
        this.data.stageId = ''
        this.data.parentId = ''
        this.data.deleted = ''
        this.data.finished = ''
        this.data.finishedUserId = ''
        this.data.startTime = ''
        this.data.endTime = ''
        this.data.remark = ''
        this.data.dictPriority = ''
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
    }
  }
</script>
<style>
  .dialog{
    width:60%;
    height: 500px;
  }
  .list {
    max-height: 400px;
  }
  .el-dialog {
     min-height:700px;
  }
  .dialog-footer {
    text-align: center;
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
  /*@font-face {*/
    /*font-family: "AncientWar";*/
    /*src: url('style/css/fonts/AncientWar.ttf');*/
  /*}*/
</style>

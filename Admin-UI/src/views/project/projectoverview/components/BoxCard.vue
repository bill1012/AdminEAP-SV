<template>
    <div style="position:relative;">
      <el-row style="padding-top:15px;">
        <el-col :span="12">
          <div  class="progress-item">
            <div class="card-panel-description">
              <div class="card-panel-text">
                任务总数
              </div>
              <count-to :start-val="0" :end-val="allTaskCount"  class="card-panel-num" />
            </div>
            <el-progress :percentage="100" />
          </div>
        </el-col>
        <el-col :span="12">
          <div class="progress-item">
            <div class="card-panel-description">
              <div class="card-panel-text">
                待认领
              </div>
              <count-to :start-val="0" :end-val="dairenlingCount"  class="card-panel-num" />
            </div>
            <el-progress :percentage = dairenlingPercentage color="#e6a23c"/>
          </div>
        </el-col>
      </el-row>
      <el-row style="padding-top:55px;">
      <el-col :span="12">
        <div class="progress-item">
          <!--<div><span>已完成</span></div>-->
          <!--<span>3</span>-->
          <div class="card-panel-description">
            <div class="card-panel-text">
              已完成
            </div>
            <count-to :start-val="0" :end-val="finishedCount"  class="card-panel-num" />
          </div>
          <el-progress :percentage = finishedPercentage color="#6f7ad3"/>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="progress-item">
          <div class="card-panel-description">
            <div class="card-panel-text">
              未完成
            </div>
            <count-to :start-val="0" :end-val="unFinishedCount"  class="card-panel-num" />
          </div>
          <el-progress :percentage = unFinishedPercentage  color="#ba89fa"/>
        </div>
      </el-col>
      </el-row>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import CountTo from 'vue-count-to'
import { getCountDayTaskByProjectIdTimes, getCountDairenlingByProjectId, getCountFinishedByProjectId, getCountUnFinishedByProjectId } from 'api/project/task/index'

export default {
  components: {
    CountTo
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        success: 'success',
        pending: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      allTaskCount: 0,
      dairenlingCount: 0,
      finishedCount: 0,
      unFinishedCount: 0,
      dairenlingPercentage: 0,
      finishedPercentage: 0,
      unFinishedPercentage: 0,
      LineDataFrom: {
        projectId: '',
        times: ''
      }
    }
  },
  created() {
    this.projectId = this.$route.params.id
    this.getAllTaskCount()
  },
  methods: {
    async getAllTaskCount() {
      this.LineDataFrom.projectId = this.projectId
      this.LineDataFrom.times = this.getNowFormatDate()
      await getCountDayTaskByProjectIdTimes(this.LineDataFrom).then(response => {
        this.allTaskCount = response.data
      })
      await getCountDairenlingByProjectId(this.projectId).then(response1 => {
        this.dairenlingCount = response1.data
      })
      await getCountFinishedByProjectId(this.projectId).then(response2 => {
        this.finishedCount = response2.data
      })
      await getCountUnFinishedByProjectId(this.projectId).then(response2 => {
        this.unFinishedCount = response2.data
      })
      this.dairenlingPercentage = parseFloat((this.dairenlingCount * 100 / this.allTaskCount).toFixed(1))
      this.finishedPercentage = parseFloat((this.finishedCount * 100 / this.allTaskCount).toFixed(1))
      this.unFinishedPercentage = parseFloat((this.unFinishedCount * 100 / this.allTaskCount).toFixed(1))
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
  },
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'roles'
    ])
  }
}
</script>

<style lang="scss" >
.box-card-component{
  .el-card__header {
    padding: 0px!important;
  }
}
</style>
<style lang="scss" scoped>
.box-card-component {
  .box-card-header {
    position: relative;
    height: 220px;
    img {
      width: 100%;
      height: 100%;
      transition: all 0.2s linear;
      &:hover {
        transform: scale(1.1, 1.1);
        filter: contrast(130%);
      }
    }
  }
  .progress-item {
    margin-bottom: 10px;
    font-size: 18px;
  }
}
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>

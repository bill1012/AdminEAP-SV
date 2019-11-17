<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { getCountDayCompTaskByProjectId } from 'api/project/task/index'
import { getCountDayCompTaskByProjectIdTimes } from 'api/project/task/index'
import { getCountDayTaskByProjectIdTimes } from 'api/project/task/index'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '320px'
    },
    autoResize: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      day: 7,
      projectId: '',
      chart: null,
      nowTime: null,
      youTime: null,
      taskCount: 0,
      TimeList: [],
      // taskCountList: [],
      data1: [],
      data2: [],
      dateTime: {
        currentDate: '',
        currentTime: '',
        currentWeek: ''
      },
      LineDataFrom: {
        projectId: '',
        times: ''
      }
    }
  },
  created() {
    this.projectId = this.$route.params.id
    this.getTime(this.day);
    // console.info('this.projectId')
    // console.info(this.projectId)
    this.getCountDayCompTaskByProjectId(this.projectId)
  },
  mounted() {
    setTimeout(() => {
      this.$nextTick(() => {
        // console.info(this.data1)
        // console.info(this.data2)
        // console.info(this.TimeList)
        this.initChart()
      })
    }, 400)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    async getCountDayCompTaskByProjectId(id) {
      // console.info(id)
      let flag = false
      let CountTask = []
      await getCountDayCompTaskByProjectId(id).then(response => {
        CountTask = response.data
        // console.info('CountTask')
        // console.info(CountTask)
      })
      // console.info('this.TimeList')
      // console.info(this.TimeList)
      for (let i = 0; i < this.TimeList.length; i++) {
        flag = false
        CountTask.forEach((item, index) => {
          // console.info('item.value')
          // console.info(item.name, this.TimeList[i])
          if (this.TimeList[i] === item.name) {
            this.data2.push({time: this.TimeList[i], value: this.taskCount - item.value})
            flag = true
          }
        })
        if (flag) {
          continue
        }
        if (flag === false && i !== 0) {
          // console.info('this.data2[ i - 1 ].value')
          // console.info(this.data2[ i - 1 ].value)
          this.data2.push({time: this.TimeList[i], value: this.data2[ i - 1 ].value})
        }
        if (flag === false && i === 0) {
          this.LineDataFrom.projectId = id
          this.LineDataFrom.times = this.TimeList[i]
          await getCountDayCompTaskByProjectIdTimes(this.LineDataFrom).then(response1 => {
            this.data2.push({time: this.TimeList[i], value: this.taskCount - response1.data})
          })
        }
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
    },
    async getTime(day) {
      this.LineDataFrom.projectId = this.projectId
      this.LineDataFrom.times = this.getNowFormatDate()
      await getCountDayTaskByProjectIdTimes(this.LineDataFrom).then(response => {
        this.taskCount = response.data
      })
      const date1 = new Date()
      let year = date1.getFullYear();
      let month = date1.getMonth() + 1 < 10 ? '0' + (date1.getMonth() + 1) : date1.getMonth() + 1;
      let date = date1.getDate() < 10 ? '0' + date1.getDate() : date1.getDate();
      this.nowTime = year + '-' + month + '-' + date
      const date2 = date1
      let time = null
      let value = null
      for (let i = day; i >= 0; i--) {
        date2.setDate(new Date().getDate() - i)
        year = date2.getFullYear();
        month = date2.getMonth() + 1 < 10 ? '0' + (date2.getMonth() + 1) : date2.getMonth() + 1;
        date = date2.getDate() < 10 ? '0' + date2.getDate() : date2.getDate();
        this.TimeList.push(year + '-' + month + '-' + date)
        // this.taskCountList.push(this.taskCount - (day - i) * (this.taskCount / day))
        time = year + '-' + month + '-' + date
        value = this.taskCount - (day - i) * (this.taskCount / day)
        this.data1.push({time: time, value: value})
      }
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions()
    },
    setOptions() {
      this.chart.setOption({
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.TimeList
        },
        yAxis: [{
          type: 'value',
          boundaryGap: false,
          axisTick: {
            show: false
          }
        }],
        series: [{
          name: '理想任务剩余数',
          type: 'line',
          stack: '',
          data: this.data1,
          itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          smooth: false,
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        }, {
          name: '实际剩余任务数',
          type: 'line',
          stack: '',
          data: this.data2,
          smooth: false,
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              },
              areaStyle: {
                color: '#f3f8ff'
              }
            }
          },
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }],
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        }
      })
    }
  }
}
</script>

<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { getCountTaskUserByProjectId } from 'api/project/task/index'

export default {
  created() {
    this.projectId = this.$route.params.id
    this.getCountTaskUserByProjectId(this.projectId)
  },
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
      default: '300px'
    }
  },
  data() {
    return {
      chart: null,
      projectId: '',
      userTaskConut: [],
      userList: []
    }
  },
  mounted() {
    setTimeout(() => {
      this.$nextTick(() => {
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
    getCountTaskUserByProjectId(id) {
      // console.info(id)
      getCountTaskUserByProjectId(id).then(response => {
        this.userTaskConut = response.data
        response.data.forEach((item, index) => {
          this.userList.push(item.name)
        })
      })
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b}<br/>占比：{d}%<br/>任务数: {c}'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: this.userList // ['Industries', 'Technology', 'Forex', 'Gold', 'Forecasts']
        },
        series: [
          {
            name: '任务分布',
            type: 'pie',
            // roseType: 'radius',
            radius: [70, 105],
            center: ['50%', '38%'],
            data: this.userTaskConut,
            animationEasing: 'cubicInOut',
            animationDuration: 6000
          }
        ]
      })
    }
  }
}
</script>

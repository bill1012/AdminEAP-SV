<template>
  <div>
    <div class="mixin-components-container">
      <el-row :gutter="20" style="margin-top:0px;">
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="header">
                <span>任务按执行者分布</span>
              </div>
              <div class="component-item" style="height:300px;">
                <div><pie-chart /></div>
              </div>
            </el-card>
          </el-col>
            <el-col :span="12">
              <el-card class="box-card">
                <div slot="header" class="header">
                  <span>期间完成的任务</span>
                </div>
                <div class="component-item" style="height:300px;">
                  <div><bar-chart></bar-chart></div>
                </div>
              </el-card>
            </el-col>
        </el-row>
      <el-row :gutter="20" style="margin-top:10px;">
        <el-col :span="12">
          <el-card class="box-card">
            <div slot="header" class="header">
              <span>概览报表</span>
            </div>
            <div class="component-item" style="height:300px;">
              <div><box-card/></div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="box-card">
            <div slot="header" class="header">
              <span>任务燃尽图</span>
            </div>
            <div class="component-item" style="height:300px;">
              <div><line-chart/></div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top:10px;">
        <el-col :span="12">
          <el-card class="box-card">
            <div slot="header" class="header" style="color: #a6a6a6;font-size: 16px; font-weight: bold;">
              <span >项目进展走势</span>
            </div>
            <div class="component-item" style="height:300px;">
              <div><line-chart-xmjzzs/></div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>


<script>
  import PieChart from './components/PieChart'
  import BarChart from './components/BarChart'
  import LineChart from './components/LineChart'
  import LineChartXmjzzs from './components/LineChartXmjzzs'
  import BoxCard from './components/BoxCard'

  export default {
    name: 'ComponentMixinDemo',
    components: {
      PieChart,
      BarChart,
      LineChart,
      LineChartXmjzzs,
      BoxCard
    },
    data() {
      return {
      }
    },
    methods: {
    },
    props: {
      sendhea: {
        type: String
      }
    }
  }
</script>

<style scoped>
  .errPage-container {
    padding: 0px;
  }
  .mixin-components-container {
    background-color: #ffffff;
    padding: 0px;
    /*min-height: calc(100vh - 84px);*/
  }
  .component-item{
    min-height: 0px;
  }
  .header{
    color: #a6a6a6;
    font-size: 16px;
    font-weight: bold;
  }
</style>

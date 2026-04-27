<template>
  <div class="ops-page">
    <div class="page-head">
      <div>
        <h2>茶园产能地图</h2>
        <p>按产区查看基地数量、面积、年产能和高海拔资源。</p>
      </div>
      <el-button type="primary" icon="el-icon-refresh" @click="fetchData">刷新</el-button>
    </div>

    <el-row :gutter="16" class="metric-row">
      <el-col :xs="24" :sm="12" :lg="6" v-for="item in metrics" :key="item.label">
        <div class="metric-card">
          <span>{{ item.label }}</span>
          <strong>{{ item.value }}</strong>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="14">
        <el-card shadow="never" header="产区产能分布">
          <div ref="regionChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="never" header="产区明细">
          <el-table :data="regionStats" stripe>
            <el-table-column prop="regionName" label="产区" min-width="140" />
            <el-table-column prop="baseCount" label="基地数" width="90" />
            <el-table-column prop="annualCapacity" label="年产能(kg)" width="130" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  data() {
    return {
      summary: {},
      regionStats: [],
      chart: null
    }
  },
  computed: {
    metrics() {
      return [
        { label: '基地总数', value: this.summary.baseCount || 0 },
        { label: '总面积(亩)', value: this.summary.totalArea || 0 },
        { label: '预计年产能(kg)', value: this.summary.annualCapacity || 0 },
        { label: '高海拔基地', value: this.summary.highAltitudeBaseCount || 0 }
      ]
    }
  },
  mounted() {
    this.fetchData()
    window.addEventListener('resize', this.resizeChart)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeChart)
    if (this.chart) this.chart.dispose()
  },
  methods: {
    fetchData() {
      Promise.all([
        this.$http.get('teabase/capacitySummary'),
        this.$http.get('teabase/regionStats')
      ]).then(([summaryRes, regionRes]) => {
        this.summary = (summaryRes.data && summaryRes.data.data) || {}
        this.regionStats = (regionRes.data && regionRes.data.data) || []
        this.$nextTick(this.renderChart)
      })
    },
    renderChart() {
      if (!this.chart) this.chart = echarts.init(this.$refs.regionChart)
      this.chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 36, right: 16, top: 32, bottom: 28, containLabel: true },
        xAxis: { type: 'category', data: this.regionStats.map(item => item.regionName) },
        yAxis: { type: 'value' },
        series: [
          {
            type: 'bar',
            name: '预计年产能',
            data: this.regionStats.map(item => item.annualCapacity || 0),
            itemStyle: { color: '#2f664a' }
          }
        ]
      })
    },
    resizeChart() {
      if (this.chart) this.chart.resize()
    }
  }
}
</script>

<style scoped>
.ops-page { display: grid; gap: 16px; }
.page-head { display: flex; justify-content: space-between; align-items: center; gap: 16px; }
.page-head h2 { margin: 0 0 6px; color: #1f3a2e; }
.page-head p { margin: 0; color: #6b7f75; }
.metric-row { margin-bottom: 4px; }
.metric-card { padding: 18px; border: 1px solid #e8efeb; border-radius: 8px; background: #fbfdfb; }
.metric-card span { display: block; color: #6b7f75; }
.metric-card strong { display: block; margin-top: 8px; font-size: 24px; color: #1f3a2e; }
.chart-box { height: 320px; }
</style>

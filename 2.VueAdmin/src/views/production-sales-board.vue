<template>
  <div class="ops-page">
    <div class="page-head">
      <div>
        <h2>产销计划看板</h2>
        <p>展示年度计划产量、销量、收入和供需缺口。</p>
      </div>
      <el-date-picker v-model="year" type="year" value-format="yyyy" placeholder="选择年份" @change="fetchData" />
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
        <el-card shadow="never" header="月度计划趋势">
          <div ref="planChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="never" header="供需缺口">
          <el-table :data="gapList" stripe>
            <el-table-column prop="planMonth" label="月份" width="80" />
            <el-table-column prop="teatype" label="茶类" />
            <el-table-column prop="gapAmount" label="缺口(kg)" width="110" />
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
      year: `${new Date().getFullYear()}`,
      summary: {},
      trend: [],
      gapList: [],
      chart: null
    }
  },
  computed: {
    metrics() {
      return [
        { label: '计划产量(kg)', value: this.summary.plannedOutput || 0 },
        { label: '计划销量(kg)', value: this.summary.plannedSales || 0 },
        { label: '目标库存(kg)', value: this.summary.targetInventory || 0 },
        { label: '缺口月份', value: this.summary.gapCount || 0 }
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
      this.$http.get('productionSalesPlan/annualBoard', { params: { year: this.year } }).then(({ data }) => {
        const payload = (data && data.data) || {}
        this.summary = payload.summary || {}
        this.trend = payload.trend || []
        this.gapList = payload.gapList || []
        this.$nextTick(this.renderChart)
      })
    },
    renderChart() {
      if (!this.chart) this.chart = echarts.init(this.$refs.planChart)
      this.chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['计划产量', '计划销量'] },
        grid: { left: 36, right: 16, top: 44, bottom: 28, containLabel: true },
        xAxis: { type: 'category', data: this.trend.map(item => `${item.planMonth}月`) },
        yAxis: { type: 'value' },
        series: [
          { name: '计划产量', type: 'bar', data: this.trend.map(item => item.plannedOutput || 0), itemStyle: { color: '#2f664a' } },
          { name: '计划销量', type: 'line', smooth: true, data: this.trend.map(item => item.plannedSales || 0), itemStyle: { color: '#d7a23a' } }
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

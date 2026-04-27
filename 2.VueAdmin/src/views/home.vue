<template>
  <div class="dashboard-container">
    <div class="welcome-section">
      <div class="welcome-text">
        <h1>{{ roleLabel }}，{{ adminName }}</h1>
        <p>高山茶产供销智能决策平台经营总览</p>
      </div>
      <div class="date-display">{{ currentDate }}</div>
    </div>

    <el-row :gutter="16" class="stat-cards">
      <el-col v-for="item in statCards" :key="item.key" :xs="24" :sm="12" :lg="6">
        <el-card shadow="hover" class="stat-card">
          <div class="card-content">
            <div class="card-icon" :style="{ backgroundColor: item.bg }">
              <i :class="item.icon"></i>
            </div>
            <div class="card-info">
              <div class="card-value">{{ formatValue(item.value, item.type) }}</div>
              <div class="card-label">{{ item.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="main-charts">
      <el-col :xs="24" :lg="16">
        <el-card class="chart-card" header="月度销售趋势">
          <div ref="salesChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card class="chart-card" header="茶类销售占比">
          <div ref="categoryChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="list-section">
      <el-col :xs="24" :lg="12">
        <el-card class="list-card" header="热销茶品">
          <el-table :data="dashboard.topProducts" stripe>
            <el-table-column prop="name" label="茶品名称" />
            <el-table-column prop="value" label="销量" width="100" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="list-card" header="库存预警">
          <el-table :data="dashboard.lowStockList" stripe>
            <el-table-column prop="productName" label="茶品" />
            <el-table-column prop="batchCode" label="批次" width="140" />
            <el-table-column prop="currentStock" label="当前库存" width="110" />
            <el-table-column prop="warningStock" label="预警值" width="110" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="decision-extension">
      <el-col :xs="24" :lg="12">
        <el-card class="list-card" header="基地与年度计划">
          <div class="summary-grid">
            <div><span>基地总面积</span><strong>{{ dashboard.baseCapacitySummary.totalArea || 0 }}</strong></div>
            <div><span>预计年产能</span><strong>{{ dashboard.baseCapacitySummary.annualCapacity || 0 }}</strong></div>
            <div><span>计划销量</span><strong>{{ dashboard.annualPlanSummary.plannedSales || 0 }}</strong></div>
            <div><span>缺口月份</span><strong>{{ dashboard.annualPlanSummary.gapCount || 0 }}</strong></div>
          </div>
          <el-table :data="dashboard.planGapList" stripe>
            <el-table-column prop="planMonth" label="月份" width="80" />
            <el-table-column prop="teatype" label="茶类" />
            <el-table-column prop="gapAmount" label="缺口(kg)" width="110" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="list-card" header="跨仓调拨与销售期">
          <el-table :data="dashboard.transferSuggestionList" stripe>
            <el-table-column prop="productName" label="茶品" />
            <el-table-column prop="sourceWarehouseName" label="来源仓" width="120" />
            <el-table-column prop="targetWarehouseName" label="目标仓" width="120" />
            <el-table-column prop="suggestedQuantity" label="建议量" width="90" />
          </el-table>
          <el-table :data="dashboard.lifecycleRiskList" stripe class="lifecycle-table">
            <el-table-column prop="productName" label="销售期风险茶品" />
            <el-table-column prop="stageCode" label="阶段" width="90" />
            <el-table-column prop="actionLabel" label="建议动作" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="advice-card" header="经营建议">
      <div class="advice-list">
        <div v-for="(item, index) in dashboard.adviceList" :key="index" class="advice-item">
          <div class="advice-tag" :class="item.level">{{ item.title }}</div>
          <div class="advice-content">{{ item.content }}</div>
          <div class="advice-action">{{ item.actionLabel }}</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  data() {
    return {
      adminName: '',
      currentDate: '',
      roleLabel: '欢迎回来',
      timer: null,
      dashboard: {
        stats: {},
        salesTrend: [],
        categoryShare: [],
        topProducts: [],
        lowStockList: [],
        adviceList: [],
        baseCapacitySummary: {},
        annualPlanSummary: {},
        planGapList: [],
        transferSuggestionList: [],
        lifecycleRiskList: []
      },
      statCards: [
        { key: 'enterpriseCount', label: '茶企数量', icon: 'el-icon-office-building', bg: '#e6f4ea', type: 'count', value: 0 },
        { key: 'baseCount', label: '茶园基地', icon: 'el-icon-location-outline', bg: '#eef3ff', type: 'count', value: 0 },
        { key: 'productCount', label: '茶品档案', icon: 'el-icon-collection-tag', bg: '#fff4e6', type: 'count', value: 0 },
        { key: 'orderCount', label: '订单总量', icon: 'el-icon-s-order', bg: '#f4ecff', type: 'count', value: 0 },
        { key: 'currentMonthSales', label: '本月销量', icon: 'el-icon-data-line', bg: '#eefbf5', type: 'count', value: 0 },
        { key: 'currentMonthRevenue', label: '本月销售额', icon: 'el-icon-money', bg: '#fff1f0', type: 'money', value: 0 },
        { key: 'inventoryWarningCount', label: '库存告警', icon: 'el-icon-warning-outline', bg: '#fff7e6', type: 'count', value: 0 },
        { key: 'slowMovingProductCount', label: '滞销茶品', icon: 'el-icon-truck', bg: '#f2f3f5', type: 'count', value: 0 }
      ],
      salesChart: null,
      categoryChart: null
    }
  },
  mounted() {
    this.adminName = this.$storage.get('adminName') || '管理员'
    this.roleLabel = this.$storage.get('role') === '商家' ? '茶企端看板' : '管理员看板'
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)
    this.fetchDashboard()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    clearInterval(this.timer)
    window.removeEventListener('resize', this.handleResize)
    if (this.salesChart) {
      this.salesChart.dispose()
      this.salesChart = null
    }
    if (this.categoryChart) {
      this.categoryChart.dispose()
      this.categoryChart = null
    }
  },
  methods: {
    updateTime() {
      const now = new Date()
      this.currentDate = `${now.toLocaleDateString()} ${now.toLocaleTimeString()}`
    },
    formatValue(value, type) {
      if (type === 'money') {
        return `¥${Number(value || 0).toFixed(2)}`
      }
      return value || 0
    },
    fetchDashboard() {
      this.$http.get('decision/dashboard').then(({ data }) => {
        if (data && data.code === 0) {
          this.dashboard = data.data || this.dashboard
          this.statCards = this.statCards.map(item => ({
            ...item,
            value: (this.dashboard.stats && this.dashboard.stats[item.key]) || 0
          }))
          this.$nextTick(() => {
            this.renderCharts()
          })
        }
      })
    },
    renderCharts() {
      if (!this.salesChart) {
        this.salesChart = echarts.init(this.$refs.salesChart)
      }
      if (!this.categoryChart) {
        this.categoryChart = echarts.init(this.$refs.categoryChart)
      }

      this.salesChart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['销量', '销售额'] },
        grid: {
          top: 48,
          left: 24,
          right: 24,
          bottom: 24,
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.dashboard.salesTrend.map(item => item.label)
        },
        yAxis: [
          { type: 'value', name: '销量' },
          { type: 'value', name: '销售额' }
        ],
        series: [
          {
            name: '销量',
            type: 'bar',
            data: this.dashboard.salesTrend.map(item => item.sales),
            itemStyle: { color: '#2d5a27' }
          },
          {
            name: '销售额',
            type: 'line',
            yAxisIndex: 1,
            smooth: true,
            data: this.dashboard.salesTrend.map(item => item.revenue),
            itemStyle: { color: '#d48806' }
          }
        ]
      })

      this.categoryChart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0, itemWidth: 12, itemHeight: 12 },
        series: [
          {
            type: 'pie',
            radius: ['42%', '68%'],
            center: ['50%', '46%'],
            data: this.dashboard.categoryShare
          }
        ]
      })
    },
    handleResize() {
      if (this.salesChart) this.salesChart.resize()
      if (this.categoryChart) this.categoryChart.resize()
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  .welcome-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
  }

  .welcome-text h1 {
    margin: 0 0 4px;
    font-size: 28px;
    color: #1f3a2e;
  }

  .welcome-text p,
  .date-display {
    color: #5b6d64;
  }

  .date-display {
    flex-shrink: 0;
    background: #f5faf7;
    padding: 10px 16px;
    border-radius: 10px;
  }

  .stat-cards,
  .main-charts,
  .list-section,
  .decision-extension {
    margin-bottom: 16px;
  }

  .stat-card,
  .chart-card,
  .list-card,
  .advice-card {
    margin-bottom: 16px;
  }

  .card-content {
    display: flex;
    align-items: center;
  }

  .card-icon {
    width: 52px;
    height: 52px;
    border-radius: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 14px;

    i {
      font-size: 22px;
      color: #244536;
    }
  }

  .card-value {
    font-size: 24px;
    font-weight: 700;
    color: #1f3a2e;
  }

  .card-label {
    margin-top: 6px;
    color: #6b7f75;
    font-size: 13px;
  }

  .chart-box {
    height: 280px;
  }

  .summary-grid {
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 10px;
    margin-bottom: 14px;

    div {
      padding: 12px;
      border-radius: 8px;
      background: #f5faf7;
      border: 1px solid #e4eee8;
    }

    span {
      display: block;
      color: #6b7f75;
      font-size: 12px;
    }

    strong {
      display: block;
      margin-top: 6px;
      color: #1f3a2e;
      font-size: 18px;
    }
  }

  .lifecycle-table {
    margin-top: 14px;
  }

  .advice-list {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: 16px;
  }

  .advice-item {
    border: 1px solid #edf2ef;
    border-radius: 12px;
    padding: 16px;
    background: #fafcfb;
  }

  .advice-tag {
    display: inline-flex;
    padding: 4px 10px;
    border-radius: 999px;
    font-size: 12px;
    font-weight: 600;
    margin-bottom: 10px;
  }

  .advice-tag.warning { background: #fff7e6; color: #d48806; }
  .advice-tag.success { background: #e6f4ea; color: #389e0d; }
  .advice-tag.danger { background: #fff1f0; color: #cf1322; }
  .advice-tag.info { background: #e6f4ff; color: #1677ff; }

  .advice-content {
    color: #33463d;
    line-height: 1.7;
    min-height: 48px;
  }

  .advice-action {
    margin-top: 12px;
    color: #2d5a27;
    font-size: 12px;
    font-weight: 600;
  }
}

@media (max-width: 1199px) {
  .dashboard-container {
    .welcome-section {
      align-items: flex-start;
      flex-wrap: wrap;
    }
  }
}

@media (max-width: 991px) {
  .dashboard-container {
    .welcome-text h1 {
      font-size: 24px;
    }

    .date-display {
      width: 100%;
    }

    .summary-grid {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }
  }
}

@media (max-width: 767px) {
  .dashboard-container {
    .chart-box {
      height: 240px;
    }

    .card-content {
      align-items: flex-start;
    }
  }
}
</style>

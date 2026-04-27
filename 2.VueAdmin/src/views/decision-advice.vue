<template>
  <div class="decision-page">
    <section class="hero-panel">
      <div>
        <p class="eyebrow">决策中心</p>
        <h1>自动生成的经营任务台</h1>
        <p class="hero-copy">系统按库存、补货、滞销和经营波动自动生成任务，商家只需要处理和复盘结果。</p>
      </div>
      <el-button type="primary" icon="el-icon-refresh" @click="refreshDashboard">刷新数据</el-button>
    </section>

    <section class="task-stat-grid">
      <article v-for="item in taskStatCards" :key="item.key" class="task-stat-card" :class="item.key">
        <p>{{ item.label }}</p>
        <strong>{{ item.value }}</strong>
      </article>
    </section>

    <el-card shadow="never" class="section-card">
      <div slot="header" class="section-header">
        <span>重点任务</span>
        <span class="section-tip">优先展示紧急 / 高优先级的待处理任务</span>
      </div>
      <div v-if="highlightedTasks.length" class="highlight-grid">
        <article v-for="task in highlightedTasks" :key="task.id || task.sourceKey" class="highlight-card">
          <div class="highlight-top">
            <div>
              <span class="task-type">{{ taskTypeLabel(task.taskType) }}</span>
              <h3>{{ task.title }}</h3>
            </div>
            <div class="tag-group">
              <el-tag size="mini" :type="priorityTagType(task.priority)">{{ priorityLabel(task.priority) }}</el-tag>
              <el-tag size="mini" :type="statusTagType(task.status)">{{ statusLabel(task.status) }}</el-tag>
            </div>
          </div>
          <p class="highlight-summary">{{ task.summary }}</p>
          <p class="highlight-action">建议动作：{{ task.actionSuggestion }}</p>
          <div class="highlight-footer">
            <span>截止时间：{{ formatDateTime(task.dueAt) }}</span>
            <div class="task-actions">
              <el-button size="mini" plain @click="openTask(task)">详情</el-button>
              <el-button
                v-if="canStart(task.status)"
                size="mini"
                type="warning"
                plain
                @click="startTask(task)"
              >开始处理</el-button>
              <el-button
                v-if="canFinish(task.status)"
                size="mini"
                type="success"
                plain
                @click="openCompleteDialog(task)"
              >标记完成</el-button>
              <el-button
                v-if="canFinish(task.status)"
                size="mini"
                type="info"
                plain
                @click="openIgnoreDialog(task)"
              >忽略</el-button>
            </div>
          </div>
        </article>
      </div>
      <el-empty v-else description="暂无重点任务" :image-size="84" />
    </el-card>

    <el-card shadow="never" class="section-card">
      <div slot="header" class="section-header">
        <span>任务列表</span>
        <span class="section-tip">支持按状态、类型、优先级筛选</span>
      </div>
      <div class="filter-bar">
        <el-select v-model="taskFilter.status" clearable placeholder="状态" size="small" @change="fetchTasks(1)">
          <el-option label="待处理" value="todo" />
          <el-option label="处理中" value="processing" />
          <el-option label="已逾期" value="expired" />
          <el-option label="已完成" value="done" />
          <el-option label="已忽略" value="ignored" />
        </el-select>
        <el-select v-model="taskFilter.taskType" clearable placeholder="任务类型" size="small" @change="fetchTasks(1)">
          <el-option label="库存预警" value="inventory_alert" />
          <el-option label="补货跟进" value="restock_followup" />
          <el-option label="滞销处理" value="slow_moving" />
          <el-option label="增长跟进" value="growth_followup" />
          <el-option label="异常关注" value="enterprise_abnormal" />
        </el-select>
        <el-select v-model="taskFilter.priority" clearable placeholder="优先级" size="small" @change="fetchTasks(1)">
          <el-option label="紧急" value="urgent" />
          <el-option label="高" value="high" />
          <el-option label="中" value="medium" />
          <el-option label="低" value="low" />
        </el-select>
      </div>
      <el-table :data="taskPage.list" stripe>
        <el-table-column label="任务类型" min-width="120">
          <template slot-scope="scope">{{ taskTypeLabel(scope.row.taskType) }}</template>
        </el-table-column>
        <el-table-column prop="title" label="任务标题" min-width="180" />
        <el-table-column prop="summary" label="问题说明" min-width="240" show-overflow-tooltip />
        <el-table-column label="优先级" width="110">
          <template slot-scope="scope">
            <el-tag size="mini" :type="priorityTagType(scope.row.priority)">{{ priorityLabel(scope.row.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="110">
          <template slot-scope="scope">
            <el-tag size="mini" :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="截止时间" width="170">
          <template slot-scope="scope">{{ formatDateTime(scope.row.dueAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="openTask(scope.row)">详情</el-button>
            <el-button v-if="canStart(scope.row.status)" size="mini" type="text" @click="startTask(scope.row)">开始处理</el-button>
            <el-button v-if="canFinish(scope.row.status)" size="mini" type="text" @click="openCompleteDialog(scope.row)">完成</el-button>
            <el-button v-if="canFinish(scope.row.status)" size="mini" type="text" @click="openIgnoreDialog(scope.row)">忽略</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="taskPage.currPage || 1"
          :page-size="taskPage.pageSize || 10"
          :total="taskPage.total || 0"
          @current-change="fetchTasks"
        />
      </div>
    </el-card>

    <section class="insight-grid">
      <el-card shadow="never" class="section-card review-card">
        <div slot="header" class="section-header">
          <span>本周改善</span>
          <span class="section-tip">轻量复盘处理结果</span>
        </div>
        <div class="review-grid">
          <div class="review-item">
            <span>本周新增任务</span>
            <strong>{{ weeklyReview.newTaskCount || 0 }}</strong>
          </div>
          <div class="review-item">
            <span>本周完成任务</span>
            <strong>{{ weeklyReview.doneTaskCount || 0 }}</strong>
          </div>
          <div class="review-item">
            <span>本周忽略任务</span>
            <strong>{{ weeklyReview.ignoredTaskCount || 0 }}</strong>
          </div>
          <div class="review-item">
            <span>当前逾期任务</span>
            <strong>{{ weeklyReview.expiredTaskCount || 0 }}</strong>
          </div>
          <div class="review-item">
            <span>高风险未闭环</span>
            <strong>{{ weeklyReview.highPriorityOpenCount || 0 }}</strong>
          </div>
        </div>
      </el-card>

      <el-card shadow="never" class="section-card review-card">
        <div slot="header" class="section-header">
          <span>经营分析</span>
          <span class="section-tip">保留原有分析能力，作为任务的辅助证据</span>
        </div>
        <div class="analysis-grid">
          <div class="analysis-item">
            <span>未来 7 天预测销量</span>
            <strong>{{ formatNumber(stats.forecast7DaysTotal) }}</strong>
            <em>主推商品：{{ forecastSummary.topProductName || '暂无预测' }}</em>
          </div>
          <div class="analysis-item">
            <span>高风险库存数</span>
            <strong>{{ formatNumber(stats.highRiskProductCount) }}</strong>
            <em>优先处理预警 / 紧急项</em>
          </div>
          <div class="analysis-item">
            <span>待补货商品数</span>
            <strong>{{ formatNumber(stats.restockPendingCount) }}</strong>
            <em>建议尽快安排补货动作</em>
          </div>
        </div>
      </el-card>
    </section>

    <el-card shadow="never" class="section-card">
      <div slot="header" class="section-header">
        <span>经营建议</span>
        <span class="section-tip">规则型建议保留，帮助理解任务来源</span>
      </div>
      <div class="advice-grid">
        <article v-for="(item, index) in adviceList" :key="index" class="advice-item">
          <div class="advice-top">
            <el-tag size="mini" :type="levelTagType(item.level)">{{ item.title }}</el-tag>
          </div>
          <p>{{ item.content }}</p>
          <span>{{ item.actionLabel }}</span>
        </article>
      </div>
    </el-card>

    <el-drawer
      :visible.sync="taskDrawerVisible"
      title="任务详情"
      size="38%"
      direction="rtl"
    >
      <div v-if="selectedTask" class="task-detail">
        <div class="detail-row">
          <span>任务标题</span>
          <strong>{{ selectedTask.title }}</strong>
        </div>
        <div class="detail-row">
          <span>任务类型</span>
          <strong>{{ taskTypeLabel(selectedTask.taskType) }}</strong>
        </div>
        <div class="detail-row">
          <span>当前状态</span>
          <el-tag size="mini" :type="statusTagType(selectedTask.status)">{{ statusLabel(selectedTask.status) }}</el-tag>
        </div>
        <div class="detail-row">
          <span>建议动作</span>
          <strong>{{ selectedTask.actionSuggestion }}</strong>
        </div>
        <div class="detail-block">
          <h4>问题说明</h4>
          <p>{{ selectedTask.summary }}</p>
        </div>
        <div class="detail-block">
          <h4>证据快照</h4>
          <el-descriptions :column="1" border size="small">
            <el-descriptions-item
              v-for="(value, key) in parsedEvidence"
              :key="key"
              :label="key"
            >
              {{ value }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
        <div v-if="selectedTask.resultNote" class="detail-block">
          <h4>处理说明</h4>
          <p>{{ selectedTask.resultNote }}</p>
        </div>
        <div v-if="selectedTask.ignoreReason" class="detail-block">
          <h4>忽略原因</h4>
          <p>{{ selectedTask.ignoreReason }}</p>
        </div>
      </div>
    </el-drawer>

    <el-dialog title="标记完成" :visible.sync="completeDialogVisible" width="420px">
      <el-input
        v-model="completeForm.resultNote"
        type="textarea"
        :rows="4"
        maxlength="200"
        show-word-limit
        placeholder="请填写本次处理动作，例如：已确认补货 300 件，预计两天内入库。"
      />
      <span slot="footer">
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitComplete">提交</el-button>
      </span>
    </el-dialog>

    <el-dialog title="忽略任务" :visible.sync="ignoreDialogVisible" width="420px">
      <el-input
        v-model="ignoreForm.ignoreReason"
        type="textarea"
        :rows="4"
        maxlength="200"
        show-word-limit
        placeholder="请填写忽略原因，例如：本周不做促销，待下月活动统一处理。"
      />
      <span slot="footer">
        <el-button @click="ignoreDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitIgnore">提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      stats: {},
      forecastSummary: {},
      adviceList: [],
      taskStats: {},
      highlightedTasks: [],
      weeklyReview: {},
      taskPage: { list: [], total: 0, pageSize: 10, currPage: 1 },
      taskFilter: {
        status: '',
        taskType: '',
        priority: ''
      },
      selectedTask: null,
      taskDrawerVisible: false,
      completeDialogVisible: false,
      ignoreDialogVisible: false,
      currentTaskId: null,
      completeForm: {
        resultNote: ''
      },
      ignoreForm: {
        ignoreReason: ''
      }
    }
  },
  computed: {
    taskStatCards() {
      return [
        { key: 'todo', label: '待处理', value: this.taskStats.todoCount || 0 },
        { key: 'processing', label: '处理中', value: this.taskStats.processingCount || 0 },
        { key: 'expired', label: '已逾期', value: this.taskStats.expiredCount || 0 },
        { key: 'done', label: '近 7 天已完成', value: this.taskStats.done7DaysCount || 0 }
      ]
    },
    parsedEvidence() {
      if (!this.selectedTask || !this.selectedTask.evidenceSnapshot) {
        return {}
      }
      try {
        return JSON.parse(this.selectedTask.evidenceSnapshot)
      } catch (error) {
        return { raw: this.selectedTask.evidenceSnapshot }
      }
    }
  },
  created() {
    this.fetchDashboard()
  },
  methods: {
    refreshDashboard() {
      this.fetchDashboard()
      this.fetchTasks(this.taskPage.currPage || 1)
    },
    fetchDashboard() {
      this.$http.get('decision/dashboard').then(({ data }) => {
        if (data && data.code === 0) {
          const payload = data.data || {}
          this.stats = payload.stats || {}
          this.forecastSummary = payload.forecastSummary || {}
          this.adviceList = payload.adviceList || []
          this.taskStats = payload.taskStats || {}
          this.highlightedTasks = payload.highlightedTasks || []
          this.weeklyReview = payload.weeklyReview || {}
          this.taskPage = payload.taskPage || this.taskPage
        }
      })
    },
    fetchTasks(page) {
      const params = {
        page: page || 1,
        limit: this.taskPage.pageSize || 10
      }
      if (this.taskFilter.status) params.status = this.taskFilter.status
      if (this.taskFilter.taskType) params.taskType = this.taskFilter.taskType
      if (this.taskFilter.priority) params.priority = this.taskFilter.priority
      this.$http.get('decision/tasks', { params }).then(({ data }) => {
        if (data && data.code === 0) {
          this.taskPage = data.data || this.taskPage
        }
      })
    },
    openTask(task) {
      this.$http.get(`decision/tasks/${task.id}`).then(({ data }) => {
        if (data && data.code === 0) {
          this.selectedTask = data.data || task
          this.taskDrawerVisible = true
        }
      })
    },
    startTask(task) {
      this.$http.post(`decision/tasks/${task.id}/start`).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message.success('任务已进入处理中')
          this.refreshDashboard()
        } else {
          this.$message.error((data && data.msg) || '操作失败')
        }
      })
    },
    openCompleteDialog(task) {
      this.currentTaskId = task.id
      this.completeForm.resultNote = ''
      this.completeDialogVisible = true
    },
    submitComplete() {
      this.$http.post(`decision/tasks/${this.currentTaskId}/complete`, this.completeForm).then(({ data }) => {
        if (data && data.code === 0) {
          this.completeDialogVisible = false
          this.$message.success('任务已完成')
          this.refreshDashboard()
        } else {
          this.$message.error((data && data.msg) || '提交失败')
        }
      })
    },
    openIgnoreDialog(task) {
      this.currentTaskId = task.id
      this.ignoreForm.ignoreReason = ''
      this.ignoreDialogVisible = true
    },
    submitIgnore() {
      this.$http.post(`decision/tasks/${this.currentTaskId}/ignore`, this.ignoreForm).then(({ data }) => {
        if (data && data.code === 0) {
          this.ignoreDialogVisible = false
          this.$message.success('任务已忽略')
          this.refreshDashboard()
        } else {
          this.$message.error((data && data.msg) || '提交失败')
        }
      })
    },
    canStart(status) {
      return status === 'todo' || status === 'expired'
    },
    canFinish(status) {
      return status === 'todo' || status === 'processing' || status === 'expired'
    },
    statusLabel(status) {
      const map = {
        todo: '待处理',
        processing: '处理中',
        done: '已完成',
        ignored: '已忽略',
        expired: '已逾期'
      }
      return map[status] || status || '-'
    },
    taskTypeLabel(taskType) {
      const map = {
        inventory_alert: '库存预警',
        restock_followup: '补货跟进',
        slow_moving: '滞销处理',
        growth_followup: '增长跟进',
        enterprise_abnormal: '异常关注'
      }
      return map[taskType] || taskType || '-'
    },
    priorityLabel(priority) {
      const map = {
        urgent: '紧急',
        high: '高',
        medium: '中',
        low: '低'
      }
      return map[priority] || priority || '-'
    },
    priorityTagType(priority) {
      const map = {
        urgent: 'danger',
        high: 'warning',
        medium: '',
        low: 'success'
      }
      return map[priority] || 'info'
    },
    statusTagType(status) {
      const map = {
        todo: '',
        processing: 'warning',
        done: 'success',
        ignored: 'info',
        expired: 'danger'
      }
      return map[status] || 'info'
    },
    levelTagType(level) {
      const map = {
        success: 'success',
        warning: 'warning',
        danger: 'danger',
        info: 'info'
      }
      return map[level] || 'info'
    },
    formatDateTime(value) {
      if (!value) return '-'
      const date = new Date(value)
      if (Number.isNaN(date.getTime())) return value
      const pad = item => `${item}`.padStart(2, '0')
      return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}`
    },
    formatNumber(value) {
      return Number(value || 0)
    }
  }
}
</script>

<style scoped>
.decision-page {
  display: grid;
  gap: 20px;
}

.hero-panel {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  padding: 28px 32px;
  border-radius: 24px;
  background: linear-gradient(135deg, #18392b 0%, #2f664a 55%, #d7b778 100%);
  color: #fff;
  box-shadow: 0 22px 40px rgba(31, 58, 46, 0.16);
}

.eyebrow {
  margin: 0 0 8px;
  font-size: 13px;
  letter-spacing: 0.18em;
  text-transform: uppercase;
  opacity: 0.78;
}

.hero-panel h1 {
  margin: 0;
  font-size: 30px;
  line-height: 1.2;
}

.hero-copy {
  margin: 12px 0 0;
  max-width: 680px;
  color: rgba(255, 255, 255, 0.88);
}

.task-stat-grid,
.insight-grid {
  display: grid;
  gap: 16px;
}

.task-stat-grid {
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

.task-stat-card {
  padding: 18px 20px;
  border-radius: 18px;
  background: #fff;
  border: 1px solid #edf1ed;
}

.task-stat-card p {
  margin: 0 0 10px;
  color: #6b7d73;
}

.task-stat-card strong {
  font-size: 28px;
  color: #18392b;
}

.task-stat-card.todo {
  background: linear-gradient(180deg, #f7faf7 0%, #ffffff 100%);
}

.task-stat-card.processing {
  background: linear-gradient(180deg, #fff8ec 0%, #ffffff 100%);
}

.task-stat-card.expired {
  background: linear-gradient(180deg, #fff0ee 0%, #ffffff 100%);
}

.task-stat-card.done {
  background: linear-gradient(180deg, #eef9f0 0%, #ffffff 100%);
}

.section-card {
  border-radius: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.section-tip {
  color: #8a968f;
  font-size: 12px;
}

.highlight-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.highlight-card {
  padding: 18px;
  border-radius: 18px;
  border: 1px solid #edf1ed;
  background: linear-gradient(180deg, #fafcf8 0%, #ffffff 100%);
}

.highlight-top,
.highlight-footer,
.filter-bar,
.task-actions,
.tag-group,
.advice-top {
  display: flex;
  align-items: center;
}

.highlight-top,
.highlight-footer {
  justify-content: space-between;
  gap: 12px;
}

.highlight-top h3 {
  margin: 8px 0 0;
  color: #18392b;
}

.task-type {
  display: inline-flex;
  padding: 4px 10px;
  border-radius: 999px;
  background: #edf4ef;
  color: #2f664a;
  font-size: 12px;
}

.highlight-summary {
  margin: 16px 0 10px;
  color: #37423d;
}

.highlight-action,
.highlight-footer span {
  color: #6b7d73;
  font-size: 13px;
}

.task-actions,
.tag-group,
.filter-bar {
  gap: 8px;
}

.filter-bar {
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.pagination-wrap {
  margin-top: 18px;
  display: flex;
  justify-content: flex-end;
}

.insight-grid {
  grid-template-columns: 1.2fr 1fr;
}

.review-grid,
.analysis-grid,
.advice-grid {
  display: grid;
  gap: 14px;
}

.review-grid {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.analysis-grid {
  grid-template-columns: repeat(3, minmax(0, 1fr));
}

.review-item,
.analysis-item,
.advice-item {
  padding: 16px;
  border-radius: 16px;
  background: #f8fbf8;
  border: 1px solid #edf1ed;
}

.review-item span,
.analysis-item span,
.analysis-item em,
.advice-item span {
  display: block;
  color: #6b7d73;
}

.review-item strong,
.analysis-item strong {
  display: block;
  margin: 8px 0;
  font-size: 24px;
  color: #18392b;
}

.analysis-item em {
  font-style: normal;
  font-size: 13px;
}

.advice-grid {
  grid-template-columns: repeat(3, minmax(0, 1fr));
}

.advice-item p {
  margin: 12px 0 8px;
  color: #37423d;
}

.task-detail {
  padding: 0 4px 24px;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #edf1ed;
}

.detail-row span,
.detail-block h4 {
  color: #6b7d73;
}

.detail-block {
  margin-top: 20px;
}

.detail-block p {
  color: #2f3833;
  line-height: 1.7;
}

@media (max-width: 1200px) {
  .task-stat-grid,
  .highlight-grid,
  .insight-grid,
  .analysis-grid,
  .advice-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .hero-panel {
    padding: 22px;
    flex-direction: column;
    align-items: flex-start;
  }

  .review-grid {
    grid-template-columns: 1fr;
  }
}
</style>

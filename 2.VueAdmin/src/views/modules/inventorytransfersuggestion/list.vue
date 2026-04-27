<template>
  <div class="page-container">
    <el-card shadow="never">
      <div slot="header" class="header-row">
        <span>库存调拨建议</span>
        <el-button type="primary" icon="el-icon-refresh" @click="generate">生成建议</el-button>
      </div>
      <el-table :data="dataList" stripe v-loading="loading">
        <el-table-column prop="productname" label="茶品" min-width="140" />
        <el-table-column prop="sourcewarehousename" label="来源仓" min-width="130" />
        <el-table-column prop="targetwarehousename" label="目标仓" min-width="130" />
        <el-table-column prop="suggestedquantity" label="建议量(kg)" width="110" />
        <el-table-column prop="reason" label="原因" min-width="220" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="enterpriseaccount" label="企业账号" width="120" />
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" :disabled="row.status === '已确认'" @click="confirm(row)">确认</el-button>
            <el-button type="text" class="danger-text" @click="remove(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager"><el-pagination background layout="total, prev, pager, next, sizes" :current-page="pageIndex" :page-size="pageSize" :page-sizes="[10,20,30,50]" :total="total" @current-change="pageIndex = $event; fetchList()" @size-change="pageSize = $event; pageIndex = 1; fetchList()" /></div>
    </el-card>

    <el-dialog title="本次生成的调拨建议" :visible.sync="generatedVisible" width="760px">
      <el-table :data="generatedList" stripe>
        <el-table-column prop="productName" label="茶品" />
        <el-table-column prop="sourceWarehouseName" label="来源仓" />
        <el-table-column prop="targetWarehouseName" label="目标仓" />
        <el-table-column prop="suggestedQuantity" label="建议量(kg)" width="120" />
      </el-table>
      <span slot="footer"><el-button type="primary" @click="generatedVisible = false">关闭</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return { loading: false, dataList: [], pageIndex: 1, pageSize: 10, total: 0, generatedVisible: false, generatedList: [] }
  },
  created() {
    this.fetchList()
  },
  methods: {
    fetchList() {
      this.loading = true
      this.$http.get('inventoryTransferSuggestion/page', { params: { page: this.pageIndex, limit: this.pageSize, sort: 'id', order: 'desc' } }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.total = data.data.total || 0
        }
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    generate() {
      this.$http.get('inventoryTransferSuggestion/generate').then(({ data }) => {
        if (data && data.code === 0) {
          this.generatedList = data.data || []
          this.generatedVisible = true
        }
      })
    },
    confirm(row) {
      this.$http.post(`inventoryTransferSuggestion/confirm/${row.id}`).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message.success('已确认')
          this.fetchList()
        }
      })
    },
    remove(id) {
      this.$confirm('确定删除该建议吗？', '提示', { type: 'warning' }).then(() => {
        this.$http.post('inventoryTransferSuggestion/delete', [id]).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('删除成功')
            this.fetchList()
          }
        })
      })
    }
  }
}
</script>

<style scoped>
.page-container { padding: 4px; }
.header-row { display: flex; justify-content: space-between; align-items: center; }
.pager { margin-top: 16px; text-align: right; }
.danger-text { color: #f56c6c; }
</style>

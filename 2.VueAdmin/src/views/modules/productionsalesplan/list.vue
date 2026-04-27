<template>
  <div class="page-container">
    <el-card shadow="never">
      <div slot="header" class="header-row">
        <span>年度产销计划</span>
        <el-button type="primary" @click="openDialog()">新增计划</el-button>
      </div>
      <el-form :inline="true" :model="searchForm" class="search-row">
        <el-form-item label="年份"><el-input v-model="searchForm.planyear" clearable /></el-form-item>
        <el-form-item label="茶品"><el-input v-model="searchForm.productname" clearable /></el-form-item>
        <el-form-item><el-button type="primary" @click="fetchList">查询</el-button><el-button @click="resetSearch">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="dataList" stripe v-loading="loading">
        <el-table-column prop="planyear" label="年份" width="80" />
        <el-table-column prop="planmonth" label="月份" width="80" />
        <el-table-column prop="productname" label="茶品" min-width="150" />
        <el-table-column prop="teatype" label="茶类" width="120" />
        <el-table-column prop="plannedoutput" label="计划产量" width="110" />
        <el-table-column prop="plannedsales" label="计划销量" width="110" />
        <el-table-column prop="targetinventory" label="目标库存" width="110" />
        <el-table-column prop="plannedrevenue" label="计划收入" width="110" />
        <el-table-column prop="risklevel" label="风险" width="100" />
        <el-table-column prop="enterpriseaccount" label="企业账号" width="120" />
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" @click="openDialog(row)">编辑</el-button>
            <el-button type="text" class="danger-text" @click="remove(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager"><el-pagination background layout="total, prev, pager, next, sizes" :current-page="pageIndex" :page-size="pageSize" :page-sizes="[10,20,30,50]" :total="total" @current-change="pageIndex = $event; fetchList()" @size-change="pageSize = $event; pageIndex = 1; fetchList()" /></div>
    </el-card>

    <el-dialog :title="form.id ? '编辑计划' : '新增计划'" :visible.sync="dialogVisible" width="760px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="8"><el-form-item label="年份" prop="planyear"><el-input-number v-model="form.planyear" :min="2024" style="width:100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="月份" prop="planmonth"><el-input-number v-model="form.planmonth" :min="1" :max="12" style="width:100%" /></el-form-item></el-col>
          <el-col :span="8">
            <el-form-item label="企业账号" prop="enterpriseaccount">
              <el-select v-model="form.enterpriseaccount" :disabled="isEnterprise" filterable clearable style="width:100%" @change="onEnterpriseChange">
                <el-option v-for="item in enterpriseOptions" :key="item.zhanghao" :label="enterpriseLabel(item)" :value="item.zhanghao" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="茶品名称" prop="productid">
              <el-select v-model="form.productid" filterable clearable style="width:100%" placeholder="请选择已有茶品" @change="onProductChange">
                <el-option v-for="item in productOptions" :key="item.id" :label="productLabel(item)" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="茶类"><el-input v-model="form.teatype" disabled /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="计划产量"><el-input-number v-model="form.plannedoutput" :min="0" :precision="2" style="width:100%" @change="refreshRiskLevel" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="计划销量"><el-input-number v-model="form.plannedsales" :min="0" :precision="2" style="width:100%" @change="refreshRiskLevel" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="当前库存"><el-input-number v-model="form.currentstock" disabled :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="目标库存"><el-input-number v-model="form.targetinventory" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="计划收入"><el-input-number v-model="form.plannedrevenue" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="风险等级"><el-select v-model="form.risklevel" style="width:100%"><el-option label="正常" value="normal" /><el-option label="预警" value="warning" /></el-select></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="备注"><el-input type="textarea" v-model="form.remark" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">保存</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
const emptyForm = () => ({
  id: null,
  planyear: new Date().getFullYear(),
  planmonth: new Date().getMonth() + 1,
  enterpriseaccount: '',
  productid: null,
  productname: '',
  teatype: '',
  plannedoutput: 0,
  plannedsales: 0,
  currentstock: 0,
  targetinventory: 0,
  plannedrevenue: 0,
  risklevel: 'normal',
  remark: ''
})

export default {
  data() {
    return {
      loading: false,
      dataList: [],
      enterpriseOptions: [],
      productOptions: [],
      pageIndex: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      form: emptyForm(),
      searchForm: { planyear: '', productname: '' },
      rules: {
        planyear: [{ required: true, message: '请输入年份', trigger: 'blur' }],
        planmonth: [{ required: true, message: '请输入月份', trigger: 'blur' }],
        productid: [{ required: true, message: '请选择茶品', trigger: 'change' }],
        enterpriseaccount: [{ required: true, message: '请选择企业账号', trigger: 'change' }]
      }
    }
  },
  computed: {
    isEnterprise() {
      return this.$storage.get('role') === '商家'
    }
  },
  created() {
    this.fetchList()
    this.loadEnterprises()
    this.loadProducts()
  },
  methods: {
    enterpriseLabel(item) {
      return item.shangjiaxingming ? `${item.zhanghao}（${item.shangjiaxingming}）` : item.zhanghao
    },
    productLabel(item) {
      return `${item.shangpinmingcheng || ''}${item.shangpinfenlei ? ' / ' + item.shangpinfenlei : ''}`
    },
    loadEnterprises() {
      if (this.isEnterprise) {
        const account = this.$storage.get('adminName') || ''
        this.enterpriseOptions = account ? [{ zhanghao: account }] : []
        return
      }
      this.$http.get('shangjia/page', { params: { page: 1, limit: 1000, sort: 'id', order: 'desc' } }).then(({ data }) => {
        if (data && data.code === 0) this.enterpriseOptions = data.data.list || []
      })
    },
    loadProducts(account) {
      const params = { page: 1, limit: 1000, sort: 'id', order: 'desc' }
      if (account) params.zhanghao = account
      this.$http.get('shangpinxinxi/page', { params }).then(({ data }) => {
        if (data && data.code === 0) this.productOptions = data.data.list || []
      })
    },
    onEnterpriseChange(account) {
      this.form.productid = null
      this.form.productname = ''
      this.form.teatype = ''
      this.form.currentstock = 0
      this.loadProducts(account)
    },
    onProductChange(productid) {
      const product = this.productOptions.find(item => item.id === productid)
      if (!product) return
      this.form.productname = product.shangpinmingcheng || ''
      this.form.teatype = product.shangpinfenlei || ''
      if (!this.isEnterprise && product.zhanghao) this.form.enterpriseaccount = product.zhanghao
      this.loadCurrentStock()
    },
    loadCurrentStock() {
      if (!this.form.productname) {
        this.form.currentstock = 0
        this.refreshRiskLevel()
        return
      }
      this.$http.get('productionSalesPlan/stockSummary', {
        params: {
          productname: this.form.productname,
          enterpriseaccount: this.form.enterpriseaccount
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.form.currentstock = Number((data.data && data.data.currentStock) || 0)
          this.refreshRiskLevel()
        }
      }).catch(() => {
        this.loadCurrentStockFromInventoryRecords()
      })
    },
    loadCurrentStockFromInventoryRecords() {
      const params = {
        page: 1,
        limit: 1000,
        productname: this.form.productname
      }
      if (this.form.enterpriseaccount) params.enterpriseaccount = this.form.enterpriseaccount
      this.$http.get('inventoryrecord/page', { params }).then(({ data }) => {
        if (data && data.code === 0) {
          const rows = data.data.list || []
          this.form.currentstock = rows.reduce((sum, item) => sum + Number(item.currentstock || 0), 0)
          this.refreshRiskLevel()
        }
      }).catch(() => {
        this.form.currentstock = 0
        this.refreshRiskLevel()
        this.$message.warning('当前库存读取失败，请确认供应库存中已维护该茶品库存')
      })
    },
    refreshRiskLevel() {
      const output = Number(this.form.plannedoutput || 0)
      const sales = Number(this.form.plannedsales || 0)
      const currentStock = Number(this.form.currentstock || 0)
      this.form.risklevel = sales > output + currentStock ? 'warning' : 'normal'
    },
    fetchList() {
      this.loading = true
      const params = { page: this.pageIndex, limit: this.pageSize, sort: 'id', order: 'desc' }
      if (this.searchForm.planyear) params.planyear = this.searchForm.planyear
      if (this.searchForm.productname) params.productname = `%${this.searchForm.productname}%`
      this.$http.get('productionSalesPlan/page', { params }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.total = data.data.total || 0
        }
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    resetSearch() {
      this.searchForm = { planyear: '', productname: '' }
      this.pageIndex = 1
      this.fetchList()
    },
    openDialog(row) {
      this.form = Object.assign(emptyForm(), row || {})
      if (!row && this.isEnterprise) this.form.enterpriseaccount = this.$storage.get('adminName') || ''
      this.loadProducts(this.form.enterpriseaccount)
      if (row && this.form.productname) this.loadCurrentStock()
      this.dialogVisible = true
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        const url = this.form.id ? 'productionSalesPlan/update' : 'productionSalesPlan/save'
        const payload = Object.assign({}, this.form)
        delete payload.currentstock
        this.$http.post(url, payload).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.fetchList()
          } else {
            this.$message.error((data && data.msg) || '保存失败')
          }
        }).catch(error => {
          this.$message.error((error.response && error.response.data && error.response.data.msg) || '保存失败，请检查后端接口是否已启动')
        })
      })
    },
    remove(id) {
      this.$confirm('确定删除该计划吗？', '提示', { type: 'warning' }).then(() => {
        this.$http.post('productionSalesPlan/delete', [id]).then(({ data }) => {
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
.search-row { margin-bottom: 12px; }
.pager { margin-top: 16px; text-align: right; }
.danger-text { color: #f56c6c; }
</style>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <div slot="header" class="header-row">
        <span>库存台账管理</span>
        <div>
          <el-button type="primary" @click="openDialog()">新增台账</el-button>
          <el-button type="danger" :disabled="!selection.length" @click="removeSelection">批量删除</el-button>
        </div>
      </div>
      <el-form :inline="true" :model="searchForm" class="search-row">
        <el-form-item label="批次编号"><el-input v-model="searchForm.batchcode" clearable /></el-form-item>
        <el-form-item label="茶品名称"><el-input v-model="searchForm.productname" clearable /></el-form-item>
        <el-form-item label="记录类型">
          <el-select v-model="searchForm.recordtype" clearable>
            <el-option label="入库" value="入库" />
            <el-option label="出库" value="出库" />
            <el-option label="盘点" value="盘点" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="dataList" stripe v-loading="loading" @selection-change="selection = $event">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="batchcode" label="批次编号" width="140" />
        <el-table-column prop="productname" label="茶品名称" min-width="150" />
        <el-table-column prop="recordtype" label="记录类型" width="100" />
        <el-table-column prop="changestock" label="变动数量" width="100" />
        <el-table-column prop="currentstock" label="当前库存" width="100" />
        <el-table-column prop="warningstock" label="预警库存" width="100" />
        <el-table-column prop="warehousename" label="仓库" width="130" />
        <el-table-column prop="recordtime" label="记录时间" width="180" />
        <el-table-column prop="enterpriseaccount" label="企业账号" width="120" />
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" @click="openDialog(row, true)">详情</el-button>
            <el-button type="text" @click="openDialog(row)">编辑</el-button>
            <el-button type="text" class="danger-text" @click="removeSelection([row.id])">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager">
        <el-pagination background layout="total, prev, pager, next, sizes" :current-page="pageIndex" :page-size="pageSize" :page-sizes="[10,20,30,50]" :total="total" @current-change="pageIndex = $event; fetchList()" @size-change="pageSize = $event; pageIndex = 1; fetchList()" />
      </div>
    </el-card>

    <el-dialog :title="readOnly ? '台账详情' : (form.id ? '编辑台账' : '新增台账')" :visible.sync="dialogVisible" width="720px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="批次编号" prop="batchcode">
              <el-select v-model="form.batchcode" :disabled="readOnly" filterable clearable style="width:100%" @change="onBatchChange">
                <el-option v-for="item in batchOptions" :key="item.batchcode" :label="batchLabel(item)" :value="item.batchcode" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业账号" prop="enterpriseaccount">
              <el-select v-model="form.enterpriseaccount" :disabled="readOnly || isEnterprise" filterable clearable style="width:100%" @change="onEnterpriseChange">
                <el-option v-for="item in enterpriseOptions" :key="item.zhanghao" :label="enterpriseLabel(item)" :value="item.zhanghao" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="茶品名称" prop="productname">
              <el-select v-model="form.productname" :disabled="readOnly" filterable clearable style="width:100%" placeholder="请选择已有茶品" @change="onProductChange">
                <el-option v-for="item in productOptions" :key="item.id" :label="productLabel(item)" :value="item.shangpinmingcheng" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="仓库" prop="warehousecode">
              <div class="warehouse-select-row">
                <el-select v-model="form.warehousecode" :disabled="readOnly" filterable clearable style="width:100%" placeholder="请选择仓库" @change="onWarehouseChange">
                  <el-option v-for="item in warehouseOptions" :key="item.warehousecode" :label="warehouseLabel(item)" :value="item.warehousecode" />
                </el-select>
                <el-button v-if="!readOnly" type="text" @click="goWarehouse">新增仓库</el-button>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="记录类型" prop="recordtype"><el-select v-model="form.recordtype" :disabled="readOnly" style="width:100%"><el-option label="入库" value="入库" /><el-option label="出库" value="出库" /><el-option label="盘点" value="盘点" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="仓库名称"><el-input v-model="form.warehousename" disabled /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="变动数量"><el-input-number v-model="form.changestock" :disabled="readOnly" :precision="2" style="width:100%" @change="calculateCurrentStock" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="当前库存"><el-input-number v-model="form.currentstock" :disabled="true" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="预警库存"><el-input-number v-model="form.warningstock" :disabled="readOnly" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="记录时间"><el-date-picker v-model="form.recordtime" :disabled="readOnly" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="关联订单号"><el-input v-model="form.relatedorderid" :disabled="readOnly" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="备注"><el-input type="textarea" :rows="4" v-model="form.remark" :disabled="readOnly" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">{{ readOnly ? '关闭' : '取消' }}</el-button>
        <el-button v-if="!readOnly" type="primary" @click="submitForm">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
const emptyForm = () => ({
  id: null,
  batchcode: '',
  productname: '',
  recordtype: '入库',
  changestock: 0,
  currentstock: 0,
  warningstock: 0,
  warehousecode: '',
  warehousename: '',
  recordtime: '',
  enterpriseaccount: '',
  relatedorderid: '',
  remark: ''
})

export default {
  data() {
    return {
      loading: false,
      dataList: [],
      enterpriseOptions: [],
      batchOptions: [],
      productOptions: [],
      warehouseOptions: [],
      selection: [],
      dialogVisible: false,
      readOnly: false,
      pageIndex: 1,
      pageSize: 10,
      total: 0,
      form: emptyForm(),
      searchForm: { batchcode: '', productname: '', recordtype: '' },
      rules: {
        batchcode: [{ required: true, message: '请输入批次编号', trigger: 'blur' }],
        productname: [{ required: true, message: '请选择茶品名称', trigger: 'change' }],
        warehousecode: [{ required: true, message: '请选择仓库', trigger: 'change' }],
        recordtype: [{ required: true, message: '请选择记录类型', trigger: 'change' }],
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
    this.loadBatches()
    this.loadProducts()
    this.loadWarehouses()
  },
  methods: {
    enterpriseLabel(item) {
      return item.shangjiaxingming ? `${item.zhanghao}（${item.shangjiaxingming}）` : item.zhanghao
    },
    productLabel(item) {
      return `${item.shangpinmingcheng || ''}${item.shangpinfenlei ? ' / ' + item.shangpinfenlei : ''}`
    },
    batchLabel(item) {
      return `${item.batchcode || ''}${item.productname ? ' / ' + item.productname : ''}`
    },
    warehouseLabel(item) {
      return `${item.warehousecode || ''}${item.warehousename ? ' / ' + item.warehousename : ''}`
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
    loadBatches(account) {
      const params = { page: 1, limit: 1000, sort: 'id', order: 'desc' }
      if (account) params.enterpriseaccount = account
      this.$http.get('teabatch/page', { params }).then(({ data }) => {
        if (data && data.code === 0) this.batchOptions = data.data.list || []
      })
    },
    loadWarehouses(account) {
      const params = { page: 1, limit: 1000, sort: 'id', order: 'desc' }
      if (account) params.enterpriseaccount = account
      this.$http.get('warehouse/page', { params }).then(({ data }) => {
        if (data && data.code === 0) this.warehouseOptions = data.data.list || []
      })
    },
    onEnterpriseChange(account) {
      this.form.productname = ''
      this.form.batchcode = ''
      this.form.warehousecode = ''
      this.form.warehousename = ''
      this.form.currentstock = 0
      this.loadBatches(account)
      this.loadProducts(account)
      this.loadWarehouses(account)
    },
    onBatchChange(batchcode) {
      const batch = this.batchOptions.find(item => item.batchcode === batchcode)
      if (!batch) return
      this.form.productname = batch.productname || ''
      if (!this.isEnterprise && batch.enterpriseaccount) {
        this.form.enterpriseaccount = batch.enterpriseaccount
        this.loadProducts(batch.enterpriseaccount)
        this.loadWarehouses(batch.enterpriseaccount)
      }
      this.calculateCurrentStock()
    },
    onProductChange(productname) {
      const product = this.productOptions.find(item => item.shangpinmingcheng === productname)
      if (!product) return
      if (!this.isEnterprise && product.zhanghao) {
        this.form.enterpriseaccount = product.zhanghao
        this.loadWarehouses(product.zhanghao)
      }
    },
    onWarehouseChange(warehousecode) {
      const warehouse = this.warehouseOptions.find(item => item.warehousecode === warehousecode)
      if (!warehouse) return
      this.form.warehousename = warehouse.warehousename || ''
      if (!this.isEnterprise && warehouse.enterpriseaccount) this.form.enterpriseaccount = warehouse.enterpriseaccount
      this.calculateCurrentStock()
    },
    goWarehouse() {
      this.dialogVisible = false
      this.$router.push('/warehouse')
    },
    fetchList() {
      this.loading = true
      const params = { page: this.pageIndex, limit: this.pageSize, sort: 'id', order: 'desc' }
      Object.keys(this.searchForm).forEach(key => {
        if (this.searchForm[key]) {
          params[key] = key === 'recordtype' ? this.searchForm[key] : `%${this.searchForm[key]}%`
        }
      })
      this.$http.get('inventoryrecord/page', { params }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.total = data.data.total || 0
        }
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    resetSearch() {
      this.searchForm = { batchcode: '', productname: '', recordtype: '' }
      this.pageIndex = 1
      this.fetchList()
    },
    openDialog(row, readOnly = false) {
      this.readOnly = readOnly
      this.dialogVisible = true
      if (!row) {
        this.form = emptyForm()
        if (this.isEnterprise) this.form.enterpriseaccount = this.$storage.get('adminName') || ''
        this.loadBatches(this.form.enterpriseaccount)
        this.loadProducts(this.form.enterpriseaccount)
        this.loadWarehouses(this.form.enterpriseaccount)
        this.$nextTick(() => this.$refs.formRef && this.$refs.formRef.clearValidate())
        return
      }
      this.$http.get(`inventoryrecord/info/${row.id}`).then(({ data }) => {
        if (data && data.code === 0) {
          this.form = Object.assign(emptyForm(), data.data)
          this.loadBatches(this.form.enterpriseaccount)
          this.loadProducts(this.form.enterpriseaccount)
          this.loadWarehouses(this.form.enterpriseaccount)
        }
      })
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        this.calculateCurrentStock()
        if (Number(this.form.currentstock || 0) < 0) {
          this.$message.error('库存不能为负数')
          return
        }
        const url = this.form.id ? 'inventoryrecord/update' : 'inventoryrecord/save'
        this.$http.post(url, this.form).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.fetchList()
          }
        })
      })
    },
    calculateCurrentStock() {
      if (!this.form.batchcode || !this.form.warehousecode) {
        this.form.currentstock = Number(this.form.changestock || 0)
        return
      }
      const params = {
        page: 1,
        limit: 1,
        sort: 'recordtime',
        order: 'desc',
        batchcode: this.form.batchcode,
        warehousecode: this.form.warehousecode
      }
      if (this.form.enterpriseaccount) params.enterpriseaccount = this.form.enterpriseaccount
      this.$http.get('inventoryrecord/page', { params }).then(({ data }) => {
        const rows = data && data.code === 0 ? (data.data.list || []) : []
        const latest = rows.find(item => item.id !== this.form.id)
        const baseStock = latest ? Number(latest.currentstock || 0) : 0
        this.form.currentstock = baseStock + Number(this.form.changestock || 0)
      }).catch(() => {
        this.form.currentstock = Number(this.form.changestock || 0)
      })
    },
    removeSelection(ids) {
      const targetIds = ids || this.selection.map(item => item.id)
      if (!targetIds.length) return
      this.$confirm('确定删除选中的库存台账吗？', '提示', { type: 'warning' }).then(() => {
        this.$http.post('inventoryrecord/delete', targetIds).then(({ data }) => {
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
.warehouse-select-row { display: flex; align-items: center; gap: 10px; }
.warehouse-select-row .el-button { flex: 0 0 auto; padding: 0; }
</style>

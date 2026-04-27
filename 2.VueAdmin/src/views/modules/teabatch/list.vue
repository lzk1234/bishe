<template>
  <div class="page-container">
    <el-card shadow="never">
      <div slot="header" class="header-row">
        <span>生产批次管理</span>
        <div>
          <el-button type="primary" @click="openDialog()">新增批次</el-button>
          <el-button type="danger" :disabled="!selection.length" @click="removeSelection">批量删除</el-button>
        </div>
      </div>
      <el-form :inline="true" :model="searchForm" class="search-row">
        <el-form-item label="批次编号"><el-input v-model="searchForm.batchcode" clearable /></el-form-item>
        <el-form-item label="茶品名称"><el-input v-model="searchForm.productname" clearable /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="dataList" stripe v-loading="loading" @selection-change="selection = $event">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="batchcode" label="批次编号" width="140" />
        <el-table-column prop="basename" label="基地名称" min-width="160" />
        <el-table-column prop="productname" label="茶品名称" min-width="150" />
        <el-table-column label="茶类" width="100">
          <template slot-scope="{ row }">{{ teaTypeLabel(row.teatype) }}</template>
        </el-table-column>
        <el-table-column prop="pickingdate" label="采摘日期" width="120" />
        <el-table-column prop="freshweight" label="鲜叶重量" width="100" />
        <el-table-column prop="finishedweight" label="成品重量" width="100" />
        <el-table-column label="批次状态" width="110">
          <template slot-scope="{ row }">{{ batchStatusLabel(row.batchstatus) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" @click="openDialog(row, true)">详情</el-button>
            <el-button type="text" @click="openDialog(row)">编辑</el-button>
            <el-button type="text" class="danger-text" @click="removeSelection([row.id])">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager">
        <el-pagination
          background
          layout="total, prev, pager, next, sizes"
          :current-page="pageIndex"
          :page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          @current-change="pageIndex = $event; fetchList()"
          @size-change="pageSize = $event; pageIndex = 1; fetchList()"
        />
      </div>
    </el-card>

    <el-dialog :title="readOnly ? '批次详情' : (form.id ? '编辑批次' : '新增批次')" :visible.sync="dialogVisible" width="680px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="批次编号" prop="batchcode"><el-input v-model="form.batchcode" :disabled="readOnly" /></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="基地名称" prop="basename">
              <el-select v-model="form.basename" :disabled="readOnly" filterable clearable style="width:100%" @change="onBaseChange">
                <el-option v-for="item in baseOptions" :key="item.id" :label="baseLabel(item)" :value="item.basename" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="茶品名称" prop="productname">
              <el-select v-model="form.productname" :disabled="readOnly" filterable clearable style="width:100%" @change="onProductChange">
                <el-option v-for="item in productOptions" :key="item.id" :label="productLabel(item)" :value="item.shangpinmingcheng" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="茶类"><el-input v-model="form.teatype" disabled /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="采摘日期"><el-date-picker v-model="form.pickingdate" :disabled="readOnly" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="海拔(m)"><el-input-number v-model="form.altitude" disabled :min="0" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="鲜叶重量"><el-input-number v-model="form.freshweight" :disabled="readOnly" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="成品重量"><el-input-number v-model="form.finishedweight" :disabled="readOnly" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="加工方式"><el-input v-model="form.processmethod" :disabled="readOnly" /></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="批次状态">
              <el-select v-model="form.batchstatus" :disabled="readOnly" style="width:100%">
                <el-option label="待采摘" value="待采摘" />
                <el-option label="加工完成" value="加工完成" />
                <el-option label="已入库" value="已入库" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="企业账号" prop="enterpriseaccount">
              <el-select v-model="form.enterpriseaccount" :disabled="readOnly || isEnterprise" filterable clearable style="width:100%" @change="onEnterpriseChange">
                <el-option v-for="item in enterpriseOptions" :key="item.zhanghao" :label="enterpriseLabel(item)" :value="item.zhanghao" />
              </el-select>
            </el-form-item>
          </el-col>
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
  basename: '',
  productname: '',
  teatype: '',
  pickingdate: '',
  freshweight: 0,
  processmethod: '',
  finishedweight: 0,
  batchstatus: '待采摘',
  altitude: null,
  enterpriseaccount: '',
  remark: ''
})

export default {
  data() {
    return {
      loading: false,
      dataList: [],
      enterpriseOptions: [],
      baseOptions: [],
      productOptions: [],
      selection: [],
      dialogVisible: false,
      readOnly: false,
      pageIndex: 1,
      pageSize: 10,
      total: 0,
      form: emptyForm(),
      searchForm: { batchcode: '', productname: '' },
      rules: {
        batchcode: [{ required: true, message: '请输入批次编号', trigger: 'blur' }],
        basename: [{ required: true, message: '请输入基地名称', trigger: 'blur' }],
        productname: [{ required: true, message: '请输入茶品名称', trigger: 'blur' }],
        enterpriseaccount: [{ required: true, message: '请输入企业账号', trigger: 'blur' }]
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
    this.loadBases()
    this.loadProducts()
  },
  methods: {
    enterpriseLabel(item) {
      return item.shangjiaxingming ? `${item.zhanghao} / ${item.shangjiaxingming}` : item.zhanghao
    },
    baseLabel(item) {
      return `${item.basename || ''}${item.location ? ' / ' + item.location : ''}`
    },
    productLabel(item) {
      return `${item.shangpinmingcheng || ''}${item.shangpinfenlei ? ' / ' + item.shangpinfenlei : ''}`
    },
    batchStatusLabel(status) {
      const map = {
        pending_pick: '待采摘',
        processing_done: '加工完成',
        in_stock: '已入库',
        pending: '待采摘',
        processing: '加工完成'
      }
      return map[status] || status || '-'
    },
    teaTypeLabel(type) {
      const map = {
        green: '绿茶',
        dark: '黑茶',
        white: '白茶',
        oolong: '乌龙茶',
        black: '红茶',
        yellow: '黄茶',
        puer: '普洱茶'
      }
      return map[type] || type || '-'
    },
    fetchList() {
      this.loading = true
      const params = { page: this.pageIndex, limit: this.pageSize, sort: 'id', order: 'desc' }
      Object.keys(this.searchForm).forEach(key => {
        if (this.searchForm[key]) params[key] = `%${this.searchForm[key]}%`
      })
      this.$http.get('teabatch/page', { params }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.total = data.data.total || 0
        }
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    resetSearch() {
      this.searchForm = { batchcode: '', productname: '' }
      this.pageIndex = 1
      this.fetchList()
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
    loadBases(account) {
      const params = { page: 1, limit: 1000, sort: 'id', order: 'desc' }
      if (account) params.enterpriseaccount = account
      this.$http.get('teabase/page', { params }).then(({ data }) => {
        if (data && data.code === 0) this.baseOptions = data.data.list || []
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
      this.form.basename = ''
      this.form.productname = ''
      this.form.teatype = ''
      this.form.altitude = null
      this.loadBases(account)
      this.loadProducts(account)
    },
    onBaseChange(basename) {
      const base = this.baseOptions.find(item => item.basename === basename)
      if (!base) return
      this.form.altitude = base.altitude
      if (!this.isEnterprise && base.enterpriseaccount) {
        this.form.enterpriseaccount = base.enterpriseaccount
        this.loadProducts(base.enterpriseaccount)
      }
    },
    onProductChange(productname) {
      const product = this.productOptions.find(item => item.shangpinmingcheng === productname)
      if (!product) return
      this.form.teatype = product.shangpinfenlei || ''
      if (!this.isEnterprise && product.zhanghao) {
        this.form.enterpriseaccount = product.zhanghao
        this.loadBases(product.zhanghao)
      }
    },
    openDialog(row, readOnly = false) {
      this.readOnly = readOnly
      this.dialogVisible = true
      if (!row) {
        this.form = emptyForm()
        if (this.isEnterprise) this.form.enterpriseaccount = this.$storage.get('adminName') || ''
        this.loadBases(this.form.enterpriseaccount)
        this.loadProducts(this.form.enterpriseaccount)
        this.$nextTick(() => this.$refs.formRef && this.$refs.formRef.clearValidate())
        return
      }
      this.$http.get(`teabatch/info/${row.id}`).then(({ data }) => {
        if (data && data.code === 0) {
          this.form = Object.assign(emptyForm(), data.data)
          this.loadBases(this.form.enterpriseaccount)
          this.loadProducts(this.form.enterpriseaccount)
        }
      })
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        const url = this.form.id ? 'teabatch/update' : 'teabatch/save'
        this.$http.post(url, this.form).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.fetchList()
          }
        })
      })
    },
    removeSelection(ids) {
      const targetIds = ids || this.selection.map(item => item.id)
      if (!targetIds.length) return
      this.$confirm('确定删除选中的生产批次吗？', '提示', { type: 'warning' }).then(() => {
        this.$http.post('teabatch/delete', targetIds).then(({ data }) => {
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

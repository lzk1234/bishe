<template>
  <div class="page-container">
    <el-card shadow="never">
      <div slot="header" class="header-row">
        <span>茶品生命周期规则</span>
        <el-button type="primary" @click="openDialog()">新增规则</el-button>
      </div>
      <el-table :data="dataList" stripe v-loading="loading">
        <el-table-column prop="productname" label="茶品" min-width="150" />
        <el-table-column prop="teatype" label="茶类" width="120" />
        <el-table-column prop="newperioddays" label="新茶期" width="90" />
        <el-table-column prop="mainsaleperioddays" label="主销期" width="90" />
        <el-table-column prop="promotionperioddays" label="促销期" width="90" />
        <el-table-column prop="warningperioddays" label="风险期" width="90" />
        <el-table-column prop="strategynote" label="策略说明" min-width="200" show-overflow-tooltip />
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

    <el-card shadow="never" header="今日最佳销售期建议" class="status-card">
      <el-table :data="todayStatus.bestSaleWindowList || []" stripe>
        <el-table-column prop="productName" label="茶品" />
        <el-table-column prop="stageCode" label="阶段" width="100" />
        <el-table-column prop="ageDays" label="上架天数" width="100" />
        <el-table-column prop="actionLabel" label="建议动作" min-width="220" />
      </el-table>
    </el-card>

    <el-dialog :title="form.id ? '编辑规则' : '新增规则'" :visible.sync="dialogVisible" width="720px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12">
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
          <el-col :span="6"><el-form-item label="新茶期"><el-input-number v-model="form.newperioddays" :min="1" /></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="主销期"><el-input-number v-model="form.mainsaleperioddays" :min="1" /></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="促销期"><el-input-number v-model="form.promotionperioddays" :min="1" /></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="风险期"><el-input-number v-model="form.warningperioddays" :min="1" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="策略说明"><el-input type="textarea" v-model="form.strategynote" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">保存</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
const emptyForm = () => ({
  id: null,
  enterpriseaccount: '',
  productid: null,
  productname: '',
  teatype: '',
  newperioddays: 15,
  mainsaleperioddays: 45,
  promotionperioddays: 90,
  warningperioddays: 120,
  strategynote: '',
  enabled: 1
})

export default {
  data() {
    return {
      loading: false,
      dataList: [],
      todayStatus: {},
      enterpriseOptions: [],
      productOptions: [],
      pageIndex: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      form: emptyForm(),
      rules: {
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
    this.fetchStatus()
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
      this.loadProducts(account)
    },
    onProductChange(productid) {
      const product = this.productOptions.find(item => item.id === productid)
      if (!product) return
      this.form.productname = product.shangpinmingcheng || ''
      this.form.teatype = product.shangpinfenlei || ''
      if (!this.isEnterprise && product.zhanghao) this.form.enterpriseaccount = product.zhanghao
    },
    fetchList() {
      this.loading = true
      this.$http.get('productLifecycleRule/page', { params: { page: this.pageIndex, limit: this.pageSize, sort: 'id', order: 'desc' } }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.total = data.data.total || 0
        }
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    fetchStatus() {
      this.$http.get('productLifecycleRule/todayStatus').then(({ data }) => {
        if (data && data.code === 0) this.todayStatus = data.data || {}
      })
    },
    openDialog(row) {
      this.form = Object.assign(emptyForm(), row || {})
      if (!row && this.isEnterprise) this.form.enterpriseaccount = this.$storage.get('adminName') || ''
      this.loadProducts(this.form.enterpriseaccount)
      this.dialogVisible = true
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        const url = this.form.id ? 'productLifecycleRule/update' : 'productLifecycleRule/save'
        this.$http.post(url, this.form).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.fetchList()
            this.fetchStatus()
          }
        })
      })
    },
    remove(id) {
      this.$confirm('确定删除该规则吗？', '提示', { type: 'warning' }).then(() => {
        this.$http.post('productLifecycleRule/delete', [id]).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('删除成功')
            this.fetchList()
            this.fetchStatus()
          }
        })
      })
    }
  }
}
</script>

<style scoped>
.page-container { padding: 4px; display: grid; gap: 16px; }
.header-row { display: flex; justify-content: space-between; align-items: center; }
.pager { margin-top: 16px; text-align: right; }
.danger-text { color: #f56c6c; }
.status-card { margin-top: 0; }
</style>

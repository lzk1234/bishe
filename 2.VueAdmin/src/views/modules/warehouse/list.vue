<template>
  <div class="page-container">
    <el-card shadow="never">
      <div slot="header" class="header-row"><span>仓库档案</span><el-button type="primary" @click="openDialog()">新增仓库</el-button></div>
      <el-form :inline="true" :model="searchForm" class="search-row">
        <el-form-item label="仓库名称"><el-input v-model="searchForm.warehousename" clearable /></el-form-item>
        <el-form-item label="仓库类型"><el-input v-model="searchForm.warehousetype" clearable /></el-form-item>
        <el-form-item><el-button type="primary" @click="fetchList">查询</el-button><el-button @click="resetSearch">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="dataList" stripe v-loading="loading">
        <el-table-column prop="warehousecode" label="仓库编号" width="130" />
        <el-table-column prop="warehousename" label="仓库名称" min-width="150" />
        <el-table-column prop="warehousetype" label="类型" width="110" />
        <el-table-column prop="location" label="位置" min-width="160" />
        <el-table-column prop="principal" label="负责人" width="100" />
        <el-table-column prop="enterpriseaccount" label="企业账号" width="120" />
        <el-table-column prop="status" label="状态" width="90" />
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" @click="openDialog(row)">编辑</el-button>
            <el-button type="text" class="danger-text" @click="remove(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager"><el-pagination background layout="total, prev, pager, next, sizes" :current-page="pageIndex" :page-size="pageSize" :page-sizes="[10,20,30,50]" :total="total" @current-change="pageIndex = $event; fetchList()" @size-change="pageSize = $event; pageIndex = 1; fetchList()" /></div>
    </el-card>
    <el-dialog :title="form.id ? '编辑仓库' : '新增仓库'" :visible.sync="dialogVisible" width="640px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="仓库编号" prop="warehousecode"><el-input v-model="form.warehousecode" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="仓库名称" prop="warehousename"><el-input v-model="form.warehousename" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="仓库类型"><el-select v-model="form.warehousetype" style="width:100%"><el-option label="总仓" value="总仓" /><el-option label="电商仓" value="电商仓" /><el-option label="门店仓" value="门店仓" /><el-option label="合作仓" value="合作仓" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态"><el-select v-model="form.status" style="width:100%"><el-option label="启用" value="启用" /><el-option label="停用" value="停用" /></el-select></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="位置"><el-input v-model="form.location" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="负责人"><el-input v-model="form.principal" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系电话"><el-input v-model="form.contactphone" /></el-form-item></el-col>
          <el-col :span="24">
            <el-form-item label="企业账号" prop="enterpriseaccount">
              <el-select v-model="form.enterpriseaccount" :disabled="isEnterprise" filterable clearable style="width:100%">
                <el-option v-for="item in enterpriseOptions" :key="item.zhanghao" :label="enterpriseLabel(item)" :value="item.zhanghao" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24"><el-form-item label="备注"><el-input type="textarea" v-model="form.remark" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">保存</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
const emptyForm = () => ({ id: null, warehousecode: '', warehousename: '', warehousetype: '总仓', location: '', principal: '', contactphone: '', enterpriseaccount: '', status: '启用', remark: '' })
export default {
  data() {
    return {
      loading: false,
      dataList: [],
      enterpriseOptions: [],
      pageIndex: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      form: emptyForm(),
      searchForm: { warehousename: '', warehousetype: '' },
      rules: {
        warehousecode: [{ required: true, message: '请输入仓库编号', trigger: 'blur' }],
        warehousename: [{ required: true, message: '请输入仓库名称', trigger: 'blur' }],
        enterpriseaccount: [{ required: true, message: '请选择企业账号', trigger: 'change' }]
      }
    }
  },
  computed: {
    isEnterprise() { return this.$storage.get('role') === '商家' }
  },
  created() {
    this.fetchList()
    this.loadEnterprises()
  },
  methods: {
    enterpriseLabel(item) {
      return item.shangjiaxingming ? `${item.zhanghao}（${item.shangjiaxingming}）` : item.zhanghao
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
    fetchList() {
      this.loading = true
      const params = { page: this.pageIndex, limit: this.pageSize, sort: 'id', order: 'desc' }
      if (this.searchForm.warehousename) params.warehousename = `%${this.searchForm.warehousename}%`
      if (this.searchForm.warehousetype) params.warehousetype = `%${this.searchForm.warehousetype}%`
      this.$http.get('warehouse/page', { params }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.total = data.data.total || 0
        }
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    resetSearch() {
      this.searchForm = { warehousename: '', warehousetype: '' }
      this.pageIndex = 1
      this.fetchList()
    },
    openDialog(row) {
      this.form = Object.assign(emptyForm(), row || {})
      if (!row && this.isEnterprise) this.form.enterpriseaccount = this.$storage.get('adminName') || ''
      this.dialogVisible = true
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        const url = this.form.id ? 'warehouse/update' : 'warehouse/save'
        this.$http.post(url, this.form).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.fetchList()
          }
        })
      })
    },
    remove(id) {
      this.$confirm('确定删除该仓库吗？', '提示', { type: 'warning' }).then(() => {
        this.$http.post('warehouse/delete', [id]).then(({ data }) => {
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

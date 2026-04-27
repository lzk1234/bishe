<template>
  <div class="page-container">
    <el-card shadow="never">
      <div slot="header" class="header-row">
        <span>茶园基地管理</span>
        <div>
          <el-button type="primary" @click="openDialog()">新增基地</el-button>
          <el-button type="danger" :disabled="!selection.length" @click="removeSelection">批量删除</el-button>
        </div>
      </div>
      <el-form :inline="true" :model="searchForm" class="search-row">
        <el-form-item label="基地名称"><el-input v-model="searchForm.basename" clearable /></el-form-item>
        <el-form-item label="基地位置"><el-input v-model="searchForm.location" clearable /></el-form-item>
        <el-form-item><el-button type="primary" @click="fetchList">查询</el-button><el-button @click="resetSearch">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="dataList" stripe v-loading="loading" @selection-change="selection = $event">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="basecode" label="基地编号" width="120" />
        <el-table-column prop="basename" label="基地名称" min-width="160" />
        <el-table-column prop="location" label="基地位置" min-width="180" />
        <el-table-column prop="altitude" label="海拔(m)" width="100" />
        <el-table-column prop="area" label="面积(亩)" width="100" />
        <el-table-column prop="principal" label="负责人" width="110" />
        <el-table-column prop="teatype" label="主栽茶类" width="120" />
        <el-table-column label="产区标签" width="140">
          <template slot-scope="{ row }">{{ row.regiontag || row.location || '-' }}</template>
        </el-table-column>
        <el-table-column label="年产能(kg)" width="120">
          <template slot-scope="{ row }">{{ row.annualcapacity == null ? 0 : row.annualcapacity }}</template>
        </el-table-column>
        <el-table-column label="主栽品种" width="120">
          <template slot-scope="{ row }">{{ row.mainvariety || row.teatype || '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template slot-scope="{ row }">{{ row.basestatus || '正常' }}</template>
        </el-table-column>
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

    <el-dialog :title="readOnly ? '基地详情' : (form.id ? '编辑基地' : '新增基地')" :visible.sync="dialogVisible" width="680px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="基地编号" prop="basecode"><el-input v-model="form.basecode" :disabled="readOnly" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="基地名称" prop="basename"><el-input v-model="form.basename" :disabled="readOnly" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="基地位置" prop="location"><el-input v-model="form.location" :disabled="readOnly" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="海拔(m)"><el-input-number v-model="form.altitude" :disabled="readOnly" :min="0" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="面积(亩)"><el-input-number v-model="form.area" :disabled="readOnly" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="负责人"><el-input v-model="form.principal" :disabled="readOnly" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系电话"><el-input v-model="form.contactphone" :disabled="readOnly" /></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="主栽茶类">
              <el-select v-model="form.teatype" :disabled="readOnly" filterable clearable style="width:100%">
                <el-option v-for="item in teaTypeOptions" :key="item.id" :label="item.shangpinfenlei" :value="item.shangpinfenlei" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="认证信息"><el-input v-model="form.certification" :disabled="readOnly" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="产区标签"><el-input v-model="form.regiontag" :disabled="readOnly" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="预计年产能"><el-input-number v-model="form.annualcapacity" :disabled="readOnly" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="主栽品种"><el-input v-model="form.mainvariety" :disabled="readOnly" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="建园年份"><el-input-number v-model="form.plantingyear" :disabled="readOnly" :min="1900" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="基地状态"><el-select v-model="form.basestatus" :disabled="readOnly" style="width:100%"><el-option label="正常" value="正常" /><el-option label="维护" value="维护" /><el-option label="停用" value="停用" /></el-select></el-form-item></el-col>
          <el-col :span="24">
            <el-form-item label="企业账号" prop="enterpriseaccount">
              <el-select v-model="form.enterpriseaccount" :disabled="readOnly || isEnterprise" filterable clearable style="width:100%">
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
  basecode: '',
  basename: '',
  location: '',
  altitude: null,
  area: 0,
  principal: '',
  contactphone: '',
  teatype: '',
  certification: '',
  enterpriseaccount: '',
  regiontag: '',
  annualcapacity: 0,
  mainvariety: '',
  plantingyear: null,
  basestatus: '正常',
  remark: ''
})

export default {
  data() {
    return {
      loading: false,
      dataList: [],
      enterpriseOptions: [],
      teaTypeOptions: [],
      selection: [],
      dialogVisible: false,
      readOnly: false,
      pageIndex: 1,
      pageSize: 10,
      total: 0,
      form: emptyForm(),
      searchForm: { basename: '', location: '' },
      rules: {
        basecode: [{ required: true, message: '请输入基地编号', trigger: 'blur' }],
        basename: [{ required: true, message: '请输入基地名称', trigger: 'blur' }],
        location: [{ required: true, message: '请输入基地位置', trigger: 'blur' }],
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
    this.loadTeaTypes()
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
    loadTeaTypes() {
      this.$http.get('shangpinfenlei/page', { params: { page: 1, limit: 1000, sort: 'id', order: 'desc' } }).then(({ data }) => {
        if (data && data.code === 0) this.teaTypeOptions = data.data.list || []
      })
    },
    fetchList() {
      this.loading = true
      const params = { page: this.pageIndex, limit: this.pageSize, sort: 'id', order: 'desc' }
      Object.keys(this.searchForm).forEach(key => {
        if (this.searchForm[key]) params[key] = `%${this.searchForm[key]}%`
      })
      this.$http.get('teabase/page', { params }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.total = data.data.total || 0
        }
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    resetSearch() {
      this.searchForm = { basename: '', location: '' }
      this.pageIndex = 1
      this.fetchList()
    },
    openDialog(row, readOnly = false) {
      this.readOnly = readOnly
      this.dialogVisible = true
      if (!row) {
        this.form = this.normalizeForm(emptyForm())
        if (this.isEnterprise) this.form.enterpriseaccount = this.$storage.get('adminName') || ''
        this.$nextTick(() => this.$refs.formRef && this.$refs.formRef.clearValidate())
        return
      }
      this.$http.get(`teabase/info/${row.id}`).then(({ data }) => {
        if (data && data.code === 0) this.form = this.normalizeForm(Object.assign(emptyForm(), data.data))
      })
    },
    normalizeForm(form) {
      form.regiontag = form.regiontag || form.location || ''
      form.annualcapacity = form.annualcapacity == null ? 0 : form.annualcapacity
      form.mainvariety = form.mainvariety || form.teatype || ''
      form.plantingyear = form.plantingyear || new Date().getFullYear()
      form.basestatus = form.basestatus || '正常'
      return form
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        this.form = this.normalizeForm(this.form)
        const url = this.form.id ? 'teabase/update' : 'teabase/save'
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
      this.$confirm('确定删除选中的基地记录吗？', '提示', { type: 'warning' }).then(() => {
        this.$http.post('teabase/delete', targetIds).then(({ data }) => {
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

<template>
  <div class="page-container">
    <el-card shadow="never">
      <div slot="header" class="header-row">
        <span>茶品档案</span>
        <div>
          <el-button type="primary" @click="openDialog()">新增茶品</el-button>
          <el-button type="danger" :disabled="!selection.length" @click="removeSelection">批量删除</el-button>
        </div>
      </div>

      <el-form :inline="true" :model="searchForm" class="search-row">
        <el-form-item label="茶品名称">
          <el-input v-model="searchForm.shangpinmingcheng" clearable placeholder="搜索茶品名称" />
        </el-form-item>
        <el-form-item label="茶类等级">
          <el-input v-model="searchForm.shangpinfenlei" clearable placeholder="搜索茶类等级" />
        </el-form-item>
        <el-form-item label="产地">
          <el-input v-model="searchForm.chandi" clearable placeholder="搜索产地" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="dataList" stripe v-loading="loading" @selection-change="selection = $event">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="shangpinmingcheng" label="茶品名称" min-width="160" />
        <el-table-column prop="shangpinfenlei" label="茶类等级" width="120" />
        <el-table-column prop="pinpai" label="品牌" width="120" />
        <el-table-column prop="chandi" label="产地" min-width="160" />
        <el-table-column prop="haiba" label="海拔(m)" width="100" />
        <el-table-column prop="shengchanpici" label="生产批次" width="140" />
        <el-table-column prop="alllimittimes" label="库存" width="80" />
        <el-table-column prop="price" label="销售价" width="100" />
        <el-table-column label="图片" width="100">
          <template slot-scope="{ row }">
            <img v-if="row.tupian" :src="imageUrl(row.tupian)" class="table-image" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
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

    <el-dialog :title="dialogModeTitle" :visible.sync="dialogVisible" width="760px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px" class="dialog-form">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="茶品名称" prop="shangpinmingcheng">
              <el-input v-model="form.shangpinmingcheng" :disabled="readOnly" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="茶类等级" prop="shangpinfenlei">
              <el-select v-model="form.shangpinfenlei" :disabled="readOnly" filterable style="width: 100%">
                <el-option v-for="item in categoryOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="品牌" prop="pinpai">
              <el-input v-model="form.pinpai" :disabled="readOnly" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业账号" prop="zhanghao">
              <el-select v-model="form.zhanghao" :disabled="readOnly || isEnterprise" filterable clearable style="width: 100%" @change="onEnterpriseChange">
                <el-option v-for="item in enterpriseOptions" :key="item.zhanghao" :label="enterpriseLabel(item)" :value="item.zhanghao" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产地" prop="chandi">
              <el-input v-model="form.chandi" :disabled="readOnly" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="海拔(m)" prop="haiba">
              <el-input-number v-model="form.haiba" :disabled="readOnly" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产批次" prop="shengchanpici">
              <el-select v-model="form.shengchanpici" :disabled="readOnly" filterable clearable style="width: 100%" @change="onBatchChange">
                <el-option v-for="item in batchOptions" :key="item.batchcode" :label="batchLabel(item)" :value="item.batchcode" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适饮场景" prop="shiyongchangjing">
              <el-input v-model="form.shiyongchangjing" :disabled="readOnly" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上架日期" prop="shangjiariqi">
              <el-date-picker v-model="form.shangjiariqi" :disabled="readOnly" type="date" value-format="yyyy-MM-dd" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库存" prop="alllimittimes">
              <el-input-number v-model="form.alllimittimes" :disabled="readOnly" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单次限购" prop="onelimittimes">
              <el-input-number v-model="form.onelimittimes" :disabled="readOnly" :min="-1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="销售价" prop="price">
              <el-input-number v-model="form.price" :disabled="readOnly" :min="0" :precision="2" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会员价" prop="vipprice">
              <el-input-number v-model="form.vipprice" :disabled="readOnly" :min="0" :precision="2" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="图片" prop="tupian">
              <file-upload
                v-if="!readOnly"
                action="file/upload"
                :limit="3"
                :multiple="true"
                :fileUrls="form.tupian || ''"
                @change="form.tupian = $event"
              />
              <div v-else class="image-preview-list">
                <img v-for="(img, index) in splitImages(form.tupian)" :key="index" :src="imageUrl(img)" class="preview-image" />
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="茶品介绍" prop="shangpinxiangqing">
              <editor v-if="!readOnly" v-model="form.shangpinxiangqing" action="file/upload" />
              <div v-else class="rich-preview" v-html="form.shangpinxiangqing"></div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">{{ readOnly ? '关闭' : '取消' }}</el-button>
        <el-button v-if="!readOnly" type="primary" @click="submitForm">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import FileUpload from '@/components/common/FileUpload'
import Editor from '@/components/common/Editor'

const emptyForm = () => ({
  id: null,
  shangpinmingcheng: '',
  shangpinfenlei: '',
  tupian: '',
  pinpai: '',
  zhanghao: '',
  shangjiariqi: '',
  shangpinxiangqing: '',
  onelimittimes: -1,
  alllimittimes: 0,
  price: 0,
  vipprice: 0,
  chandi: '',
  haiba: null,
  shengchanpici: '',
  shiyongchangjing: ''
})

export default {
  components: { FileUpload, Editor },
  data() {
    return {
      loading: false,
      dataList: [],
      selection: [],
      pageIndex: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      readOnly: false,
      form: emptyForm(),
      searchForm: {
        shangpinmingcheng: '',
        shangpinfenlei: '',
        chandi: ''
      },
      enterpriseOptions: [],
      batchOptions: [],
      categoryOptions: [],
      rules: {
        shangpinmingcheng: [{ required: true, message: '请输入茶品名称', trigger: 'blur' }],
        shangpinfenlei: [{ required: true, message: '请选择茶类等级', trigger: 'change' }],
        pinpai: [{ required: true, message: '请输入品牌', trigger: 'blur' }],
        zhanghao: [{ required: true, message: '请输入企业账号', trigger: 'blur' }],
        price: [{ required: true, message: '请输入销售价', trigger: 'blur' }]
      }
    }
  },
  computed: {
    isEnterprise() {
      return this.$storage.get('role') === '商家'
    },
    dialogModeTitle() {
      return this.readOnly ? '茶品详情' : (this.form.id ? '编辑茶品' : '新增茶品')
    }
  },
  created() {
    if (this.isEnterprise) {
      this.form.zhanghao = this.$storage.get('adminName') || ''
    }
    this.loadEnterprises()
    this.loadBatches(this.form.zhanghao)
    this.fetchCategories()
    this.fetchList()
  },
  methods: {
    enterpriseLabel(item) {
      return item.shangjiaxingming ? `${item.zhanghao} / ${item.shangjiaxingming}` : item.zhanghao
    },
    batchLabel(item) {
      return `${item.batchcode || ''}${item.productname ? ' / ' + item.productname : ''}`
    },
    imageUrl(value) {
      return this.$imageUrl(value)
    },
    splitImages(value) {
      return value ? value.split(',') : []
    },
    fetchCategories() {
      this.$http.get('option/shangpinfenlei/shangpinfenlei').then(({ data }) => {
        if (data && data.code === 0) {
          this.categoryOptions = data.data || []
        }
      })
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
    loadBatches(account) {
      const params = { page: 1, limit: 1000, sort: 'id', order: 'desc' }
      if (account) params.enterpriseaccount = account
      this.$http.get('teabatch/page', { params }).then(({ data }) => {
        if (data && data.code === 0) this.batchOptions = data.data.list || []
      })
    },
    onEnterpriseChange(account) {
      this.form.shengchanpici = ''
      this.loadBatches(account)
    },
    onBatchChange(batchcode) {
      const batch = this.batchOptions.find(item => item.batchcode === batchcode)
      if (!batch) return
      this.form.shengchanpici = batch.batchcode || ''
      this.form.chandi = batch.basename || ''
      this.form.haiba = batch.altitude == null ? this.form.haiba : batch.altitude
      if (batch.teatype) this.form.shangpinfenlei = batch.teatype
      if (!this.isEnterprise && batch.enterpriseaccount) {
        this.form.zhanghao = batch.enterpriseaccount
      }
    },
    fetchList() {
      this.loading = true
      const params = {
        page: this.pageIndex,
        limit: this.pageSize,
        sort: 'id',
        order: 'desc'
      }
      Object.keys(this.searchForm).forEach(key => {
        if (this.searchForm[key]) {
          params[key] = `%${this.searchForm[key]}%`
        }
      })
      this.$http.get('shangpinxinxi/page', { params }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.total = data.data.total || 0
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    resetSearch() {
      this.searchForm = {
        shangpinmingcheng: '',
        shangpinfenlei: '',
        chandi: ''
      }
      this.pageIndex = 1
      this.fetchList()
    },
    openDialog(row, readOnly = false) {
      this.readOnly = readOnly
      this.dialogVisible = true
      if (!row) {
        this.form = emptyForm()
        if (this.isEnterprise) {
          this.form.zhanghao = this.$storage.get('adminName') || ''
        }
        this.loadBatches(this.form.zhanghao)
        this.$nextTick(() => this.$refs.formRef && this.$refs.formRef.clearValidate())
        return
      }
      this.$http.get(`shangpinxinxi/info/${row.id}`).then(({ data }) => {
        if (data && data.code === 0) {
          this.form = Object.assign(emptyForm(), data.data)
          this.loadBatches(this.form.zhanghao)
        }
      })
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        const url = this.form.id ? 'shangpinxinxi/update' : 'shangpinxinxi/save'
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
      this.$confirm('确定删除选中的茶品档案吗？', '提示', { type: 'warning' }).then(() => {
        this.$http.post('shangpinxinxi/delete', targetIds).then(({ data }) => {
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
.table-image, .preview-image { width: 56px; height: 56px; object-fit: cover; border-radius: 8px; }
.image-preview-list { display: flex; gap: 8px; flex-wrap: wrap; }
.pager { margin-top: 16px; text-align: right; }
.danger-text { color: #f56c6c; }
.rich-preview { border: 1px solid #ebeef5; border-radius: 8px; padding: 12px; background: #fafafa; }
</style>

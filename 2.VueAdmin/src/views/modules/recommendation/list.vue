<template>
  <div class="main-content">
    <template v-if="showFlag">
      <el-form class="toolbar" :inline="true" :model="searchForm">
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userid" placeholder="请输入用户ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button
            v-if="isAuth('recommendation','刷新')"
            type="success"
            @click="refreshAll"
          >
            刷新全部推荐
          </el-button>
          <el-button
            v-if="isAuth('recommendation','删除')"
            type="danger"
            :disabled="dataListSelections.length === 0"
            @click="deleteHandler()"
          >
            批量删除
          </el-button>
        </el-form-item>
      </el-form>

      <el-table
        v-if="isAuth('recommendation','查看')"
        :data="dataList"
        v-loading="dataListLoading"
        border
        @selection-change="selectionChangeHandler"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="userid" label="用户ID" min-width="100" />
        <el-table-column prop="goodid" label="商品ID" min-width="100" />
        <el-table-column prop="shangpinmingcheng" label="商品名称" min-width="160" />
        <el-table-column prop="shangpinfenlei" label="商品分类" min-width="120" />
        <el-table-column prop="pinpai" label="品牌" min-width="120" />
        <el-table-column prop="score" label="推荐分数" min-width="100" />
        <el-table-column prop="reason" label="推荐理由" min-width="180" show-overflow-tooltip />
        <el-table-column prop="algorithmType" label="算法类型" min-width="120" />
        <el-table-column prop="createTime" label="生成时间" min-width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button
              v-if="isAuth('recommendation','查看')"
              type="text"
              size="small"
              @click="addOrUpdateHandler(scope.row.id)"
            >
              详情
            </el-button>
            <el-button
              v-if="isAuth('recommendation','删除')"
              type="text"
              size="small"
              class="danger-text"
              @click="deleteHandler(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pager"
        background
        :current-page="pageIndex"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pageSize"
        :layout="layouts.join()"
        :total="totalPage"
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
      />
    </template>

    <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate" />
  </div>
</template>

<script>
import AddOrUpdate from './add-or-update'

export default {
  components: {
    AddOrUpdate
  },
  data() {
    return {
      searchForm: {
        userid: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      addOrUpdateFlag: false,
      layouts: ['total', 'prev', 'pager', 'next', 'sizes', 'jumper']
    }
  },
  created() {
    this.getDataList()
  },
  methods: {
    getDataList() {
      this.dataListLoading = true
      const params = {
        page: this.pageIndex,
        limit: this.pageSize
      }
      if (this.searchForm.userid) {
        params.userid = this.searchForm.userid
      }
      this.$http({
        url: 'recommendation/page',
        method: 'get',
        params
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.totalPage = data.data.total || 0
        } else {
          this.dataList = []
          this.totalPage = 0
          if (data && data.msg) {
            this.$message.error(data.msg)
          }
        }
        this.dataListLoading = false
      }).catch(() => {
        this.dataListLoading = false
      })
    },
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    selectionChangeHandler(val) {
      this.dataListSelections = val
    },
    addOrUpdateHandler(id) {
      this.showFlag = false
      this.addOrUpdateFlag = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    deleteHandler(id) {
      const ids = id
        ? [Number(id)]
        : this.dataListSelections.map(item => Number(item.id))
      if (!ids.length) {
        return
      }
      this.$confirm(`确定执行${id ? '删除' : '批量删除'}操作吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'recommendation/delete',
          method: 'post',
          data: ids
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('操作成功')
            this.search()
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    },
    search() {
      this.pageIndex = 1
      this.getDataList()
    },
    refreshAll() {
      this.$confirm('确定刷新全部用户推荐结果吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'recommendation/refresh/all',
          method: 'post'
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('刷新成功')
            this.getDataList()
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.toolbar {
  margin-bottom: 20px;
}

.pager {
  margin-top: 20px;
  text-align: right;
}

.danger-text {
  color: #f56c6c;
}
</style>

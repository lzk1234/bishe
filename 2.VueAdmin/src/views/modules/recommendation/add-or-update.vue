<template>
  <el-dialog
    title="推荐详情"
    :visible.sync="dialogVisible"
    width="640px"
    :before-close="handleClose"
  >
    <el-form :model="form" label-width="120px">
      <el-form-item label="用户ID">
        <el-input v-model="form.userid" disabled />
      </el-form-item>
      <el-form-item label="商品ID">
        <el-input v-model="form.goodid" disabled />
      </el-form-item>
      <el-form-item label="商品名称">
        <el-input v-model="form.shangpinmingcheng" disabled />
      </el-form-item>
      <el-form-item label="商品分类">
        <el-input v-model="form.shangpinfenlei" disabled />
      </el-form-item>
      <el-form-item label="品牌">
        <el-input v-model="form.pinpai" disabled />
      </el-form-item>
      <el-form-item label="推荐分数">
        <el-input v-model="form.score" disabled />
      </el-form-item>
      <el-form-item label="推荐理由">
        <el-input type="textarea" :rows="3" v-model="form.reason" disabled />
      </el-form-item>
      <el-form-item label="算法类型">
        <el-input v-model="form.algorithmType" disabled />
      </el-form-item>
      <el-form-item label="生成时间">
        <el-input v-model="form.createTime" disabled />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  props: ['parent'],
  data() {
    return {
      dialogVisible: false,
      form: {
        userid: '',
        goodid: '',
        shangpinmingcheng: '',
        shangpinfenlei: '',
        pinpai: '',
        score: '',
        reason: '',
        algorithmType: '',
        createTime: ''
      }
    }
  },
  methods: {
    init(id) {
      this.dialogVisible = true
      this.resetForm()
      if (id) {
        this.getInfo(id)
      }
    },
    getInfo(id) {
      this.$http({
        url: `recommendation/info/${id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.form = Object.assign({}, this.form, data.data)
        } else {
          this.$message.error(data.msg)
        }
      })
    },
    resetForm() {
      this.form = {
        userid: '',
        goodid: '',
        shangpinmingcheng: '',
        shangpinfenlei: '',
        pinpai: '',
        score: '',
        reason: '',
        algorithmType: '',
        createTime: ''
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
      if (this.parent) {
        this.parent.addOrUpdateFlag = false
        this.parent.showFlag = true
      }
    }
  }
}
</script>

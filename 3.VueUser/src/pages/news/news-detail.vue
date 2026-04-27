<template>
  <div :style='{"border":"1px solid #f0f0ed","padding":"60px 40px","margin":"40px auto","borderRadius":"var(--radius-main)","background":"#fff","width":"1200px","position":"relative"}'>
    <div class="section-title" :style='{"margin":"0 0 50px","color":"var(--primary-color)","borderRadius":"4px","textAlign":"center","background":"#fdfdfb","fontSize":"24px","lineHeight":"100px","fontWeight":"600","letterSpacing":"4px","fontFamily":"serif","border":"1px solid #f0f0ed"}'>商城资讯</div>
    <div class="section-content">
      <div class="content-title" :style='{"color":"var(--primary-color)","fontSize":"28px","fontFamily":"serif","marginBottom":"20px"}'>{{detail.title}}</div>
      <div class="content-sub-title" :style='{"color":"var(--text-secondary)","fontStyle":"italic"}'>发布时间：{{detail.addtime}}</div>
      <el-divider><i class="el-icon-coffee-cup" :style='{"color":"var(--accent-color)"}'></i></el-divider>
      <div class="content-detail" v-html="detail.content" :style='{"lineHeight":"2","fontSize":"16px","color":"var(--text-main)"}'></div>
    </div>
  </div>
</template>

<script>
  export default {
    //数据集合
    data() {
      return {
        detail: {},
      }
    },
    created() {
      this.detail = Object.assign({}, JSON.parse(this.$route.query.detailObj));
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .section {
    width: 900px;
    margin: 0 auto;
  }

  .section-content {
      margin-top: 30px;
  }
  .content-title {
      text-align: center;
      font-size: 22px;
      font-weight: bold;
  }
  .content-sub-title {
      text-align: center;
      margin-top: 20px;
      color: #888888;
      font-size: 14px;
  }
</style>
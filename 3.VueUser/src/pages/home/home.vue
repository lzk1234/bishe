<template>
  <div class="portal-home">
    <section class="hero-section fade-in-up">
      <div class="hero-copy">
        <span class="eyebrow">Mountain Tea Decision Platform</span>
        <h1>高山茶产供销智能决策平台</h1>
        <p>面向茶企、采购客户与管理者的产地展示、茶品采购与经营分析门户。</p>
        <div class="hero-actions">
          <el-button type="primary" @click="moreBtn('shangpinxinxi')">进入茶品采购</el-button>
          <el-button plain @click="moreBtn('news')">查看市场资讯</el-button>
        </div>
      </div>
      <div class="hero-panel">
        <div class="panel-card">
          <div class="metric">{{ shangpinxinxiRecommend.length || 0 }}</div>
          <div class="label">今日推荐茶品</div>
        </div>
        <div class="panel-card">
          <div class="metric">{{ newsList.length || 0 }}</div>
          <div class="label">最新资讯</div>
        </div>
      </div>
    </section>

    <section class="feature-section fade-in-up" style="animation-delay: 0.1s">
      <div class="feature-card">
        <h3>产地可信</h3>
        <p>展示茶园基地、海拔、批次等关键字段，突出高山茶来源与品质。</p>
      </div>
      <div class="feature-card">
        <h3>采购便捷</h3>
        <p>保留购物车、订单、地址等完整采购链路，支撑“销”环节演示。</p>
      </div>
      <div class="feature-card">
        <h3>经营可视</h3>
        <p>后台通过库存预警、滞销提醒与销售趋势支撑决策建议。</p>
      </div>
    </section>

    <section class="recommend-section fade-in-up" style="animation-delay: 0.2s">
      <div class="section-header">
        <div>
          <h2>精选推荐</h2>
          <p>结合近期行为与热度，为采购用户推荐关注度较高的高山茶品。</p>
        </div>
        <div class="header-actions">
          <el-button type="primary" icon="el-icon-refresh" size="small" @click="refreshRecommendations" :disabled="!token">刷新推荐</el-button>
          <span class="more-link" @click="moreBtn('recommendation')">查看更多</span>
        </div>
      </div>

      <div class="product-grid" v-loading="loading">
        <div v-for="item in shangpinxinxiRecommend" :key="item.id || item.goodid" class="product-card" @click="toGoodsDetail(item)">
          <div class="image-wrapper">
            <img v-if="preHttp(item.tupian)" :src="item.tupian.split(',')[0]" alt="" />
            <img v-else :src="baseUrl + imagePath(item.tupian)" alt="" />
          </div>
          <div class="product-info">
            <span class="category">{{ item.shangpinfenlei || '未分类' }}</span>
            <h3>{{ item.shangpinmingcheng }}</h3>
            <p>{{ item.pinpai || '平台精选茶品' }}</p>
            <p class="reason" v-if="item.reason">{{ item.reason }}</p>
          </div>
        </div>
      </div>
    </section>

    <section class="content-section fade-in-up" style="animation-delay: 0.3s">
      <div class="news-section">
        <div class="section-header">
          <div>
            <h2>市场资讯</h2>
            <p>围绕高山茶行业动态、市场信息与平台公告。</p>
          </div>
          <span class="more-link" @click="moreBtn('news')">更多资讯</span>
        </div>
        <div class="news-list">
          <div v-for="(item, index) in newsList.slice(0, 4)" :key="index" class="news-item" @click="toDetail('newsDetail', item)">
            <div class="news-date">
              <span class="day">{{ getDay(item.addtime) }}</span>
              <span class="month">{{ getMonth(item.addtime) }}月</span>
            </div>
            <div class="news-content">
              <h4>{{ item.title }}</h4>
              <p>{{ item.introduction }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="about-section">
        <div class="section-header">
          <div>
            <h2>项目介绍</h2>
            <p>用于答辩展示平台定位、建设背景与功能亮点。</p>
          </div>
        </div>
        <div class="about-card">
          <div class="about-images">
            <img :src="baseUrl + (aboutUsDetail.picture1 || '')" class="img1">
            <img :src="baseUrl + (aboutUsDetail.picture2 || '')" class="img2">
          </div>
          <div class="about-text">
            <h3>{{ aboutUsDetail.title }}</h3>
            <p class="subtitle">{{ aboutUsDetail.subtitle }}</p>
            <div class="content-preview" v-html="aboutUsDetail.content"></div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  data() {
    return {
      baseUrl: '',
      aboutUsDetail: {},
      newsList: [],
      shangpinxinxiRecommend: [],
      loading: false,
      token: localStorage.getItem('Token')
    }
  },
  created() {
    this.baseUrl = this.$config.baseUrl
    this.getNewsList()
    this.getAboutUs()
    this.getList()
  },
  methods: {
    preHttp(str) {
      return !!str && str.substr(0, 4) === 'http'
    },
    imagePath(value) {
      return value ? value.split(',')[0] : ''
    },
    getDay(value) {
      return value ? value.split(' ')[0].split('-')[2] : ''
    },
    getMonth(value) {
      return value ? value.split(' ')[0].split('-')[1] : ''
    },
    getAboutUs() {
      this.$http.get('aboutus/detail/1').then(res => {
        if (res.data.code === 0) {
          this.aboutUsDetail = res.data.data || {}
        }
      })
    },
    getNewsList() {
      this.$http.get('news/list', {
        params: {
          page: 1,
          limit: 6,
          order: 'desc'
        }
      }).then(res => {
        if (res.data.code === 0) {
          this.newsList = (res.data.data && res.data.data.list) || []
        }
      })
    },
    getList() {
      this.loading = true
      this.$http.get('recommendation/today', { params: { limit: 8 } }).then(res => {
        if (res.data.code === 0) {
          this.shangpinxinxiRecommend = res.data.data || []
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    refreshRecommendations() {
      if (!this.token) {
        this.$message.warning('请先登录')
        return
      }
      this.loading = true
      this.$http.post('recommendation/refresh').then(res => {
        if (res.data.code === 0) {
          this.$message.success('刷新成功')
          this.getList()
        } else {
          this.loading = false
        }
      }).catch(() => {
        this.loading = false
      })
    },
    toGoodsDetail(item) {
      const detailObj = Object.assign({}, item, { id: item.goodid || item.id })
      this.$router.push({
        path: '/index/shangpinxinxiDetail',
        query: { detailObj: JSON.stringify(detailObj) }
      })
    },
    toDetail(path, item) {
      this.$router.push({ path: `/index/${path}`, query: { detailObj: JSON.stringify(item) } })
    },
    moreBtn(path) {
      this.$router.push({ path: `/index/${path}` })
    }
  }
}
</script>

<style scoped>
.portal-home { max-width: 1200px; margin: 0 auto; padding: 32px 0 56px; }
.hero-section { display: grid; grid-template-columns: 1.4fr 0.8fr; gap: 24px; background: linear-gradient(135deg, #f5fbf7, #eef5ef); border: 1px solid #e7efe9; border-radius: 24px; padding: 36px; }
.eyebrow { display: inline-block; margin-bottom: 12px; color: #2d5a27; font-weight: 600; letter-spacing: 0.08em; text-transform: uppercase; }
.hero-copy h1 { margin: 0 0 12px; font-size: 40px; color: #1f3a2e; }
.hero-copy p { margin: 0 0 24px; color: #587064; line-height: 1.8; }
.hero-actions { display: flex; gap: 12px; }
.hero-panel { display: grid; gap: 16px; }
.panel-card { background: #fff; border-radius: 18px; padding: 24px; box-shadow: 0 12px 40px rgba(34, 60, 45, 0.08); }
.metric { font-size: 36px; font-weight: 700; color: #1f3a2e; }
.label { margin-top: 8px; color: #6b8176; }
.feature-section { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin: 28px 0; }
.feature-card { background: #fff; border: 1px solid #edf1ed; border-radius: 18px; padding: 22px; }
.feature-card h3 { margin: 0 0 10px; color: #244536; }
.feature-card p { margin: 0; color: #64796e; line-height: 1.8; }
.section-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 20px; }
.section-header h2 { margin: 0 0 8px; font-size: 28px; color: #1f3a2e; }
.section-header p { margin: 0; color: #667c71; }
.header-actions { display: flex; align-items: center; gap: 16px; }
.more-link { color: #2d5a27; cursor: pointer; font-weight: 600; }
.product-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 18px; }
.product-card { background: #fff; border: 1px solid #eef2ef; border-radius: 18px; overflow: hidden; cursor: pointer; transition: transform .2s ease, box-shadow .2s ease; }
.product-card:hover { transform: translateY(-4px); box-shadow: 0 16px 36px rgba(34, 60, 45, 0.12); }
.image-wrapper { height: 240px; overflow: hidden; }
.image-wrapper img { width: 100%; height: 100%; object-fit: cover; }
.product-info { padding: 18px; }
.product-info h3 { margin: 10px 0 8px; color: #1f3a2e; font-size: 18px; }
.product-info p { margin: 0; color: #667c71; line-height: 1.6; }
.category { color: #2d5a27; font-size: 12px; font-weight: 700; }
.reason { margin-top: 8px !important; color: #a06c12 !important; }
.content-section { display: grid; grid-template-columns: 1fr 1fr; gap: 28px; margin-top: 36px; }
.news-item { display: flex; gap: 16px; padding: 16px 0; border-bottom: 1px solid #edf1ed; cursor: pointer; }
.news-date { width: 72px; min-width: 72px; border-radius: 14px; background: #f4f8f5; text-align: center; padding: 10px 8px; }
.news-date .day { display: block; font-size: 24px; font-weight: 700; color: #1f3a2e; }
.news-date .month { color: #71857a; font-size: 13px; }
.news-content h4 { margin: 0 0 8px; color: #244536; }
.news-content p { margin: 0; color: #6c8176; line-height: 1.7; }
.about-card { display: flex; flex-direction: column; gap: 18px; background: #fff; border: 1px solid #edf1ed; border-radius: 20px; padding: 18px; }
.about-images { width: 100%; display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 12px; }
.about-images img { width: 100%; height: auto; aspect-ratio: 16 / 9; object-fit: contain; border-radius: 16px; background: #f5faf7; display: block; }
.about-text { width: 100%; }
.about-text h3 { margin: 0 0 8px; color: #244536; }
.subtitle { margin: 0 0 12px; color: #7a8d83; }
.content-preview { color: #5f7569; line-height: 1.8; max-height: 240px; overflow: hidden; }
@media (max-width: 960px) {
  .hero-section, .content-section, .feature-section, .product-grid { grid-template-columns: 1fr; }
  .about-images { grid-template-columns: 1fr; }
}
</style>

<template>
  <div class="list-page">
    <div class="breadcrumb-container">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/index/home' }">平台首页</el-breadcrumb-item>
        <el-breadcrumb-item>茶品采购</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="list-container">
      <div class="headline">
        <div>
          <h1>高山茶品采购</h1>
          <p>按茶类、品牌、产地与价格区间筛选采购目标茶品。</p>
        </div>
      </div>

      <div class="category-tabs">
        <div class="tab-item" :class="{ active: swiperIndex === -1 }" @click="getList(1, '全部')">全部</div>
        <div v-for="(item, index) in fenlei" :key="index" class="tab-item" :class="{ active: swiperIndex === index }" @click="getList(1, item)">{{ item }}</div>
      </div>

      <div class="actions-bar">
        <el-form :inline="true" :model="formSearch" class="search-form">
          <el-form-item>
            <el-input v-model="formSearch.shangpinmingcheng" placeholder="搜索茶品名称" prefix-icon="el-icon-search" clearable />
          </el-form-item>
          <el-form-item>
            <el-input v-model="formSearch.pinpai" placeholder="品牌" clearable />
          </el-form-item>
          <el-form-item class="price-range">
            <el-input v-model="formSearch.pricestart" placeholder="最低价格" style="width: 110px" />
            <span class="range-sep">-</span>
            <el-input v-model="formSearch.priceend" placeholder="最高价格" style="width: 110px" />
          </el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="getList(1, curFenlei)">筛选</el-button>
        </el-form>
      </div>

      <div class="product-grid">
        <div v-for="(item, index) in dataList" :key="index" class="product-card" @click="toDetail(item)">
          <div class="image-wrapper">
            <img :src="$imageUrl(item.tupian)" class="image" />
          </div>
          <div class="product-info">
            <div class="top-row">
              <span class="category">{{ item.shangpinfenlei }}</span>
              <span class="price" v-if="item.price">¥{{ item.price }}</span>
            </div>
            <h3>{{ item.shangpinmingcheng }}</h3>
            <p class="meta">{{ item.pinpai || '平台精选' }}</p>
            <p class="meta" v-if="item.chandi">{{ item.chandi }}</p>
            <p class="meta" v-if="item.haiba">海拔 {{ item.haiba }}m</p>
          </div>
        </div>
      </div>

      <div class="pagination-wrapper">
        <el-pagination
          background
          layout="total, prev, pager, next, sizes"
          :current-page="1"
          :page-size="pageSize"
          :page-sizes="[12, 24, 36, 48]"
          :total="total"
          @current-change="curChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      swiperIndex: -1,
      baseUrl: '',
      formSearch: {
        shangpinmingcheng: '',
        pinpai: '',
        price: ''
      },
      fenlei: [],
      dataList: [],
      total: 1,
      pageSize: 10,
      pageSizes: [10, 20, 30, 50],
      totalPage: 1,
      curFenlei: '全部'
    }
  },
  created() {
    this.baseUrl = this.$config.baseUrl
    this.getFenlei()
    this.getList(1, '全部')
  },
  methods: {
    getFenlei() {
      this.$http.get('option/shangpinfenlei/shangpinfenlei').then(res => {
        if (res.data.code === 0) {
          this.fenlei = res.data.data
        }
      })
    },
    getList(page, fenlei) {
      if (fenlei === '全部') this.swiperIndex = -1
      this.fenlei.forEach((item, index) => {
        if (fenlei === item) this.swiperIndex = index
      })
      this.curFenlei = fenlei
      const params = { page, limit: this.pageSize, sort: 'shangjiariqi', order: 'desc' }
      if (this.formSearch.shangpinmingcheng) params.shangpinmingcheng = `%${this.formSearch.shangpinmingcheng}%`
      if (this.formSearch.pinpai) params.pinpai = `%${this.formSearch.pinpai}%`
      if (this.formSearch.pricestart) params.pricestart = this.formSearch.pricestart
      if (this.formSearch.priceend) params.priceend = this.formSearch.priceend
      if (fenlei !== '全部') params.shangpinfenlei = fenlei
      this.$http.get('shangpinxinxi/list', { params }).then(res => {
        if (res.data.code === 0) {
          this.dataList = res.data.data.list
          this.total = res.data.data.total
          this.pageSize = res.data.data.pageSize
          this.totalPage = res.data.data.totalPage
          this.pageSizes = [this.pageSize, this.pageSize * 2, this.pageSize * 3, this.pageSize * 5]
        }
      })
    },
    curChange(page) {
      this.getList(page, this.curFenlei)
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.getList(1, this.curFenlei)
    },
    toDetail(item) {
      this.$router.push({ path: '/index/shangpinxinxiDetail', query: { detailObj: JSON.stringify(item) } })
    }
  }
}
</script>

<style scoped>
.list-page { min-height: 100vh; background: var(--bg-color); }
.breadcrumb-container { background: transparent; padding: 24px 0; border-bottom: 1px solid #e5e5e0; margin-bottom: 32px; }
.breadcrumb-container .el-breadcrumb { max-width: 1200px; margin: 0 auto; padding: 0 20px; }
.list-container { max-width: 1200px; margin: 0 auto; padding: 0 20px 60px; }
.headline { margin-bottom: 24px; }
.headline h1 { margin: 0 0 8px; font-size: 36px; color: #1f3a2e; }
.headline p { margin: 0; color: #64796e; }
.category-tabs { display: flex; gap: 24px; margin-bottom: 24px; border-bottom: 1px solid #e5e5e0; }
.tab-item { padding: 14px 4px; cursor: pointer; color: var(--text-secondary); position: relative; }
.tab-item.active { color: var(--primary-color); font-weight: 600; }
.tab-item.active::after { content: ''; position: absolute; left: 0; right: 0; bottom: 0; height: 3px; background: var(--accent-color); }
.actions-bar { background: #fff; border: 1px solid #eef2ef; border-radius: 16px; padding: 20px 24px; margin-bottom: 28px; }
.search-form .el-form-item { margin-bottom: 0; margin-right: 18px; }
.price-range { display: inline-flex; align-items: center; }
.range-sep { margin: 0 10px; color: #8a9b92; }
.product-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 18px; }
.product-card { background: #fff; border: 1px solid #eef2ef; border-radius: 18px; overflow: hidden; cursor: pointer; transition: transform .2s ease, box-shadow .2s ease; }
.product-card:hover { transform: translateY(-4px); box-shadow: 0 16px 36px rgba(34, 60, 45, 0.12); }
.image-wrapper { height: 260px; }
.image { width: 100%; height: 100%; object-fit: cover; }
.product-info { padding: 18px; }
.top-row { display: flex; justify-content: space-between; align-items: center; }
.category { color: #2d5a27; font-size: 12px; font-weight: 700; }
.price { color: #b26a00; font-weight: 700; }
.product-info h3 { margin: 10px 0 8px; color: #1f3a2e; font-size: 18px; }
.meta { margin: 0 0 6px; color: #6c8176; font-size: 13px; }
.pagination-wrapper { margin-top: 28px; text-align: right; }
@media (max-width: 960px) {
  .product-grid { grid-template-columns: 1fr 1fr; }
}
@media (max-width: 640px) {
  .product-grid { grid-template-columns: 1fr; }
}
</style>

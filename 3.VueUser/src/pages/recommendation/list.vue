<template>
  <div class="list-page">
    <div class="breadcrumb-container">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/index/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>为您推荐</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="list-container">
      <div class="section-header fade-in-up">
        <h2 class="section-title">
          <i class="el-icon-star-on"></i>
          为您推荐
        </h2>
        <p class="section-subtitle">根据浏览、收藏和下单记录，为您生成个性化推荐。</p>
      </div>

      <div class="actions-bar fade-in-up" style="animation-delay: 0.1s">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshRecommendations">
          刷新推荐
        </el-button>
      </div>

      <div class="product-grid fade-in-up" style="animation-delay: 0.2s" v-loading="loading">
        <div
          v-for="item in dataList"
          :key="item.id || item.goodid"
          class="product-card"
          @click="toDetail(item)"
        >
          <div class="image-wrapper">
            <img v-if="preHttp(item.tupian)" :src="item.tupian.split(',')[0]" class="image" />
            <img v-else :src="baseUrl + imagePath(item.tupian)" class="image" />
            <div class="hover-overlay">
              <span>查看详情</span>
            </div>
          </div>
          <div class="product-info">
            <div class="info-header">
              <span class="category">{{ item.shangpinfenlei || '未分类' }}</span>
              <span class="price" v-if="item.price">¥{{ item.price }}</span>
            </div>
            <h3 class="name">{{ item.shangpinmingcheng }}</h3>
            <div class="brand">{{ item.pinpai || '暂无品牌' }}</div>
            <div class="recommendation-reason" v-if="item.reason">
              <i class="el-icon-info"></i>{{ item.reason }}
            </div>
          </div>
        </div>
      </div>

      <div class="empty-state" v-if="!loading && dataList.length === 0">
        <i class="el-icon-box"></i>
        <p>暂无推荐数据，请先浏览、收藏或购买一些茶叶。</p>
        <el-button type="primary" @click="$router.push('/index/shangpinxinxi')">
          去逛商品
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      baseUrl: '',
      dataList: [],
      loading: false
    }
  },
  created() {
    this.baseUrl = this.$config.baseUrl || (this.$base && this.$base.url) || ''
    this.getRecommendations()
  },
  methods: {
    preHttp(str) {
      return !!str && str.substr(0, 4) === 'http'
    },
    imagePath(value) {
      return value ? value.split(',')[0] : ''
    },
    getRecommendations() {
      this.loading = true
      this.$http({
        url: 'recommendation/user/list',
        method: 'get',
        params: {
          limit: 20
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data || []
        } else {
          this.dataList = []
          this.$message.error(data.msg)
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    refreshRecommendations() {
      this.$confirm('确定刷新当前账号的推荐结果吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'recommendation/refresh',
          method: 'post'
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('刷新成功')
            this.getRecommendations()
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    },
    toDetail(item) {
      const detailObj = Object.assign({}, item, { id: item.goodid })
      this.$router.push({
        path: '/index/shangpinxinxiDetail',
        query: {
          detailObj: JSON.stringify(detailObj)
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.list-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20px;
}

.breadcrumb-container {
  max-width: 1200px;
  margin: 0 auto 20px;
  padding: 15px 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.list-container {
  max-width: 1200px;
  margin: 0 auto;
}

.section-header {
  background: linear-gradient(135deg, #1f7a8c 0%, #bfdbf7 100%);
  color: #fff;
  padding: 40px;
  border-radius: 12px;
  margin-bottom: 30px;
  box-shadow: 0 4px 12px rgba(31, 122, 140, 0.2);
}

.section-title {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 10px 0;
}

.section-title i {
  margin-right: 10px;
}

.section-subtitle {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

.actions-bar {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: flex-end;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.product-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.image-wrapper {
  position: relative;
  width: 100%;
  padding-top: 100%;
  overflow: hidden;
}

.image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.hover-overlay span {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
}

.product-card:hover .hover-overlay {
  opacity: 1;
}

.product-info {
  padding: 15px;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.category {
  font-size: 12px;
  color: #999;
  background: #f5f5f5;
  padding: 4px 8px;
  border-radius: 4px;
}

.price {
  font-size: 18px;
  color: #e74c3c;
  font-weight: 600;
}

.name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.brand {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.recommendation-reason {
  font-size: 12px;
  color: #1f7a8c;
  background: rgba(31, 122, 140, 0.1);
  padding: 8px;
  border-radius: 4px;
}

.recommendation-reason i {
  margin-right: 5px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: #fff;
  border-radius: 12px;
}

.empty-state i {
  font-size: 64px;
  color: #ddd;
  margin-bottom: 20px;
}

.empty-state p {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
}

@keyframes fade-in-up {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.fade-in-up {
  animation: fade-in-up 0.5s ease forwards;
}
</style>

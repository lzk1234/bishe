<template>
<div class="detail-page">
	<div class="breadcrumb-container">
		<el-breadcrumb separator="/">
			<el-breadcrumb-item :to="{ path: '/index/home' }">平台首页</el-breadcrumb-item>
			<el-breadcrumb-item v-for="(item, index) in breadcrumbItem" :key="index">{{item.name}}</el-breadcrumb-item>
		</el-breadcrumb>
	</div>
	
	<div class="detail-container">
		<div class="product-main fade-in-up">
			<div class="product-gallery">
				<el-carousel trigger="click" height="500px" arrow="hover">
					<el-carousel-item v-for="(item, index) in detailBanner" :key="index">
						<el-image :src="item.substr(0,4)=='http' ? item : baseUrl + item" fit="cover" class="main-image"></el-image>
					</el-carousel-item>
				</el-carousel>
			</div>
	
			<div class="product-info">
				<div class="info-header">
					<span class="category-tag">{{detail.shangpinfenlei}}</span>
					<div class="action-buttons">
						<div @click="storeup(1)" v-show="!isStoreup" class="wishlist-btn">
							<i class="el-icon-star-off"></i> 关注茶品
						</div>
						<div @click="storeup(-1)" v-show="isStoreup" class="wishlist-btn active">
							<i class="el-icon-star-on"></i> 已关注
						</div>
					</div>
				</div>

				<h1 class="product-title">{{detail.shangpinmingcheng}}</h1>
				
				<div class="price-section">
					<div class="price-row main-price">
						<span class="label">采购价</span>
						<span class="value">¥{{detail.price}}</span>
					</div>
					<div class="price-row vip-price" v-if="detail.vipprice">
						<span class="label">会员参考价</span>
						<span class="value">¥{{detail.vipprice}}</span>
					</div>
				</div>

				<div class="spec-list">
					<div class="spec-item">
						<span class="label">品牌</span>
						<span class="value">{{detail.pinpai}}</span>
					</div>
					<div class="spec-item" v-if="detail.chandi">
						<span class="label">产地</span>
						<span class="value">{{detail.chandi}}</span>
					</div>
					<div class="spec-item" v-if="detail.haiba">
						<span class="label">海拔</span>
						<span class="value">{{detail.haiba}} m</span>
					</div>
					<div class="spec-item" v-if="detail.shengchanpici">
						<span class="label">生产批次</span>
						<span class="value">{{detail.shengchanpici}}</span>
					</div>
					<div class="spec-item" v-if="detail.shiyongchangjing">
						<span class="label">适饮场景</span>
						<span class="value">{{detail.shiyongchangjing}}</span>
					</div>
					<div class="spec-item">
						<span class="label">库存</span>
						<span class="value">{{detail.alllimittimes}} 件</span>
					</div>
					<div class="spec-item">
						<span class="label">上架日期</span>
						<span class="value">{{detail.shangjiariqi}}</span>
					</div>
				</div>

				<div class="purchase-actions" v-if="detail.alllimittimes">
					<div class="quantity-selector">
						<span class="label">采购数量</span>
						<el-input-number v-model="buynumber" :min="1" :max="detail.alllimittimes" size="medium"></el-input-number>
					</div>
					<div class="button-group">
						<el-button type="primary" class="cart-btn" @click="addCart" icon="el-icon-shopping-cart-2">加入采购单</el-button>
						<el-button type="success" class="buy-btn" @click="buyNow">立即下单</el-button>
					</div>
				</div>
				<div class="out-of-stock" v-else>
					<el-button type="info" disabled style="width: 100%">当前库存不足</el-button>
				</div>
			</div>
		</div>
		
		<div class="product-tabs fade-in-up" style="animation-delay: 0.1s">
			<el-tabs v-model="activeName" type="card">
				<el-tab-pane label="茶品详情" name="first">
					<div class="content-box" v-html="detail.shangpinxiangqing"></div>
				</el-tab-pane>
				<el-tab-pane label="采购评价" name="second">
					<div class="comment-section">
						<el-form :model="form" :rules="rules" ref="form" class="comment-form">
							<el-form-item prop="content">
								<el-input type="textarea" :rows="4" v-model="form.content" placeholder="分享您的采购体验或品鉴感受..."></el-input>
							</el-form-item>
							<div class="submit-row">
								<el-button type="primary" @click="submitForm('form')">提交评论</el-button>
								<el-button @click="resetForm('form')">清空</el-button>
							</div>
						</el-form>
						
						<div class="comment-list" v-if="infoList.length">
							<div v-for="item in infoList" :key="item.id" class="comment-item">
								<div class="user-info">
									<el-avatar :size="40" :src="item.avatarurl ? baseUrl + item.avatarurl : require('@/assets/touxiang.png')"></el-avatar>
									<div class="user-meta">
										<span class="name">{{item.nickname}}</span>
										<span class="time">{{item.addtime}}</span>
									</div>
								</div>
								<div class="comment-content">
									{{item.content}}
								</div>
								<div class="reply-content" v-if="item.reply">
									<span class="reply-label">茶企回复：</span>{{item.reply}}
								</div>
							</div>
						</div>
						<div class="no-comment" v-else>
							<p>暂无评价，欢迎分享您的采购与品鉴感受</p>
						</div>

						<div class="pagination-wrapper">
							<el-pagination
								background
								layout="prev, pager, next"
								:total="total"
								:page-size="pageSize"
								@current-change="curChange"
							></el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>
	</div>
</div>
</template>

<script>
  import CountDown from '@/components/CountDown';
  export default {
    //数据集合
    data() {
      return {
        tablename: 'shangpinxinxi',
        baseUrl: '',
        breadcrumbItem: [
          {
            name: '详情信息'
          }
        ],
        title: '',
        detailBanner: [],
        endTime: 0,
        detail: {},
        activeName: 'first',
        form: {
          content: '',
          userid: localStorage.getItem('userid'),
          nickname: localStorage.getItem('username'),
          avatarurl: '',
        },
        infoList: [],
        total: 1,
        pageSize: 5,
		pageSizes: [10,20,30,50],
        totalPage: 1,
        rules: {
          content: [
            { required: true, message: '请输入内容', trigger: 'blur' }
          ]
        },
        storeupParams: {
          name: '',
          picture: '',
          refid: 0,
          tablename: 'shangpinxinxi',
          userid: localStorage.getItem('userid')
        },
        isStoreup: false,
        storeupInfo: {},
        buynumber: 1,
        cart: {
          buynumber: 0,
          discountprice: 0,
          goodid: 0,
          goodname: '',
          picture: '',
          price: 0,
          userid: localStorage.getItem('userid')
        },
        isInCart: false,
      }
    },
    created() {
        this.init();
    },
    //方法集合
    methods: {
        init() {
          this.baseUrl = this.$config.baseUrl;
          if(this.$route.query.detailObj) {
            this.detail = JSON.parse(this.$route.query.detailObj);
          }
          if(this.$route.query.storeupObj) {
            this.detail.id = JSON.parse(this.$route.query.storeupObj).refid;
          }
          if(this.$route.query.discussObj) {
            this.detail.id = JSON.parse(this.$route.query.discussObj).goodid;
          }
          this.$http.get(this.tablename + '/detail/'  + this.detail.id, {}).then(res => {
            if (res.data.code == 0) {
              this.detail = res.data.data;
              this.title = this.detail.shangpinmingcheng;
              this.detailBanner = this.detail.tupian ? this.detail.tupian.split(",") : [];
              this.$forceUpdate();
            }
          });

          this.getDiscussList(1);
          this.getStoreupStatus();
          this.getCartList();

        },
      storeup(type) {
        if (type == 1 && !this.isStoreup) {
          this.storeupParams.name = this.title;
          this.storeupParams.picture = this.detailBanner[0];
          this.storeupParams.inteltype = this.detail.shangpinfenlei;
          this.storeupParams.refid = this.detail.id;
          this.storeupParams.type = type;
          this.$http.post('storeup/add', this.storeupParams).then(res => {
            if (res.data.code == 0) {
              this.isStoreup = true;
              this.$message({
                type: 'success',
                message: '收藏成功!',
                duration: 1500,
              });
            }
          });
        }
        if (type == -1 && this.isStoreup) {
          let delIds = new Array();
          delIds.push(this.storeupInfo.id);
          this.$http.post('storeup/delete', delIds).then(res => {
            if (res.data.code == 0) {
              this.isStoreup = false;
              this.$message({
                type: 'success',
                message: '取消成功!',
                duration: 1500,
              });
            }
          });
        }
      },
      getStoreupStatus(){
        if(localStorage.getItem("Token")) {
            this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: 1, refid: this.detail.id, tablename: 'shangpinxinxi', userid: localStorage.getItem('userid')}}).then(res => {
              if (res.data.code == 0 && res.data.data.list.length > 0) {
                this.isStoreup = true;
                this.storeupInfo = res.data.data.list[0];
              }
            });
        }
      },
      curChange(page) {
        this.getDiscussList(page);
      },
      prevClick(page) {
        this.getDiscussList(page);
      },
      nextClick(page) {
        this.getDiscussList(page);
      },
      // 下载
      download(file){
        if(!file) {
            this.$message({
              type: 'error',
              message: '文件不存在',
              duration: 1500,
            });
            return;
        }
        window.open(this.baseUrl+file)
      },
      getDiscussList(page) {
        this.$http.get('discussshangpinxinxi/list', {params: {page, limit: this.pageSize, refid: this.detail.id}}).then(res => {
          if (res.data.code == 0) {
            this.infoList = res.data.data.list;
            this.total = res.data.data.total;
            this.pageSize = res.data.data.pageSize;this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
            this.totalPage = res.data.data.totalPage;
          }
        });
      },
      submitForm(formName) {
        let sensitiveWords = "";
        let sensitiveWordsArr = [];
        if(sensitiveWords) {
            sensitiveWordsArr = sensitiveWords.split(",");
        }
        for(var i=0; i<sensitiveWordsArr.length; i++){
            //全局替换
            var reg = new RegExp(sensitiveWordsArr[i],"g");
            //判断内容中是否包括敏感词
            if (this.form.content.indexOf(sensitiveWordsArr[i]) > -1) {
                // 将敏感词替换为 **
                this.form.content = this.form.content.replace(reg,"**");
            }
        }
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$http.get('orders/list', {params: {page: 1, limit: 1, status: '已完成', goodid: this.detail.id, userid: localStorage.getItem('userid')}}).then(res => {
                if (res.data.code == 0 && res.data.data.list.length == 0) {
                  this.$message({
                    type: 'success',
                    message: '请完成订单后再评论!',
                    duration: 1500
                  });
                  return false
                } else {
                    this.form.refid = this.detail.id;
                    this.form.avatarurl = localStorage.getItem('headportrait')?localStorage.getItem('headportrait'):'';
                    this.$http.post('discussshangpinxinxi/add', this.form).then(res => {
                      if (res.data.code == 0) {
                        this.form.content = '';
                        this.getDiscussList(1);
                        this.$message({
                          type: 'success',
                          message: '评论成功!',
                          duration: 1500,
                        });
                      }
                    });
                }
            });
          } else {
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      getCartList() {
        this.$http.get('cart/list', {params: {userid: localStorage.getItem('userid'), tablename: 'shangpinxinxi', goodid: this.detail.id}}).then(res => {
          if (res.data.code == 0) {
            if (res.data.data.list.length > 0) {
              this.isInCart = true;
            } else {
              this.isInCart = false;
            }
          }
        });
      },
      addCart() {
            // 单次购买限制
            if (this.detail.onelimittimes > 0 && this.detail.onelimittimes < this.buynumber) {
                this.$message.error(`每人单次只能购买${this.detail.onelimittimes}件`);
                return
            }
            // 库存不够
            if (this.detail.alllimittimes <= 0 ) {
                this.$message.error(`茶叶已售罄`);
                return
            }
            // 库存限制
            if (this.detail.alllimittimes > 0 && this.detail.alllimittimes < this.buynumber) {
                this.$message.error(`库存不足`);
                return
            }
        if (this.isInCart) {
          this.$message.error('该茶叶已经添加到购物车');
          return;
        }

        this.cart.buynumber = this.buynumber;
        this.cart.goodid = this.detail.id;
        this.cart.goodname = this.title;
        this.cart.tablename = this.tablename;
        this.cart.zhanghao = this.detail.zhanghao;
        this.cart.goodtype = this.detail.shangpinfenlei;
        this.cart.picture = this.detailBanner[0];
        this.cart.price = (localStorage.getItem('vip')=='是'&&this.detail.vipprice>0)?this.detail.vipprice:this.detail.price;
        this.$http.post('cart/save', this.cart).then(res => {
          if (res.data.code === 0) {
            this.getCartList();
            this.$message({
              message: '添加成功',
              type: 'success',
              duration: 1500,
            });
          } else {
            this.$message.error(res.data.msg);
          }
        });
      },
        //立即购买
        buyNow() {
            // 单次购买限制
            if (this.detail.onelimittimes > 0 && this.detail.onelimittimes < this.buynumber) {
                this.$message.error(`每人单次只能购买${this.detail.onelimittimes}件`);
                return
            }
            // 库存不够
            if (this.detail.alllimittimes <= 0 ) {
                this.$message.error(`茶叶已售罄`);
                return
            }
            // 库存限制
            if (this.detail.alllimittimes > 0 && this.detail.alllimittimes < this.buynumber) {
                this.$message.error(`库存不足`);
                return
            }
            // 保存到storage中，在确认下单页面中获取要购买的茶叶
            localStorage.setItem('orderGoods', JSON.stringify([{
                tablename: this.tablename,
                goodid: this.detail.id,
                goodname: this.title,
                zhanghao: this.detail.zhanghao,
                goodtype: this.detail.shangpinfenlei,

                picture:this.detailBanner[0],
                buynumber: this.buynumber,
                userid: localStorage.getItem('userid'),
                price: (localStorage.getItem('vip')=='是'&&this.detail.vipprice>0)?this.detail.vipprice:this.detail.price,
                discountprice: this.detail.vipprice ? this.detail.vipprice : 0
            }]));
            // 跳转到确认下单页面
            let query = {
                type: 1,
            }
            this.$router.push({path: '/index/shop-order/orderConfirm', query: query});
        },


    },
    components: {
      CountDown
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.detail-page {
		min-height: 100vh;
		background: var(--bg-color);
	}

	.breadcrumb-container {
		background: transparent;
		padding: 25px 0;
		border-bottom: 1px solid #e5e5e0;
		margin-bottom: 40px;
		
		.el-breadcrumb {
			max-width: 1200px;
			margin: 0 auto;
			padding: 0 20px;
		}
	}

	.detail-container {
		max-width: 1200px;
		margin: 0 auto;
		padding: 0 20px 60px;
	}

	.product-main {
		display: flex;
		gap: 60px;
		background: #fff;
		padding: 50px;
		border-radius: var(--radius-main);
		border: 1px solid #f0f0ed;
		margin-bottom: 50px;

		.product-gallery {
			flex: 1.1;
			.el-carousel {
				border-radius: 4px;
				overflow: hidden;
			}
			.main-image { width: 100%; height: 100%; }
		}

		.product-info {
			flex: 1;
			display: flex;
			flex-direction: column;

			.info-header {
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 25px;

				.category-tag {
					background: rgba(166, 137, 102, 0.1);
					color: var(--accent-color);
					padding: 6px 15px;
					border-radius: 2px;
					font-size: 13px;
					font-weight: 600;
					letter-spacing: 1px;
				}

				.wishlist-btn {
					cursor: pointer;
					font-size: 14px;
					color: var(--text-secondary);
					display: flex;
					align-items: center;
					gap: 8px;
					transition: var(--transition-main);
					&:hover { color: var(--accent-color); }
					&.active { color: var(--accent-color); }
					i { font-size: 20px; }
				}
			}

			.product-title {
				font-size: 32px;
				font-weight: 600;
				color: var(--primary-color);
				margin: 0 0 30px;
				line-height: 1.2;
				font-family: serif;
			}

			.price-section {
				background: #fdfdfb;
				padding: 30px;
				border-radius: 4px;
				border-left: 4px solid var(--accent-color);
				margin-bottom: 40px;

				.price-row {
					display: flex;
					align-items: baseline;
					gap: 20px;
					&:not(:last-child) { margin-bottom: 15px; }

					.label { font-size: 14px; color: var(--text-secondary); width: 70px; letter-spacing: 1px; }
					.value { font-size: 28px; font-weight: 700; color: #c0392b; font-family: serif; }
				}

				.vip-price .value { color: var(--primary-color); font-size: 22px; }
			}

			.spec-list {
				margin-bottom: 45px;
				.spec-item {
					display: flex;
					padding: 15px 0;
					border-bottom: 1px solid #f5f5f0;
					font-size: 14px;
					.label { color: var(--text-secondary); width: 100px; opacity: 0.8; }
					.value { color: var(--text-main); font-weight: 500; }
					.highlight { color: var(--accent-color); font-weight: 600; }
				}
			}

			.purchase-actions {
				.quantity-selector {
					display: flex;
					align-items: center;
					gap: 25px;
					margin-bottom: 35px;
					.label { font-size: 14px; color: var(--text-secondary); }
					::v-deep .el-input-number {
						border-radius: 0;
						.el-input__inner { border-radius: 0; border-color: #eee; }
					}
				}

				.button-group {
					display: flex;
					gap: 20px;
					.el-button { flex: 1; height: 50px; font-size: 15px; font-weight: 600; border-radius: 2px; letter-spacing: 2px; }
					.cart-btn { background: #fff; border: 1px solid var(--primary-color); color: var(--primary-color); }
					.buy-btn { background: var(--primary-color); border: none; color: var(--accent-color); }
				}
			}
		}
	}

	.product-tabs {
		background: #fff;
		border-radius: var(--radius-main);
		padding: 50px;
		border: 1px solid #f0f0ed;

		::v-deep .el-tabs--card > .el-tabs__header { border-bottom: 1px solid #f0f0f0; margin-bottom: 40px; }
		::v-deep .el-tabs__item { border: none !important; font-size: 16px; height: 50px; line-height: 50px; transition: var(--transition-main); padding: 0 40px !important; }
		::v-deep .el-tabs__item.is-active { color: var(--primary-color); font-weight: 600; position: relative; }
		::v-deep .el-tabs__item.is-active::after {
			content: '';
			position: absolute;
			bottom: 0; left: 25%; width: 50%; height: 3px; background: var(--accent-color);
		}

		.content-box {
			line-height: 1.8;
			color: var(--text-secondary);
			font-size: 15px;
			img { max-width: 100%; border-radius: 8px; }
		}
	}

	.comment-section {
		.comment-form {
			margin-bottom: 40px;
			.submit-row { display: flex; justify-content: flex-end; gap: 15px; margin-top: 15px; }
		}

		.comment-item {
			padding: 25px 0;
			border-bottom: 1px solid #f0f0f0;

			.user-info {
				display: flex;
				align-items: center;
				gap: 15px;
				margin-bottom: 15px;
				.user-meta {
					display: flex;
					flex-direction: column;
					.name { font-size: 15px; font-weight: 600; color: var(--text-main); }
					.time { font-size: 12px; color: var(--text-light); }
				}
			}

			.comment-content { font-size: 14px; color: var(--text-main); line-height: 1.6; margin-bottom: 10px; }
			.reply-content {
				background: #f8f9fa;
				padding: 12px 15px;
				border-radius: 8px;
				font-size: 13px;
				color: var(--text-secondary);
				.reply-label { font-weight: 600; color: var(--primary-color); }
			}
		}

		.no-comment { text-align: center; padding: 40px; color: var(--text-light); }
	}

	.pagination-wrapper { display: flex; justify-content: center; margin-top: 30px; }
</style>

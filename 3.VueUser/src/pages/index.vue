<template>
	<div class="main-containers">
		<div class="top-container" :style='{"boxShadow":"0 1px 0 rgba(0,0,0,0.05)","padding":"0 40px","margin":"0 auto","overflow":"hidden","alignItems":"center","background":"var(--card-bg)","display":"flex","width":"100%","position":"relative","justifyContent":"space-between","height":"80px","zIndex":"1002"}'>
			<img v-if='true' :style='{"width":"140px","objectFit":"contain","display":"block","height":"50px"}' src='@/assets/logo.png'>
			<div v-if="true" :style='{"margin":"0","color":"var(--primary-color)","textAlign":"center","background":"none","display":"inline-block","fontSize":"22px","fontWeight":"600","letterSpacing":"2px"}'>钀冭寳闃?路 鑼跺彾鍟嗗煄</div>
			<div class="user-actions">
				<div v-if="Token" :style='{"color":"var(--text-secondary)","fontSize":"14px","display":"inline-block","marginRight":"15px"}'>灏婅吹浼氬憳锛歿{username}}</div>
				<el-button v-if="!Token" @click="toLogin()" type="primary" size="small" style="border-radius: 4px;">鐧诲綍 / 娉ㄥ唽</el-button>
                <el-button v-if="Token" @click="logout" size="small" style="border-radius: 4px; border: 1px solid #ddd; color: #666;">瀹夊叏閫€鍑?/el-button>
			</div>
		</div>
		
		
		<div class="body-containers" :style='{"minHeight":"100vh","padding":"0","margin":"0","position":"relative","background":"var(--bg-color)"}'>
			<div class="menu-preview" :style='{"padding":"0","borderColor":"#f0f0f0","textAlign":"center","background":"var(--primary-color)","borderWidth":"0 0 1px 0","width":"100%","borderStyle":"solid","height":"50px"}'>
				<el-menu class="el-menu-horizontal-demo" :style='{"border":0,"padding":"0","margin":"0 auto","borderColor":"transparent","alignItems":"center","display":"flex","justifyContent":"center","listStyle":"none","background":"none","width":"1200px","position":"relative","height":"50px"}' :default-active="activeIndex" :unique-opened="true" mode="horizontal" :router="true" @select="handleSelect">
					<el-menu-item v-for="(menu, index) in menuList" :index="index + ''" :key="index" :route="menu.url">
						<i v-if="true" :style='{"marginRight":"5px","color":"inherit","fontSize":"16px"}' :class="iconArr[index]"></i>
						<span :style='{"fontSize":"15px","color":"inherit"}'>{{menu.name}}</span>
					</el-menu-item>
					<el-menu-item :index="menuList.length + 1 + ''" @click="goMenu('/index/cart')">
						<i v-if="true" :style='{"marginRight":"5px","color":"inherit","fontSize":"16px"}' class="el-icon-shopping-cart-2"></i>
						<span :style='{"fontSize":"15px","color":"inherit"}'>璐墿杞?/span>
					</el-menu-item>
					<el-menu-item :index="menuList.length + 2 + ''" v-if="Token && notAdmin" @click="goMenu('/index/center')">
						<i v-if="true" :style='{"marginRight":"5px","color":"inherit","fontSize":"16px"}' class="el-icon-user"></i>
						<span :style='{"fontSize":"15px","color":"inherit"}'>涓汉涓績</span>
					</el-menu-item>
				</el-menu>
			</div>
			
			<div v-if="showBanner" class="banner-preview" :style='{"width":"100%","margin":"0","height":"auto"}'>
				<el-carousel :style='{"width":"100%","margin":"0 auto"}' trigger="click" indicator-position="inside" arrow="always" type="default" direction="horizontal" :height="bannerHeight" :autoplay="true" :interval="3000" :loop="true">
					<el-carousel-item :style='{"borderRadius":"0","width":"100%","height":"100%"}' v-for="item in carouselList" :key="item.id">
						<el-image class="banner-image" :style='{"width":"100%","height":"100%"}' :src="baseUrl + item.value" fit="contain"></el-image>
					</el-carousel-item>
				</el-carousel>
			</div>
			
			<router-view></router-view>
			
			<div class="bottom-preview" :style='{"padding":"60px 0","background":"#1B3022","color":"#fff","width":"100%","borderTop":"4px solid var(--accent-color)"}'>
				<div style="max-width: 1200px; margin: 0 auto; display: flex; justify-content: space-between; align-items: flex-start;">
					<div style="flex: 1.5;">
						<div style="font-size: 24px; color: var(--accent-color); font-weight: 600; margin-bottom: 20px;">钀冭寳闃?路 鑼跺彾鍟嗗煄</div>
						<p style="color: rgba(255,255,255,0.6); font-size: 14px; line-height: 1.8; max-width: 400px;">
							鎵胯鍗冨勾鑼堕亾绮鹃珦锛岃崯钀冨悕灞变紭璐ㄥソ鑼躲€傛垜浠嚧鍔涗簬涓虹埍鑼朵箣浜烘彁渚涙渶绾銆侀珮鍝佽川鐨勯ギ鑼朵綋楠岋紝璁╂瘡涓€鍙惰尪棣欓兘鑳戒紶閫掍竴浠藉畞闈欎笌鍠滄偊銆?
						</p>
					</div>
					<div style="flex: 1; display: flex; justify-content: space-around;">
						<div>
							<h4 style="color: #fff; margin-bottom: 20px;">閫夎喘鎸囧崡</h4>
							<ul style="list-style: none; padding: 0; font-size: 14px; color: rgba(255,255,255,0.6); line-height: 2.5;">
								<li>缁胯尪绯诲垪</li>
								<li>绾㈣尪鐗归€?/li>
								<li>涔岄緳鍚嶅搧</li>
								<li>鏅幢闄堥</li>
							</ul>
						</div>
						<div>
							<h4 style="color: #fff; margin-bottom: 20px;">鍏充簬鎴戜滑</h4>
							<ul style="list-style: none; padding: 0; font-size: 14px; color: rgba(255,255,255,0.6); line-height: 2.5;">
								<li>鍝佺墝鏁呬簨</li>
								<li>鑱旂郴鎴戜滑</li>
								<li>鏈嶅姟澹版槑</li>
								<li>鍔犲叆鎴戜滑</li>
							</ul>
						</div>
					</div>
					<div style="flex: 0.8; text-align: right;">
						<h4 style="color: #fff; margin-bottom: 20px;">鏈嶅姟鐑嚎</h4>
						<div style="font-size: 20px; color: var(--accent-color); font-weight: 600;">400-888-6666</div>
						<p style="font-size: 12px; color: rgba(255,255,255,0.4); margin-top: 10px;">鍛ㄤ竴鑷冲懆鏃?9:00 - 18:00</p>
					</div>
				</div>
				<div style="max-width: 1200px; margin: 40px auto 0; padding-top: 20px; border-top: 1px solid rgba(255,255,255,0.1); text-align: center; color: rgba(255,255,255,0.4); font-size: 12px;">
                    © 2026 高山茶智能决策平台 版权所有
				</div>
			</div>
		</div>
		
	</div>
</template>

<script>
import Vue from 'vue'
export default {
    data() {
		return {
            activeIndex: '0',
			roleMenus: [],
			baseUrl: '',
			carouselList: [],
			bannerHeight: '500px',
			menuList: [],
			form: {
				ask: '',
				userid: localStorage.getItem('userid')
			},
			Token: localStorage.getItem('Token'),
            username: localStorage.getItem('username'),
            notAdmin: localStorage.getItem('sessionTable')!='"users"',
			timer: '',
			iconArr: [
				'el-icon-star-off',
				'el-icon-goods',
				'el-icon-warning',
				'el-icon-question',
				'el-icon-info',
				'el-icon-help',
				'el-icon-picture-outline-round',
				'el-icon-camera-solid',
				'el-icon-video-camera-solid',
				'el-icon-video-camera',
				'el-icon-bell',
				'el-icon-s-cooperation',
				'el-icon-s-order',
				'el-icon-s-platform',
				'el-icon-s-operation',
				'el-icon-s-promotion',
				'el-icon-s-release',
				'el-icon-s-ticket',
				'el-icon-s-management',
				'el-icon-s-open',
				'el-icon-s-shop',
				'el-icon-s-marketing',
				'el-icon-s-flag',
				'el-icon-s-comment',
				'el-icon-s-finance',
				'el-icon-s-claim',
				'el-icon-s-opportunity',
				'el-icon-s-data',
				'el-icon-s-check'
			],	
		}
    },
	computed: {
		showBanner() {
			return this.$route.path === '/index/home';
		}
	},
    created() {
		this.baseUrl = this.$config.baseUrl;
		this.menuList = this.$config.indexNav;
		this.getCarousel();
    },
    mounted() {
        this.activeIndex = localStorage.getItem('keyPath') || '0';
		this.updateBannerHeight();
		window.addEventListener('resize', this.updateBannerHeight);
    },
	beforeDestroy() {
		window.removeEventListener('resize', this.updateBannerHeight);
	},
    watch: {
        $route(newValue) {
            let that = this
            let url = window.location.href
            let arr = url.split('#')
            for (let x in this.menuList) {
                if (newValue.path == this.menuList[x].url) {
                    this.activeIndex = x
                }
            }
            this.Token = localStorage.getItem('Token')
        },
    },
    methods: {
        handleSelect(keyPath) {
            if (keyPath) {
                localStorage.setItem('keyPath', keyPath)
            }
        },
		toLogin() {
		  this.$router.push('/login');
		},
        logout() {
            localStorage.clear();
            Vue.http.headers.common['Token'] = "";
            this.$router.push('/index/home');
            this.activeIndex = '0'
            localStorage.setItem('keyPath', this.activeIndex)
            this.Token = ''
            this.$forceUpdate()
            this.$message({
                message: '鐧诲嚭鎴愬姛',
                type: 'success',
                duration: 1000,
            });
        },
		getCarousel() {
			this.$http.get('config/list', {params: { page: 1, limit: 3 }}).then(res => {
				if (res.data.code == 0) {
					this.carouselList = res.data.data.list;
				}
			});
		},
		updateBannerHeight() {
			const viewportWidth = window.innerWidth || document.documentElement.clientWidth || 1200;
			const height = Math.round(viewportWidth * 625 / 1608);
			const boundedHeight = Math.max(146, Math.min(500, height));
			this.bannerHeight = `${boundedHeight}px`;
		},
		goBackend() {
			window.open(`${this.$config.baseUrl}admin/dist/index.html`, "_blank");
		},
		goMenu(path) {
            if (!localStorage.getItem('Token')) {
                this.toLogin();
            } else {
                this.$router.push(path);
            }
		},
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.menu-preview {
	  .el-scrollbar {
	    height: 100%;
	
	    & ::v-deep .scrollbar-wrapper {
	      overflow-x: hidden;
	    }
	  }
	}
	
	
	.menu-preview .el-menu-horizontal-demo .el-menu-item {
		cursor: pointer;
		padding: 0 30px;
		color: rgba(255, 255, 255, 0.7);
		white-space: nowrap;
		display: block;
		font-size: 15px;
		line-height: 50px;
		background: none;
		border: none !important;
		transition: all 0.3s;
		height: 50px;
	}
	
	.menu-preview .el-menu-horizontal-demo .el-menu-item:hover {
		color: var(--accent-color) !important;
		background: rgba(255, 255, 255, 0.05) !important;
	}
	
	.menu-preview .el-menu-horizontal-demo .el-menu-item.is-active {
		color: #fff !important;
		font-weight: 600;
		background: rgba(255, 255, 255, 0.1) !important;
		position: relative;
		&::after {
			content: '';
			position: absolute;
			bottom: 0;
			left: 20%;
			width: 60%;
			height: 3px;
			background: var(--accent-color);
		}
	}
	
	.banner-preview {
	  background: #eef5ef;

	  .el-carousel ::v-deep .el-carousel__indicator button {
	    width: 0;
	    height: 0;
	    display: none;
	  }
	}

	.banner-preview ::v-deep .banner-image {
		display: block;
		background: #eef5ef;
	}

	.banner-preview ::v-deep .banner-image .el-image__inner {
		width: 100%;
		height: 100%;
		object-fit: contain;
		display: block;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--left {
		width: 36px;
		font-size: 12px;
		height: 36px;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--left:hover {
		background: #2087c3;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--right {
		width: 36px;
		font-size: 12px;
		height: 36px;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--right:hover {
		background: #2087c3;
	}

	.banner-preview .el-carousel ::v-deep .el-carousel__indicators {
		padding: 0;
		margin: 0 0 12px 0;
		z-index: 2;
		position: absolute;
		list-style: none;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__indicators li {
		border-radius: 50%;
		padding: 0;
		margin: 0 4px;
		background: #fff;
		display: inline-block;
		width: 12px;
		opacity: 0.4;
		transition: 0.3s;
		height: 12px;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__indicators li:hover {
		border-radius: 50%;
		padding: 0;
		margin: 0 4px;
		background: #fff;
		display: inline-block;
		width: 12px;
		opacity: 0.7;
		height: 12px;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__indicators li.is-active {
		border-radius: 50%;
		padding: 0;
		margin: 0 4px;
		background: #fff;
		display: inline-block;
		width: 12px;
		opacity: 1;
		height: 12px;
	}

    .chat-content {
      .left-content {
          width: 100%;
          text-align: left;
      }
      .right-content {
          width: 100%;
          text-align: right;
      }
    }
</style>


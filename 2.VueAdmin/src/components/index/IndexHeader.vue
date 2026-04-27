<template>
	<div class="navbar">
		<div class="title-container">
			<el-image v-if="false" class="title-img" src="http://codegen.caihongy.cn/20201114/7856ba26477849ea828f481fa2773a95.jpg" fit="cover" />
			<span class="title-name">{{this.$project.projectName}}</span>
		</div>
		<div class="right-container">
			<div class="user-info">
				<i class="el-icon-user"></i>
				<span class="nickname">{{this.$storage.get('role')}} {{this.$storage.get('adminName')}}</span>
			</div>
			<div v-if="this.$storage.get('role')!='管理员'" class="nav-btn" @click="onIndexTap">
				<i class="el-icon-monitor"></i>
				<span>前台首页</span>
			</div>
			<div class="nav-btn logout" @click="onLogout">
				<i class="el-icon-switch-button"></i>
				<span>退出登录</span>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				dialogVisible: false,
				ruleForm: {},
				user: {},
			};
		},
		created() {
			
		},
		mounted() {
			let sessionTable = this.$storage.get("sessionTable")
			this.$http({
				url: sessionTable + '/session',
				method: "get"
			}).then(({
				data
			}) => {
				if (data && data.code === 0) {
					this.user = data.data;
					this.$storage.set('userid',data.data.id);
				} else {
					let message = this.$message
					message.error(data.msg);
				}
			});
		},
		methods: {
			onLogout() {
				let storage = this.$storage
				let router = this.$router
				storage.clear()
				router.replace({
					name: "login"
				});
			},
			onIndexTap(){
				window.location.href = `${this.$base.indexUrl}`
			},
		}
	};
</script>


<style lang="scss" scoped>
	.navbar {
		height: 64px;
		background: rgba(45, 90, 39, 0.9);
		backdrop-filter: blur(10px);
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 24px;
		box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
		position: relative;
		z-index: 1001;

		.title-container {
			display: flex;
			align-items: center;
			
			.title-img {
				width: 36px;
				height: 36px;
				border-radius: 50%;
				margin-right: 12px;
				border: 2px solid rgba(255,255,255,0.2);
			}

			.title-name {
				font-size: 20px;
				font-weight: 600;
				color: #ffffff;
				letter-spacing: 1px;
			}
		}

		.right-container {
			display: flex;
			align-items: center;
			gap: 20px;

			.user-info {
				display: flex;
				align-items: center;
				color: rgba(255,255,255,0.9);
				font-size: 14px;
				background: rgba(255,255,255,0.1);
				padding: 6px 12px;
				border-radius: 20px;
				
				i {
					margin-right: 6px;
				}
			}

			.nav-btn {
				display: flex;
				align-items: center;
				color: rgba(255,255,255,0.8);
				font-size: 14px;
				cursor: pointer;
				transition: all 0.3s;
				padding: 6px 12px;
				border-radius: 6px;

				i {
					margin-right: 4px;
					font-size: 16px;
				}

				&:hover {
					color: #ffffff;
					background: rgba(255,255,255,0.1);
				}

				&.logout:hover {
					color: #ff7675;
				}
			}
		}
	}
</style>

<template>
<div class="login-wrapper">
	<div class="login-container">
		<div class="login-box fade-in-up">
			<div class="login-header">
				<div class="logo-text">萃茗阁</div>
				<h2>欢迎回来</h2>
				<p>品味名茶之韵，感悟人生之静</p>
			</div>
			
			<el-form ref="loginForm" :model="loginForm" :rules="rules" class="login-form">
				<el-form-item prop="username">
					<el-input 
						v-model="loginForm.username" 
						placeholder="请输入账户" 
						prefix-icon="el-icon-user"
					></el-input>
				</el-form-item>
				
				<el-form-item prop="password">
					<el-input 
						v-model="loginForm.password" 
						placeholder="请输入密码" 
						type="password" 
						prefix-icon="el-icon-lock"
						show-password
					></el-input>
				</el-form-item>
				
				<el-form-item v-if="roles.length > 1" class="role-selection">
					<el-radio-group v-model="loginForm.tableName">
						<el-radio 
							v-for="(item, index) in roles" 
							:key="index" 
							:label="item.tableName"
							@change="getCurrentRow(item)"
						>{{item.roleName}}</el-radio>
					</el-radio-group>
				</el-form-item>
				
				<div class="form-actions">
					<el-button type="primary" class="submit-btn" @click="submitForm('loginForm')">立即登录</el-button>
				</div>
				
				<div class="form-footer">
					<router-link 
						v-for="(item, index) in roles" 
						:key="index"
						v-if="item.hasFrontRegister=='是'"
						:to="{path: '/register', query: {role: item.tableName, pageFlag:'register'}}"
						class="register-link"
					>注册{{item.roleName}}</router-link>
					<span class="reset-link" @click="resetForm('loginForm')">重置</span>
				</div>
			</el-form>
		</div>
	</div>
</div>
</template>

<script>

export default {
	//数据集合
	data() {
		return {
            baseUrl: this.$config.baseUrl,
            loginType: 1,
			roleMenus: [{"backMenu":[{"child":[{"appFrontIcon":"cuIcon-clothes","buttons":["新增","查看","修改","删除"],"menu":"用户","menuJump":"列表","tableName":"yonghu"}],"menu":"用户管理"},{"child":[{"appFrontIcon":"cuIcon-flashlightopen","buttons":["新增","查看","修改","删除"],"menu":"商家","menuJump":"列表","tableName":"shangjia"}],"menu":"商家管理"},{"child":[{"appFrontIcon":"cuIcon-brand","buttons":["新增","查看","修改","删除"],"menu":"茶叶分类","menuJump":"列表","tableName":"shangpinfenlei"}],"menu":"茶叶分类管理"},{"child":[{"appFrontIcon":"cuIcon-newshot","buttons":["查看","修改","删除","查看评论"],"menu":"茶叶信息","menuJump":"列表","tableName":"shangpinxinxi"}],"menu":"茶叶信息管理"},{"child":[{"appFrontIcon":"cuIcon-news","buttons":["新增","查看","修改","删除"],"menu":"商城资讯","tableName":"news"},{"appFrontIcon":"cuIcon-cardboard","buttons":["查看","修改"],"menu":"关于我们","tableName":"aboutus"},{"appFrontIcon":"cuIcon-form","buttons":["查看","修改"],"menu":"轮播图管理","tableName":"config"}],"menu":"系统管理"}],"frontMenu":[{"child":[{"appFrontIcon":"cuIcon-flashlightopen","buttons":["查看"],"menu":"茶叶信息列表","menuJump":"列表","tableName":"shangpinxinxi"}],"menu":"茶叶信息模块"}],"hasBackLogin":"是","hasBackRegister":"否","hasFrontLogin":"否","hasFrontRegister":"否","roleName":"管理员","tableName":"users"},{"backMenu":[],"frontMenu":[{"child":[{"appFrontIcon":"cuIcon-flashlightopen","buttons":["查看"],"menu":"茶叶信息列表","menuJump":"列表","tableName":"shangpinxinxi"}],"menu":"茶叶信息模块"}],"hasBackLogin":"否","hasBackRegister":"否","hasFrontLogin":"是","hasFrontRegister":"是","roleName":"用户","tableName":"yonghu"},{"backMenu":[{"child":[{"appFrontIcon":"cuIcon-newshot","buttons":["新增","查看","修改","删除","查看评论"],"menu":"茶叶信息","menuJump":"列表","tableName":"shangpinxinxi"}],"menu":"茶叶信息管理"},{"child":[{"appFrontIcon":"cuIcon-goods","buttons":["查看","删除"],"menu":"已退款订单","tableName":"orders/已退款"},{"appFrontIcon":"cuIcon-flashlightopen","buttons":["查看","删除"],"menu":"未支付订单","tableName":"orders/未支付"},{"appFrontIcon":"cuIcon-present","buttons":["查看","删除"],"menu":"已发货订单","tableName":"orders/已发货"},{"appFrontIcon":"cuIcon-goodsnew","buttons":["查看","发货","删除"],"menu":"已支付订单","tableName":"orders/已支付"},{"appFrontIcon":"cuIcon-explore","buttons":["查看","删除","日销量","月销量","品销量","类销量","月销额","日销额","品销额","类销额"],"menu":"已完成订单","tableName":"orders/已完成"},{"appFrontIcon":"cuIcon-pic","buttons":["查看","删除"],"menu":"已取消订单","tableName":"orders/已取消"}],"menu":"订单管理"}],"frontMenu":[{"child":[{"appFrontIcon":"cuIcon-flashlightopen","buttons":["查看"],"menu":"茶叶信息列表","menuJump":"列表","tableName":"shangpinxinxi"}],"menu":"茶叶信息模块"}],"hasBackLogin":"是","hasBackRegister":"是","hasFrontLogin":"否","hasFrontRegister":"否","roleName":"商家","tableName":"shangjia"}],
			loginForm: {
				username: '',
				password: '',
				tableName: '',
				code: '',
			},
			role: '',
            roles: [],
			rules: {
				username: [
					{ required: true, message: '请输入账户', trigger: 'blur' }
				],
				password: [
					{ required: true, message: '请输入密码', trigger: 'blur' }
				]
			},
			codes: [{
				num: 1,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 2,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 3,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 4,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}]
		}
	},
	created() {
        for(let item in this.roleMenus) {
            if(this.roleMenus[item].hasFrontLogin=='是') {
                this.roles.push(this.roleMenus[item]);
            }
        }
	},
	mounted() {
	},
    //方法集合
    methods: {
		randomString() {
			var len = 4;
			var chars = [
			  'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			  'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			  'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			  'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			  'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
			  '3', '4', '5', '6', '7', '8', '9'
			]
			var colors = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f']
			var sizes = ['14', '15', '16', '17', '18']
			
			var output = []
			for (var i = 0; i < len; i++) {
			  // 随机验证码
			  var key = Math.floor(Math.random() * chars.length)
			  this.codes[i].num = chars[key]
			  // 随机验证码颜色
			  var code = '#'
			  for (var j = 0; j < 6; j++) {
			    var key = Math.floor(Math.random() * colors.length)
			    code += colors[key]
			  }
			  this.codes[i].color = code
			  // 随机验证码方向
			  var rotate = Math.floor(Math.random() * 45)
			  var plus = Math.floor(Math.random() * 2)
			  if (plus == 1) rotate = '-' + rotate
			  this.codes[i].rotate = 'rotate(' + rotate + 'deg)'
			  // 随机验证码字体大小
			  var size = Math.floor(Math.random() * sizes.length)
			  this.codes[i].size = sizes[size] + 'px'
			}
		},
      getCurrentRow(row) {
        this.role = row.roleName;
      },
      submitForm(formName) {
        if (this.roles.length!=1) {
            if (!this.role) {
                this.$message.error("请选择登录用户类型");
                return false;
            }
        } else {
            this.role = this.roles[0].roleName;
            this.loginForm.tableName = this.roles[0].tableName;
        }
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$http.get(`${this.loginForm.tableName}/login`, {params: this.loginForm}).then(res => {
              if (res.data.code === 0) {
                localStorage.setItem('Token', res.data.token);
                localStorage.setItem('UserTableName', this.loginForm.tableName);
                localStorage.setItem('username', this.loginForm.username);
                localStorage.setItem('adminName', this.loginForm.username);
                localStorage.setItem('sessionTable', this.loginForm.tableName);
                localStorage.setItem('role', this.role);
                localStorage.setItem('keyPath', this.$config.indexNav.length+2);
                this.$router.push('/index/center');
                this.$message({
                  message: '登录成功',
                  type: 'success',
                  duration: 1500,
                });
              } else {
                this.$message.error(res.data.msg);
              }
            });
          } else {
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.login-wrapper {
		min-height: 100vh;
		display: flex;
		align-items: center;
		justify-content: center;
		background: #1B3022;
		background-image: linear-gradient(rgba(27, 48, 34, 0.8), rgba(27, 48, 34, 0.8)), url(https://images.unsplash.com/photo-1594631252845-29fc458695d3?q=80&w=2000&auto=format&fit=crop);
		background-size: cover;
		background-position: center;
		position: relative;
	}

	.login-container {
		position: relative;
		z-index: 1;
		width: 100%;
		display: flex;
		justify-content: center;
	}

	.login-box {
		width: 440px;
		padding: 60px 50px;
		background: #fff;
		border-radius: 4px;
		box-shadow: 0 20px 50px rgba(0,0,0,0.3);
		position: relative;
		&::before {
			content: '';
			position: absolute;
			top: 10px; left: 10px; right: 10px; bottom: 10px;
			border: 1px solid rgba(166, 137, 102, 0.2);
			pointer-events: none;
		}
		
		.login-header {
			text-align: center;
			margin-bottom: 45px;

			.logo-text {
				font-size: 32px;
				color: var(--primary-color);
				font-weight: 600;
				letter-spacing: 4px;
				margin-bottom: 15px;
				font-family: serif;
			}

			h2 {
				font-size: 20px;
				color: var(--text-main);
				margin: 0 0 10px;
				font-weight: 500;
				letter-spacing: 1px;
			}

			p {
				font-size: 13px;
				color: var(--text-secondary);
				margin: 0;
				font-style: italic;
				opacity: 0.7;
			}
		}
	}

	.login-form {
		.el-form-item {
			margin-bottom: 25px;
		}

		::v-deep .el-input__inner {
			height: 45px;
			line-height: 45px;
			border-radius: 0;
			border: none;
			border-bottom: 1px solid #e0e0e0;
			padding-left: 35px;
			background: transparent;
			transition: var(--transition-main);

			&:focus {
				border-bottom-color: var(--accent-color);
			}
		}

		::v-deep .el-input__prefix {
			left: 5px;
			color: #b2bec3;
		}

		.role-selection {
			text-align: center;
			margin: 15px 0 30px;
			::v-deep .el-radio__label {
				font-size: 13px;
			}
			::v-deep .el-radio__input.is-checked .el-radio__inner {
				background-color: var(--accent-color);
				border-color: var(--accent-color);
			}
			::v-deep .el-radio__input.is-checked + .el-radio__label {
				color: var(--accent-color);
			}
		}

		.submit-btn {
			width: 100%;
			height: 45px;
			font-size: 15px;
			font-weight: 600;
			border-radius: 0;
			letter-spacing: 4px;
			margin-bottom: 25px;
			background: var(--primary-color) !important;
			border: none !important;
			color: var(--accent-color) !important;
		}

		.form-footer {
			display: flex;
			justify-content: space-between;
			align-items: center;
			font-size: 13px;

			.register-link {
				color: var(--accent-color);
				text-decoration: none;
				font-weight: 500;
				transition: 0.3s;
				&:hover { opacity: 0.8; text-decoration: underline; }
			}

			.reset-link {
				color: var(--text-secondary);
				cursor: pointer;
				transition: 0.3s;
				&:hover { color: var(--text-main); }
			}
		}
	}
</style>

<template>
	<div class="register-wrapper">
		<div class="register-container">
			<el-form class='rgs-form fade-in-up' v-if="pageFlag=='register'" ref="registerForm" :model="registerForm" :rules="rules">
				<div class="register-header">
					<div class="logo-text">萃茗阁</div>
					<h2>账号注册</h2>
					<p>加入萃茗阁，探索源头好茶</p>
				</div>

				<el-form-item v-if="tableName=='yonghu'" prop="yonghuming" class="form-item">
					<el-input v-model="registerForm.yonghuming" placeholder="请输入用户名" prefix-icon="el-icon-user" />
				</el-form-item>
				
				<el-form-item v-if="tableName=='yonghu'" prop="mima" class="form-item">
					<el-input v-model="registerForm.mima" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock" show-password />
				</el-form-item>
				
				<el-form-item v-if="tableName=='yonghu'" prop="mima2" class="form-item">
					<el-input v-model="registerForm.mima2" type="password" placeholder="请再次输入密码" prefix-icon="el-icon-circle-check" show-password />
				</el-form-item>
				
				<el-form-item v-if="tableName=='yonghu'" prop="xingming" class="form-item">
					<el-input v-model="registerForm.xingming" placeholder="请输入姓名" prefix-icon="el-icon-edit" />
				</el-form-item>

				<el-form-item v-if="tableName=='yonghu'" prop="shouji" class="form-item">
					<el-input v-model="registerForm.shouji" placeholder="请输入手机" prefix-icon="el-icon-mobile-phone" />
				</el-form-item>
				
				<el-form-item v-if="tableName=='yonghu'" prop="xingbie" class="form-item">
					<el-select v-model="registerForm.xingbie" placeholder="请选择性别" style="width: 100%">
						<el-option v-for="(item,index) in yonghuxingbieOptions" :key="index" :label="item" :value="item"></el-option>
					</el-select>
				</el-form-item>

				<el-form-item v-if="tableName=='shangjia'" prop="zhanghao" class="form-item">
					<el-input v-model="registerForm.zhanghao" placeholder="请输入账号" prefix-icon="el-icon-user" />
				</el-form-item>

				<el-form-item v-if="tableName=='shangjia'" prop="mima" class="form-item">
					<el-input v-model="registerForm.mima" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock" show-password />
				</el-form-item>

				<el-form-item v-if="tableName=='shangjia'" prop="shangjiaxingming" class="form-item">
					<el-input v-model="registerForm.shangjiaxingming" placeholder="请输入商家姓名" prefix-icon="el-icon-edit" />
				</el-form-item>

				<div class="form-actions">
					<el-button type="primary" class="submit-btn" @click="submitForm('registerForm')">提交注册</el-button>
				</div>
				
				<div class="form-footer">
					<router-link to="/login" class="login-link">已有账号？立即登录</router-link>
					<span class="reset-link" @click="resetForm('registerForm')">重置</span>
				</div>
			</el-form>
		</div>
	</div>
</template>

<script>

export default {
    //数据集合
    data() {
		return {
            pageFlag : '',
			tableName: '',
			registerForm: {
                xingbie: '',
                vip: '',
                xingbie: '',
            },
			rules: {},
            yonghuxingbieOptions: [],
            shangjiaxingbieOptions: [],
		}
    },
	mounted() {
	},
    created() {
      this.pageFlag = this.$route.query.pageFlag;
      this.tableName = this.$route.query.role;
      if ('yonghu' == this.tableName) {
        this.rules.yonghuming = [{ required: true, message: '请输入用户名', trigger: 'blur' }];
      }
      if ('yonghu' == this.tableName) {
        this.rules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }];
      }
      if ('yonghu' == this.tableName) {
        this.rules.xingming = [{ required: true, message: '请输入姓名', trigger: 'blur' }];
      }
        this.yonghuxingbieOptions = "男,女".split(',');
      if ('yonghu' == this.tableName) {
        this.rules.youxiang = [{ required: true, validator: this.$validate.isEmail, trigger: 'blur' }];
      }
      if ('yonghu' == this.tableName) {
        this.rules.shouji = [{ required: true, validator: this.$validate.isMobile, trigger: 'blur' }];
      }
      if ('yonghu' == this.tableName) {
        this.rules.money = [{ required: true, validator: this.$validate.isNumber, trigger: 'blur' }];
      }
      if ('shangjia' == this.tableName) {
        this.rules.zhanghao = [{ required: true, message: '请输入账号', trigger: 'blur' }];
      }
      if ('shangjia' == this.tableName) {
        this.rules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }];
      }
      if ('shangjia' == this.tableName) {
        this.rules.shangjiaxingming = [{ required: true, message: '请输入商家姓名', trigger: 'blur' }];
      }
        this.shangjiaxingbieOptions = "男,女".split(',');
      if ('shangjia' == this.tableName) {
        this.rules.youxiang = [{ required: true, validator: this.$validate.isEmail, trigger: 'blur' }];
      }
      if ('shangjia' == this.tableName) {
        this.rules.lianxidianhua = [{ required: true, validator: this.$validate.isMobile, trigger: 'blur' }];
      }
      if ('shangjia' == this.tableName) {
        this.rules.money = [{ required: true, validator: this.$validate.isNumber, trigger: 'blur' }];
      }
    },
    //方法集合
    methods: {
      // 获取uuid
      getUUID () {
        return new Date().getTime();
      },
        // 下二随
      yonghutouxiangUploadChange(fileUrls) {
          this.registerForm.touxiang = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");
      },
      shangjiatouxiangUploadChange(fileUrls) {
          this.registerForm.touxiang = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");
      },

        // 多级联动参数


      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            var url=this.tableName+"/register";
               if(`yonghu` == this.tableName && this.registerForm.mima!=this.registerForm.mima2) {
                this.$message.error(`两次密码输入不一致`);
                return
               }
               if(`shangjia` == this.tableName && this.registerForm.mima!=this.registerForm.mima2) {
                this.$message.error(`两次密码输入不一致`);
                return
               }
            this.$http.post(url, this.registerForm).then(res => {
              if (res.data.code === 0) {
                this.$message({
                  message: '注册成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.$router.push('/login');
                  }
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
	.register-wrapper {
		min-height: 100vh;
		display: flex;
		align-items: center;
		justify-content: center;
		background: #1B3022;
		background-image: linear-gradient(rgba(27, 48, 34, 0.8), rgba(27, 48, 34, 0.8)), url(https://pic.imgdb.cn/item/65eac96d9f345e8d03c6aa2b.jpg);
		background-size: cover;
		background-position: center;
	}

	.register-container {
		width: 100%;
		max-width: 500px;
		padding: 20px;
	}

	.rgs-form {
		background: #fff;
		padding: 50px 40px;
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

		.register-header {
			text-align: center;
			margin-bottom: 40px;

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

		.form-item {
			margin-bottom: 20px;
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
		}

		.submit-btn {
			width: 100%;
			height: 45px;
			font-size: 15px;
			font-weight: 600;
			border-radius: 0;
			letter-spacing: 4px;
			margin: 20px 0;
			background: var(--primary-color) !important;
			border: none !important;
			color: var(--accent-color) !important;
		}

		.form-footer {
			display: flex;
			justify-content: space-between;
			align-items: center;
			font-size: 13px;

			.login-link {
				color: var(--accent-color);
				text-decoration: none;
				font-weight: 500;
				&:hover { opacity: 0.8; text-decoration: underline; }
			}

			.reset-link {
				color: var(--text-secondary);
				cursor: pointer;
				&:hover { color: var(--text-main); }
			}
		}
	}
</style>

<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-left">
        <div class="overlay">
          <h1 class="brand-title">Tea Mall</h1>
          <p class="brand-subtitle">茶叶商城管理系统</p>
          <div class="brand-decor"></div>
        </div>
      </div>
      <div class="login-right">
        <el-form class="login-form">
          <div class="form-header">
            <h2 class="form-title">欢迎登录</h2>
            <p class="form-subtitle">Management System Access</p>
          </div>

          <div class="form-body">
            <div class="input-item">
              <label>用户名</label>
              <el-input 
                placeholder="请输入用户名" 
                v-model="rulesForm.username"
                prefix-icon="el-icon-user"
              ></el-input>
            </div>

            <div class="input-item">
              <label>密码</label>
              <el-input 
                placeholder="请输入密码" 
                type="password" 
                v-model="rulesForm.password"
                prefix-icon="el-icon-lock"
                show-password
                @keyup.enter.native="login()"
              ></el-input>
            </div>

            <div v-if="roles.length > 1" class="role-selection">
              <label>登录角色</label>
              <el-radio-group v-model="rulesForm.role" class="role-group">
                <el-radio v-for="item in roles" :key="item.roleName" :label="item.roleName">
                  {{item.roleName}}
                </el-radio>
              </el-radio-group>
            </div>

            <div class="form-actions">
              <el-button type="primary" @click="login()" class="submit-btn" :loading="loading">
                立即登录
              </el-button>
              <div class="secondary-actions">
                <span>还没有账号？</span>
                <el-button type="text" @click="register('shangjia')" class="reg-btn">
                  商家入驻
                </el-button>
              </div>
            </div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import menu from "@/utils/menu";
export default {
  data() {
    return {
      baseUrl: this.$base.url,
      loading: false,
      rulesForm: {
        username: "",
        password: "",
        role: "",
      },
      menus: [],
      roles: [],
      tableName: "",
    };
  },
  mounted() {
    let menus = menu.list();
    this.menus = menus;
    for (let i = 0; i < this.menus.length; i++) {
      if (this.menus[i].hasBackLogin == '是') {
        this.roles.push(this.menus[i])
      }
    }
    if (this.roles.length > 0) {
      this.rulesForm.role = this.roles[0].roleName;
    }
  },
  methods: {
    register(tableName) {
      this.$storage.set("loginTable", tableName);
      this.$storage.set("pageFlag", "register");
      this.$router.push({ path: '/register' })
    },
    login() {
      if (!this.rulesForm.username) {
        this.$message.error("请输入用户名");
        return;
      }
      if (!this.rulesForm.password) {
        this.$message.error("请输入密码");
        return;
      }
      if (!this.rulesForm.role) {
        this.$message.error("请选择角色");
        return;
      }

      this.loading = true;
      let menus = this.menus;
      for (let i = 0; i < menus.length; i++) {
        if (menus[i].roleName == this.rulesForm.role) {
          this.tableName = menus[i].tableName;
        }
      }

      this.$http({
        url: `${this.tableName}/login?username=${this.rulesForm.username}&password=${this.rulesForm.password}`,
        method: "post"
      }).then(({ data }) => {
        this.loading = false;
        if (data && data.code === 0) {
          this.$storage.set("Token", data.token);
          this.$storage.set("role", this.rulesForm.role);
          this.$storage.set("sessionTable", this.tableName);
          this.$storage.set("adminName", this.rulesForm.username);
          this.$router.replace({ path: "/index/" });
        } else {
          this.$message.error(data.msg);
        }
      }).catch(() => {
        this.loading = false;
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  position: relative;
  overflow: hidden;

  .login-box {
    width: 100vw;
    height: 100vh;
    display: flex;
    position: relative;

    .login-left {
      flex: 1;
      background: url(https://pic.imgdb.cn/item/65eac96d9f345e8d03c6aa2b.jpg) center/cover;
      position: relative;
      min-width: 60%;

      .overlay {
        position: absolute;
        inset: 0;
        background: linear-gradient(135deg, rgba(45, 90, 39, 0.75), rgba(121, 149, 43, 0.55));
        display: flex;
        flex-direction: column;
        justify-content: center;
        padding: 80px;
        color: #ffffff;

        .brand-title {
          font-size: 56px;
          font-weight: 700;
          margin: 0;
          letter-spacing: 3px;
        }

        .brand-subtitle {
          font-size: 22px;
          opacity: 0.9;
          margin: 12px 0 35px;
        }

        .brand-decor {
          width: 70px;
          height: 4px;
          background: #ffffff;
          border-radius: 2px;
        }
      }
    }

    .login-right {
      width: 420px;
      min-width: 420px;
      padding: 60px 50px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      background: #ffffff;
      box-shadow: -10px 0 40px rgba(0, 0, 0, 0.1);

      .form-header {
        margin-bottom: 40px;

        .form-title {
          font-size: 28px;
          color: #2c3e50;
          margin: 0;
          font-weight: 600;
        }

        .form-subtitle {
          font-size: 14px;
          color: #95a5a6;
          margin: 5px 0 0;
        }
      }

      .input-item {
        margin-bottom: 24px;

        label {
          display: block;
          font-size: 14px;
          color: #7f8c8d;
          margin-bottom: 8px;
          font-weight: 500;
        }

        ::v-deep .el-input__inner {
          height: 48px;
          border-radius: 8px;
          background: #f8fbf9;
          border: 1px solid #e1e8e5;
          
          &:focus {
            border-color: #2d5a27;
          }
        }
      }

      .role-selection {
        margin-bottom: 30px;

        label {
          display: block;
          font-size: 14px;
          color: #7f8c8d;
          margin-bottom: 12px;
        }

        .role-group {
          display: flex;
          gap: 20px;
          flex-wrap: wrap;
        }
      }

      .form-actions {
        .submit-btn {
          width: 100%;
          height: 50px;
          font-size: 16px;
          font-weight: 600;
          border-radius: 8px;
          background: #2d5a27;
          border: none;
          box-shadow: 0 8px 16px rgba(45, 90, 39, 0.2);
          transition: all 0.3s;

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 12px 20px rgba(45, 90, 39, 0.3);
          }
        }

        .secondary-actions {
          margin-top: 20px;
          text-align: center;
          font-size: 14px;
          color: #7f8c8d;

          .reg-btn {
            color: #2d5a27;
            font-weight: 600;
            padding-left: 5px;
            
            &:hover {
              text-decoration: underline;
            }
          }
        }
      }
    }
  }
}

@media (max-width: 1024px) {
  .login-box {
    flex-direction: column;

    .login-left {
      min-width: 100%;
      height: 40vh;
      
      .overlay {
        padding: 40px;
        
        .brand-title {
          font-size: 36px;
        }
        
        .brand-subtitle {
          font-size: 16px;
        }
      }
    }
    
    .login-right {
      width: 100%;
      min-width: auto;
      padding: 40px 30px;
      box-shadow: 0 -10px 40px rgba(0, 0, 0, 0.1);
    }
  }
}

@media (max-width: 480px) {
  .login-box {
    .login-left {
      height: 35vh;
      
      .overlay {
        padding: 30px;
        
        .brand-title {
          font-size: 28px;
        }
        
        .brand-subtitle {
          font-size: 14px;
        }
      }
    }
    
    .login-right {
      padding: 30px 20px;
    }
  }
}
</style>

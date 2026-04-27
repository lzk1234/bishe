<template>
  <el-aside class="index-aside" width="240px">
    <div class="index-aside-inner">
      <el-menu
        default-active="1"
        class="aside-menu"
        :unique-opened="true"
      >
        <el-menu-item @click="menuHandler('/')" index="1" class="menu-item-home">
          <i class="el-icon-s-home"></i>
          <span slot="title">系统首页</span>
        </el-menu-item>
        <sub-menu
          v-for="menu in menuList"
          :key="menu.menuId"
          :menu="menu"
          :dynamicMenuRoutes="dynamicMenuRoutes"
        ></sub-menu>
      </el-menu>
    </div>
  </el-aside>
</template>
<script>
import SubMenu from "@/components/index/IndexAsideSub";
export default {
  data() {
    return {
      menuList: [],
      dynamicMenuRoutes: []
    };
  },
  components: {
    SubMenu
  },
  mounted() {
    // 获取动态菜单数据并且渲染
    this.menuList = JSON.parse(sessionStorage.getItem("menuList") || "[]");
    this.dynamicMenuRoutes = JSON.parse(
      sessionStorage.getItem("dynamicMenuRoutes") || "[]"
    );
  },
  methods: {
    menuHandler(path) {
      this.$router.push({ path: path });
    }
  }
};
</script>
<style lang="scss" scoped>
.index-aside {
  height: calc(100vh - 64px);
  background: #f8faf9;
  border-right: 1px solid #e1e8e5;
  transition: width 0.3s;
  
  .index-aside-inner {
    height: 100%;
    overflow-x: hidden;
    overflow-y: auto;

    &::-webkit-scrollbar {
      width: 4px;
    }
    &::-webkit-scrollbar-thumb {
      background: #d1d9d5;
      border-radius: 2px;
    }
    
    .aside-menu {
      border-right: none;
      background: transparent;
      padding: 12px;

      ::v-deep .el-menu-item, ::v-deep .el-submenu__title {
        height: 50px;
        line-height: 50px;
        border-radius: 8px;
        margin-bottom: 4px;
        color: #4a5d55;
        transition: all 0.3s;

        i {
          color: #7a8c85;
          margin-right: 10px;
          font-size: 18px;
        }

        &:hover {
          background-color: #ecf2f0 !important;
          color: #2d5a27;
          i {
            color: #2d5a27;
          }
        }
      }

      ::v-deep .el-menu-item.is-active {
        background-color: #2d5a27 !important;
        color: #ffffff !important;
        box-shadow: 0 4px 12px rgba(45, 90, 39, 0.2);
        
        i {
          color: #ffffff;
        }
      }

      .menu-item-home {
        font-weight: 600;
        margin-bottom: 12px;
        border-bottom: 1px solid #e1e8e5;
        border-radius: 0;
        padding-bottom: 8px;
        height: 58px;
      }
    }
  }
}
</style>


<template>
  <el-submenu v-if="menu.list && menu.list.length >= 1" :index="menu.menuId + ''">
    <template slot="title">
      <i :class="getIcon(menu.name)"></i>
      <span>{{ menu.name }}</span>
    </template>
    <sub-menu
      v-for="item in menu.list"
      :key="item.menuId"
      :menu="item"
      :dynamicMenuRoutes="dynamicMenuRoutes"
    ></sub-menu>
  </el-submenu>
  <el-menu-item v-else :index="menu.menuId + ''" @click="gotoRouteHandle(menu)">
    <i :class="getIcon(menu.name)"></i>
    <span slot="title">{{ menu.name }}</span>
  </el-menu-item>
</template>

<script>
import SubMenu from "./IndexAsideSub";
export default {
  name: "sub-menu",
  props: {
    menu: {
      type: Object,
      required: true
    },
    dynamicMenuRoutes: {
      type: Array,
      required: true
    }
  },
  components: {
    SubMenu
  },
  methods: {
    getIcon(name) {
      const iconMap = {
        '用户': 'el-icon-user',
        '商品': 'el-icon-goods',
        '订单': 'el-icon-tickets',
        '分类': 'el-icon-menu',
        '评价': 'el-icon-chat-dot-round',
        '资讯': 'el-icon-news',
        '系统': 'el-icon-setting',
        '收藏': 'el-icon-star-off',
        '地址': 'el-icon-location-outline',
        '购物车': 'el-icon-shopping-cart-2',
        '个人': 'el-icon-user-solid',
        '密码': 'el-icon-unlock',
        '反馈': 'el-icon-edit-outline',
        '公告': 'el-icon-bell',
        '客服': 'el-icon-headset',
        '关于': 'el-icon-info'
      };
      
      for(let key in iconMap) {
        if(name.indexOf(key) !== -1) return iconMap[key];
      }
      return 'el-icon-folder';
    },
    // 通过menuId与动态(菜单)路由进行匹配跳转至指定路由
    gotoRouteHandle(menu) {
      var route = this.dynamicMenuRoutes.filter(
        item => item.meta.menuId === menu.menuId
      );
      if (route.length >= 1) {
        if (route[0].component != null) {
          this.$router.replace({ name: route[0].name });
        } else {
          this.$router.push({ name: "404" });
        }
      }
    }
  }
};
</script>

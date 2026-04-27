import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import Index from '@/views/index'
import Home from '@/views/home'
import Login from '@/views/login'
import NotFound from '@/views/404'
import UpdatePassword from '@/views/update-password'
import Pay from '@/views/pay'
import Register from '@/views/register'
import Center from '@/views/center'
import News from '@/views/modules/news/list'
import AboutUs from '@/views/modules/aboutus/list'
import Recommendation from '@/views/modules/recommendation/list'
import Yonghu from '@/views/modules/yonghu/list'
import Shangpinfenlei from '@/views/modules/shangpinfenlei/list'
import Shangpinxinxi from '@/views/modules/shangpinxinxi/list'
import Orders from '@/views/modules/orders/list'
import Config from '@/views/modules/config/list'
import Shangjia from '@/views/modules/shangjia/list'
import TeaBase from '@/views/modules/teabase/list'
import TeaBatch from '@/views/modules/teabatch/list'
import InventoryRecord from '@/views/modules/inventoryrecord/list'
import DecisionOverview from '@/views/decision-overview'
import DecisionAdvice from '@/views/decision-advice'
import BaseMap from '@/views/base-map'
import ProductionSalesPlan from '@/views/modules/productionsalesplan/list'
import ProductionSalesBoard from '@/views/production-sales-board'
import Warehouse from '@/views/modules/warehouse/list'
import InventoryTransferSuggestion from '@/views/modules/inventorytransfersuggestion/list'
import ProductLifecycleRule from '@/views/modules/productlifecyclerule/list'

const routes = [
  {
    path: '/index',
    name: '系统首页',
    component: Index,
    children: [
      {
        path: '/',
        name: '平台总览',
        component: Home
      },
      {
        path: '/updatePassword',
        name: '修改密码',
        component: UpdatePassword
      },
      {
        path: '/pay',
        name: '支付',
        component: Pay
      },
      {
        path: '/center',
        name: '个人信息',
        component: Center
      },
      {
        path: '/news',
        name: '行业资讯',
        component: News
      },
      {
        path: '/aboutus',
        name: '项目介绍',
        component: AboutUs
      },
      {
        path: '/recommendation',
        name: '推荐结果',
        component: Recommendation
      },
      {
        path: '/yonghu',
        name: '客户档案',
        component: Yonghu
      },
      {
        path: '/shangpinfenlei',
        name: '茶类等级',
        component: Shangpinfenlei
      },
      {
        path: '/shangpinxinxi',
        name: '茶品档案',
        component: Shangpinxinxi
      },
      {
        path: '/orders/:status',
        name: '销售订单',
        component: Orders
      },
      {
        path: '/config',
        name: '首页轮播',
        component: Config
      },
      {
        path: '/shangjia',
        name: '茶企档案',
        component: Shangjia
      },
      {
        path: '/teabase',
        name: '茶园基地',
        component: TeaBase
      },
      {
        path: '/teabatch',
        name: '生产批次',
        component: TeaBatch
      },
      {
        path: '/inventoryrecord',
        name: '库存台账',
        component: InventoryRecord
      },
      {
        path: '/decisionOverview',
        name: '决策总览',
        component: DecisionOverview
      },
      {
        path: '/decisionAdvice',
        name: '经营建议',
        component: DecisionAdvice
      },
      {
        path: '/baseMap',
        name: '茶园产能地图',
        component: BaseMap
      },
      {
        path: '/productionSalesPlan',
        name: '年度产销计划',
        component: ProductionSalesPlan
      },
      {
        path: '/productionSalesBoard',
        name: '产销计划看板',
        component: ProductionSalesBoard
      },
      {
        path: '/warehouse',
        name: '仓库档案',
        component: Warehouse
      },
      {
        path: '/inventoryTransferSuggestion',
        name: '调拨建议',
        component: InventoryTransferSuggestion
      },
      {
        path: '/productLifecycleRule',
        name: '生命周期规则',
        component: ProductLifecycleRule
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/register',
    name: 'register',
    component: Register
  },
  {
    path: '/',
    name: 'root',
    redirect: '/index'
  },
  {
    path: '*',
    component: NotFound
  }
]

const router = new VueRouter({
  mode: 'hash',
  routes
})

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

import storage from '@/utils/storage'

router.beforeEach((to, from, next) => {
  const publicPaths = ['/login', '/register', '/']
  const token = storage.get('Token')
  const sessionTable = storage.get('sessionTable')
  if (!publicPaths.includes(to.path) && (!token || !sessionTable)) {
    storage.remove('Token')
    storage.remove('sessionTable')
    storage.remove('role')
    storage.remove('adminName')
    storage.remove('userid')
    next('/login')
  } else {
    next()
  }
})

export default router

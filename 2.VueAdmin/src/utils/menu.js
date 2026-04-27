const menu = {
  list() {
    return [
      {
        roleName: '管理员',
        tableName: 'users',
        hasBackLogin: '是',
        hasBackRegister: '否',
        hasFrontLogin: '否',
        hasFrontRegister: '否',
        backMenu: [
          {
            menu: '基础档案',
            child: [
              {
                menu: '茶企档案',
                tableName: 'shangjia',
                buttons: ['新增', '查看', '修改', '删除']
              },
              {
                menu: '客户档案',
                tableName: 'yonghu',
                buttons: ['新增', '查看', '修改', '删除']
              },
              {
                menu: '茶类等级',
                tableName: 'shangpinfenlei',
                buttons: ['新增', '查看', '修改', '删除']
              },
              {
                menu: '茶品档案',
                tableName: 'shangpinxinxi',
                buttons: ['新增', '查看', '修改', '删除', '查看评论']
              }
            ]
          },
          {
            menu: '生产管理',
            child: [
              {
                menu: '茶园基地',
                tableName: 'teabase',
                buttons: ['新增', '查看', '修改', '删除']
              },
              {
                menu: '生产批次',
                tableName: 'teabatch',
                buttons: ['新增', '查看', '修改', '删除']
              }
            ]
          },
          {
            menu: '供应库存',
            child: [
              {
                menu: '库存台账',
                tableName: 'inventoryrecord',
                buttons: ['新增', '查看', '修改', '删除']
              },
              {
                menu: '仓库档案',
                tableName: 'warehouse',
                buttons: ['新增', '查看', '修改', '删除']
              }
            ]
          },
          {
            menu: '销售管理',
            child: [
              {
                menu: '销售订单',
                tableName: 'orders/已支付',
                buttons: ['查看', '发货', '删除']
              },
              {
                menu: '发货订单',
                tableName: 'orders/已发货',
                buttons: ['查看', '删除']
              },
              {
                menu: '完成订单',
                tableName: 'orders/已完成',
                buttons: ['查看', '删除', '日销量', '月销量', '品销量', '类销量', '月销售额', '日销售额', '品销售额', '类销售额']
              },
              {
                menu: '未支付订单',
                tableName: 'orders/未支付',
                buttons: ['查看', '删除']
              },
              {
                menu: '退款订单',
                tableName: 'orders/已退款',
                buttons: ['查看', '删除']
              }
            ]
          },
          {
            menu: '决策中心',
            child: [
              {
                menu: '经营建议',
                tableName: 'decisionAdvice',
                buttons: ['查看']
              },
              {
                menu: '茶园产能地图',
                tableName: 'baseMap',
                buttons: ['查看']
              },
              {
                menu: '年度产销计划',
                tableName: 'productionSalesPlan',
                buttons: ['新增', '查看', '修改', '删除']
              },
              {
                menu: '产销计划看板',
                tableName: 'productionSalesBoard',
                buttons: ['查看']
              },
              {
                menu: '调拨建议',
                tableName: 'inventoryTransferSuggestion',
                buttons: ['新增', '查看', '修改', '删除', '确认']
              },
              {
                menu: '推荐管理',
                tableName: 'recommendation',
                buttons: ['查看', '删除', '刷新']
              }
            ]
          },
          {
            menu: '系统管理',
            child: [
              {
                menu: '行业资讯',
                tableName: 'news',
                buttons: ['新增', '查看', '修改', '删除']
              },
              {
                menu: '项目介绍',
                tableName: 'aboutus',
                buttons: ['查看', '修改']
              },
              {
                menu: '首页轮播',
                tableName: 'config',
                buttons: ['查看', '修改']
              }
            ]
          }
        ],
        frontMenu: []
      },
      {
        roleName: '用户',
        tableName: 'yonghu',
        hasBackLogin: '否',
        hasBackRegister: '否',
        hasFrontLogin: '是',
        hasFrontRegister: '是',
        backMenu: [],
        frontMenu: [
          {
            menu: '茶品采购',
            child: [
              {
                menu: '茶品列表',
                tableName: 'shangpinxinxi',
                buttons: ['查看']
              }
            ]
          }
        ]
      },
      {
        roleName: '商家',
        tableName: 'shangjia',
        hasBackLogin: '是',
        hasBackRegister: '是',
        hasFrontLogin: '否',
        hasFrontRegister: '否',
        backMenu: [
          {
            menu: '基础档案',
            child: [
              {
                menu: '茶品档案',
                tableName: 'shangpinxinxi',
                buttons: ['新增', '查看', '修改', '删除', '查看评论']
              }
            ]
          },
          {
            menu: '生产管理',
            child: [
              {
                menu: '茶园基地',
                tableName: 'teabase',
                buttons: ['新增', '查看', '修改', '删除']
              },
              {
                menu: '生产批次',
                tableName: 'teabatch',
                buttons: ['新增', '查看', '修改', '删除']
              }
            ]
          },
          {
            menu: '供应库存',
            child: [
              {
                menu: '库存台账',
                tableName: 'inventoryrecord',
                buttons: ['新增', '查看', '修改', '删除']
              },
              {
                menu: '仓库档案',
                tableName: 'warehouse',
                buttons: ['新增', '查看', '修改', '删除']
              }
            ]
          },
          {
            menu: '销售管理',
            child: [
              {
                menu: '销售订单',
                tableName: 'orders/已支付',
                buttons: ['查看', '发货', '删除']
              },
              {
                menu: '发货订单',
                tableName: 'orders/已发货',
                buttons: ['查看', '删除']
              },
              {
                menu: '完成订单',
                tableName: 'orders/已完成',
                buttons: ['查看', '删除', '日销量', '月销量', '品销量', '类销量', '月销售额', '日销售额', '品销售额', '类销售额']
              },
              {
                menu: '未支付订单',
                tableName: 'orders/未支付',
                buttons: ['查看', '删除']
              },
              {
                menu: '退款订单',
                tableName: 'orders/已退款',
                buttons: ['查看', '删除']
              }
            ]
          },
          {
            menu: '决策中心',
            child: [
              {
                menu: '经营建议',
                tableName: 'decisionAdvice',
                buttons: ['查看']
              },
              {
                menu: '茶园产能地图',
                tableName: 'baseMap',
                buttons: ['查看']
              },
              {
                menu: '年度产销计划',
                tableName: 'productionSalesPlan',
                buttons: ['新增', '查看', '修改', '删除']
              },
              {
                menu: '产销计划看板',
                tableName: 'productionSalesBoard',
                buttons: ['查看']
              },
              {
                menu: '调拨建议',
                tableName: 'inventoryTransferSuggestion',
                buttons: ['新增', '查看', '修改', '删除', '确认']
              }
            ]
          }
        ],
        frontMenu: []
      }
    ]
  }
}

export default menu

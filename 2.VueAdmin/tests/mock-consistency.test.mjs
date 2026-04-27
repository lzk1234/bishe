import assert from 'node:assert/strict'

import { buildCanonicalData } from '../../mock/canonical-data.js'
import { createMockHttpClient } from '../../mock/service.js'

function createStorage(seed = {}) {
  const store = new Map(Object.entries(seed))
  return {
    getItem(key) {
      return store.has(key) ? store.get(key) : null
    },
    setItem(key, value) {
      store.set(key, String(value))
    },
    removeItem(key) {
      store.delete(key)
    }
  }
}

function assertProductBatchLinks(data, account = '') {
  const batchesByCode = new Map(
    data.teabatch
      .filter(item => !account || item.enterpriseaccount === account)
      .map(item => [item.batchcode, item])
  )
  const inventoryByCode = new Map()
  data.inventoryrecord
    .filter(item => !account || item.enterpriseaccount === account)
    .forEach(item => {
      if (!inventoryByCode.has(item.batchcode)) {
        inventoryByCode.set(item.batchcode, [])
      }
      inventoryByCode.get(item.batchcode).push(item)
    })

  data.shangpinxinxi
    .filter(item => (!account || item.zhanghao === account) && item.shengchanpici)
    .forEach(item => {
      const batch = batchesByCode.get(item.shengchanpici)
      assert.ok(batch, `missing batch for product ${item.shangpinmingcheng}`)
      assert.equal(batch.enterpriseaccount, item.zhanghao)
      assert.equal(batch.productname, item.shangpinmingcheng)

      const inventoryList = inventoryByCode.get(item.shengchanpici) || []
      assert.ok(inventoryList.length > 0, `missing inventory for batch ${item.shengchanpici}`)
      assert.equal(
        inventoryList.every(record => record.enterpriseaccount === item.zhanghao && record.productname === item.shangpinmingcheng),
        true
      )
    })
}

const canonical = buildCanonicalData()
const merchantAccounts = canonical.shangjia.map(item => item.zhanghao)

assert.equal(merchantAccounts.every(account => /^账号\d+$/.test(account)), true)
assert.equal(merchantAccounts.includes('1'), false)
assertProductBatchLinks(canonical)

const adminStorage = createStorage({ sessionTable: 'users', adminName: 'admin' })
const adminHttp = createMockHttpClient({ storage: adminStorage })

const adminDashboard = await adminHttp.get('decision/dashboard')
const merchantPage = await adminHttp.get('shangjia/page', { params: { page: 1, limit: 100 } })
const userPage = await adminHttp.get('yonghu/page', { params: { page: 1, limit: 100 } })
const productPage = await adminHttp.get('shangpinxinxi/page', { params: { page: 1, limit: 100 } })
const basePage = await adminHttp.get('teabase/page', { params: { page: 1, limit: 100 } })

assert.equal(adminDashboard.data.data.stats.enterpriseCount, merchantPage.data.data.total)
assert.equal(canonical.yonghu.length, userPage.data.data.total)
assert.equal(adminDashboard.data.data.stats.productCount, productPage.data.data.total)
assert.equal(adminDashboard.data.data.stats.baseCount, basePage.data.data.total)

const merchantStorage = createStorage({
  sessionTable: 'shangjia',
  adminName: '账号8',
  userid: '28'
})
const merchantHttp = createMockHttpClient({ storage: merchantStorage })

const merchantDashboard = await merchantHttp.get('decision/dashboard')
const merchantProducts = await merchantHttp.get('shangpinxinxi/page', { params: { page: 1, limit: 100 } })
const merchantBases = await merchantHttp.get('teabase/page', { params: { page: 1, limit: 100 } })
const merchantBatches = await merchantHttp.get('teabatch/page', { params: { page: 1, limit: 100 } })
const merchantInventory = await merchantHttp.get('inventoryrecord/page', { params: { page: 1, limit: 100 } })

assert.equal(merchantProducts.data.data.total > 0, true)
assert.equal(merchantBases.data.data.total > 0, true)
assert.equal(merchantBatches.data.data.total > 0, true)
assert.equal(merchantInventory.data.data.total > 0, true)
assert.equal(merchantProducts.data.data.list.every(item => item.zhanghao === '账号8'), true)
assert.equal(merchantBases.data.data.list.every(item => item.enterpriseaccount === '账号8'), true)
assert.equal(merchantBatches.data.data.list.every(item => item.enterpriseaccount === '账号8'), true)
assert.equal(merchantInventory.data.data.list.every(item => item.enterpriseaccount === '账号8'), true)
assert.equal(merchantDashboard.data.data.stats.productCount, merchantProducts.data.data.total)
assert.equal(merchantDashboard.data.data.stats.baseCount, merchantBases.data.data.total)
assertProductBatchLinks(
  {
    shangpinxinxi: merchantProducts.data.data.list,
    teabatch: merchantBatches.data.data.list,
    inventoryrecord: merchantInventory.data.data.list
  },
  '账号8'
)

const shippingStorage = createStorage({
  sessionTable: 'shangjia',
  adminName: '账号8',
  userid: '28'
})
const shippingHttp = createMockHttpClient({ storage: shippingStorage })
const paidOrders = await shippingHttp.get('orders/page', { params: { page: 1, limit: 10, status: '已支付' } })
const paidOrder = paidOrders.data.data.list[0]
assert.ok(paidOrder)

const shipResult = await shippingHttp.post('orders/ship', { id: paidOrder.id, logistics: 'SF123456' })
assert.equal(shipResult.data.code, 0)

const shippedOrder = await shippingHttp.get(`orders/info/${paidOrder.id}`)
assert.equal(shippedOrder.data.data.status, '已发货')
assert.equal(shippedOrder.data.data.logistics, 'SF123456')

const foreignShippingStorage = createStorage({
  sessionTable: 'shangjia',
  adminName: '账号7',
  userid: '27'
})
const foreignShippingHttp = createMockHttpClient({ storage: foreignShippingStorage })
const foreignShipResult = await foreignShippingHttp.post('orders/ship', { id: 1709783087063, logistics: 'SF999999' })
assert.equal(foreignShipResult.data.code, 403)

console.log('mock consistency tests passed')

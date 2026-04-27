import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import { fileURLToPath } from 'node:url'
import { dirname, resolve } from 'node:path'

const __dirname = dirname(fileURLToPath(import.meta.url))
const root = resolve(__dirname, '..')

function read(relativePath) {
  return readFileSync(resolve(root, relativePath), 'utf8')
}

function assertNotIncludes(source, needle, label) {
  assert.equal(source.includes(needle), false, label)
}

const ordersList = read('src/views/modules/orders/list.vue')
const cartList = read('src/views/modules/cart/list.vue')
const storeupList = read('src/views/modules/storeup/list.vue')
const teabatchList = read('src/views/modules/teabatch/list.vue')
const inventoryList = read('src/views/modules/inventoryrecord/list.vue')
const productList = read('src/views/modules/shangpinxinxi/list.vue')
const teabaseList = read('src/views/modules/teabase/list.vue')

assertNotIncludes(ordersList, "isAuth('orders'+'/'+orderStatus,'鏂板')", 'orders admin list must not expose create')
assertNotIncludes(ordersList, "addOrUpdateHandler(scope.row.id)\">淇", 'orders admin list must not expose full edit')
assert.ok(ordersList.includes('logisticsUpdate(scope.row.id)'), 'orders admin list should keep logistics edit')

assertNotIncludes(cartList, "isAuth('cart','鏂板')", 'cart admin list must not expose create')
assertNotIncludes(cartList, "isAuth('cart','淇", 'cart admin list must not expose edit')

assertNotIncludes(storeupList, "isAuth('storeup','鏂板')", 'storeup admin list must not expose create')
assertNotIncludes(storeupList, "isAuth('storeup','淇", 'storeup admin list must not expose edit')

assert.ok(teabatchList.includes('baseOptions'), 'teabatch should load base options')
assert.ok(teabatchList.includes('productOptions'), 'teabatch should load product options')
assert.ok(teabatchList.includes('@change="onBaseChange"'), 'teabatch should refill fields from selected base')
assert.ok(teabatchList.includes('@change="onProductChange"'), 'teabatch should refill fields from selected product')
assert.ok(teabatchList.includes('loadEnterprises'), 'teabatch should allow admins to choose existing enterprises')

assert.ok(inventoryList.includes('batchOptions'), 'inventory record should load batch options')
assert.ok(inventoryList.includes('@change="onBatchChange"'), 'inventory record should refill product from selected batch')
assert.ok(inventoryList.includes('calculateCurrentStock'), 'inventory record should calculate current stock')
assert.ok(inventoryList.includes(':disabled="true"'), 'inventory current stock should be read-only')

assert.ok(productList.includes('enterpriseOptions'), 'product form should load existing enterprise accounts')
assert.ok(productList.includes('batchOptions'), 'product form should load existing batches')
assert.ok(productList.includes('@change="onBatchChange"'), 'product form should refill from selected batch')
assertNotIncludes(productList, 'allow-create', 'product category should not allow ad-hoc categories')

assert.ok(teabaseList.includes('teaTypeOptions'), 'tea base should use tea category options')
assert.ok(teabaseList.includes('loadTeaTypes'), 'tea base should load tea category options')

console.log('master data linkage tests passed')

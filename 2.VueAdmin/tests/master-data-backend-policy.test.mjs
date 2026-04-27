import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import { fileURLToPath } from 'node:url'
import { dirname, resolve } from 'node:path'

const __dirname = dirname(fileURLToPath(import.meta.url))
const repoRoot = resolve(__dirname, '..', '..')

function read(relativePath) {
  return readFileSync(resolve(repoRoot, relativePath), 'utf8')
}

const ordersController = read('1.JavaSpringBoot/src/main/java/com/controller/OrdersController.java')
const cartController = read('1.JavaSpringBoot/src/main/java/com/controller/CartController.java')
const storeupController = read('1.JavaSpringBoot/src/main/java/com/controller/StoreupController.java')
const planController = read('1.JavaSpringBoot/src/main/java/com/controller/ProductionSalesPlanController.java')
const lifecycleController = read('1.JavaSpringBoot/src/main/java/com/controller/ProductLifecycleRuleController.java')
const teabatchController = read('1.JavaSpringBoot/src/main/java/com/controller/TeabatchController.java')
const teabatchEntity = read('1.JavaSpringBoot/src/main/java/com/entity/TeabatchEntity.java')
const teabatchList = read('2.VueAdmin/src/views/modules/teabatch/list.vue')

assert.ok(ordersController.includes('front order creation is limited to user sessions'), 'orders add must reject non-user sessions')
assert.ok(cartController.includes('front cart creation is limited to user sessions'), 'cart add must reject non-user sessions')
assert.ok(storeupController.includes('front favorite creation is limited to user sessions'), 'storeup add must reject non-user sessions')

assert.ok(planController.includes('normalizePlanProduct'), 'production sales plan must normalize product snapshots server-side')
assert.ok(lifecycleController.includes('normalizeLifecycleProduct'), 'lifecycle rule must normalize product snapshots server-side')

assert.ok(teabatchEntity.includes('private Long baseid'), 'teabatch should accept real base id for binding')
assert.ok(teabatchEntity.includes('private Long productid'), 'teabatch should accept real product id for binding')
assert.ok(teabatchController.includes('getBaseid()'), 'teabatch backend should resolve base by id')
assert.ok(teabatchController.includes('getProductid()'), 'teabatch backend should resolve product by id')
assert.ok(teabatchList.includes('v-model="form.baseid"'), 'teabatch form should select base by id')
assert.ok(teabatchList.includes('v-model="form.productid"'), 'teabatch form should select product by id')

console.log('master data backend policy tests passed')

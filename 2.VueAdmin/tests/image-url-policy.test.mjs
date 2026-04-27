import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import { fileURLToPath } from 'node:url'
import { dirname, resolve } from 'node:path'

const __dirname = dirname(fileURLToPath(import.meta.url))
const repoRoot = resolve(__dirname, '..', '..')

function read(path) {
  return readFileSync(resolve(repoRoot, path), 'utf8')
}

const adminUpload = read('2.VueAdmin/src/components/common/FileUpload.vue')
const userUpload = read('3.VueUser/src/components/FileUpload.vue')
const adminImageUtil = read('2.VueAdmin/src/utils/image.js')
const userImageUtil = read('3.VueUser/src/utils/image.js')
const fileController = read('1.JavaSpringBoot/src/main/java/com/controller/FileController.java')
const interceptorConfig = read('1.JavaSpringBoot/src/main/java/com/config/InterceptorConfig.java')

assert.ok(adminUpload.includes('fileUrlArray.push(storedUrl)'), 'admin upload should store relative image paths')
assert.ok(userUpload.includes('fileUrlArray.push(storedUrl)'), 'user upload should store relative image paths')
assert.ok(adminImageUtil.includes('normalizeFilePath'), 'admin should have shared image URL normalization')
assert.ok(userImageUtil.includes('normalizeFilePath'), 'user should have shared image URL normalization')
assert.ok(adminImageUtil.includes('installImageFallback'), 'admin should install a global missing-image fallback')
assert.ok(userImageUtil.includes('installImageFallback'), 'user should install a global missing-image fallback')
assert.ok(fileController.includes('static/upload'), 'uploaded files should be persisted outside target/classes')
assert.ok(fileController.includes('resolveUploadFile'), 'download should read both stable and legacy upload locations')
assert.ok(interceptorConfig.includes('"/upload/**"'), 'uploaded images should bypass auth interceptor')
assert.ok(interceptorConfig.includes('"file:static/"'), 'stable upload directory should be exposed as static resource')

console.log('image url policy tests passed')

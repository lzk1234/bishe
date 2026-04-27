import assert from 'node:assert/strict'

import { applyMockTokenHeader, isUnauthorizedPayload } from '../src/utils/http-auth.mjs'

const mockStorage = {
  get(key) {
    return key === 'Token' ? 'fresh-token' : ''
  }
}

const mockHttp = {
  headers: {
    common: {
      Token: 'stale-token'
    }
  }
}

assert.equal(applyMockTokenHeader(mockHttp, mockStorage), 'fresh-token')
assert.equal(mockHttp.headers.common.Token, 'fresh-token')

assert.equal(isUnauthorizedPayload({ status: 401 }), true)
assert.equal(isUnauthorizedPayload({ data: { code: 401 } }), true)
assert.equal(isUnauthorizedPayload({ response: { status: 401 } }), true)
assert.equal(isUnauthorizedPayload({ response: { data: { code: 401 } } }), true)
assert.equal(isUnauthorizedPayload({ status: 200, data: { code: 0 } }), false)

console.log('http auth tests passed')

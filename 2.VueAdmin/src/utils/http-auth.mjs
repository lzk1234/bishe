export function getStoredToken(storage) {
  if (!storage || typeof storage.get !== 'function') {
    return ''
  }
  return storage.get('Token') || ''
}

export function applyMockTokenHeader(http, storage) {
  const token = getStoredToken(storage)
  if (!http || !http.headers || !http.headers.common) {
    return token
  }
  if (token) {
    http.headers.common.Token = token
  } else {
    delete http.headers.common.Token
  }
  return token
}

export function isUnauthorizedPayload(payload) {
  if (!payload) {
    return false
  }
  const status = payload.status || (payload.response && payload.response.status)
  const data = payload.data || (payload.response && payload.response.data)
  return status === 401 || (data && data.code === 401)
}

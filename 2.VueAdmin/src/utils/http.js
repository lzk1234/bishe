import axios from 'axios'
import router from '@/router/router-static'
import storage from '@/utils/storage'
import { isUnauthorizedPayload } from '@/utils/http-auth.mjs'

let http
let redirectingToLogin = false

function clearAuthState() {
    ;['Token', 'role', 'sessionTable', 'adminName', 'userid'].forEach(key => storage.remove(key))
}

function redirectToLogin() {
    clearAuthState()
    if (redirectingToLogin) {
        return
    }
    if (router.currentRoute && router.currentRoute.name === 'login') {
        return
    }
    redirectingToLogin = true
    router.replace({ name: 'login' }).catch(() => {}).finally(() => {
        redirectingToLogin = false
    })
}

http = axios.create({
    timeout: 1000 * 86400,
    withCredentials: true,
    baseURL: '/springbooty2rp6',
    headers: {
        'Content-Type': 'application/json; charset=utf-8'
    }
})

http.interceptors.request.use(config => {
    config.headers['Token'] = storage.get('Token')
    return config
}, error => {
    return Promise.reject(error)
})

http.interceptors.response.use(response => {
    if (isUnauthorizedPayload(response)) {
        redirectToLogin()
    }
    return response
}, error => {
    if (isUnauthorizedPayload(error)) {
        redirectToLogin()
    }
    return Promise.reject(error)
})

export default http

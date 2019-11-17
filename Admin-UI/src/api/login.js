import fetch from '@/utils/fetch'

export function loginByUsername(username, password) {
  const data = {
    username,
    password
  }
  return fetch({
    // url: '/login/login',
    url: '/jwt/token',
    method: 'post',
    data
  })
}

export function logout() {
  return fetch({
    // url: '/login/logout',
    url: '/jwt/invalid',
    method: 'post'
  })
}

export function getUserInfo(token) {
  return fetch({
    // url: '/user/info',
    url: '/jwt/user',
    method: 'get',
    params: { token }
  })
}


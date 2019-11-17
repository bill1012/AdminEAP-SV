import fetch from 'utils/fetch'

// 关联角色的用户列表
export function page(query) {
  return fetch({
    url: '/api/role/user/list',
    method: 'get',
    params: query
  })
}

// 未关联角色的用户列表
export function list(query) {
  return fetch({
    url: '/api/role/user/select',
    method: 'get',
    params: query
  })
}

// 新增用户角色关联
export function addObj(roleId, userIds) {
  return fetch({
    url: '/api/role/user/' + roleId + '/add',
    method: 'post',
    params: {userIds: userIds}
  })
}

// 移除角色关联
export function delObj(roleId, userIds) {
  return fetch({
    url: '/api/role/user/' + roleId + '/delete',
    method: 'delete',
    params: {userIds: userIds}
  })
}

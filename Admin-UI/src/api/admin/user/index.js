import fetch from 'utils/fetch'

export function page(query) {
  return fetch({
    url: '/api/user/list',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return fetch({
    url: '/api/user/add',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return fetch({
    url: '/api/user/get/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return fetch({
    url: '/api/user/delete/' + id,
    method: 'delete'
  })
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/user/put/' + id,
    method: 'put',
    data: obj
  })
}

export function getUsersByProjectId(id) {
  return fetch({
    url: '/api/user/getUsersByProjectId?projectId=' + id,
    method: 'get',
    data: id
  })
}

export function getUsersMapByProjectId(id, type) {
  return fetch({
    url: '/api/user/getUsersMapByProjectId?projectId=' + id + '&type=' + type,
    method: 'get',
    data: id
  })
}

export function getTaskIdExecutorId(id) {
  return fetch({
    url: '/api/user/getTaskIdExecutorId?projectId=' + id,
    method: 'get',
    data: id
  })
}

export function updatePass(userId, pass, oldPass) {
  return fetch({
    url: '/api/user/updatepwd?userid=' + userId + '&pass=' + pass + '&oldpass=' + oldPass,
    method: 'get'
  })
}

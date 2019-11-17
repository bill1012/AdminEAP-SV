import fetch from 'utils/fetch'

export function page(query) {
  return fetch({
    url: '/api/project/list',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return fetch({
    url: '/api/project/addObj',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return fetch({
    url: '/api/project/get/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return fetch({
    url: '/api/project/delete/' + id,
    method: 'delete'
  })
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/project/update/' + id,
    method: 'put',
    data: obj
  })
}

export function getDict(code) {
  return fetch({
    url: '/api/dict/dictSon?code=' + code,
    method: 'get'
  })
}

export function getUser(query) {
  return fetch({
    url: '/api/user/userPage',
    method: 'get',
    params: query
  })
}

export function getUserNO(query) {
  return fetch({
    url: '/api/user/userPageNo',
    method: 'get',
    params: query
  })
}

export function delProUser(projectId, userId) {
  return fetch({
    url: '/api/project/delProUser?projectId=' + projectId + '&userId=' + userId,
    method: 'delete'
  })
}

export function addProUser(id, userList, userId) {
  return fetch({
    data: {id: id, list: userList, createUser: userId},
    url: '/api/project/addProjectUser',
    method: 'post'
  })
}

export function getUserBoolean(projectId, userId) {
  return fetch({
    url: '/api/user/getUserBoolean?projectId=' + projectId + '&userId=' + userId,
    method: 'get'
  })
}

export function getTaskUserId(userId, projectId) {
  return fetch({
    url: '/api/task/getTaskUserId?userId=' + userId + '&projectId=' + projectId,
    method: 'get'
  })
}


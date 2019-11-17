import fetch from 'utils/fetch'

export function addObj(obj) {
  return fetch({
    url: 'api/task/addTask',
    method: 'post',
    data: obj
  })
}

export function getTasksByProjectId(projectId) {
  return fetch({
    url: 'api/task/getTasksByProjectId?projectId=' + projectId,
    method: 'get',
    data: projectId
  })
}

export function getTaskById(id) {
  return fetch({
    url: 'api/task/getTaskVoById?id=' + id,
    method: 'get',
    data: id
  })
}

export function updateTask(obj) {
  return fetch({
    url: 'api/task/updateTask',
    method: 'put',
    data: obj
  })
}

export function getCountTaskUserByProjectId(id) {
  return fetch({
    url: 'api/task/getCountTaskUserByProjectId?projectId=' + id,
    method: 'get',
    data: id
  })
}

export function getTaskListVoByExample(query) {
  return fetch({
    url: '/api/task/getTaskListVoByExample',
    method: 'get',
    params: query
  })
}

export function deleteTask(taskId) {
  return fetch({
    url: 'api/task/delete/' + taskId,
    method: 'delete',
    data: taskId
  })
}

export function updateDragTasks(data) {
  return fetch({
    url: '/api/task/updateDragTasks',
    method: 'put',
    params: data
  })
}

export function getCountDayTaskByProjectId(id) {
  return fetch({
    url: 'api/task/getCountDayTaskByProjectId?projectId=' + id,
    method: 'get',
    data: id
  })
}

export function getCountDayTaskByProjectIdTimes(query) {
  return fetch({
    url: 'api/task/getCountDayTaskByProjectIdTimes',
    method: 'get',
    params: query
  })
}

export function getCountDayCompTaskByProjectId(id) {
  return fetch({
    url: 'api/task/getCountDayCompTaskByProjectId?projectId=' + id,
    method: 'get',
    data: id
  })
}

export function getCountDayCompTaskByProjectIdTimes(query) {
  return fetch({
    url: 'api/task/getCountDayCompTaskByProjectIdTimes',
    method: 'get',
    params: query
  })
}

export function getCountDairenlingByProjectId(id) {
  return fetch({
    url: 'api/task/getCountDairenlingByProjectId?projectId=' + id,
    method: 'get',
    data: id
  })
}

export function getCountUnFinishedByProjectId(id) {
  return fetch({
    url: 'api/task/getCountUnFinishedByProjectId?projectId=' + id,
    method: 'get',
    data: id
  })
}

export function getCountFinishedByProjectId(id) {
  return fetch({
    url: 'api/task/getCountFinishedByProjectId?projectId=' + id,
    method: 'get',
    data: id
  })
}

export function putObj(obj) {
  return fetch({
    url: 'api/task/put/' + obj.id,
    method: 'put',
    data: obj
  })
}

import fetch from 'utils/fetch'

export function getRecordsByTaskId(taskId) {
  return fetch({
    url: 'api/record/getRecordsByTaskId?taskId=' + taskId,
    method: 'get',
    data: taskId
  })
}

export function add(data) {
  return fetch({
    url: 'api/record/add',
    method: 'post',
    data: data
  })
}

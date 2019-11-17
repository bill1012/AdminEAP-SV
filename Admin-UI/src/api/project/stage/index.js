import fetch from 'utils/fetch'

export function getStagesByProjectId(projectId) {
  return fetch({
    url: 'api/stage/getStagesByProjectId?projectId=' + projectId,
    method: 'get',
    data: projectId
  })
}

export function addStage(obj) {
  return fetch({
    url: 'api/stage/addStage',
    method: 'post',
    data: obj
  })
}

export function deleteStage(id) {
  return fetch({
    url: 'api/stage/delete/' + id,
    method: 'delete',
    data: id
  })
}

export function getStageById(id) {
  return fetch({
    url: 'api/stage/get/' + id,
    method: 'get',
    data: id
  })
}

export function updateStage(obj) {
  return fetch({
    url: 'api/stage/put/' + obj.id,
    method: 'put',
    data: obj
  })
}

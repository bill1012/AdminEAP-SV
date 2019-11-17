import fetch from 'utils/fetch'

export function getProjectsByUser(userId) {
  return fetch({
    url: '/api/project/projectsByUser?userId=' + userId,
    method: 'get'
  })
}

export function getProjectById(id) {
  return fetch({
    url: '/api/project/get/' + id,
    method: 'get',
    data: id
  })
}


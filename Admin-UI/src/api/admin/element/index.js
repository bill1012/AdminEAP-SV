import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/element/page',
    method: 'get',
    params: query
  });
}

export function list(query) {
  return fetch({
    url: '/api/element/list',
    method: 'get',
    params: query
  })
}
export function addObj(obj) {
  return fetch({
    url: '/api/element/add',
    method: 'post',
    data: obj
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/element/get/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return fetch({
    url: '/api/element/delete/' + id,
    method: 'delete'
  })
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/element/put/' + id,
    method: 'put',
    data: obj
  })
}

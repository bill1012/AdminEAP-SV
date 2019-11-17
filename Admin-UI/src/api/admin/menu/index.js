import fetch from 'utils/fetch';

export function fetchTree(query) {
  return fetch({
    url: '/api/menu/tree',
    method: 'get',
    params: query
  });
}

export function fetchAll() {
  return fetch({
    url: '/api/menu/all',
    method: 'get'
  });
}

export function addObj(obj) {
  return fetch({
    url: '/api/menu/add',
    method: 'post',
    data: obj
  });
}

export function getNextCode(parentId) {
  return fetch({
    url: '/api/menu/getNextCode',
    method: 'get',
    params: {parentId: parentId}
  });
}

export function checkCode(value, menuId) {
  const param = {code: value, id: menuId};
  return fetch({
    url: '/api/menu/checkCode',
    method: 'get',
    params: param
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/menu/get/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return fetch({
    url: '/api/menu/delete/' + id,
    method: 'delete'
  })
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/menu/put/' + id,
    method: 'put',
    data: obj
  })
}

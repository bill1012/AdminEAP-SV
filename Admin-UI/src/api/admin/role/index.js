import fetch from 'utils/fetch';

export function fetchTree(query) {
  return fetch({
    url: '/api/role/tree',
    method: 'get',
    params: query
  });
}

export function getNextCode(parentId) {
  return fetch({
    url: '/api/role/getNextCode',
    method: 'get',
    params: {parentId: parentId}
  });
}

export function checkCode(value, menuId) {
  const param = {code: value, id: menuId};
  return fetch({
    url: '/api/role/checkCode',
    method: 'get',
    params: param
  });
}

export function addObj(obj) {
  return fetch({
    url: '/api/role/add',
    method: 'post',
    data: obj
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/role/get/' + id,
    method: 'get'
  });
}

export function delObj(id) {
  return fetch({
    url: '/api/role/delete/' + id,
    method: 'delete'
  });
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/role/put/' + id,
    method: 'put',
    data: obj
  });
}


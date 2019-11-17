import fetch from 'utils/fetch';

export function deleteElementResource(roleId, data) {
  return fetch({
    url: '/api/role/resource/' + roleId + '/element/delete',
    method: 'post',
    params: data
  });
}

export function addElementResource(roleId, data) {
  return fetch({
    url: '/api/role/resource/' + roleId + '/element/add',
    method: 'post',
    params: data
  });
}

export function modifyElementResource(roleId, data) {
  return fetch({
    url: '/api/role/resource/' + roleId + '/element/modify',
    method: 'post',
    params: data
  });
}

export function getElementResource(roleId) {
  return fetch({
    url: '/api/role/resource/' + roleId + '/element',
    method: 'get'
  });
}

export function modifyMenuResource(roleId, data) {
  return fetch({
    url: '/api/role/resource/' + roleId + '/menu',
    method: 'post',
    params: data
  });
}

export function getMenuResource(roleId) {
  return fetch({
    url: '/api/role/resource/' + roleId + '/menu',
    method: 'get'
  });
}

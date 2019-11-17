import {asyncRouterMap, constantRouterMap} from '@/router'

const _import = require('router/_import_' + process.env.NODE_ENV)

/**
 * 通过meta.role判断是否与当前用户权限匹配,根据路径匹配
 * @param roles
 * @param route
 */
function hasPermission(menus, route) {
  if (route.path) {
    const menuArr = menus.filter(menu => menu.uri && menu.uri.length > 0 && route.path.indexOf(menu.uri) >= 0)
    if (menuArr && menuArr.length > 0) {
      route.type = menuArr[0].type;
      if (route.type === 'link') {
        route.uri = menuArr[0].component;
      } else if (!route.component && menuArr[0].component) {
        route.component = _import(menuArr[0].component);
      }
      return true;
    }
    return false;
  } else {
    return true;
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param asyncRouterMap
 * @param roles
 */
function filterAsyncRouter(asyncRouterMap, menus) {
  const accessedRouters = asyncRouterMap.filter(route => {
    if (hasPermission(menus, route)) {
      if (route.children && route.children.length) {
        route.children = filterAsyncRouter(route.children, menus)
      }
      return true
    }
    return false
  })
  return accessedRouters
}

/**
 * 设置route缺失的属性
 * @param asyncRouterMap
 * @param menus
 */
function filterAdminRouter(asyncRouterMap, menus) {
  const adminRouter = asyncRouterMap.filter(route => {
    hasPermission(menus, route);
    if (route.children && route.children.length) {
      route.children = filterAdminRouter(route.children, menus)
    }
    return true;
  })
  return adminRouter;
}

function filterEmptyRouter(accessedRouters) {
  const filterRouter = accessedRouters.filter(route => {
    if (route.path) {
      return true;
    } else {
      if (route.children && route.children.length) {
        route.children = filterEmptyRouter(route.children);
        return true;
      } else {
        return false;
      }
    }
  })
  return filterRouter;
}

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
    }
  },
  actions: {
    GenerateRoutes({commit}, data) {
      const roles = data.roles;
      const menus = data.menus;
      return new Promise(resolve => {
        let accessedRouters
        if (roles.indexOf('admin') >= 0) {
          accessedRouters = filterAdminRouter(asyncRouterMap, menus)
        } else {
          accessedRouters = filterAsyncRouter(asyncRouterMap, menus)
          accessedRouters = filterEmptyRouter(accessedRouters);
        }
        // console.log(accessedRouters);
        commit('SET_ROUTERS', accessedRouters)
        resolve()
      })
    }
  }
}

export default permission

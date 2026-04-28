// 前端 roles 控制菜单权限 通过登录后的角色对菜单就行过滤处理
// 如果需要前端 roles 控制菜单权限 请使用此文件代码替换 permission.ts 的内容

import { defineStore } from 'pinia';
import { RouteRecordRaw } from 'vue-router';

import router, { allRoutes } from '@/router';
import { store } from '@/store';

function filterPermissionsRouters(routes: Array<RouteRecordRaw>, permissions: Array<unknown>) {
  const res: Array<RouteRecordRaw> = [];
  const removeRoutes: Array<RouteRecordRaw> = [];
  routes.forEach((route) => {
    const children: Array<RouteRecordRaw> = [];
    route.children?.forEach((childRouter) => {
      // meta.permissionCode是权限控制字段
      const permissionCode = childRouter.meta?.permissionCode;
      if (!permissionCode) {
        children.push(childRouter);
      } else if (permissions.indexOf(permissionCode) !== -1) {
        children.push(childRouter);
      } else {
        removeRoutes.push(childRouter);
      }
    });
    if (children.length > 0) {
      route.children = children;
      res.push(route);
    }
  });
  return { accessedRouters: res, removeRoutes };
}

export const usePermissionStore = defineStore('permission', {
  state: () => ({
    init: false,
    whiteListRouters: ['/login', '/result/maintenance', '/result/404', '/result/403', '/result/500'],
    routers: [],
    removeRoutes: [],
  }),
  actions: {
    async initRoutes(permissions: Array<unknown>) {
      let accessedRouters = [];

      let removeRoutes: Array<RouteRecordRaw> = [];
      const res = filterPermissionsRouters(allRoutes, permissions);
      accessedRouters = res.accessedRouters;
      removeRoutes = res.removeRoutes;

      this.routers = accessedRouters;
      this.removeRoutes = removeRoutes;

      removeRoutes.forEach((item: RouteRecordRaw) => {
        if (router.hasRoute(item.name)) {
          router.removeRoute(item.name);
        }
      });
      this.init = true;
    },
    async restore() {
      this.removeRoutes.forEach((item: RouteRecordRaw) => {
        router.addRoute(item);
      });
    },
  },
});

export function getPermissionStore() {
  return usePermissionStore(store);
}
